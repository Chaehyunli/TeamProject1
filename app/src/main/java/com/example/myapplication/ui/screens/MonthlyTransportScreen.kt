// MonthlyTransportScreen.kt
package com.example.myapplication.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.myapplication.ui.components.BottomNavigationBar
import java.text.DecimalFormat
import java.util.Calendar
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import com.example.myapplication.ui.viewmodel.MonthlyTransportViewModel

@Composable
fun MonthlyTransportScreen(navController: NavHostController, viewModel: MonthlyTransportViewModel) {
    val monthlyCosts by viewModel.monthlyCosts.collectAsState()

    Log.d("MonthlyTransportScreen", "화면에 표시될 월별 교통비: $monthlyCosts")

    Scaffold(
        containerColor = Color.White,
        bottomBar = {
            BottomNavigationBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(62.dp)
                    .border(1.dp, Color.LightGray, RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                selectedItem = 2,
                onItemSelected = { /* Handle item selection */ },
                navController = navController
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                MonthlyExpenseCard(monthlyCosts) // 월별 교통비 데이터 전달
            }
            item {
                RecentExpenseGraph(monthlyCosts)
            }
        }
    }
}

@Composable
fun MonthlyExpenseCard(monthlyCosts: Map<Int, Int>) {
    // 현재 월과 전월 계산
    val calendar = Calendar.getInstance()

    // 월 바뀌어도 이전 달 데이터 잘 넘어가는지 확인하기 위함.
    // calendar.set(Calendar.YEAR, 2024)
    // calendar.set(Calendar.MONTH, Calendar.DECEMBER)

    val currentMonth = calendar.get(Calendar.MONTH) + 1
    val previousMonth = currentMonth - 1

    val currentMonthAmount = monthlyCosts[currentMonth] ?: 0
    val previousMonthAmount = monthlyCosts[previousMonth] ?: 0
    val difference = kotlin.math.abs(previousMonthAmount - currentMonthAmount)
    val isSavings = currentMonthAmount < previousMonthAmount

    // 숫자 형식 포맷터
    val formatter = DecimalFormat("#,###")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(16.dp, shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
            .background(Color(0xFFCBD2DF), shape = RoundedCornerShape(12.dp))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "${currentMonth}월 교통비",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF252F42)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "${formatter.format(currentMonthAmount)} 원",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(8.dp))

        // 전월과 비교한 결과 텍스트
        Text(
            text = buildAnnotatedString {
                append("${previousMonth}월보다 ")
                withStyle(style = SpanStyle(color = if (isSavings) Color(0xFF2563EB) else Color(0xFFFF0000))) {
                    append("${formatter.format(difference)} 원 ")
                    append(if (isSavings) "절약" else "소비")
                }
            },
            fontSize = 16.sp,
            color = Color(0xFF252F42),
            fontWeight = FontWeight.Bold
        )
    }
}

// 꺾은선 일단 보류
@Composable
fun LineChart(dataPoints: List<Int>, months: List<String>) {
    val maxDataValue = dataPoints.maxOrNull() ?: 1
    val minDataValue = dataPoints.minOrNull() ?: 0
    val lineColor = Color(0xFF1F77B4)

    Canvas(modifier = Modifier.fillMaxSize()) {
        val xOffset = 20.dp.toPx() // 양쪽 여백 설정
        val spacePerPoint = (size.width - 2 * xOffset) / (dataPoints.size - 1)
        val verticalPadding = 20.dp.toPx()
        val graphHeight = size.height - 2 * verticalPadding

        // Path to draw the line
        val path = Path().apply {
            dataPoints.forEachIndexed { index, data ->
                val x = xOffset + index * spacePerPoint
                val y = verticalPadding + graphHeight * (1 - (data - minDataValue) / (maxDataValue - minDataValue).toFloat())
                if (index == 0) moveTo(x, y) else lineTo(x, y)
            }
        }

        drawPath(
            path = path,
            color = lineColor,
            style = Stroke(width = 2.dp.toPx())
        )

        // Draw month label at each data point
        dataPoints.forEachIndexed { index, data ->
            val x = xOffset + index * spacePerPoint
            val y = verticalPadding + graphHeight * (1 - (data - minDataValue) / (maxDataValue - minDataValue).toFloat())

            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    months[index],
                    x,
                    y - 10.dp.toPx(),
                    android.graphics.Paint().apply {
                        color = android.graphics.Color.BLACK
                        textSize = 24f
                        textAlign = android.graphics.Paint.Align.CENTER
                    }
                )
            }
        }
    }
}

@Composable
fun RecentExpenseGraph(monthlyCosts: Map<Int, Int>) {
    val sortedCosts = monthlyCosts.toSortedMap() // 월별로 정렬
    val dataPoints = sortedCosts.values.toList()
    val months = sortedCosts.keys.map { "${it}월" }

    val maxDataValue = dataPoints.maxOrNull() ?: 1
    val formatter = DecimalFormat("#,###")

    // 막대 색상 리스트
    val colors = listOf(Color(0xFF5C7780), Color(0xFF6CAAE8), Color(0xFF252F42), Color(0xFF366F98))

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(16.dp, shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
            .background(Color(0xFFCBD2DF), shape = RoundedCornerShape(12.dp))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "최근 교통비 내역",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF252F42),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, shape = RoundedCornerShape(8.dp))
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                dataPoints.forEachIndexed { index, value ->
                    val maxBarHeight = 120.dp
                    val barHeightRatio = value.toFloat() / maxDataValue
                    val barHeight = maxBarHeight * barHeightRatio
                    val barColor = colors[index % colors.size] // 색상 리스트를 순환

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    ) {
                        Text(
                            text = "${formatter.format(value)}원",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF252F42),
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        Canvas(
                            modifier = Modifier
                                .width(30.dp)
                                .height(barHeight)
                        ) {
                            drawRoundRect(
                                color = barColor,
                                size = size,
                                cornerRadius = androidx.compose.ui.geometry.CornerRadius(8.dp.toPx())
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = months[index],
                            fontSize = 12.sp,
                            color = Color(0xFF252F42)
                        )
                    }
                }
            }
        }
    }
}

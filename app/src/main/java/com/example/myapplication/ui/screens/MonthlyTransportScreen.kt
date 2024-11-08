// MonthlyTransportScreen.kt
package com.example.myapplication.ui.screens

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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import androidx.compose.foundation.Canvas
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.draw.shadow
import kotlin.math.log10
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.drawText

@Composable
fun MonthlyTransportScreen(navController: NavHostController) {
    var selectedItem by remember { mutableStateOf(2) }

    Scaffold(
        containerColor = Color.White,
        bottomBar = {
            BottomNavigationBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(62.dp),
                selectedItem = selectedItem,
                onItemSelected = { selectedItem = it },
                navController = navController
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                //.padding(16.dp),
                .padding(horizontal = 16.dp, vertical = 8.dp), // 추가 여백을 조정,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                MonthlyExpenseCard() // 현재 월을 전달
            }
            item {
                RecentExpenseGraph()
            }
        }
    }
}

@Composable
fun MonthlyExpenseCard() {
    // 현재 월과 전월 계산
    val calendar = Calendar.getInstance()
    val currentMonth = SimpleDateFormat("M", Locale.getDefault()).format(calendar.time) // 현재 월

    calendar.add(Calendar.MONTH, -1) // 한 달 전으로 설정
    val previousMonth = SimpleDateFormat("M", Locale.getDefault()).format(calendar.time) // 전월

    // 예시 교통비 금액 설정 (실제로는 데이터에서 받아와야 함)
    val previousMonthAmount = 62000 // 전월 교통비 예시 값
    val currentMonthAmount = 70000 // 현재 월 교통비 예시 값
    val difference = kotlin.math.abs(previousMonthAmount - currentMonthAmount) // 차액 계산
    val isSavings = currentMonthAmount < previousMonthAmount // 전달보다 이번달이 덜 썼으면 true, 더 썼으면 false

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
            text = "${formatter.format(currentMonthAmount)} 원", // 콤마 형식 적용
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

        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(Color.White, shape = RoundedCornerShape(8.dp))
        ) {
            LineChart(dataPoints = listOf(50000, 62000, 70000, 62000, 70000), months = listOf("7월", "8월", "9월", "10월", "11월"))
        }
    }
}

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
fun RecentExpenseGraph() {
    // 임의의 데이터 예시 (단위: 원)
    val data = listOf(1000, 50000, 200000, 100000)
    val maxDataValue = data.maxOrNull() ?: 0 // 데이터 중 최대값 (그래프 높이 기준)
    val formatter = DecimalFormat("#,###") // 숫자 형식 포맷터

    // 현재 월과 이전 월 구하기
    val calendar = Calendar.getInstance()
    val dateFormat = SimpleDateFormat("M", Locale.getDefault())
    val months = List(data.size) { index ->
        calendar.add(Calendar.MONTH, -index)
        dateFormat.format(calendar.time).toInt()
    }.reversed()

    val colors = listOf(Color(0xFF5C7780), Color(0xFF6CAAE8), Color(0xFF252F42), Color(0xFF366F98))

    // 바깥쪽 배경
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(16.dp, shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
            .background(Color(0xFFCBD2DF), shape = RoundedCornerShape(12.dp))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 제목 영역 - 바깥 배경 색상 사용
        Text(
            text = "최근 교통비 내역",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF252F42),
            modifier = Modifier.padding(bottom = 8.dp) // 제목과 그래프 간격 조정
        )

        // 안쪽 흰색 배경의 그래프 영역
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, shape = RoundedCornerShape(8.dp))
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // 그래프 캔버스
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                data.forEachIndexed { index, value ->
                    val maxBarHeight = 120.dp
                    val barHeightRatio = log10(value.toFloat() + 10) / log10(maxDataValue.toFloat() + 10)
                    val barHeight = (maxBarHeight * barHeightRatio).coerceAtMost(maxBarHeight)
                    val barColor = colors[index % colors.size]

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
                            text = "${months[index]}월",
                            fontSize = 12.sp,
                            color = Color(0xFF252F42),
                            modifier = Modifier.padding(top = 1.dp)
                        )
                    }
                }
            }
        }
    }
}

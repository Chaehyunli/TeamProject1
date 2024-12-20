// MonthlyTransportScreen.kt
package com.example.myapplication.ui.screens

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.myapplication.ui.components.BottomNavigationBar
import com.example.myapplication.ui.viewmodel.MonthlyTransportViewModel
import java.text.DecimalFormat
import java.util.*

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
                    .border(1.dp, Color(0xFF808590), RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                selectedItem = 2,
                onItemSelected = { /* Handle item selection */ },
                navController = navController
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp), // 좌우 패딩
            contentPadding = PaddingValues(
                top = innerPadding.calculateTopPadding() + 16.dp, // innerPadding의 top 값 + 추가 간격
                bottom = innerPadding.calculateBottomPadding() + 16.dp // innerPadding의 bottom 값 + 추가 간격
            ),
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

    val currentMonth = calendar.get(Calendar.MONTH) + 1 //현재 월
    val previousMonth = currentMonth - 1 // 전월

    val currentMonthAmount = monthlyCosts[currentMonth] ?: 0 // 현재 월의 교통비 가져오기
    val previousMonthAmount = monthlyCosts[previousMonth] ?: 0 // 전월의 교통비 가져오기
    val difference = kotlin.math.abs(previousMonthAmount - currentMonthAmount) // 전월 교통비와 현재 월 교통비의 차이를 계산
    val isSavings = currentMonthAmount < previousMonthAmount

    // 숫자 형식 포맷터
    val formatter = DecimalFormat("#,###")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(0.5.dp,Color(0xFF252f42),shape = RoundedCornerShape(10.dp))
            .shadow(16.dp, shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
            .background(Color(0xFFCBD2DF), shape = RoundedCornerShape(10.dp))
            .padding(16.dp)
    ) {
        Text(
            text = " ${currentMonth}월 교통비",
            fontSize = 14.sp,
            color = Color(0xFF252F42)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = buildAnnotatedString {
                append("${formatter.format(currentMonthAmount)} ")
                withStyle(style = SpanStyle(fontSize = 16.sp, fontWeight = FontWeight.Normal, color = Color(0xFF252f42))){
                    append("원")
                }
            },
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF252F42)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(0.5.dp,Color(0xFF252f42),shape = RoundedCornerShape(10.dp))
                .background(Color.White, shape = RoundedCornerShape(10.dp))
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // 전월과 비교한 결과 텍스트
                Text(
                    text = buildAnnotatedString {
                        append("${previousMonth}월보다 \n")
                        withStyle(
                            style = SpanStyle(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSavings) Color(0xFF2563EB) else Color(0xFFFF0000)
                            )
                        ) {
                            append("${formatter.format(difference)}원 ")
                            append(if (isSavings) "절약" else "소비")
                        }
                    },
                    fontSize = 14.sp,
                    color = Color(0xFF252F42)
                )
                Image(
                    painter = painterResource(id = if (isSavings) com.example.myapplication.R.drawable.save else com.example.myapplication.R.drawable.unsave),
                    contentDescription = "그래프 이미지",
                    modifier = Modifier.width(130.dp) // 이미지 크기 설정
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
            .border(0.5.dp,Color(0xFF252f42),shape = RoundedCornerShape(10.dp))
            .shadow(16.dp, shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
            .background(Color(0xFFCBD2DF), shape = RoundedCornerShape(10.dp))
            .padding(16.dp)
    ) {
        Text(
            text = "최근 교통요금 내역",
            fontSize = 14.sp,
            color = Color(0xFF252F42),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(0.5.dp,Color(0xFF252f42),shape = RoundedCornerShape(10.dp))
                .background(Color.White, shape = RoundedCornerShape(10.dp))
                .padding(16.dp)
        ) {
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
                                fontSize = 10.sp,
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
                                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(3.dp.toPx())
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
}

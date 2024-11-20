package com.example.myapplication.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.RouteFinder
import java.text.NumberFormat
import java.util.*

@Composable
fun RouteResultItem(
    route: RouteFinder.RouteInfo,
    navController: NavController,
) {
    val timeText = TimeTextFormatter(time = route.time)
    val formattedCost = NumberFormat.getNumberInstance(Locale.getDefault()).format(route.cost)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFffffff))
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 4

                .dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 시간, 환승, 요금 정보
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = timeText,
                    fontSize = 22.sp,
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "환승 ${route.transfers}회 | 요금 ${formattedCost}원",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            // 막대 바 표시
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val lineNumbers = route.lineNumbers
                lineNumbers.forEachIndexed { index, line ->
                    val isLastSegment = index == lineNumbers.size - 1

                    // 현재 노선의 막대
                    Box(
                        modifier = Modifier
                            .weight(if (isLastSegment) 1f else 1f) // 마지막 막대도 일정한 비율로 출력
                            .height(6.dp)
                            .background(
                                color = getLineColor(line) // LineColors 사용
                            )
                    )

                    // 환승 지점 마름모 표시
                    if (!isLastSegment && lineNumbers[index] != lineNumbers[index + 1]) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .background(
                                    color = Color(0xFFFFFBEB),
                                    shape = RoundedCornerShape(2.dp) // 마름모 모양
                                )
                                .rotate(45F) // 사각형을 회전하여 마름모로 만듦
                                .border(
                                    width = 1.5.dp,
                                    color = Color(0xFF252f42),
                                    shape = RoundedCornerShape(4.dp)
                                )
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                }
            }

            // 경로 정보
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // 출발 정보
                RouteSegmentWithLineIcon(
                    lineNumber = route.lineNumbers.firstOrNull() ?: -1,
                    station = "${route.path.firstOrNull() ?: "알 수 없음"}역",
                    nextStation = " | ${route.path.getOrNull(1) ?: "알 수 없음"}역 방면",
                    showVerticalLine = true // 세로선 표시
                )

                // 환승 정보
                route.path.drop(1).zipWithNext().forEachIndexed { index, (current, next) ->
                    // 노선이 변경된 경우에만 환승역으로 표시
                    if (route.lineNumbers.getOrNull(index) != route.lineNumbers.getOrNull(index + 1)) {
                        RouteSegmentWithLineIcon(
                            lineNumber = route.lineNumbers.getOrNull(index + 1) ?: -1,
                            station = current.toString() + "역",
                            nextStation = " | " + next.toString() + "역 방면",
                            showVerticalLine = true // 세로선 표시
                        )
                    }
                }

                // 도착 정보: 작은 원 추가
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .size(15.dp)
                            .background(
                                color = getLineColor(route.lineNumbers.lastOrNull() ?: -1), // LineColors 사용
                                shape = RoundedCornerShape(50) // 원형
                            )
                            .border(
                                width = 2.dp,
                                color = Color(0xffffffff),
                                shape = RoundedCornerShape(50) // 라운드 코너를 50%로 설정해 원 모양으로
                            )
                    )
                    Text(
                        modifier = Modifier.padding(start = 8.dp),
                        text = "${route.path.lastOrNull() ?: "알 수 없음"}역",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF252F42)
                    )
                }
            }
        }
    }
}

@Composable
fun RouteSegmentWithLineIcon(lineNumber: Int, station: String, nextStation: String, showVerticalLine: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top // 아이콘과 텍스트를 정렬
    ) {
        // (정사각형 + 세로선)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally, // 정사각형과 세로선을 가운데 정렬
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .border(
                        width = 2.dp,
                        color = getLineColor(lineNumber), // LineColors 사용
                        shape = RoundedCornerShape(4.dp)
                    )
                    .size(26.dp)
                    .background(
                        color = Color(0xFFFFFBEB),
                        shape = RoundedCornerShape(4.dp) // 정사각형

                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (lineNumber > 0) "$lineNumber" else "",
                    color = getLineColor(lineNumber),
                    fontSize = 17.sp,
                    lineHeight = 20.sp,
                    textAlign = TextAlign.Center,
                )
            }
            if (showVerticalLine) {
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .width(0.5.dp)
                        .height(10.dp)
                        .background(Color.Gray) // 세로선
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp)) // 아이콘과 텍스트 간격

        // (역 텍스트 + 방면 텍스트)
        Row(
            verticalAlignment = Alignment.Top, // 텍스트 정렬
        ) {
            Text(
                text = station,
                fontSize = 16.sp,
                color = Color(0xFF252F42)
            )
            Text(
                text = nextStation,
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }
}




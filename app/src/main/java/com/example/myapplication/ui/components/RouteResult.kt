//RouteResult.kt
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

// RouteResultItem: 경로 결과 항목을 화면에 표시하는 컴포저블 함수
@Composable
fun RouteResultItem(
    route: RouteFinder.RouteInfo, // 경로 정보
    navController: NavController, // 네비게이션을 위한 컨트롤러
) {
    // 경로 시간 포맷팅
    val timeText = TimeTextFormatter(time = route.time) // 경로 시간 텍스트
    val formattedCost = NumberFormat.getNumberInstance(Locale.getDefault()).format(route.cost) // 경로 요금 포맷팅

    // 카드 형태의 UI 요소로 경로 정보 표시
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFffffff))
    ) {
        // 경로 기준 텍스트
        Text(
            modifier = Modifier
                .padding(start = 8.dp),
            text = route.criteria.toString(),
            fontSize = 12.sp,
            color = Color.Blue
        )

        // 경로의 상세 정보 출력
        Column(
            modifier = Modifier.padding(horizontal = 4.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 시간, 환승, 요금 정보 표시
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = timeText, // 경로 시간
                    fontSize = 20.sp,
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "환승 ${route.transfers}회 | 요금 ${formattedCost}원", // 환승 횟수 및 요금 정보
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            // 경로 정보의 막대 및 환승 지점 표시
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val lineNumbers = route.lineNumbers // 노선 번호 리스트
                lineNumbers.forEachIndexed { index, line ->
                    val isLastSegment = index == lineNumbers.size - 1

                    // 각 노선에 대한 막대 출력
                    Box(
                        modifier = Modifier
                            .weight(if (isLastSegment) 1f else 1f) // 막대의 비율
                            .height(6.dp)
                            .background(
                                color = getLineColor(line) // 노선에 맞는 색상 적용
                            )
                    )

                    // 환승 지점 표시 (마름모 형태로)
                    if (!isLastSegment && lineNumbers[index] != lineNumbers[index + 1]) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .background(
                                    color = Color(0xFFFFFBEB),
                                    shape = RoundedCornerShape(2.dp)
                                )
                                .rotate(45F)
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

            // 경로 정보 (출발, 환승, 도착)
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // 출발역 및 방면 정보
                RouteSegmentWithLineIcon(
                    lineNumber = route.lineNumbers.firstOrNull() ?: -1,
                    station = "${route.path.firstOrNull() ?: "알 수 없음"}역",
                    nextStation = " | ${route.path.getOrNull(1) ?: "알 수 없음"}역 방면",
                    showVerticalLine = true // 세로선 표시 여부
                )

                // 환승 정보 (환승역마다 표시)
                route.path.drop(1).zipWithNext().forEachIndexed { index, (current, next) ->
                    // 노선 변경 시 환승역 표시
                    if (route.lineNumbers.getOrNull(index) != route.lineNumbers.getOrNull(index + 1)) {
                        RouteSegmentWithLineIcon(
                            lineNumber = route.lineNumbers.getOrNull(index + 1) ?: -1,
                            station = current.toString() + "역",
                            nextStation = " | " + next.toString() + "역 방면",
                            showVerticalLine = true // 세로선 표시 여부
                        )
                    }
                }

                // 도착역 정보 표시 (작은 원 모양)
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
                                color = getLineColor(route.lineNumbers.lastOrNull() ?: -1), // 도착 노선 색상
                                shape = RoundedCornerShape(50)
                            )
                            .border(
                                width = 2.dp,
                                color = Color(0xffffffff),
                                shape = RoundedCornerShape(50)
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

// RouteSegmentWithLineIcon: 각 경로 구간을 표시하는 함수 (출발, 환승, 도착)
@Composable
fun RouteSegmentWithLineIcon(lineNumber: Int, station: String, nextStation: String, showVerticalLine: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        // (호선번호 아이콘 + 세로선)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally, // 정사각형과 세로선을 가운데 정렬
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .border(
                        width = 2.dp,
                        color = getLineColor(lineNumber),
                        shape = RoundedCornerShape(4.dp)
                    )
                    .size(26.dp)
                    .background(
                        color = Color(0xFFFFFBEB),
                        shape = RoundedCornerShape(4.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (lineNumber > 0) "$lineNumber" else "",
                    color = getLineColor(lineNumber), // 노선 색상 설정
                    fontSize = 17.sp,
                    lineHeight = 20.sp,
                    textAlign = TextAlign.Center,
                )
            }
            // 세로선 표시 (필요한 경우)
            if (showVerticalLine) {
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .width(0.5.dp)
                        .height(10.dp)
                        .background(Color.Gray)
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        // (역 텍스트 + 방면 텍스트)
        Row(
            verticalAlignment = Alignment.Top,
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

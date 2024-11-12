package com.example.myapplication.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteDetailScreen(
    path: List<String>,
    transferStations: List<String>,
    lineNumbers: List<Int>,
    time: Int,
    distance: Int,
    transfers: Int,
    cost: Int,
    onBack: () -> Unit
) {
    // 시간, 분, 초 계산
    val hours = time / 3600
    val minutes = (time % 3600) / 60
    val seconds = (time % 60).toString().padStart(2, '0')
    val formattedCost = NumberFormat.getNumberInstance(Locale.getDefault()).format(cost)

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(
                            text = "${if (hours > 0) "${hours}시간 " else ""}${if (minutes > 0 || hours > 0) "${minutes}분 " else ""}${seconds}초",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "환승 ${transfers}회 | 요금 ${formattedCost}원",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "뒤로가기")
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color.White
                ),
                modifier = Modifier.shadow(4.dp)
            )
        },
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    var currentLine = lineNumbers[0]
                    var segmentStartIndex = 0

                    path.forEachIndexed { index, station ->
                        val isTransferStation = transferStations.contains(station)
                        val isEndStation = index == path.size - 1

                        if (isEndStation || lineNumbers[index] != currentLine) {
                            val segmentLength = index - segmentStartIndex + 1

                            val annotatedText = buildAnnotatedString {
                                append("${currentLine}호선 ${path[segmentStartIndex]}역 ")
                                withStyle(style = SpanStyle(fontSize = 14.sp, color = Color.Gray)) {
                                    append("| ${path[segmentStartIndex + 1]}역 방면")
                                }
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                Text(
                                    text = annotatedText,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = getLineColor(currentLine)
                                )
                            }

                            val minBarHeight = 100.dp
                            val maxBarHeight = 700.dp
                            val perStationHeight = 30.dp
                            val calculatedBarHeight = (segmentLength * perStationHeight.value).dp.coerceIn(minBarHeight, maxBarHeight)

                            Row(
                                verticalAlignment = Alignment.Top,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                Spacer(modifier = Modifier.width(16.dp))

                                Box(
                                    modifier = Modifier
                                        .width(8.dp)
                                        .height(calculatedBarHeight)
                                        .background(color = getLineColor(currentLine), shape = RoundedCornerShape(4.dp))
                                )

                                Spacer(modifier = Modifier.width(8.dp))

                                Column(
                                    modifier = Modifier.height(calculatedBarHeight),
                                    verticalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    for (i in segmentStartIndex + 1 until index) {
                                        Text(
                                            text = "${path[i]}역",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Normal,
                                            color = Color.Gray
                                        )
                                    }

                                    if (isTransferStation || isEndStation) {
                                        Text(
                                            text = "${station}역 하차",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = getLineColor(currentLine)
                                        )
                                    }
                                }
                            }
                            segmentStartIndex = index
                            currentLine = lineNumbers.getOrNull(index) ?: -1
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                        .shadow(8.dp, shape = RoundedCornerShape(16.dp))
                        .background(Color(0xFF242F42), shape = RoundedCornerShape(16.dp))
                        .border(2.dp, Color(0xFFCBD2DF), RoundedCornerShape(16.dp)) // 테두리 설정
                        .clickable { /* 월별 교통비 데이터 보내기 */ }
                        .wrapContentSize() // 내부 콘텐츠 크기에 맞춤
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp) // 패딩 조정
                    ) {
                        Text(
                            text = "이 경로로 선택하기",
                            color = Color(0xFFCBD2DF),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.width(4.dp)) // 텍스트와 아이콘 사이 간격 추가
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Check Circle Icon",
                            tint = Color(0xFFCBD2DF), // 아이콘 색상 설정
                            modifier = Modifier.size(16.dp) // 아이콘 크기를 텍스트 크기에 맞춤
                        )
                    }
                }
            }
        }
    )
}

// 각 호선에 따른 색상 반환 함수
fun getLineColor(lineNumber: Int): Color {
    return when (lineNumber) {
        1 -> Color(0xFF00B050)
        2 -> Color(0xFF002060)
        3 -> Color(0xFF953735)
        4 -> Color(0xFFFF0000)
        5 -> Color(0xFF4A7EBB)
        6 -> Color(0xFFFFC514)
        7 -> Color(0xFF92D050)
        8 -> Color(0xFF00B0F0)
        9 -> Color(0xFF7030A0)
        else -> Color.Gray
    }
}

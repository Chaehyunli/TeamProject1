package com.example.myapplication.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.myapplication.ui.components.ConfirmationDialog
import com.example.myapplication.ui.components.TimeTextFormatter
import com.example.myapplication.ui.components.getLineColor
import com.example.myapplication.ui.viewmodel.RouteDetailViewModel
import java.text.NumberFormat
import java.util.*

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
    onBack: () -> Unit,
    viewModel: RouteDetailViewModel
) {
    var isButtonVisible by remember { mutableStateOf(true) } // 버튼 가시성 상태 추가
    var showDialog by remember { mutableStateOf(false) } // 다이얼로그 표시 상태

    fun onConfirm() {
        viewModel.addCost(cost) // 선택한 요금을 기존 교통비에 더하기
        isButtonVisible = false // 버튼을 숨김
        showDialog = false // 다이얼로그 닫기
    }

    fun onCancel() {
        showDialog = false // 다이얼로그 닫기
    }

    val timeText = TimeTextFormatter(time = time)
    val formattedCost = NumberFormat.getNumberInstance(Locale.getDefault()).format(cost)

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.Bottom,
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(
                            text = timeText,
                            fontSize = 18.sp,
                            color = Color(0xFF252f42)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "환승 ${transfers}회 | 요금 ${formattedCost}원",
                            fontSize = 12.sp,
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
                modifier = Modifier.border(1.dp, Color.LightGray)
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
                                withStyle(style = SpanStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal, color = Color.Gray)) {
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

                if (isButtonVisible) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(16.dp)
                            .shadow(8.dp, shape = RoundedCornerShape(16.dp))
                            .background(Color(0xFF242F42), shape = RoundedCornerShape(16.dp))
                            .border(2.dp, Color(0xFFCBD2DF), RoundedCornerShape(16.dp))
                            .clickable { showDialog = true }
                            .wrapContentSize()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = "이 경로로 선택하기",
                                color = Color(0xFFCBD2DF),
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Check Circle Icon",
                                tint = Color(0xFFCBD2DF),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }

            if (showDialog) {
                ConfirmationDialog(
                    message = "이 경로를 선택하시겠습니까?",
                    onConfirm = { onConfirm() }, // 확인 클릭 시 동작
                    onCancel = { onCancel() }   // 취소 클릭 시 동작
                )
            }
        }
    )
}
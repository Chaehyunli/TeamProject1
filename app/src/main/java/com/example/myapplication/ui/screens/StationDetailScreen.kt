// StationDetailScreen.kt
package com.example.myapplication.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import com.example.myapplication.ui.components.StationDetailDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.shadow
import com.example.myapplication.Edge
import com.example.myapplication.SubwayGraphInstance
import kotlin.math.roundToInt
import kotlin.math.max
import kotlin.math.min

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StationDetailScreen(stationName: String, onBack: () -> Unit) {
    val stationNumber = stationName.toIntOrNull()

    // SubwayGraphInstance에서 해당 역 번호에 대한 정보를 개별적으로 가져오기
    val stationData = stationNumber?.let { SubwayGraphInstance.subwayGraph.stations[it] }
    val lineNumbers = stationData?.lineNumbers
    val neighbors = stationData?.neighbors
    val hasTransfer = lineNumbers?.size?.let { it > 1 } ?: false

    // 특정 호선 번호일 경우 해당 호선의 연결된 순서대로 전체 역 목록 가져오기
    val isLineNumber = stationNumber in 1..9
    val orderedStationsInLine = if (isLineNumber) {
        SubwayGraphInstance.getStationsByLineInOrder(stationNumber!!)
    } else {
        null
    }

    // stationNameDisplay 구성
    val stationNameDisplay = if (isLineNumber) {
        "${stationNumber}호선 전체 역 목록"
    } else if (lineNumbers != null && lineNumbers.isNotEmpty()) {
        "${lineNumbers.first()}호선 ${stationNumber}역"
    } else {
        "역 정보 없음"
    }

    // transferInfo 구성
    val transferInfo = if (isLineNumber) {
        "환승역 포함한 ${stationNumber}호선 연결된 순서의 전체 역 목록입니다."
    } else if (hasTransfer) {
        "환승 가능한 역입니다."
    } else {
        "환승할 수 없는 역입니다."
    }

    // 초기 확대 및 위치 설정
    var scale by remember { mutableStateOf(2f) }
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    val screenWidth = 1000f
    val screenHeight = 1000f
    val imageWidth = 860f
    val imageHeight = 1000f

    val maxOffsetX = (imageWidth * scale - screenWidth) / 2
    val maxOffsetY = (imageHeight * scale - screenHeight) / 2

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stationNameDisplay, fontSize = 18.sp, color = Color(0xFF252f42)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "뒤로가기")
                    }
                },
                modifier = Modifier.shadow(8.dp),
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.White),
            )
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFffffff))
                    .padding(paddingValues),
                contentAlignment = Alignment.BottomCenter
            ) {
                // 특정 역의 상세 정보를 볼 때만 노선도 배경 표시
                if (!isLineNumber) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .pointerInput(Unit) {
                                detectTransformGestures { _, pan, _, _ ->
                                    offsetX = (offsetX + pan.x).coerceIn(-maxOffsetX, maxOffsetX)
                                    offsetY = (offsetY + pan.y).coerceIn(-maxOffsetY, maxOffsetY)
                                }
                            }
                            .graphicsLayer(
                                scaleX = scale,
                                scaleY = scale,
                                translationX = offsetX,
                                translationY = offsetY,
                            )
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.map_image),
                            contentDescription = "지하철 노선도",
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }

                // 하단 정보 카드 (화면 하단에 고정)
                if (isLineNumber && orderedStationsInLine != null) {
                    // 특정 호선의 연결된 순서의 전체 역 목록을 스크롤 가능하게 표시
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .background(Color.White, shape = RoundedCornerShape(12.dp))
                    ) {
                        items(orderedStationsInLine) { station ->
                            Text(
                                text = "$station 번 역",
                                fontSize = 16.sp,
                                modifier = Modifier
                                    .padding(8.dp)
                                    .fillMaxWidth()
                            )
                        }
                    }
                } else {
                    // 선택된 역의 상세 정보 출력
                    StationDetailDialog(
                        stationName = stationNameDisplay,
                        transferInfo = transferInfo,
                        transferDetailInfo = neighbors?.joinToString("\n") { edge ->
                            "${edge.destination}역 방면 | ${if (lineNumbers?.contains(edge.line) == true) "환승 가능" else "환승 불가"}"
                        } ?: "해당 역에 대한 추가 정보가 없습니다."
                    )
                }
            }
        }
    )
}






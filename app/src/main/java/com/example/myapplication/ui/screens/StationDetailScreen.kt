// StationDetailScreen.kt
package com.example.myapplication.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
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
    // stationName을 Int로 변환하여 SubwayGraphInstance에서 역 번호 가져오기
    val stationNumber = stationName.toIntOrNull()

    // SubwayGraphInstance에서 해당 역 번호에 대한 정보를 개별적으로 가져오기
    val stationData = stationNumber?.let { SubwayGraphInstance.subwayGraph.stations[it] }
    val lineNumbers = stationData?.lineNumbers
    val neighbors = stationData?.neighbors
    val hasTransfer = lineNumbers?.size?.let { it > 1 } ?: false

    // stationNameDisplay 구성
    val stationNameDisplay = if (lineNumbers != null && lineNumbers.isNotEmpty()) {
        "${lineNumbers.first()}호선 ${stationNumber}역" // 예: "9호선 903역"
    } else {
        "역 정보 없음"
    }

    // transferInfo 구성
    val transferInfo = if (hasTransfer) {
        "환승 가능한 역입니다."
    } else {
        "환승할 수 없는 역입니다."
    }

    // transferDetailInfo 구성
    val transferDetailInfo = neighbors?.joinToString("\n") { edge ->
        "${edge.destination}역 방면 | ${if (lineNumbers?.contains(edge.line) == true) "환승 가능" else "환승 불가"}"
    } ?: "해당 역에 대한 추가 정보가 없습니다."

    // 초기 확대 및 위치 설정
    var scale by remember { mutableStateOf(2f) } // 초기 scale 값을 2f로 설정하여 하얀 배경이 보이지 않도록 함
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    // 화면 크기와 이미지 크기에 맞춰 스크롤 한계를 설정
    val screenWidth = 1000f // 예시 화면 너비 (필요 시 실제 화면 너비를 측정해 동적 설정 가능)
    val screenHeight = 1000f // 예시 화면 높이
    val imageWidth = 860f  // 이미지 너비
    val imageHeight = 1000f // 이미지 높이

    // 이동 제한값 계산
    val maxOffsetX = (imageWidth * scale - screenWidth) / 2
    val maxOffsetY = (imageHeight * scale - screenHeight) / 2

    Scaffold(
        // 상단 역 설명 바
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
        // 노선도 이미지
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFffffff))
                    .padding(paddingValues),
                contentAlignment = Alignment.BottomCenter
            ) {
                // 이미지 스크롤 처리 (확대/축소 비활성화)
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(Unit) {
                            detectTransformGestures { _, pan, _, _ ->
                                // pan 값으로만 이동 제한
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

                // 하단 정보 카드 (화면 하단에 고정)
                StationDetailDialog(
                    stationName = stationNameDisplay,
                    transferInfo = transferInfo,
                    transferDetailInfo = transferDetailInfo
                )
            }
        }
    )
}




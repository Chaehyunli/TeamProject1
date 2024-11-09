// StationDetailScreen.kt
package com.example.myapplication.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt
import kotlin.math.max
import kotlin.math.min

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StationDetailScreen(stationName: String, onBack: () -> Unit) {
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
                title = { Text(text = stationName, fontSize = 18.sp, color = Color(0xFF252f42)) },
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
                    stationName = "9호선 903역",
                    transferInfo = "환승할 수 없는 역입니다.",
                    transferDetailInfo = "119역 방면 | 환승 가능\n702역 방면 | 환승 가능\n(종착역이면 종착역입니다.)"
                )
            }
        }
    )
}

// SubwayMapScreen.kt
package com.example.myapplication.ui.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.components.loadStationCoordinates
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.layout.onGloballyPositioned

@Composable
fun SubwayMapScreen() {
    val context = LocalContext.current
    val stationCoordinates = remember { loadStationCoordinates(context) }

    // 지도의 고정된 크기 (1000x700)
    val mapWidth = 1000f
    val mapHeight = 700f

    // 확대 및 위치 변수
    var scale by remember { mutableStateOf(1.5f) } // 초기 확대 상태 설정
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    // 화면 중앙 좌표
    var centerX by remember { mutableStateOf(0f) }
    var centerY by remember { mutableStateOf(0f) }

    // 밀도를 가져와서 Float 값을 Dp로 변환할 수 있도록 준비
    val density = LocalDensity.current

    if (stationCoordinates.isEmpty()) {
        Text("데이터를 로드할 수 없습니다.")
        return
    }

    // 핸드폰 화면 크기를 얻기 위해 BoxWithConstraints 사용
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .onGloballyPositioned { coordinates ->
                // 화면 중앙 좌표를 계산하여 설정
                val layoutWidth = coordinates.size.width.toFloat()
                val layoutHeight = coordinates.size.height.toFloat()
                centerX = layoutWidth / 2
                centerY = layoutHeight / 2
            }
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, _ ->
                    // 확대/축소 비율 조정
                    scale = (scale * zoom).coerceIn(0.5f, 3f)
                    // 드래그 이동
                    offsetX += pan.x
                    offsetY += pan.y
                }
            }
    ) {
        // 지도 전체를 포함하는 Box
        Box(
            modifier = Modifier
                .size(with(density) { (mapWidth * scale).toDp() }, with(density) { (mapHeight * scale).toDp() })
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    translationX = offsetX,
                    translationY = offsetY
                )
        ) {
            // 각 역을 표시하는 Box
            stationCoordinates.forEach { (stationId, position) ->
                val scaledX = position.x * scale
                val scaledY = position.y * scale
                val circleRadius = (8 * scale).dp // 기본 크기를 더 작은 8로 설정
                val textSize = (7 * scale).sp

                // Dp 변환을 위한 밀도 사용
                val xOffset = with(density) { scaledX.toDp() }
                val yOffset = with(density) { scaledY.toDp() }

                // 역 표시용 Box로 대체하여 버튼처럼 동작하게 설정
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .offset(x = xOffset, y = yOffset)
                        .size(circleRadius * 2) // 축소된 기본 원 크기
                        .border(1.dp, Color.Black, CircleShape) // 검은색 테두리
                        .background(Color(0xFFD9EAF7), CircleShape) // 연한 파란색 내부
                        .clickable {
                            // 클릭 시 화면을 1.7배 확대하고, 선택한 역을 화면 중앙에 위치시키기 위해 offset 계산
                            scale = 1.7f
                            val targetX = position.x * scale
                            val targetY = position.y * scale
                            offsetX = centerX - targetX
                            offsetY = centerY - targetY
                        }
                ) {
                    Text(
                        text = stationId.toString(),
                        fontSize = textSize,
                        color = Color.Black
                    )
                }
            }
        }
    }
}

















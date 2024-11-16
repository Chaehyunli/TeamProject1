// SubwayMapScreen.kt
package com.example.myapplication.ui.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.foundation.gestures.detectDragGestures

@Composable
fun SubwayMapScreen() {
    val context = LocalContext.current
    val stationCoordinates = remember { loadStationCoordinates(context) }

    // 확대 비율 고정
    val scale = 1.5f

    // 지도 오프셋 변수
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    // 밀도를 가져와서 Float 값을 Dp로 변환할 준비
    val density = LocalDensity.current

    if (stationCoordinates.isEmpty()) {
        Text("데이터를 로드할 수 없습니다.")
        return
    }

    // 핸드폰 화면 크기에서 중앙 좌표 계산
    var screenCenterX by remember { mutableStateOf(0f) }
    var screenCenterY by remember { mutableStateOf(0f) }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume() // 드래그 이벤트 소비
                    offsetX += dragAmount.x
                    offsetY += dragAmount.y
                }
            }
    ) {
        val screenWidth = constraints.maxWidth.toFloat()
        val screenHeight = constraints.maxHeight.toFloat()

        // 중앙 좌표 계산
        screenCenterX = screenWidth / 2
        screenCenterY = screenHeight / 2

        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(
                    translationX = offsetX,
                    translationY = offsetY
                )
        ) {
            // 각 역을 표시하는 Box
            stationCoordinates.forEach { (stationId, position) ->
                val scaledX = position.x * scale
                val scaledY = position.y * scale
                val circleRadius = (8 * scale).dp
                val textSize = (7 * scale).sp

                // Dp 변환을 위한 밀도 사용
                val xOffset = with(density) { scaledX.toDp() }
                val yOffset = with(density) { scaledY.toDp() }

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .offset(x = xOffset, y = yOffset)
                        .size(circleRadius * 2)
                        .border(1.dp, Color.Black, CircleShape)
                        .background(Color(0xFFD9EAF7), CircleShape)
                        .clickable {
                            // 정확한 오프셋 계산
                            offsetX = screenCenterX - scaledX
                            offsetY = screenCenterY - scaledY
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

























// SubwayMapScreen.kt
package com.example.myapplication.ui.screens

import android.content.Context
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.components.loadStationCoordinates
import com.example.myapplication.ui.components.loadConnections
import androidx.compose.foundation.gestures.detectDragGestures

@Composable
fun SubwayMapScreen(
    initialStationId: Int? = null,
    onStationSelected: (Int) -> Unit,
    lockSelection: Boolean = false // MeetingPlaceResultScreen.kt 에서 다른 역 선택하면 강조 표시 바뀌어서 추가
) {
    val context = LocalContext.current
    val stationCoordinates = remember { loadStationCoordinates(context) }
    val connections = remember { loadConnections(context) }

    // 노선 번호에 따른 색상 맵
    val lineColors = mapOf(
        1 to Color(0xFF00B050), // 1호선
        2 to Color(0xFF002060), // 2호선
        3 to Color(0xFF953735), // 3호선
        4 to Color(0xFFFF0000), // 4호선
        5 to Color(0xFF4A7EBB), // 5호선
        6 to Color(0xFFFFC514), // 6호선
        7 to Color(0xFF92D050), // 7호선
        8 to Color(0xFF00B0F0), // 8호선
        9 to Color(0xFF7030A0) // 9호선
    )

    // 확대 비율 고정
    val scale = 1.5f

    // 지도 오프셋 변수 (애니메이션 처리)
    var rawOffsetX by remember { mutableStateOf(0f) }
    var rawOffsetY by remember { mutableStateOf(0f) }
    val offsetX by animateFloatAsState(targetValue = rawOffsetX) // 부드러운 X 이동
    val offsetY by animateFloatAsState(targetValue = rawOffsetY) // 부드러운 Y 이동

    var selectedStationId by remember { mutableStateOf<Int?>(null) } // 선택된 역 ID 상태

    val density = LocalDensity.current

    if (stationCoordinates.isEmpty()) {
        Text("데이터를 로드할 수 없습니다.")
        return
    }

    // 핸드폰 화면 크기에서 중앙 좌표 계산
    var screenCenterX by remember { mutableStateOf(0f) }
    var screenCenterY by remember { mutableStateOf(0f) }

    LaunchedEffect(initialStationId) {
        if (initialStationId != null) {
            stationCoordinates[initialStationId]?.let { position ->
                rawOffsetX = screenCenterX - position.x * scale
                rawOffsetY = screenCenterY - position.y * scale - 200f
                selectedStationId = initialStationId // 초기 선택된 역 설정
            }
        }
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume() // 드래그 이벤트 소비
                    rawOffsetX += dragAmount.x
                    rawOffsetY += dragAmount.y
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
            // 캔버스를 이용해 선 그리기
            Canvas(modifier = Modifier.fillMaxSize()) {
                connections.forEach { (src, dst, lineNumber) ->
                    val srcPosition = stationCoordinates[src]
                    val dstPosition = stationCoordinates[dst]

                    if (srcPosition != null && dstPosition != null) {
                        // 반지름 값 계산
                        val radius = (8 * scale).dp.toPx()

                        // 시작점과 끝점을 원 중심으로 이동
                        val adjustedStartX = srcPosition.x * scale + radius
                        val adjustedStartY = srcPosition.y * scale + radius
                        val adjustedEndX = dstPosition.x * scale + radius
                        val adjustedEndY = dstPosition.y * scale + radius

                        // 선 그리기
                        drawLine(
                            color = lineColors[lineNumber] ?: Color.Black,
                            start = Offset(adjustedStartX, adjustedStartY),
                            end = Offset(adjustedEndX, adjustedEndY),
                            strokeWidth = 5.dp.toPx(),
                            cap = StrokeCap.Round
                        )
                    }
                }
            }

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
                        .background(
                            if (stationId == selectedStationId) Color.Red else Color(0xFFD9EAF7),
                            CircleShape
                        ) // 선택된 역에 대해 배경색 변경
                        .clickable(enabled = !lockSelection) {
                            // 선택된 역 번호 전달
                            onStationSelected(stationId)
                            selectedStationId = stationId // 선택된 역 설정

                            // 애니메이션이 적용된 오프셋 변경
                            rawOffsetX = screenCenterX - scaledX
                            rawOffsetY = screenCenterY - scaledY
                        }
                ) {
                    Text(
                        text = stationId.toString(),
                        fontSize = textSize,
                        color = Color.Black
                    )
                }
            }

            // 선택된 역 주위에 빨간색 원 그리기
            selectedStationId?.let { selectedId ->
                val selectedPosition = stationCoordinates[selectedId]
                if (selectedPosition != null) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val centerX = selectedPosition.x * scale
                        val centerY = selectedPosition.y * scale

                        drawCircle(
                            color = Color.Red,
                            radius = (12 * scale).dp.toPx(),
                            center = Offset(centerX + (8 * scale).dp.toPx(), centerY + (8 * scale).dp.toPx()),
                            style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.dp.toPx())
                        )
                    }
                }
            }
        }
    }
}

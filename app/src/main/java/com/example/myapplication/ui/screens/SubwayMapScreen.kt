import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.myapplication.SubwayMapDataInstance
import com.example.myapplication.ui.components.getLineColor

@Composable
fun SubwayMapScreen(
    initialStationId: Int? = null,
    onStationSelected: (Int) -> Unit,
    lockSelection: Boolean = false,
    centerOffset: Offset = Offset(0f, 0f),
    meetingPlace: Boolean = false,
    navController: NavHostController // NavController를 추가하여 홈 화면으로 이동 가능하게 함
) {
    val stationCoordinates = SubwayMapDataInstance.stationCoordinates
    val connections = SubwayMapDataInstance.connections

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
                if (meetingPlace) {
                    // MeetingPlaceResultScreen 전용 중심 좌표 계산
                    rawOffsetX = centerOffset.x - position.x * scale
                    rawOffsetY = centerOffset.y - position.y * scale
                } else {
                    // 기존 HomeScreen 계산법
                    rawOffsetX = screenCenterX - position.x * scale - 15f
                    rawOffsetY = screenCenterY - position.y * scale - 275f
                }
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
            .clickable {
                if(!meetingPlace) {
                    navController.popBackStack() // 이전 화면으로 이동
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
                            color = getLineColor(lineNumber),
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
                            if (stationId == selectedStationId) Color(0xFF2563EB) else Color(0xFFD9EAF7),
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

            selectedStationId?.let { selectedId ->
                val selectedPosition = stationCoordinates[selectedId]
                if (selectedPosition != null) {
                    // LocalDensity 사용
                    val density = LocalDensity.current

                    // 애니메이션 값 생성
                    val infiniteTransition = rememberInfiniteTransition()
                    val animatedRadius = infiniteTransition.animateFloat(
                        initialValue = with(density) { (12 * scale).dp.toPx() }, // LocalDensity로 변환
                        targetValue = with(density) { (20 * scale).dp.toPx() }, // LocalDensity로 변환
                        animationSpec = infiniteRepeatable(
                            animation = tween(durationMillis = 1200, easing = LinearEasing),
                            repeatMode = RepeatMode.Reverse
                        )
                    )

                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val centerX = selectedPosition.x * scale
                        val centerY = selectedPosition.y * scale

                        // 애니메이션 효과 원 그리기
                        drawCircle(
                            color = Color(0xFF4585F4).copy(alpha = 0.5f),
                            radius = animatedRadius.value,
                            center = Offset(
                                centerX + with(density) { (8 * scale).dp.toPx() },
                                centerY + with(density) { (8 * scale).dp.toPx() }
                            ),
                            style = androidx.compose.ui.graphics.drawscope.Stroke(width = with(density) { 3.dp.toPx() })
                        )
                    }
                }
            }
        }
    }
}
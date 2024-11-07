package com.example.myapplication.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.myapplication.R
import com.example.myapplication.ui.components.BottomNavigationBar
import com.example.myapplication.ui.components.StationInputField
import kotlin.math.max
import kotlin.math.min
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.sp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    var selectedItem by remember { mutableStateOf(0) }
    var searchText by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    // 초기 확대 및 위치 설정
    var scale by remember { mutableStateOf(2f) } // 초기 scale 값을 2f로 설정하여 하얀 배경이 보이지 않도록 함
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    // 이미지 크기와 스크롤 한계 설정
    val imageWidth = 1000f // 이미지 가로 크기 (예시 값, 실제 이미지 크기에 맞게 조정 필요)
    val imageHeight = 1000f // 이미지 세로 크기 (예시 값, 실제 이미지 크기에 맞게 조정 필요)
    val screenWidth = 500f  // 스크린 가로 크기 (예시 값, 실제 디바이스 크기에 맞게 조정 필요)
    val screenHeight = 800f // 스크린 세로 크기 (예시 값, 실제 디바이스 크기에 맞게 조정 필요)

    // 확대/축소에 따른 스크롤 범위 동적 계산
    val maxOffsetX = (imageWidth * scale - screenWidth) / 2
    val maxOffsetY = (imageHeight * scale - screenHeight) / 2

    Box(modifier = Modifier.fillMaxSize()) {
        // 확대, 축소 및 스크롤 가능한 이미지
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 72.dp)
                .pointerInput(Unit) {
                    detectTransformGestures { _, pan, zoom, _ ->
                        // 확대/축소 범위 제한 (1f에서 3f 사이)
                        scale = (scale * zoom).coerceIn(1f, 3f)

                        // 스크롤 범위 제한
                        val newOffsetX = offsetX + pan.x * scale
                        val newOffsetY = offsetY + pan.y * scale
                        offsetX = newOffsetX.coerceIn(-maxOffsetX, maxOffsetX)
                        offsetY = newOffsetY.coerceIn(-maxOffsetY, maxOffsetY)
                    }
                }
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    translationX = offsetX,
                    translationY = offsetY
                )
        ) {
            Image(
                painter = painterResource(id = R.drawable.map_image), // 노선도 이미지 리소스
                contentDescription = "지하철 노선도",
                modifier = Modifier.fillMaxSize()
            )
        }

        // 검색 바와 길찾기 버튼
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
                .padding(16.dp)
                .align(Alignment.TopCenter),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 검색 바
            StationInputField(
                value = searchText,
                onValueChange = { searchText = it },
                focusManager = focusManager,
                onSearchClick = { /* 검색 로직 구현 */
                    navController.navigate("stationDetail")
                                },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            // 길찾기 버튼
            // 길찾기 버튼 (아이콘 아래에 텍스트 추가)
            IconButton(
                onClick = { /* 길찾기 기능 구현 */ },
                modifier = Modifier
                    .size(48.dp)
                    .padding(start = 8.dp)
                    .background(Color(0xFF252F42), shape = CircleShape)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.NearMe,
                        contentDescription = "길찾기",
                        tint = Color.White,
                        modifier = Modifier.size(23.dp)
                    )
                    Text(
                        text = "길찾기",
                        color = Color.White,
                        fontSize = 8.5.sp,
                        modifier = Modifier.offset(y = -5.dp) // 텍스트를 위로 이동하여 간격 조정
                    )
                }
            }


        }

        // 하단 네비게이션 바
        BottomNavigationBar(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .height(62.dp),
            selectedItem = selectedItem,
            onItemSelected = { selectedItem = it },
            navController = navController
        )
    }
}

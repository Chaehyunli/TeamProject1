// HomeScreen.kt
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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.sp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    var selectedItem by remember { mutableStateOf(0) }
    var searchText by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }  // 경고창 표시 상태 변수
    val focusManager = LocalFocusManager.current
    var isFocused by remember { mutableStateOf(false) }  // 포커스 상태를 추적

    // 초기 확대 및 위치 설정
    var scale by remember { mutableStateOf(2f) }
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    val imageWidth = 1000f
    val imageHeight = 1000f
    val screenWidth = 500f
    val screenHeight = 800f

    val maxOffsetX = (imageWidth * scale - screenWidth) / 2
    val maxOffsetY = (imageHeight * scale - screenHeight) / 2

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable { // 화면 다른 부분을 누를 때 포커스를 해제
                focusManager.clearFocus()
                isFocused = false
            }
    ) {
        // 확대, 축소 및 스크롤 가능한 이미지
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 0.dp)
                .pointerInput(Unit) {
                    detectTransformGestures { _, pan, zoom, _ ->
                        scale = (scale * zoom).coerceIn(1f, 3f)
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
                painter = painterResource(id = R.drawable.map_image),
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
                onSearchClick = {
                    if (searchText.isBlank()) {
                        showDialog = true
                    } else {
                        navController.navigate("stationDetail/$searchText")
                    }
                    focusManager.clearFocus() // focus 해제
                    isFocused = false
                },
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        isFocused = true
                    }
            )

            Spacer(modifier = Modifier.width(8.dp))

            // 길찾기 버튼
            IconButton(
                onClick = {
                    navController.navigate("routeSearch")
                },
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
                        modifier = Modifier.offset(y = -5.dp)
                    )
                }
            }
        }

        // 경고 다이얼로그
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                text = { Text("※ 검색할 역 번호를 입력해 주세요.") },
                confirmButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("확인")
                    }
                }
            )
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

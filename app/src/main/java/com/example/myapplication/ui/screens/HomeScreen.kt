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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.myapplication.ui.components.BottomNavigationBar
import com.example.myapplication.ui.components.StationInputField
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.components.WarningDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    var selectedItem by remember { mutableStateOf(0) }
    var searchText by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }  // 경고창 표시 상태 변수
    val focusManager = LocalFocusManager.current
    var isFocused by remember { mutableStateOf(false) }  // 포커스 상태를 추적

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable { // 화면 다른 부분을 누를 때 포커스를 해제
                focusManager.clearFocus()
                isFocused = false
            }
    ) {
        // 확대, 축소 및 스크롤 가능한 SubwayMapScreen
        SubwayMapScreen()

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
                    .border(1.dp, Color.Black, shape = RoundedCornerShape(16.dp)) // 테두리 추가
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
                    .border(1.dp, Color(0xFFCBD2DF), shape = CircleShape) // 테두리 추가
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

        if (showDialog) {
            WarningDialog(
                message = "※ 검색할 역, 호선을 입력해 주세요.",
                onDismiss = { showDialog = false }
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


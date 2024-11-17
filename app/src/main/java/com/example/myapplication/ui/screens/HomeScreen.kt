// HomeScreen.kt
package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.myapplication.ui.components.BottomNavigationBar
import com.example.myapplication.ui.components.StationInputField
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import com.example.myapplication.ui.components.WarningDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    var selectedItem by remember { mutableStateOf(0) }
    var searchText by remember { mutableStateOf("") } // 검색창 상태
    var showDialog by remember { mutableStateOf(false) }  // 경고창 표시 상태 변수
    var triggerSearch by remember { mutableStateOf(false) } // 검색 버튼 트리거 상태
    val focusManager = LocalFocusManager.current // focusManager 가져오기

    // 검색 버튼 자동 클릭 감지
    LaunchedEffect(triggerSearch) {
        if (triggerSearch && searchText.isNotBlank()) {
            navController.navigate("stationDetail/$searchText")
            triggerSearch = false // 상태 초기화
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // SubwayMapScreen
        SubwayMapScreen(
            onStationSelected = { selectedStationId ->
            // 선택된 역의 상세 화면으로 바로 이동
            // 원래 입력 필드에 입력되고 이동 되는거 바로 이동하게 함
            navController.navigate("stationDetail/$selectedStationId")
            },
            lockSelection = false
        )

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
                focusManager = focusManager, // focusManager 전달
                onSearchClick = {
                    if (searchText.isBlank()) {
                        showDialog = true
                        focusManager.clearFocus()
                    } else {
                        navController.navigate("stationDetail/$searchText")
                    }
                },
                modifier = Modifier
                    .weight(1f)
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
                    .background(Color(0xFF252F42), shape = CircleShape)
                    .border(1.dp, Color(0xFFCBD2DF), shape = CircleShape) // 테두리 추가
            ) {
                Icon(
                    imageVector = Icons.Default.NearMe,
                    contentDescription = "길찾기",
                    tint = Color.White
                )
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
                .height(62.dp)
                .border(1.dp, Color.LightGray, RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
            selectedItem = selectedItem,
            onItemSelected = { selectedItem = it },
            navController = navController
        )
    }
}




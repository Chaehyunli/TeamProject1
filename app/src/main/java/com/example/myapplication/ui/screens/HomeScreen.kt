// HomeScreen.kt
package com.example.myapplication.ui.screens

import SubwayMapScreen
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.myapplication.SubwayGraphInstance
import com.example.myapplication.ui.components.BottomNavigationBar
import com.example.myapplication.ui.components.StationInputField
import com.example.myapplication.ui.components.WarningDialog
import androidx.activity.compose.BackHandler


@Composable
fun HomeScreen(navController: NavHostController) {
    var selectedItem by remember { mutableStateOf(0) }
    var searchText by remember { mutableStateOf("") } // 검색창 상태
    var showDialog by remember { mutableStateOf(false) }  // 경고창 표시 상태 변수
    var warningMessage by remember { mutableStateOf("") } // 경고 메시지
    var triggerSearch by remember { mutableStateOf(false) } // 검색 버튼 트리거 상태
    val focusManager = LocalFocusManager.current // focusManager 가져오기

    // 유효한 역 번호 및 호선 정의
    val validStations = SubwayGraphInstance.subwayGraph.getAllStationNumbers() + listOf(1, 2, 3, 4, 5, 6, 7, 8, 9)

    // 뒤로가기 버튼 핸들링
    BackHandler {
        // 뒤로가기 버튼을 눌렀을 때 앱 종료
        (navController.context as? android.app.Activity)?.finish()
    }

    // 검색 버튼 자동 클릭 감지
    LaunchedEffect(triggerSearch) {
        if (triggerSearch && searchText.isNotBlank()) {
            val stationId = searchText.toIntOrNull() // 숫자로 변환 가능 여부 확인
            if (stationId != null && validStations.contains(stationId)) {
                // 유효한 역 번호인 경우
                navController.navigate("stationDetail/$searchText")
            } else {
                // 유효하지 않은 입력일 경우
                warningMessage = "※ 유효한 역 번호를 입력해 주세요."
                showDialog = true
            }
            triggerSearch = false // 상태 초기화
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable { focusManager.clearFocus() }
    ) {
        // SubwayMapScreen
        SubwayMapScreen(
            onStationSelected = { selectedStationId ->
                // 선택된 역의 상세 화면으로 바로 이동
                navController.navigate("stationDetail/$selectedStationId")
            },
            lockSelection = false,
            navController = navController
        )

        // 검색 바와 길찾기 버튼
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(horizontal = 16.dp)
                .padding(top = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 검색 바
            StationInputField(
                value = searchText,
                onValueChange = { searchText = it },
                focusManager = focusManager,
                onSearchClick = {
                    val trimmedSearchText = searchText.trim() // 앞뒤 공백 제거
                    if (trimmedSearchText.isBlank()) {
                        warningMessage = "※ 검색할 역, 호선을 입력해 주세요."
                        showDialog = true
                    } else {
                           val stationId = trimmedSearchText.toIntOrNull() // 입력값이 숫자인지 확인
                        if (stationId == null || !validStations.contains(stationId)) {
                            // 유효하지 않은 입력일 경우
                            warningMessage = "※ 유효한 역 번호를 입력해 주세요."
                            showDialog = true
                        } else {
                            // 유효한 역 번호일 경우
                            navController.navigate("stationDetail/$searchText")
                        }
                    }
                    focusManager.clearFocus()
                },
                modifier = Modifier
                    .weight(1f)
                    .border(2.dp, Color(0xFF252f42), shape = RoundedCornerShape(20.dp))
            )

            Spacer(modifier = Modifier.width(10.dp))

            // 길찾기 버튼
            IconButton(
                onClick = {
                    navController.navigate("routeSearch")
                },
                modifier = Modifier
                    .size(52.dp)
                    .background(Color(0xFF252F42), shape = CircleShape)
                    .border(2.dp, Color(0xFFCBD2DF), shape = CircleShape) // 테두리 추가
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Icon(
                        imageVector = Icons.Default.NearMe,
                        contentDescription = "길찾기",
                        tint = Color(0xFFCBD3DF),
                        modifier = Modifier.size(24.dp) // 아이콘 크기 조정
                    )
                    Text(
                        text = "길찾기",
                        color = Color(0xFFCBD3DF),
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 12.sp
                    )
                }
            }
        }

        // 경고 메세지 표시
        if (showDialog) {
            WarningDialog(
                message = warningMessage,
                onDismiss = { showDialog = false }
            )
        }

        // 하단 네비게이션 바
        BottomNavigationBar(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .height(62.dp)
                .border(1.dp, Color(0xFF808590), RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
            selectedItem = selectedItem,
            onItemSelected = { selectedItem = it },
            navController = navController
        )
    }
}

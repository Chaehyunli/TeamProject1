// MeetingPlaceResultScreen.kt
package com.example.myapplication.ui.screens

import SubwayMapScreen
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.geometry.Offset

import com.example.myapplication.ui.components.BottomNavigationBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeetingPlaceResultScreen(
    navController: NavHostController,
    result: MeetingPlaceResult,
    inputFields: List<String>
) {
    var selectedItem by remember { mutableStateOf(1) } // 현재 선택된 BottomNavigation 아이템 (약속장소 추천)
    val bestStation = result.bestStation
    val timesFromStartStations = result.timesFromStartStations

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = { Text("약속장소 결과 보기") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "뒤로가기"
                        )
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color.White
                ),
                modifier = Modifier.border(1.dp, Color.LightGray)
            )
        },
        bottomBar = {
            BottomNavigationBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(62.dp)
                    .border(1.dp, Color.LightGray, RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                selectedItem = selectedItem,
                onItemSelected = { selectedItem = it },
                navController = navController
            )
        }
    ) { innerPadding ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // 현재 화면 중심 좌표 계산
            val centerOffset = Offset(
                x = constraints.maxWidth / 2f,
                y = constraints.maxHeight / 2f
            )

            SubwayMapScreen(
                initialStationId = bestStation,
                onStationSelected = { /* 아무 동작도 하지 않음 */ },
                lockSelection = true,
                centerOffset = centerOffset,
                meetingPlace = true // 약속장소 전용 플래그 전달
            )

            // 추천 장소 정보 카드
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color(0xFFF3F4F6).copy(alpha = 0.9f), // 불투명 배경 추가
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(16.dp)
                    .align(Alignment.TopCenter)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "추천 약속장소: $bestStation",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    timesFromStartStations.forEachIndexed { index, time ->
                        Text(
                            text = "출발지: ${inputFields[index]}, 소요시간: ${formatTime(time)}",
                            fontSize = 16.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}

// 소요 시간을 시간, 분, 초로 변환하는 함수
fun formatTime(seconds: Int): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return "${minutes}분 ${String.format("%02d", remainingSeconds)}초"
}

package com.example.myapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.myapplication.ui.components.BottomNavigationBar

// MeetingPlaceResultScreen.kt
@OptIn(ExperimentalMaterial3Api::class) // 실험적 API 사용 경고 무시
@Composable
fun MeetingPlaceResultScreen(
    navController: NavHostController,
    result: MeetingPlaceResult,
    inputFields: List<String>
) {
    var selectedItem by remember { mutableStateOf(1) } // 현재 선택된 BottomNavigation 아이템 (약속장소 추천)

    val meetingPlaceResult = result
    val bestStation = meetingPlaceResult.bestStation
    val timesFromStartStations = meetingPlaceResult.timesFromStartStations

    // 소요 시간을 분과 초로 변환하는 함수
    fun formatTime(seconds: Int): String {
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        return "${minutes}분 ${String.format("%02d", remainingSeconds)}초"
    }

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
                modifier = Modifier.shadow(4.dp) // 그림자 추가
            )
        },
        bottomBar = {
            BottomNavigationBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(62.dp),
                selectedItem = selectedItem,
                onItemSelected = { item -> selectedItem = item },
                navController = navController
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "추천 약속장소...",
                fontSize = 18.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = bestStation.toString(),
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 각 출발지 소요 시간 표시
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                inputFields.forEachIndexed { index, field ->
                    val time = timesFromStartStations.getOrNull(index) ?: 0
                    Text(
                        text = "출발지: $field, 소요시간: ${formatTime(time)}",
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center, // 출발지 텍스트 왼쪽 정렬
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

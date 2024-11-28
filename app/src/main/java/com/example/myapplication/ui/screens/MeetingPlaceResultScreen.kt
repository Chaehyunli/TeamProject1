// MeetingPlaceResultScreen.kt
package com.example.myapplication.ui.screens

import SubwayMapScreen
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.myapplication.ui.components.TimeTextFormatter

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

    // 소요시간 평균 계산
    val averageTime = if (timesFromStartStations.isNotEmpty()) {
        timesFromStartStations.sum() / timesFromStartStations.size
    } else 0

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = { Text(text = "약속장소 결과 보기", fontSize = 18.sp, color = Color(0xFF252f42)) },
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

    ) { innerPadding ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // 현재 화면 중심 좌표 계산
            val centerOffset = Offset(
                x = constraints.maxWidth / 2f,
                y = constraints.maxHeight / 4f
            )

            SubwayMapScreen(
                initialStationId = bestStation,
                onStationSelected = { /* 아무 동작도 하지 않음 */ },
                lockSelection = true,
                centerOffset = centerOffset,
                meetingPlace = true, // 약속장소 전용 플래그 전달
                navController = navController
            )

            // 추천 장소 정보 카드
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(topStart = 24.dp,topEnd = 24.dp)
                    )
                    .border(1.dp,Color.LightGray, RoundedCornerShape(topStart=24.dp,topEnd=24.dp))
                    .padding(16.dp)
                    .align(Alignment.BottomCenter)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp,horizontal = 8.dp)
                ) {
                    Text(
                        text = "${bestStation}역",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFF6F61)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Divider(color = Color(0xFF808590), thickness = 1.dp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "평균 소요시간 ${TimeTextFormatter(time = averageTime)}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF252f42)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    timesFromStartStations.forEachIndexed { index, time -> Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${inputFields[index]}역 ▶ 소요시간 ${TimeTextFormatter(time = time)}",
                            fontSize = 16.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}


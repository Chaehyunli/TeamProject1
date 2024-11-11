// RouteDetailScreen.kt
package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteDetailScreen(
    path: List<String>, // 경로의 역 목록
    criteria: List<String>, // 경로 기준 목록
    time: Int, // 총 시간
    distance: Int, // 총 거리
    transfers: Int, // 환승 횟수
    cost: Int, // 총 비용
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("경로 상세 정보") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "뒤로가기")
                    }
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                // 전달받은 데이터를 이용해 경로 상세 정보 표시
                Text("경로: ${path.joinToString(" -> ")}", fontSize = 18.sp)
                Text("기준: ${criteria.joinToString(", ")}", fontSize = 16.sp)
                Text("총 시간: ${time}초", fontSize = 16.sp)
                Text("총 거리: ${distance}m", fontSize = 16.sp)
                Text("환승 횟수: ${transfers}회", fontSize = 16.sp)
                Text("총 비용: ${cost}원", fontSize = 16.sp)
            }
        }
    )
}
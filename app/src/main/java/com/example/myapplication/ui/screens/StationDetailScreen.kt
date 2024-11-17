// StationDetailScreen.kt
package com.example.myapplication.ui.screens

import SubwayMapScreen
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.ui.components.StationDetailDialog
import com.example.myapplication.SubwayGraphInstance

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StationDetailScreen(
    navController: NavController, // NavController를 추가하여 다른 화면으로 이동할 수 있도록 설정
    stationName: String,
    onBack: () -> Unit
) {
    // stationName을 Int로 변환하여 SubwayGraphInstance에서 역 번호 가져오기
    val stationNumber = stationName.toIntOrNull()

    // SubwayGraphInstance에서 해당 역 번호에 대한 정보를 개별적으로 가져오기
    val stationData = stationNumber?.let { SubwayGraphInstance.subwayGraph.stations[it] }
    val lineNumbers = stationData?.lineNumbers
    val neighbors = stationData?.neighbors
    val hasTransfer = lineNumbers?.size?.let { it > 1 } ?: false

    // 특정 호선 번호일 경우 해당 호선의 연결된 순서대로 전체 역 목록 가져오기
    val isLineNumber = stationNumber in 1..9
    val orderedStationsInLine = if (isLineNumber) {
        SubwayGraphInstance.getStationsByLineInOrder(stationNumber!!)
    } else {
        null
    }

    // stationNameDisplay 구성
    val stationNameDisplay = if (isLineNumber) {
        "${stationNumber}호선 전체 역 목록"
    } else if (lineNumbers != null && lineNumbers.isNotEmpty()) {
        "${lineNumbers.first()}호선 ${stationNumber}역"
    } else {
        "역 정보 없음"
    }

    // transferInfo 구성
    val transferInfo = if (isLineNumber) {
        "환승역 포함한 ${stationNumber}호선 연결된 순서의 전체 역 목록입니다."
    } else if (hasTransfer) {
        "환승 가능한 역입니다."
    } else {
        "환승할 수 없는 역입니다."
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stationNameDisplay, fontSize = 18.sp, color = Color(0xFF252f42)) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("home")  }) { // HomeScreen으로 이동
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "뒤로가기")
                    }
                },
                modifier = Modifier
                    .border(1.dp, Color.LightGray),
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.White),
            )
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.BottomCenter
            ) {
                // 호선 번호가 아닌 경우에만 노선도 표시
                if (!isLineNumber) {
                    if (stationNumber != null) {
                        SubwayMapScreen(
                            initialStationId = stationNumber, // 선택된 역을 초기 중심 역으로 설정
                            onStationSelected = { selectedStationId ->
                                // 선택된 역이 있으면 화면 이동
                                navController.navigate("stationDetail/$selectedStationId")
                            }
                        )
                    } else {
                        // 역 번호가 잘못된 경우 오류 메시지 표시
                        Text(
                            text = "유효하지 않은 역 정보입니다.",
                            color = Color.Red,
                            fontSize = 16.sp,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(16.dp)
                        )
                    }
                }

                // 하단 정보 카드 (화면 하단에 고정)
                if (isLineNumber && orderedStationsInLine != null) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        items(orderedStationsInLine) { station ->
                            Text(
                                text = "$station 번 역",
                                fontSize = 16.sp,
                                modifier = Modifier
                                    .padding(8.dp)
                                    .fillMaxWidth()
                            )
                        }
                    }
                } else {
                    // 선택된 역의 상세 정보 출력
                    StationDetailDialog(
                        stationName = stationNameDisplay,
                        transferInfo = transferInfo,
                        transferDetailInfo = neighbors?.joinToString("\n") { edge ->
                            "${edge.destination}역 방면 | ${if (lineNumbers?.contains(edge.line) == true) "환승 가능" else "환승 불가"}"
                        } ?: "해당 역에 대한 추가 정보가 없습니다.",
                        onStartClick = {
                            navController.navigate("routeSearch?startStation=$stationNumber&endStation=")
                        },
                        onEndClick = {
                            navController.navigate("routeSearch?startStation=&endStation=$stationNumber")
                        }
                    )
                }
            }
        }
    )
}

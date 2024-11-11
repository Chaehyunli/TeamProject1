// RouteSearchScreen.kt
// 길찾기 화면
package com.example.myapplication.ui.screens

import SortButton
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import com.example.myapplication.RouteFinder
import com.example.myapplication.SubwayGraphInstance
import com.example.myapplication.ui.components.RouteInputField
import com.example.myapplication.ui.components.WarningDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteSearchScreen(onBack: () -> Unit) {
    var showAlertDialog by remember { mutableStateOf(false) }
    var alertMessage by remember { mutableStateOf("") }
    var searchResults by remember { mutableStateOf<List<RouteFinder.RouteInfo>?>(null) }
    val focusManager = LocalFocusManager.current
    var selectedSortCriteria by remember { mutableStateOf("최단 거리 순") }

    // 출발지와 도착지 입력 필드를 관리하는 리스트
    var inputFields = remember { mutableStateListOf("", "") }

    // 지하철 전체 역 목록을 가져와 저장
    val validStations = SubwayGraphInstance.subwayGraph.getAllStationNumbers()

    Scaffold(
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(color = Color.White)
                    .clickable { focusManager.clearFocus() },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top Box - Full width and at the top of the screen
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFcbd2df))
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.Start
                        ) {
                            // Back and Switch Icons
                            IconButton(
                                onClick = onBack,
                                modifier = Modifier
                                    .padding(4.dp)
                                    .size(36.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "뒤로가기",
                                    tint = Color(0xFF252f42)
                                )
                            }
                            IconButton(
                                onClick = {
                                    val temp = inputFields[0]
                                    inputFields[0] = inputFields[1]
                                    inputFields[1] = temp
                                },
                                modifier = Modifier
                                    .padding(4.dp)
                                    .size(40.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.SwapHoriz,
                                    contentDescription = "전환",
                                    tint = Color(0xFF252f42)
                                )
                            }
                        }

                        // Departure and Arrival Fields using RouteInputField
                        Column(
                            modifier = Modifier
                                .clip(shape = RoundedCornerShape(10.dp))
                                .weight(4.5f)
                        ) {
                            RouteInputField(
                                label = "출발지 입력",
                                value = inputFields[0],
                                onValueChange = { newText -> inputFields[0] = newText },
                                onDelete = {}, // 삭제 기능 필요 없음
                                canDeleteField = inputFields[0].isNotEmpty(),
                                focusManager = focusManager
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            RouteInputField(
                                label = "도착지 입력",
                                value = inputFields[1],
                                onValueChange = { newText -> inputFields[1] = newText },
                                onDelete = {}, // 삭제 기능 필요 없음
                                canDeleteField = inputFields[1].isNotEmpty(),
                                focusManager = focusManager
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // SortButton with callback
                    SortButton { criteria ->
                        selectedSortCriteria = criteria
                    }

                    Spacer(modifier = Modifier.width(35.dp))

                    // 변경된 부분: 검색 버튼
                    Button(
                        onClick = {
                            val departure = inputFields[0]
                            val arrival = inputFields[1]
                            when {
                                departure.isBlank() -> {
                                    alertMessage = "※ 출발지를 입력하세요."
                                    showAlertDialog = true
                                }
                                arrival.isBlank() -> {
                                    alertMessage = "※ 도착지를 입력하세요."
                                    showAlertDialog = true
                                }
                                departure.toIntOrNull() == null || departure.toInt() !in validStations -> {
                                    alertMessage = "※ 출발지 역이 유효하지 않습니다."
                                    showAlertDialog = true
                                }
                                arrival.toIntOrNull() == null || arrival.toInt() !in validStations -> {
                                    alertMessage = "※ 도착지 역이 유효하지 않습니다."
                                    showAlertDialog = true
                                }
                                else -> {
                                    val startStation = departure.toInt()
                                    val endStation = arrival.toInt()
                                    searchResults = SubwayGraphInstance.findUniqueRoutes(startStation, endStation)
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF242F42)),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = "검색",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                HorizontalDivider(color = Color(0xFF808590), thickness = 0.5.dp)
                Spacer(modifier = Modifier.height(8.dp))

                // 경로 찾기 결과 표시
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    searchResults?.sortedWith(
                        when (selectedSortCriteria) {
                            "최단 거리 순" -> compareBy { it.distance }
                            "최소 시간 순" -> compareBy { it.time }
                            "최소 비용 순" -> compareBy { it.cost }
                            "최소 환승 순" -> compareBy { it.transfers }
                            else -> compareBy { it.time }
                        }
                    )?.forEach { route ->
                        Text(
                            text = """
                                경로: ${route.path.joinToString(" -> ")}
                                기준: ${route.criteria.joinToString(", ")}
                                총 시간: ${route.time}초
                                총 거리: ${route.distance}m
                                환승 횟수: ${route.transfers}회
                                총 비용: ${route.cost}원
                            """.trimIndent(),
                            modifier = Modifier.padding(8.dp),
                            color = Color.DarkGray,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                Spacer(modifier = Modifier.height(38.dp))

                // Guide message
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.backgroundlogo), // 사용할 이미지 리소스 ID
                        contentDescription = "배경 이미지",
                        modifier = Modifier
                            .size(200.dp) // 이미지 크기 조정
                            .align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    )
    if (showAlertDialog) {
        WarningDialog(
            message = alertMessage,
            onDismiss = { showAlertDialog = false }
        )
    }
}

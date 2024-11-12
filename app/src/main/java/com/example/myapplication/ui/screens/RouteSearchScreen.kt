// RouteSearchScreen.kt
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.RouteFinder
import com.example.myapplication.SubwayGraphInstance
import com.example.myapplication.ui.components.RouteInputField
import com.example.myapplication.ui.components.WarningDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteSearchScreen(
    navController: NavController,
    navBackStackEntry: NavBackStackEntry, // NavBackStackEntry를 추가하여 매개변수 접근
    onBack: () -> Unit
) {
    // startStation 및 endStation 값을 NavBackStackEntry에서 가져와 기본값으로 설정
    val startStation = navBackStackEntry.arguments?.getString("startStation") ?: ""
    val endStation = navBackStackEntry.arguments?.getString("endStation") ?: ""

    var showAlertDialog by rememberSaveable { mutableStateOf(false) }
    var alertMessage by rememberSaveable { mutableStateOf("") }
    var searchResults by rememberSaveable { mutableStateOf<List<RouteFinder.RouteInfo>?>(null) }
    val focusManager = LocalFocusManager.current
    var selectedSortCriteria by rememberSaveable { mutableStateOf("최단 거리 순") }

    // 출발지와 도착지 입력 필드를 관리하는 리스트
    var inputFields by rememberSaveable { mutableStateOf(listOf(startStation, endStation)) }

    // 지하철 전체 역 목록을 가져와 저장
    val validStations = SubwayGraphInstance.subwayGraph.getAllStationNumbers()

    // 검색 로직을 함수로 정의하여 전환 버튼과 검색 버튼에서 호출 가능하게 함
    fun onSearch() {
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
        focusManager.clearFocus()
    }

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
                                    inputFields = listOf(inputFields[1], temp)
                                    onSearch() // 전환 후 자동으로 검색 실행
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
                                onValueChange = { newText -> inputFields = listOf(newText, inputFields[1]) },
                                onDelete = {}, // 삭제 기능 필요 없음
                                canDeleteField = inputFields[0].isNotEmpty(),
                                focusManager = focusManager
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            RouteInputField(
                                label = "도착지 입력",
                                value = inputFields[1],
                                onValueChange = { newText -> inputFields = listOf(inputFields[0], newText) },
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

                    // 검색 버튼
                    Button(
                        onClick = {
                            onSearch() // 검색 로직 호출
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
                Divider(color = Color(0xFF808590), thickness = 0.5.dp)
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
                    )?.forEachIndexed { index, route ->
                        Text(
                            text = """
                            경로: ${route.path.joinToString(" -> ")}
                            기준: ${route.criteria.joinToString(", ")}
                            총 시간: ${route.time}초
                            총 거리: ${route.distance}m
                            환승 횟수: ${route.transfers}회
                            총 비용: ${route.cost}원
                        """.trimIndent(),
                            modifier = Modifier
                                .padding(8.dp)
                                .clickable {
                                    // 각 필드를 개별 문자열로 전달
                                    val path = route.path.joinToString(",")
                                    val time = route.time
                                    val distance = route.distance
                                    val transfers = route.transfers
                                    val cost = route.cost

                                    // lineNumbers를 경로 길이에 맞게 조정
                                    val adjustedLineNumbers = if (route.lineNumbers.size >= route.path.size - 1) {
                                        route.lineNumbers.subList(0, route.path.size - 1)
                                    } else {
                                        route.lineNumbers + List(route.path.size - 1 - route.lineNumbers.size) { -1 }
                                    }

                                    val lineNumbers = adjustedLineNumbers.joinToString(",")

                                    // 환승역 목록 생성 및 문자열로 변환
                                    val transferStations = route.path.filterIndexed { idx, _ ->
                                        idx > 0 && adjustedLineNumbers.getOrNull(idx) != adjustedLineNumbers.getOrNull(idx - 1)
                                    }.joinToString(",")

                                    // 경로 상세 화면으로 이동
                                    navController.navigate(
                                        "routeDetail/$path/$transferStations/$time/$distance/$transfers/$cost/$lineNumbers"
                                    )
                                },
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
                        painter = painterResource(id = R.drawable.backgroundlogo),
                        contentDescription = "배경 이미지",
                        modifier = Modifier
                            .size(200.dp)
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

// MeetingPlaceScreen.kt
package com.example.myapplication.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.myapplication.SubwayGraphInstance
import com.example.myapplication.ui.components.BottomNavigationBar
import com.example.myapplication.ui.components.RouteInputField
import com.example.myapplication.ui.components.WarningDialog

data class MeetingPlaceResult(
    val bestStation: Int, // 추천된 약속 장소
    val timesFromStartStations: List<Int> // 출발지 리스트에서 추천 장소까지의 소요시간
)

@Composable
fun MeetingPlaceScreen(navController: NavHostController) {
    val focusManager = LocalFocusManager.current
    var selectedItem by remember { mutableStateOf(1) }
    var showAlertDialog by remember { mutableStateOf(false) }
    var warningMessage by remember { mutableStateOf("") } // 경고 메시지
    var inputFields by rememberSaveable { mutableStateOf(listOf("", "")) }

    Scaffold(
        containerColor = Color.White,
        bottomBar = {
            BottomNavigationBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(62.dp)
                    .border(1.dp, Color(0xFF808590), RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                selectedItem = selectedItem,
                onItemSelected = { selectedItem = it },
                navController = navController
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .clickable { focusManager.clearFocus() },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                inputFields.forEachIndexed { index, text ->
                    RouteInputField(
                        label = "${index + 1}. 출발지 입력",
                        value = text,
                        onValueChange = { newValue ->
                            inputFields = inputFields.toMutableList().apply { this[index] = newValue }
                        },
                        onDelete = {
                            if (inputFields.size > 2) {
                                inputFields = inputFields.toMutableList().apply { removeAt(index) }
                            } else {
                                inputFields = inputFields.toMutableList().apply { this[index] = "" }
                            }
                        },
                        focusManager = focusManager,
                        canDeleteField = inputFields.size > 2 || text.isNotEmpty() // 처음 두 필드는 값이 있을 때만 X 표시
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                Row(
                    modifier = Modifier
                        .clickable {
                            if (inputFields.size < 4) {
                                inputFields = inputFields + ""
                            }
                            focusManager.clearFocus()
                        }
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.PersonAdd, // 사용할 아이콘 (필요에 따라 변경 가능)
                        contentDescription = "사람 추가하기 아이콘",
                        tint = Color(0xFF707070), // 아이콘 색상
                        modifier = Modifier.size(28.dp) // 아이콘 크기
                    )
                    Spacer(modifier = Modifier.width(8.dp)) // 아이콘과 텍스트 간격
                    Text(
                        text = "사람 추가하기",
                        color = Color(0xFF707070)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = {
                        val trimmedInputFields = inputFields.map { it.trim() } // 모든 입력값의 앞뒤 공백 제거
                        if (trimmedInputFields.any { it.isBlank() }) {
                            showAlertDialog = true
                            warningMessage = "※ 출발지를 입력하세요."
                        } else if (trimmedInputFields.any { field ->
                                val stationNumber = field.toIntOrNull()
                                stationNumber == null || SubwayGraphInstance.subwayGraph.getNeighbors(stationNumber) == null
                            }) {
                            showAlertDialog = true
                            warningMessage = "※ 지하철 역이 유효하지 않습니다."
                        } else {
                            SubwayGraphInstance.calculateMeetingPlaceRoute(trimmedInputFields)?.let { result ->
                                val meetingPlaceResult = MeetingPlaceResult(
                                    bestStation = result.bestStation,
                                    timesFromStartStations = result.timesFromStartStations
                                )

                                val resultString = "${meetingPlaceResult.bestStation},${meetingPlaceResult.timesFromStartStations.joinToString(",")}"
                                val inputFieldsString = trimmedInputFields.joinToString(",")

                                navController.navigate("meeting_place_result/$resultString/$inputFieldsString")
                            }
                        }
                        focusManager.clearFocus()
                    },
                    modifier = Modifier
                        .height(50.dp)
                        .border(
                            width = 2.dp, // 테두리 두께
                            color = Color(0xFFcbd2df), // 테두리 색상
                            shape = RoundedCornerShape(20.dp)
                        ),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF242F42)),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(
                        text = "약속장소 찾기",
                        color = Color(0xFFcbd2df)
                    )
                }
            }

            if (showAlertDialog) {
                WarningDialog(
                    message = warningMessage,
                    onDismiss = { showAlertDialog = false }
                )
            }
        }
    }
}
// MeetingPlaceScreen.kt
package com.example.myapplication.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.myapplication.SubwayGraphInstance
import com.example.myapplication.ui.components.BottomNavigationBar // 하단 아이콘 nav
import com.example.myapplication.ui.components.RouteInputField
import com.example.myapplication.ui.components.WarningDialog

data class MeetingPlaceResult(
    val bestStation: Int,
    val timesFromStartStations: List<Int>
)

@Composable
fun MeetingPlaceScreen(navController: NavHostController) {
    val focusManager = LocalFocusManager.current
    var selectedItem by remember { mutableStateOf(1) }
    var showAlertDialog by remember { mutableStateOf(false) }
    var invalidStationAlert by remember { mutableStateOf(false) }
    var inputFields = remember { mutableStateListOf("", "") }

    Scaffold(
        containerColor = Color.White,
        bottomBar = {
            BottomNavigationBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(62.dp),
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
                        onValueChange = { newValue -> inputFields[index] = newValue },
                        onDelete = { inputFields.removeAt(index) },
                        focusManager = focusManager,
                        canDeleteField = inputFields.size > 2 || text.isNotEmpty() // 처음 두 필드는 값이 있을 때만 X 표시
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                Text(
                    text = "사람 추가하기",
                    color = Color(0xFF707070),
                    modifier = Modifier
                        .clickable {
                            if (inputFields.size < 4) {
                                inputFields.add("")
                            }
                            focusManager.clearFocus()
                        }
                        .padding(8.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        if (inputFields.any { it.isBlank() }) {
                            showAlertDialog = true
                            focusManager.clearFocus()
                        } else if (inputFields.any { field ->
                                val stationNumber = field.toIntOrNull()
                                stationNumber == null || SubwayGraphInstance.subwayGraph.getNeighbors(stationNumber) == null
                            }) {
                            invalidStationAlert = true
                        } else {
                            SubwayGraphInstance.calculateMeetingPlaceRoute(inputFields)?.let { result ->
                                val meetingPlaceResult = MeetingPlaceResult(
                                    bestStation = result.bestStation,
                                    timesFromStartStations = result.timesFromStartStations
                                )

                                val resultString = "${meetingPlaceResult.bestStation},${meetingPlaceResult.timesFromStartStations.joinToString(",")}"
                                val inputFieldsString = inputFields.joinToString(",")

                                navController.navigate("meeting_place_result/$resultString/$inputFieldsString")
                                focusManager.clearFocus()
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF242F42)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "약속장소 찾기",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            if (showAlertDialog) {
                WarningDialog(
                    message = "※ 출발지를 입력하세요.",
                    onDismiss = { showAlertDialog = false }
                )
            }

            if (invalidStationAlert) {
                WarningDialog(
                    message = "※ 지하철 역이 유효하지 않습니다.",
                    onDismiss = { invalidStationAlert = false }
                )
            }
        }
    }
}
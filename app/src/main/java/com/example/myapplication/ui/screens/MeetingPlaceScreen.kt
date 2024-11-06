package com.example.myapplication.ui.screens

// 약속장소 찾기 버튼 누를 시에 기능 호출하게 하는 거 구현해야함.
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

@Composable
fun MeetingPlaceScreen(navController: NavHostController) {
    val focusManager = LocalFocusManager.current // focusManager 선언
    var selectedItem by remember { mutableStateOf(1) } // 현재 선택된 BottomNavigation 아이템 (약속장소 추천)
    var showAlertDialog by remember { mutableStateOf(false) }
    var invalidStationAlert by remember { mutableStateOf(false) }
    var inputFields = remember { mutableStateListOf("", "") } // 각 필드의 텍스트 값을 저장하는 리스트

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 내용 부분 (상단에 배치)
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .clickable { focusManager.clearFocus() }, // 필드 외부를 클릭하면 포커스 해제
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            inputFields.forEachIndexed { index, text ->
                RouteInputField(
                    label = "${index + 1}. 출발지 입력",
                    onDelete = {
                        if (inputFields.size > 2) {
                            inputFields.removeAt(index)
                        }
                    },
                    canDelete = inputFields.size > 2,
                    focusManager = focusManager,
                    value = text,
                    onValueChange = { newText -> inputFields[index] = newText }
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
                        focusManager.clearFocus() // 버튼 클릭 시 포커스 해제
                    }
                    .padding(8.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    // 입력 필드가 모두 비어있지 않은지 확인
                    if (inputFields.any { it.isBlank() }) {
                        showAlertDialog = true
                        focusManager.clearFocus() // 버튼 클릭 시 포커스 해제
                    } else if (inputFields.any { field ->
                            val stationNumber = field.toIntOrNull()
                            stationNumber == null || SubwayGraphInstance.subwayGraph.getNeighbors(stationNumber) == null
                        }){
                        // 유효하지 않은 역 번호가 있는 경우 경고 표시
                        invalidStationAlert = true

                    }else{
                        // 약속 장소 찾기 기능은 MeetingPlaceScreen에서 호출하여 데이터 준비
                        // 화면 전환 후, MeetingPlaceResultScreen에서 ViewModel을 참조하여 준비된 데이터를 표시
                        // 이렇게 하면 MeetingPlaceResultScreen은 데이터 준비에 신경 쓰지 않고, 결과를 보여주는 역할만 담당

                        // 여기서 약속 장소 찾기 기능 호출
                        navController.navigate("meeting_place_result") // 화면 전환
                        focusManager.clearFocus() // 버튼 클릭 시 포커스 해제
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

        // 경고문 출력 (출발지 다 입력되지 않았을 때)
        if (showAlertDialog) {
            WarningDialog(
                message = "※ 출발지를 입력하세요.",
                onDismiss = { showAlertDialog = false }
            )
        }

        // 유효하지 않은 역 경고문 출력
        if (invalidStationAlert) {
            WarningDialog(
                message = "※ 지하철 역이 유효하지 않습니다.",
                onDismiss = { invalidStationAlert = false }
            )
        }

        // BottomNavigationBar는 하단에 위치
        BottomNavigationBar(
            modifier = Modifier
                .fillMaxWidth()
                .height(62.dp),
            selectedItem = selectedItem,
            onItemSelected = { item -> selectedItem = item }, // 아이템 선택 시 selectedItem 업데이트
            navController = navController // navController 전달
        )
    }
}

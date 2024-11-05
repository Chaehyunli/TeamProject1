package com.example.myapplication.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

import com.example.myapplication.ui.components.BottomNavigationBar // 하단 아이콘 nav
import com.example.myapplication.ui.components.RouteInputField

@Composable
fun MeetingPlaceScreen(navController: NavHostController) {
    val focusManager = LocalFocusManager.current // focusManager 선언
    var inputFields by remember { mutableStateOf(listOf(1, 2)) }
    var selectedItem by remember { mutableStateOf(1) } // 현재 선택된 BottomNavigation 아이템 (약속장소 추천)

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

            inputFields.forEachIndexed { index, _ ->
                RouteInputField(
                    label = "${index + 1}. 출발지 입력",
                    onDelete = { inputFields = inputFields.dropLast(1) },
                    canDelete = inputFields.size > 2,
                    focusManager = focusManager // FocusManager 전달
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            Text(
                text = "사람 추가하기",
                color = Color(0xFF707070),
                modifier = Modifier
                    .clickable {
                        if (inputFields.size < 4) inputFields = inputFields + (inputFields.size + 1)
                        focusManager.clearFocus() // 버튼 클릭 시 포커스 해제
                    }
                    .padding(8.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    /* 약속 장소 찾기 기능 */
                    focusManager.clearFocus() // 버튼 클릭 시 포커스 해제
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

        // BottomNavigationBar는 하단에 위치
        BottomNavigationBar(
            modifier = Modifier
                .fillMaxWidth()
                .height(62.dp),
            selectedItem = selectedItem,
            onItemSelected = { selectedItem = it }, // 아이템 선택 시 selectedItem 업데이트
            navController = navController // navController 전달
        )
    }
}

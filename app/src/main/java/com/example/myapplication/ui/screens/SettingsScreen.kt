// SettingsScreen.kt
package com.example.myapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.myapplication.ui.components.BottomNavigationBar

@Composable
fun SettingsScreen(navController: NavHostController) {
    var selectedItem by remember { mutableStateOf(3) } // 현재 선택된 BottomNavigation 아이템 (설정)

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
                .padding(16.dp)
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 내용 부분 (상단에 배치)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // 이용 약관 및 정책 항목
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { /* 이용 약관 페이지로 이동 코드 작성 */ }
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("이용 약관 및 정책", fontSize = 16.sp, color = Color.Black)
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "이용 약관으로 이동",
                        tint = Color.Gray
                    )
                }

                Divider(color = Color.LightGray, thickness = 1.dp)

                // 버전 정보
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("버전", fontSize = 16.sp, color = Color.Black)
                    Text("v1.0.0", fontSize = 16.sp, color = Color.Gray)
                }
            }
        }
    }
}

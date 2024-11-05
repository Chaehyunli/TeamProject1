// SettingsScreen.kt
package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.myapplication.ui.components.BottomNavigationBar

@Composable
fun SettingsScreen(navController: NavHostController) {
    var selectedItem by remember { mutableStateOf(3) } // 현재 선택된 BottomNavigation 아이템 (설정)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Settings Screen")

        Spacer(modifier = Modifier.weight(1f)) // 중간에 빈 공간 추가

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
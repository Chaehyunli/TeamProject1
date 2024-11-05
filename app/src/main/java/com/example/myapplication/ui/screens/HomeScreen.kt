// HomeScreen.kt
// 홈 화면
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
fun HomeScreen(navController: NavHostController) {
    var selectedItem by remember { mutableStateOf(0) } // 현재 선택된 BottomNavigation 아이템 (홈)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 상단에 위치할 내용
        Text("Home Screen")

        Spacer(modifier = Modifier.weight(1f)) // 중간에 빈 공간 추가

        // BottomNavigationBar를 하단에 배치
        BottomNavigationBar(
            modifier = Modifier
                .fillMaxWidth()
                .height(62.dp),
            selectedItem = selectedItem,
            onItemSelected = { selectedItem = it },
            navController = navController
        )
    }
}

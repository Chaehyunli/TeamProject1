// MonthlyTransportScreen.kt
package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.myapplication.ui.components.BottomNavigationBar

@Composable
fun MonthlyTransportScreen(navController: NavHostController) {
    var selectedItem by remember { mutableStateOf(2) } // 현재 선택된 BottomNavigation 아이템 (이 달의 교통비)

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
            Text("Monthly Transport Screen")

            Spacer(modifier = Modifier.weight(1f)) // 중간에 빈 공간 추가
        }
    }
}

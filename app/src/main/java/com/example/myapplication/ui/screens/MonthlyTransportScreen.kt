package com.example.myapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.myapplication.ui.components.BottomNavigationBar
import com.example.myapplication.ui.components.SearchBar

@Composable
fun HomeScreen(navController: NavHostController) {
    var searchText by remember { mutableStateOf(TextFieldValue("")) }
    var selectedItem by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 검색바 UI
        SearchBar(
            searchText = searchText,
            onSearchTextChange = { searchText = it },
            onSearchClick = { /* 검색 기능을 구현할 수 있습니다 */ }
        )

        // 지하철 노선도 UI
        SubwayMap()

        // 하단 네비게이션 바
        BottomNavigationBar(
            selectedItem = selectedItem,
            onItemSelected = { selectedItem = it },
            navController = navController
        )
    }
}

@Composable
fun SubwayMap() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
            .background(Color.LightGray), // 실제 노선도 UI로 변경 필요
        contentAlignment = Alignment.Center
    ) {
        Text("지하철 노선도", color = Color.DarkGray) // 노선도 이미지 또는 커스텀 UI로 교체 가능
    }
}

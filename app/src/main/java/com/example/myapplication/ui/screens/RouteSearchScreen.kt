package com.example.myapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.components.SearchInputField
import com.example.myapplication.ui.components.SearchButton
import com.example.myapplication.ui.components.SortButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("길찾기", fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "뒤로가기")
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color(0xFFD0D3DC))
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 출발지와 도착지 입력 필드
                SearchInputField()

                Spacer(modifier = Modifier.height(16.dp))

                // 정렬 버튼과 검색 버튼
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SortButton()
                    SearchButton()
                }

                Spacer(modifier = Modifier.height(48.dp))

                // 안내 메시지와 아이콘
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text("출발지와 목적지를 입력하세요.", color = Color.Gray, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Icon(
                        imageVector = Icons.Default.ArrowBack, // 아이콘을 대체해야 함
                        contentDescription = "지도 아이콘",
                        modifier = Modifier.size(100.dp),
                        tint = Color.Gray
                    )
                }
            }
        }
    )
}

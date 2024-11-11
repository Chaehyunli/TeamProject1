// RouteDetailScreen.kt
package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class) // 실험적 API 사용 경고 무시
@Composable
fun RouteDetailScreen(
    routeId: String, // 전달받은 routeId
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("경로 상세 - $routeId", fontSize = 20.sp) }, // routeId 표시
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "뒤로가기")
                    }
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                // routeId를 이용해 데이터 표시 (추후 실제 데이터 바인딩)
                Text("여기에 경로 ${routeId}의 상세 정보가 표시됩니다.", fontSize = 18.sp)
            }
        }
    )
}

// TermsScreen.kt
package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TermsScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "이용약관", fontSize = 24.sp, color = Color.Black) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "뒤로가기")
                    }
                },
                modifier = Modifier
                    .border(1.dp, Color.LightGray)
                    .background(Color.White),
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.White)
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .border(1.dp, Color.LightGray)
                    .background(Color.White)
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = """
                        
                        이 프로그램은 명지대학교 2024 팀프로젝트1 
                        1조 알로항이 제작한 어플리케이션 입니다.

                        
                        팀원명단
                        
                        60212232 임채현
                        60222090 남서현
                        60222119 이선민
                        60232015 배수하
                    """.trimIndent(),
                    fontSize = 16.sp,
                    color = Color.Black
                )
            }
        }
    )
}

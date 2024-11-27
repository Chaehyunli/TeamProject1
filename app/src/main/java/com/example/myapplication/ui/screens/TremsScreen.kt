// TermsScreen.kt
package com.example.myapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
                title = { Text(text = "이용약관", fontSize = 18.sp, color = Color(0xFF252f42)) },
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
                    .verticalScroll(rememberScrollState()) // 스크롤 가능하도록 수정
                    .border(1.dp, Color.LightGray)
                    .background(Color.White)
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = """
Metro Mapping 서비스 이용약관

제1조 (목적)

본 약관은 Metro Mapping(이하 "서비스")에서 제공하는 기능과 관련하여, 회사와 사용자 간의 권리, 의무 및 책임 사항을 규정함을 목적으로 합니다.

제2조 (용어의 정의)

1. "서비스"란 Metro Mapping 애플리케이션이 제공하는 역 및 호선 검색, 경로 탐색, 월별 교통비 계산, 약속 장소 추천 기능 등을 의미합니다.
2. "사용자"란 본 서비스를 이용하는 개인 또는 단체를 의미합니다.

제3조 (약관의 효력 및 변경)

1. 본 약관은 서비스 화면에 게시하거나 기타의 방법으로 사용자에게 공지함으로써 효력을 발생합니다.
2. 서비스는 필요 시 본 약관을 변경할 수 있으며, 변경된 약관은 공지 후 효력이 발생합니다.

제4조 (서비스 이용)

1. 사용자는 본 약관에 동의함으로써 서비스를 이용할 수 있습니다.
2. 본 서비스는 제공되는 기능(역/호선 검색, 경로 탐색, 월별 교통비 계산 등)을 통해 사용자 편의를 제공합니다.

제5조 (개인정보 보호)

1. 서비스는 사용자의 개인정보를 관련 법령에 따라 보호합니다.
2. 개인정보는 서비스 제공 및 개선 목적으로만 사용되며, 동의 없이 제3자에게 제공되지 않습니다.

제6조 (사용자의 의무)

1. 사용자는 본 약관 및 관련 법령을 준수하며, 서비스의 정상적인 운영을 방해하지 않아야 합니다.
2. 부정한 방법으로 서비스를 이용하거나, 타인의 정보를 도용해서는 안 됩니다.

제7조 (서비스의 변경 및 중단)

1. 서비스는 운영상 필요한 경우 서비스의 전부 또는 일부를 변경하거나 중단할 수 있습니다.
2. 서비스 중단 시 사전에 공지하며, 부득이한 사유로 인한 경우 사후에 공지할 수 있습니다.

제8조 (책임 제한)

1. 서비스는 사용자에게 제공되는 경로 및 추천 정보의 정확성과 신뢰성을 위해 최선을 다합니다.
2. 그러나 경로 탐색 결과의 정확성, 통신 장애, 시스템 오류 등에 대해 법적 책임을 지지 않습니다.

제9조 (기타)

1. 본 약관에 명시되지 않은 사항은 관계 법령 및 상관례에 따릅니다.
2. 서비스와 관련하여 발생한 분쟁은 대한민국 법령에 따라 해결합니다.

본 약관은 2024년 11월 28일부터 시행됩니다.

                    """.trimIndent(),
                    fontSize = 16.sp,
                    color = Color.Black
                )
            }
        }
    )
}

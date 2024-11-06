package com.example.myapplication.ui.screens
// 아이콘 생성 아직 못했음, 노선도에 할려면 위치 정보 필요함.
// 근데 너무 복잡함. 하다가 망할 것 같아서 일단 뺏음
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.myapplication.R
import com.example.myapplication.ui.components.BottomNavigationBar

@OptIn(ExperimentalMaterial3Api::class) // 이거 실험 버전 경고 뜨는 거 무시허는 구문, 앱 실행에는 아무런 문제 없음.
@Composable
fun MeetingPlaceResultScreen(navController: NavHostController) {
    var selectedItem by remember { mutableStateOf(1) } // 현재 선택된 BottomNavigation 아이템 (약속장소 추천)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "약속장소 결과 보기",
                        fontSize = 20.sp,
                        color = Color.Black,
                        modifier = Modifier.fillMaxWidth(),
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "뒤로가기",
                            tint = Color(0xFF252F42)
                        )
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color.White // TopAppBar 배경색을 흰색으로 설정
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(4.dp) // 그림자 추가
            )
        },
        bottomBar = {
            BottomNavigationBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(62.dp),
                selectedItem = selectedItem,
                onItemSelected = { item -> selectedItem = item },
                navController = navController
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // 지도 이미지
            Image(
                painter = painterResource(id = R.drawable.map_image),
                contentDescription = "Map",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

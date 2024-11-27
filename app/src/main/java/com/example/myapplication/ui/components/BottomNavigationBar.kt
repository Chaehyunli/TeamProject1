// BottomNavigation.kt
package com.example.myapplication.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.ui.navigation.Screen

@Composable
fun BottomNavigationBar(
    modifier: Modifier = Modifier,
    selectedItem: Int,
    onItemSelected: (Int) -> Unit,
    navController: NavController
) {
    NavigationBar(
        containerColor = Color(0xFFFFFFFF),
        modifier = modifier
            .height(76.dp) // 컴포넌트의 높이 조정
            .shadow(16.dp, shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
        contentColor = Color(0xFF808590)
    ) {
        val items = listOf(
            Triple(Icons.Default.Home, Screen.Home.route, "홈"),
            Triple(Icons.Default.Place, Screen.MeetingPlace.route, "약속장소 추천"),
            Triple(Icons.Default.Receipt, Screen.MonthlyTransport.route, "이 달의 교통비"),
            Triple(Icons.Default.Settings, Screen.Settings.route, "설정")
        )

        items.forEachIndexed { index, (icon, route, label) ->
            NavigationBarItem(
                selected = selectedItem == index,
                onClick = {
                    onItemSelected(index)
                    navController.navigate(route)
                },
                icon = {
                    Column(
                        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
                        modifier = Modifier.padding(top = 4.dp) // 아이콘과 텍스트 간격 줄이기
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.height(0.dp)) // 아이콘과 텍스트 간격
                        Text(
                            text = label,
                            fontSize = 10.sp,
                            color = if (selectedItem == index) Color(0xFF252F42) else Color(0xFF808590),
                            maxLines = 1
                        )
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF252F42),
                    unselectedIconColor = Color(0xFF808590),
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}

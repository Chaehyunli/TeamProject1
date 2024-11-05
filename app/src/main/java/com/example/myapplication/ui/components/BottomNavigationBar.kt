package com.example.myapplication.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

import com.example.myapplication.ui.navigation.Screen // 경로 참조

@Composable
fun BottomNavigationBar(
    modifier: Modifier = Modifier,
    selectedItem: Int,
    onItemSelected: (Int) -> Unit,
    navController: NavController // NavController 추가
) {
    NavigationBar(
        containerColor = Color(0xFFFFFFFF),
        modifier = modifier,
        contentColor = Color(0xFF808590)
    ) {
        val items = listOf(
            Icons.Default.Home to Screen.Home.route,
            Icons.Default.Place to Screen.MeetingPlace.route,
            Icons.Default.Receipt to Screen.MonthlyTransport.route,
            Icons.Default.Settings to Screen.Settings.route
        )

        items.forEachIndexed { index, (icon, route) ->
            NavigationBarItem(
                icon = { Icon(imageVector = icon, contentDescription = null) },
                selected = selectedItem == index,
                onClick = {
                    onItemSelected(index)
                    navController.navigate(route) // 해당 스크린으로 이동
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF252F42),
                    unselectedIconColor = Color(0xFF808590)
                )
            )
        }
    }
}
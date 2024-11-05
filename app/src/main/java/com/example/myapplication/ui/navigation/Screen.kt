// Screen.kt
package com.example.myapplication.ui.navigation

// 네비게이션을 위한 스크린 라우트 정의
sealed class Screen(val route: String) {
    object Home : Screen("home")
    object MeetingPlace : Screen("meeting_place")
    object MonthlyTransport : Screen("monthly_transport")
    object Settings : Screen("settings")
}

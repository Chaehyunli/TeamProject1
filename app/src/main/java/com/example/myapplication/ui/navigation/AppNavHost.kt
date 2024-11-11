// AppNavHost.kt
package com.example.myapplication.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.example.myapplication.ui.screens.*

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController, startDestination = Screen.Home.route) {
        // 홈 화면
        composable(Screen.Home.route) { HomeScreen(navController) }

        // 역 세부 화면, stationNumber 경로 매개변수 추가
        composable("stationDetail/{stationNumber}") { backStackEntry ->
            val stationNumber = backStackEntry.arguments?.getString("stationNumber") ?: ""
            StationDetailScreen(
                navController = navController, // NavController 전달
                stationName = stationNumber,
                onBack = { navController.popBackStack() }
            )
        }

        // 길찾기 화면, startStation과 endStation 쿼리 매개변수 설정
        composable(
            route = "routeSearch?startStation={startStation}&endStation={endStation}",
            arguments = listOf(
                navArgument("startStation") { type = NavType.StringType; defaultValue = "" },
                navArgument("endStation") { type = NavType.StringType; defaultValue = "" }
            )
        ) { backStackEntry ->
            RouteSearchScreen(
                navBackStackEntry = backStackEntry, // NavBackStackEntry를 전달
                onBack = { navController.popBackStack() }
            )
        }

        // MeetingPlace 및 MonthlyTransport 등 기타 화면 추가
        composable(Screen.MeetingPlace.route) { MeetingPlaceScreen(navController) }
        composable(Screen.MonthlyTransport.route) { MonthlyTransportScreen(navController) }
        composable(Screen.Settings.route) { SettingsScreen(navController) }

        // MeetingPlaceResult 화면, result와 inputFields 전달
        composable("meeting_place_result/{result}/{inputFields}") { backStackEntry ->
            val resultString = backStackEntry.arguments?.getString("result")
            val inputFieldsString = backStackEntry.arguments?.getString("inputFields")

            if (resultString != null && inputFieldsString != null) {
                // resultString을 파싱하여 MeetingPlaceResult 객체 생성
                val resultParts = resultString.split(",")
                val bestStation = resultParts[0].toInt()
                val timesFromStartStations = resultParts.drop(1).map { it.toInt() }

                val meetingPlaceResult = MeetingPlaceResult(
                    bestStation = bestStation,
                    timesFromStartStations = timesFromStartStations
                )

                // inputFieldsString을 파싱하여 출발지 목록 생성
                val inputFields = inputFieldsString.split(",")

                MeetingPlaceResultScreen(navController, meetingPlaceResult, inputFields)
            }
        }
    }
}


package com.example.myapplication.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.myapplication.ui.screens.*

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) { HomeScreen(navController) }
        composable("home") { HomeScreen(navController) }
        composable("stationDetail/{stationNumber}") { backStackEntry ->
            val stationNumber = backStackEntry.arguments?.getString("stationNumber") ?: ""
            StationDetailScreen(stationName = stationNumber, onBack = { navController.popBackStack() })
        }
        composable(Screen.MeetingPlace.route) { MeetingPlaceScreen(navController) }
        composable(Screen.MonthlyTransport.route) { MonthlyTransportScreen(navController) }
        composable(Screen.Settings.route) { SettingsScreen(navController) }
        composable("routeSearch") { RouteSearchScreen(onBack = { navController.popBackStack() }) }

        // result와 inputFields 전달
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

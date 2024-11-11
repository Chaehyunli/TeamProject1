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

        // 역 세부 화면
        composable("stationDetail/{stationNumber}") { backStackEntry ->
            val stationNumber = backStackEntry.arguments?.getString("stationNumber") ?: ""
            StationDetailScreen(
                navController = navController,
                stationName = stationNumber,
                onBack = { navController.popBackStack() }
            )
        }

        // 길찾기 화면
        composable(
            route = "routeSearch?startStation={startStation}&endStation={endStation}",
            arguments = listOf(
                navArgument("startStation") { type = NavType.StringType; defaultValue = "" },
                navArgument("endStation") { type = NavType.StringType; defaultValue = "" }
            )
        ) { backStackEntry ->
            RouteSearchScreen(
                navBackStackEntry = backStackEntry,
                navController = navController, // NavController 전달
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = "routeDetail/{path}/{criteria}/{time}/{distance}/{transfers}/{cost}",
            arguments = listOf(
                navArgument("path") { type = NavType.StringType },
                navArgument("criteria") { type = NavType.StringType },
                navArgument("time") { type = NavType.IntType },
                navArgument("distance") { type = NavType.IntType },
                navArgument("transfers") { type = NavType.IntType },
                navArgument("cost") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            // 전달받은 매개변수들을 추출하여 RouteDetailScreen으로 전달
            val path = backStackEntry.arguments?.getString("path")?.split(",") ?: emptyList()
            val criteria = backStackEntry.arguments?.getString("criteria")?.split(",") ?: emptyList()
            val time = backStackEntry.arguments?.getInt("time") ?: 0
            val distance = backStackEntry.arguments?.getInt("distance") ?: 0
            val transfers = backStackEntry.arguments?.getInt("transfers") ?: 0
            val cost = backStackEntry.arguments?.getInt("cost") ?: 0

            RouteDetailScreen(
                path = path,
                criteria = criteria,
                time = time,
                distance = distance,
                transfers = transfers,
                cost = cost,
                onBack = { navController.popBackStack() }
            )
        }

        // 기타 화면들
        composable(Screen.MeetingPlace.route) { MeetingPlaceScreen(navController) }
        composable(Screen.MonthlyTransport.route) { MonthlyTransportScreen(navController) }
        composable(Screen.Settings.route) { SettingsScreen(navController) }

        // MeetingPlaceResult 화면
        composable("meeting_place_result/{result}/{inputFields}") { backStackEntry ->
            val resultString = backStackEntry.arguments?.getString("result")
            val inputFieldsString = backStackEntry.arguments?.getString("inputFields")

            if (resultString != null && inputFieldsString != null) {
                val resultParts = resultString.split(",")
                val bestStation = resultParts[0].toInt()
                val timesFromStartStations = resultParts.drop(1).map { it.toInt() }

                val meetingPlaceResult = MeetingPlaceResult(
                    bestStation = bestStation,
                    timesFromStartStations = timesFromStartStations
                )

                val inputFields = inputFieldsString.split(",")

                MeetingPlaceResultScreen(navController, meetingPlaceResult, inputFields)
            }
        }
    }
}

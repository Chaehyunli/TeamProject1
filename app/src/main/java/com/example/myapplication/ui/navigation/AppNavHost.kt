// AppNavHost.kt
package com.example.myapplication.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.example.myapplication.ui.screens.*
import com.example.myapplication.ui.viewmodel.MonthlyTransportViewModel
import com.example.myapplication.ui.viewmodel.RouteDetailViewModel

@Composable
fun AppNavHost(navController: NavHostController) {
    // 앱의 네비게이션 경로 (startDestination: 초기 화면 설정)
    NavHost(navController, startDestination = Screen.Home.route) {
        // 홈 화면 (HomeScreen.kt)
        composable(Screen.Home.route) { HomeScreen(navController) }

        // 지도에서 역 클릭 했을 때 역 세부 화면 (StationDetailScreen.kt)
        composable("stationDetail/{stationNumber}") { backStackEntry ->
            val stationNumber = backStackEntry.arguments?.getString("stationNumber") ?: ""
            StationDetailScreen(
                navController = navController,
                stationName = stationNumber,
                onBack = { navController.popBackStack() }
            )
        }

        // 길찾기 화면 (RouteSearchScreen.kt)
        composable(
            route = "routeSearch?startStation={startStation}&endStation={endStation}",
            arguments = listOf(
                navArgument("startStation") { type = NavType.StringType; defaultValue = "" },
                navArgument("endStation") { type = NavType.StringType; defaultValue = "" }
            )
        ) { backStackEntry ->
            RouteSearchScreen(
                navBackStackEntry = backStackEntry, // 백스택 항목 전달
                navController = navController, // NavController 전달
                onBack = { navController.popBackStack() } // 뒤로가기 처리
            )
        }

        // 길찾기 결과 화면에서 경로 클릭하면 나오는 상세 화면 (RouteDetailScreen.kt)
        composable(
            route = "routeDetail/{path}/{transferStations}/{time}/{distance}/{transfers}/{cost}/{lineNumbers}",
            arguments = listOf(
                navArgument("path") { type = NavType.StringType }, // 경로 정보
                navArgument("transferStations") { type = NavType.StringType },  // 환승역
                navArgument("time") { type = NavType.IntType }, // 소요 시간
                navArgument("distance") { type = NavType.IntType }, // 거리
                navArgument("transfers") { type = NavType.IntType }, // 환승 횟수
                navArgument("cost") { type = NavType.IntType }, // 비용
                navArgument("lineNumbers") { type = NavType.StringType } // 노선 번호
            )
        ) { backStackEntry ->
            val viewModel: RouteDetailViewModel = viewModel() // RouteDetailViewModel 생성

            // 전달된 매개변수를 파싱하여 화면에 전달
            val path = backStackEntry.arguments?.getString("path")?.split(",") ?: emptyList()
            val transferStations = backStackEntry.arguments?.getString("transferStations")?.split(",") ?: emptyList()
            val time = backStackEntry.arguments?.getInt("time") ?: 0
            val distance = backStackEntry.arguments?.getInt("distance") ?: 0
            val transfers = backStackEntry.arguments?.getInt("transfers") ?: 0
            val cost = backStackEntry.arguments?.getInt("cost") ?: 0
            val lineNumbers = backStackEntry.arguments?.getString("lineNumbers")
                ?.split(",")?.mapNotNull { it.toIntOrNull() } ?: emptyList()

            RouteDetailScreen(
                path = path,
                transferStations = transferStations,
                time = time,
                distance = distance,
                transfers = transfers,
                cost = cost,
                lineNumbers = lineNumbers, // 검증된 lineNumbers 전달
                onBack = { navController.popBackStack() },
                viewModel = viewModel // ViewModel 전달
            )
        }

        // 이 달의 교통비 화면 (MonthlyTransportScreen.kt)
        composable(Screen.MonthlyTransport.route) {
            // MonthlyTransportViewModel 생성 후 전달
            val monthlyTransportViewModel: MonthlyTransportViewModel = viewModel() // MonthlyTransportViewModel 생성
            MonthlyTransportScreen(
                navController = navController,
                viewModel = monthlyTransportViewModel // ViewModel 전달
            )
        }

        // 약속장소 추천 화면 (MeetingPlaceScreen.kt)
        composable(Screen.MeetingPlace.route) { MeetingPlaceScreen(navController) }
        
        // 설정 화면 (SettingScreen.kt)
        composable(Screen.Settings.route) { SettingsScreen(navController) }

        // 약속장소 추천 결과 화면 (MeetingPlaceResultScreen.kt)
        composable("meeting_place_result/{result}/{inputFields}") { backStackEntry ->
            val resultString = backStackEntry.arguments?.getString("result") // 결과 데이터
            val inputFieldsString = backStackEntry.arguments?.getString("inputFields") // 입력 필드 데이터

            if (resultString != null && inputFieldsString != null) {
                val resultParts = resultString.split(",")
                val bestStation = resultParts[0].toInt() // 추천된 최적 역
                val timesFromStartStations = resultParts.drop(1).map { it.toInt() } // 출발역에서의 소요 시간

                val meetingPlaceResult = MeetingPlaceResult(
                    bestStation = bestStation,
                    timesFromStartStations = timesFromStartStations
                )

                val inputFields = inputFieldsString.split(",")

                MeetingPlaceResultScreen(navController, meetingPlaceResult, inputFields)
            }
        }

        // Settings 이용약관 화면 추가 (TremsScreen.kt)
        composable("terms") {
            TermsScreen(navController = navController)
        }
    }
}


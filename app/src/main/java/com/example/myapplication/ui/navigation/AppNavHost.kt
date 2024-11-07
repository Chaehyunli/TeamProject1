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
        composable("stationDetail") { StationDetailScreen(stationName = "903역",onBack = { navController.popBackStack()} )}
        composable(Screen.MeetingPlace.route) { MeetingPlaceScreen(navController) }
        composable(Screen.MonthlyTransport.route) { MonthlyTransportScreen(navController) }
        composable(Screen.Settings.route) { SettingsScreen(navController) }

        composable(Screen.MeetingPlaceResult.route) { MeetingPlaceResultScreen(navController) }
    }
}

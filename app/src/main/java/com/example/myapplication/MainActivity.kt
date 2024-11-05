// MainActivity.kt
package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ui.components.BottomNavigationBar
import com.example.myapplication.ui.navigation.AppNavHost
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                val navController = rememberNavController() // NavHostController 얻기
                var selectedItem by remember { mutableStateOf(0) }

                Column(modifier = Modifier.fillMaxSize()) {
                    // 네비게이션 호스트
                    AppNavHost(navController = navController)

                    // 하단 네비게이션 바
                    BottomNavigationBar(
                        selectedItem = selectedItem,
                        onItemSelected = { selectedItem = it },
                        navController = navController,
                    )
                }
            }
        }
    }
}

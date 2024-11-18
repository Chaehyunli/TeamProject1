// MainActivity.kt
package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ui.navigation.AppNavHost
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.ui.viewmodel.SampleDataViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// val sampleDataViewModel: SampleDataViewModel = viewModel()
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                var isLoading by remember { mutableStateOf(true) } // 초기 로딩 상태
                val navController = rememberNavController()

                // 데이터 초기화 (비동기 처리)
                LaunchedEffect(Unit) {
                    withContext(Dispatchers.IO) {
                        // 데이터 초기화 작업
                        SubwayGraphInstance.initialize(this@MainActivity)
                        SubwayMapDataInstance.initialize(this@MainActivity)
                        delay(500) // UI 전환의 부드러움을 위해 약간의 지연 추가
                    }
                    isLoading = false
                }

                if (isLoading) {
                    // 로딩 화면 표시
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    // 메인 화면 표시
                    AppNavHost(navController = navController)
                }
            }
        }
    }
}

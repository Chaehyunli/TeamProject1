// SplashActivity.kt
package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.MainActivity
import com.example.myapplication.ui.viewmodel.SampleDataViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SplashActivity : AppCompatActivity() {
    // SampleDataViewModel 초기화
    private val sampleDataViewModel: SampleDataViewModel by viewModels()

    // 액티비티 생성 시 호출되는 함수
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash) // 스플래시 화면 레이아웃 설정

        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                // 백그라운드에서 데이터 초기화
                if (!SubwayGraphInstance.isInitialized) {
                    SubwayGraphInstance.initialize(this@SplashActivity) // 지하철 그래프 초기화
                }
                if (!SubwayMapDataInstance.isInitialized) {
                    SubwayMapDataInstance.initialize(this@SplashActivity) // 지하철 지도 데이터 초기화
                }
            }
            withContext(Dispatchers.Main) {
                // sample 데이터 로드 잘 됐는지 확인하기 위해 명시적으로 sampleDataViewModel 접근
                sampleDataViewModel.apply {
                    //
                }
                startMainActivity()
            }
        }

    }

    // 메인 액티비티로 전환하는 함수
    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java) // MainActivity로 전환하기 위한 Intent 생성
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }
}

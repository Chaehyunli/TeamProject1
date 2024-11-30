// SplashActivity.kt
package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SplashActivity : AppCompatActivity() {

    // 액티비티 생성 시 호출되는 함수
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash) // 스플래시 화면 레이아웃 설정

        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                // 백그라운드에서 데이터 초기화
                SubwayGraphInstance.initialize(this@SplashActivity) // 지하철 그래프 초기화
                SubwayMapDataInstance.initialize(this@SplashActivity) // 지하철 지도 데이터 초기화
            }
            withContext(Dispatchers.Main) {
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

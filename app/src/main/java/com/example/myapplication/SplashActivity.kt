// SplashActivity.kt
package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.MainActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // 비동기로 데이터 초기화
        Thread {
            SubwayGraphInstance.initialize(this)  // 초기화 작업
            runOnUiThread {
                // 데이터 로드가 완료되면 MainActivity로 전환
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish() // SplashActivity 종료하여 스택에서 제거
            }
        }.start()
    }
}

// SampleDataViewModel.kt
package com.example.myapplication.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.TransportCostPreferences
import kotlinx.coroutines.launch

class SampleDataViewModel(application: Application) : AndroidViewModel(application) {
    private val context = application
    private var isInitialized = false // 상태 플래그 추가

    init {
        if (!isInitialized) {
            viewModelScope.launch {
                TransportCostPreferences.setCost(context, 50000, 2024, 8)
                TransportCostPreferences.setCost(context, 55000, 2024, 9)
                TransportCostPreferences.setCost(context, 60000, 2024, 10)
                TransportCostPreferences.setCost(context, 24500, 2024, 11)
                isInitialized = true
            }
            Log.d("SampleDataViewModel", "sample 데이터 초기화 완료")
        }
    }
}

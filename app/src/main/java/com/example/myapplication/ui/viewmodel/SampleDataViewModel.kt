// src/main/java/com/example/myapplication/ui/viewmodel/SampleDataViewModel.kt
package com.example.myapplication.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.TransportCostPreferences
import kotlinx.coroutines.launch

class SampleDataViewModel(application: Application) : AndroidViewModel(application) {
    private val context = application

    init {
        // 8월, 9월, 10월 교통비 임의로 저장
        viewModelScope.launch {
            TransportCostPreferences.setCost(context, 50000, 2024, 8)  // 8월 교통비 50000원
            Log.d("SampleDataViewModel", "8월 교통비 저장: 50000원")

            TransportCostPreferences.setCost(context, 55000, 2024, 9)  // 9월 교통비 55000원
            Log.d("SampleDataViewModel", "9월 교통비 저장: 55000원")

            TransportCostPreferences.setCost(context, 60000, 2024, 10) // 10월 교통비 60000원
            Log.d("SampleDataViewModel", "10월 교통비 저장: 60000원")
        }
    }
}

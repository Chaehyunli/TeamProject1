// RouteDetailViewModel.kt
package com.example.myapplication.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.TransportCostPreferences
import kotlinx.coroutines.launch
import java.util.Calendar

class RouteDetailViewModel(application: Application) : AndroidViewModel(application) {
    private val context = application

    // 월별 교통비 더하기 함수
    fun addCost(additionalCost: Int) {
        viewModelScope.launch {
            TransportCostPreferences.addCost(context, additionalCost)
        }
    }

    // 월별 교통비를 새 값으로 설정하는 함수 (초기화 용도)
    fun setCost(newCost: Int) {
        viewModelScope.launch {
            TransportCostPreferences.setCost(context, newCost)
        }
    }
}

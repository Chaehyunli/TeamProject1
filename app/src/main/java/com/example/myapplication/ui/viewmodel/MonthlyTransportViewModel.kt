// MonthlyTransportViewModel.kt
package com.example.myapplication.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.TransportCostPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Calendar

class MonthlyTransportViewModel(application: Application) : AndroidViewModel(application) {
    private val context = application

    // 월별 교통비 데이터를 저장할 StateFlow
    private val _monthlyCosts = MutableStateFlow<Map<Int, Int>>(emptyMap())
    val monthlyCosts: StateFlow<Map<Int, Int>> get() = _monthlyCosts

    init {
        loadRecentMonthsCosts()
    }

    // 현재 달 포함 최근 4개월의 교통비를 불러오는 함수
    private fun loadRecentMonthsCosts() {
        viewModelScope.launch {
            val costs = mutableMapOf<Int, Int>()
            val calendar = Calendar.getInstance()

            // 현재 달 포함 최근 4개월의 데이터 가져오기
            for (i in 0 until 4) {
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH) + 1 // Calendar.MONTH는 0부터 시작하므로 +1

                // `first()`를 사용하여 값을 직접 가져옴
                val cost = TransportCostPreferences.getCost(context, year, month).first()
                costs[month] = cost
                Log.d("MonthlyTransportViewModel", "${month}월 교통비 불러오기: ${cost}원")

                // 이전 달로 이동
                calendar.add(Calendar.MONTH, -1)
            }

            // 모든 데이터를 수집한 후 한 번에 StateFlow에 설정
            _monthlyCosts.value = costs.toSortedMap() // 월별로 정렬하여 저장
            Log.d("MonthlyTransportViewModel", "불러온 최근 4개월 교통비: $costs")
        }
    }
}

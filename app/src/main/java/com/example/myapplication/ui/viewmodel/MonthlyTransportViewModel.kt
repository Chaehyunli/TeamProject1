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

// 월별 교통비를 관리하는 ViewModel
class MonthlyTransportViewModel(application: Application) : AndroidViewModel(application) {
    private val context = application

    // 월별 교통비 데이터를 저장할 StateFlow
    private val _monthlyCosts = MutableStateFlow<Map<Int, Int>>(emptyMap())
    val monthlyCosts: StateFlow<Map<Int, Int>> get() = _monthlyCosts

    init {
        loadRecentMonthsCosts() // 초기화 시 최근 4개월 데이터 로드
    }

    // 현재 달 포함 최근 4개월의 교통비를 로드하는 함수
    private fun loadRecentMonthsCosts() {
        viewModelScope.launch {
            val costs = mutableMapOf<Int, Int>()
            val calendar = Calendar.getInstance()

            // 월 바뀌어도 이전 달 데이터 잘 넘어가는지 확인을 위한 코드 (테스트용)
            // calendar.set(Calendar.YEAR, 2024)
            // calendar.set(Calendar.MONTH, Calendar.JANUARY)

            // 현재 달 포함 최근 4개월의 데이터 가져오기
            for (i in 0 until 4) {
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH) + 1

                // 현재 월의 교통비 데이터를 가져와 Map에 저장
                val cost = TransportCostPreferences.getCost(context, year, month).first()
                costs[month] = cost
                Log.d("MonthlyTransportViewModel", "${month}월 교통비 불러오기: ${cost}원")

                // 이전 달로 이동
                calendar.add(Calendar.MONTH, -1)
            }

            // 월별 데이터를 StateFlow에 정렬된 상태로 저장
            _monthlyCosts.value = costs.toSortedMap() // 월별로 정렬하여 저장
            Log.d("MonthlyTransportViewModel", "불러온 최근 4개월 교통비: ${costs}")
        }
    }
}

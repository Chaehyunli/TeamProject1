// TransportCostPreferences.kt
package com.example.myapplication.data

import android.content.Context
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.Calendar

val Context.dataStore by preferencesDataStore("transport_costs")

object TransportCostPreferences {

    private fun getMonthlyCostKey(year: Int, month: Int): String {
        return "${year}_${month}_cost"
    }

    // 현재 연도와 월에 맞는 기본 키를 가져오는 함수
    private fun getCurrentMonthKey(): String {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        return getMonthlyCostKey(year, month)
    }

    // 월별 교통비 더하기 함수
    suspend fun addCost(context: Context, additionalCost: Int, year: Int = Calendar.getInstance().get(Calendar.YEAR), month: Int = Calendar.getInstance().get(Calendar.MONTH) + 1) {
        val key = intPreferencesKey(getMonthlyCostKey(year, month))
        val currentCost = getCost(context, year, month).first() // 현재 저장된 비용 가져오기

        context.dataStore.edit { preferences ->
            preferences[key] = currentCost + additionalCost // 기존 값에 추가 요금 더하기
        }
    }

    // 월별 교통비 설정 (초기화) 함수
    suspend fun setCost(context: Context, cost: Int, year: Int = Calendar.getInstance().get(Calendar.YEAR), month: Int = Calendar.getInstance().get(Calendar.MONTH) + 1) {
        val key = intPreferencesKey(getMonthlyCostKey(year, month))
        context.dataStore.edit { preferences ->
            preferences[key] = cost // 주어진 값으로 교체 (초기화)
        }
    }

    // 월별 교통비 가져오는 함수
    fun getCost(context: Context, year: Int = Calendar.getInstance().get(Calendar.YEAR), month: Int = Calendar.getInstance().get(Calendar.MONTH) + 1): Flow<Int> {
        val key = intPreferencesKey(getMonthlyCostKey(year, month))
        return context.dataStore.data.map { preferences ->
            preferences[key] ?: 0
        }
    }
}

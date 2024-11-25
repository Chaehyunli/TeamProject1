package com.example.myapplication.ui.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MonthlyTransportViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MonthlyTransportViewModel::class.java)) {
            return MonthlyTransportViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

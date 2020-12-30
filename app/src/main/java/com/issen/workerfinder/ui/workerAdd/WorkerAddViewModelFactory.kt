package com.issen.workerfinder.ui.workerAdd

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class WorkerAddViewModelFactory() :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WorkerAddViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WorkerAddViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
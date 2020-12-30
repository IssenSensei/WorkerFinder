package com.issen.workerfinder.ui.taskEdit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.issen.workerfinder.database.repositories.UserRepository

class TaskEditViewModelFactory() :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskEditViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TaskEditViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
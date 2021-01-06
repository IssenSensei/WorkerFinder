package com.issen.workerfinder.ui.taskWorkerPicker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.issen.workerfinder.database.repositories.UserRepository

class TaskWorkerPickerViewModelFactory(
    private val userRepository: UserRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskWorkerPickerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TaskWorkerPickerViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

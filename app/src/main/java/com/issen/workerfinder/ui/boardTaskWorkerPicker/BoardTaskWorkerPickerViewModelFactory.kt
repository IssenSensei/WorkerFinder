package com.issen.workerfinder.ui.boardTaskWorkerPicker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.issen.workerfinder.database.repositories.DashboardNotificationRepository
import com.issen.workerfinder.database.repositories.TaskRepository
import com.issen.workerfinder.database.repositories.UserRepository

class BoardTaskWorkerPickerViewModelFactory(
    private val userRepository: UserRepository,
    private val dashboardNotificationRepository: DashboardNotificationRepository,
    private val taskRepository: TaskRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BoardTaskWorkerPickerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BoardTaskWorkerPickerViewModel(userRepository, dashboardNotificationRepository, taskRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

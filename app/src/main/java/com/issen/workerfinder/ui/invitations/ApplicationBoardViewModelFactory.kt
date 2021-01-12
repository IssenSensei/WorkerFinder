package com.issen.workerfinder.ui.invitations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.issen.workerfinder.database.repositories.DashboardNotificationRepository
import com.issen.workerfinder.database.repositories.TaskRepository
import com.issen.workerfinder.database.repositories.UserRepository

class ApplicationBoardViewModelFactory(
    private val taskRepository: TaskRepository,
    private val dashboardNotificationRepository: DashboardNotificationRepository
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ApplicationBoardViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ApplicationBoardViewModel(taskRepository, dashboardNotificationRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
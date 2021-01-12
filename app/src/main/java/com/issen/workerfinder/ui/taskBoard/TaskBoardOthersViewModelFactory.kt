package com.issen.workerfinder.ui.taskBoard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.issen.workerfinder.database.repositories.DashboardNotificationRepository
import com.issen.workerfinder.database.repositories.TaskRepository

class TaskBoardOthersViewModelFactory(
    private val taskRepository: TaskRepository,
    private val dashboardNotificationRepository: DashboardNotificationRepository
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskBoardOthersViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TaskBoardOthersViewModel(taskRepository, dashboardNotificationRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
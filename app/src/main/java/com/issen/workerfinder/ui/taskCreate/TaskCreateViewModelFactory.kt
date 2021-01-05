package com.issen.workerfinder.ui.taskCreate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.issen.workerfinder.database.repositories.DashboardNotificationRepository
import com.issen.workerfinder.database.repositories.TaskPhotoRepository
import com.issen.workerfinder.database.repositories.TaskRepeatDayRepository
import com.issen.workerfinder.database.repositories.TaskRepository

class TaskCreateViewModelFactory(
    private val taskRepository: TaskRepository,
    private val taskPhotoRepository: TaskPhotoRepository,
    private val taskRepeatDayRepository: TaskRepeatDayRepository,
    private val notificationRepository: DashboardNotificationRepository
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskCreateViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TaskCreateViewModel(taskRepository, taskPhotoRepository, taskRepeatDayRepository, notificationRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
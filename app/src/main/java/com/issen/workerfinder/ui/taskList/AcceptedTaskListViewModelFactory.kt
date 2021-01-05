package com.issen.workerfinder.ui.taskList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.issen.workerfinder.database.repositories.CommentRepository
import com.issen.workerfinder.database.repositories.DashboardNotificationRepository
import com.issen.workerfinder.database.repositories.TaskRepository

class AcceptedTaskListViewModelFactory(
    private val taskRepository: TaskRepository,
    private val dashboardNotificationRepository: DashboardNotificationRepository,
    private val commentRepository: CommentRepository
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AcceptedTaskListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AcceptedTaskListViewModel(taskRepository, dashboardNotificationRepository, commentRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
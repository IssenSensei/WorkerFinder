package com.issen.workerfinder.ui.taskList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.issen.workerfinder.database.repositories.TaskRepository

class CommissionedTaskListViewModelFactory(
    private val taskRepository: TaskRepository
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CommissionedTaskListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CommissionedTaskListViewModel(taskRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
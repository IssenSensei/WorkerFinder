package com.issen.workerfinder.ui.taskBoard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.issen.workerfinder.database.repositories.TaskRepository

class TaskBoardViewModelFactory(
    private val taskRepository: TaskRepository
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskBoardViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TaskBoardViewModel(taskRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
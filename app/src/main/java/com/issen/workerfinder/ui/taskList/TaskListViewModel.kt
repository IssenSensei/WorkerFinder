package com.issen.workerfinder.ui.taskList

import android.app.Application
import androidx.lifecycle.*
import com.issen.workerfinder.database.FullTaskModel
import com.issen.workerfinder.database.TaskModel
import com.issen.workerfinder.database.TaskModelRepository
import com.issen.workerfinder.database.WorkerFinderDatabase
import com.issen.workerfinder.enums.CompletionTypes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskListViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TaskModelRepository
    val allTasks: LiveData<List<FullTaskModel>>

    init {
        val taskModelDao = WorkerFinderDatabase.getDatabase(application, viewModelScope).taskModelDao
        val taskPhotosDao = WorkerFinderDatabase.getDatabase(application, viewModelScope).taskPhotosDao
        val taskRepeatDaysDao = WorkerFinderDatabase.getDatabase(application, viewModelScope).taskRepeatDaysDao
        repository = TaskModelRepository(taskModelDao, taskPhotosDao, taskRepeatDaysDao)
        allTasks = repository.allTasks
    }


    fun insert(taskModel: TaskModel) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(taskModel)
    }

    fun completeTask(taskModel: TaskModel) {
        taskModel.completed = CompletionTypes.COMPLETED.toString()
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateTask(taskModel)
        }
    }

    fun abandonTask(taskModel: TaskModel) {
        taskModel.completed = CompletionTypes.ABANDONED.toString()
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateTask(taskModel)
        }
    }

}
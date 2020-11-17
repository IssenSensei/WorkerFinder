package com.issen.workerfinder.ui.taskList

import android.app.Application
import androidx.lifecycle.*
import com.issen.workerfinder.database.FullTaskModel
import com.issen.workerfinder.database.TaskModel
import com.issen.workerfinder.database.TaskModelRepository
import com.issen.workerfinder.database.WorkerFinderDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskListViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TaskModelRepository
    val allTasks: LiveData<List<FullTaskModel>>

    init {
        val database = WorkerFinderDatabase.getDatabase(application, viewModelScope)
        val taskModelDao = database.taskModelDao
        val taskPhotosDao = database.taskPhotosDao
        val taskRepeatDaysDao = database.taskRepeatDaysDao
        val userModelDao = database.userModelDao
        repository = TaskModelRepository(taskModelDao, taskPhotosDao, taskRepeatDaysDao, userModelDao)
        allTasks = repository.allTasks
    }


    fun insert(taskModel: TaskModel) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(taskModel)
    }

    fun completeTask(taskModel: TaskModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.completeTask(taskModel.taskId)
        }
    }

    fun abandonTask(taskModel: TaskModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.abandonTask(taskModel.taskId)
        }
    }

}
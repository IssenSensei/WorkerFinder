package com.issen.workerfinder.ui.taskList

import android.app.Application
import androidx.lifecycle.*
import com.issen.workerfinder.database.models.FullTaskModel
import com.issen.workerfinder.database.models.TaskModel
import com.issen.workerfinder.database.TaskModelRepository
import com.issen.workerfinder.database.WorkerFinderDatabase
import com.issen.workerfinder.ui.misc.TaskListFilter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskListViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TaskModelRepository
//    var taskList: LiveData<List<FullTaskModel>>

    val mediatorLiveData: MediatorLiveData<List<FullTaskModel>> = MediatorLiveData()

    init {
        val database = WorkerFinderDatabase.getDatabase(application, viewModelScope)
        val taskModelDao = database.taskModelDao
        val taskPhotosDao = database.taskPhotosDao
        val taskRepeatDaysDao = database.taskRepeatDaysDao
        val userModelDao = database.userModelDao
        repository = TaskModelRepository(taskModelDao, taskPhotosDao, taskRepeatDaysDao, userModelDao)
        mediatorLiveData.addSource(repository.allTasks) {
            mediatorLiveData.setValue(
                it
            )
        }
//        taskList = repository.allTasks
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

    fun queryDesc() {
        mediatorLiveData.addSource(repository.activeTasks) {
            mediatorLiveData.setValue(
                it
            )
        }
    }

}
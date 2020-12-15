package com.issen.workerfinder.ui.taskList

import android.app.Application
import androidx.lifecycle.*
import com.issen.workerfinder.TaskApplication
import com.issen.workerfinder.database.DashboardNotificationsRepository
import com.issen.workerfinder.database.models.TaskModelFull
import com.issen.workerfinder.database.models.TaskModel
import com.issen.workerfinder.database.TaskModelRepository
import com.issen.workerfinder.database.WorkerFinderDatabase
import com.issen.workerfinder.database.models.DashboardNotification
import com.issen.workerfinder.enums.DashboardNotificationTypes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class TaskListViewModel(application: Application) : AndroidViewModel(application) {

    private val taskModelRepository: TaskModelRepository
    private val dashboardNotificationsRepository: DashboardNotificationsRepository
//    var taskList: LiveData<List<FullTaskModel>>

    val mediatorLiveData: MediatorLiveData<List<TaskModelFull>> = MediatorLiveData()

    init {
        val database = WorkerFinderDatabase.getDatabase(application, viewModelScope)
        val taskModelDao = database.taskModelDao
        val taskPhotosDao = database.taskPhotosDao
        val taskRepeatDaysDao = database.taskRepeatDaysDao
        val userModelDao = database.userDataDao
        val commentsDao = database.commentsDao
        val dashboardNotificationDao = database.dashboardNotificationDao
        taskModelRepository = TaskModelRepository(taskModelDao, taskPhotosDao, taskRepeatDaysDao, userModelDao, commentsDao)
        dashboardNotificationsRepository = DashboardNotificationsRepository(dashboardNotificationDao)
        mediatorLiveData.addSource(taskModelRepository.allTasks) {
            mediatorLiveData.setValue(
                it
            )
        }
//        taskList = repository.allTasks
    }


    fun insert(taskModel: TaskModel) = viewModelScope.launch(Dispatchers.IO) {
        taskModelRepository.insert(taskModel)
    }

    fun markTaskAsCompleted(taskModel: TaskModel) {
        viewModelScope.launch(Dispatchers.IO) {
            taskModelRepository.markTaskAsCompleted(taskModel.taskId)
        }
    }

    fun markTaskAsPending(taskModel: TaskModel) {
        viewModelScope.launch(Dispatchers.IO) {
            taskModelRepository.markTaskAsPending(taskModel.taskId)
            dashboardNotificationsRepository.notify(
                DashboardNotification(
                    0,
                    Date().toString(),
                    taskModel.userFirebaseKey,
                    TaskApplication.currentLoggedInUserFull!!.userData.userId,
                    DashboardNotificationTypes.TASKCOMPLETED.toString(),
                    taskModel.taskId,
                    false
                )
            )
        }
    }

    fun queryDesc() {
        mediatorLiveData.addSource(taskModelRepository.activeTasks) {
            mediatorLiveData.setValue(
                it
            )
        }
    }

}
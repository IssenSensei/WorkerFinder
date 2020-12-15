package com.issen.workerfinder.ui.taskDetail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.issen.workerfinder.TaskApplication
import com.issen.workerfinder.database.DashboardNotificationsRepository
import com.issen.workerfinder.database.TaskModelRepository
import com.issen.workerfinder.database.WorkerFinderDatabase
import com.issen.workerfinder.database.models.DashboardNotification
import com.issen.workerfinder.database.models.TaskModelFull
import com.issen.workerfinder.enums.DashboardNotificationTypes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class TaskDetailViewModel(application: Application) : AndroidViewModel(application) {

    private val taskModelRepository: TaskModelRepository
    private val dashboardNotificationsRepository: DashboardNotificationsRepository

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
    }

    fun abandonTask(taskModel: TaskModelFull) {
        viewModelScope.launch(Dispatchers.IO) {
            taskModelRepository.abandonTask(taskModel.task.taskId)
            dashboardNotificationsRepository.notify(
                DashboardNotification(
                    0,
                    Date().toString(),
                    taskModel.task.userFirebaseKey,
                    TaskApplication.currentLoggedInUserFull!!.userData.userId,
                    DashboardNotificationTypes.TASKABANDONED.toString(),
                    taskModel.task.taskId,
                    false
                )
            )
        }
    }
}
package com.issen.workerfinder.ui.taskDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.issen.workerfinder.WorkerFinderApplication
import com.issen.workerfinder.database.models.DashboardNotification
import com.issen.workerfinder.database.models.TaskModelFull
import com.issen.workerfinder.database.repositories.DashboardNotificationRepository
import com.issen.workerfinder.database.repositories.TaskRepository
import com.issen.workerfinder.enums.DashboardNotificationTypes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class TaskDetailViewModel(
    private val taskRepository: TaskRepository,
    private val dashboardNotificationRepository: DashboardNotificationRepository
) : ViewModel() {

    fun abandonTask(taskModel: TaskModelFull) {
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.abandonTask(taskModel.task.taskId)
            dashboardNotificationRepository.notify(
                DashboardNotification(
                    0,
                    Date().toString(),
                    taskModel.task.userFirebaseKey,
                    WorkerFinderApplication.currentLoggedInUserFull!!.userData.userId,
                    DashboardNotificationTypes.TASKABANDONED.toString(),
                    taskModel.task.taskId,
                    false
                )
            )
        }
    }
}
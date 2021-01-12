package com.issen.workerfinder.ui.invitations

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.issen.workerfinder.WorkerFinderApplication.Companion.currentLoggedInUserFull
import com.issen.workerfinder.database.models.TaskModelFull
import com.issen.workerfinder.database.repositories.DashboardNotificationRepository
import com.issen.workerfinder.database.repositories.TaskRepository
import kotlinx.coroutines.launch

class ApplicationBoardViewModel(private val taskRepository: TaskRepository, private val dashboardNotificationRepository: DashboardNotificationRepository) : ViewModel() {

    val applicationsList: LiveData<List<TaskModelFull>> = taskRepository.getUserApplications(currentLoggedInUserFull!!.userData.userId)


    fun cancelApplication(taskModelFull: TaskModelFull) {
        viewModelScope.launch {
            dashboardNotificationRepository.cancelBoardApplicationNotification(taskModelFull.task.taskId, currentLoggedInUserFull!!.userData.userId)
        }
    }
}
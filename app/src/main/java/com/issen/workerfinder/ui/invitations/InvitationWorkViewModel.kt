package com.issen.workerfinder.ui.invitations

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.issen.workerfinder.WorkerFinderApplication.Companion.currentLoggedInUserFull
import com.issen.workerfinder.database.models.DashboardNotification
import com.issen.workerfinder.database.models.TaskModel
import com.issen.workerfinder.database.models.TaskModelFull
import com.issen.workerfinder.database.repositories.DashboardNotificationRepository
import com.issen.workerfinder.database.repositories.TaskRepository
import com.issen.workerfinder.enums.DashboardNotificationTypes
import kotlinx.coroutines.launch
import java.util.*

class InvitationWorkViewModel(private val taskRepository: TaskRepository, private val dashboardNotificationRepository: DashboardNotificationRepository) : ViewModel() {

    val invitationList: LiveData<List<TaskModelFull>> = taskRepository.getUserInvitations(currentLoggedInUserFull!!.userData.userId)

    fun cancelInvitation(task: TaskModel) {
        viewModelScope.launch {
            dashboardNotificationRepository.cancelWorkNotification(task.taskId, currentLoggedInUserFull!!.userData.userId)
            dashboardNotificationRepository.notify(
                DashboardNotification(
                    0,
                    Date().toString(),
                    currentLoggedInUserFull!!.userData.userId,
                    task.workerFirebaseKey,
                    DashboardNotificationTypes.WORKCANCELED.toString(),
                    task.taskId,
                    false
                )
            )
            taskRepository.deleteTask(task.taskId)
        }
    }
}
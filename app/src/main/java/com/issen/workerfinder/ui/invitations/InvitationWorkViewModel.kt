package com.issen.workerfinder.ui.invitations

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.issen.workerfinder.WorkerFinderApplication.Companion.currentLoggedInUserFull
import com.issen.workerfinder.database.models.TaskModelFull
import com.issen.workerfinder.database.repositories.DashboardNotificationRepository
import com.issen.workerfinder.database.repositories.TaskRepository
import kotlinx.coroutines.launch

class InvitationWorkViewModel(private val taskRepository: TaskRepository, private val dashboardNotificationRepository: DashboardNotificationRepository) : ViewModel() {

    val invitationList: LiveData<List<TaskModelFull>> = taskRepository.getUserInvitations(currentLoggedInUserFull!!.userData.userId)

    fun cancelInvitation(taskId: Int) {
        viewModelScope.launch {
            dashboardNotificationRepository.cancelNotification(taskId, currentLoggedInUserFull!!.userData.userId)
        }
    }
}
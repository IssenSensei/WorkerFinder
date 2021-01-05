package com.issen.workerfinder.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.issen.workerfinder.database.models.Contacts
import com.issen.workerfinder.database.models.DashboardNotification
import com.issen.workerfinder.database.models.DashboardNotificationFull
import com.issen.workerfinder.database.repositories.ContactRepository
import com.issen.workerfinder.database.repositories.DashboardNotificationRepository
import com.issen.workerfinder.database.repositories.TaskRepository
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val dashboardNotificationRepository: DashboardNotificationRepository,
    private val contactRepository: ContactRepository,
    private val taskRepository: TaskRepository
) : ViewModel() {

    var dashboardNotificationsList: LiveData<List<DashboardNotificationFull>> = dashboardNotificationRepository.getAllNotifications()

    fun acceptContact(dashboardNotificationFull: DashboardNotificationFull) {
        viewModelScope.launch {
            dashboardNotificationRepository.acceptContact(dashboardNotificationFull.notification.id)
            contactRepository.addContact(
                Contacts(
                    0,
                    dashboardNotificationFull.notification.notificationCausedByUserId,
                    dashboardNotificationFull.notification.notificationOwnerId
                )
            )
        }
    }

    fun refuseContact(dashboardNotificationFull: DashboardNotificationFull) {
        viewModelScope.launch {
            dashboardNotificationRepository.refuseContact(dashboardNotificationFull.notification.id)
        }
    }

    fun acceptTask(dashboardNotification: DashboardNotification) {
        viewModelScope.launch {
            dashboardNotificationRepository.acceptTask(dashboardNotification.id)
            taskRepository.markTaskAsCompleted(dashboardNotification.modifiedRecordId)
        }
    }

    fun rejectTask(dashboardNotification: DashboardNotification) {
        viewModelScope.launch {
            dashboardNotificationRepository.rejectTask(dashboardNotification.id)
            taskRepository.markTaskAsActive(dashboardNotification.modifiedRecordId)
        }
    }

    fun acceptWork(dashboardNotification: DashboardNotification) {
        viewModelScope.launch {
            dashboardNotificationRepository.acceptWork(dashboardNotification.id)
            taskRepository.acceptTask(dashboardNotification.modifiedRecordId)
        }
    }

    fun refuseWork(dashboardNotification: DashboardNotification) {
        viewModelScope.launch {
            dashboardNotificationRepository.refuseWork(dashboardNotification.id)
            taskRepository.refuseTask(dashboardNotification.modifiedRecordId)
        }
    }
}
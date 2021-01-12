package com.issen.workerfinder.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.issen.workerfinder.WorkerFinderApplication.Companion.currentLoggedInUserFull
import com.issen.workerfinder.database.models.Contacts
import com.issen.workerfinder.database.models.DashboardNotification
import com.issen.workerfinder.database.models.DashboardNotificationFull
import com.issen.workerfinder.database.repositories.ContactRepository
import com.issen.workerfinder.database.repositories.DashboardNotificationRepository
import com.issen.workerfinder.database.repositories.TaskRepository
import com.issen.workerfinder.enums.DashboardNotificationTypes
import kotlinx.coroutines.launch
import java.util.*

class DashboardViewModel(
    private val dashboardNotificationRepository: DashboardNotificationRepository,
    private val contactRepository: ContactRepository,
    private val taskRepository: TaskRepository
) : ViewModel() {

    var dashboardNotificationsList: LiveData<List<DashboardNotificationFull>> = dashboardNotificationRepository.getAllNotifications()
//    var dashboardNotificationsList: LiveData<List<DashboardNotificationFull>> = dashboardNotificationRepository.getAllUserNotifications(currentLoggedInUserFull!!.userData.userId)

    fun acceptContact(dashboardNotificationFull: DashboardNotificationFull) {
        viewModelScope.launch {
            dashboardNotificationRepository.acceptContact(dashboardNotificationFull.notification.id)
            dashboardNotificationRepository.notify(
                DashboardNotification(
                    0,
                    Date().toString(),
                    dashboardNotificationFull.userData.userId,
                    currentLoggedInUserFull!!.userData.userId,
                    DashboardNotificationTypes.CONTACTACCEPTED.toString(),
                    0,
                    false
                )
            )
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
            dashboardNotificationRepository.notify(
                DashboardNotification(
                    0,
                    Date().toString(),
                    dashboardNotificationFull.userData.userId,
                    currentLoggedInUserFull!!.userData.userId,
                    DashboardNotificationTypes.CONTACTREFUSED.toString(),
                    0,
                    false
                )
            )
        }
    }

    fun acceptTask(dashboardNotificationFull: DashboardNotificationFull) {
        viewModelScope.launch {
            dashboardNotificationRepository.acceptTask(dashboardNotificationFull.notification.id)
            dashboardNotificationRepository.notify(
                DashboardNotification(
                    0,
                    Date().toString(),
                    dashboardNotificationFull.userData.userId,
                    currentLoggedInUserFull!!.userData.userId,
                    DashboardNotificationTypes.TASKACCEPTED.toString(),
                    dashboardNotificationFull.notification.modifiedRecordId,
                    false
                )
            )
            taskRepository.markTaskAsCompleted(dashboardNotificationFull.notification.modifiedRecordId)
        }
    }

    fun rejectTask(dashboardNotificationFull: DashboardNotificationFull) {
        viewModelScope.launch {
            dashboardNotificationRepository.rejectTask(dashboardNotificationFull.notification.id)
            dashboardNotificationRepository.notify(
                DashboardNotification(
                    0,
                    Date().toString(),
                    dashboardNotificationFull.userData.userId,
                    currentLoggedInUserFull!!.userData.userId,
                    DashboardNotificationTypes.TASKREJECTED.toString(),
                    dashboardNotificationFull.notification.modifiedRecordId,
                    false
                )
            )
            taskRepository.markTaskAsActive(dashboardNotificationFull.notification.modifiedRecordId)
        }
    }

    fun acceptWork(dashboardNotificationFull: DashboardNotificationFull) {
        viewModelScope.launch {
            dashboardNotificationRepository.acceptWork(dashboardNotificationFull.notification.id)
            dashboardNotificationRepository.notify(
                DashboardNotification(
                    0,
                    Date().toString(),
                    dashboardNotificationFull.userData.userId,
                    currentLoggedInUserFull!!.userData.userId,
                    DashboardNotificationTypes.WORKACCEPTED.toString(),
                    dashboardNotificationFull.notification.modifiedRecordId,
                    false
                )
            )
            taskRepository.acceptTask(dashboardNotificationFull.notification.modifiedRecordId)
        }
    }

    fun refuseWork(dashboardNotificationFull: DashboardNotificationFull) {
        viewModelScope.launch {
            dashboardNotificationRepository.refuseWork(dashboardNotificationFull.notification.id)
            dashboardNotificationRepository.notify(
                DashboardNotification(
                    0,
                    Date().toString(),
                    dashboardNotificationFull.userData.userId,
                    currentLoggedInUserFull!!.userData.userId,
                    DashboardNotificationTypes.WORKREFUSED.toString(),
                    dashboardNotificationFull.notification.modifiedRecordId,
                    false
                )
            )
            taskRepository.refuseTask(dashboardNotificationFull.notification.modifiedRecordId)
        }
    }
}
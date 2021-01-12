package com.issen.workerfinder.ui.invitations

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.issen.workerfinder.WorkerFinderApplication.Companion.currentLoggedInUserFull
import com.issen.workerfinder.database.models.DashboardNotification
import com.issen.workerfinder.database.models.UserDataFull
import com.issen.workerfinder.database.repositories.DashboardNotificationRepository
import com.issen.workerfinder.database.repositories.UserRepository
import com.issen.workerfinder.enums.DashboardNotificationTypes
import kotlinx.coroutines.launch
import java.util.*

class InvitationContactsViewModel(
    private val userRepository: UserRepository,
    private val dashboardNotificationRepository: DashboardNotificationRepository
) : ViewModel() {

    val invitationList: LiveData<List<UserDataFull>> = userRepository.getUserInvitations(currentLoggedInUserFull!!.userData.userId)

    fun cancelInvitation(userId: String) {
        viewModelScope.launch {
            dashboardNotificationRepository.cancelContactNotification(userId, currentLoggedInUserFull!!.userData.userId)
            dashboardNotificationRepository.notify(
                DashboardNotification(
                    0,
                    Date().toString(),
                    currentLoggedInUserFull!!.userData.userId,
                    userId,
                    DashboardNotificationTypes.CONTACTCANCELED.toString(),
                    0,
                    false
                )
            )
        }
    }
}
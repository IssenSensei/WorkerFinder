package com.issen.workerfinder.ui.invitations

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.issen.workerfinder.WorkerFinderApplication.Companion.currentLoggedInUserFull
import com.issen.workerfinder.database.models.UserDataFull
import com.issen.workerfinder.database.repositories.DashboardNotificationRepository
import com.issen.workerfinder.database.repositories.UserRepository
import kotlinx.coroutines.launch

class InvitationContactsViewModel(
    private val userRepository: UserRepository,
    private val dashboardNotificationRepository: DashboardNotificationRepository
) : ViewModel() {

    val invitationList: LiveData<List<UserDataFull>> = userRepository.getUserInvitations(currentLoggedInUserFull!!.userData.userId)

    fun cancelInvitation(userId: String) {
        viewModelScope.launch {
            dashboardNotificationRepository.cancelNotification(userId, currentLoggedInUserFull!!.userData.userId)
        }
    }
}
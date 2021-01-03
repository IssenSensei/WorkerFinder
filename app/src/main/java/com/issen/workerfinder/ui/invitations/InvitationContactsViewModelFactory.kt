package com.issen.workerfinder.ui.invitations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.issen.workerfinder.database.repositories.DashboardNotificationRepository
import com.issen.workerfinder.database.repositories.UserRepository

class InvitationContactsViewModelFactory(
    private val userRepository: UserRepository,
    private val dashboardNotificationRepository: DashboardNotificationRepository
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InvitationContactsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return InvitationContactsViewModel(userRepository, dashboardNotificationRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
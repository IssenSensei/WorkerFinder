package com.issen.workerfinder.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.issen.workerfinder.database.repositories.ContactRepository
import com.issen.workerfinder.database.repositories.DashboardNotificationRepository

class WordViewModelFactory(
    private val dashboardNotificationRepository: DashboardNotificationRepository,
    private val contactRepository: ContactRepository
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DashboardViewModel(dashboardNotificationRepository, contactRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
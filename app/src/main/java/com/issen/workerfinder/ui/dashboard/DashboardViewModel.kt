package com.issen.workerfinder.ui.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.issen.workerfinder.database.repositories.ContactRepository
import com.issen.workerfinder.database.repositories.DashboardNotificationRepository
import com.issen.workerfinder.database.WorkerFinderDatabase
import com.issen.workerfinder.database.models.DashboardNotificationFull
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val dashboardNotificationRepository: DashboardNotificationRepository,
    private val contactRepository: ContactRepository
) : ViewModel() {

    var dashboardNotificationsList: LiveData<List<DashboardNotificationFull>> = dashboardNotificationRepository.getAllNotifications()

    fun acceptContact(dashboardNotificationFull: DashboardNotificationFull) {
        viewModelScope.launch {
            //notification resolved
            //notify causedBy
            //addContact
        }
    }

    fun refuseContact(dashboardNotificationFull: DashboardNotificationFull) {
        viewModelScope.launch {
            //notification resolved
            //notify causedBy
        }
    }
}
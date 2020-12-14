package com.issen.workerfinder.ui.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.issen.workerfinder.database.WorkerFinderDatabase
import com.issen.workerfinder.database.models.DashboardNotificationFull

class DashboardViewModel(application: Application) : AndroidViewModel(application) {

    var dashboardNotificationsList: LiveData<List<DashboardNotificationFull>>

    init {
        val database = WorkerFinderDatabase.getDatabase(application, viewModelScope)
//        workerList = database.contactsDao.getUserWorkers(currentLoggedInFullUser!!.userData.userId)
        dashboardNotificationsList = database.dashboardNotificationDao.getAllNotifications()
    }
}
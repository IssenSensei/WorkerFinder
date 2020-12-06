package com.issen.workerfinder.ui.workerList

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.issen.workerfinder.TaskApplication.Companion.currentLoggedInFullUser
import com.issen.workerfinder.database.WorkerFinderDatabase
import com.issen.workerfinder.database.models.FullUserData

class WorkerListViewModel(application: Application) : AndroidViewModel(application) {

    var workerList: LiveData<List<FullUserData>>

    init {
        val database = WorkerFinderDatabase.getDatabase(application, viewModelScope)
//        workerList = database.contactsDao.getUserWorkers(currentLoggedInFullUser!!.userData.userId)
        workerList = database.userDataDao.getUserWorkers(currentLoggedInFullUser!!.userData.userId)
    }

}
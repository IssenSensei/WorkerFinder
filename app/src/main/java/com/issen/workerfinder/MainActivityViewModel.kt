package com.issen.workerfinder

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.issen.workerfinder.TaskApplication.Companion.currentLoggedInUserFull
import com.issen.workerfinder.database.WorkerFinderDatabase
import com.issen.workerfinder.database.models.UserDataFull

public class MainActivityViewModel(application: Application) : AndroidViewModel(application) {

    var workerList: LiveData<List<UserDataFull>>
    var userListFull: LiveData<List<UserDataFull>>

    init {
        val database = WorkerFinderDatabase.getDatabase(application, viewModelScope)
        workerList = database.userDataDao.getUserWorkers(currentLoggedInUserFull!!.userData.userId)
        userListFull = database.userDataDao.getUserUsers(currentLoggedInUserFull!!.userData.userId)
    }

}
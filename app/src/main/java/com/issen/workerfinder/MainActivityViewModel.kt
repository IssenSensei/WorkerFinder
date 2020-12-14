package com.issen.workerfinder

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.issen.workerfinder.TaskApplication.Companion.currentLoggedInFullUser
import com.issen.workerfinder.database.WorkerFinderDatabase
import com.issen.workerfinder.database.models.FullUserData

public class MainActivityViewModel(application: Application) : AndroidViewModel(application) {

    var workerList: LiveData<List<FullUserData>>
    var userList: LiveData<List<FullUserData>>

    init {
        val database = WorkerFinderDatabase.getDatabase(application, viewModelScope)
        workerList = database.userDataDao.getUserWorkers(currentLoggedInFullUser!!.userData.userId)
        userList = database.userDataDao.getUserUsers(currentLoggedInFullUser!!.userData.userId)
    }

}
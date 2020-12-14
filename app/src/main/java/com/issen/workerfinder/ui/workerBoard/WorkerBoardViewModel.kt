package com.issen.workerfinder.ui.workerBoard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.issen.workerfinder.database.WorkerFinderDatabase
import com.issen.workerfinder.database.models.UserDataFull

class WorkerBoardViewModel(application: Application) : AndroidViewModel(application) {
    var workerList: LiveData<List<UserDataFull>>

    init {
        val database = WorkerFinderDatabase.getDatabase(application, viewModelScope)
        workerList = database.userDataDao.getBoardWorkers()
    }
}
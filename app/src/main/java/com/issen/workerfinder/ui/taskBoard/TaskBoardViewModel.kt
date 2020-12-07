package com.issen.workerfinder.ui.taskBoard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.issen.workerfinder.database.WorkerFinderDatabase
import com.issen.workerfinder.database.models.FullTaskModel

class TaskBoardViewModel(application: Application) : AndroidViewModel(application) {
    var taskList: LiveData<List<FullTaskModel>>

    init {
        val database = WorkerFinderDatabase.getDatabase(application, viewModelScope)
        taskList = database.taskModelDao.getBoardTasks()
    }
}
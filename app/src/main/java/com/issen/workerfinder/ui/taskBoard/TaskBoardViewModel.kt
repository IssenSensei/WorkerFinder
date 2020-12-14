package com.issen.workerfinder.ui.taskBoard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.issen.workerfinder.database.WorkerFinderDatabase
import com.issen.workerfinder.database.models.TaskModelFull

class TaskBoardViewModel(application: Application) : AndroidViewModel(application) {
    var taskListFull: LiveData<List<TaskModelFull>>

    init {
        val database = WorkerFinderDatabase.getDatabase(application, viewModelScope)
        taskListFull = database.taskModelDao.getBoardTasks()
    }
}
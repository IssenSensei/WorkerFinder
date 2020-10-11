package com.example.workerfinder.database

import androidx.lifecycle.LiveData

class TaskModelRepository(private val taskModelDao: TaskModelDao) {

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    val allActiveTasks: LiveData<List<TaskModel>> = taskModelDao.getActiveTasks()

    suspend fun insert(taskModel: TaskModel) {
        taskModelDao.insert(taskModel)
    }
}
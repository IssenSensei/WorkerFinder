package com.issen.workerfinder.database.repositories

import androidx.lifecycle.LiveData
import androidx.sqlite.db.SimpleSQLiteQuery
import com.issen.workerfinder.database.dao.TaskModelDao
import com.issen.workerfinder.database.models.TaskModel
import com.issen.workerfinder.database.models.TaskModelFull

class TaskRepository(private val taskModelDao: TaskModelDao) {

    val allTasks: LiveData<List<TaskModelFull>> = taskModelDao.getAllTasks()
    val activeTasks: LiveData<List<TaskModelFull>> = taskModelDao.getActiveTasks()
    fun getTasksQueried(query: SimpleSQLiteQuery): LiveData<List<TaskModelFull>> = taskModelDao.getTasksQueried(query)
    fun getBoardTasks(): LiveData<List<TaskModelFull>> = taskModelDao.getBoardTasks()

    suspend fun insert(taskModel: TaskModel) : Long {
        return taskModelDao.insert(taskModel)
    }

    suspend fun getCompletedTasks(firebaseKey: String): Int = taskModelDao.getCompletedTasksCount(firebaseKey)
    suspend fun getActiveTasks(firebaseKey: String): Int = taskModelDao.getActiveTasksCount(firebaseKey)
    suspend fun getAbandonedTasks(firebaseKey: String): Int = taskModelDao.getAbandonedTasksCount(firebaseKey)
    suspend fun markTaskAsPending(taskId: Int) = taskModelDao.markTaskAsPending(taskId)
    suspend fun markTaskAsCompleted(taskId: Int) = taskModelDao.markTaskAsCompleted(taskId)
    suspend fun abandonTask(taskId: Int) = taskModelDao.abandonTask(taskId)


}
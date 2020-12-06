package com.issen.workerfinder.database

import androidx.lifecycle.LiveData
import com.issen.workerfinder.database.dao.TaskModelDao
import com.issen.workerfinder.database.dao.TaskPhotosDao
import com.issen.workerfinder.database.dao.TaskRepeatDaysDao
import com.issen.workerfinder.database.dao.UserModelDao
import com.issen.workerfinder.database.models.*

class TaskModelRepository(
    private val taskModelDao: TaskModelDao,
    private val taskPhotosDao: TaskPhotosDao,
    private val taskRepeatDaysDao: TaskRepeatDaysDao,
    private val userModelDao: UserModelDao
) {

    val allTasks: LiveData<List<FullTaskModel>> = taskModelDao.getAllTasks()
    val activeTasks: LiveData<List<FullTaskModel>> = taskModelDao.getActiveTasks()


    suspend fun insert(taskModel: TaskModel, photos: List<TaskModelPhotos>, repeatDays: List<TaskModelRepeatDays>) {
        val taskId = taskModelDao.insert(taskModel)
        photos.forEach {
            it.taskId = taskId.toInt()
        }
        repeatDays.forEach {
            it.taskId = taskId.toInt()
        }
        taskPhotosDao.insert(photos)
        taskRepeatDaysDao.insert(repeatDays)
    }

    suspend fun insert(taskModel: TaskModel) {
        taskModelDao.insert(taskModel)
    }

    fun getUserById(firebaseKey: String): LiveData<UserModel>{
        return userModelDao.getUserById(firebaseKey)
    }

    suspend fun getCompletedTasks(firebaseKey: String): Int = taskModelDao.getCompletedTasksCount(firebaseKey)
    suspend fun getActiveTasks(firebaseKey: String): Int = taskModelDao.getActiveTasksCount(firebaseKey)
    suspend fun getAbandonedTasks(firebaseKey: String): Int = taskModelDao.getAbandonedTasksCount(firebaseKey)
    suspend fun completeTask(taskId: Int) = taskModelDao.completeTask(taskId)
    suspend fun abandonTask(taskId: Int) = taskModelDao.abandonTask(taskId)
    fun updateUser(userModel: UserModel) = userModelDao.update(userModel)

}
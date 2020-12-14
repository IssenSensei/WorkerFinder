package com.issen.workerfinder.database

import androidx.lifecycle.LiveData
import com.issen.workerfinder.database.dao.*
import com.issen.workerfinder.database.models.*

class TaskModelRepository(
    private val taskModelDao: TaskModelDao,
    private val taskPhotosDao: TaskPhotosDao,
    private val taskRepeatDaysDao: TaskRepeatDaysDao,
    private val userDataDao: UserDataDao,
    private val commentsDao: CommentsDao
) {

    val allTasks: LiveData<List<TaskModelFull>> = taskModelDao.getAllTasks()
    val activeTasks: LiveData<List<TaskModelFull>> = taskModelDao.getActiveTasks()


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

    fun getUserById(firebaseKey: String): LiveData<UserDataFull> {
        return userDataDao.getUserById(firebaseKey)
    }

    suspend fun getCompletedTasks(firebaseKey: String): Int = taskModelDao.getCompletedTasksCount(firebaseKey)
    suspend fun getActiveTasks(firebaseKey: String): Int = taskModelDao.getActiveTasksCount(firebaseKey)
    suspend fun getAbandonedTasks(firebaseKey: String): Int = taskModelDao.getAbandonedTasksCount(firebaseKey)
    suspend fun markTaskAsPending(taskId: Int) = taskModelDao.markTaskAsPending(taskId)
    suspend fun markTaskAsCompleted(taskId: Int) = taskModelDao.markTaskAsCompleted(taskId)
    suspend fun abandonTask(taskId: Int) = taskModelDao.abandonTask(taskId)
    fun updateUser(userData: UserData) = userDataDao.update(userData)
    suspend fun setAccountPublic(firebaseKey: String, public: Boolean) = userDataDao.setAccountPublic(firebaseKey, public)
    suspend fun getRatingAsWorker(userId: String) = commentsDao.getRatingAsWorker(userId)
    suspend fun getRatingAsUser(userId: String) = commentsDao.getRatingAsUser(userId)
    suspend fun getCommentUser(userId: String): List<UserDataWithComments>? = userDataDao.getCommentUser(userId)
    suspend fun getCommentWorker(userId: String): List<UserDataWithComments>? = userDataDao.getCommentWorker(userId)
}
package com.issen.workerfinder.database.repositories

import androidx.lifecycle.LiveData
import androidx.sqlite.db.SimpleSQLiteQuery
import com.issen.workerfinder.database.dao.UserDataDao
import com.issen.workerfinder.database.models.UserData
import com.issen.workerfinder.database.models.UserDataFull
import com.issen.workerfinder.database.models.UserDataWithComments

class UserRepository(private val userDataDao: UserDataDao){

    fun getUsersQueried(query: SimpleSQLiteQuery): LiveData<List<UserDataFull>> = userDataDao.getUsersQueried(query)
    fun getUserContacts(userId: String): LiveData<List<UserDataFull>> = userDataDao.getUserContacts(userId)
    fun getUserWorkers(userId: String): LiveData<List<UserDataFull>> = userDataDao.getUserWorkers(userId)
    fun getUserWorkers(userId: String, userName: String): LiveData<List<UserDataFull>> = userDataDao.getUserWorkers(userId, userName)
    fun getUserUsers(userId: String): LiveData<List<UserDataFull>> = userDataDao.getUserUsers(userId)
    fun getBoardWorkers(): LiveData<List<UserDataFull>> = userDataDao.getBoardWorkers()
    fun getUsersList(userId: String): LiveData<List<UserDataFull>> = userDataDao.getUsersList(userId)
    fun getUserInvitations(userId: String): LiveData<List<UserDataFull>> = userDataDao.gerUserInvitations(userId)

    suspend fun setAccountPublic(firebaseKey: String, public: Boolean) = userDataDao.setAccountPublic(firebaseKey, public)
    suspend fun getCommentUser(userId: String): List<UserDataWithComments>? = userDataDao.getCommentUser(userId)
    suspend fun getCommentWorker(userId: String): List<UserDataWithComments>? = userDataDao.getCommentWorker(userId)
    fun updateUser(userData: UserData) = userDataDao.update(userData)

    suspend fun getUserByFirebaseKey(userId: String): UserDataFull  = userDataDao.getUserByFirebaseKey(userId)
    suspend fun insert(userData: UserData) = userDataDao.insert(userData)
    fun getBoardTaskApplications(taskId: Int): LiveData<List<UserDataFull>> = userDataDao.getBoardTaskApplications(taskId)
    fun getBoardTaskApplications(taskId: Int, name: String): LiveData<List<UserDataFull>> = userDataDao.getBoardTaskApplications(taskId, name)


}
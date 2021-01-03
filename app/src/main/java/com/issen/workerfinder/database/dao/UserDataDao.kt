package com.issen.workerfinder.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.sqlite.db.SimpleSQLiteQuery
import com.issen.workerfinder.database.models.UserData
import com.issen.workerfinder.database.models.UserDataFull
import com.issen.workerfinder.database.models.UserDataWithComments

@Dao
interface UserDataDao {

    @Insert
    suspend fun insert(userData: UserData): Long

    @Insert
    suspend fun insert(userDataList: MutableList<UserData>)

    @Update
    fun update(userData: UserData)

    @Delete
    fun delete(userData: UserData)

    @Query("DELETE FROM user_table WHERE userId = :userId")
    fun delete(userId: String)

    @Query("DELETE FROM user_table")
    suspend fun deleteAll()

    @Transaction
    @Query("SELECT * FROM user_table where userId = :userId")
    suspend fun getUserByFirebaseKey(userId: String): UserDataFull

    @Query("UPDATE user_table SET isAccountPublic = :public WHERE userId = :userId")
    suspend fun setAccountPublic(userId: String, public: Boolean)

    @Transaction
    @Query("SELECT DISTINCT * FROM user_table WHERE userId not in (SELECT contactId from contact_table where userId = :userId) and userId" +
            " not in (SELECT userId from contact_table where contactId = :userId) and userId not like :userId and isAccountPublic = 1 and" +
            " isOpenForWork = 1")
    fun getUsersList(userId: String): LiveData<List<UserDataFull>>

    @Transaction
    @Query("SELECT DISTINCT * FROM user_table WHERE userId in (SELECT contactId from contact_table where userId = :userId) OR userId in " +
            "(SELECT userId from contact_table where contactId = :userId)")
    fun getUserContacts(userId: String): LiveData<List<UserDataFull>>

    @Transaction
    @Query("SELECT DISTINCT * FROM user_table WHERE userId in (SELECT notificationOwnerId from dashboard_notifications_table where notificationCausedByUserId =" +
            " :userId AND dashboardNotificationType = 'CONTACTINVITED')")
    fun gerUserInvitations(userId: String): LiveData<List<UserDataFull>>

    @Transaction
    @Query("SELECT * FROM user_table WHERE userId in (SELECT contactId from contact_table where userId = :userId)")
    fun getUserWorkers(userId: String): LiveData<List<UserDataFull>>

    @Transaction
    @Query("SELECT * FROM user_table WHERE userId in (SELECT userId from contact_table where contactId = :userId)")
    fun getUserUsers(userId: String): LiveData<List<UserDataFull>>

    @Transaction
    @Query("SELECT * FROM user_table WHERE isAccountPublic = 1 AND isOpenForWork = 1")
    fun getBoardWorkers(): LiveData<List<UserDataFull>>

    @Transaction
    @Query(
        "SELECT comment_table.*, user_table.* from user_table join comment_table on userId = commentedUserId " +
                "where commentedUserId = :userId and commentByWorker = 0"
    )
    suspend fun getCommentUser(userId: String): List<UserDataWithComments>?

    @Transaction
    @Query(
        "SELECT comment_table.*, user_table.* from user_table join comment_table on userId = commentedUserId " +
                "where commentedUserId = :userId and commentByWorker = 1"
    )
    suspend fun getCommentWorker(userId: String): List<UserDataWithComments>?

    @RawQuery
    fun getUsersQueried(query: SimpleSQLiteQuery): LiveData<List<UserDataFull>>




}
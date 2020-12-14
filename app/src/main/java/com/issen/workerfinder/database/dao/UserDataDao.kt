package com.issen.workerfinder.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.issen.workerfinder.database.models.FullUserData
import com.issen.workerfinder.database.models.UserData
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

    @Query("DELETE FROM user_table")
    suspend fun deleteAll()

    @Transaction
    @Query("SELECT * FROM user_table where firebaseKey = :firebaseKey")
    fun getUserByFirebaseKey(firebaseKey: String): FullUserData

    @Transaction
    @Query("SELECT * FROM user_table where firebaseKey = :firebaseKey")
    fun getUserById(firebaseKey: String): LiveData<FullUserData>

    @Query("UPDATE user_table SET isAccountPublic = :public WHERE firebaseKey = :firebaseKey")
    suspend fun setAccountPublic(firebaseKey: String, public: Boolean)

    @Transaction
    @Query("SELECT * FROM user_table WHERE userId in (SELECT contactId from contact_table where userId = :userId)")
    fun getUserWorkers(userId: Int): LiveData<List<FullUserData>>

    @Transaction
    @Query("SELECT * FROM user_table WHERE userId in (SELECT userId from contact_table where contactId = :userId)")
    fun getUserUsers(userId: Int): LiveData<List<FullUserData>>

    @Transaction
    @Query("SELECT * FROM user_table WHERE isAccountPublic = 1 AND isOpenForWork = 1")
    fun getBoardWorkers(): LiveData<List<FullUserData>>

    @Transaction
    @Query(
        "SELECT comment_table.*, user_table.* from user_table join comment_table on userId = commentedUserId " +
                "where commentedUserId = :userId and commentByWorker = 0"
    )
    suspend fun getCommentUser(userId: Int): List<UserDataWithComments>?

    @Transaction
    @Query(
        "SELECT comment_table.*, user_table.* from user_table join comment_table on userId = commentedUserId " +
                "where commentedUserId = :userId and commentByWorker = 1"
    )
    suspend fun getCommentWorker(userId: Int): List<UserDataWithComments>?
}
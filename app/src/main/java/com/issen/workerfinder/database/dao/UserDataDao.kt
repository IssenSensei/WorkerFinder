package com.issen.workerfinder.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.issen.workerfinder.database.models.FullUserData
import com.issen.workerfinder.database.models.UserData

@Dao
interface UserDataDao {

    @Insert
    suspend fun insert(userData: UserData) : Long

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

    @Query("SELECT * FROM user_table where firebaseKey = :firebaseKey")
    fun getUserById(firebaseKey: String): LiveData<FullUserData>

    @Query("UPDATE user_table SET isAccountPublic = :public WHERE firebaseKey = :firebaseKey")
    suspend fun setAccountPublic(firebaseKey: String, public: Boolean)

}
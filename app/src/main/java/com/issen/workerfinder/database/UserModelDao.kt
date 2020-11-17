package com.issen.workerfinder.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserModelDao {

    @Insert
    suspend fun insert(userModel: UserModel) : Long

    @Insert
    suspend fun insert(userModelList: MutableList<UserModel>)

    @Update
    fun update(userModel: UserModel)

    @Delete
    fun delete(userModel: UserModel)

    @Query("DELETE FROM user_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM user_table where firebaseKey = :firebaseKey")
    fun getUserById(firebaseKey: String): LiveData<UserModel>


}
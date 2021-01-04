package com.issen.workerfinder.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.issen.workerfinder.database.models.UserCategoryCrossRef

@Dao
interface UserCategoryCrossRefDao {

    @Insert
    suspend fun insert(mutableListOf: MutableList<UserCategoryCrossRef>)

    @Query("DELETE FROM usercategorycrossref")
    suspend fun deleteAll()

}
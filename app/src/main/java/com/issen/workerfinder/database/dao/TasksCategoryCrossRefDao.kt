package com.issen.workerfinder.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.issen.workerfinder.database.models.TasksCategoryCrossRef

@Dao
interface TasksCategoryCrossRefDao {

    @Insert
    suspend fun insert(mutableListOf: MutableList<TasksCategoryCrossRef>)

    @Query("DELETE FROM taskscategorycrossref")
    suspend fun deleteAll()
}
package com.issen.workerfinder.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TaskRepeatDaysDao {

    @Insert
    suspend fun insert(taskModelRepeatDays: TaskModelRepeatDays)

    @Insert
    suspend fun insert(taskModelRepeatDaysList: List<TaskModelRepeatDays>)

    @Query("DELETE FROM day_table")
    suspend fun deleteAll()

}

package com.issen.workerfinder.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.issen.workerfinder.database.models.TaskModelPhotos

@Dao
interface TaskPhotoDao {

    @Insert
    suspend fun insert(taskModelPhotos: TaskModelPhotos)

    @Insert
    suspend fun insert(taskModelPhotosList: List<TaskModelPhotos>)

    @Query("DELETE FROM photo_table")
    suspend fun deleteAll()

}

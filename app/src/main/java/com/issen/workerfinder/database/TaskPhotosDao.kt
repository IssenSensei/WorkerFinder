package com.issen.workerfinder.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TaskPhotosDao {

    @Insert
    suspend fun insert(taskModelPhotos: TaskModelPhotos)

    @Insert
    suspend fun insert(taskModelPhotosList: List<TaskModelPhotos>)

    @Query("DELETE FROM photo_table")
    suspend fun deleteAll()

}

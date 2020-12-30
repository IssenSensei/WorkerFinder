package com.issen.workerfinder.database.repositories

import com.issen.workerfinder.database.dao.TaskPhotoDao
import com.issen.workerfinder.database.models.TaskModelPhotos

class TaskPhotoRepository(private val taskPhotoDao: TaskPhotoDao) {

    suspend fun insert(photos: MutableList<TaskModelPhotos>) {
        taskPhotoDao.insert(photos)
    }

}
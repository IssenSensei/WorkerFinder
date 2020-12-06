package com.issen.workerfinder.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "photo_table")
data class TaskModelPhotos(

    @PrimaryKey(autoGenerate = true)
    var photoID: Int = 0,

    var taskId: Int = 0,

    @ColumnInfo(name = "task_photo")
    var taskPhoto: String = ""

) : Serializable


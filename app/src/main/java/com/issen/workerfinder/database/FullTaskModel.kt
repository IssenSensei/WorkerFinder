package com.issen.workerfinder.database

import androidx.room.*
import java.io.Serializable

data class FullTaskModel(

    @Embedded val task: TaskModel,
    @Relation(
        parentColumn = "taskId",
        entityColumn = "taskId"
    )
    val photos: List<TaskModelPhotos>,

    @Relation(
        parentColumn = "taskId",
        entityColumn = "taskId"
    )
    val repeatDays: List<TaskModelRepeatDays>
) : Serializable


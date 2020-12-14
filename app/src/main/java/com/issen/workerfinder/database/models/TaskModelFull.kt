package com.issen.workerfinder.database.models

import androidx.room.*
import java.io.Serializable

data class TaskModelFull(

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
    val repeatDays: List<TaskModelRepeatDays>,

    @Relation(
        parentColumn = "task_user_id",
        entityColumn = "userId"
    )
    val taskUser: UserData?,

    @Relation(
        parentColumn = "task_worker_id",
        entityColumn = "userId"
    )
    val taskWorker: UserData?
) : Serializable


package com.issen.workerfinder.database.models

import androidx.room.Entity

@Entity(primaryKeys = ["taskId", "categoryId"])
data class TasksCategoryCrossRef(
    val taskId: Long,
    val categoryId: Long
)
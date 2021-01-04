package com.issen.workerfinder.database.models

import androidx.room.Entity

@Entity(primaryKeys = ["userId", "categoryId"])
data class UserCategoryCrossRef(
    val userId: String,
    val categoryId: Long
)
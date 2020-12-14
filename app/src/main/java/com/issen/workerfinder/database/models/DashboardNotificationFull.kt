package com.issen.workerfinder.database.models

import androidx.room.Embedded
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.io.Serializable

data class DashboardNotificationFull(

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    @Embedded
    val notification: DashboardNotification,
    @Relation(
        parentColumn = "notificationCausedByUserId",
        entityColumn = "userId"
    )
    val userData: UserData

) : Serializable

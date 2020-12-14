package com.issen.workerfinder.database.models

import androidx.room.Embedded
import androidx.room.Relation
import java.io.Serializable

data class DashboardNotificationFull(

    @Embedded
    val notification: DashboardNotification,

    @Relation(
        parentColumn = "notificationCausedByUserId",
        entityColumn = "userId"
    )
    val userData: UserData

) : Serializable

package com.issen.workerfinder.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "dashboard_notifications_table")
data class DashboardNotification(

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    var date: String = "",

    var notificationOwnerId: String = "",

    var notificationCausedByUserId: String = "",

    var dashboardNotificationType: String = "",

    var modifiedRecordId: Int = 0
) : Serializable

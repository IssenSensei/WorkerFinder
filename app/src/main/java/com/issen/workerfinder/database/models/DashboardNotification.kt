package com.issen.workerfinder.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.issen.workerfinder.sealedClasses.DashboardNotificationTypes
import java.io.Serializable

@Entity(tableName = "dashboard_notifications_table")
data class DashboardNotification(

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    var date: String = "",

    var notificationOwnerId: String = "",

    var notificationCausedByUserId: String = "",

    var type: DashboardNotificationTypes? = null,

    var modifiedRecordId: Int = 0,

    val resolved: Boolean

) : Serializable

package com.issen.workerfinder.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.issen.workerfinder.database.models.DashboardNotification
import com.issen.workerfinder.database.models.DashboardNotificationFull

@Dao
interface DashboardNotificationDao {

    @Insert
    suspend fun insert(mutableListOf: MutableList<DashboardNotification>)

    @Transaction
    @Query("SELECT * FROM dashboard_notifications_table ORDER BY date DESC")
    fun getAllNotifications(): LiveData<List<DashboardNotificationFull>>

    @Insert
    suspend fun notify(dashboardNotification: DashboardNotification)

    @Query("UPDATE dashboard_notifications_table SET dashboardNotificationType = 'CONTACTCANCELED' WHERE notificationOwnerId = " +
            ":notificationOwner AND notificationCausedByUserId = :notificationCausedBy")
    suspend fun cancelNotification(notificationOwner: String, notificationCausedBy: String)

    @Query("UPDATE dashboard_notifications_table SET dashboardNotificationType = 'CONTACTACCEPTED' WHERE id = :id")
    suspend fun acceptContact(id: Int)

    @Query("UPDATE dashboard_notifications_table SET dashboardNotificationType = 'CONTACTREFUSED' WHERE id = :id")
    suspend fun refuseContact(id: Int)


}
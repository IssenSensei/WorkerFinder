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

    @Transaction
    @Query("SELECT * FROM dashboard_notifications_table WHERE notificationOwnerId = :userId ORDER BY date DESC")
    fun getAllUserNotifications(userId: String): LiveData<List<DashboardNotificationFull>>

    @Insert
    suspend fun notify(dashboardNotification: DashboardNotification)

    @Query("UPDATE dashboard_notifications_table SET dashboardNotificationType = 'CONTACTCANCELED', resolved = 1 WHERE notificationOwnerId = " +
            ":notificationOwner AND notificationCausedByUserId = :notificationCausedBy")
    suspend fun cancelContactNotification(notificationOwner: String, notificationCausedBy: String)

    @Query("UPDATE dashboard_notifications_table SET dashboardNotificationType = 'CONTACTACCEPTED', resolved = 1 WHERE id = :id")
    suspend fun acceptContact(id: Int)

    @Query("UPDATE dashboard_notifications_table SET dashboardNotificationType = 'CONTACTREFUSED', resolved = 1 WHERE id = :id")
    suspend fun refuseContact(id: Int)

    @Query("UPDATE dashboard_notifications_table SET dashboardNotificationType = 'TASKACCEPTED', resolved = 1 WHERE id = :id")
    suspend fun acceptTask(id: Int)

    @Query("UPDATE dashboard_notifications_table SET dashboardNotificationType = 'TASKREJECTED', resolved = 1 WHERE id = :id")
    suspend fun rejectTask(id: Int)

    @Query("UPDATE dashboard_notifications_table SET dashboardNotificationType = 'WORKACCEPTED', resolved = 1 WHERE id = :id")
    suspend fun acceptWork(id: Int)

    @Query("UPDATE dashboard_notifications_table SET dashboardNotificationType = 'WORKREFUSED', resolved = 1 WHERE id = :id")
    suspend fun refuseWork(id: Int)

    @Query("UPDATE dashboard_notifications_table SET dashboardNotificationType = 'WORKCANCELED', resolved = 1 WHERE modifiedRecordId = " +
            ":modifiedRecordId AND notificationCausedByUserId = :notificationCausedBy")
    suspend fun cancelWorkNotification(modifiedRecordId: Int, notificationCausedBy: String)

    @Query("UPDATE dashboard_notifications_table SET dashboardNotificationType = 'TASKBOARDCANCELED', resolved = 1 WHERE modifiedRecordId = " +
            ":modifiedRecordId AND notificationCausedByUserId = :notificationCausedBy")
    suspend fun cancelBoardApplicationNotification(modifiedRecordId: Int, notificationCausedBy: String)

    @Query("DELETE FROM dashboard_notifications_table")
    suspend fun deleteAll()


}
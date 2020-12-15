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



}
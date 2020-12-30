package com.issen.workerfinder.database.repositories

import com.issen.workerfinder.database.dao.DashboardNotificationDao
import com.issen.workerfinder.database.models.DashboardNotification

class DashboardNotificationRepository(private val dashboardNotificationDao: DashboardNotificationDao) {

    suspend fun notify(dashboardNotification: DashboardNotification) = dashboardNotificationDao.notify(dashboardNotification)
    fun getAllNotifications() = dashboardNotificationDao.getAllNotifications()

}
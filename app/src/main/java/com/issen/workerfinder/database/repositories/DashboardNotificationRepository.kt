package com.issen.workerfinder.database.repositories

import com.issen.workerfinder.database.dao.DashboardNotificationDao
import com.issen.workerfinder.database.models.DashboardNotification

class DashboardNotificationRepository(private val dashboardNotificationDao: DashboardNotificationDao) {

    fun getAllNotifications() = dashboardNotificationDao.getAllNotifications()

    suspend fun notify(dashboardNotification: DashboardNotification) = dashboardNotificationDao.notify(dashboardNotification)
    suspend fun cancelNotification(notificationOwner: String, notificationCausedBy: String) = dashboardNotificationDao.cancelNotification(notificationOwner, notificationCausedBy)
    suspend fun acceptContact(id: Int) = dashboardNotificationDao.acceptContact(id)
    suspend fun refuseContact(id: Int) = dashboardNotificationDao.refuseContact(id)

}
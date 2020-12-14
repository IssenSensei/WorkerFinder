package com.issen.workerfinder.database

import com.issen.workerfinder.database.dao.*
import com.issen.workerfinder.database.models.*

class DashboardNotificationsRepository(private val dashboardNotificationDao: DashboardNotificationDao) {

    suspend fun notifyOwnerOfTaskState(dashboardNotification: DashboardNotification) = dashboardNotificationDao.notifyOwnerOfTaskState(dashboardNotification)

}
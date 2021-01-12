package com.issen.workerfinder.database.repositories

import com.issen.workerfinder.database.dao.DashboardNotificationDao
import com.issen.workerfinder.database.models.DashboardNotification

class DashboardNotificationRepository(private val dashboardNotificationDao: DashboardNotificationDao) {

    fun getAllNotifications() = dashboardNotificationDao.getAllNotifications()
    fun getAllUserNotifications(userId: String) = dashboardNotificationDao.getAllUserNotifications(userId)

    suspend fun notify(dashboardNotification: DashboardNotification) = dashboardNotificationDao.notify(dashboardNotification)
    suspend fun cancelContactNotification(notificationOwner: String, notificationCausedBy: String) = dashboardNotificationDao.cancelContactNotification(notificationOwner, notificationCausedBy)
    suspend fun acceptContact(id: Int) = dashboardNotificationDao.acceptContact(id)
    suspend fun refuseContact(id: Int) = dashboardNotificationDao.refuseContact(id)
    suspend fun acceptTask(id: Int) = dashboardNotificationDao.acceptTask(id)
    suspend fun rejectTask(id: Int) = dashboardNotificationDao.rejectTask(id)
    suspend fun cancelWorkNotification(modifiedRecordId: Int, notificationCausedBy: String) = dashboardNotificationDao.cancelWorkNotification(modifiedRecordId, notificationCausedBy)
    suspend fun cancelBoardApplicationNotification(modifiedRecordId: Int, notificationCausedBy: String) = dashboardNotificationDao.cancelBoardApplicationNotification(modifiedRecordId, notificationCausedBy)
    suspend fun acceptWork(id: Int) = dashboardNotificationDao.acceptWork(id)
    suspend fun refuseWork(id: Int) = dashboardNotificationDao.refuseWork(id)
//    suspend fun selectBoardWorker(userId: String, taskId: Int) = dashboardNotificationDao.selectBoardWorker(userId, taskId)


}
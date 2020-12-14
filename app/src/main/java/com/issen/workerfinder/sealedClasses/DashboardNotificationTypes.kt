package com.issen.workerfinder.sealedClasses

sealed class DashboardNotificationTypes {
    class WorkOfferState(val status: WorkOfferNotificationStates) : DashboardNotificationTypes()
    class TaskState(val status: TaskNotificationStates) : DashboardNotificationTypes()
    class ContactState(val status: ContactNotificationStates) : DashboardNotificationTypes()
    class RatingState(val status: RatingNotificationStates) : DashboardNotificationTypes()
}

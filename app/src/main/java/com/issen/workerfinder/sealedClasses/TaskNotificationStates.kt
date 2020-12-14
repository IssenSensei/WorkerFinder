package com.issen.workerfinder.sealedClasses

sealed class TaskNotificationStates {
    object Completed : TaskNotificationStates()
    object Abandoned : TaskNotificationStates()
    object Rejected : TaskNotificationStates()
    object Rated : TaskNotificationStates()
}
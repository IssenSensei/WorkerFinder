package com.issen.workerfinder.sealedClasses

sealed class ContactNotificationStates {
    object Invited : ContactNotificationStates()
    object Accepted : ContactNotificationStates()
    object Refused : ContactNotificationStates()
    object Canceled : ContactNotificationStates()
}
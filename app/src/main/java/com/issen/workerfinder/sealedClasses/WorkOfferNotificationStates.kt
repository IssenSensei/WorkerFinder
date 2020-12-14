package com.issen.workerfinder.sealedClasses

sealed class WorkOfferNotificationStates {
    object New : WorkOfferNotificationStates()
    object Accepted : WorkOfferNotificationStates()
    object Refused : WorkOfferNotificationStates()
    object Canceled : WorkOfferNotificationStates()
}
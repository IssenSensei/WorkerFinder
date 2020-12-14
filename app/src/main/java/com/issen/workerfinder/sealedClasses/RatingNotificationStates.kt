package com.issen.workerfinder.sealedClasses

sealed class RatingNotificationStates {
    object RatedByWorker : RatingNotificationStates()
    object RatedByUser : RatingNotificationStates()
}
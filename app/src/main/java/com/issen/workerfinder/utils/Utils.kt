package com.issen.workerfinder.utils

import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.widget.NestedScrollView
import com.issen.workerfinder.enums.DashboardNotificationTypes

fun isLayoutRtl(view: View): Boolean {
    return ViewCompat.getLayoutDirection(view) == ViewCompat.LAYOUT_DIRECTION_RTL
}

fun View.hideAnimated() {
    alpha = 1f
    visibility = View.VISIBLE
    animate().alpha(0.0f).setDuration(150).withEndAction { visibility = View.GONE }
}

fun View.showAnimated() {
    alpha = 0f
    visibility = View.VISIBLE
    animate().alpha(1.0f).setDuration(150)
}

fun nestedScrollTo(nested: NestedScrollView, targetView: View) {
    nested.post(Runnable { nested.scrollTo(500, targetView.bottom) })
}

fun getDashboardNotificationGroup(type: String) = when(type){
    DashboardNotificationTypes.RATEDBYWORKER.toString() -> {
        "RATING"
    }
    DashboardNotificationTypes.RATEDBYUSER.toString() ->{
        "RATING"
    }
    DashboardNotificationTypes.WORKOFFERED.toString() -> {
        "WORK"
    }
    DashboardNotificationTypes.WORKACCEPTED.toString() ->{
        "WORK"
    }
    DashboardNotificationTypes.WORKREFUSED.toString() ->{
        "WORK"
    }
    DashboardNotificationTypes.WORKCANCELED.toString() -> {
        "WORK"
    }
    DashboardNotificationTypes.TASKCOMPLETED.toString() -> {
        "TASK"
    }
    DashboardNotificationTypes.TASKABANDONED.toString() ->{
        "TASK"
    }
    DashboardNotificationTypes.TASKREJECTED.toString() ->{
        "TASK"
    }
    DashboardNotificationTypes.TASKACCEPTED.toString() -> {
        "TASK"
    }
    DashboardNotificationTypes.CONTACTINVITED.toString() -> {
        "CONTACT"
    }
    DashboardNotificationTypes.CONTACTREFUSED.toString() ->{
        "CONTACT"
    }
    DashboardNotificationTypes.CONTACTCANCELED.toString() ->{
        "CONTACT"
    }
    DashboardNotificationTypes.CONTACTACCEPTED.toString() -> {
        "CONTACT"
    }
    DashboardNotificationTypes.CONTACTREMOVED.toString() -> {
        "CONTACT"
    }
    else -> {
        "ERROR"
    }
}
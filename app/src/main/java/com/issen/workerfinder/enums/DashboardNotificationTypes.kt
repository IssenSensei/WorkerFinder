package com.issen.workerfinder.enums

enum class DashboardNotificationTypes(private val type: String) {
    CONTACTINVITED("CONTACTINVITED"),
    CONTACTACCEPTED("CONTACTACCEPTED"),
    CONTACTREFUSED("CONTACTREFUSED"),
    CONTACTCANCELED("CONTACTCANCELED"),

    TASKCOMPLETED("TASKCOMPLETED"),
    TASKABANDONED("TASKABANDONED"),
    TASKREJECTED("TASKREJECTED"),
    TASKACCEPTED("TASKACCEPTED"),

    WORKOFFERED("WORKOFFERED"),
    WORKACCEPTED("WORKACCEPTED"),
    WORKREFUSED("WORKREFUSED"),
    WORKCANCELED("WORKCANCELED"),

    RATEDBYWORKER("RATEDBYWORKER"),
    RATEDBYUSER("RATEDBYUSER");


    override fun toString(): String {
        return type
    }

}

package com.issen.workerfinder

import android.app.Application
import android.content.Context
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.issen.workerfinder.database.models.UserModel
import com.issen.workerfinder.enums.PriorityTypes


class TaskApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        mAuth = FirebaseAuth.getInstance();
    }

    private var mAuth: FirebaseAuth? = null

    companion object {

        var currentLoggedInUser: UserModel? = null
        var currentUserToken: String = ""

        lateinit var appContext: Context

        fun getIndicatorColor(priorityValue: String): Int = when (priorityValue) {
            PriorityTypes.URGENT.toString() -> {
                ContextCompat.getColor(appContext, R.color.colorUrgent)
            }
            PriorityTypes.HIGH.toString() -> {
                ContextCompat.getColor(appContext, R.color.colorHigh)
            }
            PriorityTypes.NORMAL.toString() -> {
                ContextCompat.getColor(appContext, R.color.colorNormal)
            }
            PriorityTypes.LOW.toString() -> {
                ContextCompat.getColor(appContext, R.color.colorLow)
            }
            PriorityTypes.OTHER.toString() -> {
                ContextCompat.getColor(appContext, R.color.colorOther)
            }
            else -> {
                0
            }
        }
    }
}
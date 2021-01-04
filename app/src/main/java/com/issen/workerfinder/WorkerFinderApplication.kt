package com.issen.workerfinder

import android.app.Application
import android.content.Context
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.issen.workerfinder.database.WorkerFinderDatabase
import com.issen.workerfinder.database.models.UserDataFull
import com.issen.workerfinder.database.repositories.*
import com.issen.workerfinder.enums.PriorityTypes


class WorkerFinderApplication : Application() {

    val database by lazy {WorkerFinderDatabase.getDatabase(this)}
    val categoryRepository by lazy {CategoryRepository(database.categoryDao)}
    val commentRepository by lazy {CommentRepository(database.commentDao)}
    val contactRepository by lazy {ContactRepository(database.contactDao)}
    val dashboardNotificationRepository by lazy {DashboardNotificationRepository(database.dashboardNotificationDao)}
    val taskPhotoRepository by lazy {TaskPhotoRepository(database.taskPhotoDao)}
    val taskRepeatDayRepository by lazy {TaskRepeatDayRepository(database.taskRepeatDayDao)}
    val taskRepository by lazy {TaskRepository(database.taskModelDao)}
    val userRepository by lazy {UserRepository(database.userDataDao)}
    val tasksCategoryCrossRefRepository by lazy {TasksCategoryCrossRefRepository(database.tasksCategoryCrossRefDao)}
    val userCategoryCrossRefRepository by lazy {UserCategoryCrossRefRepository(database.userCategoryCrossRefDao)}

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        mAuth = FirebaseAuth.getInstance();
    }

    private var mAuth: FirebaseAuth? = null

    companion object {

        var currentLoggedInUserFull: UserDataFull? = null
        var currentUserToken: String = ""

        lateinit var appContext: Context

        fun getPriorityIndicatorColor(priorityValue: String): Int = when (priorityValue) {
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
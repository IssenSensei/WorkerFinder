package com.issen.workerfinder.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.issen.workerfinder.database.dao.*
import com.issen.workerfinder.database.models.*
import com.issen.workerfinder.enums.CompletionTypes
import com.issen.workerfinder.enums.CyclicTypes
import com.issen.workerfinder.enums.DashboardNotificationTypes
import com.issen.workerfinder.enums.PriorityTypes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.*


@Database(
    entities = [TaskModel::class, TaskModelPhotos::class, TaskModelRepeatDays::class, UserData::class, Categories::class,
        Comments::class, Contacts::class, DashboardNotification::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(DataConverter::class)
abstract class WorkerFinderDatabase : RoomDatabase() {

    abstract val taskModelDao: TaskModelDao
    abstract val taskPhotosDao: TaskPhotosDao
    abstract val taskRepeatDaysDao: TaskRepeatDaysDao
    abstract val userDataDao: UserDataDao
    abstract val categoriesDao: CategoriesDao
    abstract val commentsDao: CommentsDao
    abstract val contactsDao: ContactsDao
    abstract val dashboardNotificationDao: DashboardNotificationDao

    private class WorkerFinderDatabaseCallback(private val scope: CoroutineScope) :
        RoomDatabase.Callback() {
        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let {
                scope.launch {
                    it.populateDbOpen(this)
                }
            }
        }

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let {
                scope.launch {
                    it.populateDbCreate(this)
                }
                //                scope.launch {
//                    val userModelDao = it.userModelDao
//                    userModelDao.insert(
//                        mutableListOf(
//                            FullUserData(1, "name lastName", "https://i.imgflip.com/15l4w6.jpg", "email", "111111111", "aaaaa1", false),
//                            FullUserData(0, "name1 lastName1", "https://i.imgflip.com/15l4w6.jpg", "email1", "222222222", "aaaaaa2", false),
//                            FullUserData(0, "name2 lastName2", "https://i.imgflip.com/15l4w6.jpg", "email2", "333333333", "aaaaaa3", false)
//                        )
//                    )

//                }

            }
        }
    }


    companion object {

        @Volatile
        private var INSTANCE: WorkerFinderDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): WorkerFinderDatabase {

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WorkerFinderDatabase::class.java,
                    "worker_finder_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(WorkerFinderDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance

            }
        }
    }

    fun populateDbOpen(coroutineScope: CoroutineScope) {
        populateTasksOpen(coroutineScope)
    }

    fun populateDbCreate(coroutineScope: CoroutineScope) {
        populateUsers(coroutineScope)
        populateTasks(coroutineScope)
        populatePhotos(coroutineScope)
        populateRepeatDays(coroutineScope)
    }

    private fun populateRepeatDays(coroutineScope: CoroutineScope) {
        coroutineScope.launch {
            taskRepeatDaysDao.deleteAll()
            taskRepeatDaysDao.insert(
                mutableListOf(
                    TaskModelRepeatDays(0, 2, "aaaaa"),
                    TaskModelRepeatDays(0, 1, "bbbb"),
                    TaskModelRepeatDays(0, 2, "ccccc"),
                    TaskModelRepeatDays(0, 1, "ddddd"),
                    TaskModelRepeatDays(0, 2, "eeeeeeee")
                )
            )
        }
    }

    private fun populatePhotos(coroutineScope: CoroutineScope) {
        coroutineScope.launch {
            taskPhotosDao.deleteAll()
            taskPhotosDao.insert(
                mutableListOf(
                    TaskModelPhotos(0, 1, "https://i.imgflip.com/15l4w6.jpg"),
                    TaskModelPhotos(0, 1, "https://i.imgflip.com/15l4w6.jpg"),
                    TaskModelPhotos(0, 2, "https://i.imgflip.com/15l4w6.jpg"),
                    TaskModelPhotos(0, 2, "https://i.imgflip.com/15l4w6.jpg"),
                    TaskModelPhotos(0, 3, "https://i.imgflip.com/15l4w6.jpg"),
                    TaskModelPhotos(0, 2, "https://i.imgflip.com/15l4w6.jpg"),
                    TaskModelPhotos(0, 5, "https://i.imgflip.com/15l4w6.jpg"),
                    TaskModelPhotos(0, 2, "https://i.imgflip.com/15l4w6.jpg"),
                    TaskModelPhotos(0, 7, "https://i.imgflip.com/15l4w6.jpg")
                )
            )
        }
    }

    private fun populateTasks(coroutineScope: CoroutineScope) {
        coroutineScope.launch {
            taskModelDao.deleteAll()
            taskModelDao.insert(
                mutableListOf(
                    TaskModel(
                        1,
                        "Odśnieżanie",
                        "Należy odśnieżyć całą posesję",
                        "aaaaaaa",
                        "aaaaaaa",
                        "22-12-2020",
                        CyclicTypes.NONE.toString(),
                        Date(),
                        PriorityTypes.URGENT.toString(),
                        CompletionTypes.ACTIVE.toString(),
                        "completion"
                    ),
                    TaskModel(
                        2,
                        "Pomoc na budowie",
                        "Potrzebny pomocnik na budowie",
                        "aaaaaaa",
                        "aaaaaaa",
                        "01-01-2020",
                        CyclicTypes.MONTHDAY.toString(),
                        Date(),
                        PriorityTypes.NORMAL.toString(),
                        CompletionTypes.COMPLETED.toString(),
                        "completion"
                    ),
                    TaskModel(
                        3,
                        "Klaun na urodzinach online",
                        "Najlepiej jakiś rudy",
                        "aaaaaaa",
                        "aaaaaaa",
                        "03-02-2021",
                        CyclicTypes.NONE.toString(),
                        Date(),
                        PriorityTypes.LOW.toString(),
                        CompletionTypes.ACTIVE.toString(),
                        "completion"
                    )
                )

            )
        }
    }

    private fun populateTasksOpen(coroutineScope: CoroutineScope) {
        coroutineScope.launch {
            taskModelDao.insert(
                mutableListOf(
                    TaskModel(
                        0,
                        "Odśnieżanie",
                        "Należy odśnieżyć całą posesję",
                        "user",
                        "Zbysiu Zbyś",
                        "22-12-2020",
                        CyclicTypes.NONE.toString(),
                        Date(),
                        PriorityTypes.URGENT.toString(),
                        CompletionTypes.ABANDONED.toString(),
                        "completion"
                    ),
                    TaskModel(
                        0,
                        "Pomoc na budowie",
                        "Potrzebny pomocnik na budowie",
                        "user2",
                        "",
                        "01-01-2020",
                        CyclicTypes.MONTHDAY.toString(),
                        Date(),
                        PriorityTypes.NORMAL.toString(),
                        CompletionTypes.COMPLETED.toString(),
                        "completion"
                    ),
                    TaskModel(
                        0,
                        "Klaun na urodzinach online",
                        "Najlepiej jakiś rudy",
                        "user",
                        "",
                        "03-02-2021",
                        CyclicTypes.NONE.toString(),
                        Date(),
                        PriorityTypes.LOW.toString(),
                        CompletionTypes.PENDING.toString(),
                        "completion"
                    ),
                    TaskModel(
                        0,
                        "Zadanie testowe oznaczone jako active",
                        "opisu brak",
                        "user",
                        "",
                        "03-02-2021",
                        CyclicTypes.NONE.toString(),
                        Date(),
                        PriorityTypes.URGENT.toString(),
                        CompletionTypes.ACTIVE.toString(),
                        "completion"
                    )
                )
            )
        }
    }


    fun populateNotificationsOpen(coroutineScope: CoroutineScope, userId: String) {
        coroutineScope.launch {
            dashboardNotificationDao.insert(
                mutableListOf(
                    DashboardNotification(
                        0,
                        Date().toString(),
                        userId,
                        userId,
                        DashboardNotificationTypes.CONTACTACCEPTED.toString(),
                        1,
                        false
                    ),
                    DashboardNotification(
                        0,
                        Date().toString(),
                        userId,
                        userId,
                        DashboardNotificationTypes.CONTACTCANCELED.toString(),
                        1,
                        false
                    ),
                    DashboardNotification(
                        0,
                        Date().toString(),
                        userId,
                        userId,
                        DashboardNotificationTypes.CONTACTREFUSED.toString(),
                        1,
                        false
                    ),
                    DashboardNotification(
                        0,
                        Date().toString(),
                        userId,
                        userId,
                        DashboardNotificationTypes.CONTACTINVITED.toString(),
                        1,
                        false
                    ),
                    DashboardNotification(
                        0,
                        Date().toString(),
                        userId,
                        userId,
                        DashboardNotificationTypes.RATEDBYWORKER.toString(),
                        1,
                        false
                    ),
                    DashboardNotification(
                        0,
                        Date().toString(),
                        userId,
                        userId,
                        DashboardNotificationTypes.RATEDBYUSER.toString(),
                        1,
                        false
                    ),
                    DashboardNotification(
                        0,
                        Date().toString(),
                        userId,
                        userId,
                        DashboardNotificationTypes.TASKREJECTED.toString(),
                        1,
                        false
                    ),
                    DashboardNotification(
                        0,
                        Date().toString(),
                        userId,
                        userId,
                        DashboardNotificationTypes.TASKCOMPLETED.toString(),
                        1,
                        false
                    ),
                    DashboardNotification(
                        0,
                        Date().toString(),
                        userId,
                        userId,
                        DashboardNotificationTypes.TASKABANDONED.toString(),
                        1,
                        false
                    ),
                    DashboardNotification(
                        0,
                        Date().toString(),
                        userId,
                        userId,
                        DashboardNotificationTypes.TASKACCEPTED.toString(),
                        1,
                        false
                    ),
                    DashboardNotification(
                        0,
                        Date().toString(),
                        userId,
                        userId,
                        DashboardNotificationTypes.WORKCANCELED.toString(),
                        1,
                        false
                    ),
                    DashboardNotification(
                        0,
                        Date().toString(),
                        userId,
                        userId,
                        DashboardNotificationTypes.WORKREFUSED.toString(),
                        1,
                        false
                    ),
                    DashboardNotification(
                        0,
                        Date().toString(),
                        userId,
                        userId,
                        DashboardNotificationTypes.WORKOFFERED.toString(),
                        1,
                        false
                    ),
                    DashboardNotification(
                        0,
                        Date().toString(),
                        userId,
                        userId,
                        DashboardNotificationTypes.WORKACCEPTED.toString(),
                        1,
                        false
                    )
                )
            )
        }

    }

    fun populateComments(coroutineScope: CoroutineScope, userId: String) {
        coroutineScope.launch {
            commentsDao.deleteAll()
            commentsDao.insert(
                mutableListOf(
                    Comments(0, userId, "adwadawdawdwa", 4.5f, "Było w porządeczku", true),
                    Comments(0, userId, "adwadawdawdwa", 4.8f, "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", true),
                    Comments(0, userId, "adagduawwddyga", 2f, "bbbbbbbbbbbbbbbbbbbbbbbbbbbbbb", true),
                    Comments(0, userId, "adagduaawdwdyga", 4f, "cccccccccccccccccccccccccccccccc", true),
                    Comments(0, userId, "adagduadawawdyga", 0f, "dddddddddddddddddddddddddddddddddddddddd", false),
                    Comments(0, userId, "adagduefawdyga", 1f, "eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee", true),
                    Comments(0, userId, "adagdueqwawdyga", 3f, "ffffffffffffffffffffffffffffffffffff", false),
                    Comments(0, userId, "adagdeqwuawdyga", 5f, "gggggggggggggggggggggggggggg", true),
                    Comments(0, userId, "adagduawdfqyga", 2f, "hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh", true),
                    Comments(0, userId, "adagdusefawdyga", 3f, "iiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii", false),
                    Comments(0, userId, "adagduawsefdyga", 3.5f, "jjjjjjjjjjjjjjjjjjjjjjjjjjjjj", true),
                    Comments(0, userId, "adagduawdyga", 4.9f, "kkkkkkkkkkkkkkkkkkkkkkkkkkkkk", false),
                    Comments(0, userId, "adagduawdyga", 4.1f, "llllllllllllllllllllllllllllllllllllllllllllllllllllllll", true),
                    Comments(0, userId, "adagduawdyga", 3.3f, " zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz ", true
                    )
                )
            )
        }
    }

    fun populateContacts(coroutineScope: CoroutineScope, userId: String) {
        coroutineScope.launch {
            contactsDao.insert(
                mutableListOf(
                    Contacts(0, userId, "adwawd"),
                    Contacts(0, userId, "qqqdaw"),
                    Contacts(0, userId, "vsevesc"),
                    Contacts(0, userId, "grgsfe")
                )
            )
        }
    }

    private fun populateUsers(coroutineScope: CoroutineScope) {
        coroutineScope.launch {
//            userDataDao.deleteAll()
            userDataDao.insert(
                mutableListOf(
                    UserData(
                        "aaaaaaa", "name lastName", "https://i.imgflip.com/15l4w6.jpg", "email", "0000000", "aaaaa", false, true
                    ),
                    UserData(
                        "aaaaaaa1", "name1 lastName1", "https://i.imgflip.com/15l4w6.jpg", "email1", "11111111", "aaaaaa1", false, true
                    ),
                    UserData(
                        "aaaaaaa2", "name2 lastName2", "https://i.imgflip.com/15l4w6.jpg", "email2", "2222222222", "aaaaaa2", true, true
                    ),
                    UserData(
                        "aaaaaaa3", "name3 lastName3", "https://i.imgflip.com/15l4w6.jpg", "email3", "3333333333", "aaaaaa3", false, true
                    ),
                    UserData(
                        "aaaaaaa4", "name4 lastName4", "https://i.imgflip.com/15l4w6.jpg", "email4", "44444444444", "aaaaaa4", true, true
                    ),
                    UserData(
                        "aaaaaaa5", "name5 lastName5", "https://i.imgflip.com/15l4w6.jpg", "email5", "555555555", "aaaaaa5", true, true
                    ),
                    UserData(
                        "aaaaaaa6", "name6 lastName6", "https://i.imgflip.com/15l4w6.jpg", "email6", "6666666666", "aaaaaa6", false, false
                    ),
                    UserData(
                        "aaaaaaa7", "name7 lastName7", "https://i.imgflip.com/15l4w6.jpg", "email7", "777777777", "aaaaaa7", false, false
                    ),
                    UserData(
                        "aaaaaaa8", "name8 lastName8", "https://i.imgflip.com/15l4w6.jpg", "email8", "88888888", "aaaaaa8", false, false
                    )
                )
            )
        }
    }


}




package com.issen.workerfinder.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
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
        Comments::class, Contacts::class, DashboardNotification::class, TasksCategoryCrossRef::class, UserCategoryCrossRef::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(DataConverter::class)
abstract class WorkerFinderDatabase : RoomDatabase() {

    abstract val taskModelDao: TaskModelDao
    abstract val taskPhotoDao: TaskPhotoDao
    abstract val taskRepeatDayDao: TaskRepeatDayDao
    abstract val userDataDao: UserDataDao
    abstract val categoryDao: CategoryDao
    abstract val commentDao: CommentDao
    abstract val contactDao: ContactDao
    abstract val dashboardNotificationDao: DashboardNotificationDao
    abstract val tasksCategoryCrossRefDao: TasksCategoryCrossRefDao
    abstract val userCategoryCrossRefDao: UserCategoryCrossRefDao

    companion object {
        @Volatile
        private var INSTANCE: WorkerFinderDatabase? = null

        fun getDatabase(context: Context): WorkerFinderDatabase {

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WorkerFinderDatabase::class.java,
                    "worker_finder_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance

            }
        }
    }

    fun populateDb(coroutineScope: CoroutineScope, userId: String) {
        populateUsers(coroutineScope, userId)
        populateRepeatDays(coroutineScope)
        populatePhotos(coroutineScope)
        populateTasksOpen(coroutineScope, userId)
        populateComments(coroutineScope, userId)
        populateContacts(coroutineScope, userId)
        populateNotificationsOpen(coroutineScope, userId)
        populateCategories(coroutineScope)
        populateUserCategories(coroutineScope, userId)
        populateTaskCategories(coroutineScope, userId)
    }

    private fun populateUsers(coroutineScope: CoroutineScope, userId: String) {
        coroutineScope.launch {
            userDataDao.deleteAllExcept(userId)
            userDataDao.insert(
                mutableListOf(
                    UserData(
                        "a1", "name lastName", "https://i.imgflip.com/15l4w6.jpg", "email", "0000000", "aaaaa", "Lublin", false, true
                    ),
                    UserData(
                        "a2", "name1 lastName1", "https://i.imgflip.com/15l4w6.jpg", "email1", "11111111", "aaaaaa1", "Wrocław", false, true
                    ),
                    UserData(
                        "a3", "name2 lastName2", "https://i.imgflip.com/15l4w6.jpg", "email2", "2222222222", "aaaaaa2", "Kraśnik", true, true
                    ),
                    UserData(
                        "a4", "name3 lastName3", "https://i.imgflip.com/15l4w6.jpg", "email3", "3333333333", "aaaaaa3", "Warszawa", false, true
                    ),
                    UserData(
                        "a5", "name4 lastName4", "https://i.imgflip.com/15l4w6.jpg", "email4", "44444444444", "aaaaaa4", "Lublin", true, true
                    ),
                    UserData(
                        "a6", "name5 lastName5", "https://i.imgflip.com/15l4w6.jpg", "email5", "555555555", "aaaaaa5", "Gdańsk", true, true
                    ),
                    UserData(
                        "a7", "name6 lastName6", "https://i.imgflip.com/15l4w6.jpg", "email6", "6666666666", "aaaaaa6", "Lublin", false, false
                    ),
                    UserData(
                        "a8", "name7 lastName7", "https://i.imgflip.com/15l4w6.jpg", "email7", "777777777", "aaaaaa7", "Lublin", false, false
                    ),
                    UserData(
                        "a9", "name8 lastName8", "https://i.imgflip.com/15l4w6.jpg", "email8", "88888888", "aaaaaa8", "Lublin", true, true
                    ),
                    UserData(
                        "a10", "name9 lastName9", "https://i.imgflip.com/15l4w6.jpg", "email89", "88888888", "aaaaaa8", "Lublin", false, false
                    ),
                    UserData(
                        "a11", "name10 lastName10", "https://i.imgflip.com/15l4w6.jpg", "email10", "88888888", "aaaaaa8", "Lublin", false, false
                    ),
                    UserData(
                        "a12", "name11 lastName11", "https://i.imgflip.com/15l4w6.jpg", "email11", "88888888", "aaaaaa8", "Lublin", true, false
                    ),
                    UserData(
                        "a13", "name12 lastName12", "https://i.imgflip.com/15l4w6.jpg", "email12", "88888888", "aaaaaa8", "Lublin", false, false
                    ),
                    UserData(
                        "a14", "name13 lastName13", "https://i.imgflip.com/15l4w6.jpg", "email13", "88888888", "aaaaaa8", "Lublin", true, true
                    ),
                    UserData(
                        "a15", "name14 lastName14", "https://i.imgflip.com/15l4w6.jpg", "email14", "88888888", "aaaaaa8", "Lublin", true, true
                    ),
                    UserData(
                        "a16", "name15 lastName15", "https://i.imgflip.com/15l4w6.jpg", "email14", "88888888", "aaaaaa8", "Lublin", true, true
                    ),
                    UserData(
                        "a17", "name16 lastName16", "https://i.imgflip.com/15l4w6.jpg", "email14", "88888888", "aaaaaa8", "Lublin", true, true
                    ),
                    UserData(
                        "a18", "name17 lastName17", "https://i.imgflip.com/15l4w6.jpg", "email14", "88888888", "aaaaaa8", "Lublin", true, true
                    )
                )
            )
        }
    }

    private fun populateRepeatDays(coroutineScope: CoroutineScope) {
        coroutineScope.launch {
            taskRepeatDayDao.deleteAll()
            taskRepeatDayDao.insert(
                mutableListOf(
                    TaskModelRepeatDays(1, 2, "aaaaa"),
                    TaskModelRepeatDays(2, 1, "bbbb"),
                    TaskModelRepeatDays(3, 2, "ccccc"),
                    TaskModelRepeatDays(4, 1, "ddddd"),
                    TaskModelRepeatDays(5, 2, "eeeeeeee")
                )
            )
        }
    }

    private fun populatePhotos(coroutineScope: CoroutineScope) {
        coroutineScope.launch {
            taskPhotoDao.deleteAll()
            taskPhotoDao.insert(
                mutableListOf(
                    TaskModelPhotos(1, 1, "https://i.imgflip.com/15l4w6.jpg"),
                    TaskModelPhotos(2, 1, "https://i.imgflip.com/15l4w6.jpg"),
                    TaskModelPhotos(3, 2, "https://i.imgflip.com/15l4w6.jpg"),
                    TaskModelPhotos(4, 2, "https://i.imgflip.com/15l4w6.jpg"),
                    TaskModelPhotos(5, 3, "https://i.imgflip.com/15l4w6.jpg"),
                    TaskModelPhotos(6, 2, "https://i.imgflip.com/15l4w6.jpg"),
                    TaskModelPhotos(7, 5, "https://i.imgflip.com/15l4w6.jpg"),
                    TaskModelPhotos(8, 2, "https://i.imgflip.com/15l4w6.jpg"),
                    TaskModelPhotos(9, 7, "https://i.imgflip.com/15l4w6.jpg")
                )
            )
        }
    }


    private fun populateTasksOpen(coroutineScope: CoroutineScope, userId: String) {
        coroutineScope.launch {
            taskModelDao.deleteAll()
            taskModelDao.insert(
                mutableListOf(
                    TaskModel(
                        1,
                        "Odśnieżanie",
                        "Należy odśnieżyć całą posesję",
                        userId,
                        "a1",
                        "22-12-2020",
                        CyclicTypes.NONE.toString(),
                        Date(),
                        PriorityTypes.URGENT.toString(),
                        CompletionTypes.ABANDONED.toString(),
                        "completion",
                        "Lublin",
                        4000f
                    ),
                    TaskModel(
                        2,
                        "Pomoc na budowie",
                        "Potrzebny pomocnik na budowie",
                        userId,
                        "a2",
                        "01-01-2020",
                        CyclicTypes.MONTHDAY.toString(),
                        Date(),
                        PriorityTypes.NORMAL.toString(),
                        CompletionTypes.COMPLETED.toString(),
                        "completion",
                        "Warszawa",
                        8000f
                    ),
                    TaskModel(
                        3,
                        "Klaun na urodzinach online",
                        "Najlepiej jakiś rudy",
                        userId,
                        "a3",
                        "03-02-2021",
                        CyclicTypes.NONE.toString(),
                        Date(),
                        PriorityTypes.LOW.toString(),
                        CompletionTypes.PENDING.toString(),
                        "completion",
                        "Zamość",
                        2300f
                    ),
                    TaskModel(
                        4,
                        "Zadanie testowe oznaczone jako active",
                        "opisu brak",
                        userId,
                        "a4",
                        "03-02-2021",
                        CyclicTypes.NONE.toString(),
                        Date(),
                        PriorityTypes.URGENT.toString(),
                        CompletionTypes.ACTIVE.toString(),
                        "completion",
                        "Gdańsk",
                        90f
                    ),
                    TaskModel(
                        5,
                        "Odśnieżanie",
                        "Należy odśnieżyć całą posesję",
                        userId,
                        userId,
                        "22-12-2020",
                        CyclicTypes.NONE.toString(),
                        Date(),
                        PriorityTypes.URGENT.toString(),
                        CompletionTypes.ABANDONED.toString(),
                        "completion"
                    ),
                    TaskModel(
                        6,
                        "Pomoc na budowie",
                        "Potrzebny pomocnik na budowie",
                        userId,
                        userId,
                        "01-01-2020",
                        CyclicTypes.MONTHDAY.toString(),
                        Date(),
                        PriorityTypes.NORMAL.toString(),
                        CompletionTypes.COMPLETED.toString(),
                        "completion"
                    ),
                    TaskModel(
                        7,
                        "Klaun na urodzinach online",
                        "Najlepiej jakiś rudy",
                        userId,
                        userId,
                        "03-02-2021",
                        CyclicTypes.NONE.toString(),
                        Date(),
                        PriorityTypes.LOW.toString(),
                        CompletionTypes.PENDING.toString(),
                        "completion"
                    ),
                    TaskModel(
                        8,
                        "Zadanie testowe oznaczone jako active",
                        "opisu brak",
                        userId,
                        userId,
                        "03-02-2021",
                        CyclicTypes.NONE.toString(),
                        Date(),
                        PriorityTypes.URGENT.toString(),
                        CompletionTypes.ACTIVE.toString(),
                        "completion"
                    ),
                    TaskModel(
                        9,
                        "Odśnieżanie",
                        "Należy odśnieżyć całą posesję",
                        "a5",
                        userId,
                        "22-12-2020",
                        CyclicTypes.NONE.toString(),
                        Date(),
                        PriorityTypes.URGENT.toString(),
                        CompletionTypes.ABANDONED.toString(),
                        "completion",
                        "Lublin",
                        3200f
                    ),
                    TaskModel(
                        10,
                        "Pomoc na budowie",
                        "Potrzebny pomocnik na budowie",
                        "a6",
                        userId,
                        "01-01-2020",
                        CyclicTypes.MONTHDAY.toString(),
                        Date(),
                        PriorityTypes.NORMAL.toString(),
                        CompletionTypes.COMPLETED.toString(),
                        "completion",
                        "Warszawa",
                        5000f
                    ),
                    TaskModel(
                        11,
                        "Klaun na urodzinach online",
                        "Najlepiej jakiś rudy",
                        "a7",
                        userId,
                        "03-02-2021",
                        CyclicTypes.NONE.toString(),
                        Date(),
                        PriorityTypes.LOW.toString(),
                        CompletionTypes.PENDING.toString(),
                        "completion",
                        "Lublin",
                        300f
                    ),
                    TaskModel(
                        12,
                        "Zadanie testowe oznaczone jako active",
                        "opisu brak",
                        "a8",
                        userId,
                        "03-02-2021",
                        CyclicTypes.NONE.toString(),
                        Date(),
                        PriorityTypes.URGENT.toString(),
                        CompletionTypes.ACTIVE.toString(),
                        "completion",
                        "Lublin",
                        10000f
                    ),
                    TaskModel(
                        13,
                        "Odśnieżanie",
                        "Należy odśnieżyć całą posesję",
                        "a5",
                        "",
                        "22-12-2020",
                        CyclicTypes.NONE.toString(),
                        Date(),
                        PriorityTypes.URGENT.toString(),
                        CompletionTypes.BOARD.toString(),
                        "completion"
                    ),
                    TaskModel(
                        14,
                        "Pomoc na budowie",
                        "Potrzebny pomocnik na budowie",
                        "a6",
                        "",
                        "01-01-2020",
                        CyclicTypes.MONTHDAY.toString(),
                        Date(),
                        PriorityTypes.NORMAL.toString(),
                        CompletionTypes.BOARD.toString(),
                        "completion"
                    ),
                    TaskModel(
                        15,
                        "Klaun na urodzinach online",
                        "Najlepiej jakiś rudy",
                        "a7",
                        "",
                        "03-02-2021",
                        CyclicTypes.NONE.toString(),
                        Date(),
                        PriorityTypes.LOW.toString(),
                        CompletionTypes.BOARD.toString(),
                        "completion"
                    ),
                    TaskModel(
                        16,
                        "Zadanie testowe oznaczone jako active",
                        "opisu brak",
                        "a8",
                        "",
                        "03-02-2021",
                        CyclicTypes.NONE.toString(),
                        Date(),
                        PriorityTypes.URGENT.toString(),
                        CompletionTypes.BOARD.toString(),
                        "completion"
                    )
                )
            )
        }
    }


    private fun populateNotificationsOpen(coroutineScope: CoroutineScope, userId: String) {
        coroutineScope.launch {
            dashboardNotificationDao.deleteAll()
            dashboardNotificationDao.insert(
                mutableListOf(
                    DashboardNotification(
                        1,
                        Date().toString(),
                        userId,
                        "a1",
                        DashboardNotificationTypes.CONTACTACCEPTED.toString(),
                        1
                    ),
                    DashboardNotification(
                        2,
                        Date().toString(),
                        userId,
                        "a2",
                        DashboardNotificationTypes.CONTACTCANCELED.toString(),
                        1
                    ),
                    DashboardNotification(
                        3,
                        Date().toString(),
                        userId,
                        "a3",
                        DashboardNotificationTypes.CONTACTREFUSED.toString(),
                        1
                    ),
                    DashboardNotification(
                        4,
                        Date().toString(),
                        userId,
                        "a4",
                        DashboardNotificationTypes.CONTACTINVITED.toString(),
                        1
                    ),
                    DashboardNotification(
                        16,
                        Date().toString(),
                        userId,
                        "a16",
                        DashboardNotificationTypes.CONTACTINVITED.toString(),
                        1
                    ),
                    DashboardNotification(
                        5,
                        Date().toString(),
                        userId,
                        "a5",
                        DashboardNotificationTypes.CONTACTREMOVED.toString(),
                        1
                    ),
                    DashboardNotification(
                        6,
                        Date().toString(),
                        userId,
                        "a6",
                        DashboardNotificationTypes.RATEDBYWORKER.toString(),
                        1
                    ),
                    DashboardNotification(
                        7,
                        Date().toString(),
                        userId,
                        "a7",
                        DashboardNotificationTypes.RATEDBYUSER.toString(),
                        1
                    ),
                    DashboardNotification(
                        8,
                        Date().toString(),
                        userId,
                        "a8",
                        DashboardNotificationTypes.TASKREJECTED.toString(),
                        1
                    ),
                    DashboardNotification(
                        9,
                        Date().toString(),
                        userId,
                        "a9",
                        DashboardNotificationTypes.TASKCOMPLETED.toString(),
                        1
                    ),
                    DashboardNotification(
                        17,
                        Date().toString(),
                        userId,
                        "a17",
                        DashboardNotificationTypes.TASKCOMPLETED.toString(),
                        1
                    ),
                    DashboardNotification(
                        10,
                        Date().toString(),
                        userId,
                        "a10",
                        DashboardNotificationTypes.TASKABANDONED.toString(),
                        1
                    ),
                    DashboardNotification(
                        11,
                        Date().toString(),
                        userId,
                        "a11",
                        DashboardNotificationTypes.TASKACCEPTED.toString(),
                        1
                    ),
                    DashboardNotification(
                        12,
                        Date().toString(),
                        userId,
                        "a12",
                        DashboardNotificationTypes.WORKCANCELED.toString(),
                        1
                    ),
                    DashboardNotification(
                        13,
                        Date().toString(),
                        userId,
                        "a13",
                        DashboardNotificationTypes.WORKREFUSED.toString(),
                        1
                    ),
                    DashboardNotification(
                        14,
                        Date().toString(),
                        userId,
                        "a14",
                        DashboardNotificationTypes.WORKOFFERED.toString(),
                        1
                    ),
                    DashboardNotification(
                        18,
                        Date().toString(),
                        userId,
                        "a18",
                        DashboardNotificationTypes.WORKOFFERED.toString(),
                        1
                    ),
                    DashboardNotification(
                        15,
                        Date().toString(),
                        userId,
                        "a15",
                        DashboardNotificationTypes.WORKACCEPTED.toString(),
                        1
                    ),
                    DashboardNotification(
                        19,
                        Date().toString(),
                        "a17",
                        userId,
                        DashboardNotificationTypes.WORKOFFERED.toString(),
                        1
                    ),
                    DashboardNotification(
                        20,
                        Date().toString(),
                        "a10",
                        userId,
                        DashboardNotificationTypes.WORKOFFERED.toString(),
                        2
                    ),
                    DashboardNotification(
                        21,
                        Date().toString(),
                        "a11",
                        userId,
                        DashboardNotificationTypes.WORKREFUSED.toString(),
                        3
                    ),
                    DashboardNotification(
                        22,
                        Date().toString(),
                        "a12",
                        userId,
                        DashboardNotificationTypes.WORKREFUSED.toString(),
                        4
                    ),
                    DashboardNotification(
                        23,
                        Date().toString(),
                        "a13",
                        userId,
                        DashboardNotificationTypes.CONTACTINVITED.toString(),
                        1
                    ),
                    DashboardNotification(
                        24,
                        Date().toString(),
                        "a14",
                        userId,
                        DashboardNotificationTypes.CONTACTINVITED.toString(),
                        1
                    ),
                    DashboardNotification(
                        25,
                        Date().toString(),
                        "a18",
                        userId,
                        DashboardNotificationTypes.CONTACTINVITED.toString(),
                        1
                    ),
                    DashboardNotification(
                        26,
                        Date().toString(),
                        "a15",
                        userId,
                        DashboardNotificationTypes.CONTACTINVITED.toString(),
                        1
                    )
                )
            )
        }

    }

    private fun populateComments(coroutineScope: CoroutineScope, userId: String) {
        coroutineScope.launch {
            commentDao.deleteAll()
            commentDao.insert(
                mutableListOf(
                    Comments(1, userId, "a8", 4.5f, "Było w porządeczku", true),
                    Comments(2, userId, "a6", 4.8f, "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", true),
                    Comments(3, userId, "a9", 2f, "bbbbbbbbbbbbbbbbbbbbbbbbbbbbbb", true),
                    Comments(4, userId, "a5", 4f, "cccccccccccccccccccccccccccccccc", true),
                    Comments(5, userId, "a6", 0f, "dddddddddddddddddddddddddddddddddddddddd", false),
                    Comments(6, userId, "a6", 1f, "eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee", true),
                    Comments(7, userId, "a7", 3f, "ffffffffffffffffffffffffffffffffffff", false),
                    Comments(8, userId, "a5", 5f, "gggggggggggggggggggggggggggg", true),
                    Comments(9, userId, "a8", 2f, "hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh", true),
                    Comments(10, userId, "a3", 3f, "iiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii", false),
                    Comments(11, userId, "a5", 3.5f, "jjjjjjjjjjjjjjjjjjjjjjjjjjjjj", true),
                    Comments(12, userId, "a8", 4.9f, "kkkkkkkkkkkkkkkkkkkkkkkkkkkkk", false),
                    Comments(13, userId, "a9", 4.1f, "llllllllllllllllllllllllllllllllllllllllllllllllllllllll", true),
                    Comments(
                        14, userId, "a6", 3.3f, " zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz ", true
                    )
                )
            )
        }
    }

    private fun populateContacts(coroutineScope: CoroutineScope, userId: String) {
        coroutineScope.launch {
            contactDao.deleteAll()
            contactDao.insert(
                mutableListOf(
                    Contacts(0, userId, "a1"),
                    Contacts(0, userId, "a8"),
                    Contacts(0, userId, "a7"),
                    Contacts(0, userId, "a9")
                )
            )
        }
    }

    private fun populateCategories(coroutineScope: CoroutineScope) {
        coroutineScope.launch {
            categoryDao.deleteAll()
            categoryDao.insert(
                mutableListOf(
                    Categories(1, "IT"),
                    Categories(2, "Magazynier"),
                    Categories(3, "Ochrona"),
                    Categories(4, "Budowa"),
                    Categories(5, "Kasjer"),
                    Categories(6, "Java"),
                    Categories(7, "Android"),
                    Categories(8, "TuMożeByćCokolwiek"),
                    Categories(9, "Unity")
                )
            )
        }
    }

    private fun populateTaskCategories(coroutineScope: CoroutineScope, userId: String) {
        coroutineScope.launch {
            tasksCategoryCrossRefDao.deleteAll()
            tasksCategoryCrossRefDao.insert(
                mutableListOf(
                    TasksCategoryCrossRef(2, 1),
                    TasksCategoryCrossRef(2, 9),
                    TasksCategoryCrossRef(4, 2),
                    TasksCategoryCrossRef(4, 3),
                    TasksCategoryCrossRef(6, 4),
                    TasksCategoryCrossRef(6, 5),
                    TasksCategoryCrossRef(8, 6),
                    TasksCategoryCrossRef(8, 7),
                    TasksCategoryCrossRef(10, 8),
                    TasksCategoryCrossRef(10, 9),
                    TasksCategoryCrossRef(12, 1),
                    TasksCategoryCrossRef(12, 2)
                )
            )
        }
    }

    private fun populateUserCategories(coroutineScope: CoroutineScope, userId: String) {
        coroutineScope.launch {
            userCategoryCrossRefDao.deleteAll()
            userCategoryCrossRefDao.insert(
                mutableListOf(
                    UserCategoryCrossRef(userId, 1),
                    UserCategoryCrossRef(userId, 9),
                    UserCategoryCrossRef(userId, 7),
                    UserCategoryCrossRef("a1", 1),
                    UserCategoryCrossRef("a2", 8),
                    UserCategoryCrossRef("a4", 6),
                    UserCategoryCrossRef("a3", 7),
                    UserCategoryCrossRef("a5", 8),
                    UserCategoryCrossRef("a6", 9),
                    UserCategoryCrossRef("a7", 1),
                    UserCategoryCrossRef("a8", 2),
                    UserCategoryCrossRef("a9", 3),
                    UserCategoryCrossRef("a1", 4),
                    UserCategoryCrossRef("a2", 5),
                    UserCategoryCrossRef("a3", 6),
                    UserCategoryCrossRef("a4", 7),
                    UserCategoryCrossRef("a5", 3)
                )
            )
        }
    }


}




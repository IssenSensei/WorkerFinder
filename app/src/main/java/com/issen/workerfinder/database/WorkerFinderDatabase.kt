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
import com.issen.workerfinder.enums.PriorityTypes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.*


@Database(
    entities = [TaskModel::class, TaskModelPhotos::class, TaskModelRepeatDays::class, UserData::class, Categories::class,
        Comments::class, Contacts::class],
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
        populateTasks(coroutineScope)
        populateUsers(coroutineScope)
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
                        "user",
                        "Zbysiu Zbyś",
                        "22-12-2020",
                        CyclicTypes.NONE.toString(),
                        Date(),
                        PriorityTypes.URGENT.toString(),
                        CompletionTypes.ONGOING.toString(),
                        "completion"
                    ),
                    TaskModel(
                        2,
                        "Pomoc na budowie",
                        "Potrzebny pomocnik na budowie",
                        "user2",
                        "Zbysiu Robotnik Zbyś",
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
                        "user",
                        "Zbysiu Rudy Zbyś",
                        "03-02-2021",
                        CyclicTypes.NONE.toString(),
                        Date(),
                        PriorityTypes.LOW.toString(),
                        CompletionTypes.ONGOING.toString(),
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
                        CompletionTypes.PENDING.toString(),
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
                    )
                )
            )
        }
    }

    fun populateComments(coroutineScope: CoroutineScope, userId: Int) {
        coroutineScope.launch {
            commentsDao.deleteAll()
            commentsDao.insert(
                mutableListOf(
                    Comments(0, userId,3,4.5f, "Było w porządeczku", true),
                    Comments(0, userId,2, 4.8f, "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", true),
                    Comments(0, userId,3, 2f, "bbbbbbbbbbbbbbbbbbbbbbbbbbbbbb", true),
                    Comments(0, userId,4, 4f, "cccccccccccccccccccccccccccccccc", true),
                    Comments(0, userId,5, 0f, "dddddddddddddddddddddddddddddddddddddddd", false),
                    Comments(0, userId,2, 1f, "eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee", true),
                    Comments(0, userId,4, 3f, "ffffffffffffffffffffffffffffffffffff", false),
                    Comments(0, userId,6, 5f, "gggggggggggggggggggggggggggg", true),
                    Comments(0, userId,6, 2f, "hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh", true),
                    Comments(0, userId,5, 3f, "iiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii", false),
                    Comments(0, userId,6, 3.5f, "jjjjjjjjjjjjjjjjjjjjjjjjjjjjj", true),
                    Comments(0, userId,4, 4.9f, "kkkkkkkkkkkkkkkkkkkkkkkkkkkkk", false),
                    Comments(0, userId,2, 4.1f, "llllllllllllllllllllllllllllllllllllllllllllllllllllllll", true),
                    Comments(0, userId,3, 3.3f, "zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz", true)
                )
            )
        }
    }

    fun populateContacts(coroutineScope: CoroutineScope, userId: Int) {
        coroutineScope.launch {
            contactsDao.insert(
                mutableListOf(
                    Contacts(0, userId, 2),
                    Contacts(0, userId, 3),
                    Contacts(0, userId, 5),
                    Contacts(0, userId, 6)
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
                        0, "name lastName", "https://i.imgflip.com/15l4w6.jpg", "email", "0000000", "aaaaa", "random " +
                                "description", false, true
                    ),
                    UserData(
                        0, "name1 lastName1", "https://i.imgflip.com/15l4w6.jpg", "email1", "11111111", "aaaaaa1", "random " +
                                "description1", false, true
                    ),
                    UserData(
                        0, "name2 lastName2", "https://i.imgflip.com/15l4w6.jpg", "email2", "2222222222", "aaaaaa2", "random " +
                                "description2", true, true
                    ),
                    UserData(
                        0, "name3 lastName3", "https://i.imgflip.com/15l4w6.jpg", "email3", "3333333333", "aaaaaa3", "random " +
                                "description3", false, true
                    ),
                    UserData(
                        0, "name4 lastName4", "https://i.imgflip.com/15l4w6.jpg", "email4", "44444444444", "aaaaaa4", "random " +
                                "description4", true, true
                    ),
                    UserData(
                        0, "name5 lastName5", "https://i.imgflip.com/15l4w6.jpg", "email5", "555555555", "aaaaaa5", "random " +
                                "description5", true, true
                    ),
                    UserData(
                        0, "name6 lastName6", "https://i.imgflip.com/15l4w6.jpg", "email6", "6666666666", "aaaaaa6", "random " +
                                "description6", false, false
                    ),
                    UserData(
                        0, "name7 lastName7", "https://i.imgflip.com/15l4w6.jpg", "email7", "777777777", "aaaaaa7", "random " +
                                "description7", false, false
                    ),
                    UserData(
                        0, "name8 lastName8", "https://i.imgflip.com/15l4w6.jpg", "email8", "88888888", "aaaaaa8", "random " +
                                "description8", false, false
                    )
                )
            )
        }
    }


}




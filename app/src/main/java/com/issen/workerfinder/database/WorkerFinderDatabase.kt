package com.issen.workerfinder.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.issen.workerfinder.database.dao.TaskModelDao
import com.issen.workerfinder.database.dao.TaskPhotosDao
import com.issen.workerfinder.database.dao.TaskRepeatDaysDao
import com.issen.workerfinder.database.dao.UserModelDao
import com.issen.workerfinder.database.models.TaskModel
import com.issen.workerfinder.database.models.TaskModelPhotos
import com.issen.workerfinder.database.models.TaskModelRepeatDays
import com.issen.workerfinder.database.models.UserModel
import com.issen.workerfinder.enums.CompletionTypes
import com.issen.workerfinder.enums.CyclicTypes
import com.issen.workerfinder.enums.PriorityTypes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.*


@Database(entities = [TaskModel::class, TaskModelPhotos::class, TaskModelRepeatDays::class, UserModel::class], version = 1, exportSchema = false)
@TypeConverters(DataConverter::class)
abstract class WorkerFinderDatabase : RoomDatabase() {

    abstract val taskModelDao: TaskModelDao
    abstract val taskPhotosDao: TaskPhotosDao
    abstract val taskRepeatDaysDao: TaskRepeatDaysDao
    abstract val userModelDao: UserModelDao

    private class WorkerFinderDatabaseCallback(private val scope: CoroutineScope) :
        RoomDatabase.Callback() {
        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let {
                scope.launch {
                    val taskModelDao = it.taskModelDao
                    val taskPhotosDao = it.taskPhotosDao
                    val taskRepeatDaysDao = it.taskRepeatDaysDao
                    val userModelDao = it.userModelDao
                    taskModelDao.deleteAll()
                    taskPhotosDao.deleteAll()
                    taskRepeatDaysDao.deleteAll()
//                    userModelDao.deleteAll()
                    taskModelDao.insert(
                        mutableListOf(
                            TaskModel(
                                1,
                                "Odśnieżanie",
                                "Należy odśnieżyć całą posesję",
                                "user",
                                "Zbysiu Zbyś",
                                "category",
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
                                "category2",
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
                                "category",
                                "03-02-2021",
                                CyclicTypes.NONE.toString(),
                                Date(),
                                PriorityTypes.LOW.toString(),
                                CompletionTypes.ONGOING.toString(),
                                "completion"
                            )
                        )

                    )
                    taskPhotosDao.insert(
                        mutableListOf(
                            TaskModelPhotos(0, 1, "https://i.imgflip.com/15l4w6.jpg"),
                            TaskModelPhotos(0, 1, "https://i.imgflip.com/15l4w6.jpg"),
                            TaskModelPhotos(0, 2, "https://i.imgflip.com/15l4w6.jpg"),
                            TaskModelPhotos(0, 2, "https://i.imgflip.com/15l4w6.jpg"),
                            TaskModelPhotos(0, 2, "https://i.imgflip.com/15l4w6.jpg"),
                            TaskModelPhotos(0, 2, "https://i.imgflip.com/15l4w6.jpg")
                        )
                    )
                    taskRepeatDaysDao.insert(
                        mutableListOf(
                            TaskModelRepeatDays(0, 2, "aaaaa"),
                            TaskModelRepeatDays(0, 1, "bbbb"),
                            TaskModelRepeatDays(0, 2, "ccccc"),
                            TaskModelRepeatDays(0, 1, "ddddd"),
                            TaskModelRepeatDays(0, 2, "eeeeeeee")
                        )
                    )
//                    userModelDao.insert(
//                        mutableListOf(
//                            UserModel(1, "name lastName", "https://i.imgflip.com/15l4w6.jpg", "email", "111111111", "aaaaa1",false),
//                            UserModel(0, "name1 lastName1", "https://i.imgflip.com/15l4w6.jpg", "email1", "222222222", "aaaaaa2", false),
//                            UserModel(0, "name2 lastName2", "https://i.imgflip.com/15l4w6.jpg", "email2", "333333333", "aaaaaa3", false)
//                        )
//                    )

                }
            }
        }

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let {
                scope.launch {
                    val userModelDao = it.userModelDao
                    userModelDao.insert(
                        mutableListOf(
                            UserModel(1, "name lastName", "https://i.imgflip.com/15l4w6.jpg", "email", "111111111", "aaaaa1", false),
                            UserModel(0, "name1 lastName1", "https://i.imgflip.com/15l4w6.jpg", "email1", "222222222", "aaaaaa2", false),
                            UserModel(0, "name2 lastName2", "https://i.imgflip.com/15l4w6.jpg", "email2", "333333333", "aaaaaa3", false)
                        )
                    )
                }

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


}




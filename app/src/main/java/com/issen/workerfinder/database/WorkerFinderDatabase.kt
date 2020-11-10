package com.issen.workerfinder.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.issen.workerfinder.enums.CompletionTypes
import com.issen.workerfinder.enums.CyclicTypes
import com.issen.workerfinder.enums.PriorityTypes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.*


@Database(entities = [TaskModel::class, TaskModelPhotos::class, TaskModelRepeatDays::class], version = 1, exportSchema = false)
@TypeConverters(DataConverter::class)
abstract class WorkerFinderDatabase : RoomDatabase() {

    abstract val taskModelDao: TaskModelDao
    abstract val taskPhotosDao: TaskPhotosDao
    abstract val taskRepeatDaysDao: TaskRepeatDaysDao

    private class WorkerFinderDatabaseCallback(private val scope: CoroutineScope) :
        RoomDatabase.Callback() {
        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let {
                scope.launch {
                    val taskModelDao = it.taskModelDao
                    val taskPhotosDao = it.taskPhotosDao
                    val taskRepeatDaysDao = it.taskRepeatDaysDao
                    taskModelDao.deleteAll()
                    taskPhotosDao.deleteAll()
                    taskRepeatDaysDao.deleteAll()
                    taskModelDao.insert(
                        mutableListOf(
                            TaskModel(
                                1,
                                "title",
                                "description",
                                "user",
                                "worker",
                                "category",
                                "22-12-20202",
                                CyclicTypes.NONE.toString(),
                                Date(),
                                PriorityTypes.URGENT.toString(),
                                CompletionTypes.ONGOING.toString(),
                                "completion"
                            ),
                            TaskModel(
                                2,
                                "title2",
                                "description2",
                                "user2",
                                "worker2",
                                "category2",
                                "22-12-20202",
                                CyclicTypes.MONTHDAY.toString(),
                                Date(),
                                PriorityTypes.NORMAL.toString(),
                                CompletionTypes.COMPLETED.toString(),
                                "completion"
                            ),
                            TaskModel(
                                3,
                                "title",
                                "description",
                                "user",
                                "worker",
                                "category",
                                "22-12-20202",
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




package com.example.workerfinder.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [TaskModel::class], version = 1, exportSchema = false)
@TypeConverters(DataConverter::class)
abstract class TaskModelDatabase : RoomDatabase() {

    abstract val taskModelDao: TaskModelDao

    companion object {

        @Volatile
        private var INSTANCE: TaskModelDatabase? = null

        fun getInstance(context: Context): TaskModelDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        TaskModelDatabase::class.java,
                        "task_model_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}
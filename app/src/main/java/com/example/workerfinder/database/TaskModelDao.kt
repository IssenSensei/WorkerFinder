package com.example.workerfinder.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TaskModelDao {

    @Insert
    fun insert(taskModel: TaskModel)

    @Update
    fun update(taskModel: TaskModel)

    @Delete
    fun delete(taskModel: TaskModel)

    @Query("SELECT * FROM task_table WHERE task_completion_type LIKE 'ONGOING'")
    fun getActiveTasks(): LiveData<List<TaskModel>>

    @Query("SELECT * FROM task_table WHERE task_completion_type NOT LIKE 'ONGOING'")
    fun getInactiveTasks(): LiveData<List<TaskModel>>
}
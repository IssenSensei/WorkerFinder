package com.issen.workerfinder.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TaskModelDao {

    @Insert
    suspend fun insert(taskModel: TaskModel) : Long

    @Insert
    suspend fun insert(taskModelList: MutableList<TaskModel>)

    @Update
    fun update(taskModel: TaskModel)

    @Delete
    fun delete(taskModel: TaskModel)

    @Query("DELETE FROM task_table")
    suspend fun deleteAll()

    @Transaction
    @Query("SELECT * FROM task_table")
    fun getAllTasks(): LiveData<List<FullTaskModel>>

    @Transaction
    @Query("SELECT * FROM task_table WHERE task_completion_type LIKE 'ONGOING'")
    fun getActiveTasks(): LiveData<List<FullTaskModel>>

    @Transaction
    @Query("SELECT * FROM task_table WHERE task_completion_type NOT LIKE 'ONGOING'")
    fun getInactiveTasks(): LiveData<List<FullTaskModel>>

    @Update
    fun completeTask(taskModel: TaskModel)

}
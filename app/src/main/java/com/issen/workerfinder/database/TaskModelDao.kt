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

    @Query("SELECT count(*) FROM task_table where task_worker = :firebaseKey and task_completion_type = 'ONGOING'")
    suspend fun getActiveTasksCount(firebaseKey: String): Int

    @Query("SELECT count(*) FROM task_table where task_worker = :firebaseKey and task_completion_type = 'COMPLETED'")
    suspend fun getCompletedTasksCount(firebaseKey: String): Int

    @Query("SELECT count(*) FROM task_table where task_worker = :firebaseKey and task_completion_type = 'ABANDONED'")
    suspend fun getAbandonedTasksCount(firebaseKey: String): Int

    @Query("UPDATE task_table set task_completion_type = 'COMPLETED' where taskId = :taskId")
    suspend fun completeTask(taskId: Int)

    @Query("UPDATE task_table set task_completion_type = 'ABANDONED' where taskId = :taskId")
    suspend fun abandonTask(taskId: Int)
}
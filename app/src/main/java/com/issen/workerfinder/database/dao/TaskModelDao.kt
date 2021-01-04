package com.issen.workerfinder.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.sqlite.db.SimpleSQLiteQuery
import com.issen.workerfinder.database.models.TaskModel
import com.issen.workerfinder.database.models.TaskModelFull

@Dao
interface TaskModelDao {

    @Insert
    suspend fun insert(taskModel: TaskModel): Long

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
    fun getAllTasks(): LiveData<List<TaskModelFull>>

    @Transaction
    @Query("SELECT * FROM task_table WHERE task_worker_id = '' AND task_completion_type = 'PENDING'")
    fun getBoardTasks(): LiveData<List<TaskModelFull>>

    @Transaction
    @Query("SELECT * FROM task_table WHERE task_completion_type LIKE 'ACTIVE'")
    fun getActiveTasks(): LiveData<List<TaskModelFull>>

    @Transaction
    @Query("SELECT * FROM task_table WHERE task_completion_type NOT LIKE 'ACTIVE'")
    fun getInactiveTasks(): LiveData<List<TaskModelFull>>

    @Query("SELECT count(*) FROM task_table where task_worker_id = :firebaseKey and task_completion_type = 'ACTIVE'")
    suspend fun getActiveTasksCount(firebaseKey: String): Int

    @Query("SELECT count(*) FROM task_table where task_worker_id = :firebaseKey and task_completion_type = 'COMPLETED'")
    suspend fun getCompletedTasksCount(firebaseKey: String): Int

    @Query("SELECT count(*) FROM task_table where task_worker_id = :firebaseKey and task_completion_type = 'ABANDONED'")
    suspend fun getAbandonedTasksCount(firebaseKey: String): Int

    @Query("UPDATE task_table set task_completion_type = 'PENDING' where taskId = :taskId")
    suspend fun markTaskAsPending(taskId: Int)

    @Query("UPDATE task_table set task_completion_type = 'COMPLETED' where taskId = :taskId")
    suspend fun markTaskAsCompleted(taskId: Int)

    @Query("UPDATE task_table set task_completion_type = 'ACTIVE' where taskId = :taskId")
    suspend fun markTaskAsActive(taskId: Int)

    @Query("UPDATE task_table set task_completion_type = 'ABANDONED' where taskId = :taskId")
    suspend fun abandonTask(taskId: Int)

    @RawQuery
    fun getTasksQueried(query: SimpleSQLiteQuery): LiveData<List<TaskModelFull>>

    @Transaction
    @Query("SELECT * FROM task_table WHERE task_user_id <> :userId AND task_worker_id = :userId")
    fun getAllAcceptedTasks(userId: String): LiveData<List<TaskModelFull>>

    @Transaction
    @Query("SELECT * FROM task_table WHERE task_user_id = :userId AND task_worker_id <> :userId")
    fun getAllCommissionedTasks(userId: String): LiveData<List<TaskModelFull>>

    @Transaction
    @Query("SELECT * FROM task_table WHERE task_user_id = :userId AND task_worker_id = :userId")
    fun getAllCreatedTasks(userId: String): LiveData<List<TaskModelFull>>

    @Transaction
    @Query("SELECT DISTINCT * FROM task_table WHERE taskId in (SELECT modifiedRecordId from dashboard_notifications_table where notificationCausedByUserId =" +
            " :userId AND (dashboardNotificationType = 'WORKOFFERED' OR dashboardNotificationType = 'WORKREFUSED'))")
    fun getUserInvitations(userId: String): LiveData<List<TaskModelFull>>

    @Query("UPDATE task_table set task_user_id = :userId where taskId = :modifiedRecordId")
    suspend fun acceptTask(modifiedRecordId: Int, userId: String)
}
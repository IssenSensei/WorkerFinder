package com.issen.workerfinder.ui.taskList

import android.app.Application
import androidx.lifecycle.*
import androidx.sqlite.db.SimpleSQLiteQuery
import com.issen.workerfinder.TaskApplication
import com.issen.workerfinder.database.DashboardNotificationsRepository
import com.issen.workerfinder.database.models.TaskModelFull
import com.issen.workerfinder.database.models.TaskModel
import com.issen.workerfinder.database.TaskModelRepository
import com.issen.workerfinder.database.WorkerFinderDatabase
import com.issen.workerfinder.database.models.DashboardNotification
import com.issen.workerfinder.enums.DashboardNotificationTypes
import com.issen.workerfinder.ui.misc.TaskListFilter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class TaskListViewModel(application: Application) : AndroidViewModel(application) {

    private val taskModelRepository: TaskModelRepository
    private val dashboardNotificationsRepository: DashboardNotificationsRepository
//    var taskList: LiveData<List<FullTaskModel>>
    lateinit var source: LiveData<List<TaskModelFull>>
    val mediatorLiveData: MediatorLiveData<List<TaskModelFull>> = MediatorLiveData()
    init {
        val database = WorkerFinderDatabase.getDatabase(application, viewModelScope)
        val taskModelDao = database.taskModelDao
        val taskPhotosDao = database.taskPhotosDao
        val taskRepeatDaysDao = database.taskRepeatDaysDao
        val userModelDao = database.userDataDao
        val commentsDao = database.commentsDao
        val dashboardNotificationDao = database.dashboardNotificationDao
        taskModelRepository = TaskModelRepository(taskModelDao, taskPhotosDao, taskRepeatDaysDao, userModelDao, commentsDao)
        dashboardNotificationsRepository = DashboardNotificationsRepository(dashboardNotificationDao)
        source = taskModelRepository.allTasks
        mediatorLiveData.addSource(source) {
            mediatorLiveData.setValue(
                it
            )
        }
//        taskList = repository.allTasks
    }


    fun insert(taskModel: TaskModel) = viewModelScope.launch(Dispatchers.IO) {
        taskModelRepository.insert(taskModel)
    }

    fun markTaskAsCompleted(taskModel: TaskModel) {
        viewModelScope.launch(Dispatchers.IO) {
            taskModelRepository.markTaskAsCompleted(taskModel.taskId)
        }
    }

    fun markTaskAsPending(taskModel: TaskModel) {
        viewModelScope.launch(Dispatchers.IO) {
            taskModelRepository.markTaskAsPending(taskModel.taskId)
            dashboardNotificationsRepository.notify(
                DashboardNotification(
                    0,
                    Date().toString(),
                    taskModel.userFirebaseKey,
                    TaskApplication.currentLoggedInUserFull!!.userData.userId,
                    DashboardNotificationTypes.TASKCOMPLETED.toString(),
                    taskModel.taskId,
                    false
                )
            )
        }
    }

    fun requery(selectedTaskListFilter: TaskListFilter) {
        mediatorLiveData.removeSource(source)
        source = taskModelRepository.getTasksQueried(setQuerySource(selectedTaskListFilter))
        mediatorLiveData.addSource(source) {
            mediatorLiveData.setValue(
                it
            )
        }
    }

    private fun setQuerySource(selectedTaskListFilter: TaskListFilter): SimpleSQLiteQuery {
        var queryString = "SELECT * FROM task_table ";
        var queryArgs = arrayListOf<Any>()
        var shouldAddAnd = false

        if(selectedTaskListFilter.filterByWorker.isNotEmpty()){
            queryString += " WHERE task_worker_id IN ("
            selectedTaskListFilter.filterByWorker.forEachIndexed { index, item ->
                queryString += if(index == 0) "? " else ", ?"
                queryArgs.add(item)
            }
            queryString += ")"
            shouldAddAnd = true
        }
        if(selectedTaskListFilter.filterByUser.isNotEmpty()){
            queryString += if(shouldAddAnd) " and" else " WHERE"
            queryString += " task_user_id IN ("
            selectedTaskListFilter.filterByUser.forEachIndexed { index, item ->
                queryString += if(index == 0) "? " else ", ?"
                queryArgs.add(item)
            }
            queryString += ")"
            shouldAddAnd = true
        }
        if(selectedTaskListFilter.filterByCyclic.isNotEmpty()){
            queryString += if(shouldAddAnd) " and" else " WHERE"
            queryString += " task_cyclic_type IN ("
            selectedTaskListFilter.filterByCyclic.forEachIndexed { index, item ->
                queryString += if(index == 0) "? " else ", ?"
                queryArgs.add(item)
            }
            queryString += ")"
            shouldAddAnd = true
        }
        if(selectedTaskListFilter.filterByPriority.isNotEmpty()){
            queryString += if(shouldAddAnd) " and" else " WHERE"
            queryString += " task_priority_type IN ("
            selectedTaskListFilter.filterByPriority.forEachIndexed { index, item ->
                queryString += if(index == 0) "? " else ", ?"
                queryArgs.add(item)
            }
            queryString += ")"
            shouldAddAnd = true
        }
        if(selectedTaskListFilter.filterByCompletionType.isNotEmpty()){
            queryString += if(shouldAddAnd) " and" else " WHERE"
            queryString += " task_completion_type IN ("
            selectedTaskListFilter.filterByCompletionType.forEachIndexed { index, item ->
                queryString += if(index == 0) "? " else ", ?"
                queryArgs.add(item)
            }
            queryString += ")"
        }
        if(selectedTaskListFilter.sortBy != "none"){
            queryString += " order by ?" + if(selectedTaskListFilter.orderAscending) "asc" else "desc"
            queryArgs.add(selectedTaskListFilter.sortBy)
        }


        return SimpleSQLiteQuery(queryString, queryArgs.toArray())
    }



}
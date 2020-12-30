package com.issen.workerfinder.ui.taskList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.sqlite.db.SimpleSQLiteQuery
import com.issen.workerfinder.WorkerFinderApplication
import com.issen.workerfinder.database.models.DashboardNotification
import com.issen.workerfinder.database.models.TaskModel
import com.issen.workerfinder.database.models.TaskModelFull
import com.issen.workerfinder.database.repositories.DashboardNotificationRepository
import com.issen.workerfinder.database.repositories.TaskRepository
import com.issen.workerfinder.enums.DashboardNotificationTypes
import com.issen.workerfinder.ui.filters.FilterContainer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class TaskListViewModel(
    private val taskRepository: TaskRepository,
    private val dashboardNotificationRepository: DashboardNotificationRepository
) : ViewModel() {

    //    var taskList: LiveData<List<FullTaskModel>>
    var source: LiveData<List<TaskModelFull>>
    val mediatorLiveData: MediatorLiveData<List<TaskModelFull>> = MediatorLiveData()

    init {
        source = taskRepository.allTasks
        mediatorLiveData.addSource(source) {
            mediatorLiveData.setValue(
                it
            )
        }
//        taskList = repository.allTasks
    }


    fun insert(taskModel: TaskModel) = viewModelScope.launch(Dispatchers.IO) {
        taskRepository.insert(taskModel)
    }

    fun markTaskAsCompleted(taskModel: TaskModel) {
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.markTaskAsCompleted(taskModel.taskId)
        }
    }

    fun markTaskAsPending(taskModel: TaskModel) {
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.markTaskAsPending(taskModel.taskId)
            dashboardNotificationRepository.notify(
                DashboardNotification(
                    0,
                    Date().toString(),
                    taskModel.userFirebaseKey,
                    WorkerFinderApplication.currentLoggedInUserFull!!.userData.userId,
                    DashboardNotificationTypes.TASKCOMPLETED.toString(),
                    taskModel.taskId,
                    false
                )
            )
        }
    }

    fun requery(selectedFilterContainer: FilterContainer) {
        mediatorLiveData.removeSource(source)
        source = taskRepository.getTasksQueried(setQuerySource(selectedFilterContainer))
        mediatorLiveData.addSource(source) {
            mediatorLiveData.setValue(
                it
            )
        }
    }

    private fun setQuerySource(selectedFilterContainer: FilterContainer): SimpleSQLiteQuery {
        var queryString = "SELECT * FROM task_table ";
        var queryArgs = arrayListOf<Any>()
        var shouldAddAnd = false

        if (selectedFilterContainer.filterByWorker.isNotEmpty()) {
            queryString += " WHERE task_worker_id IN ("
            selectedFilterContainer.filterByWorker.forEachIndexed { index, item ->
                queryString += if (index == 0) "? " else ", ?"
                queryArgs.add(item)
            }
            queryString += ")"
            shouldAddAnd = true
        }
        if (selectedFilterContainer.filterByUser.isNotEmpty()) {
            queryString += if (shouldAddAnd) " and" else " WHERE"
            queryString += " task_user_id IN ("
            selectedFilterContainer.filterByUser.forEachIndexed { index, item ->
                queryString += if (index == 0) "? " else ", ?"
                queryArgs.add(item)
            }
            queryString += ")"
            shouldAddAnd = true
        }
        if (selectedFilterContainer.filterByCyclic.isNotEmpty()) {
            queryString += if (shouldAddAnd) " and" else " WHERE"
            queryString += " task_cyclic_type IN ("
            selectedFilterContainer.filterByCyclic.forEachIndexed { index, item ->
                queryString += if (index == 0) "? " else ", ?"
                queryArgs.add(item)
            }
            queryString += ")"
            shouldAddAnd = true
        }
        if (selectedFilterContainer.filterByPriority.isNotEmpty()) {
            queryString += if (shouldAddAnd) " and" else " WHERE"
            queryString += " task_priority_type IN ("
            selectedFilterContainer.filterByPriority.forEachIndexed { index, item ->
                queryString += if (index == 0) "? " else ", ?"
                queryArgs.add(item)
            }
            queryString += ")"
            shouldAddAnd = true
        }
        if (selectedFilterContainer.filterByCompletionType.isNotEmpty()) {
            queryString += if (shouldAddAnd) " and" else " WHERE"
            queryString += " task_completion_type IN ("
            selectedFilterContainer.filterByCompletionType.forEachIndexed { index, item ->
                queryString += if (index == 0) "? " else ", ?"
                queryArgs.add(item)
            }
            queryString += ")"
        }
        if (selectedFilterContainer.sortBy != "none") {
            queryString += " order by ?" + if (selectedFilterContainer.orderAscending) " asc" else " desc"
            queryArgs.add(selectedFilterContainer.sortBy)
        }


        return SimpleSQLiteQuery(queryString, queryArgs.toArray())
    }


}
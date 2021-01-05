package com.issen.workerfinder.ui.taskList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.sqlite.db.SimpleSQLiteQuery
import com.google.firebase.auth.FirebaseAuth
import com.issen.workerfinder.WorkerFinderApplication.Companion.currentLoggedInUserFull
import com.issen.workerfinder.database.models.Comments
import com.issen.workerfinder.database.models.DashboardNotification
import com.issen.workerfinder.database.models.TaskModel
import com.issen.workerfinder.database.models.TaskModelFull
import com.issen.workerfinder.database.repositories.CommentRepository
import com.issen.workerfinder.database.repositories.DashboardNotificationRepository
import com.issen.workerfinder.database.repositories.TaskRepository
import com.issen.workerfinder.enums.DashboardNotificationTypes
import com.issen.workerfinder.ui.filters.FilterContainer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class AcceptedTaskListViewModel(
    private val taskRepository: TaskRepository,
    private val dashboardNotificationRepository: DashboardNotificationRepository,
    private val commentRepository: CommentRepository
) : ViewModel() {

    private var auth = FirebaseAuth.getInstance()
    private var source: LiveData<List<TaskModelFull>>
    var popupTask: TaskModelFull? = null
    val mediatorLiveData: MediatorLiveData<List<TaskModelFull>> = MediatorLiveData()

    init {
        source = taskRepository.getAllAcceptedTasks(auth.currentUser!!.uid)
        mediatorLiveData.addSource(source) {
            mediatorLiveData.setValue(
                it
            )
        }
    }


    fun insert(taskModel: TaskModel) = viewModelScope.launch(Dispatchers.IO) {
        taskRepository.insert(taskModel)
    }

    fun markTaskAsPending(taskModel: TaskModel) {
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.markTaskAsPending(taskModel.taskId)
            dashboardNotificationRepository.notify(
                DashboardNotification(
                    0,
                    Date().toString(),
                    taskModel.userFirebaseKey,
                    currentLoggedInUserFull!!.userData.userId,
                    DashboardNotificationTypes.TASKCOMPLETED.toString(),
                    taskModel.taskId
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
        var queryString = "SELECT * FROM task_table WHERE task_user_id <> ? AND task_worker_id = ?";
        var queryArgs = arrayListOf<Any>()
        queryArgs.add(currentLoggedInUserFull!!.userData.userId)
        queryArgs.add(currentLoggedInUserFull!!.userData.userId)

        if (selectedFilterContainer.filterByUser.isNotEmpty()) {
            queryString += " and task_user_id IN ("
            selectedFilterContainer.filterByUser.forEachIndexed { index, item ->
                queryString += if (index == 0) "? " else ", ?"
                queryArgs.add(item)
            }
            queryString += ")"
        }
        if (selectedFilterContainer.filterByCyclic.isNotEmpty()) {
            queryString += " and task_cyclic_type IN ("
            selectedFilterContainer.filterByCyclic.forEachIndexed { index, item ->
                queryString += if (index == 0) "? " else ", ?"
                queryArgs.add(item)
            }
            queryString += ")"
        }
        if (selectedFilterContainer.filterByPriority.isNotEmpty()) {
            queryString += " and task_priority_type IN ("
            selectedFilterContainer.filterByPriority.forEachIndexed { index, item ->
                queryString += if (index == 0) "? " else ", ?"
                queryArgs.add(item)
            }
            queryString += ")"
        }
        if (selectedFilterContainer.filterByCompletionType.isNotEmpty()) {
            queryString += " and task_completion_type IN ("
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

    fun rateUser(rating: Float, comment: String) {
        viewModelScope.launch {
            commentRepository.addRating(
                Comments(
                    0,
                    popupTask!!.task.userFirebaseKey,
                    popupTask!!.task.workerFirebaseKey,
                    rating,
                    comment,
                    true
                )
            )
            dashboardNotificationRepository.notify(
                DashboardNotification(
                    0,
                    Date().toString(),
                    popupTask!!.task.userFirebaseKey,
                    popupTask!!.task.workerFirebaseKey,
                    DashboardNotificationTypes.RATEDBYWORKER.toString(),
                    popupTask!!.task.taskId
                )
            )
        }
    }


}
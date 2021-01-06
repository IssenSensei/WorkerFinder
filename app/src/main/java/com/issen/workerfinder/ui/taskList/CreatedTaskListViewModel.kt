package com.issen.workerfinder.ui.taskList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.sqlite.db.SimpleSQLiteQuery
import com.google.firebase.auth.FirebaseAuth
import com.issen.workerfinder.WorkerFinderApplication.Companion.currentLoggedInUserFull
import com.issen.workerfinder.database.models.TaskModel
import com.issen.workerfinder.database.models.TaskModelFull
import com.issen.workerfinder.database.repositories.TaskRepository
import com.issen.workerfinder.ui.filters.FilterContainer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CreatedTaskListViewModel(
    private val taskRepository: TaskRepository
) : ViewModel() {

    private var auth = FirebaseAuth.getInstance()
    private var source: LiveData<List<TaskModelFull>>
    val mediatorLiveData: MediatorLiveData<List<TaskModelFull>> = MediatorLiveData()

    init {
        source = taskRepository.getAllCreatedTasks(auth.currentUser!!.uid)
        mediatorLiveData.addSource(source) {
            mediatorLiveData.setValue(
                it
            )
        }
    }

    fun markTaskAsCompleted(taskModel: TaskModel) {
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.markTaskAsCompleted(taskModel.taskId)
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
        var queryString = "SELECT * FROM task_table WHERE task_worker_id = ? and task_user_id = ?";
        var queryArgs = arrayListOf<Any>()
        queryArgs.add(currentLoggedInUserFull!!.userData.userId)
        queryArgs.add(currentLoggedInUserFull!!.userData.userId)


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
            queryString += " order by " + selectedFilterContainer.sortBy + if (selectedFilterContainer.orderAscending) " asc" else " desc"
        }


        return SimpleSQLiteQuery(queryString, queryArgs.toArray())
    }


}
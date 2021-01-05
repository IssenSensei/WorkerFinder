package com.issen.workerfinder.ui.taskBoard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.sqlite.db.SimpleSQLiteQuery
import com.issen.workerfinder.database.models.TaskModelFull
import com.issen.workerfinder.database.repositories.TaskRepository
import com.issen.workerfinder.ui.filters.FilterContainer

class TaskBoardViewModel(private val taskRepository: TaskRepository) : ViewModel() {

    var source: LiveData<List<TaskModelFull>>
    val mediatorLiveData: MediatorLiveData<List<TaskModelFull>> = MediatorLiveData()
    init {
        source = taskRepository.getBoardTasks()
        mediatorLiveData.addSource(source) {
            mediatorLiveData.setValue(
                it
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
        var queryString = "SELECT * FROM task_table WHERE task_worker_id = '' AND task_completion_type = 'BOARD'";
        var queryArgs = arrayListOf<Any>()

        if(selectedFilterContainer.filterByUser.isNotEmpty()){
            queryString += " and task_user_id IN ("
            selectedFilterContainer.filterByUser.forEachIndexed { index, item ->
                queryString += if(index == 0) "? " else ", ?"
                queryArgs.add(item)
            }
            queryString += ")"
        }
        if(selectedFilterContainer.filterByCyclic.isNotEmpty()){
            queryString += " and task_cyclic_type IN ("
            selectedFilterContainer.filterByCyclic.forEachIndexed { index, item ->
                queryString += if(index == 0) "? " else ", ?"
                queryArgs.add(item)
            }
            queryString += ")"
        }
        if(selectedFilterContainer.filterByCategory.isNotEmpty()){
            queryString += " category IN ("
            selectedFilterContainer.filterByCategory.forEachIndexed { index, item ->
                queryString += if(index == 0) "? " else ", ?"
                queryArgs.add(item)
            }
            queryString += ")"
        }
        if(selectedFilterContainer.filterByPay.isNotEmpty()){
            queryString += " and pay IN ("
            selectedFilterContainer.filterByPay.forEachIndexed { index, item ->
                queryString += if(index == 0) "? " else ", ?"
                queryArgs.add(item)
            }
            queryString += ")"
        }
        if(selectedFilterContainer.filterByRating.isNotEmpty()){
            queryString += " rating IN ("
            selectedFilterContainer.filterByRating.forEachIndexed { index, item ->
                queryString += if(index == 0) "? " else ", ?"
                queryArgs.add(item)
            }
            queryString += ")"
        }
        if(selectedFilterContainer.filterByLocalization.isNotEmpty()){
            queryString += " and localization IN ("
            selectedFilterContainer.filterByLocalization.forEachIndexed { index, item ->
                queryString += if(index == 0) "? " else ", ?"
                queryArgs.add(item)
            }
            queryString += ")"
        }
        if(selectedFilterContainer.filterDueDate.isNotEmpty()){
            queryString += " and task_next_completion_date IN ("
            selectedFilterContainer.filterDueDate.forEachIndexed { index, item ->
                queryString += if(index == 0) "? " else ", ?"
                queryArgs.add(item)
            }
            queryString += ")"
        }

        if(selectedFilterContainer.sortBy != "none"){
            queryString += " order by ?" + if(selectedFilterContainer.orderAscending) " asc" else " desc"
            queryArgs.add(selectedFilterContainer.sortBy)
        }


        return SimpleSQLiteQuery(queryString, queryArgs.toArray())
    }
}
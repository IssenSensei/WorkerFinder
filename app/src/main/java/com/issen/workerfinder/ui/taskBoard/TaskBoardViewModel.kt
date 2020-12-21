package com.issen.workerfinder.ui.taskBoard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.viewModelScope
import androidx.sqlite.db.SimpleSQLiteQuery
import com.issen.workerfinder.database.TaskModelRepository
import com.issen.workerfinder.database.WorkerFinderDatabase
import com.issen.workerfinder.database.models.TaskModelFull
import com.issen.workerfinder.ui.filters.FilterContainer

class TaskBoardViewModel(application: Application) : AndroidViewModel(application) {

    private val taskModelRepository: TaskModelRepository
    var source: LiveData<List<TaskModelFull>>
    val mediatorLiveData: MediatorLiveData<List<TaskModelFull>> = MediatorLiveData()
    init {
        val database = WorkerFinderDatabase.getDatabase(application, viewModelScope)
        val taskModelDao = database.taskModelDao
        val taskPhotosDao = database.taskPhotosDao
        val taskRepeatDaysDao = database.taskRepeatDaysDao
        val userModelDao = database.userDataDao
        val commentsDao = database.commentsDao
        taskModelRepository = TaskModelRepository(taskModelDao, taskPhotosDao, taskRepeatDaysDao, userModelDao, commentsDao)

        source = database.taskModelDao.getBoardTasks()
        mediatorLiveData.addSource(source) {
            mediatorLiveData.setValue(
                it
            )
        }
    }

    fun requery(selectedFilterContainer: FilterContainer) {
        mediatorLiveData.removeSource(source)
        source = taskModelRepository.getTasksQueried(setQuerySource(selectedFilterContainer))
        mediatorLiveData.addSource(source) {
            mediatorLiveData.setValue(
                it
            )
        }
    }

    private fun setQuerySource(selectedFilterContainer: FilterContainer): SimpleSQLiteQuery {
        var queryString = "SELECT * FROM task_table WHERE task_worker_id = '' AND task_completion_type = 'PENDING'";
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
//        if(selectedFilterContainer.filterByCategory.isNotEmpty()){
//            queryString += " category IN ("
//            selectedFilterContainer.filterByCategory.forEachIndexed { index, item ->
//                queryString += if(index == 0) "? " else ", ?"
//                queryArgs.add(item)
//            }
//            queryString += ")"
//        }
        if(selectedFilterContainer.filterByPay.isNotEmpty()){
            queryString += " and pay IN ("
            selectedFilterContainer.filterByPay.forEachIndexed { index, item ->
                queryString += if(index == 0) "? " else ", ?"
                queryArgs.add(item)
            }
            queryString += ")"
        }
//        if(selectedFilterContainer.filterByRating.isNotEmpty()){
//            queryString += " rating IN ("
//            selectedFilterContainer.filterByRating.forEachIndexed { index, item ->
//                queryString += if(index == 0) "? " else ", ?"
//                queryArgs.add(item)
//            }
//            queryString += ")"
//        }
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
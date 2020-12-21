package com.issen.workerfinder.ui.workerBoard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.viewModelScope
import androidx.sqlite.db.SimpleSQLiteQuery
import com.issen.workerfinder.database.TaskModelRepository
import com.issen.workerfinder.database.WorkerFinderDatabase
import com.issen.workerfinder.database.models.TaskModelFull
import com.issen.workerfinder.database.models.UserDataFull
import com.issen.workerfinder.ui.filters.FilterContainer

class WorkerBoardViewModel(application: Application) : AndroidViewModel(application) {
    private val taskModelRepository: TaskModelRepository
    var source: LiveData<List<UserDataFull>>
    val mediatorLiveData: MediatorLiveData<List<UserDataFull>> = MediatorLiveData()
    init {
        val database = WorkerFinderDatabase.getDatabase(application, viewModelScope)
        val taskModelDao = database.taskModelDao
        val taskPhotosDao = database.taskPhotosDao
        val taskRepeatDaysDao = database.taskRepeatDaysDao
        val userModelDao = database.userDataDao
        val commentsDao = database.commentsDao
        taskModelRepository = TaskModelRepository(taskModelDao, taskPhotosDao, taskRepeatDaysDao, userModelDao, commentsDao)

        source = database.userDataDao.getBoardWorkers()
        mediatorLiveData.addSource(source) {
            mediatorLiveData.setValue(
                it
            )
        }
    }

    fun requery(selectedFilterContainer: FilterContainer) {
        mediatorLiveData.removeSource(source)
        source = taskModelRepository.getUsersQueried(setQuerySource(selectedFilterContainer))
        mediatorLiveData.addSource(source) {
            mediatorLiveData.setValue(
                it
            )
        }
    }

    private fun setQuerySource(selectedFilterContainer: FilterContainer): SimpleSQLiteQuery {
        var queryString = "SELECT * FROM user_table WHERE isAccountPublic = 1 AND isOpenForWork = 1";
        var queryArgs = arrayListOf<Any>()
        //        if(selectedFilterContainer.filterByCategory.isNotEmpty()){
//            queryString += " category IN ("
//            selectedFilterContainer.filterByCategory.forEachIndexed { index, item ->
//                queryString += if(index == 0) "? " else ", ?"
//                queryArgs.add(item)
//            }
//            queryString += ")"
//        }
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
        if(selectedFilterContainer.sortBy != "none"){
            queryString += " order by ?" + if(selectedFilterContainer.orderAscending) " asc" else " desc"
            queryArgs.add(selectedFilterContainer.sortBy)
        }

        return SimpleSQLiteQuery(queryString, queryArgs.toArray())
    }
}
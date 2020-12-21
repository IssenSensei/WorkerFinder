package com.issen.workerfinder

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.issen.workerfinder.TaskApplication.Companion.currentLoggedInUserFull
import com.issen.workerfinder.database.WorkerFinderDatabase
import com.issen.workerfinder.database.models.UserDataFull
import com.issen.workerfinder.ui.filters.FilterContainer

class MainActivityViewModel(application: Application) : AndroidViewModel(application) {

    //todo get data from shared prefs on start and read to learn about possible better ways to do this
    var currentTaskListFilter = FilterContainer()
    var selectedTaskListFilter: FilterContainer = currentTaskListFilter

    var currentWorkerBoardFilter = FilterContainer()
    var selectedWorkerBoardFilter: FilterContainer = currentWorkerBoardFilter

    var currentTaskBoardFilter = FilterContainer()
    var selectedTaskBoardFilter: FilterContainer = currentTaskBoardFilter

    var workerList: LiveData<List<UserDataFull>>
    var userListFull: LiveData<List<UserDataFull>>

    init {
        val database = WorkerFinderDatabase.getDatabase(application, viewModelScope)
        workerList = database.userDataDao.getUserWorkers(currentLoggedInUserFull!!.userData.userId)
        userListFull = database.userDataDao.getUserUsers(currentLoggedInUserFull!!.userData.userId)
    }

    fun setFilter(name: String, list: MutableList<String>){
        if (list.contains(name)) {
            list.remove(name)
        } else {
            list.add(name)
        }
    }

    fun setOrder(orderAscending: Boolean, filterContainer: FilterContainer) {
        filterContainer.orderAscending = orderAscending
    }

    fun setSort(selectedSortValue: String, filterContainer: FilterContainer) {
        filterContainer.sortBy = selectedSortValue
    }

    fun setGroup(selectedGroupValue: String, filterContainer: FilterContainer) {
        filterContainer.groupBy = selectedGroupValue
    }

    fun applyTaskListFilters(){
        currentTaskListFilter = selectedTaskListFilter
    }

    fun clearSelectedTaskListFilters() {
        selectedTaskListFilter = currentTaskListFilter
    }

    fun applyTaskBoardFilters(){
        currentTaskBoardFilter = selectedTaskBoardFilter
    }

    fun clearSelectedTaskBoardFilters() {
        selectedTaskBoardFilter = currentTaskBoardFilter
    }

    fun applyWorkerBoardFilters(){
        currentWorkerBoardFilter = selectedWorkerBoardFilter
    }

    fun clearSelectedWorkerBoardFilters() {
        selectedWorkerBoardFilter = currentWorkerBoardFilter
    }

}
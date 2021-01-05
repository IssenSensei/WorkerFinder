package com.issen.workerfinder

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.issen.workerfinder.WorkerFinderApplication.Companion.currentLoggedInUserFull
import com.issen.workerfinder.database.WorkerFinderDatabase
import com.issen.workerfinder.database.models.UserDataFull
import com.issen.workerfinder.database.repositories.UserRepository
import com.issen.workerfinder.ui.filters.FilterContainer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivityViewModel(private val userRepository: UserRepository, private val database: WorkerFinderDatabase) : ViewModel() {

    //todo get data from shared prefs on start and read to learn about possible better ways to do this
    var currentCreatedTaskListFilter = FilterContainer()
    var selectedCreatedTaskListFilter: FilterContainer = currentCreatedTaskListFilter

    var currentAcceptedTaskListFilter = FilterContainer()
    var selectedAcceptedTaskListFilter: FilterContainer = currentAcceptedTaskListFilter

    var currentCommissionedTaskListFilter = FilterContainer()
    var selectedCommissionedTaskListFilter: FilterContainer = currentCommissionedTaskListFilter

    var currentTaskBoardFilter = FilterContainer()
    var selectedTaskBoardFilter: FilterContainer = currentTaskBoardFilter

    var currentContactListFilter = FilterContainer()
    var selectedContactListFilter: FilterContainer = currentContactListFilter

    var currentContactAddFilter = FilterContainer()
    var selectedContactAddFilter: FilterContainer = currentContactAddFilter

    var currentContactBoardFilter = FilterContainer()
    var selectedContactBoardFilter: FilterContainer = currentContactBoardFilter

    var workerList: LiveData<List<UserDataFull>> = userRepository.getUserWorkers(currentLoggedInUserFull!!.userData.userId)
    var userListFull: LiveData<List<UserDataFull>> = userRepository.getUserUsers(currentLoggedInUserFull!!.userData.userId)

    fun setFilter(name: String, list: MutableList<String>) {
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

    fun applyCreatedTaskListFilters() {
        currentCreatedTaskListFilter = selectedCreatedTaskListFilter
    }

    fun clearSelectedCreatedTaskListFilters() {
        selectedCreatedTaskListFilter = currentCreatedTaskListFilter
    }

    fun applyAcceptedTaskListFilters() {
        currentAcceptedTaskListFilter = selectedAcceptedTaskListFilter
    }

    fun clearSelectedAcceptedTaskListFilters() {
        selectedAcceptedTaskListFilter = currentAcceptedTaskListFilter
    }

    fun applyCommissionedTaskListFilters() {
        currentCommissionedTaskListFilter = selectedCommissionedTaskListFilter
    }

    fun clearSelectedCommissionedTaskListFilters() {
        selectedCommissionedTaskListFilter = currentCommissionedTaskListFilter
    }

    fun applyTaskBoardFilters() {
        currentTaskBoardFilter = selectedTaskBoardFilter
    }

    fun clearSelectedTaskBoardFilters() {
        selectedTaskBoardFilter = currentTaskBoardFilter
    }

    fun applyContactListFilters() {
        currentContactListFilter = selectedContactListFilter
    }

    fun clearSelectedContactListFilters() {
        selectedContactListFilter = currentContactListFilter
    }

    fun applyContactAddFilters() {
        currentContactAddFilter = selectedContactAddFilter
    }

    fun clearSelectedContactAddFilters() {
        selectedContactAddFilter = currentContactAddFilter
    }

    fun applyContactBoardFilters() {
        currentContactBoardFilter = selectedContactBoardFilter
    }

    fun clearSelectedContactBoardFilters() {
        selectedContactBoardFilter = currentContactBoardFilter
    }

    fun populateDb() {
        viewModelScope.launch(Dispatchers.IO) {
            database.populateDb(this, currentLoggedInUserFull!!.userData.userId)
        }
    }

}
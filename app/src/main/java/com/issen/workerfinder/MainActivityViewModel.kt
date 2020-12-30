package com.issen.workerfinder

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.issen.workerfinder.WorkerFinderApplication.Companion.currentLoggedInUserFull
import com.issen.workerfinder.database.WorkerFinderDatabase
import com.issen.workerfinder.database.models.UserData
import com.issen.workerfinder.database.models.UserDataFull
import com.issen.workerfinder.database.repositories.UserRepository
import com.issen.workerfinder.ui.filters.FilterContainer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivityViewModel(private val userRepository: UserRepository, private val database: WorkerFinderDatabase) : ViewModel() {

    //todo get data from shared prefs on start and read to learn about possible better ways to do this
    var currentTaskListFilter = FilterContainer()
    var selectedTaskListFilter: FilterContainer = currentTaskListFilter

    var currentWorkerBoardFilter = FilterContainer()
    var selectedWorkerBoardFilter: FilterContainer = currentWorkerBoardFilter

    var currentTaskBoardFilter = FilterContainer()
    var selectedTaskBoardFilter: FilterContainer = currentTaskBoardFilter

    var workerList: LiveData<List<UserDataFull>> = userRepository.getUserWorkers(currentLoggedInUserFull!!.userData.userId)
    var userListFull: LiveData<List<UserDataFull>> = userRepository.getUserUsers(currentLoggedInUserFull!!.userData.userId)

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

    fun populateDb() {
        viewModelScope.launch(Dispatchers.IO) {
            database.userDataDao.delete("aaaaaaaaaaaaaaaaaa")
            database.userDataDao.insert(UserData(
                "aaaaaaaaaaaaaaaaaa",
                "Testowy Zbyszek",
                "",
                "email123",
                "000999888",
                "Test account",
                true,
                true
            ))

            database.populateComments(
                this, currentLoggedInUserFull
                !!.userData.userId
            )
            database.populateContacts(
                this, currentLoggedInUserFull
                !!.userData.userId
            )
            database.populateNotificationsOpen(
                this, currentLoggedInUserFull
                !!.userData.userId
            )
        }

    }

}
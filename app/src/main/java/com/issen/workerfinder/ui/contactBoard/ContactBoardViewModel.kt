package com.issen.workerfinder.ui.contactBoard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.sqlite.db.SimpleSQLiteQuery
import com.issen.workerfinder.database.models.UserDataFull
import com.issen.workerfinder.database.repositories.UserRepository
import com.issen.workerfinder.ui.filters.FilterContainer

class ContactBoardViewModel(private val userRepository: UserRepository) : ViewModel() {

    var source: LiveData<List<UserDataFull>>
    val mediatorLiveData: MediatorLiveData<List<UserDataFull>> = MediatorLiveData()

    init {
        source = userRepository.getBoardWorkers()
        mediatorLiveData.addSource(source) {
            mediatorLiveData.setValue(
                it
            )
        }
    }

    fun requery(selectedFilterContainer: FilterContainer) {
        mediatorLiveData.removeSource(source)
        source = userRepository.getUsersQueried(setQuerySource(selectedFilterContainer))
        mediatorLiveData.addSource(source) {
            mediatorLiveData.setValue(
                it
            )
        }
    }

    private fun setQuerySource(selectedFilterContainer: FilterContainer): SimpleSQLiteQuery {
        var queryString = "SELECT * FROM user_table WHERE isAccountPublic = 1 AND isOpenForWork = 1";
        var queryArgs = arrayListOf<Any>()
//        if (selectedFilterContainer.filterByCategory.isNotEmpty()) {
//            queryString += " category IN ("
//            selectedFilterContainer.filterByCategory.forEachIndexed { index, item ->
//                queryString += if (index == 0) "? " else ", ?"
//                queryArgs.add(item)
//            }
//            queryString += ")"
//        }
//        if (selectedFilterContainer.filterByRating.isNotEmpty()) {
//            queryString += " rating IN ("
//            selectedFilterContainer.filterByRating.forEachIndexed { index, item ->
//                queryString += if (index == 0) "? " else ", ?"
//                queryArgs.add(item)
//            }
//            queryString += ")"
//        }
        if (selectedFilterContainer.filterByLocalization.isNotEmpty()) {
            queryString += " and localization IN ("
            selectedFilterContainer.filterByLocalization.forEachIndexed { index, item ->
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
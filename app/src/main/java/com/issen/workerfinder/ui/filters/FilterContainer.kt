package com.issen.workerfinder.ui.filters

import java.io.Serializable

data class FilterContainer(

    var orderAscending: Boolean = false,
    var sortBy: String = "none",
    var groupBy: String = "none",
    var filterByWorker: MutableList<String> = mutableListOf(),
    var filterByUser: MutableList<String> = mutableListOf(),
    var filterByCategory: MutableList<String> = mutableListOf(),
    var filterByCyclic: MutableList<String> = mutableListOf(),
    var filterByPriority: MutableList<String> = mutableListOf(),
    var filterByCompletionType: MutableList<String> = mutableListOf(),
    var filterByRating: MutableList<String> = mutableListOf(),
    var filterByLocalization: MutableList<String> = mutableListOf(),
    var filterByPay: MutableList<String> = mutableListOf(),
    var filterDueDate: MutableList<String> = mutableListOf()


) : Serializable {

    fun clearData() {
        orderAscending = false
        sortBy = "none"
        groupBy = "none"
        filterByWorker = mutableListOf()
        filterByUser = mutableListOf()
        filterByCategory = mutableListOf()
        filterByCyclic = mutableListOf()
        filterByPriority = mutableListOf()
        filterByCompletionType = mutableListOf()
        filterByRating = mutableListOf()
        filterByLocalization = mutableListOf()
        filterByPay = mutableListOf()
        filterDueDate = mutableListOf()
    }

    fun resetData(filterContainer: FilterContainer){
        orderAscending = filterContainer.orderAscending
        sortBy = filterContainer.sortBy
        groupBy = filterContainer.groupBy
        filterByWorker = filterContainer.filterByWorker
        filterByUser = filterContainer.filterByUser
        filterByCategory = filterContainer.filterByCategory
        filterByCyclic = filterContainer.filterByCyclic
        filterByPriority = filterContainer.filterByPriority
        filterByCompletionType = filterContainer.filterByCompletionType
        filterByRating = filterContainer.filterByRating
        filterByLocalization = filterContainer.filterByLocalization
        filterByPay = filterContainer.filterByPay
        filterDueDate = filterContainer.filterDueDate
    }
}


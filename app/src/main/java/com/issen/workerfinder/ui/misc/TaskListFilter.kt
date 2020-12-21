package com.issen.workerfinder.ui.misc

import java.io.Serializable

data class TaskListFilter(

    var orderAscending: Boolean = false,
    var sortBy: String = "none",
    var groupBy: String = "none",
    var filterByWorker: MutableList<String> = mutableListOf(),
    var filterByUser: MutableList<String> = mutableListOf(),
    var filterByCategory: MutableList<String> = mutableListOf(),
    var filterByCyclic: MutableList<String> = mutableListOf(),
    var filterByPriority: MutableList<String> = mutableListOf(),
    var filterByCompletionType: MutableList<String> = mutableListOf()

) : Serializable


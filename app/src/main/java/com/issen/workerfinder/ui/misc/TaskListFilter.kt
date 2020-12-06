package com.issen.workerfinder.ui.misc

import java.io.Serializable

data class TaskListFilter(

    var orderBy: String = "asc",
    var sortBy: String = "none",
    var groupBy: String = "none",
    var filterBy: MutableList<String> = mutableListOf()


) : Serializable


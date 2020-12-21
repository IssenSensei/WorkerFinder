package com.issen.workerfinder.ui.misc

import java.io.Serializable

data class ContactsFilter(

    var orderAscending: Boolean = false,
    var sortBy: String = "none",
    var groupBy: String = "none",
    var filterByCategory: MutableList<String> = mutableListOf(),
    var filterByLocalization: MutableList<String> = mutableListOf()

) : Serializable


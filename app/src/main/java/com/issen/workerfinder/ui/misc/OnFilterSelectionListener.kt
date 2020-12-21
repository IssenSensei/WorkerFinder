package com.issen.workerfinder.ui.misc

import android.view.View
import com.issen.workerfinder.database.models.UserDataFull
import com.issen.workerfinder.enums.CompletionTypes
import com.issen.workerfinder.enums.CyclicTypes
import com.issen.workerfinder.enums.PriorityTypes

interface OnFilterSelectionListener {
    fun onUserFilterSelected(userDataFull: UserDataFull, isWorker: Boolean, view: View)
    fun onFilterPriorityChanged(priorityTypes: PriorityTypes, view: View)
    fun onFilterCompletionChanged(completionTypes: CompletionTypes, view: View)
    fun onFilterCyclicChanged(cyclicTypes: CyclicTypes, view: View)
//  fun onFilterCategoryChanged()
//  fun onFilterPayChanged()
//  fun onFilterLocalizationChanged(cyclicTypes: CyclicTypes, view: View)
//  fun onFilterRatingChanged(cyclicTypes: CyclicTypes, view: View)
//  fun onFilterDueDateChanged(cyclicTypes: CyclicTypes, view: View)

}
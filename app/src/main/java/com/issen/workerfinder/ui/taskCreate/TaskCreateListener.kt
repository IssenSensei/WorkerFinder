package com.issen.workerfinder.ui.taskCreate

import android.view.View

interface TaskCreateListener{
    fun onHeaderClicked(view: View)
    fun onAddWorkerClicked()
    fun onCategoryFastSelectClicked()
    fun onSelectPictureClicked()
    fun onTakePictureClicked()
    fun onCyclicDayClicked()
    fun onCyclicYearDayClicked()
    fun onCyclicMonthDayClicked()
    fun onCyclicWeekdayClicked()
    fun onCyclicNoneClicked()
    fun onSetNextDateClicked()
    fun setRemote(view: View)
}
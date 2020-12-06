package com.issen.workerfinder.ui.misc

import android.view.View

interface OnCustomizeDrawerListener{

    fun onMainHeaderClicked(view: View)
    fun onFilterContainerClicked(view: View)
    fun onAcceptClicked()
    fun onCancelClicked()
    fun onClearClicked()
    fun onBackClicked()
    fun onOrderSwitched(view: View)
}
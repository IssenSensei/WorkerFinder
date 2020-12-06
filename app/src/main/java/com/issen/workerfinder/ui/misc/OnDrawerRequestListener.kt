package com.issen.workerfinder.ui.misc

import androidx.drawerlayout.widget.DrawerLayout

interface OnDrawerRequestListener{
    fun onDrawerRequest(interactionImpl: (v: DrawerLayout) -> Unit)
    fun onDrawerClose(interactionImpl: (v: DrawerLayout) -> Unit)
}
package com.issen.workerfinder.utils

import android.view.View
import androidx.core.view.ViewCompat

fun isLayoutRtl(view: View): Boolean {
    return ViewCompat.getLayoutDirection(view) == ViewCompat.LAYOUT_DIRECTION_RTL
}


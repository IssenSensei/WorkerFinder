package com.issen.workerfinder.utils

import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.widget.NestedScrollView

fun isLayoutRtl(view: View): Boolean {
    return ViewCompat.getLayoutDirection(view) == ViewCompat.LAYOUT_DIRECTION_RTL
}

fun View.hideAnimated() {
    alpha = 1f
    visibility = View.VISIBLE
    animate().alpha(0.0f).setDuration(150).withEndAction { visibility = View.GONE }
}

fun View.showAnimated() {
    alpha = 0f
    visibility = View.VISIBLE
    animate().alpha(1.0f).setDuration(150)
}

fun nestedScrollTo(nested: NestedScrollView, targetView: View) {
    nested.post(Runnable { nested.scrollTo(500, targetView.bottom) })
}
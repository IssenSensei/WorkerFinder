package com.issen.workerfinder.ui.taskCreate

import android.content.Context
import android.graphics.PorterDuff
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.issen.workerfinder.R
import com.issen.workerfinder.TaskApplication.Companion.getPriorityIndicatorColor
//import com.issen.workerfinder.TaskApplication.Companion.getPriorityIndicatorColor
import com.issen.workerfinder.enums.PriorityTypes
import kotlinx.android.synthetic.main.item_priority.view.*

class PrioritySpinnerAdapter(
    context: Context
) : ArrayAdapter<PriorityTypes>(context, 0, PriorityTypes.values()) {
    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View =
            convertView ?: layoutInflater.inflate(R.layout.item_priority, parent, false)
        getItem(position)?.let { priorityValue ->
            val color = getPriorityIndicatorColor(priorityValue.toString())
            setItemForCountry(view, priorityValue, color)
        }
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View

        view = layoutInflater.inflate(R.layout.item_priority_dropdown, parent, false)
        getItem(position)?.let { priorityValue ->
            val color = getPriorityIndicatorColor(priorityValue.toString())
            setItemForCountry(view, priorityValue, color)
        }
        convertView?.setOnClickListener {
            val root = parent.rootView
            root.dispatchKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK))
            root.dispatchKeyEvent(KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK))
        }
        return view
    }

    private fun setItemForCountry(view: View, priorityType: PriorityTypes, color: Int) {
        view.priority_name.text = priorityType.name
        view.priority_indicator.setColorFilter(color, PorterDuff.Mode.SRC_IN)
    }

}

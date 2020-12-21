package com.issen.workerfinder.ui.filters

import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.issen.workerfinder.TaskApplication
import com.issen.workerfinder.databinding.ItemFilterPriorityBinding
import com.issen.workerfinder.enums.PriorityTypes
import com.issen.workerfinder.ui.misc.OnFilterSelectionListener
import kotlinx.android.synthetic.main.item_priority.view.*

class PriorityFilterAdapter(
    private val onFilterSelectionListener: OnFilterSelectionListener, private val values: Array<PriorityTypes>,
    private val activeFilters: MutableList<String>
) : RecyclerView.Adapter<PriorityFilterAdapter.PriorityFilterViewHolder>() {
    var views: ArrayList<PriorityFilterViewHolder> = arrayListOf()

    override fun getItemCount() = values.size

    override fun onBindViewHolder(holder: PriorityFilterViewHolder, position: Int) {
        holder.bind(values[position], onFilterSelectionListener, activeFilters)
        views.add(holder)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PriorityFilterViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemFilterPriorityBinding.inflate(layoutInflater, parent, false)
        return PriorityFilterViewHolder(binding)
    }

    fun clearValues(){
        views.forEach {
            it.binding.filterPriorityCheckbox.isChecked = false
        }
    }

    class PriorityFilterViewHolder(val binding: ItemFilterPriorityBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            priorityItem: PriorityTypes, onFilterSelectionListener: OnFilterSelectionListener,
            activeFilters: MutableList<String>
        ) {
            binding.clickListener = onFilterSelectionListener
            binding.priorityItem = priorityItem
            val color = TaskApplication.getPriorityIndicatorColor(priorityItem.toString())
            binding.filterPriorityIndicator.setColorFilter(color, PorterDuff.Mode.SRC_IN)
            binding.filterPriorityCheckbox.isChecked = activeFilters.contains(priorityItem.toString())
            binding.executePendingBindings()
        }
    }
}

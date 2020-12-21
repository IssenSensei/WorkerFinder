package com.issen.workerfinder.ui.filters

import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.issen.workerfinder.TaskApplication
import com.issen.workerfinder.databinding.ItemFilterCompletionBinding
import com.issen.workerfinder.databinding.ItemFilterCyclicBinding
import com.issen.workerfinder.databinding.ItemFilterPriorityBinding
import com.issen.workerfinder.enums.CompletionTypes
import com.issen.workerfinder.enums.CyclicTypes
import com.issen.workerfinder.enums.PriorityTypes
import com.issen.workerfinder.ui.misc.OnFilterSelectionListener
import kotlinx.android.synthetic.main.item_priority.view.*

class CyclicFilterAdapter(
    private val onFilterSelectionListener: OnFilterSelectionListener, private val values: Array<CyclicTypes>,
    private val activeFilters: MutableList<String>
) : RecyclerView.Adapter<CyclicFilterAdapter.CyclicFilterViewHolder>() {
    var views: ArrayList<CyclicFilterViewHolder> = arrayListOf()

    override fun getItemCount() = values.size

    override fun onBindViewHolder(holder: CyclicFilterViewHolder, position: Int) {
        holder.bind(values[position], onFilterSelectionListener, activeFilters)
        views.add(holder)
    }

    fun clearValues(){
        views.forEach {
            it.binding.filterCyclicCheckbox.isChecked = false
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CyclicFilterViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemFilterCyclicBinding.inflate(layoutInflater, parent, false)
        return CyclicFilterViewHolder(binding)
    }

    class CyclicFilterViewHolder(val binding: ItemFilterCyclicBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            cyclicTypes: CyclicTypes, onFilterSelectionListener: OnFilterSelectionListener,
            activeFilters: MutableList<String>
        ) {
            binding.clickListener = onFilterSelectionListener
            binding.cyclicType = cyclicTypes
            binding.filterCyclicCheckbox.isChecked = activeFilters.contains(cyclicTypes.toString())
            binding.executePendingBindings()
        }
    }
}

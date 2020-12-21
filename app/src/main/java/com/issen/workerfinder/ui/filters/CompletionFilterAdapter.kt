package com.issen.workerfinder.ui.filters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.issen.workerfinder.databinding.ItemFilterCompletionBinding
import com.issen.workerfinder.enums.CompletionTypes
import com.issen.workerfinder.ui.misc.OnFilterSelectionListener

class CompletionFilterAdapter(
    private val onFilterSelectionListener: OnFilterSelectionListener, private val values: Array<CompletionTypes>,
    private val activeFilters: MutableList<String>
) : RecyclerView.Adapter<CompletionFilterAdapter.CompletionFilterViewHolder>() {
    var views: ArrayList<CompletionFilterViewHolder> = arrayListOf()

    override fun getItemCount() = values.size

    override fun onBindViewHolder(holder: CompletionFilterViewHolder, position: Int) {
        holder.bind(values[position], onFilterSelectionListener, activeFilters)
        views.add(holder)
    }

    fun clearValues(){
        views.forEach {
            it.binding.filterCompletionCheckbox.isChecked = false
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompletionFilterViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemFilterCompletionBinding.inflate(layoutInflater, parent, false)
        return CompletionFilterViewHolder(binding)
    }

    class CompletionFilterViewHolder(val binding: ItemFilterCompletionBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            completionTypes: CompletionTypes, onFilterSelectionListener: OnFilterSelectionListener,
            activeFilters: MutableList<String>
        ) {
            binding.clickListener = onFilterSelectionListener
            binding.completionType = completionTypes
            binding.filterCompletionCheckbox.isChecked = activeFilters.contains(completionTypes.toString())
            binding.executePendingBindings()
        }
    }
}

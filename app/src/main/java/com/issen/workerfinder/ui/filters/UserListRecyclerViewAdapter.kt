package com.issen.workerfinder.ui.filters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.issen.workerfinder.database.models.UserDataFull
import com.issen.workerfinder.databinding.ItemFilterWorkerBinding
import com.issen.workerfinder.ui.misc.OnFilterSelectionListener


class UserListRecyclerViewAdapter(
    private val onFilterSelectionListener: OnFilterSelectionListener,
    private val isWorker: Boolean,
    private val activeFilters: MutableList<String>
) :
    ListAdapter<UserDataFull, UserListRecyclerViewAdapter.ViewHolder>(WorkerListDiffCallback()) {
    var views: ArrayList<ViewHolder> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemFilterWorkerBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!, onFilterSelectionListener, isWorker, activeFilters)
        views.add(holder)
    }

    fun clearValues(){
        views.forEach {
            it.binding.filterUserCheckbox.isChecked = false
        }
    }

    class ViewHolder(val binding: ItemFilterWorkerBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: UserDataFull,
            onFilterSelectionListener: OnFilterSelectionListener,
            isWorker: Boolean,
            activeFilters: MutableList<String>
        ) {
            binding.user = item
            binding.clickListener = onFilterSelectionListener
            binding.isWorker = isWorker
            binding.filterUserCheckbox.isChecked = activeFilters.contains(item.userData.userId)
            binding.executePendingBindings()
        }
    }
}

class WorkerListDiffCallback : DiffUtil.ItemCallback<UserDataFull>() {
    override fun areItemsTheSame(oldItem: UserDataFull, newItem: UserDataFull): Boolean {
        return oldItem.userData.userId == newItem.userData.userId
    }

    override fun areContentsTheSame(oldItem: UserDataFull, newItem: UserDataFull): Boolean {
        return oldItem == newItem
    }
}

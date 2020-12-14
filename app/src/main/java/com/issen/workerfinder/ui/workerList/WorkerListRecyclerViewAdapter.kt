package com.issen.workerfinder.ui.workerList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.issen.workerfinder.database.models.UserDataFull
import com.issen.workerfinder.databinding.ItemWorkerBinding
import com.issen.workerfinder.ui.misc.WorkerListener


class WorkerListRecyclerViewAdapter(private val workerListener: WorkerListener) :
    ListAdapter<UserDataFull, WorkerListRecyclerViewAdapter.ViewHolder>(WorkerListDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemWorkerBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!, workerListener)
    }

    class ViewHolder(val binding: ItemWorkerBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: UserDataFull, workerListener: WorkerListener) {
            binding.worker = item
            binding.clickListener = workerListener
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

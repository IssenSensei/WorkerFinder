package com.issen.workerfinder.ui.workerBoard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.issen.workerfinder.database.models.FullUserData
import com.issen.workerfinder.databinding.ItemWorkerBinding
import com.issen.workerfinder.ui.misc.WorkerListener


class WorkerBoardRecyclerViewAdapter(private val workerListener: WorkerListener) :
    ListAdapter<FullUserData, WorkerBoardRecyclerViewAdapter.ViewHolder>(WorkerBoardDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemWorkerBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!, workerListener)
    }

    class ViewHolder(val binding: ItemWorkerBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: FullUserData, workerListener: WorkerListener) {
            binding.worker = item
            binding.clickListener = workerListener
            binding.executePendingBindings()
        }
    }
}

class WorkerBoardDiffCallback : DiffUtil.ItemCallback<FullUserData>() {
    override fun areItemsTheSame(oldItem: FullUserData, newItem: FullUserData): Boolean {
        return oldItem.userData.firebaseKey == newItem.userData.firebaseKey
    }

    override fun areContentsTheSame(oldItem: FullUserData, newItem: FullUserData): Boolean {
        return oldItem == newItem
    }
}

package com.issen.workerfinder.ui.workerList

import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.issen.workerfinder.TaskApplication.Companion.getIndicatorColor
import com.issen.workerfinder.database.models.FullTaskModel
import com.issen.workerfinder.database.models.FullUserData
import com.issen.workerfinder.databinding.ItemTaskBinding
import com.issen.workerfinder.databinding.ItemWorkerBinding
import com.issen.workerfinder.enums.CompletionTypes
import com.issen.workerfinder.enums.CyclicTypes


class WorkerListRecyclerViewAdapter(private val workerListListener: WorkerListListener) :
    ListAdapter<FullUserData, WorkerListRecyclerViewAdapter.ViewHolder>(WorkerListDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemWorkerBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!, workerListListener)
    }

    class ViewHolder(val binding: ItemWorkerBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: FullUserData, workerListListener: WorkerListListener) {
            binding.worker = item
            binding.executePendingBindings()
        }
    }
}

class WorkerListDiffCallback : DiffUtil.ItemCallback<FullUserData>() {
    override fun areItemsTheSame(oldItem: FullUserData, newItem: FullUserData): Boolean {
        return oldItem.userData.firebaseKey == newItem.userData.firebaseKey
    }

    override fun areContentsTheSame(oldItem: FullUserData, newItem: FullUserData): Boolean {
        return oldItem == newItem
    }
}

interface WorkerListListener {
    fun onWorkerClicked(fullUserData: FullUserData)
}

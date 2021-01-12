package com.issen.workerfinder.ui.taskWorkerPicker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.issen.workerfinder.database.models.UserDataFull
import com.issen.workerfinder.databinding.ItemWorkerPickerBinding
import com.issen.workerfinder.ui.boardTaskWorkerPicker.TaskWorkerPickerDiffCallback

class TaskWorkerPickerRecyclerViewAdapter(private val taskWorkerPickerListener: TaskWorkerPickerListener) :
    ListAdapter<UserDataFull, TaskWorkerPickerRecyclerViewAdapter.ViewHolder>(TaskWorkerPickerDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemWorkerPickerBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!, taskWorkerPickerListener)
    }

    class ViewHolder(val binding: ItemWorkerPickerBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: UserDataFull, taskWorkerPickerListener: TaskWorkerPickerListener) {
            binding.worker = item
            binding.clickListener = taskWorkerPickerListener
            binding.executePendingBindings()
        }
    }
}

class TaskWorkerPickerDiffCallback : DiffUtil.ItemCallback<UserDataFull>() {
    override fun areItemsTheSame(oldItem: UserDataFull, newItem: UserDataFull): Boolean {
        return oldItem.userData.userId == newItem.userData.userId
    }

    override fun areContentsTheSame(oldItem: UserDataFull, newItem: UserDataFull): Boolean {
        return oldItem == newItem
    }
}

interface TaskWorkerPickerListener{
    fun onWorkerPicked(userDataFull: UserDataFull)
}
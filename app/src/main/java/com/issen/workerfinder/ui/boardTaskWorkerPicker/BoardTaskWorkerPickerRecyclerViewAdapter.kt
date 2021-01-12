package com.issen.workerfinder.ui.boardTaskWorkerPicker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.issen.workerfinder.database.models.UserDataFull
import com.issen.workerfinder.databinding.ItemBoardWorkerPickerBinding
import com.issen.workerfinder.ui.taskWorkerPicker.TaskWorkerPickerDiffCallback

class BoardTaskWorkerPickerRecyclerViewAdapter(private val boardTaskWorkerPickerListener: BoardTaskWorkerPickerListener) :
    ListAdapter<UserDataFull, BoardTaskWorkerPickerRecyclerViewAdapter.ViewHolder>(TaskWorkerPickerDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemBoardWorkerPickerBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!, boardTaskWorkerPickerListener)
    }

    class ViewHolder(val binding: ItemBoardWorkerPickerBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: UserDataFull, boardTaskWorkerPickerListener: BoardTaskWorkerPickerListener) {
            binding.worker = item
            binding.clickListener = boardTaskWorkerPickerListener
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

interface BoardTaskWorkerPickerListener {
    fun onBoardWorkerPicked(userDataFull: UserDataFull)
}
package com.issen.workerfinder.ui.taskBoard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.issen.workerfinder.database.models.FullTaskModel
import com.issen.workerfinder.database.models.FullUserData
import com.issen.workerfinder.databinding.ItemTaskBinding
import com.issen.workerfinder.databinding.ItemTaskBoardBinding
import com.issen.workerfinder.databinding.ItemWorkerBinding


class TaskBoardRecyclerViewAdapter(private val taskBoardListener: TaskBoardListener) :
    ListAdapter<FullTaskModel, TaskBoardRecyclerViewAdapter.ViewHolder>(TaskBoardDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemTaskBoardBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!, taskBoardListener)
    }

    class ViewHolder(val binding: ItemTaskBoardBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: FullTaskModel, taskBoardListener: TaskBoardListener) {
            binding.fullTask = item
            binding.clickListener = taskBoardListener
            binding.executePendingBindings()
        }
    }
}

class TaskBoardDiffCallback : DiffUtil.ItemCallback<FullTaskModel>() {
    override fun areItemsTheSame(oldItem: FullTaskModel, newItem: FullTaskModel): Boolean {
        return oldItem.task.taskId == newItem.task.taskId
    }

    override fun areContentsTheSame(oldItem: FullTaskModel, newItem: FullTaskModel): Boolean {
        return oldItem == newItem
    }
}

interface TaskBoardListener {
    fun onTaskClicked(fullTaskModel: FullTaskModel)
}

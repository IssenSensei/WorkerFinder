package com.issen.workerfinder.ui.taskBoard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.issen.workerfinder.database.models.TaskModelFull
import com.issen.workerfinder.databinding.ItemTaskBoardBinding


class TaskBoardRecyclerViewAdapter(private val taskBoardListener: TaskBoardListener) :
    ListAdapter<TaskModelFull, TaskBoardRecyclerViewAdapter.ViewHolder>(TaskBoardDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemTaskBoardBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!, taskBoardListener)
    }

    class ViewHolder(val binding: ItemTaskBoardBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: TaskModelFull, taskBoardListener: TaskBoardListener) {
            binding.fullTask = item
            binding.clickListener = taskBoardListener
            binding.executePendingBindings()
        }
    }
}

class TaskBoardDiffCallback : DiffUtil.ItemCallback<TaskModelFull>() {
    override fun areItemsTheSame(oldItem: TaskModelFull, newItem: TaskModelFull): Boolean {
        return oldItem.task.taskId == newItem.task.taskId
    }

    override fun areContentsTheSame(oldItem: TaskModelFull, newItem: TaskModelFull): Boolean {
        return oldItem == newItem
    }
}

interface TaskBoardListener {
    fun onTaskClicked(taskModelFull: TaskModelFull)
    fun onTaskApplied(taskModelFull: TaskModelFull)
}

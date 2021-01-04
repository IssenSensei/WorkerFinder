package com.issen.workerfinder.ui.misc

import androidx.recyclerview.widget.DiffUtil
import com.issen.workerfinder.database.models.TaskModelFull

class TaskListDiffCallback : DiffUtil.ItemCallback<TaskModelFull>() {
    override fun areItemsTheSame(oldItem: TaskModelFull, newItem: TaskModelFull): Boolean {
        return oldItem.task.taskId == newItem.task.taskId
    }

    override fun areContentsTheSame(oldItem: TaskModelFull, newItem: TaskModelFull): Boolean {
        return oldItem == newItem
    }
}
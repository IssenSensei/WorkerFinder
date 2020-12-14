package com.issen.workerfinder.ui.taskList

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
//import com.issen.workerfinder.TaskApplication.Companion.getPriorityIndicatorColor
import com.issen.workerfinder.database.models.TaskModelFull
import com.issen.workerfinder.databinding.ItemTaskBinding
import com.issen.workerfinder.enums.CyclicTypes


class TaskListRecyclerViewAdapter(private val taskListListener: TaskListListener, private val context: Context) :
    ListAdapter<TaskModelFull, TaskListRecyclerViewAdapter.ViewHolder>(TaskListDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemTaskBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!, taskListListener, context)
    }

    class ViewHolder(val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: TaskModelFull, taskListListener: TaskListListener, context: Context) {
            binding.fullTask = item
            binding.photosIndicator.visibility = if (item.photos.isNotEmpty()) View.VISIBLE else View.GONE
            binding.cyclicIndicator.visibility = if (item.task.cyclic != CyclicTypes.NONE.toString()) View.VISIBLE else View.GONE
//            binding.taskPriorityIndicator.setColorFilter(getPriorityIndicatorColor(item.task.priority), PorterDuff.Mode.SRC_IN)
            binding.clickListener = taskListListener

            binding.executePendingBindings()
        }
    }

}

class TaskListDiffCallback : DiffUtil.ItemCallback<TaskModelFull>() {
    override fun areItemsTheSame(oldItem: TaskModelFull, newItem: TaskModelFull): Boolean {
        return oldItem.task.taskId == newItem.task.taskId
    }

    override fun areContentsTheSame(oldItem: TaskModelFull, newItem: TaskModelFull): Boolean {
        return oldItem == newItem
    }
}

interface TaskListListener {
    fun onTaskComplete(taskFull: TaskModelFull)
    fun onTaskSelected(taskFull: TaskModelFull)

}

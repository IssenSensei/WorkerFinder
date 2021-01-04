package com.issen.workerfinder.ui.taskList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.issen.workerfinder.database.models.TaskModelFull
import com.issen.workerfinder.databinding.ItemTaskBinding
import com.issen.workerfinder.enums.CyclicTypes
import com.issen.workerfinder.ui.misc.TaskListDiffCallback
import com.issen.workerfinder.ui.misc.TaskListListener

class TaskListRecyclerViewAdapter(private val taskListListener: TaskListListener, private val userId: String) :
    ListAdapter<TaskModelFull, TaskListRecyclerViewAdapter.ViewHolder>(TaskListDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemTaskBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!, taskListListener, userId)
    }

    class ViewHolder(val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: TaskModelFull, taskListListener: TaskListListener, userId: String) {
            binding.fullTask = item
            binding.photosIndicator.visibility = if (item.photos.isNotEmpty()) View.VISIBLE else View.GONE
            binding.cyclicIndicator.visibility = if (item.task.cyclic != CyclicTypes.NONE.toString()) View.VISIBLE else View.GONE
            binding.clickListener = taskListListener
            binding.executePendingBindings()
        }
    }
}





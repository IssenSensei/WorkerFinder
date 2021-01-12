package com.issen.workerfinder.ui.invitations

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.issen.workerfinder.database.models.TaskModelFull
import com.issen.workerfinder.databinding.ItemApplicationBoardBinding

class ApplicationBoardRecyclerViewAdapter(private val applicationBoardListener: ApplicationBoardListener) :
    ListAdapter<TaskModelFull, ApplicationBoardRecyclerViewAdapter.ViewHolder>(ApplicationBoardDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemApplicationBoardBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!, applicationBoardListener)
    }

    class ViewHolder(val binding: ItemApplicationBoardBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: TaskModelFull, applicationBoardListener: ApplicationBoardListener) {
            binding.task = item
            binding.clickListener = applicationBoardListener
            binding.executePendingBindings()
        }
    }
}

class ApplicationBoardDiffCallback : DiffUtil.ItemCallback<TaskModelFull>() {
    override fun areItemsTheSame(oldItem: TaskModelFull, newItem: TaskModelFull): Boolean {
        return oldItem.task.taskId == newItem.task.taskId
    }

    override fun areContentsTheSame(oldItem: TaskModelFull, newItem: TaskModelFull): Boolean {
        return oldItem == newItem
    }
}

interface ApplicationBoardListener {
    fun onCancelClicked(taskModelFull: TaskModelFull)
}

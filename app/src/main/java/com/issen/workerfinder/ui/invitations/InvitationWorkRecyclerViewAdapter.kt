package com.issen.workerfinder.ui.invitations

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.issen.workerfinder.database.models.TaskModel
import com.issen.workerfinder.database.models.TaskModelFull
import com.issen.workerfinder.databinding.ItemInvitationWorkBinding

class InvitationWorkRecyclerViewAdapter(private val invitationWorkListener: InvitationWorkListener) :
    ListAdapter<TaskModelFull, InvitationWorkRecyclerViewAdapter.ViewHolder>(InvitationWorkDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemInvitationWorkBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!, invitationWorkListener)
    }

    class ViewHolder(val binding: ItemInvitationWorkBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: TaskModelFull, invitationWorkListener: InvitationWorkListener) {
            binding.task = item
            binding.clickListener = invitationWorkListener
            binding.executePendingBindings()
        }
    }
}

class InvitationWorkDiffCallback : DiffUtil.ItemCallback<TaskModelFull>() {
    override fun areItemsTheSame(oldItem: TaskModelFull, newItem: TaskModelFull): Boolean {
        return oldItem.task.taskId == newItem.task.taskId
    }

    override fun areContentsTheSame(oldItem: TaskModelFull, newItem: TaskModelFull): Boolean {
        return oldItem == newItem
    }
}

interface InvitationWorkListener {
    fun onCancelClicked(taskModel: TaskModel)
    fun onTaskClicked(taskModelFull: TaskModelFull)
}

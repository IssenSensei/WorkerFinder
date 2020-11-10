package com.issen.workerfinder.ui.taskList

import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.issen.workerfinder.TaskApplication.Companion.getIndicatorColor
import com.issen.workerfinder.database.FullTaskModel
import com.issen.workerfinder.databinding.ItemTaskBinding
import com.issen.workerfinder.enums.CyclicTypes


class TaskListRecyclerViewAdapter(private val taskListListener: TaskListListener) :
    ListAdapter<FullTaskModel, TaskListRecyclerViewAdapter.ViewHolder>(TaskListDiffCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemTaskBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!, taskListListener)
    }

    class ViewHolder(val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: FullTaskModel, taskListListener: TaskListListener) {
            binding.fullTask = item
            binding.photosIndicator.visibility = if (item.photos.isNotEmpty()) View.VISIBLE else View.GONE
            binding.cyclicIndicator.visibility = if (item.task.cyclic != CyclicTypes.NONE.toString()) View.VISIBLE else View.GONE
            binding.taskPriorityIndicator.setColorFilter(getIndicatorColor(item.task.priority), PorterDuff.Mode.SRC_IN)
            binding.clickListener = taskListListener
            binding.executePendingBindings()
        }
    }

}
class TaskListDiffCallback : DiffUtil.ItemCallback<FullTaskModel>() {
    override fun areItemsTheSame(oldItem: FullTaskModel, newItem: FullTaskModel): Boolean {
        return oldItem.task.taskId == newItem.task.taskId
    }

    override fun areContentsTheSame(oldItem: FullTaskModel, newItem: FullTaskModel): Boolean {
        return oldItem == newItem
    }
}

interface TaskListListener {
    fun onTaskComplete(fullTask: FullTaskModel)
    fun onTaskAbandon(fullTask: FullTaskModel)
    fun onTaskSelected(fullTask: FullTaskModel)

//    fun onItemHold(task: TaskModel) = clickListener(task.taskTitle) //aktywny do edycji/usuwania
//    fun onItemRootViewClicked(task: TaskModel) = clickListener(task.firebaseKey)
//    fun onPriorityClick(task: TaskModel) = clickListener(task.firebaseKey) //zmiana priorytetu
//    fun onImageClicked(task: TaskModel) = clickListener(task.firebaseKey) //powiększanie zdjęcia

}

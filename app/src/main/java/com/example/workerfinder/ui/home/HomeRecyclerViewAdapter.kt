package com.example.workerfinder.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.workerfinder.database.TaskModel
import com.example.workerfinder.databinding.ItemTaskBinding


class HomeRecyclerViewAdapter(val clickListener: TaskListListener) :
    ListAdapter<TaskModel, HomeRecyclerViewAdapter.ViewHolder>(HomeDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemTaskBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!, clickListener)

//        val item = getItem(position)
//        holder.binding.taskTitle.text = item.taskTitle

    }

    class ViewHolder(val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: TaskModel, clickListener: TaskListListener) {
            binding.task = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }
//
//        val taskDescription: TextView = binding.taskDescription
//        val taskExpand: ImageView = binding.taskExpand
//        val hasPictures: ImageView = binding.hasPictures
//        val isCyclic: ImageView = binding.isCyclic
//        val detailView: LinearLayout = binding.taskListDetails
//        val photosContainer: LinearLayout = binding.taskListPhotos
//
//        val taskPriorityIndicator: ImageView = binding.taskPriority
//        val taskCompleteCheckbox: CheckBox = binding.taskCompleteCheckbox
    }



}
class HomeDiffCallback : DiffUtil.ItemCallback<TaskModel>() {
    override fun areItemsTheSame(oldItem: TaskModel, newItem: TaskModel): Boolean {
        return oldItem.firebaseKey == newItem.firebaseKey
    }

    override fun areContentsTheSame(oldItem: TaskModel, newItem: TaskModel): Boolean {
        return oldItem == newItem
    }
}

interface TaskListListener {
    fun onTaskComplete(task: TaskModel)

//    fun onItemHold(task: TaskModel) = clickListener(task.taskTitle) //aktywny do edycji/usuwania
//    fun onItemRootViewClicked(task: TaskModel) = clickListener(task.firebaseKey)
//    fun onPriorityClick(task: TaskModel) = clickListener(task.firebaseKey) //zmiana priorytetu
//    fun onImageClicked(task: TaskModel) = clickListener(task.firebaseKey) //powiększanie zdjęcia

}

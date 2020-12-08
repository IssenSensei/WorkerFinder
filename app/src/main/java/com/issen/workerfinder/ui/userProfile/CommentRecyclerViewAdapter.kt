package com.issen.workerfinder.ui.userProfile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.issen.workerfinder.database.models.UserDataWithComments
import com.issen.workerfinder.databinding.ItemDialogCommentBinding


class CommentRecyclerViewAdapter :
    ListAdapter<UserDataWithComments, CommentRecyclerViewAdapter.ViewHolder>(TaskListDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemDialogCommentBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!)
    }

    class ViewHolder(val binding: ItemDialogCommentBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: UserDataWithComments) {
            binding.comment = item
            binding.executePendingBindings()
        }
    }

}

class TaskListDiffCallback : DiffUtil.ItemCallback<UserDataWithComments>() {
    override fun areItemsTheSame(oldItem: UserDataWithComments, newItem: UserDataWithComments): Boolean {
        return oldItem.comment.id == newItem.comment.id
    }

    override fun areContentsTheSame(oldItem: UserDataWithComments, newItem: UserDataWithComments): Boolean {
        return oldItem == newItem
    }
}

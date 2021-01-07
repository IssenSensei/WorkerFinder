package com.issen.workerfinder.ui.conversation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.issen.workerfinder.database.models.MessagesFull
import com.issen.workerfinder.databinding.ItemConversationMessageBinding

class ConversationRecyclerViewAdapter :
    ListAdapter<MessagesFull, ConversationRecyclerViewAdapter.ViewHolder>(ConversationDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemConversationMessageBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!)
    }

    class ViewHolder(val binding: ItemConversationMessageBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MessagesFull) {
            binding.message = item
            binding.executePendingBindings()
        }
    }
}

class ConversationDiffCallback : DiffUtil.ItemCallback<MessagesFull>() {
    override fun areItemsTheSame(oldItem: MessagesFull, newItem: MessagesFull): Boolean {
        return oldItem.message.messageId == newItem.message.messageId
    }

    override fun areContentsTheSame(oldItem: MessagesFull, newItem: MessagesFull): Boolean {
        return oldItem == newItem
    }
}

package com.issen.workerfinder.ui.conversationList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.issen.workerfinder.database.models.ConversationsFull
import com.issen.workerfinder.databinding.ItemConversationListBinding

class ConversationListRecyclerViewAdapter(private val conversationListListener: ConversationListListener) :
    ListAdapter<ConversationsFull, ConversationListRecyclerViewAdapter.ViewHolder>(ConversationListDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemConversationListBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!, conversationListListener)
    }

    class ViewHolder(val binding: ItemConversationListBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ConversationsFull, conversationListListener: ConversationListListener) {
            binding.conversation = item
            binding.clickListener = conversationListListener
            binding.executePendingBindings()
        }
    }
}

class ConversationListDiffCallback : DiffUtil.ItemCallback<ConversationsFull>() {
    override fun areItemsTheSame(oldItem: ConversationsFull, newItem: ConversationsFull): Boolean {
        return oldItem.conversation.conversationId == newItem.conversation.conversationId
    }

    override fun areContentsTheSame(oldItem: ConversationsFull, newItem: ConversationsFull): Boolean {
        return oldItem == newItem
    }
}

interface ConversationListListener{
    fun onConversationClicked(conversationsFull: ConversationsFull)
}
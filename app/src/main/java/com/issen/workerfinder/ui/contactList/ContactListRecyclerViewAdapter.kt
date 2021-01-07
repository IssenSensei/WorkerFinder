package com.issen.workerfinder.ui.contactList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.issen.workerfinder.database.models.UserDataFull
import com.issen.workerfinder.databinding.ItemContactListBinding
import com.issen.workerfinder.ui.misc.ContactListener


class ContactListRecyclerViewAdapter(private val contactListener: ContactListener, private val shouldDisplayChatButton: Boolean, private val contactChatListener: ContactChatListener) :
    ListAdapter<UserDataFull, ContactListRecyclerViewAdapter.ViewHolder>(ContactListDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemContactListBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!, contactListener, shouldDisplayChatButton, contactChatListener)
    }

    class ViewHolder(val binding: ItemContactListBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: UserDataFull, contactListener: ContactListener, shouldDisplayChatButton: Boolean, contactChatListener: ContactChatListener) {
            binding.worker = item
            binding.clickListener = contactListener
            binding.shouldDisplayChatButton = shouldDisplayChatButton
            binding.contactChatListener = contactChatListener
            binding.executePendingBindings()
        }
    }
}

class ContactListDiffCallback : DiffUtil.ItemCallback<UserDataFull>() {
    override fun areItemsTheSame(oldItem: UserDataFull, newItem: UserDataFull): Boolean {
        return oldItem.userData.userId == newItem.userData.userId
    }

    override fun areContentsTheSame(oldItem: UserDataFull, newItem: UserDataFull): Boolean {
        return oldItem == newItem
    }
}

interface ContactChatListener {
    fun onContactChatClicked(userDataFull: UserDataFull)
}
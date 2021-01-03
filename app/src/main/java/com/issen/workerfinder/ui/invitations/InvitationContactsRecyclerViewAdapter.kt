package com.issen.workerfinder.ui.invitations

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.issen.workerfinder.database.models.UserDataFull
import com.issen.workerfinder.databinding.ItemInvitationContactBinding

class InvitationContactsRecyclerViewAdapter(private val invitationContactsListener: InvitationContactsListener) :
    ListAdapter<UserDataFull, InvitationContactsRecyclerViewAdapter.ViewHolder>(InvitationContactsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemInvitationContactBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!, invitationContactsListener)
    }

    class ViewHolder(val binding: ItemInvitationContactBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: UserDataFull, invitationContactsListener: InvitationContactsListener) {
            binding.worker = item
            binding.clickListener = invitationContactsListener
            binding.executePendingBindings()
        }
    }
}

class InvitationContactsDiffCallback : DiffUtil.ItemCallback<UserDataFull>() {
    override fun areItemsTheSame(oldItem: UserDataFull, newItem: UserDataFull): Boolean {
        return oldItem.userData.userId == newItem.userData.userId
    }

    override fun areContentsTheSame(oldItem: UserDataFull, newItem: UserDataFull): Boolean {
        return oldItem == newItem
    }
}

interface InvitationContactsListener {
    fun onCancelClicked(userDataFull: UserDataFull)
    fun onContactClicked(userDataFull: UserDataFull)
}

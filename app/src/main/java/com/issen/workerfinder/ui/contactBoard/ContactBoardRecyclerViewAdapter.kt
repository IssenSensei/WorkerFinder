package com.issen.workerfinder.ui.contactBoard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.issen.workerfinder.database.models.UserDataFull
import com.issen.workerfinder.databinding.ItemContactBoardBinding
import com.issen.workerfinder.ui.misc.ContactListener


class ContactBoardRecyclerViewAdapter(private val contactListener: ContactListener) :
    ListAdapter<UserDataFull, ContactBoardRecyclerViewAdapter.ViewHolder>(ContactBoardDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemContactBoardBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!, contactListener)
    }

    class ViewHolder(val binding: ItemContactBoardBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: UserDataFull, contactListener: ContactListener) {
            binding.worker = item
            binding.clickListener = contactListener
            binding.executePendingBindings()
        }
    }
}

class ContactBoardDiffCallback : DiffUtil.ItemCallback<UserDataFull>() {
    override fun areItemsTheSame(oldItem: UserDataFull, newItem: UserDataFull): Boolean {
        return oldItem.userData.userId == newItem.userData.userId
    }

    override fun areContentsTheSame(oldItem: UserDataFull, newItem: UserDataFull): Boolean {
        return oldItem == newItem
    }
}

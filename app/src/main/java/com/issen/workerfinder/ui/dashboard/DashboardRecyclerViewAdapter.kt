package com.issen.workerfinder.ui.dashboard

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.issen.workerfinder.database.models.DashboardNotificationFull
import com.issen.workerfinder.databinding.*
import com.issen.workerfinder.utils.getDashboardNotificationGroup

class DashboardRecyclerViewAdapter(private val dashboardListener: DashboardListener, private val context: Context) :
    ListAdapter<DashboardNotificationFull, RecyclerView.ViewHolder>(DashboardDiffCallback()) {

    private val TYPE_CONTACT = 1
    private val TYPE_RATING = 2
    private val TYPE_TASK = 3
    private val TYPE_WORK = 4

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_CONTACT -> {
                ContactViewHolder(ItemNotificationContactBinding.inflate(layoutInflater, parent, false))
            }
            TYPE_RATING -> {
                RatingViewHolder(ItemNotificationRatingBinding.inflate(layoutInflater, parent, false))
            }
            TYPE_TASK -> {
                TaskViewHolder(ItemNotificationTaskBinding.inflate(layoutInflater, parent, false))
            }
            TYPE_WORK -> {
                WorkViewHolder(ItemNotificationWorkBinding.inflate(layoutInflater, parent, false))
            }
            else -> {
                //todo probably to fix
                ContactViewHolder(ItemNotificationContactBinding.inflate(layoutInflater, parent, false))
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is ContactViewHolder -> {
                holder.bind(getItem(position)!!)
            }
            is RatingViewHolder -> {
                holder.bind(getItem(position)!!)
            }
            is TaskViewHolder -> {
                holder.bind(getItem(position)!!)
            }
            is WorkViewHolder -> {
                holder.bind(getItem(position)!!)
            }
        }
    }

    override fun getItemViewType(position: Int) =
        when (getDashboardNotificationGroup(getItem(position).notification.dashboardNotificationType)) {
            "CONTACT" -> {
                TYPE_CONTACT
            }
            "RATING" -> {
                TYPE_RATING
            }
            "TASK" -> {
                TYPE_TASK
            }
            "WORK" -> {
                TYPE_WORK
            }
            else -> {
                0
            }
        }


    class ContactViewHolder(val binding: ItemNotificationContactBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DashboardNotificationFull) {
            binding.notificationData = item
            binding.executePendingBindings()
        }
    }

    class RatingViewHolder(val binding: ItemNotificationRatingBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DashboardNotificationFull) {
            binding.notificationData = item
            binding.executePendingBindings()
        }
    }

    class TaskViewHolder(val binding: ItemNotificationTaskBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DashboardNotificationFull) {
            binding.notificationData = item
            binding.executePendingBindings()
        }
    }

    class WorkViewHolder(val binding: ItemNotificationWorkBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DashboardNotificationFull) {
            binding.notificationData = item
            binding.executePendingBindings()
        }
    }

}

class DashboardDiffCallback : DiffUtil.ItemCallback<DashboardNotificationFull>() {
    override fun areItemsTheSame(oldItem: DashboardNotificationFull, newItem: DashboardNotificationFull): Boolean {
        return oldItem.notification.id == newItem.notification.id
    }

    override fun areContentsTheSame(oldItem: DashboardNotificationFull, newItem: DashboardNotificationFull): Boolean {
        return oldItem == newItem
    }
}

interface DashboardListener {

}

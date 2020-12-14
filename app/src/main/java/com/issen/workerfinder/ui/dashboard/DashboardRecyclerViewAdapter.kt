package com.issen.workerfinder.ui.dashboard

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.issen.workerfinder.database.models.DashboardNotificationFull

class DashboardRecyclerViewAdapter(private val dashboardListener: DashboardListener, private val context: Context) :
    ListAdapter<DashboardNotificationFull, RecyclerView.ViewHolder>(DashboardDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

}

class DashboardDiffCallback : DiffUtil.ItemCallback<DashboardNotificationFull>() {
    override fun areItemsTheSame(oldItem: DashboardNotificationFull, newItem: DashboardNotificationFull): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: DashboardNotificationFull, newItem: DashboardNotificationFull): Boolean {
        return oldItem == newItem
    }
}

interface DashboardListener {

}

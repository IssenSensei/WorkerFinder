package com.issen.workerfinder.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.issen.workerfinder.R
import com.issen.workerfinder.WorkerFinderApplication
import com.issen.workerfinder.database.models.DashboardNotificationFull
import kotlinx.android.synthetic.main.fragment_dashboard.view.*

class DashboardFragment : Fragment(), DashboardListener {

    private val dashboardViewModel: DashboardViewModel by viewModels {
        WordViewModelFactory(
            (requireActivity().application as WorkerFinderApplication).dashboardNotificationRepository,
            (requireActivity().application as WorkerFinderApplication).contactRepository,
            (requireActivity().application as WorkerFinderApplication).taskRepository
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        val adapter = DashboardRecyclerViewAdapter(this, requireContext())

        dashboardViewModel.dashboardNotificationsList.observe(viewLifecycleOwner, Observer {notifications ->
            notifications?.let {
                adapter.submitList(it)
            }
        })

        view.dashboard_recycler_list.adapter = adapter

        return view
    }

    override fun onContactAccept(dashboardNotificationFull: DashboardNotificationFull) {
        dashboardViewModel.acceptContact(dashboardNotificationFull)
    }

    override fun onContactRefuse(dashboardNotificationFull: DashboardNotificationFull) {
        dashboardViewModel.refuseContact(dashboardNotificationFull)
    }

    override fun onCheckProfile() {
        TODO("Not yet implemented")
    }

    override fun onCheckRating() {
        TODO("Not yet implemented")
    }

    override fun onCreateTask() {
        TODO("Not yet implemented")
    }

    override fun onChatWithUser() {
        TODO("Not yet implemented")
    }

    override fun onTaskAccept(dashboardNotificationFull: DashboardNotificationFull) {
        dashboardViewModel.acceptTask(dashboardNotificationFull)
    }

    override fun onTaskReject(dashboardNotificationFull: DashboardNotificationFull) {
        dashboardViewModel.rejectTask(dashboardNotificationFull)
    }

    override fun onWorkAccept(dashboardNotificationFull: DashboardNotificationFull) {
        dashboardViewModel.acceptWork(dashboardNotificationFull)
    }

    override fun onWorkRefuse(dashboardNotificationFull: DashboardNotificationFull) {
        dashboardViewModel.refuseWork(dashboardNotificationFull)
    }

    override fun onCheckTaskDetails() {
        TODO("Not yet implemented")
    }

    override fun onTaskEdit() {
        TODO("Not yet implemented")
    }

}
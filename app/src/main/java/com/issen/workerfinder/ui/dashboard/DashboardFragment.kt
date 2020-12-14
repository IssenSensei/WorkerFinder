package com.issen.workerfinder.ui.dashboard

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.issen.workerfinder.R
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.fragment_dashboard.view.*

class DashboardFragment : Fragment(), DashboardListener {

    private lateinit var dashboardViewModel: DashboardViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        dashboardViewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        val adapter = DashboardRecyclerViewAdapter(this, requireContext())

        dashboardViewModel.dashboardNotificationsList.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        view.dashboard_recycler_list.adapter = adapter

        return view
    }

}
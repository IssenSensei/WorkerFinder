package com.issen.workerfinder.ui.taskList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.issen.workerfinder.R
import kotlinx.android.synthetic.main.fragment_task_list.view.*

class TaskListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_task_list, container, false)

        val pagerAdapter = TaskListPagerAdapter(this)
        view.task_list_pager.adapter = pagerAdapter

        TabLayoutMediator(view.task_list_tab_layout, view.task_list_pager) { tab, position ->
            tab.text = when (position) {
                0 -> "Moje"
                1 -> "Przyjęte"
                2 -> "Zlecone"
                else -> "Moje"
            }
        }.attach()
        return view
    }

}
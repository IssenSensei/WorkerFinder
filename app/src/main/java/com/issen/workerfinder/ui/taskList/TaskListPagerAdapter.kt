package com.issen.workerfinder.ui.taskList

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class TaskListPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> CreatedTaskListFragment()
            1 -> AcceptedTaskListFragment()
            2 -> CommissionedTaskListFragment()
            else -> CreatedTaskListFragment()
        }
    }

}

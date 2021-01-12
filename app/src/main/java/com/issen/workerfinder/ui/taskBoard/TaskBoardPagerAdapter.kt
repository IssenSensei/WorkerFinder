package com.issen.workerfinder.ui.taskBoard

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class TaskBoardPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> TaskBoardOthersFragment()
            1 -> TaskBoardMineFragment()
            else -> TaskBoardOthersFragment()
        }
    }

}

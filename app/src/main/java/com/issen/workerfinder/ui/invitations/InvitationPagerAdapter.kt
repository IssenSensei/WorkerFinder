package com.issen.workerfinder.ui.invitations

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class InvitationPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> InvitationContactsFragment()
            1 -> InvitationWorkFragment()
            else -> InvitationContactsFragment()
        }
    }

}

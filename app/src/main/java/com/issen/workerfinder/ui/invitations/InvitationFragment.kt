package com.issen.workerfinder.ui.invitations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.issen.workerfinder.R
import kotlinx.android.synthetic.main.fragment_invitation.view.*

class InvitationFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_invitation, container, false)

        val pagerAdapter = InvitationPagerAdapter(this)
        view.invitation_pager.adapter = pagerAdapter

        TabLayoutMediator(view.invitation_tab_layout, view.invitation_pager) { tab, position ->
            when (position) {
                0 -> {
                    tab.setIcon(R.drawable.ic_invitation_contact)
                    tab.text = getString(R.string.contacts_header)
                }
                1 -> {
                    tab.setIcon(R.drawable.ic_invitation_task)
                    tab.text = getString(R.string.tasks_header)
                }
                2 -> {
                    tab.setIcon(R.drawable.ic_task_board)
                    tab.text = getString(R.string.applications_header)
                }
                else -> {
                    tab.setIcon(R.drawable.ic_invitation_contact)
                    tab.text = getString(R.string.contacts_header)
                }
            }
        }.attach()
        return view
    }

}
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
            tab.text = when (position) {
                0 -> "Kontakty"
                1 -> "Zadania"
                else -> "Kontakty"
            }
        }.attach()
        return view
    }

}
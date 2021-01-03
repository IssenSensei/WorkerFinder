package com.issen.workerfinder.ui.invitations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.issen.workerfinder.R
import com.issen.workerfinder.WorkerFinderApplication
import com.issen.workerfinder.database.models.UserDataFull
import kotlinx.android.synthetic.main.fragment_invitation_contacts.view.*

class InvitationContactsFragment : Fragment(), InvitationContactsListener {

    private val invitationContactsViewModel: InvitationContactsViewModel by viewModels {
        InvitationContactsViewModelFactory(
            (requireActivity().application as WorkerFinderApplication).userRepository,
            (requireActivity().application as WorkerFinderApplication).dashboardNotificationRepository
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_invitation_contacts, container, false)

        val adapter = InvitationContactsRecyclerViewAdapter(this)

        invitationContactsViewModel.invitationList.observe(viewLifecycleOwner) {
            it.let {
                adapter.submitList(it)
            }
        }

        view.invitation_contacts_recycler_list.adapter = adapter
        return view
    }

    override fun onCancelClicked(userDataFull: UserDataFull) {
        invitationContactsViewModel.cancelInvitation(userDataFull.userData.userId)
    }

    override fun onContactClicked(userDataFull: UserDataFull) {
        val actionProfile = InvitationFragmentDirections.actionNavInvitationsToNavUserProfile(userDataFull)
        findNavController().navigate(actionProfile)
    }

}
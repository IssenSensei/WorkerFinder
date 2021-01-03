package com.issen.workerfinder.ui.invitations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.issen.workerfinder.R

class InvitationTasksFragment : Fragment() {

    companion object {
        fun newInstance() = InvitationTasksFragment()
    }

    private lateinit var viewModel: InvitationTasksViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_invitation_tasks, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(InvitationTasksViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
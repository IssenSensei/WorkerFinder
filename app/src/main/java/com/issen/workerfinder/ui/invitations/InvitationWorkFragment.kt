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
import com.issen.workerfinder.database.models.TaskModel
import com.issen.workerfinder.database.models.TaskModelFull
import kotlinx.android.synthetic.main.fragment_invitation_work.view.*

class InvitationWorkFragment : Fragment(), InvitationWorkListener {

    private val invitationWorkViewModel: InvitationWorkViewModel by viewModels {
        InvitationWorkViewModelFactory(
            (requireActivity().application as WorkerFinderApplication).taskRepository,
            (requireActivity().application as WorkerFinderApplication).dashboardNotificationRepository
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_invitation_work, container, false)

        val adapter = InvitationWorkRecyclerViewAdapter(this)
        invitationWorkViewModel.invitationList.observe(viewLifecycleOwner) {
            it.let {
                adapter.submitList(it)
            }
        }

        view.invitation_work_recycler_list.adapter = adapter

        return view
    }

    override fun onCancelClicked(taskModel: TaskModel) {
        invitationWorkViewModel.cancelInvitation(taskModel.taskId)
    }

    override fun onTaskClicked(taskModelFull: TaskModelFull) {
        val actionDetail = InvitationFragmentDirections.actionNavInvitationsToNavTaskDetail(taskModelFull)
        findNavController().navigate(actionDetail)
    }
}
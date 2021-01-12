package com.issen.workerfinder.ui.invitations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.issen.workerfinder.R
import com.issen.workerfinder.WorkerFinderApplication
import com.issen.workerfinder.database.models.TaskModelFull
import kotlinx.android.synthetic.main.fragment_application_board.view.*

class ApplicationBoardFragment : Fragment(), ApplicationBoardListener {

    private val applicationBoardViewModel: ApplicationBoardViewModel by viewModels {
        ApplicationBoardViewModelFactory(
            (requireActivity().application as WorkerFinderApplication).taskRepository,
            (requireActivity().application as WorkerFinderApplication).dashboardNotificationRepository
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view =  inflater.inflate(R.layout.fragment_application_board, container, false)
        val adapter = ApplicationBoardRecyclerViewAdapter(this)
        applicationBoardViewModel.applicationsList.observe(viewLifecycleOwner) {
            it.let {
                adapter.submitList(it)
            }
        }

        view.application_board_recycler_list.adapter = adapter

        return view
    }

    override fun onCancelClicked(taskModelFull: TaskModelFull) {
        applicationBoardViewModel.cancelApplication(taskModelFull)
    }
}
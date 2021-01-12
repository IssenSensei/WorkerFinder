package com.issen.workerfinder.ui.boardTaskWorkerPicker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.issen.workerfinder.R
import com.issen.workerfinder.WorkerFinderApplication
import com.issen.workerfinder.database.models.UserDataFull
import kotlinx.android.synthetic.main.fragment_board_task_worker_picker.view.*

class BoardTaskWorkerPickerFragment : Fragment(), BoardTaskWorkerPickerListener {

    private val boardTaskWorkerPickerFragmentArgs: BoardTaskWorkerPickerFragmentArgs by navArgs()
    private val boardTaskWorkerPickerViewModel: BoardTaskWorkerPickerViewModel by viewModels {
        BoardTaskWorkerPickerViewModelFactory(
            (requireActivity().application as WorkerFinderApplication).userRepository,
            (requireActivity().application as WorkerFinderApplication).dashboardNotificationRepository,
            (requireActivity().application as WorkerFinderApplication).taskRepository
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_board_task_worker_picker, container, false)
        boardTaskWorkerPickerViewModel.setId(boardTaskWorkerPickerFragmentArgs.taskId)
        val adapter = BoardTaskWorkerPickerRecyclerViewAdapter(this)
        boardTaskWorkerPickerViewModel.mediatorLiveData.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        root.board_task_worker_picker_recycler_list.adapter = adapter
        root.board_task_worker_picker_search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                if (p0 != null) {
                    boardTaskWorkerPickerViewModel.reQuery(p0)
                } else {
                    boardTaskWorkerPickerViewModel.reQuery("")
                }
                return true
            }
        })

        return root
    }

    override fun onBoardWorkerPicked(userDataFull: UserDataFull) {
        boardTaskWorkerPickerViewModel.selectWorker(userDataFull)
        findNavController().popBackStack(R.id.nav_task_board, false)
    }
}
package com.issen.workerfinder.ui.taskWorkerPicker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.issen.workerfinder.R
import com.issen.workerfinder.WorkerFinderApplication
import com.issen.workerfinder.database.models.UserDataFull
import kotlinx.android.synthetic.main.fragment_task_worker_picker.view.*

class TaskWorkerPickerFragment : Fragment(), TaskWorkerPickerListener {

    private val taskWorkerPickerViewModel: TaskWorkerPickerViewModel by viewModels {
        TaskWorkerPickerViewModelFactory(
            (requireActivity().application as WorkerFinderApplication).userRepository
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root =  inflater.inflate(R.layout.fragment_task_worker_picker, container, false)

        val adapter = TaskWorkerPickerRecyclerViewAdapter(this)
        taskWorkerPickerViewModel.mediatorLiveData.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        root.task_worker_picker_recycler_list.adapter = adapter
        root.task_worker_picker_search.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                if (p0 != null) {
                    taskWorkerPickerViewModel.reQuery(p0)
                } else {
                    taskWorkerPickerViewModel.reQuery("")
                }
                return true
            }
        })

        return root
    }

    override fun onWorkerPicked(userDataFull: UserDataFull) {
        val actionWorkerPicked = TaskWorkerPickerFragmentDirections.actionNavTaskWorkerPickerToNavNewTask(userDataFull)
        findNavController().navigate(actionWorkerPicked)
    }
}
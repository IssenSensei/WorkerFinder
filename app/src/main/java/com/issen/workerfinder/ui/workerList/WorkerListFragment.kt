package com.issen.workerfinder.ui.workerList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.issen.workerfinder.R
import com.issen.workerfinder.database.models.FullTaskModel
import com.issen.workerfinder.database.models.FullUserData
import com.issen.workerfinder.ui.taskList.TaskListRecyclerViewAdapter
import kotlinx.android.synthetic.main.fragment_task_list.view.*
import kotlinx.android.synthetic.main.fragment_worker_list.view.*

class WorkerListFragment : Fragment(), WorkerListListener {

    private lateinit var workerListViewModel: WorkerListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        workerListViewModel = ViewModelProvider(this).get(WorkerListViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_worker_list, container, false)

        val adapter = WorkerListRecyclerViewAdapter(this)

        workerListViewModel.workerList.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })
        root.worker_recycler_list.adapter = adapter

        return root
    }

    override fun onWorkerClicked(fullUserData: FullUserData) {
        TODO("Not yet implemented")
    }

}
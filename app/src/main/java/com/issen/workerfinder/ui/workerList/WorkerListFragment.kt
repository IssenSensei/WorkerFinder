package com.issen.workerfinder.ui.workerList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.issen.workerfinder.R

class WorkerListFragment : Fragment() {

    companion object {
        fun newInstance() = WorkerListFragment()
    }

    private lateinit var viewModel: WorkerListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_worker_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(WorkerListViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
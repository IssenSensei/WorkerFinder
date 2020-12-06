package com.issen.workerfinder.ui.workerBoard

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.issen.workerfinder.R

class WorkerBoardFragment : Fragment() {

    companion object {
        fun newInstance() = WorkerBoardFragment()
    }

    private lateinit var viewModel: WorkerBoardViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_worker_board, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(WorkerBoardViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
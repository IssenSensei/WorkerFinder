package com.issen.workerfinder.ui.workerBoard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.issen.workerfinder.R
import com.issen.workerfinder.database.models.FullUserData
import kotlinx.android.synthetic.main.fragment_worker_board.view.*

class WorkerBoardFragment : Fragment(), WorkerBoardListener {

    private lateinit var workerBoardViewModel: WorkerBoardViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        workerBoardViewModel = ViewModelProvider(this).get(WorkerBoardViewModel::class.java)
        val root =  inflater.inflate(R.layout.fragment_worker_board, container, false)

        val adapter = WorkerBoardRecyclerViewAdapter(this)

        workerBoardViewModel.workerList.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })
        root.worker_board_recycler_list.adapter = adapter

        return root
    }

    override fun onWorkerClicked(fullUserData: FullUserData) {
        TODO("Not yet implemented")
    }

}
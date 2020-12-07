package com.issen.workerfinder.ui.taskBoard

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.issen.workerfinder.R
import com.issen.workerfinder.database.models.FullTaskModel
import com.issen.workerfinder.ui.workerBoard.WorkerBoardRecyclerViewAdapter
import kotlinx.android.synthetic.main.fragment_task_board.view.*
import kotlinx.android.synthetic.main.fragment_worker_board.view.*

class TaskBoardFragment : Fragment(), TaskBoardListener {

    private lateinit var taskBoardViewModel: TaskBoardViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        taskBoardViewModel = ViewModelProvider(this).get(TaskBoardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_task_board, container, false)

        val adapter = TaskBoardRecyclerViewAdapter(this)

        taskBoardViewModel.taskList.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })
        root.task_board_recycler_list.adapter = adapter
        return root
    }

    override fun onTaskClicked(fullTaskModel: FullTaskModel) {
        TODO("Not yet implemented")
    }

}
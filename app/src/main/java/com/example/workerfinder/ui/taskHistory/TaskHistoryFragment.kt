package com.example.workerfinder.ui.taskHistory

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.workerfinder.R

class TaskHistoryFragment : Fragment() {

    companion object {
        fun newInstance() = TaskHistoryFragment()
    }

    private lateinit var viewModel: TaskHistoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.task_history_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(TaskHistoryViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
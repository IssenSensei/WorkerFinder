package com.issen.workerfinder.ui.taskList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.issen.workerfinder.R
import com.issen.workerfinder.database.FullTaskModel
import kotlinx.android.synthetic.main.fragment_task_list.view.*


class TaskListFragment : Fragment(), TaskListListener {

    private lateinit var taskListViewModel: TaskListViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        taskListViewModel =
            ViewModelProvider(this).get(TaskListViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_task_list, container, false)
        val adapter = TaskListRecyclerViewAdapter(this)

        taskListViewModel.allTasks.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })
        root.task_recycler_list.adapter = adapter

        root.task_recycler_list_fab.setOnClickListener(
            Navigation.createNavigateOnClickListener(
                R.id.action_nav_home_to_nav_new_task,
                null
            )
        )

        return root
    }

    override fun onTaskComplete(fullTask: FullTaskModel) {
        taskListViewModel.completeTask(fullTask.task)
        Toast.makeText(context, fullTask.task.completed, Toast.LENGTH_SHORT).show()
    }

    override fun onTaskAbandon(fullTask: FullTaskModel) {
        taskListViewModel.abandonTask(fullTask.task)
        Toast.makeText(context, fullTask.task.completed, Toast.LENGTH_SHORT).show()
    }

    override fun onTaskSelected(fullTask: FullTaskModel) {
        val actionDetail = TaskListFragmentDirections.actionNavTaskListToNavTaskDetail(fullTask)
        findNavController().navigate(actionDetail)
    }

}
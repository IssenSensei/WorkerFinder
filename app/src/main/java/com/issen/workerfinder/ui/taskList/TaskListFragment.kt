package com.issen.workerfinder.ui.taskList

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.issen.workerfinder.R
import com.issen.workerfinder.TaskApplication.Companion.currentLoggedInUserFull
import com.issen.workerfinder.database.models.TaskModelFull
import com.issen.workerfinder.ui.misc.OnCustomizeDrawerListener
import com.issen.workerfinder.ui.misc.OnDrawerRequestListener
import com.issen.workerfinder.ui.misc.TaskListFilter
import kotlinx.android.synthetic.main.fragment_task_list.view.*


class TaskListFragment : Fragment(), TaskListListener {
    private var auth = FirebaseAuth.getInstance()

    private lateinit var taskListViewModel: TaskListViewModel
    private lateinit var onDrawerRequestListener: OnDrawerRequestListener
    private lateinit var onCustomizeDrawerListener: OnCustomizeDrawerListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        taskListViewModel =
            ViewModelProvider(this).get(TaskListViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_task_list, container, false)
        val adapter = TaskListRecyclerViewAdapter(this, requireContext())

        taskListViewModel.mediatorLiveData.observe(viewLifecycleOwner, Observer {
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_filter, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_filter -> {
                toggleFilterDrawer()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnDrawerRequestListener)
            onDrawerRequestListener = context
        if (context is OnCustomizeDrawerListener)
            onCustomizeDrawerListener = context

    }

    private fun toggleFilterDrawer() {
        onDrawerRequestListener.onDrawerRequest {
            if (it.isDrawerOpen(GravityCompat.END)) {
                it.closeDrawer(GravityCompat.END)
            } else {
                it.openDrawer(GravityCompat.END)
            }
        }
    }

    /**
     * Method can work in 2 ways:
     * If user created task for himself, complete task
     * If user completes task for someone else, notify task owner of task state and wait for approval
     */
    override fun onTaskComplete(taskFull: TaskModelFull) {
        if (taskFull.task.workerFirebaseKey == currentLoggedInUserFull!!.userData.userId) {
            taskListViewModel.markTaskAsCompleted(taskFull.task)
        } else {
            taskListViewModel.markTaskAsPending(taskFull.task)
        }
        Toast.makeText(context, "Zadanie oznaczone jako wykonane!", Toast.LENGTH_SHORT).show()
    }

    private fun abandonTask(taskFull: TaskModelFull) {
        taskListViewModel.abandonTask(taskFull.task)
        Toast.makeText(context, taskFull.task.completed, Toast.LENGTH_SHORT).show()
    }

    override fun onTaskSelected(taskFull: TaskModelFull) {
        val actionDetail = TaskListFragmentDirections.actionNavTaskListToNavTaskDetail(taskFull)
        findNavController().navigate(actionDetail)
    }

    fun onAcceptClicked(selectedTaskListFilter: TaskListFilter) {
        taskListViewModel.queryDesc()
    }
}
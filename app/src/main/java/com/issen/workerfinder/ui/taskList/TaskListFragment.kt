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
import com.issen.workerfinder.database.models.FullTaskModel
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
        val adapter = TaskListRecyclerViewAdapter(this)

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

    fun onAcceptClicked(selectedTaskListFilter: TaskListFilter) {
        taskListViewModel.queryDesc()
    }
}
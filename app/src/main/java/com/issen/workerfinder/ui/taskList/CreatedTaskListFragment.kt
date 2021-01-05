package com.issen.workerfinder.ui.taskList

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.issen.workerfinder.R
import com.issen.workerfinder.WorkerFinderApplication
import com.issen.workerfinder.database.models.TaskModelFull
import com.issen.workerfinder.ui.filters.FilterContainer
import com.issen.workerfinder.ui.misc.OnCustomizeDrawerListener
import com.issen.workerfinder.ui.misc.OnDrawerRequestListener
import com.issen.workerfinder.ui.misc.TaskListListener
import kotlinx.android.synthetic.main.fragment_created_task_list.view.*


class CreatedTaskListFragment : Fragment(), TaskListListener {
    private var auth = FirebaseAuth.getInstance()
    private val createdTaskListViewModel: CreatedTaskListViewModel by viewModels {
        CreatedTaskListViewModelFactory(
            (requireActivity().application as WorkerFinderApplication).taskRepository
        )
    }
    private lateinit var onDrawerRequestListener: OnDrawerRequestListener
    private lateinit var onCustomizeDrawerListener: OnCustomizeDrawerListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_created_task_list, container, false)
        val adapter = TaskListRecyclerViewAdapter(this, auth.currentUser!!.uid)

        createdTaskListViewModel.mediatorLiveData.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })
        root.created_task_recycler_list.adapter = adapter

        root.created_task_recycler_list_fab.setOnClickListener(
            Navigation.createNavigateOnClickListener(
                R.id.action_nav_home_to_nav_new_task,
                null
            )
        )

        return root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnDrawerRequestListener)
            onDrawerRequestListener = context
        if (context is OnCustomizeDrawerListener)
            onCustomizeDrawerListener = context
    }

    fun toggleFilterDrawer() {
        onDrawerRequestListener.onDrawerRequest {
            if (it.isDrawerOpen(GravityCompat.END)) {
                it.closeDrawer(GravityCompat.END)
            } else {
                it.findViewById<LinearLayout>(R.id.drawer_filter_filter_category_subheader).visibility = View.VISIBLE
                it.findViewById<LinearLayout>(R.id.drawer_filter_filter_cyclic_subheader).visibility = View.VISIBLE
                it.findViewById<LinearLayout>(R.id.drawer_filter_filter_localization_subheader).visibility = View.VISIBLE
                it.findViewById<LinearLayout>(R.id.drawer_filter_filter_date_subheader).visibility = View.VISIBLE
                it.findViewById<LinearLayout>(R.id.drawer_filter_filter_completion_subheader).visibility = View.VISIBLE
                it.findViewById<LinearLayout>(R.id.drawer_filter_filter_priority_subheader).visibility = View.VISIBLE
                it.findViewById<LinearLayout>(R.id.drawer_filter_filter_search_subheader).visibility = View.VISIBLE
                it.findViewById<RadioButton>(R.id.drawer_filter_sort_completion_date).visibility = View.VISIBLE
                it.findViewById<RadioButton>(R.id.drawer_filter_group_priority).visibility = View.VISIBLE
                it.findViewById<RadioButton>(R.id.drawer_filter_group_completion_date).visibility = View.VISIBLE
                it.openDrawer(GravityCompat.END)
            }
        }
    }

    override fun onTaskComplete(taskFull: TaskModelFull) {
        createdTaskListViewModel.markTaskAsCompleted(taskFull.task)
    }

    override fun onTaskSelected(taskFull: TaskModelFull) {
        val actionDetail = TaskListFragmentDirections.actionNavTaskListToNavTaskDetail(taskFull)
        findNavController().navigate(actionDetail)
    }

    fun onAcceptClicked(selectedFilterContainer: FilterContainer) {
        createdTaskListViewModel.requery(selectedFilterContainer)
    }
}
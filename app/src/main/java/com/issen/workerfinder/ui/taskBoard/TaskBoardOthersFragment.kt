package com.issen.workerfinder.ui.taskBoard

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
import androidx.navigation.fragment.findNavController
import com.issen.workerfinder.R
import com.issen.workerfinder.WorkerFinderApplication
import com.issen.workerfinder.database.models.TaskModelFull
import com.issen.workerfinder.ui.filters.FilterContainer
import com.issen.workerfinder.ui.misc.OnDrawerRequestListener
import kotlinx.android.synthetic.main.fragment_task_board_others.view.*

class TaskBoardOthersFragment : Fragment(), TaskBoardListener {

    private val taskBoardOthersViewModel: TaskBoardOthersViewModel by viewModels {
        TaskBoardOthersViewModelFactory(
            (requireActivity().application as WorkerFinderApplication).taskRepository,
            (requireActivity().application as WorkerFinderApplication).dashboardNotificationRepository
        )
    }
    private lateinit var onDrawerRequestListener: OnDrawerRequestListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_task_board_others, container, false)

        val adapter = TaskBoardRecyclerViewAdapter(this)

        taskBoardOthersViewModel.mediatorLiveData.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })
        root.task_board_others_recycler_list.adapter = adapter

        root.task_board_others_fab.setOnClickListener {
            val actionNewTask = TaskBoardFragmentDirections.actionNavTaskBoardToNavNewTask(null, true)
            findNavController().navigate(actionNewTask)
        }
        return root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnDrawerRequestListener)
            onDrawerRequestListener = context

    }

    fun toggleFilterDrawer() {
        onDrawerRequestListener.onDrawerRequest {
            if (it.isDrawerOpen(GravityCompat.END)) {
                it.closeDrawer(GravityCompat.END)
            } else {
                it.findViewById<LinearLayout>(R.id.drawer_filter_filter_user_subheader).visibility = View.VISIBLE
                it.findViewById<LinearLayout>(R.id.drawer_filter_filter_category_subheader).visibility = View.VISIBLE
                it.findViewById<LinearLayout>(R.id.drawer_filter_filter_cyclic_subheader).visibility = View.VISIBLE
                it.findViewById<LinearLayout>(R.id.drawer_filter_filter_localization_subheader).visibility = View.VISIBLE
                it.findViewById<LinearLayout>(R.id.drawer_filter_filter_pay_subheader).visibility = View.VISIBLE
                it.findViewById<LinearLayout>(R.id.drawer_filter_filter_rating_subheader).visibility = View.VISIBLE
                it.findViewById<LinearLayout>(R.id.drawer_filter_filter_date_subheader).visibility = View.VISIBLE
                it.findViewById<RadioButton>(R.id.drawer_filter_sort_pay).visibility = View.VISIBLE
                it.findViewById<RadioButton>(R.id.drawer_filter_sort_completion_date).visibility = View.VISIBLE
                it.findViewById<RadioButton>(R.id.drawer_filter_sort_rating).visibility = View.VISIBLE
                it.findViewById<RadioButton>(R.id.drawer_filter_group_localization).visibility = View.VISIBLE
                it.openDrawer(GravityCompat.END)
            }
        }
    }

    override fun onTaskClicked(taskModelFull: TaskModelFull) {
        val actionDetail = TaskBoardFragmentDirections.actionNavTaskBoardToNavTaskDetail(taskModelFull)
        findNavController().navigate(actionDetail)
    }

    override fun onTaskApplied(taskModelFull: TaskModelFull) {
        taskBoardOthersViewModel.applyForBoardTask(taskModelFull)
    }

    fun onAcceptClicked(filterContainer: FilterContainer) {
        taskBoardOthersViewModel.requery(filterContainer)
    }

}
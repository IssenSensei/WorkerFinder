package com.issen.workerfinder.ui.taskBoard

import android.content.Context
import android.os.Bundle
import android.view.*
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
import kotlinx.android.synthetic.main.fragment_task_board.view.*

class TaskBoardFragment : Fragment(), TaskBoardListener {

    private val taskBoardViewModel: TaskBoardViewModel by viewModels {
        TaskBoardViewModelFactory(
            (requireActivity().application as WorkerFinderApplication).taskRepository
        )
    }
    private lateinit var onDrawerRequestListener: OnDrawerRequestListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_task_board, container, false)

        val adapter = TaskBoardRecyclerViewAdapter(this)

        taskBoardViewModel.mediatorLiveData.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })
        root.task_board_recycler_list.adapter = adapter
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

    }

    private fun toggleFilterDrawer() {
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

    fun onAcceptClicked(filterContainer: FilterContainer) {
        taskBoardViewModel.requery(filterContainer)
    }

}
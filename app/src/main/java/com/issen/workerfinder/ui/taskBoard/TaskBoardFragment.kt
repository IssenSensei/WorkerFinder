package com.issen.workerfinder.ui.taskBoard

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.issen.workerfinder.R
import com.issen.workerfinder.database.models.TaskModelFull
import com.issen.workerfinder.ui.filters.FilterContainer
import com.issen.workerfinder.ui.misc.OnDrawerRequestListener
import kotlinx.android.synthetic.main.fragment_task_board.view.*

class TaskBoardFragment : Fragment(), TaskBoardListener {

    private lateinit var taskBoardViewModel: TaskBoardViewModel
    private lateinit var onDrawerRequestListener: OnDrawerRequestListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        taskBoardViewModel = ViewModelProvider(this).get(TaskBoardViewModel::class.java)
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
package com.issen.workerfinder.ui.taskList

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.issen.workerfinder.R
import kotlinx.android.synthetic.main.fragment_task_list.*
import kotlinx.android.synthetic.main.fragment_task_list.view.*

class TaskListFragment : Fragment() {

    private lateinit var pagerAdapter: TaskListPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_task_list, container, false)

        pagerAdapter = TaskListPagerAdapter(this)
        view.task_list_pager.adapter = pagerAdapter
        view.task_list_pager.offscreenPageLimit = 2

        TabLayoutMediator(view.task_list_tab_layout, view.task_list_pager) { tab, position ->
            tab.text = when (position) {
                0 -> "Moje"
                1 -> "Przyjęte"
                2 -> "Zlecone"
                else -> "Moje"
            }
        }.attach()
        return view
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
                when(val fragment = getCurrentListFragment()){
                    is CreatedTaskListFragment -> fragment.toggleFilterDrawer()
                    is AcceptedTaskListFragment -> fragment.toggleFilterDrawer()
                    is CommissionedTaskListFragment -> fragment.toggleFilterDrawer()
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun getCurrentListFragment(): Fragment {
        return childFragmentManager.findFragmentByTag("f" + pagerAdapter.getItemId(task_list_pager.currentItem))!!
    }

}
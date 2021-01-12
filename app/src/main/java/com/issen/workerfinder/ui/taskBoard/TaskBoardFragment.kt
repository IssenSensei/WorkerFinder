package com.issen.workerfinder.ui.taskBoard

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.issen.workerfinder.R
import kotlinx.android.synthetic.main.fragment_task_board.*
import kotlinx.android.synthetic.main.fragment_task_board.view.*

class TaskBoardFragment : Fragment() {

    private lateinit var pagerAdapter: TaskBoardPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_task_board, container, false)

        pagerAdapter = TaskBoardPagerAdapter(this)
        view.task_board_pager.adapter = pagerAdapter

        TabLayoutMediator(view.task_board_tab_layout, view.task_board_pager) { tab, position ->
            when (position) {
                0 -> {
                    tab.setIcon(R.drawable.ic_contacts)
                    tab.text = getString(R.string.others_board_header)
                }
                1 -> {
                    tab.setIcon(R.drawable.ic_invitation_contact)
                    tab.text = getString(R.string.mine_board_header)
                }
                else -> {
                    tab.setIcon(R.drawable.ic_contacts)
                    tab.text = getString(R.string.others_board_header)
                }
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
                when (val fragment = getCurrentListFragment()) {
                    is TaskBoardMineFragment -> fragment.toggleFilterDrawer()
                    is TaskBoardOthersFragment -> fragment.toggleFilterDrawer()
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun getCurrentListFragment(): Fragment {
        return childFragmentManager.findFragmentByTag("f" + pagerAdapter.getItemId(task_board_pager.currentItem))!!
    }

}
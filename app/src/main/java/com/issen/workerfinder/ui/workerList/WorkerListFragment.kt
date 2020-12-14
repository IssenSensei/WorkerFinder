package com.issen.workerfinder.ui.workerList

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.issen.workerfinder.R
import com.issen.workerfinder.database.models.UserDataFull
import com.issen.workerfinder.ui.misc.OnDrawerRequestListener
import com.issen.workerfinder.ui.misc.WorkerListener
import kotlinx.android.synthetic.main.fragment_worker_list.view.*

class WorkerListFragment : Fragment(), WorkerListener {

    private lateinit var workerListViewModel: WorkerListViewModel
    private lateinit var onDrawerRequestListener: OnDrawerRequestListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        workerListViewModel = ViewModelProvider(this).get(WorkerListViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_worker_list, container, false)

        val adapter = WorkerListRecyclerViewAdapter(this)

        workerListViewModel.workerList.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })
        root.worker_recycler_list.adapter = adapter

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

    override fun onWorkerClicked(userDataFull: UserDataFull) {
        val actionProfile = WorkerListFragmentDirections.actionNavWorkerListToNavUserProfile(userDataFull)
        findNavController().navigate(actionProfile)
    }

}
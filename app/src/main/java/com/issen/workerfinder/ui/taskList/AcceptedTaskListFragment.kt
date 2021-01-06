package com.issen.workerfinder.ui.taskList

import android.app.AlertDialog
import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat.getSystemService
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
import com.issen.workerfinder.utils.createChannel
import com.issen.workerfinder.utils.sendNotification
import kotlinx.android.synthetic.main.fragment_accepted_task_list.view.*


class AcceptedTaskListFragment : Fragment(), TaskListListener, PopupMenu.OnMenuItemClickListener {
    private var auth = FirebaseAuth.getInstance()
    private val acceptedTaskListViewModel: AcceptedTaskListViewModel by viewModels {
        AcceptedTaskListViewModelFactory(
            (requireActivity().application as WorkerFinderApplication).taskRepository,
            (requireActivity().application as WorkerFinderApplication).dashboardNotificationRepository,
            (requireActivity().application as WorkerFinderApplication).commentRepository
        )
    }
    private lateinit var onDrawerRequestListener: OnDrawerRequestListener
    private lateinit var onCustomizeDrawerListener: OnCustomizeDrawerListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_accepted_task_list, container, false)
        val adapter = TaskListRecyclerViewAdapter(this, auth.currentUser!!.uid)

        acceptedTaskListViewModel.mediatorLiveData.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })
        root.accepted_task_recycler_list.adapter = adapter

        root.accepted_task_recycler_list_fab.setOnClickListener(
            Navigation.createNavigateOnClickListener(
                R.id.action_nav_home_to_nav_new_task,
                null
            )
        )

        createChannel(
            getString(R.string.notification_rating_channel_id),
            getString(R.string.notification_rating_channel_name),
            requireActivity()
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
                it.findViewById<LinearLayout>(R.id.drawer_filter_filter_pay_subheader).visibility = View.VISIBLE
                it.findViewById<LinearLayout>(R.id.drawer_filter_filter_rating_subheader).visibility = View.VISIBLE
                it.findViewById<LinearLayout>(R.id.drawer_filter_filter_date_subheader).visibility = View.VISIBLE
                it.findViewById<LinearLayout>(R.id.drawer_filter_filter_completion_subheader).visibility = View.VISIBLE
                it.findViewById<LinearLayout>(R.id.drawer_filter_filter_priority_subheader).visibility = View.VISIBLE
                it.findViewById<LinearLayout>(R.id.drawer_filter_filter_search_subheader).visibility = View.VISIBLE
                it.findViewById<RadioButton>(R.id.drawer_filter_sort_completion_date).visibility = View.VISIBLE
                it.findViewById<RadioButton>(R.id.drawer_filter_sort_pay).visibility = View.VISIBLE
                it.findViewById<RadioButton>(R.id.drawer_filter_sort_rating).visibility = View.VISIBLE
                it.findViewById<RadioButton>(R.id.drawer_filter_group_priority).visibility = View.VISIBLE
                it.findViewById<RadioButton>(R.id.drawer_filter_group_completion_date).visibility = View.VISIBLE
                it.openDrawer(GravityCompat.END)
            }
        }
    }

    override fun onTaskComplete(taskFull: TaskModelFull) {
        acceptedTaskListViewModel.markTaskAsPending(taskFull.task)
    }

    override fun onTaskSelected(taskFull: TaskModelFull) {
        val actionDetail = TaskListFragmentDirections.actionNavTaskListToNavTaskDetail(taskFull)
        findNavController().navigate(actionDetail)
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_show_rating_dialog -> {
                showRatingDialog()
                true
            }
            else -> true
        }
    }

    override fun onPopupClicked(view: View, taskFull: TaskModelFull) {
        PopupMenu(requireContext(), view).apply {
            setOnMenuItemClickListener(this@AcceptedTaskListFragment)
            inflate(R.menu.popup_menu_task)
            show()
        }
        acceptedTaskListViewModel.popupTask = taskFull
    }

    fun onAcceptClicked(selectedFilterContainer: FilterContainer) {
        acceptedTaskListViewModel.requery(selectedFilterContainer)
    }

    private fun showRatingDialog() {
        //todo allow 1 rating per task
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.apply {
            setTitle("Oceń użytkownika")
            val view: View = layoutInflater.inflate(R.layout.dialog_rating, null)
            setView(view)
            setPositiveButton("Zatwierdź") { dialogInterface, i ->
                acceptedTaskListViewModel.rateUser(
                    view.findViewById<RatingBar>(R.id.dialog_rating_rating).rating,
                    view.findViewById<EditText>(R.id.dialog_rating_comment).text.toString()
                )

                val notificationManager = getSystemService(
                    requireContext(),
                    NotificationManager::class.java
                ) as NotificationManager

                notificationManager.sendNotification(
                    requireContext().getText(R.string.notification_rating_content).toString(),
                    requireContext()
                )

            }
            setNeutralButton("Anuluj") { dialogInterface, i -> }
            create()
            show()
        }
    }
}
package com.issen.workerfinder

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.issen.workerfinder.TaskApplication.Companion.currentLoggedInUserFull
import com.issen.workerfinder.database.WorkerFinderDatabase
import com.issen.workerfinder.database.models.UserDataFull
import com.issen.workerfinder.databinding.ActivityMainBinding
import com.issen.workerfinder.databinding.NavHeaderMainBinding
import com.issen.workerfinder.enums.CompletionTypes
import com.issen.workerfinder.enums.CyclicTypes
import com.issen.workerfinder.enums.PriorityTypes
import com.issen.workerfinder.ui.filters.CompletionFilterAdapter
import com.issen.workerfinder.ui.filters.CyclicFilterAdapter
import com.issen.workerfinder.ui.filters.PriorityFilterAdapter
import com.issen.workerfinder.ui.filters.UserListRecyclerViewAdapter
import com.issen.workerfinder.ui.misc.*
import com.issen.workerfinder.ui.taskList.TaskListFragment
import com.issen.workerfinder.utils.ViewAnimation
import com.issen.workerfinder.utils.hideAnimated
import com.issen.workerfinder.utils.nestedScrollTo
import com.issen.workerfinder.utils.showAnimated
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.drawer_content_task_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), OnDrawerRequestListener, OnCustomizeDrawerListener, OnFilterSelectionListener {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private var auth = FirebaseAuth.getInstance()
    private var doubleBackToExitPressedOnce = false
    private lateinit var mainActivityViewModel: MainActivityViewModel

    private lateinit var priorityAdapter: PriorityFilterAdapter
    private lateinit var cyclicAdapter: CyclicFilterAdapter
    private lateinit var completionAdapter: CompletionFilterAdapter
    private lateinit var userAdapter: UserListRecyclerViewAdapter
    private lateinit var workerAdapter: UserListRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mainBinding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mainBinding.drawerFilterClickListener = this

        val navHeaderBinding: NavHeaderMainBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.nav_header_main, mainBinding.root.nav_view, false)
        mainBinding.root.nav_view.addHeaderView(navHeaderBinding.root)

        main_loading.showAnimated()
        MainScope().launch(Dispatchers.IO) {
            currentLoggedInUserFull = WorkerFinderDatabase.getDatabase(applicationContext, lifecycleScope)
                .userDataDao
                .getUserByFirebaseKey(auth.currentUser!!.uid)
        }.invokeOnCompletion {
            navHeaderBinding.user = currentLoggedInUserFull
            main_loading.hideAnimated()
            WorkerFinderDatabase.getDatabase(applicationContext, lifecycleScope).populateComments(
                lifecycleScope, currentLoggedInUserFull
                !!.userData.userId
            )
            WorkerFinderDatabase.getDatabase(applicationContext, lifecycleScope).populateContacts(
                lifecycleScope, currentLoggedInUserFull
                !!.userData.userId
            )
            WorkerFinderDatabase.getDatabase(applicationContext, lifecycleScope).populateNotificationsOpen(
                lifecycleScope, currentLoggedInUserFull
                !!.userData.userId
            )
            mainActivityViewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        }
        prepareDrawer()
        handleUI()
        prepareFilterContent()
    }

    private fun handleUI() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navController = findNavController(R.id.nav_host_fragment)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_dashboard, R.id.nav_task_list, R.id.nav_task_board, R.id.nav_worker_board,
                R.id.nav_worker_list
            ), drawerLayout
        )

        setupNavigationMenu(navController)
        setupActionBar(navController, appBarConfiguration)
    }


    private fun prepareFilterContent() {
        workerAdapter = UserListRecyclerViewAdapter(this, true, mainActivityViewModel.currentTaskListFilter.filterByWorker)
        mainActivityViewModel.workerList.observe(this, Observer {
            it?.let {
                workerAdapter.submitList(it)
            }
        })
        drawer_filter_worker_container_recycler.adapter = workerAdapter

        userAdapter = UserListRecyclerViewAdapter(this, false, mainActivityViewModel.currentTaskListFilter.filterByUser)
        mainActivityViewModel.userListFull.observe(this, Observer {
            it?.let {
                userAdapter.submitList(it)
            }
        })
        drawer_filter_user_container_recycler.adapter = userAdapter

        priorityAdapter = PriorityFilterAdapter(this, PriorityTypes.values(), mainActivityViewModel.currentTaskListFilter.filterByPriority)
        drawer_filter_priority_container_recycler.adapter = priorityAdapter

        cyclicAdapter = CyclicFilterAdapter(this, CyclicTypes.values(), mainActivityViewModel.currentTaskListFilter.filterByCyclic)
        drawer_filter_cyclic_container_recycler.adapter = cyclicAdapter

        completionAdapter = CompletionFilterAdapter(this, CompletionTypes.values(), mainActivityViewModel.currentTaskListFilter.filterByCompletionType)
        drawer_filter_completion_container_recycler.adapter = completionAdapter
    }

    private fun setupNavigationMenu(navController: NavController) {
        val sideNavView = findViewById<NavigationView>(R.id.nav_view)
        sideNavView?.setupWithNavController(navController)

        sideNavView.menu.findItem(R.id.nav_logout)
            .setOnMenuItemClickListener {
                logout()
                true
            }
    }

    private fun setupActionBar(
        navController: NavController,
        appBarConfig: AppBarConfiguration
    ) {
        setupActionBarWithNavController(navController, appBarConfig)
    }

    private fun logout() {
        Firebase.auth.signOut()
        setResult(RESULT_CANCELED)
        finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun navigateProfile(view: View) {
        val bundle = bundleOf("userDataFull" to currentLoggedInUserFull!!)
        findNavController(R.id.nav_host_fragment).navigate(R.id.nav_user_profile, bundle)
        drawer_layout.closeDrawers()
    }

    private fun deleteUser(password: String) {
        val credential = EmailAuthProvider
            .getCredential(currentLoggedInUserFull!!.userData.email, password)
        auth.currentUser!!.reauthenticate(credential)
            .addOnSuccessListener {
                auth.currentUser!!.delete()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Account deleted successfully!", Toast.LENGTH_SHORT).show()
                            setResult(RESULT_CANCELED)
                            finish()
                        } else {
                            Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error authenticating user!", Toast.LENGTH_SHORT).show()
            }
    }

    fun askUserForPassword() {
        val promptsView: View = LayoutInflater.from(this).inflate(R.layout.dialog_password_prompt, null)
        val passwordInput = promptsView.findViewById<View>(R.id.dialog_user_password) as EditText

        val dialog = AlertDialog.Builder(this).apply {
            setView(promptsView)
            setCancelable(false)
            setTitle("Verification")
            setPositiveButton("Verify") { dialog, id -> deleteUser(passwordInput.text.toString()) }
            setNegativeButton("Cancel") { dialog, id -> dialog.cancel() }
        }

        dialog.create().show()
    }

    override fun onBackPressed() {
        when {
            drawer_layout.isDrawerOpen(GravityCompat.END) -> {
                onCancelClicked()
            }
            drawer_layout.isDrawerOpen(GravityCompat.START) -> {
                drawer_layout.closeDrawer(GravityCompat.START)
            }
            else -> {
                if (doubleBackToExitPressedOnce) {
                    super.onBackPressed()
                    finishAffinity()
                }

                this.doubleBackToExitPressedOnce = true
                Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show()

                Handler().postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
            }
        }
    }

    override fun onMainHeaderClicked(view: View) {
        toggleSectionInput(
            view.findViewWithTag("viewButton"),
            (view.parent as LinearLayout).findViewWithTag(view.tag.toString() + "Container")
        )
    }

    override fun onFilterContainerClicked(view: View) {
        drawer_filter_main_container.visibility = View.GONE
        drawer_filter_back.visibility = View.VISIBLE
        (drawer_filter_main_container.parent as LinearLayout).findViewWithTag<LinearLayout>(view.tag.toString() + "Container").visibility =
            View.VISIBLE
    }

    override fun onUserFilterSelected(userDataFull: UserDataFull, isWorker: Boolean, view: View) {
        if(isWorker) {
            mainActivityViewModel.setFilter(userDataFull.userData.userId, mainActivityViewModel.selectedTaskListFilter.filterByWorker)
        } else {
            mainActivityViewModel.setFilter(userDataFull.userData.userId, mainActivityViewModel.selectedTaskListFilter.filterByUser)
        }
        view.findViewWithTag<CheckBox>("checkbox").isChecked = !view.findViewWithTag<CheckBox>("checkbox").isChecked
    }

    override fun onFilterPriorityChanged(priorityTypes: PriorityTypes, view: View) {
        mainActivityViewModel.setFilter(priorityTypes.name, mainActivityViewModel.selectedTaskListFilter.filterByPriority)
        view.findViewWithTag<CheckBox>("checkbox").isChecked = !view.findViewWithTag<CheckBox>("checkbox").isChecked
    }

    override fun onFilterCompletionChanged(completionTypes: CompletionTypes, view: View) {
        mainActivityViewModel.setFilter(completionTypes.name, mainActivityViewModel.selectedTaskListFilter.filterByCompletionType)
        view.findViewWithTag<CheckBox>("checkbox").isChecked = !view.findViewWithTag<CheckBox>("checkbox").isChecked
    }

    override fun onFilterCyclicChanged(cyclicTypes: CyclicTypes, view: View) {
        mainActivityViewModel.setFilter(cyclicTypes.name, mainActivityViewModel.selectedTaskListFilter.filterByCyclic)
        view.findViewWithTag<CheckBox>("checkbox").isChecked = !view.findViewWithTag<CheckBox>("checkbox").isChecked
    }


    override fun onAcceptClicked() {
        when (val currentFragment = getCurrentlyDisplayedFragment()) {
            is TaskListFragment -> {
                mainActivityViewModel.applyFilters()
                currentFragment.onAcceptClicked(mainActivityViewModel.currentTaskListFilter)
            }
            else -> {
                Toast.makeText(this, "Error closing drawer!", Toast.LENGTH_SHORT).show()
            }
        }
        drawer_layout.closeDrawer(GravityCompat.END)
    }

    override fun onCancelClicked() {
        drawer_layout.closeDrawer(GravityCompat.END)

        //todo add sharedprefs
        //todo clear filters
        mainActivityViewModel.clearSelectedFilters()
        drawer_filter_group_radio.check(R.id.drawer_filter_group_none)
        drawer_filter_sort_radio.check(R.id.drawer_filter_sort_none)
    }

    override fun onBackClicked() {
        mainActivityViewModel.selectedTaskListFilter = mainActivityViewModel.currentTaskListFilter
        drawer_filter_worker_container.visibility = View.GONE
        drawer_filter_user_container.visibility = View.GONE
        drawer_filter_category_container.visibility = View.GONE
        drawer_filter_cyclic_container.visibility = View.GONE
        drawer_filter_priority_container.visibility = View.GONE
        drawer_filter_completion_container.visibility = View.GONE
        drawer_filter_main_container.visibility = View.VISIBLE
        drawer_filter_back.visibility = View.INVISIBLE
    }

    override fun onClearClicked() {
        mainActivityViewModel.selectedTaskListFilter = TaskListFilter()

        drawer_filter_group_radio.check(R.id.drawer_filter_group_none)
        drawer_filter_sort_radio.check(R.id.drawer_filter_sort_none)
        drawer_filter_sort_switch.isChecked = false

        completionAdapter.clearValues()
        cyclicAdapter.clearValues()
        priorityAdapter.clearValues()
        userAdapter.clearValues()
        workerAdapter.clearValues()
    }

    override fun onOrderSwitched(view: View) {
        mainActivityViewModel.setOrder(drawer_filter_sort_switch.isChecked)
    }

    private fun prepareDrawer() {
        drawer_filter_sort_radio.setOnCheckedChangeListener { radioGroup, checkedId ->
            val radio = radioGroup.findViewById<RadioButton>(checkedId)
            mainActivityViewModel.setSort(getSelectedSortValue(radio))
        }

        drawer_filter_group_radio.setOnCheckedChangeListener { radioGroup, checkedId ->
            val radio = radioGroup.findViewById<RadioButton>(checkedId)
            mainActivityViewModel.setGroup(getSelectedGroupValue(radio))
        }
    }

    private fun getSelectedGroupValue(radio: RadioButton): String {
        return when (radio.id) {
            R.id.drawer_filter_group_none -> {
                ""
            }
            R.id.drawer_filter_group_worker -> {
                "task_worker"
            }
            R.id.drawer_filter_group_user -> {
                "task_user"
            }
            R.id.drawer_filter_group_category -> {
                "task_category"
            }
            R.id.drawer_filter_group_cyclic_type -> {
                "task_cyclic_type"
            }
            R.id.drawer_filter_group_priority -> {
                "task_priority_type"
            }
            R.id.drawer_filter_group_completed -> {
                "task_completion_type"
            }
            else -> {
                ""
            }
        }
    }

    private fun getSelectedSortValue(radio: RadioButton): String {
        return when (radio.id) {
            R.id.drawer_filter_sort_none -> {
                ""
            }
            R.id.drawer_filter_sort_taskName -> {
                "task_title"
            }
            R.id.drawer_filter_sort_worker -> {
                "task_worker"
            }
            R.id.drawer_filter_sort_user -> {
                "task_user"
            }
            R.id.drawer_filter_sort_category -> {
                "task_category"
            }
            R.id.drawer_filter_sort_next_date -> {
                "task_next_completion_date"
            }
            R.id.drawer_filter_sort_priority -> {
                "task_priority_type"
            }
            R.id.drawer_filter_sort_completion_date -> {
                "task_completion_date"
            }
            else -> {
                ""
            }
        }
    }

    override fun onDrawerRequest(interactionImpl: (v: DrawerLayout) -> Unit) {
        interactionImpl(drawer_layout)
    }

    override fun onDrawerClose(interactionImpl: (v: DrawerLayout) -> Unit) {
        interactionImpl(drawer_layout)
    }

    private fun getCurrentlyDisplayedFragment() = (supportFragmentManager.primaryNavigationFragment as NavHostFragment)
        .childFragmentManager.primaryNavigationFragment

    private fun toggleSectionInput(view: View, containerView: View) {
        val show = toggleArrow(view)
        if (show) {
            ViewAnimation.expand(containerView) {
                fun onFinish() {
                    nestedScrollTo(
                        drawer_filter_nested_scroll_view,
                        containerView
                    )
                }
            }
        } else {
            ViewAnimation.collapse(containerView)
        }
    }

    private fun toggleArrow(view: View): Boolean {
        return if (view.rotation == 0f) {
            view.animate().setDuration(200).rotation(180f)
            true
        } else {
            view.animate().setDuration(200).rotation(0f)
            false
        }
    }

}
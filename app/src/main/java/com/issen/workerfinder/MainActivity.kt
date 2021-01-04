package com.issen.workerfinder

import android.os.Bundle
import android.os.Handler
import android.view.*
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
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
import com.issen.workerfinder.WorkerFinderApplication.Companion.currentLoggedInUserFull
import com.issen.workerfinder.database.models.UserDataFull
import com.issen.workerfinder.databinding.ActivityMainBinding
import com.issen.workerfinder.databinding.NavHeaderMainBinding
import com.issen.workerfinder.enums.CompletionTypes
import com.issen.workerfinder.enums.CyclicTypes
import com.issen.workerfinder.enums.PriorityTypes
import com.issen.workerfinder.ui.contactBoard.ContactBoardFragment
import com.issen.workerfinder.ui.filters.*
import com.issen.workerfinder.ui.misc.OnCustomizeDrawerListener
import com.issen.workerfinder.ui.misc.OnDrawerRequestListener
import com.issen.workerfinder.ui.misc.OnFilterSelectionListener
import com.issen.workerfinder.ui.taskBoard.TaskBoardFragment
import com.issen.workerfinder.ui.taskList.CreatedTaskListFragment
import com.issen.workerfinder.utils.ViewAnimation
import com.issen.workerfinder.utils.hideAnimated
import com.issen.workerfinder.utils.nestedScrollTo
import com.issen.workerfinder.utils.showAnimated
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.drawer_content_task_board.*
import kotlinx.android.synthetic.main.drawer_content_task_list.*
import kotlinx.android.synthetic.main.drawer_content_worker_board.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), OnDrawerRequestListener, OnCustomizeDrawerListener, OnFilterSelectionListener {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private var auth = FirebaseAuth.getInstance()
    private var doubleBackToExitPressedOnce = false
    private val mainActivityViewModel: MainActivityViewModel by viewModels {
        MainActivityViewModelFactory(
            (application as WorkerFinderApplication).userRepository,
            (application as WorkerFinderApplication).database
        )
    }

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
        MainScope().launch {
            currentLoggedInUserFull = (application as WorkerFinderApplication).userRepository.getUserByFirebaseKey(auth.currentUser!!.uid)
            navHeaderBinding.user = currentLoggedInUserFull
            main_loading.hideAnimated()
            prepareFilterContent()
        }
        prepareDrawer()
        handleUI()
    }

    private fun handleUI() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navController = findNavController(R.id.nav_host_fragment)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_dashboard, R.id.nav_task_list, R.id.nav_task_board, R.id.nav_contact_board,
                R.id.nav_contact_list
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
        drawer_filter_task_list_worker_container_recycler.adapter = workerAdapter

        userAdapter = UserListRecyclerViewAdapter(this, false, mainActivityViewModel.currentTaskListFilter.filterByUser)
        mainActivityViewModel.userListFull.observe(this, Observer {
            it?.let {
                userAdapter.submitList(it)
            }
        })
        drawer_filter_task_list_user_container_recycler.adapter = userAdapter

        priorityAdapter = PriorityFilterAdapter(this, PriorityTypes.values(), mainActivityViewModel.currentTaskListFilter.filterByPriority)
        drawer_filter_task_list_priority_container_recycler.adapter = priorityAdapter

        cyclicAdapter = CyclicFilterAdapter(this, CyclicTypes.values(), mainActivityViewModel.currentTaskListFilter.filterByCyclic)
        drawer_filter_task_list_cyclic_container_recycler.adapter = cyclicAdapter

        completionAdapter =
            CompletionFilterAdapter(this, CompletionTypes.values(), mainActivityViewModel.currentTaskListFilter.filterByCompletionType)
        drawer_filter_task_list_completion_container_recycler.adapter = completionAdapter
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_database, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.action_populateDb -> {
                populateDb()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun populateDb() {
        mainActivityViewModel.populateDb()
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
                onCloseClicked()
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
        when (getCurrentlyDisplayedFragment()) {
            is CreatedTaskListFragment -> {
                drawer_filter_task_list_main_container.visibility = View.GONE
                (drawer_filter_task_list_main_container.parent as LinearLayout).findViewWithTag<LinearLayout>(view.tag.toString() + "Container").visibility =
                    View.VISIBLE
            }
            is TaskBoardFragment -> {
                drawer_filter_task_board_main_container.visibility = View.GONE
                (drawer_filter_task_board_main_container.parent as LinearLayout).findViewWithTag<LinearLayout>(
                    view.tag.toString() + "Container"
                ).visibility = View.VISIBLE
            }
            is ContactBoardFragment -> {
                drawer_filter_worker_board_main_container.visibility = View.GONE
                (drawer_filter_worker_board_main_container.parent as LinearLayout).findViewWithTag<LinearLayout>(
                    view.tag.toString() + "Container"
                ).visibility = View.VISIBLE
            }
        }
        drawer_filter_back.visibility = View.VISIBLE

    }

    override fun onUserFilterSelected(userDataFull: UserDataFull, isWorker: Boolean, view: View) {
        when (getCurrentlyDisplayedFragment()) {
            is CreatedTaskListFragment -> {
                if (isWorker) {
                    mainActivityViewModel.setFilter(
                        userDataFull.userData.userId,
                        mainActivityViewModel.selectedTaskListFilter.filterByWorker
                    )
                } else {
                    mainActivityViewModel.setFilter(userDataFull.userData.userId, mainActivityViewModel.selectedTaskListFilter.filterByUser)
                }
            }
            is TaskBoardFragment -> {
                mainActivityViewModel.setFilter(userDataFull.userData.userId, mainActivityViewModel.selectedTaskBoardFilter.filterByUser)
            }
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
        when (getCurrentlyDisplayedFragment()) {
            is CreatedTaskListFragment -> {
                mainActivityViewModel.setFilter(cyclicTypes.name, mainActivityViewModel.selectedTaskListFilter.filterByCyclic)
            }
            is TaskBoardFragment -> {
                mainActivityViewModel.setFilter(cyclicTypes.name, mainActivityViewModel.selectedTaskBoardFilter.filterByCyclic)
            }
        }
        view.findViewWithTag<CheckBox>("checkbox").isChecked = !view.findViewWithTag<CheckBox>("checkbox").isChecked
    }


    override fun onAcceptClicked() {
        when (val currentFragment = getCurrentlyDisplayedFragment()) {
            is CreatedTaskListFragment -> {
                mainActivityViewModel.applyTaskListFilters()
                currentFragment.onAcceptClicked(mainActivityViewModel.currentTaskListFilter)
            }
            is TaskBoardFragment -> {
                mainActivityViewModel.applyTaskBoardFilters()
                currentFragment.onAcceptClicked(mainActivityViewModel.currentTaskBoardFilter)
            }
            is ContactBoardFragment -> {
                mainActivityViewModel.applyWorkerBoardFilters()
                currentFragment.onAcceptClicked(mainActivityViewModel.currentWorkerBoardFilter)
            }
        }
        drawer_layout.closeDrawer(GravityCompat.END)
    }

    override fun onCloseClicked() {
        drawer_layout.closeDrawer(GravityCompat.END)
    }

    override fun onBackClicked() {
        when (getCurrentlyDisplayedFragment()) {
            is CreatedTaskListFragment -> {
                mainActivityViewModel.clearSelectedTaskListFilters()
                drawer_filter_task_list_worker_container.visibility = View.GONE
                drawer_filter_task_list_user_container.visibility = View.GONE
                drawer_filter_task_list_category_container.visibility = View.GONE
                drawer_filter_task_list_cyclic_container.visibility = View.GONE
                drawer_filter_task_list_priority_container.visibility = View.GONE
                drawer_filter_task_list_completion_container.visibility = View.GONE
                drawer_filter_task_list_main_container.visibility = View.VISIBLE
            }
            is TaskBoardFragment -> {
                mainActivityViewModel.clearSelectedTaskBoardFilters()
                drawer_filter_task_board_user_container.visibility = View.GONE
                drawer_filter_task_board_category_container.visibility = View.GONE
                drawer_filter_task_board_cyclic_container.visibility = View.GONE
                drawer_filter_task_board_localization_container.visibility = View.GONE
                drawer_filter_task_board_pay_container.visibility = View.GONE
                drawer_filter_task_board_rating_container.visibility = View.GONE
                drawer_filter_task_board_due_date_container.visibility = View.GONE
                drawer_filter_task_board_main_container.visibility = View.VISIBLE
            }
            is ContactBoardFragment -> {
                mainActivityViewModel.clearSelectedWorkerBoardFilters()
                drawer_filter_worker_board_category_container.visibility = View.GONE
                drawer_filter_worker_board_localization_container.visibility = View.GONE
                drawer_filter_worker_board_rating_container.visibility = View.GONE
                drawer_filter_worker_board_main_container.visibility = View.VISIBLE
            }
        }
        drawer_filter_back.visibility = View.INVISIBLE
    }

    override fun onClearClicked() {
        when (getCurrentlyDisplayedFragment()) {
            is CreatedTaskListFragment -> {
                mainActivityViewModel.selectedTaskListFilter = FilterContainer()

                drawer_filter_task_list_group_radio.check(R.id.drawer_filter_task_list_group_none)
                drawer_filter_task_list_sort_radio.check(R.id.drawer_filter_task_list_sort_none)
                drawer_filter_task_list_sort_switch.isChecked = false

                completionAdapter.clearValues()
                cyclicAdapter.clearValues()
                priorityAdapter.clearValues()
                userAdapter.clearValues()
                workerAdapter.clearValues()
            }
            is TaskBoardFragment -> {
                mainActivityViewModel.selectedTaskBoardFilter = FilterContainer()

                drawer_filter_task_board_group_radio.check(R.id.drawer_filter_task_board_group_none)
                drawer_filter_task_board_sort_radio.check(R.id.drawer_filter_task_board_sort_none)
                drawer_filter_task_board_sort_switch.isChecked = false

                completionAdapter.clearValues()
                cyclicAdapter.clearValues()
                priorityAdapter.clearValues()
                userAdapter.clearValues()
                workerAdapter.clearValues()
            }
            is ContactBoardFragment -> {
                mainActivityViewModel.selectedWorkerBoardFilter = FilterContainer()

                drawer_filter_worker_board_sort_radio.check(R.id.drawer_filter_worker_board_sort_none)
                drawer_filter_worker_board_sort_switch.isChecked = false

                completionAdapter.clearValues()
                cyclicAdapter.clearValues()
                priorityAdapter.clearValues()
                userAdapter.clearValues()
                workerAdapter.clearValues()
            }
        }
    }

    override fun onOrderSwitched(view: View) {
        when (getCurrentlyDisplayedFragment()) {
            is CreatedTaskListFragment -> {
                mainActivityViewModel.setOrder(drawer_filter_task_list_sort_switch.isChecked, mainActivityViewModel.selectedTaskListFilter)
            }
            is TaskBoardFragment -> {
                mainActivityViewModel.setOrder(
                    drawer_filter_task_board_sort_switch.isChecked,
                    mainActivityViewModel.selectedTaskBoardFilter
                )
            }
            is ContactBoardFragment -> {
                mainActivityViewModel.setOrder(
                    drawer_filter_worker_board_sort_switch.isChecked,
                    mainActivityViewModel.selectedWorkerBoardFilter
                )
            }
        }
    }

    private fun prepareDrawer() {

        //todo rating has no tag in worker and task boards
        drawer_filter_task_list_sort_radio.setOnCheckedChangeListener { radioGroup, checkedId ->
            val radio = radioGroup.findViewById<RadioButton>(checkedId)
            mainActivityViewModel.setSort(radio.tag.toString(), mainActivityViewModel.selectedTaskListFilter)
        }
        drawer_filter_task_list_group_radio.setOnCheckedChangeListener { radioGroup, checkedId ->
            val radio = radioGroup.findViewById<RadioButton>(checkedId)
            mainActivityViewModel.setGroup(radio.tag.toString(), mainActivityViewModel.selectedTaskListFilter)
        }

        drawer_filter_task_board_sort_radio.setOnCheckedChangeListener { radioGroup, checkedId ->
            val radio = radioGroup.findViewById<RadioButton>(checkedId)
            mainActivityViewModel.setSort(radio.tag.toString(), mainActivityViewModel.selectedTaskBoardFilter)
        }
        drawer_filter_task_board_group_radio.setOnCheckedChangeListener { radioGroup, checkedId ->
            val radio = radioGroup.findViewById<RadioButton>(checkedId)
            mainActivityViewModel.setGroup(radio.tag.toString(), mainActivityViewModel.selectedTaskBoardFilter)
        }

        drawer_filter_worker_board_sort_radio.setOnCheckedChangeListener { radioGroup, checkedId ->
            val radio = radioGroup.findViewById<RadioButton>(checkedId)
            mainActivityViewModel.setSort(radio.tag.toString(), mainActivityViewModel.selectedWorkerBoardFilter)
        }
    }

    override fun onDrawerRequest(interactionImpl: (v: DrawerLayout) -> Unit) {
        interactionImpl(drawer_layout)
        when (getCurrentlyDisplayedFragment()) {
            is CreatedTaskListFragment -> {
                drawer_content_task_list.visibility = View.VISIBLE
                drawer_content_task_board.visibility = View.GONE
                drawer_content_worker_board.visibility = View.GONE
            }
            is TaskBoardFragment -> {
                drawer_content_task_list.visibility = View.GONE
                drawer_content_task_board.visibility = View.VISIBLE
                drawer_content_worker_board.visibility = View.GONE
            }
            is ContactBoardFragment -> {
                drawer_content_task_list.visibility = View.GONE
                drawer_content_task_board.visibility = View.GONE
                drawer_content_worker_board.visibility = View.VISIBLE
            }
        }
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
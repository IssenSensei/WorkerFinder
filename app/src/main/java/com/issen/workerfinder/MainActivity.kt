package com.issen.workerfinder

import android.os.Bundle
import android.os.Handler
import android.view.*
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
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
import com.issen.workerfinder.ui.contactAdd.ContactAddFragment
import com.issen.workerfinder.ui.contactBoard.ContactBoardFragment
import com.issen.workerfinder.ui.contactList.ContactListFragment
import com.issen.workerfinder.ui.filters.*
import com.issen.workerfinder.ui.misc.OnCustomizeDrawerListener
import com.issen.workerfinder.ui.misc.OnDrawerRequestListener
import com.issen.workerfinder.ui.misc.OnFilterSelectionListener
import com.issen.workerfinder.ui.taskBoard.TaskBoardFragment
import com.issen.workerfinder.ui.taskList.AcceptedTaskListFragment
import com.issen.workerfinder.ui.taskList.CommissionedTaskListFragment
import com.issen.workerfinder.ui.taskList.CreatedTaskListFragment
import com.issen.workerfinder.ui.taskList.TaskListFragment
import com.issen.workerfinder.utils.ViewAnimation
import com.issen.workerfinder.utils.hideAnimated
import com.issen.workerfinder.utils.showAnimated
import com.issen.workerfinder.utils.toggleArrow
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.drawer_filter_content.*
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
    private lateinit var selectedFilterContainerLayout: LinearLayout

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
        //todo send to fragments
        workerAdapter = UserListRecyclerViewAdapter(this, true, getCurrentSelectedFilterContainer().filterByWorker)
        mainActivityViewModel.workerList.observe(this, Observer {
            it?.let {
                workerAdapter.submitList(it)
            }
        })
        drawer_filter_worker_container_recycler.adapter = workerAdapter

        userAdapter = UserListRecyclerViewAdapter(this, false, getCurrentSelectedFilterContainer().filterByUser)
        mainActivityViewModel.userListFull.observe(this, Observer {
            it?.let {
                userAdapter.submitList(it)
            }
        })
        drawer_filter_user_container_recycler.adapter = userAdapter

        priorityAdapter = PriorityFilterAdapter(this, PriorityTypes.values(), getCurrentSelectedFilterContainer().filterByPriority)
        drawer_filter_priority_container_recycler.adapter = priorityAdapter

        cyclicAdapter = CyclicFilterAdapter(this, CyclicTypes.values(), getCurrentSelectedFilterContainer().filterByCyclic)
        drawer_filter_cyclic_container_recycler.adapter = cyclicAdapter

        completionAdapter =
            CompletionFilterAdapter(this, CompletionTypes.values(), getCurrentSelectedFilterContainer().filterByCompletionType)
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_database, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
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
        drawer_filter_main_container.visibility = View.GONE
        selectedFilterContainerLayout = (drawer_filter_main_container.parent as LinearLayout).findViewWithTag(view.tag.toString() + "Container")
        selectedFilterContainerLayout.visibility = View.VISIBLE
        drawer_filter_back.visibility = View.VISIBLE
    }

    override fun onBackClicked() {
        drawer_filter_main_container.visibility = View.VISIBLE
        if (::selectedFilterContainerLayout.isInitialized) {
            selectedFilterContainerLayout.visibility = View.GONE
        }
        drawer_filter_back.visibility = View.INVISIBLE
    }

    override fun onUserFilterSelected(userDataFull: UserDataFull, isWorker: Boolean, view: View) {
        if (isWorker) {
            mainActivityViewModel.setFilter(
                userDataFull.userData.userId,
                getCurrentSelectedFilterContainer().filterByWorker
            )
        } else {
            mainActivityViewModel.setFilter(userDataFull.userData.userId, getCurrentSelectedFilterContainer().filterByUser)
        }
        view.findViewWithTag<CheckBox>("checkbox").isChecked = !view.findViewWithTag<CheckBox>("checkbox").isChecked
    }

    override fun onFilterPriorityChanged(priorityTypes: PriorityTypes, view: View) {
        mainActivityViewModel.setFilter(priorityTypes.name, getCurrentSelectedFilterContainer().filterByPriority)
        view.findViewWithTag<CheckBox>("checkbox").isChecked = !view.findViewWithTag<CheckBox>("checkbox").isChecked
    }

    override fun onFilterCompletionChanged(completionTypes: CompletionTypes, view: View) {
        mainActivityViewModel.setFilter(completionTypes.name, getCurrentSelectedFilterContainer().filterByCompletionType)
        view.findViewWithTag<CheckBox>("checkbox").isChecked = !view.findViewWithTag<CheckBox>("checkbox").isChecked
    }

    override fun onFilterCyclicChanged(cyclicTypes: CyclicTypes, view: View) {
        mainActivityViewModel.setFilter(cyclicTypes.name, getCurrentSelectedFilterContainer().filterByCyclic)
        view.findViewWithTag<CheckBox>("checkbox").isChecked = !view.findViewWithTag<CheckBox>("checkbox").isChecked
    }


    override fun onAcceptClicked() {
        when (val currentFragment = getCurrentlyDisplayedFragment()) {
            is TaskListFragment -> {
                when (val fragment = currentFragment.getCurrentListFragment()) {
                    is CreatedTaskListFragment -> {
                        mainActivityViewModel.applyCreatedTaskListFilters()
                        fragment.onAcceptClicked(mainActivityViewModel.currentCreatedTaskListFilter)
                    }
                    is AcceptedTaskListFragment -> {
                        mainActivityViewModel.applyAcceptedTaskListFilters()
                        fragment.onAcceptClicked(mainActivityViewModel.currentAcceptedTaskListFilter)
                    }
                    is CommissionedTaskListFragment -> {
                        mainActivityViewModel.applyCommissionedTaskListFilters()
                        fragment.onAcceptClicked(mainActivityViewModel.currentCommissionedTaskListFilter)
                    }
                }
            }
            is TaskBoardFragment -> {
                mainActivityViewModel.applyTaskBoardFilters()
                currentFragment.onAcceptClicked(mainActivityViewModel.currentTaskBoardFilter)
            }
            is ContactListFragment -> {
                mainActivityViewModel.applyContactListFilters()
                currentFragment.onAcceptClicked(mainActivityViewModel.currentContactListFilter)
            }
            is ContactAddFragment -> {
                mainActivityViewModel.applyContactAddFilters()
                currentFragment.onAcceptClicked(mainActivityViewModel.currentContactAddFilter)
            }
            is ContactBoardFragment -> {
                mainActivityViewModel.applyContactBoardFilters()
                currentFragment.onAcceptClicked(mainActivityViewModel.currentContactBoardFilter)
            }
        }
        drawer_layout.closeDrawer(GravityCompat.END)
    }

    override fun onCloseClicked() {
        drawer_layout.closeDrawer(GravityCompat.END)
    }

    override fun onClearClicked() {
        when (val currentFragment = getCurrentlyDisplayedFragment()) {
            is TaskListFragment -> {
                when (val fragment = currentFragment.getCurrentListFragment()) {
                    is CreatedTaskListFragment -> {
                        mainActivityViewModel.selectedCreatedTaskListFilter.clearData()
                    }
                    is AcceptedTaskListFragment -> {
                        mainActivityViewModel.selectedAcceptedTaskListFilter.clearData()
                    }
                    is CommissionedTaskListFragment -> {
                        mainActivityViewModel.selectedCommissionedTaskListFilter.clearData()
                    }
                }
            }
            is TaskBoardFragment -> {
                mainActivityViewModel.selectedTaskBoardFilter.clearData()
            }
            is ContactListFragment -> {
                mainActivityViewModel.selectedContactListFilter.clearData()
            }
            is ContactAddFragment -> {
                mainActivityViewModel.selectedContactAddFilter.clearData()
            }
            is ContactBoardFragment -> {
                mainActivityViewModel.selectedContactBoardFilter.clearData()
            }
        }
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
        drawer_filter_sort_switch.isChecked = view.tag == "asc"
        mainActivityViewModel.setOrder(drawer_filter_sort_switch.isChecked, getCurrentSelectedFilterContainer())
    }

    override fun onOrderSwitchClicked(view: View) {
        mainActivityViewModel.setOrder((view as SwitchCompat).isChecked, getCurrentSelectedFilterContainer())
    }

    private fun prepareDrawer() {
        (drawer_layout as DrawerLayout).addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}
            override fun onDrawerOpened(drawerView: View) {}
            override fun onDrawerStateChanged(newState: Int) {}

            override fun onDrawerClosed(drawerView: View) {
                cleanUpDrawer(drawer_layout)
                getCurrentSelectedFilterContainer().resetData(getCurrentCurrentFilterContainer())
            }
        })

        //todo rating has no tag in worker and task boards
        drawer_filter_sort_radio.setOnCheckedChangeListener { radioGroup, checkedId ->
            val radio = radioGroup.findViewById<RadioButton>(checkedId)
            mainActivityViewModel.setSort(radio.tag.toString(), getCurrentSelectedFilterContainer())
        }
        drawer_filter_group_radio.setOnCheckedChangeListener { radioGroup, checkedId ->
            val radio = radioGroup.findViewById<RadioButton>(checkedId)
            mainActivityViewModel.setGroup(radio.tag.toString(), getCurrentSelectedFilterContainer())
        }
    }

    override fun onDrawerRequest(interactionImpl: (v: DrawerLayout) -> Unit) {
        interactionImpl(drawer_layout)
    }

    override fun onDrawerClose(interactionImpl: (v: DrawerLayout) -> Unit) {
        interactionImpl(drawer_layout)
    }

    private fun cleanUpDrawer(drawerLayout: DrawerLayout) {
        onBackClicked()
        drawerLayout.findViewById<LinearLayout>(R.id.drawer_filter_filter_worker_subheader).visibility = View.GONE
        drawerLayout.findViewById<LinearLayout>(R.id.drawer_filter_filter_user_subheader).visibility = View.GONE
        drawerLayout.findViewById<LinearLayout>(R.id.drawer_filter_filter_category_subheader).visibility = View.GONE
        drawerLayout.findViewById<LinearLayout>(R.id.drawer_filter_filter_cyclic_subheader).visibility = View.GONE
        drawerLayout.findViewById<LinearLayout>(R.id.drawer_filter_filter_localization_subheader).visibility = View.GONE
        drawerLayout.findViewById<LinearLayout>(R.id.drawer_filter_filter_pay_subheader).visibility = View.GONE
        drawerLayout.findViewById<LinearLayout>(R.id.drawer_filter_filter_rating_subheader).visibility = View.GONE
        drawerLayout.findViewById<LinearLayout>(R.id.drawer_filter_filter_date_subheader).visibility = View.GONE
        drawerLayout.findViewById<LinearLayout>(R.id.drawer_filter_filter_completion_subheader).visibility = View.GONE
        drawerLayout.findViewById<LinearLayout>(R.id.drawer_filter_filter_priority_subheader).visibility = View.GONE
        drawerLayout.findViewById<LinearLayout>(R.id.drawer_filter_filter_search_subheader).visibility = View.GONE
        drawerLayout.findViewById<LinearLayout>(R.id.drawer_filter_filter_open_for_work_subheader).visibility = View.GONE
        drawerLayout.findViewById<RadioButton>(R.id.drawer_filter_sort_completion_date).visibility = View.GONE
        drawerLayout.findViewById<RadioButton>(R.id.drawer_filter_sort_pay).visibility = View.GONE
        drawerLayout.findViewById<RadioButton>(R.id.drawer_filter_sort_rating).visibility = View.GONE
        drawerLayout.findViewById<RadioButton>(R.id.drawer_filter_group_priority).visibility = View.GONE
        drawerLayout.findViewById<RadioButton>(R.id.drawer_filter_group_localization).visibility = View.GONE
        drawerLayout.findViewById<RadioButton>(R.id.drawer_filter_group_completion_date).visibility = View.GONE
    }

    private fun getCurrentSelectedFilterContainer(): FilterContainer {
        return when (val fragment = getCurrentlyDisplayedFragment()) {
            is TaskListFragment -> {
                when (fragment.getCurrentListFragment()) {
                    is CreatedTaskListFragment -> {
                        mainActivityViewModel.selectedCreatedTaskListFilter
                    }
                    is AcceptedTaskListFragment -> {
                        mainActivityViewModel.selectedAcceptedTaskListFilter
                    }
                    is CommissionedTaskListFragment -> {
                        mainActivityViewModel.selectedCommissionedTaskListFilter
                    }
                    else -> {
                        FilterContainer()
                    }
                }
            }
            is TaskBoardFragment -> {
                mainActivityViewModel.selectedTaskBoardFilter
            }
            is ContactListFragment -> {
                mainActivityViewModel.selectedContactBoardFilter
            }
            is ContactAddFragment -> {
                mainActivityViewModel.selectedContactBoardFilter
            }
            is ContactBoardFragment -> {
                mainActivityViewModel.selectedContactBoardFilter
            }
            else -> FilterContainer()
        }
    }

    private fun getCurrentCurrentFilterContainer(): FilterContainer {
        return when (val fragment = getCurrentlyDisplayedFragment()) {
            is TaskListFragment -> {
                when (fragment.getCurrentListFragment()) {
                    is CreatedTaskListFragment -> {
                        mainActivityViewModel.currentCreatedTaskListFilter
                    }
                    is AcceptedTaskListFragment -> {
                        mainActivityViewModel.currentAcceptedTaskListFilter
                    }
                    is CommissionedTaskListFragment -> {
                        mainActivityViewModel.currentCommissionedTaskListFilter
                    }
                    else -> {
                        FilterContainer()
                    }
                }
            }
            is TaskBoardFragment -> {
                mainActivityViewModel.currentTaskBoardFilter
            }
            is ContactListFragment -> {
                mainActivityViewModel.currentContactBoardFilter
            }
            is ContactAddFragment -> {
                mainActivityViewModel.currentContactBoardFilter
            }
            is ContactBoardFragment -> {
                mainActivityViewModel.currentContactBoardFilter
            }
            else -> FilterContainer()
        }
    }

    private fun getCurrentlyDisplayedFragment() = (supportFragmentManager.primaryNavigationFragment as NavHostFragment)
        .childFragmentManager.primaryNavigationFragment

    private fun toggleSectionInput(view: View, containerView: View) {
        val show = toggleArrow(view)
        if (show) {
            ViewAnimation.expand(containerView) {
            }
        } else {
            ViewAnimation.collapse(containerView)
        }
    }


}
package com.issen.workerfinder

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.AuthUI.IdpConfig
import com.firebase.ui.auth.AuthUI.IdpConfig.EmailBuilder
import com.firebase.ui.auth.IdpResponse
import com.google.android.gms.auth.api.credentials.Credentials
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.*
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.issen.workerfinder.TaskApplication.Companion.currentLoggedInUser
import com.issen.workerfinder.database.UserModel
import com.issen.workerfinder.database.WorkerFinderDatabase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private lateinit var mFirebaseAuth: FirebaseAuth
    private lateinit var mFirebaseUser: FirebaseUser
    private lateinit var mAuthListener: AuthStateListener

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        handleAuth()
        handleUI()

    }

    override fun onStart() {
        super.onStart()
        mFirebaseAuth.addAuthStateListener(mAuthListener)
    }

    override fun onStop() {
        super.onStop()
        if (mAuthListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthListener)
        }
    }

    private fun handleAuth() {
        mFirebaseAuth = FirebaseAuth.getInstance()
        mAuthListener = AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user != null) {
                mFirebaseUser = firebaseAuth.currentUser!!
                currentLoggedInUser = mFirebaseUser
            } else {
                val actionCodeSettings = ActionCodeSettings.newBuilder()
                    .setAndroidPackageName("com.issen.workerfinder", true, null)
                    .setHandleCodeInApp(true)
                    .setUrl("https://workerfinder.page.link")
                    .build();

                val providers = arrayListOf(
                    EmailBuilder().enableEmailLinkSignIn()
                        .setActionCodeSettings(actionCodeSettings).build(),
                    IdpConfig.GoogleBuilder().build()
                )

                startActivityForResult(
                    AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                    RC_SIGN_IN
                )
            }
        }

        if (AuthUI.canHandleIntent(intent)) {
            val link = intent?.data?.toString()
            val providers: List<IdpConfig> = listOf(
                EmailBuilder().build()
            )
            if (link != null) {
                startActivityForResult(
                    AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setEmailLink(link)
                        .setAvailableProviders(providers)
                        .build(),
                    RC_SIGN_IN_EMAIL_LINK
                )
            }
        }
    }

    private fun handleUI() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navController = findNavController(R.id.nav_host_fragment)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_task_list, R.id.nav_new_task, R.id.nav_new_worker,
                R.id.nav_workers, R.id.nav_settings, R.id.nav_info, R.id.nav_about
            ), drawerLayout
        )

        setupNavigationMenu(navController)
        setupActionBar(navController, appBarConfiguration)
    }

    private fun setupNavigationMenu(navController: NavController) {
        val sideNavView = findViewById<NavigationView>(R.id.nav_view)
        sideNavView?.setupWithNavController(navController)
    }

    private fun setupActionBar(
        navController: NavController,
        appBarConfig: AppBarConfiguration
    ) {
        setupActionBarWithNavController(navController, appBarConfig)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_logout -> {
                logout()
            }
            R.id.action_settings -> {
                //todo
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun logout() {
        AuthUI.getInstance().signOut(this)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                prepareUser(response)
            } else {
                if (response != null)
                    Toast.makeText(this, "Error occured, please try again", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun prepareUser(response: IdpResponse?) {
        val user = FirebaseAuth.getInstance().currentUser
        mFirebaseUser = user!!
        if (response?.isNewUser!!) {
            MainScope().launch {
                currentLoggedInUser = mFirebaseUser
                WorkerFinderDatabase.getDatabase(applicationContext, this).userModelDao.insert(
                    UserModel(
                        0,
                        user.displayName.toString(),
                        user.photoUrl.toString(),
                        user.email.toString(),
                        user.phoneNumber.toString(),
                        mFirebaseUser.uid,
                        false
                    )
                )
            }
        }
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putString("userToken", response.idpToken)
            apply()
        }
    }

    fun navigateProfile(view: View) {
        findNavController(R.id.nav_host_fragment).navigate(R.id.nav_user_profile)
        drawer_layout.closeDrawers()
    }


    //todo read more about tokens and authentication in firebase
    fun deleteUser() {
        val currentUser = FirebaseAuth.getInstance().currentUser!!

        val sharedPref = this.getPreferences(Context.MODE_PRIVATE) ?: return
        val userToken = sharedPref.getString("userToken", null)

        if(userToken != null){
            val credential = GoogleAuthProvider.getCredential(userToken, null)
            FirebaseAuth.getInstance().currentUser!!.reauthenticate(credential).addOnSuccessListener {
                currentUser.delete()
                    .addOnFailureListener {
                        Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
                    }
                    .addOnSuccessListener {
                        Credentials.getClient(this).disableAutoSignIn();
                    }
            }
        } else {
            Toast.makeText(this, "Incorrect token", Toast.LENGTH_SHORT).show()
        }

    }

    companion object {

        private const val RC_SIGN_IN = 9000
        private const val RC_SIGN_IN_EMAIL_LINK = 9001
    }


}
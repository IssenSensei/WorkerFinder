package com.issen.workerfinder.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.issen.workerfinder.MainActivity
import com.issen.workerfinder.R
import com.issen.workerfinder.WorkerFinderApplication
import com.issen.workerfinder.database.models.UserData
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        auth = Firebase.auth
    }

    fun register(view: View) {
        auth.createUserWithEmailAndPassword(activity_register_email.text.toString(), activity_register_password.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    MainScope().launch {
                        (application as WorkerFinderApplication).userRepository.insert(
                            UserData(
                                auth.currentUser!!.uid,
                                activity_register_user_name.text.toString(),
                                "",
                                activity_register_email.text.toString(),
                                activity_register_phone.text.toString(),
                                "",
                                "",
                                false,
                                false
                            )
                        )
                    }.invokeOnCompletion {
                        navigateMain()
                    }
                } else {
                    Toast.makeText(
                        baseContext, "Register failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun navigateMain() {
        val startMain = Intent(this, MainActivity::class.java)
        startActivityForResult(startMain, 0)
    }

    fun navigateLogin(view: View) {
        setResult(RESULT_CANCELED)
        finish()
    }
}

//
//    override fun onStart() {
//        super.onStart()
//        mFirebaseAuth.addAuthStateListener(mAuthListener)
//    }
//
//    val mAuthListener = AuthStateListener { firebaseAuth ->
//            val user = firebaseAuth.currentUser
//            if (user != null) {
//                mFirebaseUser = firebaseAuth.currentUser!!
//
//                currentLoggedInUser =
//                    WorkerFinderDatabase.getDatabase(applicationContext, this.lifecycleScope)
//                        .userModelDao
//                        .getUserById(user.uid)
//                        .value!!
//
//            } else {
//                val actionCodeSettings = ActionCodeSettings.newBuilder()
//                    .setAndroidPackageName("com.issen.workerfinder", true, null)
//                    .setHandleCodeInApp(true)
//                    .setUrl("https://workerfinder.page.link")
//                    .build();
//
//                val providers = arrayListOf(
//                    EmailBuilder().enableEmailLinkSignIn()
//                        .setActionCodeSettings(actionCodeSettings).build(),
//                    IdpConfig.GoogleBuilder().build()
//                )
//
//                startActivityForResult(
//                    AuthUI.getInstance()
//                        .createSignInIntentBuilder()
//                        .setAvailableProviders(providers)
//                        .build(),
//                    RC_SIGN_IN
//                )
//            }
//        }
//
//    override fun onStop() {
//        super.onStop()
//        if (mAuthListener != null) {
//            mFirebaseAuth.removeAuthStateListener(mAuthListener)
//        }
//    }
//
//    private fun handleAuth() {
//        mFirebaseAuth = FirebaseAuth.getInstance()
//        mAuthListener = AuthStateListener { firebaseAuth ->
//            val user = firebaseAuth.currentUser
//            if (user != null) {
//                mFirebaseUser = firebaseAuth.currentUser!!
//
//                currentLoggedInUser =
//                    WorkerFinderDatabase.getDatabase(applicationContext, this.lifecycleScope)
//                        .userModelDao
//                        .getUserById(user.uid)
//                        .value!!
//
//            } else {
//                val actionCodeSettings = ActionCodeSettings.newBuilder()
//                    .setAndroidPackageName("com.issen.workerfinder", true, null)
//                    .setHandleCodeInApp(true)
//                    .setUrl("https://workerfinder.page.link")
//                    .build();
//
//                val providers = arrayListOf(
//                    EmailBuilder().enableEmailLinkSignIn()
//                        .setActionCodeSettings(actionCodeSettings).build(),
//                    IdpConfig.GoogleBuilder().build()
//                )
//
//                startActivityForResult(
//                    AuthUI.getInstance()
//                        .createSignInIntentBuilder()
//                        .setAvailableProviders(providers)
//                        .build(),
//                    RC_SIGN_IN
//                )
//            }
//        }
//
//        if (AuthUI.canHandleIntent(intent)) {
//            val link = intent?.data?.toString()
//            val providers: List<IdpConfig> = listOf(
//                EmailBuilder().build()
//            )
//            if (link != null) {
//                startActivityForResult(
//                    AuthUI.getInstance()
//                        .createSignInIntentBuilder()
//                        .setEmailLink(link)
//                        .setAvailableProviders(providers)
//                        .build(),
//                    RC_SIGN_IN_EMAIL_LINK
//                )
//            }
//        }
//    }


//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (requestCode == RC_SIGN_IN) {
//            val response = IdpResponse.fromResultIntent(data)
//            if (resultCode == Activity.RESULT_OK) {
//                prepareUser(response)
//            } else {
//                if (response != null)
//                    Toast.makeText(this, "Error occured, please try again", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//
//    private fun prepareUser(response: IdpResponse?) {
//        val user = FirebaseAuth.getInstance().currentUser
//        mFirebaseUser = user!!
//        if (response?.isNewUser!!) {
//            MainScope().launch {
//                WorkerFinderDatabase.getDatabase(applicationContext, this).userModelDao.insert(
//                    UserModel(
//                        0,
//                        user.displayName.toString(),
//                        user.photoUrl.toString(),
//                        user.email.toString(),
//                        user.phoneNumber.toString(),
//                        mFirebaseUser.uid,
//                        false
//                    )
//                )
//                currentLoggedInUser = WorkerFinderDatabase.getDatabase(applicationContext, this).userModelDao.getUserById(mFirebaseUser.uid).value!!
//            }
//        }
//        val sharedPref = this.getPreferences(Context.MODE_PRIVATE) ?: return
//        with(sharedPref.edit()) {
//            putString("userToken", response.idpToken)
//            apply()
//        }
//    }


//
//    //todo read more about tokens and authentication in firebase
//    fun deleteUser() {
//        val currentUser = FirebaseAuth.getInstance().currentUser!!
//
//        val sharedPref = this.getPreferences(Context.MODE_PRIVATE) ?: return
//        val userToken = sharedPref.getString("userToken", null)
//
//        if (userToken != null) {
//            val credential = GoogleAuthProvider.getCredential(userToken, null)
//            FirebaseAuth.getInstance().currentUser!!.reauthenticate(credential).addOnSuccessListener {
//                currentUser.delete()
//                    .addOnFailureListener {
//                        Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
//                    }
//                    .addOnSuccessListener {
//                        Credentials.getClient(this).disableAutoSignIn();
//                    }
//            }
//        } else {
//            Toast.makeText(this, "Incorrect token", Toast.LENGTH_SHORT).show()
//        }
//
//    }


//companion object {
//
//    private const val RC_SIGN_IN = 9000
//    private const val RC_SIGN_IN_EMAIL_LINK = 9001
//}
package com.issen.workerfinder

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = Firebase.auth
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            navigateMain()
        }
    }

    fun login(view: View) {
        auth.signInWithEmailAndPassword(activity_login_email.text.toString(), activity_login_password.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    navigateMain()
                } else {
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun navigateMain() {
        val startMain = Intent(this, MainActivity::class.java)
        startActivityForResult(startMain, 0)
    }

    fun navigateRegister(view: View) {
        val startRegister = Intent(this, RegisterActivity::class.java)
        startActivityForResult(startRegister, 1)
    }

    fun navigateRemindPassword(view: View) {
        val startRemind = Intent(this, RemindPasswordActivity::class.java)
        startActivityForResult(startRemind, 2)
    }
}
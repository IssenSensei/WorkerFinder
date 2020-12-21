package com.issen.workerfinder.auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.issen.workerfinder.R
import kotlinx.android.synthetic.main.activity_remind_password.*


class RemindPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_remind_password)
    }


    fun remindPassword(view: View) {
        val auth = FirebaseAuth.getInstance()

        auth.sendPasswordResetEmail(activity_remind_password_email.text.toString())
            .addOnSuccessListener {
                Toast.makeText(this, "Wiadomość wyłana pomyślnie, sprawdź skrzynkę pocztową!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Wystąpił błąd, spróbuj ponownie", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }
}
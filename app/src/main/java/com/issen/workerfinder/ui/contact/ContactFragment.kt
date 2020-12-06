package com.issen.workerfinder.ui.contact

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.issen.workerfinder.R
import com.issen.workerfinder.TaskApplication.Companion.currentLoggedInUser
import kotlinx.android.synthetic.main.fragment_contact.*
import kotlinx.android.synthetic.main.fragment_contact.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage


class ContactFragment : Fragment() {

    private var contact = arrayListOf<String>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_contact, container, false)

        contact = arrayListOf("Error", "Idea", "Other")
        val contactSpinnerAdapter = ArrayAdapter<String>(requireContext(), R.layout.item_contact_dropdown, contact)
        root.contact_spinner.adapter = contactSpinnerAdapter
        root.contact_spinner.setSelection(0)

        root.contact_send_button.setOnClickListener {
            sendMail()
        }

        return root
    }

    //todo change this before app publication
    private fun sendMail() {
        val username = "workerfinderauto@gmail.com"
        val password = "PublicPass1"

            val props = Properties()
            props["mail.smtp.auth"] = "true"
            props["mail.smtp.starttls.enable"] = "true"
            props["mail.smtp.host"] = "smtp.gmail.com"
            props["mail.smtp.port"] = "587"
        val session = Session.getInstance(props,
            object : Authenticator() {
                override fun getPasswordAuthentication(): PasswordAuthentication {
                    return PasswordAuthentication(username, password)
                }
            })
            try {
                val message: Message = MimeMessage(session)
                message.setFrom(InternetAddress(currentLoggedInUser!!.email))
                message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse("IssenSensei@gmail.com")
                )
                message.subject = "WorkerFinder " + contact_spinner.selectedItem
                message.setText(contact_item_content.text.toString())
                lifecycleScope.launch (Dispatchers.IO) {
                    Transport.send(message)
                }
                Toast.makeText(requireContext(), "Email sent seccessfully!", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error sending mail, please try again!", Toast.LENGTH_SHORT).show()
            }

        }


}
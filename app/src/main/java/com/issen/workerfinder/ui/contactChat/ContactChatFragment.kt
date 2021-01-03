package com.issen.workerfinder.ui.contactChat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.issen.workerfinder.R
import com.issen.workerfinder.ui.misc.OnDrawerRequestListener

class ContactChatFragment : Fragment() {

    private lateinit var onDrawerRequestListener: OnDrawerRequestListener
    private val contactChatViewModel: ContactChatViewModel by viewModels {
        ContactChatViewModelFactory()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_contact_chat, container, false)
        return root
    }

}
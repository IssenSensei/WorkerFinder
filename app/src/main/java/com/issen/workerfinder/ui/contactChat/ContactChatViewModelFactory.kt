package com.issen.workerfinder.ui.contactChat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ContactChatViewModelFactory() :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ContactChatViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ContactChatViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
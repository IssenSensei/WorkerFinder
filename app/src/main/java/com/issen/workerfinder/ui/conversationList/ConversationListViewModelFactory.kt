package com.issen.workerfinder.ui.conversationList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.issen.workerfinder.database.repositories.ConversationRepository

class ConversationListViewModelFactory(
    private val conversationRepository: ConversationRepository
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ConversationListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ConversationListViewModel(conversationRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
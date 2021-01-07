package com.issen.workerfinder.ui.conversation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.issen.workerfinder.database.models.UserData
import com.issen.workerfinder.database.repositories.ConversationRepository
import com.issen.workerfinder.database.repositories.MessageRepository

class ConversationViewModelFactory(
    private val messageRepository: MessageRepository,
    private val conversationRepository: ConversationRepository,
    private val conversationId: Int,
    private val secondUser: UserData
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ConversationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ConversationViewModel(messageRepository, conversationRepository, conversationId, secondUser) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
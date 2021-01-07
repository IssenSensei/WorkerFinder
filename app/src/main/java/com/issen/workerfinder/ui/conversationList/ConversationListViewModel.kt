package com.issen.workerfinder.ui.conversationList

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.issen.workerfinder.WorkerFinderApplication.Companion.currentLoggedInUserFull
import com.issen.workerfinder.database.models.ConversationsFull
import com.issen.workerfinder.database.repositories.ConversationRepository

class ConversationListViewModel(private val conversationRepository: ConversationRepository) : ViewModel() {

    val conversationList: LiveData<List<ConversationsFull>> = conversationRepository.getUserConversations(currentLoggedInUserFull!!.userData.userId)
}
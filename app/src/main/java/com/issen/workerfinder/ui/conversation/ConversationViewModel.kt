package com.issen.workerfinder.ui.conversation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.issen.workerfinder.WorkerFinderApplication.Companion.currentLoggedInUserFull
import com.issen.workerfinder.database.models.Conversations
import com.issen.workerfinder.database.models.Messages
import com.issen.workerfinder.database.models.MessagesFull
import com.issen.workerfinder.database.models.UserData
import com.issen.workerfinder.database.repositories.ConversationRepository
import com.issen.workerfinder.database.repositories.MessageRepository
import kotlinx.coroutines.launch
import java.util.*

class ConversationViewModel(
    private val messageRepository: MessageRepository,
    private val conversationRepository: ConversationRepository,
    var conversationId: Int,
    private val secondUser: UserData
) : ViewModel() {

    var source: LiveData<List<MessagesFull>> = messageRepository.getConversationMessages(conversationId)
    val messageList: MediatorLiveData<List<MessagesFull>> = MediatorLiveData()

    init {
        if (conversationId == 0) {
            viewModelScope.launch {
                val x = conversationRepository.findConversation(secondUser.userId, currentLoggedInUserFull!!.userData.userId)
                conversationId = x?.conversation?.conversationId ?: 0
                reQuery(conversationId)
            }
        }
        messageList.addSource(source) {
            messageList.setValue(
                it
            )
        }
    }

    fun reQuery(conversationId: Int) {
        messageList.removeSource(source)
        source = messageRepository.getConversationMessages(conversationId)
        messageList.addSource(source) {
            messageList.setValue(
                it
            )
        }
    }

    fun sendMessage(message: String) {
        if (message != "") {
            viewModelScope.launch {
                if (conversationId == 0) {
                    val x = conversationRepository.findConversation(secondUser.userId, currentLoggedInUserFull!!.userData.userId)
                    conversationId = x?.conversation?.conversationId
                        ?: conversationRepository.createConversation(
                            Conversations(
                                0,
                                currentLoggedInUserFull!!.userData.userId,
                                secondUser.userId
                            )
                        ).toInt()
                    reQuery(conversationId)
                }
                messageRepository.sendMessage(
                    Messages(
                        0,
                        conversationId,
                        currentLoggedInUserFull!!.userData.userId,
                        message,
                        Date().toString()
                    )
                )
            }
        }
    }
}
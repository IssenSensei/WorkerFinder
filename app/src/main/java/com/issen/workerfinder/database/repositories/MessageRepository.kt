package com.issen.workerfinder.database.repositories

import androidx.lifecycle.LiveData
import com.issen.workerfinder.database.dao.MessageDao
import com.issen.workerfinder.database.models.Messages
import com.issen.workerfinder.database.models.MessagesFull

class MessageRepository(private val messageDao: MessageDao){
    fun getConversationMessages(conversationId: Int): LiveData<List<MessagesFull>> = messageDao.getConversationMessages(conversationId)
    suspend fun sendMessage(messages: Messages) = messageDao.sendMessage(messages)

}
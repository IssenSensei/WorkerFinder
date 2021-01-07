package com.issen.workerfinder.database.repositories

import androidx.lifecycle.LiveData
import com.issen.workerfinder.database.dao.ConversationDao
import com.issen.workerfinder.database.models.Conversations
import com.issen.workerfinder.database.models.ConversationsFull

class ConversationRepository(private val conversationDao: ConversationDao){
    fun getUserConversations(userId: String): LiveData<List<ConversationsFull>> = conversationDao.getUserConversations(userId)
    suspend fun createConversation(conversations: Conversations): Long = conversationDao.createConversation(conversations)
    suspend fun findConversation(userId: String, userId2: String): ConversationsFull? = conversationDao.findConversation(userId, userId2)

}
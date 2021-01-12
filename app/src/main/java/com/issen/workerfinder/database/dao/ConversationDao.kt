package com.issen.workerfinder.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.issen.workerfinder.database.models.Conversations
import com.issen.workerfinder.database.models.ConversationsFull

@Dao
interface ConversationDao {

    @Insert
    suspend fun insert(mutableListOf: MutableList<Conversations>)

    @Insert
    suspend fun createConversation(conversations: Conversations): Long

    @Query("DELETE FROM conversation_table")
    suspend fun deleteAll()

    @Transaction
    @Query("SELECT DISTINCT * FROM conversation_table WHERE firstUserId = :userId OR secondUserId = :userId ")
    fun getUserConversations(userId: String): LiveData<List<ConversationsFull>>

    @Transaction
    @Query("SELECT * FROM conversation_table WHERE (firstUserId = :userId AND secondUserId = :userId2) OR (firstUserId = :userId2 AND secondUserId = :userId)")
    suspend fun findConversation(userId: String, userId2: String): ConversationsFull?
}
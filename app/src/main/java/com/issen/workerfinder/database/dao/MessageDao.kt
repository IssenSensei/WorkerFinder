package com.issen.workerfinder.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.issen.workerfinder.database.models.Messages
import com.issen.workerfinder.database.models.MessagesFull

@Dao
interface MessageDao {

    @Insert
    suspend fun insert(mutableListOf: MutableList<Messages>)

    @Insert
    suspend fun sendMessage(messages: Messages)

    @Query("DELETE FROM message_table")
    suspend fun deleteAll()

    @Transaction
    @Query("SELECT * FROM message_table WHERE conversationId = :conversationId")
    fun getConversationMessages(conversationId: Int): LiveData<List<MessagesFull>>
}
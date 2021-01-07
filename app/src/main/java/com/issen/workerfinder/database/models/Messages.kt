package com.issen.workerfinder.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity(tableName = "message_table")
data class Messages(

    @PrimaryKey(autoGenerate = true)
    var messageId: Int = 0,

    var conversationId: Int = 0,

    var userId: String = "",

    var messageContent: String = "",

    var messageDate: String = Date().toString()


) : Serializable

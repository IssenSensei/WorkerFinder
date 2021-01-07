package com.issen.workerfinder.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "conversation_table")
data class Conversations(

    @PrimaryKey(autoGenerate = true)
    var conversationId: Int = 0,

    var firstUserId: String = "",

    var secondUserId: String = ""

) : Serializable

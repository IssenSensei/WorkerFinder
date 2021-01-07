package com.issen.workerfinder.database.models

import androidx.room.Embedded
import androidx.room.Relation
import java.io.Serializable

data class ConversationsFull(

    @Embedded val conversation: Conversations,

    @Relation(
        parentColumn = "firstUserId",
        entityColumn = "userId"
    )
    val firstUser: UserData,

    @Relation(
        parentColumn = "secondUserId",
        entityColumn = "userId"
    )
    val secondUser: UserData

) : Serializable


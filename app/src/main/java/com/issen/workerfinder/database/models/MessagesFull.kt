package com.issen.workerfinder.database.models

import androidx.room.Embedded
import androidx.room.Relation
import java.io.Serializable

data class MessagesFull(

    @Embedded val message: Messages,

    @Relation(
        parentColumn = "userId",
        entityColumn = "userId"
    )
    val user: UserData

) : Serializable


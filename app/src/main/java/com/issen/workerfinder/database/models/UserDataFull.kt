package com.issen.workerfinder.database.models

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import java.io.Serializable

data class UserDataFull(

    @Embedded val userData: UserData,
    @Relation(
        parentColumn = "userId",
        entityColumn = "categoryId",
        associateBy = Junction(UserCategoryCrossRef::class)
    )
    val categories: List<Categories>,

    @Relation(
        parentColumn = "userId",
        entityColumn = "userId"
    )
    val contacts: List<Contacts>,

    @Relation(
        parentColumn = "userId",
        entityColumn = "commentedUserId"
    )
    val comments: List<Comments>
) : Serializable


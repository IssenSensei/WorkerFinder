package com.issen.workerfinder.database.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.io.Serializable

data class UserDataWithComments(

    @Embedded val userData: UserData,

    @Embedded val comment: Comments

) : Serializable


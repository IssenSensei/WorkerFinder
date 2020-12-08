package com.issen.workerfinder.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "user_table")
data class UserData(

    @PrimaryKey(autoGenerate = true)
    var userId: Int = 0,

    var userName: String = "",

    var photo: String = "",

    var email: String = "",

    var phone: String = "",

    var firebaseKey: String = "",

    var description: String = "",

    var isAccountPublic: Boolean = false,

    var isOpenForWork: Boolean = false


) : Serializable


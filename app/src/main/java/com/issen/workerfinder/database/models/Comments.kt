package com.issen.workerfinder.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "comment_table")
data class Comments(

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    var commentedUserId: String = "",

    var commentatorUserId: String = "",

    var rating: Float = 0f,

    var comment: String = "",

    var commentByWorker: Boolean = false

) : Serializable

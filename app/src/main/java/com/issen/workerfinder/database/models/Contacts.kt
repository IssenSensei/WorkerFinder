package com.issen.workerfinder.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "contact_table")
data class Contacts(

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    var userId: Int = 0,

    var contactId: Int = 0

) : Serializable

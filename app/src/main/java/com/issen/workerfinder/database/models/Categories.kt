package com.issen.workerfinder.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "category_table")
data class Categories(

    @PrimaryKey(autoGenerate = true)
    var categoryId: Int = 0,

    var category: String = ""

) : Serializable

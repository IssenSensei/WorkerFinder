package com.issen.workerfinder.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "day_table")
data class TaskModelRepeatDays(

    @PrimaryKey(autoGenerate = true)
    var dayId: Int = 0,

    var taskId: Int = 0,

    @ColumnInfo(name = "repeat_day")
    var repeatDay: String = ""

) : Serializable


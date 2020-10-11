package com.example.workerfinder.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.example.workerfinder.enums.CompletionTypes
import com.example.workerfinder.enums.CyclicTypes
import com.example.workerfinder.enums.PriorityTypes
import java.io.Serializable
import java.util.*

@Entity(tableName = "task_table")
data class TaskModel(

    @ColumnInfo(name = "task_title")
    var taskTitle: String = "",

    @ColumnInfo(name = "task_description")
    var taskDescription: String = "",

    @ColumnInfo(name = "task_user")
    var user: String = "",

    @ColumnInfo(name = "task_worker")
    var worker: String = "",

    @ColumnInfo(name = "task_category")
    var category: String = "",

    @ColumnInfo(name = "task_next_completion_date")
    var nextCompletionDate: String = "",

    @ColumnInfo(name = "task_cyclic_type")
    var cyclic: String = CyclicTypes.NONE.toString(),

    @ColumnInfo(name = "task_repeat_days")
    @Ignore
    var repeatDays: MutableList<String> = mutableListOf(),

    @ColumnInfo(name = "task_repeat_until_date")
    var repeatUntil: Date = Date(),

    @ColumnInfo(name = "task_photos")
    @Ignore
    var photos: MutableList<String> = mutableListOf(),

    @ColumnInfo(name = "task_priority_type")
    var priority: String = PriorityTypes.NORMAL.toString(),

    @ColumnInfo(name = "task_completion_type")
    var completed: String = CompletionTypes.ONGOING.toString(),

    @ColumnInfo(name = "task_completion_date")
    var completionDate: String = "",

    @PrimaryKey
    var firebaseKey: String = ""
) : Serializable


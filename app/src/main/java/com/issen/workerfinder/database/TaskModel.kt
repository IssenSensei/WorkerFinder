package com.issen.workerfinder.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.issen.workerfinder.enums.CompletionTypes
import com.issen.workerfinder.enums.CyclicTypes
import com.issen.workerfinder.enums.PriorityTypes
import java.io.Serializable
import java.util.*

@Entity(tableName = "task_table")
data class TaskModel(

    @PrimaryKey(autoGenerate = true)
    var taskId: Int = 0,

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

    @ColumnInfo(name = "task_repeat_until_date")
    var repeatUntil: Date = Date(),

    @ColumnInfo(name = "task_priority_type")
    var priority: String = PriorityTypes.NORMAL.toString(),

    @ColumnInfo(name = "task_completion_type")
    var completed: String = CompletionTypes.ONGOING.toString(),

    @ColumnInfo(name = "task_completion_date")
    var completionDate: String = ""


) : Serializable


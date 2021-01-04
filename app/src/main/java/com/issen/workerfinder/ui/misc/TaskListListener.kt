package com.issen.workerfinder.ui.misc

import com.issen.workerfinder.database.models.TaskModelFull

interface TaskListListener {
    fun onTaskComplete(taskFull: TaskModelFull)
    fun onTaskSelected(taskFull: TaskModelFull)
}
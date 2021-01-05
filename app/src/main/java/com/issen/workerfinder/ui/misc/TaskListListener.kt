package com.issen.workerfinder.ui.misc

import android.view.View
import com.issen.workerfinder.database.models.TaskModelFull

interface TaskListListener {
    fun onTaskComplete(taskFull: TaskModelFull)
    fun onTaskSelected(taskFull: TaskModelFull)
    fun onPopupClicked(view: View, taskFull: TaskModelFull)
}
package com.issen.workerfinder.database.repositories

import com.issen.workerfinder.database.dao.TaskRepeatDayDao
import com.issen.workerfinder.database.models.TaskModelRepeatDays

class TaskRepeatDayRepository(private val taskRepeatDayDao: TaskRepeatDayDao){
    suspend fun insert(repeatDays: MutableList<TaskModelRepeatDays>) {
        taskRepeatDayDao.insert(repeatDays)
    }

}
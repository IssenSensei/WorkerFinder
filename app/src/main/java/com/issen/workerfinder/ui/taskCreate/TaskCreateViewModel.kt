package com.issen.workerfinder.ui.taskCreate

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.issen.workerfinder.database.*
import com.issen.workerfinder.enums.PriorityTypes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskCreateViewModel(application: Application) : AndroidViewModel(application) {

    private val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

    private val repository: TaskModelRepository
    val repeatDays = mutableListOf<TaskModelRepeatDays>()
    val photos = mutableListOf<TaskModelPhotos>()

    init {
        val database = WorkerFinderDatabase.getDatabase(application, viewModelScope)
        val taskModelDao = database.taskModelDao
        val taskPhotosDao = database.taskPhotosDao
        val taskRepeatDaysDao = database.taskRepeatDaysDao
        val userModelDao = database.userModelDao
        repository = TaskModelRepository(taskModelDao, taskPhotosDao, taskRepeatDaysDao, userModelDao)
    }

//    fun insert(taskModel: TaskModel, photos: TaskModelPhotos, repeatDays: TaskModelRepeatDays) {
//        viewModelScope.launch(Dispatchers.IO) {
//            repository.insert(taskModel, photos, repeatDays)
//        }
//    }

    fun insert(taskModel: TaskModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(taskModel, photos, repeatDays)
        }
    }

    fun saveDate(date: String) {
        repeatDays.add(TaskModelRepeatDays(0, 0, date))
    }

    fun clearDates() {
        repeatDays.clear()
    }

    fun savePhoto(photo: String) {
        photos.add(TaskModelPhotos(0, 0, photo))
    }

    fun generateMockupModel() = TaskModel(
        0,
        "title" + randomString(10),
        "description" + randomString(20),
        worker = "worker" + randomString(20),
        category = "category" + randomString(20),
        completionDate = "completionDate" + randomString(20),
        priority = PriorityTypes.NORMAL.toString()
    )

    private fun randomString(length: Int): String {
        return (1..length)
            .map { i -> kotlin.random.Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("");
    }
}
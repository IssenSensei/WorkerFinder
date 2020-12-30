package com.issen.workerfinder.ui.taskCreate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.issen.workerfinder.database.models.TaskModel
import com.issen.workerfinder.database.models.TaskModelPhotos
import com.issen.workerfinder.database.models.TaskModelRepeatDays
import com.issen.workerfinder.database.repositories.TaskPhotoRepository
import com.issen.workerfinder.database.repositories.TaskRepeatDayRepository
import com.issen.workerfinder.database.repositories.TaskRepository
import com.issen.workerfinder.enums.PriorityTypes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskCreateViewModel(
    private val taskRepository: TaskRepository,
    private val taskPhotoRepository: TaskPhotoRepository,
    private val taskRepeatDayRepository: TaskRepeatDayRepository
) : ViewModel() {

    private val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

    val repeatDays = mutableListOf<TaskModelRepeatDays>()
    val photos = mutableListOf<TaskModelPhotos>()

//    fun insert(taskModel: TaskModel, photos: TaskModelPhotos, repeatDays: TaskModelRepeatDays) {
//        viewModelScope.launch(Dispatchers.IO) {
//            repository.insert(taskModel, photos, repeatDays)
//        }
//    }

    fun insert(taskModel: TaskModel) {
        viewModelScope.launch(Dispatchers.IO) {
            val id: Long = taskRepository.insert(taskModel)
            photos.forEach {
                it.taskId = id.toInt()
            }
            repeatDays.forEach {
                it.taskId = id.toInt()
            }
            taskPhotoRepository.insert(photos)
            taskRepeatDayRepository.insert(repeatDays)
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
        workerFirebaseKey = "worker" + randomString(20),
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
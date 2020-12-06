package com.issen.workerfinder.ui.userProfile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.issen.workerfinder.database.TaskModelRepository
import com.issen.workerfinder.database.models.UserModel
import com.issen.workerfinder.database.WorkerFinderDatabase
import kotlinx.coroutines.launch

class UserProfileViewModel(application: Application, firebaseKey: String) : AndroidViewModel(application) {


    private val repository: TaskModelRepository

    private val _activeTasks = MutableLiveData<Int>()
    val activeTasks: LiveData<Int>
        get() = _activeTasks

    private val _completedTasks = MutableLiveData<Int>()
    val completedTasks: LiveData<Int>
        get() = _completedTasks

    private val _abandonedTasks = MutableLiveData<Int>()
    val abandonedTasks: LiveData<Int>
        get() = _abandonedTasks

    init {
        val database = WorkerFinderDatabase.getDatabase(application, viewModelScope)
        val taskModelDao = database.taskModelDao
        val taskPhotosDao = database.taskPhotosDao
        val taskRepeatDaysDao = database.taskRepeatDaysDao
        val userModelDao = database.userModelDao
        repository = TaskModelRepository(taskModelDao, taskPhotosDao, taskRepeatDaysDao, userModelDao)
        viewModelScope.launch { _activeTasks.value = repository.getActiveTasks(firebaseKey) }
        viewModelScope.launch { _completedTasks.value = repository.getCompletedTasks(firebaseKey) }
        viewModelScope.launch { _abandonedTasks.value = repository.getAbandonedTasks(firebaseKey) }
    }

    fun getUserByKey(firebaseKey: String): LiveData<UserModel> {
        return repository.getUserById(firebaseKey)
    }

    fun updateUser(userModel: UserModel) {
        repository.updateUser(userModel)
    }
}
package com.issen.workerfinder.ui.userProfile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.issen.workerfinder.database.TaskModelRepository
import com.issen.workerfinder.database.models.UserDataFull
import com.issen.workerfinder.database.WorkerFinderDatabase
import com.issen.workerfinder.database.models.UserData
import com.issen.workerfinder.database.models.UserDataWithComments
import kotlinx.coroutines.launch

class UserProfileViewModel(application: Application, userDataFull: UserDataFull) : AndroidViewModel(application) {


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

    private val _ratingAsWorker = MutableLiveData<Float?>()
    val ratingAsWorker: LiveData<Float?>
        get() = _ratingAsWorker

    private val _ratingAsUser = MutableLiveData<Float?>()
    val ratingAsUser: LiveData<Float?>
        get() = _ratingAsUser

    private val _commentUser = MutableLiveData<List<UserDataWithComments>>()
    val commentUser: LiveData<List<UserDataWithComments>>
        get() = _commentUser

    private val _commentWorker = MutableLiveData<List<UserDataWithComments>>()
    val commentWorker: LiveData<List<UserDataWithComments>>
        get() = _commentWorker

    init {
        val database = WorkerFinderDatabase.getDatabase(application, viewModelScope)
        val taskModelDao = database.taskModelDao
        val taskPhotosDao = database.taskPhotosDao
        val taskRepeatDaysDao = database.taskRepeatDaysDao
        val userModelDao = database.userDataDao
        val commentsDao = database.commentsDao
        repository = TaskModelRepository(taskModelDao, taskPhotosDao, taskRepeatDaysDao, userModelDao, commentsDao)
        viewModelScope.launch { _activeTasks.value = repository.getActiveTasks(userDataFull.userData.firebaseKey) }
        viewModelScope.launch { _completedTasks.value = repository.getCompletedTasks(userDataFull.userData.firebaseKey) }
        viewModelScope.launch { _abandonedTasks.value = repository.getAbandonedTasks(userDataFull.userData.firebaseKey) }
        viewModelScope.launch { _ratingAsWorker.value = repository.getRatingAsWorker(userDataFull.userData.userId) }
        viewModelScope.launch { _ratingAsUser.value = repository.getRatingAsUser(userDataFull.userData.userId) }
        viewModelScope.launch { _commentUser.value = repository.getCommentUser(userDataFull.userData.userId) }
        viewModelScope.launch { _commentWorker.value = repository.getCommentWorker(userDataFull.userData.userId) }
    }

    fun getUserByKey(firebaseKey: String): LiveData<UserDataFull> {
        return repository.getUserById(firebaseKey)
    }

    fun updateUser(userData: UserData) {
        repository.updateUser(userData)
    }

    fun setAccountPublic(firebaseKey: String, isPublic: Boolean) {
        viewModelScope.launch {
            repository.setAccountPublic(firebaseKey, isPublic)
        }
    }
}
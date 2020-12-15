package com.issen.workerfinder.ui.userProfile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.issen.workerfinder.TaskApplication.Companion.currentLoggedInUserFull
import com.issen.workerfinder.database.ContactRepository
import com.issen.workerfinder.database.DashboardNotificationsRepository
import com.issen.workerfinder.database.TaskModelRepository
import com.issen.workerfinder.database.models.UserDataFull
import com.issen.workerfinder.database.WorkerFinderDatabase
import com.issen.workerfinder.database.models.DashboardNotification
import com.issen.workerfinder.database.models.UserData
import com.issen.workerfinder.database.models.UserDataWithComments
import com.issen.workerfinder.enums.DashboardNotificationTypes
import kotlinx.coroutines.launch
import java.util.*

class UserProfileViewModel(application: Application, userDataFull: UserDataFull) : AndroidViewModel(application) {


    private val taskModelRepository: TaskModelRepository
    private val contactRepository: ContactRepository
    private val dashboardNotificationsRepository: DashboardNotificationsRepository

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

    private val _isUserInContactList = MutableLiveData<Boolean>()
    val isUserInContactList: LiveData<Boolean>
        get() = _isUserInContactList

    init {
        val database = WorkerFinderDatabase.getDatabase(application, viewModelScope)
        val taskModelDao = database.taskModelDao
        val taskPhotosDao = database.taskPhotosDao
        val taskRepeatDaysDao = database.taskRepeatDaysDao
        val userModelDao = database.userDataDao
        val commentsDao = database.commentsDao
        val contactsDao = database.contactsDao
        val dashboardNotificationDao = database.dashboardNotificationDao
        taskModelRepository = TaskModelRepository(taskModelDao, taskPhotosDao, taskRepeatDaysDao, userModelDao, commentsDao)
        contactRepository = ContactRepository(contactsDao)
        dashboardNotificationsRepository = DashboardNotificationsRepository(dashboardNotificationDao)
        viewModelScope.launch { _activeTasks.value = taskModelRepository.getActiveTasks(userDataFull.userData.userId) }
        viewModelScope.launch { _completedTasks.value = taskModelRepository.getCompletedTasks(userDataFull.userData.userId) }
        viewModelScope.launch { _abandonedTasks.value = taskModelRepository.getAbandonedTasks(userDataFull.userData.userId) }
        viewModelScope.launch { _ratingAsWorker.value = taskModelRepository.getRatingAsWorker(userDataFull.userData.userId) }
        viewModelScope.launch { _ratingAsUser.value = taskModelRepository.getRatingAsUser(userDataFull.userData.userId) }
        viewModelScope.launch { _commentUser.value = taskModelRepository.getCommentUser(userDataFull.userData.userId) }
        viewModelScope.launch { _commentWorker.value = taskModelRepository.getCommentWorker(userDataFull.userData.userId) }
        viewModelScope.launch {
            _isUserInContactList.value = contactRepository.checkIfIsOnContactList(
                userDataFull.userData.userId,
                currentLoggedInUserFull!!.userData.userId
            )
        }
    }

    fun getUserByKey(firebaseKey: String): LiveData<UserDataFull> {
        return taskModelRepository.getUserById(firebaseKey)
    }

    fun updateUser(userData: UserData) {
        taskModelRepository.updateUser(userData)
    }

    fun setAccountPublic(firebaseKey: String, isPublic: Boolean) {
        viewModelScope.launch {
            taskModelRepository.setAccountPublic(firebaseKey, isPublic)
        }
    }

    fun removeContact(userDataFull: UserDataFull) {
        viewModelScope.launch {
            contactRepository.removeContact(userDataFull.userData.userId, currentLoggedInUserFull!!.userData.userId)
            dashboardNotificationsRepository.notify(
                DashboardNotification(
                    0,
                    Date().toString(),
                    userDataFull.userData.userId,
                    currentLoggedInUserFull!!.userData.userId,
                    DashboardNotificationTypes.CONTACTREMOVED.toString(),
                    0,
                    false
                )
            )
        }
    }

    fun addContact(userDataFull: UserDataFull) {
        viewModelScope.launch {
            dashboardNotificationsRepository.notify(
                DashboardNotification(
                    0,
                    Date().toString(),
                    userDataFull.userData.userId,
                    currentLoggedInUserFull!!.userData.userId,
                    DashboardNotificationTypes.CONTACTINVITED.toString(),
                    0,
                    false
                )
            )
        }
    }
}
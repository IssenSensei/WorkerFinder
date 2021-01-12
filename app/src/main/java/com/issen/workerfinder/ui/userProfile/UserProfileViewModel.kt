package com.issen.workerfinder.ui.userProfile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.issen.workerfinder.WorkerFinderApplication.Companion.currentLoggedInUserFull
import com.issen.workerfinder.database.models.DashboardNotification
import com.issen.workerfinder.database.models.UserData
import com.issen.workerfinder.database.models.UserDataFull
import com.issen.workerfinder.database.models.UserDataWithComments
import com.issen.workerfinder.database.repositories.*
import com.issen.workerfinder.enums.DashboardNotificationTypes
import kotlinx.coroutines.launch
import java.util.*

class UserProfileViewModel(
    private val taskRepository: TaskRepository,
    private val commentRepository: CommentRepository,
    private val userRepository: UserRepository,
    private val contactRepository: ContactRepository,
    private val dashboardNotificationRepository: DashboardNotificationRepository,
    userDataFull: UserDataFull
) : ViewModel() {

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

    private val _isAccountPublic = MutableLiveData<Boolean>()
    val isAccountPublic: LiveData<Boolean>
        get() = _isAccountPublic

    init {
        viewModelScope.launch { _activeTasks.value = taskRepository.getActiveTasks(userDataFull.userData.userId) }
        viewModelScope.launch { _completedTasks.value = taskRepository.getCompletedTasks(userDataFull.userData.userId) }
        viewModelScope.launch { _abandonedTasks.value = taskRepository.getAbandonedTasks(userDataFull.userData.userId) }
        viewModelScope.launch { _ratingAsWorker.value = commentRepository.getRatingAsWorker(userDataFull.userData.userId) }
        viewModelScope.launch { _ratingAsUser.value = commentRepository.getRatingAsUser(userDataFull.userData.userId) }
        viewModelScope.launch { _commentUser.value = userRepository.getCommentUser(userDataFull.userData.userId) }
        viewModelScope.launch { _commentWorker.value = userRepository.getCommentWorker(userDataFull.userData.userId) }
        viewModelScope.launch {
            _isUserInContactList.value = contactRepository.checkIfIsOnContactList(
                userDataFull.userData.userId,
                currentLoggedInUserFull!!.userData.userId
            )
        }
    }

    fun updateUser(userData: UserData) {
        userRepository.updateUser(userData)
    }

    fun setAccountPublic(firebaseKey: String, isPublic: Boolean) {
        _isAccountPublic.value = isPublic
        viewModelScope.launch {
            userRepository.setAccountPublic(firebaseKey, isPublic)
        }
    }

    fun removeContact(userDataFull: UserDataFull) {
        viewModelScope.launch {
            contactRepository.removeContact(userDataFull.userData.userId, currentLoggedInUserFull!!.userData.userId)
            dashboardNotificationRepository.notify(
                DashboardNotification(
                    0,
                    Date().toString(),
                    userDataFull.userData.userId,
                    currentLoggedInUserFull!!.userData.userId,
                    DashboardNotificationTypes.CONTACTREMOVED.toString(),
                    0,
                    true
                )
            )
            dashboardNotificationRepository.notify(
                DashboardNotification(
                    0,
                    Date().toString(),
                    currentLoggedInUserFull!!.userData.userId,
                    userDataFull.userData.userId,
                    DashboardNotificationTypes.CONTACTREMOVED.toString(),
                    0,
                    true
                )
            )
        }
    }

    fun addContact(userDataFull: UserDataFull) {
        viewModelScope.launch {
            dashboardNotificationRepository.notify(
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
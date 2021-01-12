package com.issen.workerfinder.ui.boardTaskWorkerPicker

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.issen.workerfinder.database.models.UserDataFull
import com.issen.workerfinder.database.repositories.DashboardNotificationRepository
import com.issen.workerfinder.database.repositories.TaskRepository
import com.issen.workerfinder.database.repositories.UserRepository
import kotlinx.coroutines.launch

class BoardTaskWorkerPickerViewModel(
    private val userRepository: UserRepository,
    private val dashboardNotificationRepository: DashboardNotificationRepository,
    private val taskRepository: TaskRepository
) : ViewModel() {

    var taskId: Int = 0
    private var source: LiveData<List<UserDataFull>>
    val mediatorLiveData: MediatorLiveData<List<UserDataFull>> = MediatorLiveData()

    init {
        source = userRepository.getBoardTaskApplications(taskId)
        mediatorLiveData.addSource(source) {
            mediatorLiveData.setValue(
                it
            )
        }
    }

    fun reQuery(name: String) {
        mediatorLiveData.removeSource(source)
        source = userRepository.getBoardTaskApplications(taskId, name)
        mediatorLiveData.addSource(source) {
            mediatorLiveData.setValue(
                it
            )
        }
    }

    fun selectWorker(userDataFull: UserDataFull) {
        viewModelScope.launch {
//            dashboardNotificationRepository.selectBoardWorker(userDataFull.userData.userId, taskId)
            taskRepository.selectBoardWorker(taskId, userDataFull.userData.userId)
        }
    }

    fun setId(taskId: Int) {
        this.taskId = taskId
        reQuery("")
    }
}



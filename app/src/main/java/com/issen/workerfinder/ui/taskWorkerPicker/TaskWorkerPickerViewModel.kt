package com.issen.workerfinder.ui.taskWorkerPicker

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.issen.workerfinder.WorkerFinderApplication.Companion.currentLoggedInUserFull
import com.issen.workerfinder.database.models.UserDataFull
import com.issen.workerfinder.database.repositories.UserRepository

class TaskWorkerPickerViewModel(private val userRepository: UserRepository) : ViewModel() {

    private var source: LiveData<List<UserDataFull>>
    val mediatorLiveData: MediatorLiveData<List<UserDataFull>> = MediatorLiveData()

    init {
        source = userRepository.getUserWorkers(currentLoggedInUserFull!!.userData.userId)
        mediatorLiveData.addSource(source) {
            mediatorLiveData.setValue(
                it
            )
        }
    }

    fun reQuery(name: String) {
        mediatorLiveData.removeSource(source)
        source = userRepository.getUserWorkers(currentLoggedInUserFull!!.userData.userId, name)
        mediatorLiveData.addSource(source) {
            mediatorLiveData.setValue(
                it
            )
        }
    }
}


package com.issen.workerfinder.ui.workerList

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.issen.workerfinder.WorkerFinderApplication.Companion.currentLoggedInUserFull
import com.issen.workerfinder.database.models.UserDataFull
import com.issen.workerfinder.database.repositories.UserRepository

class WorkerListViewModel(private val userRepository: UserRepository) : ViewModel() {

    var workerList: LiveData<List<UserDataFull>> = userRepository.getUserWorkers(currentLoggedInUserFull!!.userData.userId)

}
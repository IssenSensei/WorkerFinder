package com.issen.workerfinder.ui.contactAdd

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.issen.workerfinder.WorkerFinderApplication.Companion.currentLoggedInUserFull
import com.issen.workerfinder.database.models.UserDataFull
import com.issen.workerfinder.database.repositories.UserRepository

class ContactAddViewModel(private val userRepository: UserRepository) : ViewModel() {

    val usersList: LiveData<List<UserDataFull>> = userRepository.getUsersList(currentLoggedInUserFull!!.userData.userId)

}
package com.issen.workerfinder.ui.contactList

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.issen.workerfinder.WorkerFinderApplication.Companion.currentLoggedInUserFull
import com.issen.workerfinder.database.models.UserDataFull
import com.issen.workerfinder.database.repositories.UserRepository

class ContactListViewModel(private val userRepository: UserRepository) : ViewModel() {

    var contactList: LiveData<List<UserDataFull>> = userRepository.getUserContacts(currentLoggedInUserFull!!.userData.userId)

}
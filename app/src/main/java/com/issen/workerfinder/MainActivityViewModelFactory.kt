package com.issen.workerfinder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.issen.workerfinder.database.WorkerFinderDatabase
import com.issen.workerfinder.database.repositories.UserRepository

class MainActivityViewModelFactory(
    private val userRepository: UserRepository,
    private val workerFinderDatabase: WorkerFinderDatabase
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainActivityViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainActivityViewModel(userRepository, workerFinderDatabase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
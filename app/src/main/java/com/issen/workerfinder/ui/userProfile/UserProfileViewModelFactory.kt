package com.issen.workerfinder.ui.userProfile

import android.app.Application
import androidx.lifecycle.ViewModel

import androidx.lifecycle.ViewModelProvider
import com.issen.workerfinder.database.models.UserDataFull


class UserProfileViewModelFactory(application: Application, param: UserDataFull) : ViewModelProvider.Factory {
    private val mApplication: Application = application
    private val mParam: UserDataFull = param

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return UserProfileViewModel(mApplication, mParam) as T
    }
}
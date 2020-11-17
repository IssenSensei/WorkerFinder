package com.issen.workerfinder.ui.userProfile

import android.app.Application
import androidx.lifecycle.ViewModel

import androidx.lifecycle.ViewModelProvider


class UserProfileViewModelFactory(application: Application, param: String) : ViewModelProvider.Factory {
    private val mApplication: Application = application
    private val mParam: String = param

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return UserProfileViewModel(mApplication, mParam) as T
    }
}
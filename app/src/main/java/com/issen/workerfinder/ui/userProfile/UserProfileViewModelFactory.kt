package com.issen.workerfinder.ui.userProfile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.issen.workerfinder.database.models.UserDataFull
import com.issen.workerfinder.database.repositories.*

class UserProfileViewModelFactory(
    private val taskRepository: TaskRepository,
    private val commentRepository: CommentRepository,
    private val userRepository: UserRepository,
    private val contactRepository: ContactRepository,
    private val dashboardNotificationRepository: DashboardNotificationRepository,
    private val param: UserDataFull
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserProfileViewModel(
                taskRepository, commentRepository, userRepository, contactRepository, dashboardNotificationRepository, param
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

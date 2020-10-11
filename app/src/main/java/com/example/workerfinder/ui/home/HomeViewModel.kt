package com.example.workerfinder.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.workerfinder.database.TaskModel

class HomeViewModel : ViewModel() {

    private val _taskList = MutableLiveData<MutableList<TaskModel>>().apply {
        value = mutableListOf(TaskModel( taskTitle = "task1" ,firebaseKey = "qa"), TaskModel(taskTitle = "task2", firebaseKey = "aaa"))
    }
    val text: LiveData<MutableList<TaskModel>> = _taskList
}
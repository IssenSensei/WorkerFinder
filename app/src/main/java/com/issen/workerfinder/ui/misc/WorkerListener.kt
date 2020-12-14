package com.issen.workerfinder.ui.misc

import com.issen.workerfinder.database.models.UserDataFull

interface WorkerListener {
    fun onWorkerClicked(userDataFull: UserDataFull)
}
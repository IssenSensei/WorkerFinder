package com.issen.workerfinder.ui.misc

import com.issen.workerfinder.database.models.FullUserData

interface WorkerListener {
    fun onWorkerClicked(fullUserData: FullUserData)
}
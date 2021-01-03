package com.issen.workerfinder.ui.misc

import com.issen.workerfinder.database.models.UserDataFull

interface ContactListener {
    fun onContactClicked(userDataFull: UserDataFull)
}
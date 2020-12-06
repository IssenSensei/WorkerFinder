package com.issen.workerfinder.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.issen.workerfinder.database.models.Contacts
import com.issen.workerfinder.database.models.FullUserData

@Dao
interface ContactsDao {

    @Insert
    suspend fun insert(mutableListOf: MutableList<Contacts>)

}
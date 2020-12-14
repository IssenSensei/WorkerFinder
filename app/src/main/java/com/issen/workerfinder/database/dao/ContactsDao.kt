package com.issen.workerfinder.database.dao

import androidx.room.Dao
import androidx.room.Insert
import com.issen.workerfinder.database.models.Contacts

@Dao
interface ContactsDao {

    @Insert
    suspend fun insert(mutableListOf: MutableList<Contacts>)

}
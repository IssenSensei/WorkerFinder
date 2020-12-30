package com.issen.workerfinder.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.issen.workerfinder.database.models.Contacts

@Dao
interface ContactDao {

    @Insert
    suspend fun insert(mutableListOf: MutableList<Contacts>)

    @Query(
        "DELETE FROM contact_table WHERE (contactId = :userId and userId = :currentLoggedInUserId) or (contactId = " +
                ":currentLoggedInUserId and userId = :userId)"
    )
    suspend fun remove(userId: String, currentLoggedInUserId: String)

    @Query(
        "SELECT EXISTS (SELECT * FROM contact_table WHERE (contactId = :userId and userId = :currentLoggedInUserId) or (contactId = " +
                ":currentLoggedInUserId and userId = :userId))"
    )
    suspend fun checkIfIsOnContactList(userId: String, currentLoggedInUserId: String): Boolean

    @Insert
    suspend fun addContact(contacts: Contacts)

}
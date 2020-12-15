package com.issen.workerfinder.database

import com.issen.workerfinder.database.dao.*
import com.issen.workerfinder.database.models.*

class ContactRepository(private val contactsDao: ContactsDao) {

    suspend fun addContact(contacts: Contacts) =
        contactsDao.addContact(contacts)

    suspend fun removeContact(userId: String, currentLoggedInUserId: String) =
        contactsDao.remove(userId, currentLoggedInUserId)

    suspend fun checkIfIsOnContactList(userId: String, currentLoggedInUserId: String) =
        contactsDao.checkIfIsOnContactList(userId, currentLoggedInUserId)

}
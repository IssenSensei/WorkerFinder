package com.issen.workerfinder.database.repositories

import com.issen.workerfinder.database.dao.ContactDao
import com.issen.workerfinder.database.models.Contacts

class ContactRepository(private val contactDao: ContactDao) {

    suspend fun addContact(contacts: Contacts) = contactDao.addContact(contacts)
    suspend fun removeContact(userId: String, currentLoggedInUserId: String) = contactDao.remove(userId, currentLoggedInUserId)
    suspend fun checkIfIsOnContactList(userId: String, currentLoggedInUserId: String) =
        contactDao.checkIfIsOnContactList(userId, currentLoggedInUserId)

}
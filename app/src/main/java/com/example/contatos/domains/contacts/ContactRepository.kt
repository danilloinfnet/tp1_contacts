package com.example.contatos.domains.contacts

import androidx.annotation.WorkerThread
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContactRepository @Inject constructor(private val contactDao: ContactDAO) {
    
    fun getContacts() = contactDao.getAll()
    
    fun getContact(contactId: String) = contactDao.getContact(contactId)

    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun createContact(contact: Contact) = contactDao.insertContact(contact)
    
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun updateContact(contact: Contact) = contactDao.updateContact(contact)
    
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(contact: Contact) = contactDao.deleteContact(contact)

    companion object {
        @Volatile private var instance: ContactRepository? = null
        
        fun getInstance(contactDao: ContactDAO) =
            instance ?: synchronized(this) {
                instance ?: ContactRepository(contactDao).also { instance = it }
            }
    }
}
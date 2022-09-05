package com.example.contatos.domains.contacts

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDAO {
    @Query("SELECT * FROM contact")
    fun getAll(): Flow<List<Contact>>
    
    @Query("SELECT * FROM contact where id = :contactId")
    fun getContact(contactId: String): Flow<Contact >
    
    @Insert
    suspend fun insertContact(contact: Contact)

    @Update(entity = Contact::class)
    suspend fun updateContact(vararg contact: Contact)
    
    @Delete
    suspend fun deleteContact(contact: Contact)
}
package com.example.contatos.ui.managecontact

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contatos.domains.contacts.Contact
import com.example.contatos.domains.contacts.ContactRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManageContactViewModel  @Inject constructor (
    private val contactRepository: ContactRepository,
        ) : ViewModel() {

    fun getContact(contactId: String): Flow<Contact> {
        return contactRepository.getContact(contactId)
    }

    fun createContact(contact: Contact) = viewModelScope.launch {
        contactRepository.createContact(contact)
    }

    fun updateContact(contact: Contact) = viewModelScope.launch { 
        contactRepository.updateContact(contact)
    }
}
package com.example.contatos.ui.contactdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.contatos.domains.contacts.Contact
import com.example.contatos.domains.contacts.ContactRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactViewModel @Inject constructor(
    private val contactRepository: ContactRepository,
) : ViewModel() {
    fun getContact(contactId: String): Flow<Contact> {
        return contactRepository.getContact(contactId)
    }

    fun deleteContact(contact: Contact) = viewModelScope.launch { 
        contactRepository.delete(contact)
    }
}

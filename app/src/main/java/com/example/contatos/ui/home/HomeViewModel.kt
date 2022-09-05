package com.example.contatos.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.contatos.domains.contacts.Contact
import com.example.contatos.domains.contacts.ContactRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject internal constructor(
    private val contactRepository: ContactRepository,
): ViewModel() {
    
    val contactsList: LiveData<List<Contact>> = contactRepository.getContacts().asLiveData()
    
}

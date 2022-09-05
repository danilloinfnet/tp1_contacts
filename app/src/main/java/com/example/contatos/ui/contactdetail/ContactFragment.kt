package com.example.contatos.ui.contactdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.contatos.PhotosStorage
import com.example.contatos.R
import com.example.contatos.domains.contacts.Contact
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.File

@AndroidEntryPoint
class ContactFragment : Fragment() {

    private lateinit var _view: View
    private val args: ContactFragmentArgs by navArgs()
    private var searchJob: Job? = null

    companion object {
        fun newInstance() = ContactFragment()
    }

    private lateinit var viewModel: ContactViewModel
    private lateinit var contact: Contact

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_contact, container, false)
    }

    private fun search(contactId: String) {
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            viewModel.getContact(contactId).collect {
                if (it != null) {
                    contact = it
                    _view.findViewById<TextView>(R.id.tvContactDetailUsername).text = contact.name
                    _view.findViewById<TextView>(R.id.tvContactDetailPhoneNumber).text = contact.phone
                    if (contact.imageUrl != "") {
                        val drawable = PhotosStorage.loadPhotoFromInternalStorage(context, contact.imageUrl)
                        _view.findViewById<ImageView>(R.id.imgContactDetail).setImageDrawable(drawable)
                    }
                    _view.findViewById<ImageButton>(R.id.imgButtonContactFragmentDelete).setOnClickListener {
                        viewModel.deleteContact(contact)
                        if (contact.imageUrl != "") {
                            PhotosStorage.deletePhotoFromInternalStorage(context, contact.imageUrl)
                        }
                        findNavController().navigateUp()
                    }   
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        _view = view
        activity?.let {
            viewModel = ViewModelProvider(requireActivity()).get(ContactViewModel::class.java)    
        }
        
        val contactId = args.contactId
        search(contactId)
        _view.findViewById<ImageButton>(R.id.imgButtonContactFragmentBack).setOnClickListener {
            findNavController().navigateUp()
        }
        _view.findViewById<ImageButton>(R.id.imgButtonContactFragmentEdit).setOnClickListener { 
            val direction = ContactFragmentDirections.actionNavContactFragmentToNavManageContact(
                contactId
            )
            _view.findNavController().navigate(direction)
        }
    }
}
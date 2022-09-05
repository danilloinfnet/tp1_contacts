package com.example.contatos.ui.managecontact

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.contatos.PhotosStorage
import com.example.contatos.R
import com.example.contatos.domains.contacts.Contact
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.FileDescriptor
import java.io.IOException
import java.util.*


@AndroidEntryPoint
class ManageContactFragment : Fragment() {

    companion object {
        fun newInstance() = ManageContactFragment()
    }

    private val args: ManageContactFragmentArgs by navArgs()
    private var searchJob: Job? = null
    private lateinit var contact: Contact
    
    private lateinit var viewModel: ManageContactViewModel
    private lateinit var _view: View
    private var filenameExt: String = ""
    private var editMode: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_manage_contact, container, false)
    }

    private fun setUpListeners() {
        _view.findViewById<ImageView>(R.id.imgContactDetail).setOnClickListener {
            dispatchTakePictureIntent()
        }
        _view.findViewById<Button>(R.id.btnManageContactFragmentSave).setOnClickListener {
            val name = _view.findViewById<EditText>(R.id.txtManageContactName)
            val phone = _view.findViewById<EditText>(R.id.txtManageContactPhone)

            
            if (!editMode) {
                val currentContact = Contact(name = name.text.toString(), phone = phone.text.toString(), imageUrl = filenameExt)
                viewModel.createContact(currentContact)   
            } else {
                val currentContact = Contact(id= contact.id, name = name.text.toString(), phone = phone.text.toString(), imageUrl = filenameExt)
                viewModel.updateContact(currentContact)
            }
            findNavController().navigateUp()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _view = view
        activity?.let {
            viewModel = ViewModelProvider(this).get(ManageContactViewModel::class.java)
        }
        
        val contactId = args.contactId
        if (contactId != null) {
            editMode = true
            fetchContact(contactId)
        }

        setUpListeners()
    }

    private fun fetchContact(contactId: String) {
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            viewModel.getContact(contactId).collect {
                contact = it
                _view.findViewById<TextView>(R.id.txtManageContactFragmentTitle).text = getString(R.string.manage_contact_fragment_title_update)
                _view.findViewById<Button>(R.id.btnManageContactFragmentSave).text = getString(R.string.btn_update)
                _view.findViewById<EditText>(R.id.txtManageContactName).setText(contact.name)
                _view.findViewById<EditText>(R.id.txtManageContactPhone).setText(contact.phone)
                if (contact.imageUrl != "") {
                    filenameExt = contact.imageUrl
                    val drawable = PhotosStorage.loadPhotoFromInternalStorage(context, contact.imageUrl)
                    _view.findViewById<ImageView>(R.id.imgContactDetail).setImageDrawable(drawable)
                }
            }
        }
    }

    val PHOTO_PICKER_REQUEST_CODE = 1
    val Fragment.packageManager get() = activity?.packageManager


    private fun dispatchTakePictureIntent() {
        val intent = Intent(MediaStore.ACTION_PICK_IMAGES)
        startActivityForResult(intent, PHOTO_PICKER_REQUEST_CODE)
    }

    // onActivityResult() handles callbacks from the photo picker.
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            // Handle error
            return
        }
        if (requestCode == PHOTO_PICKER_REQUEST_CODE && resultCode == AppCompatActivity.RESULT_OK) {
            val imgUploaded = _view.findViewById<ImageView>(R.id.imgContactDetail)
            val currentUri = data?.data!!   
            imgUploaded.setImageURI(currentUri)
            val bitmap = uriToBitmap(currentUri)
            if(bitmap != null) {
                filenameExt = PhotosStorage.savePhotoInternalToInternalStorage(context, UUID.randomUUID().toString(), bitmap)   
            }
        }
        
        return
    }

    private fun uriToBitmap(selectedFileUri: Uri): Bitmap? {
        try {
            val parcelFileDescriptor = context?.contentResolver?.openFileDescriptor(selectedFileUri, "r")
            val fileDescriptor: FileDescriptor = parcelFileDescriptor!!.fileDescriptor
            val image = BitmapFactory.decodeFileDescriptor(fileDescriptor)
            parcelFileDescriptor.close()
            return image
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }
}
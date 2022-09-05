package com.example.contatos.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController

import androidx.recyclerview.widget.RecyclerView
import com.example.contatos.PhotosStorage
import com.example.contatos.R
import com.example.contatos.domains.contacts.Contact

class ContactsAdapter(val context: Context?, var contactsList:List<Contact> = listOf()): RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder>() {

    class ContactsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val imgContact: ImageView = itemView.findViewById(R.id.imgCardContact)
        val txtContactName: TextView = itemView.findViewById(R.id.txtCardContactName)
        private val card: CardView = itemView.findViewById(R.id.card)
        
        fun bind(contact: Contact) {
            val contactId = contact.id
            card.setOnClickListener{
                val direction = HomeFragmentDirections.actionNavHomeToNavContactFragment(
                    contactId
                )
                itemView.findNavController().navigate(direction)
            }
        }
    }

    fun changeData(contacts: List<Contact>) {
        contactsList = contacts
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder {
        val card = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.contact_card, parent, false)

        return ContactsViewHolder(card)
    }

    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {
        val contact = contactsList[position]
        holder.bind(contact)
        
        holder.txtContactName.text = contact.name
        if (contact.imageUrl != "null") {
            holder.imgContact.visibility = ImageView.VISIBLE
            val drawable = PhotosStorage.loadPhotoFromInternalStorage(context, contact.imageUrl)
            holder.imgContact.setImageDrawable(drawable)
        }
    }

    override fun getItemCount() = contactsList.size
}


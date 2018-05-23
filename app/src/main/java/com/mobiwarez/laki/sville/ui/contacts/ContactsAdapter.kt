package com.mobiwarez.laki.sville.ui.contacts

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.mobiwarez.data.contacts.db.model.ContactModel
//import com.mobiwarez.laki.seapp.R
import com.mobiwarez.laki.sville.R
import com.mobiwarez.laki.sville.extensions.loadAvatar
import com.mobiwarez.laki.sville.extensions.loadImg
import kotlinx.android.synthetic.main.list_item_contact.view.*

/**
 * Created by Laki on 04/11/2017.
 */
class ContactsAdapter(private val chatStarter: ChatStarter): RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder>() {

    //val starter = chatStarter
    var contacts = arrayListOf<ContactModel>()
    override fun getItemCount(): Int = contacts.size

    override fun onBindViewHolder(holder: ContactsViewHolder?, position: Int) {
        holder?.setItem(contacts[position], chatStarter)

    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ContactsViewHolder {
        val itemView = LayoutInflater.from(parent?.context).inflate(R.layout.list_item_contact, parent, false)
        return ContactsViewHolder(itemView)

    }

    fun addContacts(cons: List<ContactModel>){
        contacts = cons as ArrayList<ContactModel>
        notifyDataSetChanged()
    }

    interface ChatStarter {
        fun startChat(model: ContactModel)
    }

    class ContactsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var model: ContactModel? = null

        val conName = itemView.findViewById<TextView>(R.id.contactname)
        val avatar = itemView.findViewById<ImageView>(R.id.contactAvatar)


        fun setItem(contactModel: ContactModel, chatStarter: ChatStarter) {
            model = contactModel
            conName.text = contactModel.contactName
            avatar.loadAvatar(contactModel.contactAvatar)

            itemView.setOnClickListener{ _ -> chatStarter.startChat(model!!)}
        }

    }
}
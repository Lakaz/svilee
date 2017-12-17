package com.mobiwarez.laki.seapp.ui.contacts

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import com.mobiwarez.laki.seapp.R

import kotlinx.android.synthetic.main.activity_contacts.*

class ContactsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts)
        toolbar.title = "Contacts"
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val manager = supportFragmentManager
        if (manager.findFragmentByTag(ContactsFragment.TAG) == null){
            val fragment = ContactsFragment.Companion.newInstance()
            manager.beginTransaction().add(R.id.contacts_container, fragment, ContactsFragment.TAG).commit()
        }
    }

}

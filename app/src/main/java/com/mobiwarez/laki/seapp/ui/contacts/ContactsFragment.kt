package com.mobiwarez.laki.seapp.ui.contacts

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mobiwarez.data.contacts.db.model.ContactModel
import com.mobiwarez.laki.seapp.R
import com.mobiwarez.laki.seapp.contactsmanager.ContactsManager
import com.mobiwarez.laki.seapp.fragments.RxBaseFragment
import com.mobiwarez.laki.seapp.ui.chat.FireChatActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_contacts.*
import timber.log.Timber


class ContactsFragment : RxBaseFragment(), ContactsContract.View, ContactsAdapter.ChatStarter {



    var adapter: ContactsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = ContactsAdapter(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_contacts, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initializeRecyclerView()

    }


    override fun onResume() {
        super.onResume()
        fetchContacts()
    }

    private fun fetchContacts() {
        val manager = ContactsManager(context)
        val subscription = manager.getContacts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            contacts -> if (contacts.isEmpty()){
                                            showMessage("You have no contacts yet")
                                        }else {
                                            adapter?.addContacts(contacts)
                                        }
                        },
                        {
                            error -> showMessage("Failed to load contacts")
                                        Timber.d(error)
                        }
                )

        subscriptions.add(subscription)
    }

    private fun initializeRecyclerView() {

        if (contacts_recycler.adapter == null) {
            adapter = ContactsAdapter( this)
            contacts_recycler.adapter = adapter
        } else {
            adapter = contacts_recycler.adapter as ContactsAdapter
        }
        val sharedToysLayoutManager = LinearLayoutManager(null)
        contacts_recycler.layoutManager = sharedToysLayoutManager

    }


    override fun showContacts(contactModels: List<ContactModel>) {
        adapter?.addContacts(contactModels)
    }

    override fun showMessage(message: String?) {
        Snackbar.make(fragment_layout, message!!, Snackbar.LENGTH_SHORT).show()
    }

    override fun showError(message: String?) {

    }


    override fun startChat(model: ContactModel) {

        val intent = Intent(activity, FireChatActivity::class.java)
        intent.putExtra("chatRoom", model.chatRoom)
        intent.putExtra("uuid", model.contactUUID)
        intent.putExtra("name", model.contactName)
        startActivity(intent)

    }


    companion object {
        val TAG = ContactsFragment::class.java.simpleName

        fun newInstance(): ContactsFragment {
            return ContactsFragment()
        }
    }
}// Required empty public constructor

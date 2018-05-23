package com.mobiwarez.laki.sville.data.contactsmanager

import android.content.Context
import com.mobiwarez.data.contacts.db.definition.ContactsDatabaseFactory
import com.mobiwarez.data.contacts.db.model.ContactModel
import com.mobiwarez.laki.sville.data.remotebackend.AppEngineBackend
import io.reactivex.Observable

/**
 * Created by Laki on 04/11/2017.
 */
class ContactsManager(val context:Context) {

    private val contactsDao = ContactsDatabaseFactory.getInstance(context).contactDao()

    fun getChatRoom(uuid: String): Observable<String> {
        return Observable.create{
            subscription ->
                try {
                    val contact = contactsDao.findById(uuid)
                    if (contact != null) subscription.onNext(contact.chatRoom) else subscription.onNext("no chat room")
                    subscription.onComplete()
                }catch (e: Exception){
                    subscription.onError(Throwable(e))
                }
        }
    }

    fun addNewContact(contact: ContactModel): Observable<Boolean>{
        return Observable.create{
            subscription ->
                try {
                    contactsDao.insertAll(contact)
                    subscription.onNext(true)
                    subscription.onComplete()
                }catch (e: Exception){
                    subscription.onError(Throwable(e))
                }
        }
    }

    fun getContacts(): Observable<List<ContactModel>> {
        return Observable.create{
            subscription ->
                try {
                    val list = contactsDao.all
                    subscription.onNext(list)
                    subscription.onComplete()
                }catch (e:Exception){
                    subscription.onError(Throwable(e))
                }
        }
    }

    fun requestChat(chatRoom:String, invitedUUID: String, inviterName: String): Observable<String>{
        val myApi = AppEngineBackend.getInstance()
        return Observable.create{
            subscription ->
                try {
                    val response = myApi.inviteToChat(chatRoom, invitedUUID, inviterName).execute()
                    subscription.onNext(response.getResponse())
                    subscription.onComplete()
                }catch (e: Exception){
                    subscription.onError(Throwable(e))
                }
        }
    }

}
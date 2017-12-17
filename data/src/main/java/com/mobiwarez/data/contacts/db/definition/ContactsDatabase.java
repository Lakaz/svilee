package com.mobiwarez.data.contacts.db.definition;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.mobiwarez.data.contacts.db.ContactDao;
import com.mobiwarez.data.contacts.db.model.ContactModel;
import com.mobiwarez.data.givenToys.db.GivenToyDao;
import com.mobiwarez.data.givenToys.db.model.GivenToyModel;

/**
 * Created by Laki on 04/11/2017.
 */

@Database(entities = {ContactModel.class}, version = 1)
public abstract class ContactsDatabase extends RoomDatabase {
    public abstract ContactDao contactDao();
}

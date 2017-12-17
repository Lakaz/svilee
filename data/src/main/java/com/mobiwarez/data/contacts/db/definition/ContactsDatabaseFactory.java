package com.mobiwarez.data.contacts.db.definition;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.mobiwarez.data.givenToys.db.definition.GivenToysDatabase;

/**
 * Created by Laki on 04/11/2017.
 */

public class ContactsDatabaseFactory {

    private static ContactsDatabase instance = null;

    public static ContactsDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context,
                    ContactsDatabase.class, "database-contacts").build();
            return instance;
        }
        else {
            return instance;
        }
    }

}

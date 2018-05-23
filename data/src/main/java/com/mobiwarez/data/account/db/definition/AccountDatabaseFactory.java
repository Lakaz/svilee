package com.mobiwarez.data.account.db.definition;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.mobiwarez.data.contacts.db.definition.ContactsDatabase;

public class AccountDatabaseFactory {

    private static AccountDatabase instance = null;

    public static AccountDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context,
                    AccountDatabase.class, "database-account").build();
            return instance;
        }
        else {
            return instance;
        }
    }

}

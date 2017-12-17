package com.mobiwarez.data.givenToys.db.definition;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.mobiwarez.data.receivedToys.db.definition.ReceivedToysDatabase;

/**
 * Created by Laki on 31/10/2017.
 */

public class GivenToysDatabaseFactory {

    private static GivenToysDatabase instance = null;

    public static GivenToysDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context,
                    GivenToysDatabase.class, "database-given-toys").build();
            return instance;
        }
        else {
            return instance;
        }
    }

}

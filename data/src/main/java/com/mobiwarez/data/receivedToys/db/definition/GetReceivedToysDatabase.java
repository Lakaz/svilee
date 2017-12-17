package com.mobiwarez.data.receivedToys.db.definition;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.mobiwarez.data.toy.db.definition.ToysDatabase;

/**
 * Created by Laki on 31/10/2017.
 */

public class GetReceivedToysDatabase {

    private static ReceivedToysDatabase instance = null;

    public static ReceivedToysDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context,
                    ReceivedToysDatabase.class, "database-received-toys").build();
            return instance;
        }
        else {
            return instance;
        }
    }

}

package com.mobiwarez.data.toy.db.definition;

import android.arch.persistence.room.Room;
import android.content.Context;

/**
 * Created by Laki on 19/10/2017.
 */

public class ToysDatabaseFactory {

    private static ToysDatabase instance = null;

    public static ToysDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context,
                    ToysDatabase.class, "database-toys").build();
            return instance;
        }
        else {
            return instance;
        }
    }

}

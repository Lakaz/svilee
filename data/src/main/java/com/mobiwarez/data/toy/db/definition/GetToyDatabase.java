package com.mobiwarez.data.toy.db.definition;

import android.arch.persistence.room.Room;
import android.content.Context;

/**
 * Created by Laki on 24/10/2017.
 */

public class GetToyDatabase {

    private static ToysDatabase instance = null;

    public ToysDatabase getInstance(Context context) {
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

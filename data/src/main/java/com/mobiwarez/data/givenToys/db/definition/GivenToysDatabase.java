package com.mobiwarez.data.givenToys.db.definition;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.mobiwarez.data.givenToys.db.GivenToyDao;
import com.mobiwarez.data.givenToys.db.model.GivenToyModel;
import com.mobiwarez.data.receivedToys.db.ReceivedToyDao;
import com.mobiwarez.data.receivedToys.db.model.ReceivedToyModel;

/**
 * Created by Laki on 31/10/2017.
 */

@Database(entities = {GivenToyModel.class}, version = 1)
public abstract class GivenToysDatabase extends RoomDatabase {
    public abstract GivenToyDao givenToyDao();
}

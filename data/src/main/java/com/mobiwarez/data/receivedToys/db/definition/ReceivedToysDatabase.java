package com.mobiwarez.data.receivedToys.db.definition;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.mobiwarez.data.receivedToys.db.ReceivedToyDao;
import com.mobiwarez.data.receivedToys.db.model.ReceivedToyModel;
import com.mobiwarez.data.toy.db.ToyDao;
import com.mobiwarez.data.toy.db.model.ToyModel;

/**
 * Created by Laki on 31/10/2017.
 */
@Database(entities = {ReceivedToyModel.class}, version = 1)
public abstract class ReceivedToysDatabase extends RoomDatabase{
    public abstract ReceivedToyDao receivedToyDao();
}

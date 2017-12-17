package com.mobiwarez.data.toy.db.definition;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.mobiwarez.data.toy.db.ToyDao;
import com.mobiwarez.data.toy.db.model.ToyModel;

/**
 * Created by Laki on 19/10/2017.
 */

@Database(entities = {ToyModel.class}, version = 1)
public abstract class ToysDatabase extends RoomDatabase{
    public abstract ToyDao toyDao();
}

package com.mobiwarez.data.account.db.definition;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.mobiwarez.data.account.db.AccountDao;
import com.mobiwarez.data.account.db.model.AccountModel;

@Database(entities = {AccountModel.class}, version = 1)
public abstract class AccountDatabase extends RoomDatabase {
    public abstract AccountDao accountDao();
}

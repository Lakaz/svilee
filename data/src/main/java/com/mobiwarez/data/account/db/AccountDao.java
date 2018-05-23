package com.mobiwarez.data.account.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.mobiwarez.data.account.db.model.AccountModel;
import com.mobiwarez.data.contacts.db.model.ContactModel;

import java.util.List;

@Dao
public interface AccountDao {

    @Query("SELECT * FROM AccountModel")
    List<AccountModel> getAll();

/*
    @Query("SELECT * FROM AccountModel WHERE contact_uuid LIKE :id LIMIT 1")
    ContactModel findById(String id);
*/

    @Insert
    void insertAll(AccountModel... accountModels);

    @Delete
    void delete(AccountModel accountModel);

    @Update
    void updateAccount(AccountModel... accountModels);

}

package com.mobiwarez.data.contacts.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.mobiwarez.data.contacts.db.model.ContactModel;
import com.mobiwarez.data.givenToys.db.model.GivenToyModel;
import com.mobiwarez.data.toy.db.model.ToyModel;

import java.util.List;

import io.reactivex.Single;

/**
 * Created by Laki on 04/11/2017.
 */

@Dao
public interface ContactDao {

    @Query("SELECT * FROM ContactModel")
    List<ContactModel> getAll();

    @Query("SELECT * FROM ContactModel WHERE contact_uuid LIKE :id LIMIT 1")
    ContactModel findById(String id);

    @Insert
    void insertAll(ContactModel... contactModels);

    @Delete
    void delete(ContactModel contactModel);

    @Update
    void updateContact(ContactModel... contactModels);



}

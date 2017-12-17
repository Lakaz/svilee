package com.mobiwarez.data.receivedToys.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.mobiwarez.data.receivedToys.db.model.ReceivedToyModel;
import com.mobiwarez.data.toy.db.model.ToyModel;

import java.util.List;

import io.reactivex.Single;

/**
 * Created by Laki on 31/10/2017.
 */

@Dao
public interface ReceivedToyDao {

    @Query("SELECT * FROM ReceivedToyModel")
    List<ReceivedToyModel> getAll();


    @Insert
    void insertAll(ReceivedToyModel... receivedToyModels);

    @Delete
    void delete(ReceivedToyModel receivedToyModel);

    @Update
    void updateReceivedToyModel(ReceivedToyModel... receivedToyModels);

}

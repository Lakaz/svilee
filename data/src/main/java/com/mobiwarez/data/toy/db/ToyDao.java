package com.mobiwarez.data.toy.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.mobiwarez.data.toy.db.model.ToyModel;
import com.mobiwarez.domain.domain.models.Toy;

import java.util.List;

import io.reactivex.Single;

/**
 * Created by Laki on 19/10/2017.
 */

@Dao
public interface ToyDao {

    @Query("SELECT * FROM ToyModel")
    Single<List<ToyModel>> getAll();


    @Insert
    void insertAll(ToyModel... toyModels);

    @Delete
    void delete(ToyModel toyModel);

    @Update
    void updateUsers(ToyModel... toyModels);

}

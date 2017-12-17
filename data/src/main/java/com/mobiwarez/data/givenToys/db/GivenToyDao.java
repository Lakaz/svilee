package com.mobiwarez.data.givenToys.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import com.mobiwarez.data.givenToys.db.model.GivenToyModel;

import java.util.List;

import io.reactivex.Single;

/**
 * Created by Laki on 31/10/2017.
 */

@Dao
public interface GivenToyDao {

    @Query("SELECT * FROM GivenToyModel")
    List<GivenToyModel> getAll();


    @Query("SELECT * FROM GivenToyModel WHERE toyId LIKE :id LIMIT 1")
    GivenToyModel findById(String id);


    @Insert
    void insertAll(GivenToyModel... givenToyModels);

    @Delete
    void delete(GivenToyModel givenToyModel);

    @Update
    void updateGivenToyModel(GivenToyModel... givenToyModels);

}

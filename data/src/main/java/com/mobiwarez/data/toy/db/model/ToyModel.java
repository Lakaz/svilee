package com.mobiwarez.data.toy.db.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by Laki on 19/10/2017.
 */

@Entity
public class ToyModel {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "toyId")
    public String toyId;

    @ColumnInfo(name = "toy_name")
    public String toyName;

    @ColumnInfo(name = "toy_description")
    public String toyDescription;

    @ColumnInfo(name = "toy_category")
    public String toyCategory;

    @ColumnInfo(name = "time_posted")
    public String timePosted;

    @ColumnInfo(name = "toy_given")
    public int toyGiven;

    @ColumnInfo(name = "toy_url")
    public String toyUrl;
}

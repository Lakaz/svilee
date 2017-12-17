package com.mobiwarez.data.receivedToys.db.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by Laki on 31/10/2017.
 */

@Entity
public class ReceivedToyModel {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "toyId")
    public String toyId;

    @ColumnInfo(name = "toy_name")
    public String toyName;

    @ColumnInfo(name = "toy_description")
    public String toyDescription;

    @ColumnInfo(name = "toy_ageGroup")
    public String toyAgeGroup;

    @ColumnInfo(name = "toy_category")
    public String toyCategory;

    @ColumnInfo(name = "time_posted")
    public String timePosted;

    @ColumnInfo(name = "time_received")
    public String timeReceived;

    @ColumnInfo(name = "toy_given")
    public int toyGiven;

    @ColumnInfo(name = "toy_url")
    public String toyUrl;

    @ColumnInfo(name = "toy_pick_up_location")
    public String toyPickUpLocation;

    @ColumnInfo(name = "toy_pick_up_time")
    public String toyPickUpTime;

    @ColumnInfo(name = "giver_name")
    public String giverName;

    @ColumnInfo(name = "avatar_url")
    public String avatarUrl;

    @ColumnInfo(name = "giver_uuid")
    public String giverUuid;

}

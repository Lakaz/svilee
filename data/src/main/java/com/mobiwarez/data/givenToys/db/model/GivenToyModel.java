package com.mobiwarez.data.givenToys.db.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by Laki on 31/10/2017.
 */

@Entity
public class GivenToyModel {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "toyId")
    public String toyId;

    @ColumnInfo(name = "toy_GiverName")
    public String toyGiverName;

    @ColumnInfo(name = "user_avatar")
    public String userAvatar;

    @ColumnInfo(name = "item_title")
    public String itemTitle;

    @ColumnInfo(name = "pick_up_time")
    public String pickUpTime;

    @ColumnInfo(name = "pick_up_location")
    public String pickUpLocation;

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
    public int isToyGiven;

    @ColumnInfo(name = "toy_synced")
    public int isToySynced;

    @ColumnInfo(name = "toy_imagePath")
    public String toyImagePath;

    @ColumnInfo(name = "toy_url")
    public String toyUrl;

    @ColumnInfo(name = "receiver_name")
    public String receiverName;

    @ColumnInfo(name = "giver_UUID")
    public String giverUUID;

    @ColumnInfo(name = "given_locationName")
    public String givenLocationName;

    @ColumnInfo(name = "latitude")
    public float latitude;

    @ColumnInfo(name = "longitude")
    public float longitude;


}

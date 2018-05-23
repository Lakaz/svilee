package com.mobiwarez.data.account.db.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class AccountModel {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "credits")
    public String credits;

    @ColumnInfo(name = "reward_points")
    public String rewardPoints;

}

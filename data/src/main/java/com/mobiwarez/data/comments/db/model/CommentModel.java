package com.mobiwarez.data.comments.db.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;


@Entity
public class CommentModel {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "commentId")
    public String commentId;


    @ColumnInfo(name = "commentorName")
    public String commentorName;

    @ColumnInfo(name = "commentedName")
    public String commentedName;

    @ColumnInfo(name = "commentedUUID")
    public String commentedUUID;

    @ColumnInfo(name = "commentedAvatarUrl")
    public String commentedAvatarUrl;

    @ColumnInfo(name = "commentorAvataUrl")
    public String commentorAvataUrl;

    @ColumnInfo(name = "comment")
    public String comment;

    @ColumnInfo(name = "timeCommented")
    public String timeCommented;



}

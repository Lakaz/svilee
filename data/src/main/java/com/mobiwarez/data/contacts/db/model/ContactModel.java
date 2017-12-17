package com.mobiwarez.data.contacts.db.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by Laki on 04/11/2017.
 */

@Entity
public class ContactModel {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "contact_id")
    public String contactId;

    @ColumnInfo(name = "contact_name")
    public String contactName;

    @ColumnInfo(name = "contact_avatar")
    public String contactAvatar;

    @ColumnInfo(name = "chat_room")
    public String chatRoom;

    @ColumnInfo(name = "contact_uuid")
    public String contactUUID;


}

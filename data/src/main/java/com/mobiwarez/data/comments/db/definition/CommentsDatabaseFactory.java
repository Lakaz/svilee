package com.mobiwarez.data.comments.db.definition;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.mobiwarez.data.account.db.definition.AccountDatabase;

public class CommentsDatabaseFactory {

    private static CommentsDatabase instance = null;

    public static CommentsDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context,
                    CommentsDatabase.class, "database-comments").build();
            return instance;
        }
        else {
            return instance;
        }
    }

}

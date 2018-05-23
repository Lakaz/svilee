package com.mobiwarez.data.comments.db.definition;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.mobiwarez.data.comments.db.CommentDao;
import com.mobiwarez.data.comments.db.model.CommentModel;

@Database(entities = {CommentModel.class}, version = 1)
public abstract class CommentsDatabase extends RoomDatabase {
    public abstract CommentDao commentDao();
}

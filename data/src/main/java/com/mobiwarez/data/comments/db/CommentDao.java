package com.mobiwarez.data.comments.db;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.mobiwarez.data.account.db.model.AccountModel;
import com.mobiwarez.data.comments.db.model.CommentModel;

import java.util.List;

@Dao
public interface CommentDao {

    @Query("SELECT * FROM CommentModel")
    List<AccountModel> getAll();

    @Query("SELECT * FROM CommentModel WHERE commentId LIKE :id LIMIT 1")
    CommentModel findById(String id);

    @Insert
    void insertAll(CommentModel... commentModels);

    @Delete
    void delete(CommentModel commentModel);

    @Update
    void updateComment(CommentModel... commentModels);

}

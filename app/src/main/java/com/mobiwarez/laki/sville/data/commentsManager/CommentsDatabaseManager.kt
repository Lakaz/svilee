package com.mobiwarez.laki.sville.data.commentsManager

import android.content.Context
import com.mobiwarez.data.comments.db.definition.CommentsDatabaseFactory
import com.mobiwarez.data.comments.db.model.CommentModel
import io.reactivex.Observable

class CommentsDatabaseManager(val context: Context) {

    private val commentsDao = CommentsDatabaseFactory.getInstance(context).commentDao()

    fun addComment(commentModel: CommentModel): Observable<Boolean>{
        return Observable.create{
            subscription ->
            try {
                commentsDao.insertAll(commentModel)
                subscription.onNext(true)
                subscription.onComplete()
            }catch (e: Exception){
                subscription.onError(Throwable(e))
            }
        }

    }

    fun getComment(commentId: String): Observable<CommentModel> {
        return Observable.create {
            subscription ->
            try {
                val comment = commentsDao.findById(commentId)
                subscription.onNext(comment)
                subscription.onComplete()
            }catch (e: Exception){
                subscription.onError(Throwable(e))
            }
        }
    }

    fun simpleGetComment(commentId: String): CommentModel? {
        return try {
            val comment = commentsDao.findById(commentId)
            comment
        }catch (e: Exception){
            null
        }
    }

}
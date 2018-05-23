package com.mobiwarez.laki.sville.data.commentsManager

import com.mobiwarez.data.comments.db.model.CommentModel
import com.mobiwarez.laki.sville.converter.EngineToModelConverter
import com.mobiwarez.laki.sville.data.remotebackend.AppEngineBackend
import com.mobiwarez.laki.sville.models.CommentsViewModel
import com.mobiwarez.laki.sville.ui.comments.CommentListingResponse
import com.mobiwarez.laki.sville.ui.models.ToyViewModel
import com.mobiwarez.laki.sville.ui.toys.list.ItemListingResponse
import io.reactivex.Observable
import timber.log.Timber

class CommentsManager {

    val myApi = AppEngineBackend.getInstance()!!

    val converter = EngineToModelConverter()

    fun updateComment(comment: CommentModel): Observable<String> {

        return Observable.create {
            subscriber ->
            try {
                val response = myApi.postComment(
                        comment.commentId,
                        comment.commentorName,
                        comment.commentedName,
                        comment.commentorAvataUrl,
                        comment.commentedAvatarUrl,
                        comment.comment,
                        comment.commentedUUID
                ).execute()
                subscriber.onNext(response.response)
                subscriber.onComplete()
            }catch (e: Exception){
                e.printStackTrace()
                Timber.d(Throwable(e))
                subscriber.onError(Throwable(e))
            }

        }
    }

    fun getComments(uuid: String): Observable<CommentListingResponse> {
        return Observable.create {
            subscriber ->

            var itemListings = listOf<CommentsViewModel>()


                try {

                    val response = myApi.getComments(uuid).execute()
                    val TOTAL_PAGES = response.getNextPageToken()
                    val data = response.getItems()
                    if (data != null) itemListings = data.map{converter.convertCommentToCommentViewModel(it)}
                    val listingResults = CommentListingResponse(itemListings, TOTAL_PAGES)
                    subscriber.onNext(listingResults)
                    subscriber.onComplete()
                } catch (e1: Exception) {
                    e1.printStackTrace()
                    subscriber.onError(Throwable(e1))
                }





        }
    }

}
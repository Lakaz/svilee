package com.mobiwarez.domain.domain.repository

import com.mobiwarez.domain.domain.models.Toy
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Created by Laki on 19/10/2017.
 */
interface ToyRepository {

    fun getUserToy(): Single<List<Toy>>

    fun getToys(feedId: Int): Single<List<Toy>>

    fun toyExists(feedUrl: String): Single<Boolean>

    fun createNewToy(toy: Toy): Completable

    fun deleteToy(feedId: Int): Completable

    fun getToysFromLocal(): Single<List<Toy>>

/*
    abstract fun pullArticlesForFeedFromOrigin(feed: Feed): Completable

    abstract fun markArticleAsRead(articleId: Int): Completable

    abstract fun favouriteArticle(articleId: Int): Completable

    abstract fun unFavouriteArticle(articleId: Int): Completable

    abstract fun getUnreadArticlesCount(): Single<Long>

    abstract fun getFavouriteArticles(): Single<List<Article>>

    abstract fun shouldUpdateFeedsInBackground(): Single<Boolean>

    abstract fun setShouldUpdateFeedsInBackground(shouldUpdate: Boolean): Completable
*/

}
package com.mobiwarez.laki.seapp.util

import android.content.Context
import com.mobiwarez.data.givenToys.db.GivenToyDao
import com.mobiwarez.data.givenToys.db.definition.GivenToysDatabaseFactory
import com.mobiwarez.data.givenToys.db.model.GivenToyModel
import com.mobiwarez.data.receivedToys.db.definition.GetReceivedToysDatabase
import com.mobiwarez.data.receivedToys.db.model.ReceivedToyModel
import io.reactivex.Observable
import timber.log.Timber

/**
 * Created by Laki on 02/11/2017.
 */
class DatabaseManger(val context: Context) {

    private val dao: GivenToyDao = GivenToysDatabaseFactory.getInstance(context).givenToyDao()

    fun saveGivenItem(model: GivenToyModel): Observable<String> {
        return Observable.create{
            subscriber ->
            try {
                dao.insertAll(model)
                subscriber.onNext("done")
                subscriber.onComplete()
            }catch (e: Exception){
                subscriber.onError(Throwable(e))
            }

        }
    }

    fun getGivenItems(): Observable<List<GivenToyModel>> {
        return Observable.create{
            subscriber ->

                try {
                    val givenItems = dao.all
                    subscriber.onNext(givenItems)
                    subscriber.onComplete()
                }catch (e: Exception) {
                    subscriber.onError(Throwable(e))
                }
        }
    }

    fun deleteGivenItem(model: GivenToyModel): Observable<String> {
        return Observable.create{
            subscriber ->

                try {
                    dao.delete(model)
                    subscriber.onNext("done")
                    subscriber.onComplete()
                }catch (e: Exception) {
                    Timber.d(Throwable(e))
                    subscriber.onError(Throwable(e))
                }

        }
    }

    fun updateGivenItem(model: GivenToyModel): Observable<String> {
        return Observable.create{
            subscriber ->
                try {
                    dao.updateGivenToyModel(model)
                    subscriber.onNext("done")
                    subscriber.onComplete()
                }catch (e: Exception) {
                    Timber.d(Throwable(e))
                    subscriber.onError(Throwable(e))
                }
        }
    }

    fun saveReceivedItems(model: ReceivedToyModel): Observable<String> {
        return Observable.create{
            subscriber ->
                val dao = GetReceivedToysDatabase.getInstance(context).receivedToyDao()

                try{
                    dao.insertAll(model)
                    subscriber.onNext("done")
                    subscriber.onComplete()
                }catch (e: Exception) {
                    subscriber.onError(Throwable(e))
                }
        }
    }

    fun getReceivedItems(): Observable<List<ReceivedToyModel>> {
        return Observable.create {
            subscriber ->
                val dao = GetReceivedToysDatabase.getInstance(context).receivedToyDao()

                try {
                    val receivedItems = dao.all
                    subscriber.onNext(receivedItems)
                    subscriber.onComplete()
                }catch (e: Exception) {
                    subscriber.onError(Throwable(e))
                }
        }
    }



}
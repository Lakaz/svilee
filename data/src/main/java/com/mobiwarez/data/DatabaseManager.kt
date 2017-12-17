package com.mobiwarez.data

import android.content.Context
import com.mobiwarez.data.givenToys.db.definition.GivenToysDatabaseFactory
import com.mobiwarez.domain.domain.models.Toy
import io.reactivex.Observable


/**
 * Created by Laki on 02/11/2017.
 */
class DatabaseManager(val context: Context) {

    fun saveItemGiven(toy: Toy): Observable<String> {

        return Observable.create{
            val dao = GivenToysDatabaseFactory.getInstance(context).givenToyDao()


        }


    }

}
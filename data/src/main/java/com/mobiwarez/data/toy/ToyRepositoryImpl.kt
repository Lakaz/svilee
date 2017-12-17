package com.mobiwarez.data.toy

import com.mobiwarez.data.toy.converter.ToyModelConverterImpl
import com.mobiwarez.data.toy.db.ToyDao
import com.mobiwarez.data.toy.service.ToyService
import com.mobiwarez.domain.domain.models.Toy
import com.mobiwarez.domain.domain.repository.ToyRepository
import io.reactivex.Completable
import io.reactivex.Scheduler
import io.reactivex.Single

/**
 * Created by Laki on 19/10/2017.
 */
class ToyRepositoryImpl(val toyDao: ToyDao, val backgroundScheduler: Scheduler) : ToyRepository {

    //val myApi = AppEngineBackend.getInstance()

    val converter = ToyModelConverterImpl()

    override fun getUserToy(): Single<List<Toy>> {

        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.


/*
        val tt = toyDao.all

        val single = Single.defer {  toyDao.all.map {  } }
        single.subscribeOn(backgroundScheduler)
        return
*/
    }



    override fun getToys(feedId: Int): Single<List<Toy>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun toyExists(feedUrl: String): Single<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun getToysFromLocal(): Single<List<Toy>> {

        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

/*
        val single = Single.defer { toyDao.all }
        single.subscribeOn(backgroundScheduler)
        return single
*/
    }



    override fun createNewToy(toy: Toy): Completable {

        val saveAction = Completable.fromAction {
            val toyModel = converter.domainToModel(toy)
            toyDao.insertAll(toyModel)
        }

        val completable = Completable.defer { saveAction }
        completable.subscribeOn(backgroundScheduler)
        return completable


    }

    override fun deleteToy(feedId: Int): Completable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
package com.mobiwarez.laki.sville.data

import com.mobiwarez.laki.sville.data.remotebackend.AppEngineBackend
import io.reactivex.Observable
import com.example.laki.myapplication.backend.myApi.MyApi
import com.example.laki.myapplication.backend.myApi.model.SuccessResponse


/**
 * Created by Laki on 04/11/2017.
 */
class ReceiverManager {

    val myApi = AppEngineBackend.getInstance()

    fun receiveItem(id: String, uuid: String): Observable<String> {
        return Observable.create{
            subscription ->
                try{
                    val response = myApi.receiveItem(id, uuid).execute()
                    val message = response.getResponse()
                    subscription.onNext(message)
                    subscription.onComplete()
                }catch (e:Exception){
                    subscription.onError(Throwable(e))
                }
        }
    }


    fun receiveReactItem(id: String, uname: String, rUuid: String): Observable<String> {
        return Observable.create{
            subscription ->
            try{
                val response = myApi.receiveReactItem(id, uname, rUuid).execute()
                val message = response.getResponse()
                subscription.onNext(message)
                subscription.onComplete()
            }catch (e:Exception){
                subscription.onError(Throwable(e))
            }
        }
    }

}
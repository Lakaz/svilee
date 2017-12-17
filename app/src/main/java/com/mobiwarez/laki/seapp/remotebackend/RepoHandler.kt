package com.mobiwarez.laki.seapp.remotebackend

import android.location.Location
import android.util.Log
//import com.example.laki.myapplication.backend.ItemStatusResponse
import com.mobiwarez.laki.seapp.ui.models.ToyViewModel
import io.reactivex.Single

import com.example.laki.myapplication.backend.myApi.MyApi
import com.example.laki.myapplication.backend.myApi.model.Item
import com.example.laki.myapplication.backend.myApi.model.ItemStatusResponse
import com.example.laki.myapplication.backend.myApi.model.CollectionResponseItem
import com.example.laki.myapplication.backend.myApi.model.SuccessResponse
import com.mobiwarez.data.givenToys.db.model.GivenToyModel
import com.mobiwarez.laki.seapp.converter.EngineToModelConverter
import com.mobiwarez.laki.seapp.ui.toys.list.ItemListingResponse
import com.mobiwarez.laki.seapp.ui.toys.sharedtoys.StatusResponse
import io.reactivex.Completable
import io.reactivex.Observable
import timber.log.Timber
import java.io.IOException
import java.util.ArrayList


/**
 * Created by Laki on 01/11/2017.
 */
class RepoHandler {

    val myApi = AppEngineBackend.getInstance()!!
    val converter = EngineToModelConverter()


    fun deleteItemListing(itemId: String): Observable<String> {
        return Observable.create{
            subscriber ->
                try {
                    val response = myApi.deleteItem(itemId).execute()
                    subscriber.onNext(response.getResponse())
                    subscriber.onComplete()
                }catch (e: Exception){
                    e.printStackTrace()
                    Timber.d(Throwable(e))
                    subscriber.onError(Throwable(e))
                }
        }
    }

    fun getItemStatus(itemId: String): Observable<StatusResponse> {
        return Observable.create {
            subscriber ->

                try{
                    val response = myApi.checkItemStatus(itemId).execute()
                    subscriber.onNext(converter.convertToStatusResponse(response))
                    subscriber.onComplete()
                }catch (e: Exception){
                    subscriber.onError(Throwable(e))
                }
        }
    }

    fun getItemListings(ageGroup: String, category: String, location: Location, pageNumber: Int): Observable<ItemListingResponse> {
        return Observable.create {
            subscriber ->

                val longitude = location.longitude.toFloat()
                val latitude = location.latitude.toFloat()
                var itemListings = listOf<ToyViewModel>()
                try {

                    val response = myApi.getItemListings(ageGroup, category, latitude, longitude, pageNumber).execute()
                    val TOTAL_PAGES = response.getNextPageToken()
                    val data = response.getItems()
                    if (data != null) itemListings = data.map{converter.convertItemToToyModel(it, location)}
                    val listingResults = ItemListingResponse(itemListings, TOTAL_PAGES)

                    subscriber.onNext(listingResults)
                    subscriber.onComplete()
                } catch (e1: Exception) {
                    e1.printStackTrace()
                    subscriber.onError(Throwable(e1))
                }


        }
    }



    fun postListing(model: GivenToyModel): Observable<String> {

        return Observable.create{
            subscriber ->
                try {
                    val successResponse = myApi.postItem(
                            model.toyId,
                            model.toyGiverName,
                            model.toyDescription,
                            model.toyAgeGroup,
                            model.toyAgeGroup,
                            model.toyUrl,
                            model.userAvatar,
                            "uuid",
                            "available",
                            model.givenLocationName,
                            model.latitude,
                            model.longitude,
                            model.pickUpLocation,
                            model.pickUpTime,
                            model.itemTitle).execute()
                    val response = successResponse.getResponse()
                    subscriber.onNext(response)
                    subscriber.onComplete()
                } catch (e: IOException) {
                    Timber.d( e.message)
                    subscriber.onError(Throwable(e))

                }

        }



    }

    fun updateItem(model: GivenToyModel): Observable<String> {

        val itemId = model.toyId
        val giverName = model.toyGiverName
        val description = model.toyDescription
        val ageGroup = model.toyAgeGroup
        val category = model.toyCategory
        val image = model.toyUrl
        val avatar = model.userAvatar
        val uuid = model.giverUUID
        val status = "available"
        val placename = model.givenLocationName
        val latitude = model.latitude
        val longitude = model.longitude

        return Observable.create{
            subscriber ->
                try {
                    val response = myApi.updateItem(itemId, giverName, description, ageGroup, category, image, avatar, uuid, status, placename, latitude, longitude).execute()
                    subscriber.onNext(response.getResponse())
                    subscriber.onComplete()
                } catch (e: Exception) {
                    Timber.d(Throwable(e))
                    subscriber.onError(Throwable(e))
                }
        }

    }

    fun getMyListings(uuid: String): Observable<List<ToyViewModel>> {

        return Observable.create{
            subscriber ->
                try {

                } catch (e: Exception){
                    subscriber.onError(Throwable(e))
                }
        }
    }

}
package com.mobiwarez.laki.seapp.remotebackend;

import android.location.Location;
import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import com.example.laki.myapplication.backend.myApi.MyApi;
//import com.example.laki.myapplication.backend.Item;
import com.example.laki.myapplication.backend.myApi.model.Item;
import com.example.laki.myapplication.backend.myApi.model.SuccessResponse;
//import com.example.laki.myapplication.backend.SuccessResponse;
//import com.example.laki.myapplication.backend.myApi.CollectionResponseItem;
//import com.example.laki.myapplication.backend.myApi.Item;

import java.util.List;


/**
 * Created by Laki on 30/10/2017.
 */

public class ItemHandler {




    public static Observable getItems(String ageGroup, String category, Location location, int pageNumber) {
        MyApi myApi = AppEngineBackend.getInstance();

        return Observable.create((ObservableOnSubscribe<List<Item>>) e -> {

            float longitude = (float) location.getLongitude();
            float latitude = (float) location.getLatitude();

            try {

                e.onNext(myApi.getItemListings(ageGroup, category,latitude, longitude, pageNumber ).execute().getItems());
            } catch (Exception e1) {
                e1.printStackTrace();
                e.onError(e1);

            }
            e.onComplete();
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());


    }

    public static Observable receiveItems(String id, String receiverName) {
        MyApi myApi = AppEngineBackend.getInstance();

        return Observable.create((ObservableOnSubscribe<SuccessResponse>) e -> {

            try {

                e.onNext(myApi.receiveItem(id, receiverName).execute());
            } catch (Exception e1) {
                e1.printStackTrace();
                e.onError(e1);

            }
            e.onComplete();
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }
}

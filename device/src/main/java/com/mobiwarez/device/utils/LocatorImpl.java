package com.mobiwarez.device.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
//import android.location.Location;
import android.location.Location;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.location.LocationRequest;
import com.patloew.rxlocation.RxLocation;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.Single;

/**
 * Created by Laki on 26/10/2017.
 */

public class LocatorImpl implements Locator {

    private RxLocation rxLocation;
    private Context context;
    private final Scheduler backgroundScheduler;

    private final LocationRequest locationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(5000);


    public LocatorImpl(Context context, Scheduler backgroundScheduler) {
        this.rxLocation = new RxLocation(context);
        this.context = context;
        this.backgroundScheduler = backgroundScheduler;
    }

    @Override
    public Observable<Location> getLocation() {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }
        return Observable.defer(() -> rxLocation.location().updates(locationRequest))
                .subscribeOn(backgroundScheduler);

    }

    @Override
    public Single<List<Address>> getAddresses(Location location) {


        return Single.defer(() -> rxLocation.geocoding().fromLocation(location, 10))
                .subscribeOn(backgroundScheduler);

    }



}

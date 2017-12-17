package com.mobiwarez.device.utils;

import android.location.Address;
import android.location.Location;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by Laki on 26/10/2017.
 */

public interface Locator {

    Observable<Location> getLocation();

    Single<List<Address>> getAddresses(Location location);

}

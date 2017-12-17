package com.mobiwarez.data.util.connectivity;


import io.reactivex.Observable;
import io.reactivex.Single;

public interface ConnectivityReceiver {

    Observable<Boolean> getConnectivityStatus();

    Single<Boolean> isConnected();
}

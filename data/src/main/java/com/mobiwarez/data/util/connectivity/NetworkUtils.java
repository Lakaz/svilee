package com.mobiwarez.data.util.connectivity;


import io.reactivex.Single;

public interface NetworkUtils {

    Single<Boolean> isConnectedToInternet();

    Single<NetworkData> getActiveNetworkData();
}
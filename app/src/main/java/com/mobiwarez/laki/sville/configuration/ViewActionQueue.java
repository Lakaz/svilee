package com.mobiwarez.laki.sville.configuration;


import android.location.Location;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
//import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public interface ViewActionQueue<View> {

    void subscribeTo(Observable<Consumer<View>> observable, Consumer<View> onCompleteAction, Consumer<Throwable> errorAction);

   // void subscribeToLoc(Observable<Location> observable, Consumer<View> onCompleteAction, Consumer<Throwable> errorAction);

    void subscribeTo(Single<Consumer<View>> single, Consumer<Throwable> errorAction);

    void subscribeTo(Completable completable, Consumer<View> onCompleteAction, Consumer<Throwable> errorAction);

    void pause();

    void resume();

    void destroy();

    Observable<Consumer<View>> viewActionsObservable();

    //void subscribeTo(Observable<Location> locationObservable, Consumer<View> onComplete, Consumer<Throwable> o1);
}

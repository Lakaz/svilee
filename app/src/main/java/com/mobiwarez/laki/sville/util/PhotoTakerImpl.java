package com.mobiwarez.laki.sville.util;

import android.content.Context;
import android.net.Uri;

import com.mlsdev.rximagepicker.RxImagePicker;
import com.mlsdev.rximagepicker.Sources;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.Single;

/**
 * Created by Laki on 26/10/2017.
 */

public class PhotoTakerImpl implements PhotoTaker {

    private Context context;
    private final Scheduler backgroundScheduler;

    public PhotoTakerImpl(Context context, Scheduler backgroundScheduler) {
        this.context = context;
        this.backgroundScheduler = backgroundScheduler;
    }

    @Override
    public Observable<Uri> loadFromGallery() {

        return RxImagePicker.with(context).requestImage(Sources.GALLERY).subscribeOn(backgroundScheduler);
        //return Observable.defer(() -> RxImagePicker.with(context).requestImage(Sources.GALLERY))
                //.subscribeOn(backgroundScheduler);
    }

    @Override
    public Observable loadFromCamera() {
        return RxImagePicker.with(context).requestImage(Sources.CAMERA);
    }
}

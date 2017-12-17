package com.mobiwarez.laki.seapp.util;

import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by Laki on 26/10/2017.
 */

public interface PhotoTaker<T> {

    Observable<T> loadFromGallery();

    Observable<T> loadFromCamera();
}

package com.mobiwarez.laki.sville.dagger.application.module;

import android.content.Context;
import android.text.format.DateUtils;

import com.mobiwarez.data.util.CurrentTimeProvider;
import com.mobiwarez.data.util.CurrentTimeProviderImpl;
import com.mobiwarez.data.util.connectivity.ConnectivityManagerWrapper;
import com.mobiwarez.data.util.connectivity.ConnectivityManagerWrapperImpl;
import com.mobiwarez.data.util.connectivity.ConnectivityReceiver;
import com.mobiwarez.data.util.connectivity.ConnectivityReceiverImpl;
import com.mobiwarez.data.util.connectivity.NetworkUtils;
import com.mobiwarez.data.util.connectivity.NetworkUtilsImpl;
import com.mobiwarez.device.utils.Locator;
import com.mobiwarez.device.utils.LocatorImpl;
import com.mobiwarez.laki.sville.dagger.application.ForApplication;
import com.mobiwarez.laki.sville.util.ActivityUtils;
import com.mobiwarez.laki.sville.util.ActivityUtilsImpl;
import com.mobiwarez.laki.sville.util.DateUtilsImpl;
import com.mobiwarez.laki.sville.util.ImageLoader;
import com.mobiwarez.laki.sville.util.ImageLoaderImpl;
import com.mobiwarez.laki.sville.util.PhotoTaker;
import com.mobiwarez.laki.sville.util.PhotoTakerImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Laki on 19/10/2017.
 */

@Module
public final class UtilsModule {


/*
    @Provides
    @Singleton
    CollectionUtils provideCollectionUtils() {
        return new CollectionUtilsImpl();
    }
*/
    @Provides
    @Singleton
    ActivityUtils provideActivityUtils() {
        return new ActivityUtilsImpl();
    }


    @Provides
    @Singleton
    CurrentTimeProvider provideCurrentTimeProvider() {
        return new CurrentTimeProviderImpl();
    }

    @Provides
    @Singleton
    PhotoTaker providePhotoTaker(final @ForApplication Context context) {
        return new PhotoTakerImpl(context, Schedulers.io());
    }

    @Provides
    @Singleton
    Locator provideLocation(final @ForApplication Context context) {
        return new LocatorImpl(context, Schedulers.io());
    }


/*
    @Provides
    @Singleton
    DateUtils provideDateUtils() {
        return new DateUtilsImpl();
    }
*/

    @Provides
    @Singleton
    ConnectivityReceiver provideConnectivityReceiver(final @ForApplication Context context, final NetworkUtils networkUtils) {
        return new ConnectivityReceiverImpl(context, networkUtils, Schedulers.io());
    }

/*
    @Provides
    @Singleton
    Notifications provideNotifications(final NotificationManagerCompat notificationManagerCompat) {
        return new NotificationsImpl(notificationManagerCompat);
    }

    @Provides
    @Singleton
    NotificationManagerCompat provideNotificationManagerCompat(final @ForApplication Context context) {
        return NotificationManagerCompat.from(context);
    }
*/

    @Provides
    @Singleton
    ImageLoader provideImageLoader(final @ForApplication Context context) {
        return new ImageLoaderImpl(context);
    }

/*
    @Provides
    @Singleton
    PreferenceUtils providePreferenceUtils(final @ForApplication Context context) {
        return new PreferenceUtilsImpl(context);
    }
*/

    @Provides
    @Singleton
    NetworkUtils provideNetworkUtils(final ConnectivityManagerWrapper connectivityManagerWrapper) {
        return new NetworkUtilsImpl(connectivityManagerWrapper);
    }

    @Provides
    @Singleton
    ConnectivityManagerWrapper provideConnectivityManagerWrapper(final @ForApplication Context context) {
        return new ConnectivityManagerWrapperImpl(context);
    }

    public interface Exposes {

        //CollectionUtils collectionUtils();

        ActivityUtils activityUtils();

        CurrentTimeProvider currentTimeProvider();

        //DateUtils dateUtils();

        ConnectivityReceiver connectivityReceiver();

        PhotoTaker photoTaker();

        Locator provideLocation();

        //Notifications notificationUtils();

        ImageLoader imageLoader();
    }


}

package com.mobiwarez.laki.seapp.dagger.application.module;

import android.content.Context;
import android.content.res.Resources;

import com.mobiwarez.laki.seapp.configuration.UUIDViewIdGenerator;
import com.mobiwarez.laki.seapp.configuration.ViewActionQueueProvider;
import com.mobiwarez.laki.seapp.configuration.ViewActionQueueProviderImpl;
import com.mobiwarez.laki.seapp.configuration.ViewIdGenerator;
import com.mobiwarez.laki.seapp.dagger.application.ForApplication;
import com.mobiwarez.laki.seapp.dagger.application.SeeAppApplication;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;

/**
 * Created by Laki on 19/10/2017.
 */

@Module
public class ApplicationModule {

    private final SeeAppApplication seeAppApplication;

    public ApplicationModule(final SeeAppApplication seeAppApplication){
        this.seeAppApplication = seeAppApplication;
    }

    @Provides
    @Singleton
    @ForApplication
    Context provideContext() {
        return seeAppApplication;
    }


    @Provides
    @Singleton
    SeeAppApplication provideSeeApplication(){return seeAppApplication;}

    @Provides
    @Singleton
    ViewIdGenerator provideViewIdGenerator(){
        return new UUIDViewIdGenerator();
    }

    @Provides
    @Singleton
    Resources provideResources() {
        return seeAppApplication.getResources();
    }


    @Provides
    @Singleton
    ViewActionQueueProvider provideViewActionQueueProvider(final @Named(ThreadingModule.MAIN_SCHEDULER) Scheduler mainScheduler) {
        return new ViewActionQueueProviderImpl(mainScheduler);
    }




    public interface Exposes {

        SeeAppApplication seeAppApplication();

        @ForApplication
        Context context();

        ViewIdGenerator viewIdGenerator();

        ViewActionQueueProvider viewActionQueueProvider();

        Resources resources();
    }


}

package com.mobiwarez.laki.sville.dagger.application.module;

import android.content.Context;

import com.mobiwarez.data.toy.ToyRepoImpl;
import com.mobiwarez.data.toy.db.definition.GetToyDatabase;
import com.mobiwarez.data.toy.db.definition.ToysDatabase;
import com.mobiwarez.domain.domain.repository.ToyRepository;
import com.mobiwarez.laki.sville.dagger.application.ForApplication;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;

/**
 * Created by Laki on 24/10/2017.
 */

@Module
public class RealDataModule {




    @Provides
    @Singleton
    ToyRepository provideToyRepository(ToysDatabase toysDatabase, final @Named(ThreadingModule.BACKGROUND_SCHEDULER) Scheduler backgroundScheduler) {
        return new ToyRepoImpl(toysDatabase, backgroundScheduler);
    }


    @Provides
    @Singleton
    ToysDatabase provideToyDatabase(@ForApplication Context context, GetToyDatabase getToyDatabase) {
        return getToyDatabase.getInstance(context);
    }

    @Provides
    @Singleton
    GetToyDatabase provideGetToyDatabase() {
        return new GetToyDatabase();
    }

    interface Exposes {

        ToyRepository toyRepository();

        ToysDatabase toyDatabase();

        GetToyDatabase getDatabase();
    }


}

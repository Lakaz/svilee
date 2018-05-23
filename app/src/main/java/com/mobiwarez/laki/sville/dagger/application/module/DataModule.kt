package com.mobiwarez.laki.sville.dagger.application.module

//import com.mobiwarez.data.toy.ToyRepositoryImpl
//import com.mobiwarez.data.toy.ToyRepositoryImpl
import android.content.ComponentName
import android.content.Context
import com.mobiwarez.data.toy.ToyRepoImpl
//import com.mobiwarez.data.toy.ToyRepositoryImpl
import com.mobiwarez.data.toy.db.ToyDao
import com.mobiwarez.data.toy.db.definition.GetToyDatabase
import com.mobiwarez.data.toy.db.definition.ToysDatabase
import com.mobiwarez.data.toy.db.definition.ToysDatabaseFactory
import com.mobiwarez.data.toy.service.ToyService
import com.mobiwarez.data.util.PreferenceUtils
import com.mobiwarez.domain.domain.repository.ToyRepository
import com.mobiwarez.laki.sville.dagger.application.ForApplication

import javax.inject.Named
import javax.inject.Singleton

import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler

/*
import oxim.digital.reedly.data.feed.FeedRepositoryImpl;
import oxim.digital.reedly.data.feed.converter.FeedModelConverter;
import oxim.digital.reedly.data.feed.converter.FeedModelConverterImpl;
import oxim.digital.reedly.data.feed.db.FeedDao;
import oxim.digital.reedly.data.feed.db.FeedDaoImpl;
import oxim.digital.reedly.data.feed.service.FeedService;
import oxim.digital.reedly.data.feed.service.parser.ExternalParserWrapper;
import oxim.digital.reedly.data.feed.service.parser.EarlParserWrapper;
import oxim.digital.reedly.data.feed.service.parser.FeedParser;
import oxim.digital.reedly.data.feed.service.parser.FeedParserImpl;
import oxim.digital.reedly.data.util.CurrentTimeProvider;
import oxim.digital.reedly.data.util.PreferenceUtils;
import oxim.digital.reedly.domain.repository.FeedRepository;
import rx.Scheduler;
*/

@Module
class DataModule {

    @Provides
    @Singleton
    fun provideToyRepository(toyDatabase: ToysDatabase, @Named(ThreadingModule.BACKGROUND_SCHEDULER) backgroundScheduler: Scheduler): ToyRepository {
        return ToyRepoImpl(toyDatabase, backgroundScheduler)
    }


    @Provides
    @Singleton
    fun provideToyDatabase(@ForApplication context: Context, getToyDatabase: GetToyDatabase): ToysDatabase {
        return getToyDatabase.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideGetToyDatabase(): GetToyDatabase {
        return GetToyDatabase()
    }



    interface Exposes {

        fun toyRepository(): ToyRepository

        fun toyDatabase(): ToysDatabase

        fun getDatabase(): GetToyDatabase
    }
}

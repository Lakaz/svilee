package com.mobiwarez.laki.seapp.dagger.application.module

import android.app.PendingIntent
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.res.Resources

import com.mobiwarez.data.toy.service.ToyService
import com.mobiwarez.device.job.Jobs
import com.mobiwarez.device.job.JobsImpl
import com.mobiwarez.laki.seapp.dagger.application.ForApplication

import javax.inject.Singleton

import dagger.Module
import dagger.Provides

/*
import oxim.digital.reedly.MainActivity;
import oxim.digital.reedly.background.BackgroundFeedsUpdateService;
import oxim.digital.reedly.background.FeedUpdateJobInfoFactory;
import oxim.digital.reedly.background.FeedsUpdateSchedulerImpl;
import oxim.digital.reedly.background.NotificationFactory;
import oxim.digital.reedly.background.NotificationFactoryImpl;
import oxim.digital.reedly.dagger.application.ForApplication;
import oxim.digital.reedly.data.feed.service.FeedService;
import oxim.digital.reedly.data.feed.service.FeedServiceImpl;
import oxim.digital.reedly.data.feed.service.parser.FeedParser;
import oxim.digital.reedly.device.job.Jobs;
import oxim.digital.reedly.device.job.JobsImpl;
import oxim.digital.reedly.domain.update.FeedsUpdateScheduler;
*/

@Module
class ServiceModule {

    /*
    @Provides
    @Singleton
    FeedService provideFeedService(final FeedParser feedParser) {
        return new FeedServiceImpl(feedParser);
    }
*/

/*
    @Provides
    @Singleton
    fun provideJobSchedulerWrapper(jobScheduler: JobScheduler): Jobs {
        return JobsImpl(jobScheduler)
    }

    @Provides
    @Singleton
    fun provideJobScheduler(@ForApplication context: Context): JobScheduler {
        return context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
    }
*/

    /*
    @Provides
    @Singleton
    FeedsUpdateScheduler provideFeedsUpdateScheduler(final JobInfo feedUpdateJobInfo, final Jobs jobs) {
        return new FeedsUpdateSchedulerImpl(feedUpdateJobInfo, jobs);
    }

    @Provides
    @Singleton
    JobInfo provideFeedJobInfo(final ComponentName feedsUpdateJobService) {
        return FeedUpdateJobInfoFactory.createJobInfo(FEEDS_UPDATE_JOB_ID, FEEDS_UPDATE_INTERVAL_MINS, feedsUpdateJobService);
    }

    @Provides
    @Singleton
    NotificationFactory provideNotificationFactory(final @ForApplication Context context, final Resources resources) {
        return new NotificationFactoryImpl(context, resources);
    }

    @Provides
    @Singleton
    ComponentName provideFeedsUpdateJobService(final @ForApplication Context context) {
        return new ComponentName(context, BackgroundFeedsUpdateService.class);
    }

    @Provides
    @Singleton
    PendingIntent provideFeedUpdateNotificationPendingIntent(final @ForApplication Context context) {
        final Intent targetActivityIntent = new Intent(context, MainActivity.class);
        return PendingIntent.getActivity(context, 0, targetActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
*/

    interface Exposes {

//        fun toyService(): ToyService

        //FeedService feedService();
    }

    companion object {

        private val FEEDS_UPDATE_JOB_ID = 1978
        private val FEEDS_UPDATE_INTERVAL_MINS = 30
    }
}

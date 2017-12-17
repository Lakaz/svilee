package com.mobiwarez.laki.seapp.job;

import android.support.annotation.NonNull;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobRequest;
import com.evernote.android.job.util.support.PersistableBundleCompat;
//import com.example.laki.myapplication.backend.myApi.MyApi;
//import com.mobiwarez.sache.remoteBackEnd.AppEngineBackend;

import java.io.IOException;

/**
 * Created by Laki on 03/08/2017.
 */

public class SubmitTokeJob extends Job {

    public static final String TOKEN_JOB_TAG = "token_tag";

    @NonNull
    @Override
    protected Result onRunJob(Params params) {

        String email = params.getExtras().getString("email", "");
        String token = params.getExtras().getString("token", "");

/*
        MyApi myApi = AppEngineBackend.getInstance();

        try {
            myApi.serveRegistrationToken(email, token).execute();
        } catch (IOException e) {
            e.printStackTrace();

            return Result.RESCHEDULE;
        }

        return Result.SUCCESS;
*/
        return Result.SUCCESS;
    }

    @Override
    protected void onReschedule(int newJobId) {
        super.onReschedule(newJobId);
    }

    public static int scheduleSubmitTokenJob(String email, String token) {

/*
        PersistableBundleCompat extras = new PersistableBundleCompat();
        extras.putString("email", email);
        extras.putString("token", token);

        return new JobRequest.Builder(TOKEN_JOB_TAG)
                .setPersisted(true)
                .setExecutionWindow(30_000L, 40_000L)
                .setBackoffCriteria(5_000L, JobRequest.BackoffPolicy.EXPONENTIAL)
                .setRequiredNetworkType(JobRequest.NetworkType.CONNECTED)
                .setExtras(extras)
                .setRequirementsEnforced(true)
                .setPersisted(true)
                .build()
                .schedule();

*/

        return 0;
    }

}

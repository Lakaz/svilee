package com.mobiwarez.laki.seapp.job;

import android.content.Context;
import android.support.annotation.NonNull;

import com.evernote.android.job.JobCreator;
import com.evernote.android.job.JobManager;

public class AddReceiver extends JobCreator.AddJobCreatorReceiver {

    @Override
    protected void addJobCreator(@NonNull Context context, @NonNull JobManager manager) {
        manager.addJobCreator(new UploadJobCreator());
    }
}

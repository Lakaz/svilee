package com.mobiwarez.device.job;

import android.app.job.JobInfo;

public interface Jobs {

    int schedule(JobInfo jobInfo);

    void cancel(int jobId);
}

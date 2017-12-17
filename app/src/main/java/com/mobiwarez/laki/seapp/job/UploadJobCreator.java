package com.mobiwarez.laki.seapp.job;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;

/**
 * Created by Laki on 01/08/2017.
 */

public class UploadJobCreator implements JobCreator {
    @Override
    public Job create(String tag) {

        switch (tag) {
            case UploadItemJob.UPLOAD_ITEM_TAG:
                return new UploadItemJob();


            case SubmitTokeJob.TOKEN_JOB_TAG:
                return new SubmitTokeJob();

/*
            case GetOnlineStatusJob.STATUS_JOB_TAG:
                return new GetOnlineStatusJob();

            case NumberViewsJob.VIEWS_JOB_TAG:
                return new NumberViewsJob();

            case DeactivateZacheJob.DEACTIVATE_ZACHE_JOB_TAG:
                return new DeactivateZacheJob();
*/
            default:
                return null;
        }

    }


}

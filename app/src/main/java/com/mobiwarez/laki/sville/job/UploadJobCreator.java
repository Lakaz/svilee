package com.mobiwarez.laki.sville.job;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;

/**
 * Created by Laki on 01/08/2017.
 */

public class UploadJobCreator implements JobCreator {



    @Override
    public Job create(String tag) {

        switch (tag) {

            case "UPDATE_CREDITS_JOB":
                return new UpdateCreditsJob();

            case UploadItemJob.UPLOAD_ITEM_TAG:
                return new UploadItemJob();


            case SubmitTokeJob.TOKEN_JOB_TAG:
                return new SubmitTokeJob();

            case "POST_COMMENT_JOB":
                return new PostCommentJob();

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

package com.mobiwarez.laki.sville.job;

import android.support.annotation.NonNull;
import android.util.Log;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobRequest;
import com.evernote.android.job.util.support.PersistableBundleCompat;
import com.mobiwarez.data.givenToys.db.model.GivenToyModel;
import com.mobiwarez.laki.sville.ui.models.ToyViewModel;

/**
 * Created by Laki on 02/11/2017.
 */

public class UploadItemJob extends Job {

    public static final String UPLOAD_ITEM_TAG = "UPLOAD_ITEM_JOB";

    public static final String ITEM_ID = "itemID";
    public static final String ITEM_DESCRIPTION = "itemDescription";
    public static final String GIVER_AVATAR_URL = "avatarUrl";
    public static final String ITEM_CATEGORY = "itemCategory";
    public static final String ITEM_AGE_GROUP = "itemAgeGroup";
    public static final String ITEM_IMAGE_PATH = "itemImagePath";
    public static final String GIVER_NAME = "giverName";
    public static final String LOCATION_GIVEN = "location";

    @NonNull
    @Override
    protected Result onRunJob(Params params) {
        PersistableBundleCompat extras = params.getExtras();
        String itemId = extras.getString(ITEM_ID, "");

        UploadItem uploadItem = new UploadItem(itemId, this.getContext());

        boolean result = uploadItem.postItem();

        if (result){
            Log.d(UPLOAD_ITEM_TAG, "Posting succeeded");
            return Result.SUCCESS;
        }

        else {
            Log.d(UPLOAD_ITEM_TAG, "Rescheduling failed  post job");
            return Result.RESCHEDULE;
        }

    }

    public static int buildPostItemJob(String id) {
        PersistableBundleCompat extras = new PersistableBundleCompat();

        extras.putString(ITEM_ID, id);

        Log.d("POST", "posing gift");
/*
        extras.putString(ITEM_DESCRIPTION, toyViewModel.toyDescription);
        extras.putString(GIVER_AVATAR_URL, toyViewModel.toyUrl);
        extras.putString(ITEM_CATEGORY, toyViewModel.toyCategory);
        extras.putString(ITEM_AGE_GROUP, toyViewModel.toyAgeGroup);
        extras.putString(ITEM_IMAGE_PATH, toyViewModel.toyImagePath);
        extras.putString(GIVER_NAME, toyViewModel.toyGiverName);
        extras.putString(LOCATION_GIVEN, toyViewModel.getToyLocation());
*/

        return new JobRequest.Builder( UploadItemJob.UPLOAD_ITEM_TAG )
                .setExecutionWindow( 2_000L, 6_000L )
                .setRequiredNetworkType( JobRequest.NetworkType.CONNECTED )
                .setBackoffCriteria(10_000L, JobRequest.BackoffPolicy.EXPONENTIAL)
                .setExtras( extras )
                .setRequirementsEnforced( true )
                .build()
                .schedule();

    }
}

package com.mobiwarez.laki.seapp.job;

import com.example.laki.myapplication.backend.myApi.MyApi;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mobiwarez.data.givenToys.db.GivenToyDao;
import com.mobiwarez.data.givenToys.db.definition.GivenToysDatabaseFactory;
import com.mobiwarez.data.givenToys.db.model.GivenToyModel;
import com.mobiwarez.laki.seapp.R;
import com.mobiwarez.laki.seapp.prefs.GetUserData;
import com.mobiwarez.laki.seapp.remotebackend.AppEngineBackend;
import com.mobiwarez.laki.seapp.ui.models.ToyViewModel;
import com.mobiwarez.laki.seapp.ui.toys.sharedtoys.SharedToysActivity;
import com.mobiwarez.laki.seapp.util.DatabaseManger;
import com.mobiwarez.laki.seapp.util.IdGenerator;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Laki on 02/11/2017.
 */

public class UploadItem {

    static final String TAG = UploadItem.class.getSimpleName();


    private ToyViewModel toyViewModel;
    private String photoDownloadUrl;
    private boolean imagePostedSuccess;
    private boolean inProgress;
    private boolean uploadSuccess;
    private boolean isSavingPost;

    private NotificationManager mNotifyManager;
    private NotificationCompat.Builder mBuilder;
    int id = new Random().nextInt();
    private boolean fullPostSuccess;
    private Context context;
    private String itemId;
    private GivenToyModel givenToyModel;
    protected GivenToyDao factory;


    public UploadItem(String id, Context context) {
        this.itemId = id;
        this.context = context;

        factory = GivenToysDatabaseFactory.getInstance(context).givenToyDao();

        Intent intent = new Intent(context, SharedToysActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);


        mNotifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new android.support.v7.app.NotificationCompat.Builder(context);
        mBuilder.setContentTitle("sVill Upload")
                .setContentText("Upload in progress")
                .setSmallIcon(R.mipmap.launchicon)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        toyModel();

    }

    public void toyModel(){
        givenToyModel = factory.findById(itemId);
    }

    private void updateModel(){

        if (!TextUtils.isEmpty(photoDownloadUrl) && !photoDownloadUrl.equals("no_image")) {
            givenToyModel.toyUrl = photoDownloadUrl;
        }
        givenToyModel.isToySynced = 1;
        //factory.updateGivenToyModel(givenToyModel);

        DatabaseManger databaseManger = new DatabaseManger(context);
        databaseManger.updateGivenItem(givenToyModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mess -> System.out.print("done"));
    }

    public boolean postItem() {

        inProgress = true;
        // Displays the progress bar for the first time.
        mBuilder.setProgress(100, 0, false);
        mNotifyManager.notify(id, mBuilder.build());
        Log.d(TAG, "showed notification");

        boolean isSuccess = false;

        if (givenToyModel.toyImagePath.equals("no_image")){
            photoDownloadUrl = "no_image";
            uploadPost();

        }else {
            uploadImage();
        }

        while(inProgress){

        }


        return fullPostSuccess;
    }

    private boolean uploadImage() {
        File compressedImage = new File(givenToyModel.toyImagePath);
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference postsImagesRef = storageReference.child("images/items/+"+ IdGenerator.getFileName()+".jpg");



        Task task1 = postsImagesRef.putFile(Uri.fromFile(compressedImage))
                .addOnSuccessListener(taskSnapshot -> {
                    Log.d(TAG, "uploading image successfulyy");
                    photoDownloadUrl = taskSnapshot.getDownloadUrl().toString();
                    imagePostedSuccess = true;
                    //inProgress = false;
                })
                .addOnFailureListener(e -> {
                    Log.d(TAG, "Failed to upload image");
                    uploadSuccess = false;
                    //inProgress = false;
                    isSavingPost = false;
                })
                .addOnCompleteListener(task2 -> {
                    Log.d(TAG, "upload completed");
                    //inProgress = false;
                    SaveImagePostToIndex();
                })
                .addOnProgressListener(taskSnapshot -> {

                    Log.d(TAG, "uploading image in progress");

                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                    mBuilder.setProgress(100, (int) progress, false);
                    mNotifyManager.notify(id, mBuilder.build());

                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                })
                .addOnPausedListener(taskSnapshot -> Log.d(TAG, "uploading image paused"));



        while (!task1.isComplete()) {

        }


        return task1.isSuccessful();
    }

    private void SaveImagePostToIndex() {

        if (imagePostedSuccess) {
            uploadPost();
        }

        else {
            uploadSuccess = false;
            inProgress = false;
        }

    }

    private void uploadPost() {
        SavePostToIndex savePostToIndex = new SavePostToIndex();
        savePostToIndex.execute(givenToyModel);
    }


    private class SavePostToIndex extends AsyncTask<GivenToyModel, Void, String> {

        @Override
        protected String doInBackground(GivenToyModel... givenToyModels) {
            MyApi myApi = AppEngineBackend.getInstance();

            GivenToyModel givenToyModel = givenToyModels[0];

            String response = null;

            try {
                com.example.laki.myapplication.backend.myApi.model.SuccessResponse successResponse = myApi.postItem(
                        givenToyModel.toyId,
                        givenToyModel.toyGiverName,
                        givenToyModel.toyDescription,
                        givenToyModel.toyAgeGroup,
                        givenToyModel.toyCategory,
                        photoDownloadUrl,
                        givenToyModel.userAvatar,
                        givenToyModel.giverUUID,
                        "available",
                        givenToyModel.givenLocationName,
                        givenToyModel.latitude,
                        givenToyModel.longitude,
                        givenToyModel.pickUpLocation,
                        givenToyModel.pickUpTime,
                        givenToyModel.itemTitle).execute();
                response = successResponse.getResponse();
            } catch (IOException e) {
                Log.d("POSTIMAGE",e.getMessage());
                response = "failure";
            }
            return response;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            isSavingPost = false;

            if (s != null) {
                if (s.equals("success")) {
                    fullPostSuccess = true;
                    uploadSuccess = true;
                    inProgress = false;
                    Log.d("POSTJOB", "upload success");
                    mBuilder.setContentText("Post complete")
                            // Removes the progress bar
                            .setProgress(0, 0, false);
                    mNotifyManager.notify(id, mBuilder.build());
                    updateModel();

                } else {
                    fullPostSuccess = false;
                    inProgress = false;
                }
            }
        }
    }



}

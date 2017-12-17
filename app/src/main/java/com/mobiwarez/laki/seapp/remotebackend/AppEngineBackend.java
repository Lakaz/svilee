package com.mobiwarez.laki.seapp.remotebackend;

import com.example.laki.myapplication.backend.myApi.MyApi;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

/**
 * Created by Laki on 07/07/2017.
 */
public class AppEngineBackend {

    private static MyApi ourInstance = null;

    public static MyApi getInstance() {
        if (ourInstance == null) {

            MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                    .setRootUrl("https://socialexchange-a7a20.appspot.com/_ah/api/");


            ourInstance = builder.build();
            return ourInstance;

        }

        else {
            return ourInstance;
        }
    }

    private AppEngineBackend() {
    }
}

package com.mobiwarez.laki.seapp.messaging;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.laki.myapplication.backend.myApi.MyApi;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.mobiwarez.laki.seapp.prefs.GetUserData;
import com.mobiwarez.laki.seapp.prefs.UserInfo;
import com.mobiwarez.laki.seapp.remotebackend.AppEngineBackend;
//import com.mobiwarez.sache.remoteBackEnd.AppEngineBackend;
//import com.mobiwarez.sache.user.GetUserData;
//import com.mobiwarez.sache.user.UserInfo;

import java.io.IOException;

/**
 * Created by Laki on 09/07/2017.
 */

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService{

    private static final String TAG = "MyFirebaseIIDService";

    private static Context context;

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        context = this;
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }
    // [END refresh_token]

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.


        if (GetUserData.getUserEmail(this) != null && !GetUserData.getUserEmail(this).equals("no_email")) {
            String email = GetUserData.getUserUUID(this);
            RegisterTokenTask registerTokenTask = new RegisterTokenTask();
            registerTokenTask.execute(new String[]{email, token});
        }

        else {

            //SharedPreferences sharedPref = this.getPreferences( Context.MODE_PRIVATE );
            UserInfo.saveUserToken(token, this);

        }

/*
        editor.putLong( KEY_GEOFENCE_LAT, Double.doubleToRawLongBits( locationMarker.getPosition().latitude ));
        editor.putLong( KEY_GEOFENCE_LON, Double.doubleToRawLongBits( locationMarker.getPosition().longitude ));
*/



/*
        String email = GetUserData.getUserEmail(this);
        RegisterTokenTask registerTokenTask = new RegisterTokenTask();
        registerTokenTask.execute(new String[]{email, token});
*/
    }


    private class RegisterTokenTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            MyApi myApi = AppEngineBackend.getInstance();

            String email = strings[0];
            String token = strings[1];

            try {
                myApi.serveRegistrationToken(email, token).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            UserInfo.markToken(true,MyFirebaseInstanceIdService.context);
        }
    }


}

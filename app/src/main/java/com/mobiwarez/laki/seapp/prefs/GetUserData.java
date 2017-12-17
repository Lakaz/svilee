package com.mobiwarez.laki.seapp.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


/**
 * Created by Laki on 10/02/2017.
 */

public class GetUserData {

/*
    public static boolean getUserLoggedIn(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(Config.LOGGEDIN_SHARED_PREF, false);
    }

    public static boolean getUserSawWellCome(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(Config.SAW_WELLCOME_MESSAGE, false);
    }



    public static String getUserId(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Config.USER_ID, null);
    }
*/


    public static String getRealPhotoUrl(Context context) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if (user != null) {
            String url = user.getPhotoUrl().toString();
            url = TextUtils.isEmpty(url) ? "no_image" : url;
            return url;
        }
        return "no_image";
    }

    public static String getUserEmail(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Config.EMAIL_SHARED_PREF, "no_email");
    }

    public static String getUserDisplayName(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Config.DISPLAY_NAME_SHARED_PREF, "no_name");
    }

    public static String getUserPhotoUrl(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Config.PHOTO_URL_SHARED_PREF, "no_photo");
    }

    public static String getUserToken(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Config.TOKEN, "no_token");
    }

    public static boolean getStatusOfToken(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(Config.TOKEN_SAVED, false);
    }


    public static String getUserUUID(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Config.UUID, "no uuid");
    }


/*
    public static String getUserLast(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Config.LASTNAME_SHARED_PREF, null);
    }
*/



}

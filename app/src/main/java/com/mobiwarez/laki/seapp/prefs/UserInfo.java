package com.mobiwarez.laki.seapp.prefs;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Laki on 09/07/2017.
 */

public class UserInfo {

    public static String UserName;

    public static void saveUserInfo(String userEmail, String displayName, String photoUrl, String uuid, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(Config.EMAIL_SHARED_PREF, userEmail);
        editor.putString(Config.DISPLAY_NAME_SHARED_PREF, displayName);
        editor.putString(Config.PHOTO_URL_SHARED_PREF, photoUrl);
        editor.putString(Config.UUID, uuid);

        editor.commit();

    }

    public static void saveUserProfileUrl(String url, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(Config.PHOTO_URL_SHARED_PREF, url);

        editor.commit();

    }

    public static void saveUserToken(String token, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(Config.TOKEN, token);
        editor.commit();

    }

    public static void statusToken(Context context) {

    }


    public static void markToken(boolean token, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(Config.TOKEN_SAVED, token);
        editor.commit();

    }



}

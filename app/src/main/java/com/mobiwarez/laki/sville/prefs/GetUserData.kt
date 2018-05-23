package com.mobiwarez.laki.sville.prefs

import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


/**
 * Created by Laki on 10/02/2017.
 */

class GetUserData {

    companion object {


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


        fun getRealPhotoUrl(context: Context): String {
            val firebaseAuth = FirebaseAuth.getInstance()
            val user = firebaseAuth.currentUser

            if (user != null) {
                user.photoUrl?.let {
                    var url = user.photoUrl!!.toString()
                    url = if (TextUtils.isEmpty(url)) "no_image" else url
                    return url

                }
            }
            return "no_image"
        }

        fun getUserEmail(context: Context): String {
            val sharedPreferences = context.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getString(Config.EMAIL_SHARED_PREF, "no_email")
        }

        fun getUserDisplayName(context: Context): String {
            val sharedPreferences = context.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getString(Config.DISPLAY_NAME_SHARED_PREF, "no_name")
        }

        fun getUserPhotoUrl(context: Context): String {
            val sharedPreferences = context.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getString(Config.PHOTO_URL_SHARED_PREF, "no_photo")
        }

        fun getUserToken(context: Context): String {
            val sharedPreferences = context.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getString(Config.TOKEN, "no_token")
        }

        fun getStatusOfToken(context: Context): Boolean {
            val sharedPreferences = context.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getBoolean(Config.TOKEN_SAVED, false)
        }


        fun getUserUUID(context: Context): String {
            val sharedPreferences = context.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getString(Config.UUID, "no uuid")
        }


        /*
    public static String getUserLast(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Config.LASTNAME_SHARED_PREF, null);
    }
*/
    }

}

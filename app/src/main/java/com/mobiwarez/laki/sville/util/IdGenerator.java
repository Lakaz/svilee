package com.mobiwarez.laki.sville.util;

import android.content.Context;
//import com.mobiwarez.sache.user.GetUserData;

import com.mobiwarez.laki.sville.prefs.GetUserData;
import com.mobiwarez.laki.sville.prefs.GetUserData;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Laki on 12/07/2017.
 */

public class IdGenerator {

    public static String createGeofenceKey(Context context) {
        return "geofence_" + GetUserData.Companion.getUserEmail(context)+getTimeStamp();
    }

    public static String createZacheId(Context context) {
        return "zache_"+ GetUserData.Companion.getUserEmail(context)+getTimeStamp();
    }

    public static String createTimeScheduleId(Context context) {
        return "timesche_"+ GetUserData.Companion.getUserEmail(context)+getTimeStamp();
    }

    public static String createConnectionId(Context context, String email) {
        return "connection_"+ email+getTimeStamp();
    }


    public static String createLocationScheduleId(Context context) {
        return "locationsche_"+ GetUserData.Companion.getUserEmail(context)+getTimeStamp();
    }

    public static String createPostId(Context context) {
        return "post_"+ GetUserData.Companion.getUserEmail(context)+getTimeStamp();
    }


    public static String getTimeStamp() {
        return new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    }

    public static String getCommentId(Context context){
        return "comment_"+ GetUserData.Companion.getUserUUID(context);
    }

    public static String getFileName() {
        return UUID.randomUUID().toString();
    }
}

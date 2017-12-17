package com.mobiwarez.laki.seapp.prefs;

/**
 * Created by Laki on 26/01/2017.
 */
public class Config {

    //URL to our login.php file
    //public static final String LOGIN_URL = "http://192.168.94.1/Android/LoginLogout/login.php";
    public static final String LOGIN_URL = "https://connectzestore.com/app/login-call.php";
    public static final String POST_AD_URL = "https://connectzestore.com/app/app_0c9f0035fe458e87613834c4c18c50b529eb2e24.php";


    //Keys for email and password as defined in our $_POST['key'] in login.php
    public static final String KEY_EMAIL = "username";
    public static final String KEY_PASSWORD = "pass";

    //If server response is equal to this that means login is successful
    public static final String LOGIN_SUCCESS = "success";

    //Keys for Sharedpreferences
    //This would be the name of our shared preferences
    public static final String SHARED_PREF_NAME = "myloginapp";

    //This would be used to store the email of current logged in user
    public static final String EMAIL_SHARED_PREF = "email";
    public static final String PHONE_SHARED_PREF = "phone";
    public static final String USER_ID = "user_id";

    //We will use this to store the boolean in sharedpreference to track user is loggedin or not
    public static final String LOGGEDIN_SHARED_PREF = "loggedin";

    public static final String FIRSTNAME_SHARED_PREF = "firstName";
    public static final String LASTNAME_SHARED_PREF = "lastName";


    public static final String KEYWORD = "KEYWORD";
    public static final String COUNTRY = "COUNTRY";
    public static final String SUBCATEGORY = "SUBCATEGORY";
    public static final String CITY = "CITY";
    public static final String CATEGORY = "CATEGORY";


    public static final String SAW_WELLCOME_MESSAGE = "SAW_WELLCOME_MESSAGE" ;
    public static final String SIGNED_SHARED_PREF = "SIGNED_UP";
    public static final String DISPLAY_NAME_SHARED_PREF = "displayName";
    public static final String PHOTO_URL_SHARED_PREF = "photo_url";
    public static final String TOKEN = "token";
    public static final String TOKEN_SAVED = "token_saved";
    public static final String UUID = "uuid";
}

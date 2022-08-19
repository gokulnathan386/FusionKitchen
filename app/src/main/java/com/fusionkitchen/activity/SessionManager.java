package com.fusionkitchen.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashMap;

/**
 * Created by Lincoln on 05/01/21.
 * Post code api url and php session id store process
 */
public class SessionManager {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "AndroidHivePref";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    // User name (make variable public to access from outside)
    public static final String KEY_posturl = "posturl";

    // Email address (make variable public to access from outside)
    public static final String KEY_phpsessid = "phpsessid";

    public static final String KEY_area = "areaname";
    public static final String KEY_postcode = "postcode";

    public static final String KEY_lat = "lat";
    public static final String KEY_lon = "lon";
    public static final String KEY_address = "address";

    // Constructor
    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     */
    public void createLoginSession(String posturl, String phpsessid, String area, String postcode, String lat, String lon, String address) {
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing name in pref
        editor.putString(KEY_posturl, posturl);

        // Storing email in pref
        editor.putString(KEY_phpsessid, phpsessid);

        editor.putString(KEY_area, area);
        editor.putString(KEY_postcode, postcode);

        editor.putString(KEY_lat, lat);
        editor.putString(KEY_lon, lon);
        editor.putString(KEY_address, address);
        // commit changes
        editor.commit();
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     */
    public void checkLogin() {
        // Check login status
        if (!this.isLoggedIn()) {
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, Postcode_Activity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        }

    }


    /**
     * Get stored session data
     */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(KEY_posturl, pref.getString(KEY_posturl, null));

        // user email id
        user.put(KEY_phpsessid, pref.getString(KEY_phpsessid, null));

        // user email id
        user.put(KEY_area, pref.getString(KEY_area, null));
        user.put(KEY_postcode, pref.getString(KEY_postcode, null));

        user.put(KEY_lat, pref.getString(KEY_lat, null));
        user.put(KEY_lon, pref.getString(KEY_lon, null));
        user.put(KEY_address, pref.getString(KEY_address, null));
        // return user
        return user;
    }

    /**
     * Clear session details
     */
    public void logoutUser() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, Postcode_Activity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }

    /**
     * Quick check for login
     **/
    // Get Login State
    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }
}

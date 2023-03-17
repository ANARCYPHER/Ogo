package com.cscodetech.urbantaxi.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.cscodetech.urbantaxi.model.RiderData;
import com.google.gson.Gson;


public class SessionManager {

    private final SharedPreferences mPrefs;
    SharedPreferences.Editor mEditor;

    public static String login = "login";

    public static String user = "users";


    public static String currency = "currency";

    public static String language = "language";


    public static String contact = "contact";

    public SessionManager(Context context) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        mEditor = mPrefs.edit();
    }

    public void setStringData(String key, String val) {
        mEditor.putString(key, val);
        mEditor.commit();
    }

    public String getStringData(String key) {
        return mPrefs.getString(key, "");
    }

    public void setFloatData(String key, float val) {
        mEditor.putFloat(key, val);
        mEditor.commit();
    }

    public float getFloatData(String key) {
        return mPrefs.getFloat(key, 0);
    }

    public void setBooleanData(String key, Boolean val) {
        mEditor.putBoolean(key, val);
        mEditor.commit();
    }

    public boolean getBooleanData(String key) {
        return mPrefs.getBoolean(key, false);
    }

    public void setIntData(String key, int val) {
        mEditor.putInt(key, val);
        mEditor.commit();
    }

    public int getIntData(String key) {
        return mPrefs.getInt(key, 0);
    }

    public void setUserDetails(RiderData val) {
        mEditor.putString(user, new Gson().toJson("val"));
        mEditor.commit();
    }

    public RiderData getUserDetails() {
        return new Gson().fromJson(mPrefs.getString(user, ""), RiderData.class);
    }


    public void logoutUser() {
        mEditor.clear();
        mEditor.commit();
    }
}

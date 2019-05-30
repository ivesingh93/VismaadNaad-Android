package com.vismaad.naad.player.service;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.ArraySet;

import com.facebook.FacebookSdk;
import com.google.gson.Gson;
import com.vismaad.naad.player.service.NotificationManager;
import com.vismaad.naad.rest.model.raagi.Shabad;
import com.squareup.picasso.RequestCreator;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by DELL on 1/29/2018.
 */

public class App extends Application implements Application.ActivityLifecycleCallbacks {
    NotificationManager notificationManager;

    private static Application _instance;
    private static SharedPreferences _preferences;
    private static ShabadPlayerForegroundService playerService;
    private static RadioPlayerService mRadioPlayerService;
    private static Gson gson;

    public static Gson getGson() {
        if (gson == null) {
            gson = new Gson();
        }
        return gson;
    }

    /**
     * Get application.
     *
     * @return the application
     */
    public static Application get() {
        return _instance;
    }

    public static ShabadPlayerForegroundService getService() {
        if (playerService == null) {
            playerService = new ShabadPlayerForegroundService();
        }
        return playerService;
    }

    public static void setService (ShabadPlayerForegroundService s) {
        playerService = s;
    }

    public static RadioPlayerService getRadioService() {
        if (mRadioPlayerService == null) {
            mRadioPlayerService = new RadioPlayerService();
        }
        return mRadioPlayerService;
    }

    public static void setService (RadioPlayerService s) {
        mRadioPlayerService = s;
    }




    /**
     * Gets shared preferences.
     *
     * @return the shared preferences
     */
    public static SharedPreferences getSharedPreferences() {
        if (_preferences == null)
            _preferences = PreferenceManager.getDefaultSharedPreferences(_instance);
        return _preferences;
    }

    public static void clearSharedPreferences() {
        getSharedPreferences().edit().clear().apply();
    }

    /**
     * Sets shared preferences.
     *
     * @return the shared preferences
     */
    public static void setPreferences(String key, String value) {
        getSharedPreferences().edit().putString(key, value).apply();
    }

    /**
     * Sets shared preferences.
     *
     * @return the shared preferences
     */
    public static void setPreferencesInt(String key, int value) {
        getSharedPreferences().edit().putInt(key, value).apply();
    }

    public static void setPreferencesList(String key, Set<String> value) {
        getSharedPreferences().edit().putStringSet(key, value).apply();
    }

    public static void setPreferencesBoolean(String key, boolean value) {
        getSharedPreferences().edit().putBoolean(key, value).apply();
    }

    public static void setPreferencesLong(String key, long value) {
        getSharedPreferences().edit().putLong(key, value).apply();
    }

    public static String getPrefranceData(String key) {
        return getSharedPreferences().getString(key, "");
    }

    public static int getPrefranceDataInt(String key) {
        return Integer.parseInt(getSharedPreferences().getString(key, "0"));
    }

    public static int getPreferanceInt(String key) {
        return getSharedPreferences().getInt(key, 0);
    }

    public static long getPreferenceLong(String key) {
        return getSharedPreferences().getLong(key, 0);
    }

    public static boolean getPrefranceDataBoolean(String key) {
        return getSharedPreferences().getBoolean(key, false);
    }

    public static Set<String> getPreferencesList(String key) {
        return getSharedPreferences().getStringSet(key, new HashSet<String>());
    }


    @Override
    public void onCreate() {
        super.onCreate();
        _instance = this;
        registerActivityLifecycleCallbacks(this);
        notificationManager = new NotificationManager(getApplicationContext());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createMainNotificationChannel();
        }
//        FacebookSdk.sdkInitialize(getApplicationContext());
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }


}

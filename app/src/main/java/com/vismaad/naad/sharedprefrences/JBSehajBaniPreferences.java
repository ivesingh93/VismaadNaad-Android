package com.vismaad.naad.sharedprefrences;

import android.content.SharedPreferences;

public class JBSehajBaniPreferences {

    public static void setJwtToken(SharedPreferences preferences, String email) {
        preferences.edit().putString(SehajBaniPreferences.JWT_TOKEN, email).commit();
    }

    public static String getJwtToken(SharedPreferences preferences) {
        return preferences.getString(SehajBaniPreferences.JWT_TOKEN, SehajBaniPreferences.JWT_TOKEN_VALUE);
    }

    public static void setLoginId(SharedPreferences preferences, String email) {
        preferences.edit().putString(SehajBaniPreferences.LOGIN_ID, email).commit();
    }

    public static String getLoginId(SharedPreferences preferences) {
        return preferences.getString(SehajBaniPreferences.LOGIN_ID, SehajBaniPreferences.LOGIN_ID_VALUE);
    }

    public static void setRaggiId(SharedPreferences preferences, String raggi) {
        preferences.edit().putString(SehajBaniPreferences.RAGGI_ID, raggi).commit();
    }

    public static String getRaggiId(SharedPreferences preferences) {
        return preferences.getString(SehajBaniPreferences.RAGGI_ID, SehajBaniPreferences.RAGGI_ID_VALUE);
    }

    public static void setAdsCount(SharedPreferences preferences, int raggi) {
        preferences.edit().putInt(SehajBaniPreferences.ADS_SHABADS, raggi).commit();
    }

    public static int getAdsCount(SharedPreferences preferences) {
        return preferences.getInt(SehajBaniPreferences.ADS_SHABADS, SehajBaniPreferences.ADS_SHABADS_VALUE);
    }


    public static void setBtnSkip(SharedPreferences preferences, String raggi) {
        preferences.edit().putString(SehajBaniPreferences.SKIP_BUTTON, raggi).commit();
    }

    public static String getBtnSkip(SharedPreferences preferences) {
        return preferences.getString(SehajBaniPreferences.SKIP_BUTTON, SehajBaniPreferences.SKIP_BUTTON_VALUE);
    }

    public static void setCount(SharedPreferences preferences, int count) {
        preferences.edit().putInt(SehajBaniPreferences.Count, count).commit();
    }

    public static int getCount(SharedPreferences preferences) {
        return preferences.getInt(SehajBaniPreferences.Count, SehajBaniPreferences.Count_VALUE);
    }

}

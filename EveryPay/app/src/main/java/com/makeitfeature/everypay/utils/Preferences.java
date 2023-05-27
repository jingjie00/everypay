package com.makeitfeature.everypay.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {
    private static final String preferences = "preferences";
    public final static String SIGNIN_ANOMYNOUS = "SIGNIN_ANOMYNOUS";
    public final static String SETTING_LANGUAGE = "LANGUAGE";
    public final static String SETTING_ONSCREEN="ON_SCREEN_RECOGNITION";
    public final static String SETTING_AUTOBACKUP="AUTOBACKUP";
    public final static String SETTING_NEXTBACKUP="NEXTBACKUP";
    public final static String SETTING_OUTPUTINTERVAL="OUTPUTINTERVAL";
    public final static String SETTING_TEXTLOCK ="TEXTLOCK";

    static public boolean getBooleanPreferenceValue(Context context, String key)
    {
        SharedPreferences sp = context.getSharedPreferences(preferences,0);
        switch(key){
            case SETTING_TEXTLOCK: {
                return sp.getBoolean(key,true);
            } default:{
            return sp.getBoolean(key, false);
            }
        }

    }

    static public void writeBooleanToPreference(Context context, String key, boolean val)
    {
        SharedPreferences.Editor editor = context.getSharedPreferences(preferences,0).edit();
        editor.putBoolean(key, val);
        editor.commit();
    }

    static public String getStringPreferenceValue(Context context, String key)
    {
        SharedPreferences sp = context.getSharedPreferences(preferences,0);
        String val = sp.getString(key,"");
        return val;
    }

    static public void writeStringToPreference(Context context, String key, String val)
    {
        SharedPreferences.Editor editor = context.getSharedPreferences(preferences,0).edit();
        editor.putString(key, val);
        editor.commit();
    }

    static public Long getLongPreferenceValue(Context context, String key)
    {
        SharedPreferences sp = context.getSharedPreferences(preferences,0);
        switch(key){
            case SETTING_NEXTBACKUP: {
                return sp.getLong(key, Long.MAX_VALUE); //max value just for safety
            }
            case SETTING_OUTPUTINTERVAL:{
                return sp.getLong(key,1000L);
            }case SETTING_LANGUAGE:{
                return sp.getLong(key,0L);
            }
            default:
                return 0L;
        }
    }

    static public void writeLongToPreference(Context context, String key, long val)
    {
        SharedPreferences.Editor editor = context.getSharedPreferences(preferences,0).edit();
        editor.putLong(key, val);
        editor.commit();
    }



    static public void clearAllPreference(Context context){
        SharedPreferences.Editor editor = context.getSharedPreferences(preferences,0).edit();
        editor.clear().commit();
    }
}

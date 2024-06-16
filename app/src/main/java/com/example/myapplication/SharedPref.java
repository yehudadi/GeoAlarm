package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {

    public static final String SHARED_PREFS = "SHARED_PREFS";
    public static final String DARK_MODE = "DARK_MODE";
    private SharedPreferences sharedpref;

    public SharedPref(Context context) {
        sharedpref = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
    }

    // read & write to SharedPreferences - boolean
    public boolean read(String key, boolean defValue) {
        return sharedpref.getBoolean(key, defValue);
    }

    public void write(String key, boolean value) {
        SharedPreferences.Editor prefsEditor = sharedpref.edit();
        prefsEditor.putBoolean(key, value);
        prefsEditor.apply();
    }

    // Close the SharedPreferences instance when not in use
    public void close() {
        sharedpref = null;
    }
}

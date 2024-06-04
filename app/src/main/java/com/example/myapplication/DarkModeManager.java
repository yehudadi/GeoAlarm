package com.example.myapplication;


import androidx.appcompat.app.AppCompatDelegate;

public class DarkModeManager {
    public static boolean IS_DARK_MODE_ON;

    public static void setDefaultDarkMode(SharedPref sharedPref) {
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
            IS_DARK_MODE_ON = true;
        else IS_DARK_MODE_ON = false;

        sharedPref.write(SharedPref.DARK_MODE, IS_DARK_MODE_ON);
    }

    public static void setDarkMode(boolean isDark, SharedPref sharedPref) {

        if (isDark) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        sharedPref.write(SharedPref.DARK_MODE, isDark);
        IS_DARK_MODE_ON = isDark;
    }
}

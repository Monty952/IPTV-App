package com.iptv.online.player.live.tv.smart.AdsPackage.custom;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatDelegate;
@SuppressWarnings("all")
/* loaded from: classes3.dex */
public class ThemeHelper {
    public static final String KEY_DARK_MODE_ENABLED_LEGACY = "dark_mode_enabled";
    public static final String KEY_DARK_MODE_LEGACY = "KEY_DARK_MODE";
    public static final String KEY_DARK_MODE_OPTION = "KEY_DARK_MODE_OPTION";
    public static final String PREFS_NAME = "AppPrefs";
    public static final int THEME_DARK = 2;
    public static final int THEME_LIGHT = 1;
    public static final int THEME_SYSTEM = 0;

    public static int getDarkModeOption(Context context) {
        if (context == null) {
            return 1;
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences("iptv_player_prefs", 0);
        if (sharedPreferences.contains("app_theme_mode")) {
            return sharedPreferences.getInt("app_theme_mode", 1) == 2 ? 2 : 1;
        }
        SharedPreferences sharedPreferences2 = context.getSharedPreferences("tvcast_theme_prefs", 0);
        if (sharedPreferences2.contains("key_theme_mode")) {
            return sharedPreferences2.getInt("key_theme_mode", 1) == 2 ? 2 : 1;
        }
        SharedPreferences sharedPreferences3 = context.getSharedPreferences("AppPrefs", 0);
        if (sharedPreferences3.contains(KEY_DARK_MODE_OPTION)) {
            return sharedPreferences3.getInt(KEY_DARK_MODE_OPTION, 1) == 2 ? 2 : 1;
        }
        SharedPreferences sharedPreferences4 = context.getSharedPreferences("launcher_prefs", 0);
        if (sharedPreferences4.contains(KEY_DARK_MODE_ENABLED_LEGACY)) {
            return sharedPreferences4.getBoolean(KEY_DARK_MODE_ENABLED_LEGACY, false) ? 2 : 1;
        } else if (sharedPreferences4.contains(KEY_DARK_MODE_LEGACY)) {
            return sharedPreferences4.getBoolean(KEY_DARK_MODE_LEGACY, false) ? 2 : 1;
        } else {
            SharedPreferences sharedPreferences5 = context.getSharedPreferences("calculator_settings_pref", 0);
            return (sharedPreferences5.contains("key_dark_mode_option") && sharedPreferences5.getInt("key_dark_mode_option", 1) == 2) ? 2 : 1;
        }
    }

    public static boolean isDarkMode(Context context) {
        return getDarkModeOption(context) == 2;
    }

    public static void applyTheme(Context context, int i) {
        if (context == null) {
            return;
        }
        int i2 = i == 2 ? 2 : 1;
        context.getSharedPreferences("iptv_player_prefs", 0).edit().putInt("app_theme_mode", i2).apply();
        context.getSharedPreferences("AppPrefs", 0).edit().putInt(KEY_DARK_MODE_OPTION, i2).commit();
        boolean z = i2 == 2;
        context.getSharedPreferences("launcher_prefs", 0).edit().putBoolean(KEY_DARK_MODE_ENABLED_LEGACY, z).putBoolean(KEY_DARK_MODE_LEGACY, z).commit();
        context.getSharedPreferences("calculator_settings_pref", 0).edit().putInt("key_dark_mode_option", i2).commit();
        int i3 = z
                ? AppCompatDelegate.MODE_NIGHT_YES
                : AppCompatDelegate.MODE_NIGHT_NO;
        if (AppCompatDelegate.getDefaultNightMode() != i3) {
            AppCompatDelegate.setDefaultNightMode(i3);
        }
       /* int i3 = z ? 2 : 1;
        if (AppCompatDelegate.getDefaultNightMode() != i3) {
            AppCompatDelegate.setDefaultNightMode(i3);
        }*/
    }

    public static void applyTheme(Context context) {
        applySavedTheme(context);
    }

    public static void applySavedTheme(Context context) {
        AppCompatDelegate.setDefaultNightMode(getDarkModeOption(context) != 2 ? 1 : 2);
    }
}

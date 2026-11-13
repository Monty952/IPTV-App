package com.iptv.online.player.live.tv.smart.AdsPackage.custom;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.LocaleList;
import java.util.Locale;
@SuppressWarnings("all")

/* loaded from: classes3.dex */
public class LocaleHelper {
    public static final String DEFAULT_LANGUAGE = "en";
    public static final String KEY_SELECTED_LANGUAGE = "KEY_SELECTED_LANGUAGE";
    public static final String PREFS_NAME = "AppPrefs";

    public static Context setLocale(Context context) {
        return setLocale(context, getPersistedLanguage(context, DEFAULT_LANGUAGE));
    }

    public static Context setLocale(Context context, String str) {
        if (context == null) {
            return null;
        }
        if (str == null || str.trim().isEmpty()) {
            str = DEFAULT_LANGUAGE;
        }
        persistLanguage(context, str);
        if (context.getApplicationContext() != null && context.getApplicationContext() != context) {
            updateResources(context.getApplicationContext(), str);
        }
        return updateResources(context, str);
    }

    public static String getPersistedLanguage(Context context, String str) {
        return context == null ? str : context.getSharedPreferences("AppPrefs", 0).getString(KEY_SELECTED_LANGUAGE, str);
    }

    public static void persistLanguage(Context context, String str) {
        if (context == null || str == null) {
            return;
        }
        context.getSharedPreferences("AppPrefs", 0).edit().putString(KEY_SELECTED_LANGUAGE, str).commit();
    }

    public static Locale getLocale(String str) {
        if (str == null || str.isEmpty()) {
            return Locale.ENGLISH;
        }
        if ("zh".equalsIgnoreCase(str)) {
            return Locale.SIMPLIFIED_CHINESE;
        }
        if ("id".equalsIgnoreCase(str) || "in".equalsIgnoreCase(str)) {
            return new Locale("id");
        }
        return new Locale(str);
    }

    public static Context updateResources(Context context, String str) {
        if (context == null) {
            return null;
        }
        Locale locale = getLocale(str);
        Locale.setDefault(locale);
        Resources resources = context.getResources();
        Configuration configuration = new Configuration(resources.getConfiguration());
        configuration.setLocale(locale);
        LocaleList localeList = new LocaleList(locale);
        LocaleList.setDefault(localeList);
        configuration.setLocales(localeList);
        configuration.setLayoutDirection(locale);
        try {
            resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        } catch (Exception unused) {
        }
        return context.createConfigurationContext(configuration);
    }
}

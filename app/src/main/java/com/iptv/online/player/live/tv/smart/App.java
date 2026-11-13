package com.iptv.online.player.live.tv.smart;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;
import android.util.Log;
//import com.adsUtils.adsloader.AppOpenLoader;
//import com.adsUtils.adsutil.ReferrerHelper;
//import com.adsUtils.analytics.AnalyticsTracker;
//import com.adsUtils.config.AdsConfig;
//import com.adsUtils.custom.LocaleHelper;
//import com.google.firebase.FirebaseApp;
import com.iptv.online.player.live.tv.smart.AdsPackage.custom.LocaleHelper;
import com.iptv.online.player.live.tv.smart.data.local.PlaylistStorageManager;
//import com.iptv.online.player.live.tv.smart.notification.NotificationHelper;
//import com.microsoft.clarity.Clarity;
//import com.microsoft.clarity.ClarityConfig;

/* loaded from: classes5.dex */
public class App extends Application {
    private static final String TAG = "AppController";
    private static Context context;
    private static App instance;

    public static Context getAppContext() {
        return context;
    }

    public static synchronized App getInstance() {
        App app;
        synchronized (App.class) {
            app = instance;
        }
        return app;
    }

    @Override // android.content.ContextWrapper
    protected void attachBaseContext(Context context2) {
        super.attachBaseContext(com.iptv.online.player.live.tv.smart.AdsPackage.custom.LocaleHelper.setLocale(context2));
    }

    @Override // android.app.Application
    public void onCreate() {
        super.onCreate();
        instance = this;
        context = getApplicationContext();
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().build());
       /* try {
            if (FirebaseApp.getApps(this).isEmpty()) {
                FirebaseApp.initializeApp(this);
            }
            NotificationHelper.init(this);
        } catch (Exception e) {
            Log.e(TAG, "Failed to initialize FirebaseApp or NotificationHelper: " + e.getMessage());
        }
        try {
            AnalyticsTracker.init(this);
            AppOpenLoader.init(this);
            Clarity.initialize(this, new ClarityConfig(AdsConfig.getClarityProjectId()));
            ReferrerHelper.initOneSignal(this);
        } catch (Exception e2) {
            Log.e(TAG, "Failed to initialize AppController: " + e2.getMessage());
        }*/
        try {
            PlaylistStorageManager.applyTheme(new PlaylistStorageManager(this).getThemeMode());
        } catch (Exception unused) {
        }
    }
}

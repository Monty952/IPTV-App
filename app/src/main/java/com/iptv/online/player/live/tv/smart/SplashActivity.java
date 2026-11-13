package com.iptv.online.player.live.tv.smart;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import androidx.activity.OnBackPressedCallback;
import androidx.media3.common.C;
//import com.adsUtils.adsloader.AppOpenLoader;
//import com.adsUtils.adsloader.bannerads.BannerLoader;
//import com.adsUtils.adsloader.bannerads.BannerType;
//import com.adsUtils.adsutil.AdsConsentManager;
//import com.adsUtils.adsutil.AdsUtils;
//import com.adsUtils.adsutil.AppsDataTracker;
//import com.adsUtils.adsutil.InstallReferrerListener;
//import com.adsUtils.adsutil.ReferrerHelper;
//import com.adsUtils.analytics.AnalyticsTracker;
//import com.adsUtils.config.AdsConfig;
//import com.adsUtils.config.ConfigManager;
//import com.adsUtils.config.RemoteConfigListener;
//import com.adsUtils.custom.CustomActivity;
//import com.google.firebase.FirebaseApp;
//import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.iptv.online.player.live.tv.smart.AdsPackage.CustomActivity;
import com.iptv.online.player.live.tv.smart.AdsPackage.config.AdsConfig;
import com.iptv.online.player.live.tv.smart.AdsPackage.config.RemoteConfigListener;
import com.iptv.online.player.live.tv.smart.data.local.PlaylistStorageManager;
import com.iptv.online.player.live.tv.smart.ui.home.HomeActivity;
@SuppressWarnings("all")

/* loaded from: classes5.dex */
public class SplashActivity extends CustomActivity implements RemoteConfigListener {
//    private AdsConsentManager consentManager;
    // New Code
    private static final String ONBOARDING_PREFS = "onboarding_prefs";
    private static final String KEY_COUNTRY_COMPLETED = "country_completed";
    private static final String KEY_LANGUAGE_COMPLETED = "language_completed";
    private static final String KEY_INTRO_COMPLETED = "intro_completed";

    private Runnable runnable;
    private boolean isConsent = false;
    private boolean isConsentCalled = true;
    private boolean isNavigated = false;
    private boolean isDataLoaded = false;
    private boolean isFirebaseStarted = false;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final OnBackPressedCallback backPressedCallback = new OnBackPressedCallback(true) { // from class: com.iptv.online.player.live.tv.smart.SplashActivity.3
        @Override // androidx.activity.OnBackPressedCallback
        public void handleOnBackPressed() {
            SplashActivity.this.finish();
        }
    };

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.adsUtils.custom.CustomActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (!isTaskRoot()) {
            finish();
            return;
        }
        setContentView(R.layout.activity_splash);
        setupSystemBars();
        View findViewById = findViewById(R.id.splash_main);
        if (findViewById != null) {
            enableEdgeToEdge(findViewById);
        }
        try {
           /* if (FirebaseApp.getApps(this).isEmpty()) {
                FirebaseApp.initializeApp(this);
            }
            FirebaseCrashlytics.getInstance().log("SplashActivity initialized");*/
        } catch (Exception unused) {
        }
       /* ReferrerHelper.initInstallReferrer(this, new InstallReferrerListener() { // from class: com.iptv.online.player.live.tv.smart.SplashActivity.1
            @Override // com.adsUtils.adsutil.InstallReferrerListener
            public void ReferrerSetupFinished() {
                SplashActivity.this.loadFirebaseData();
            }

            @Override // com.adsUtils.adsutil.InstallReferrerListener
            public void ReferrerError() {
                SplashActivity.this.loadFirebaseData();
            }
        });*/
        getOnBackPressedDispatcher().addCallback(this, this.backPressedCallback);
        startNextActivity();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadFirebaseData() {
        if (this.isFirebaseStarted) {
            return;
        }
        this.isFirebaseStarted = true;
       /* ConfigManager.registerListener(this);
        ConfigManager.init(this);*/
    }

    @Override // com.adsUtils.config.RemoteConfigListener
    public void onDefaultsReady() {
        Log.d("ConfigDataTag", "Defaults Ready");
    }

    @Override // com.adsUtils.config.RemoteConfigListener
    public void onLiveConfigReady() {
        Log.d("ConfigDataTag", "Live Config Updated");
        if (this.isDataLoaded) {
            return;
        }
        this.isDataLoaded = true;
        handleRemoteConfigLoaded();
    }

    private void handleRemoteConfigLoaded() {
        if (AdsConfig.getIsAppMaintenance()) {
            this.isNavigated = true;
            showMaintenanceDialog();
        } else if (AdsConfig.getIsRedirectOtherAppDialog()) {
            this.isNavigated = true;
            showRedirectDialog();
        } else {
//            AppsDataTracker.initAppsFlyer(this);
//            ReferrerHelper.initOneSignal(this);
            this.isConsent = AdsConfig.getIsConsent();
           /* if (this.consentManager == null) {
                this.consentManager = new AdsConsentManager(this, new Runnable() { // from class: com.iptv.online.player.live.tv.smart.SplashActivity$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        SplashActivity.this.startNextActivity();
                    }
                });
            }*/
            //Recheck this
            SplashActivity.this.startNextActivity();
            try {
                if (this.isConsent) {
                    if (this.isConsentCalled) {
                        this.isConsentCalled = false;
//                        this.consentManager.initializeConsent();
                        return;
                    }
                    return;
                }
//                this.consentManager.initializeAds();
            } catch (Exception e) {
                Log.e("ConsentCheck", "Consent Error : " + e.getMessage());
                if (this.isNavigated) {
                    return;
                }
                startNextActivity();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startNextActivity() {
//        AppOpenLoader.getInstance().setCurrentActivity(this);
//        ReferrerHelper.initAppMetrica(this);
//        ReferrerHelper.initClarity(this);
//        ReferrerHelper.initOneSignal(this);
        View findViewById = findViewById(R.id.llAdsView);
        if (findViewById != null && (findViewById instanceof ViewGroup)) {
//            BannerLoader.showBannerAds(this, (ViewGroup) findViewById, BannerType.FIXED_BANNER);
        }
        Runnable runnable = new Runnable() { // from class: com.iptv.online.player.live.tv.smart.SplashActivity.2
            @Override // java.lang.Runnable
            public void run() {
                if (SplashActivity.this.handler != null && SplashActivity.this.runnable != null) {
                    SplashActivity.this.handler.removeCallbacks(SplashActivity.this.runnable);
                }
                SplashActivity.this.goNext();

               /* try {
                    if (SplashActivity.this.isConsent) {
                        if (SplashActivity.this.consentManager == null || !SplashActivity.this.consentManager.isConsentRequested || !SplashActivity.this.consentManager.isAdmobInitialized) {
                            Log.d("ConsentCheck", "Ads not allowed due to user consent.");
                            SplashActivity.this.goNext();
                        } else if (AdsConfig.getIsUpdateAppDialog() && 1 < AdsConfig.getNewVersionCode()) {
                            SplashActivity.this.showUpdateDialog();
                        } else {
                            SplashActivity.this.showAppOpenAd();
                        }
                    } else if (AdsConfig.getIsUpdateAppDialog() && 1 < AdsConfig.getNewVersionCode()) {
                        SplashActivity.this.showUpdateDialog();
                    } else {
                        SplashActivity.this.showAppOpenAd();
                    }
                } catch (Exception e) {
                    Log.e("ConsentCheck", "Next Error: " + e.getMessage());
                    SplashActivity.this.goNext();
                }*/
            }
        };
        this.runnable = runnable;
        this.handler.postDelayed(runnable, C.DEFAULT_MAX_SEEK_TO_PREVIOUS_POSITION_MS);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showAppOpenAd() {
       /* AppOpenLoader.getInstance().showAppOpenForSplash(new Runnable() { // from class: com.iptv.online.player.live.tv.smart.SplashActivity$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                SplashActivity.this.lambda$showAppOpenAd$0();
            }
        });*/
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showAppOpenAd$0() {
        Log.d("ConsentCheck", "Splash App Open");
        goNext();
    }

    /* JADX INFO: Access modifiers changed from: private */
    // New Code
    private void goNext() {
        if (this.isNavigated || isFinishing() || isDestroyed()) {
            return;
        }
        this.isNavigated = true;
        android.content.SharedPreferences prefs =
                getSharedPreferences(ONBOARDING_PREFS, MODE_PRIVATE);

        boolean countryCompleted =
                prefs.getBoolean(KEY_COUNTRY_COMPLETED, false);

        boolean languageCompleted =
                prefs.getBoolean(KEY_LANGUAGE_COMPLETED, false);

        boolean introCompleted =
                prefs.getBoolean(KEY_INTRO_COMPLETED, false);

        Intent intent;
        if (introCompleted) {
            // User has already completed onboarding.
            intent = new Intent(this, HomeActivity.class);

        } else if (!countryCompleted) {
            // First step: country selection.
            intent = new Intent(this, SelectCountryActivity.class);

        } else if (!languageCompleted) {
            // Second step: language selection.
            intent = new Intent(this, LanguageActivity.class);

        } else {
            // Third step: show the introduction screens.
            intent = new Intent(this, IntroActivity1.class);
        }

        startActivity(intent);
        overridePendingTransition(17432576, 17432577);
        finish();
    }

    // Old Code
    /*public void goNext() {
        if (this.isNavigated || isFinishing() || isDestroyed()) {
            return;
        }
        this.isNavigated = true;
//        AppOpenLoader.getInstance().cancelSplashFlow();
        PlaylistStorageManager playlistStorageManager = new PlaylistStorageManager(this);
        if (AdsConfig.getExtraSharedpreferences() == 1 && playlistStorageManager.hasOpenedBefore()) {
//            AnalyticsTracker.logNavigation("SplashActivity", "HomeActivity");
            startActivity(new Intent(this, HomeActivity.class));
            overridePendingTransition(17432576, 17432577);
            finish();
            return;
        }
//        AnalyticsTracker.logNavigation("SplashActivity", AdsConfig.getAppExtraScreen() ? "SelectCountryActivity" : "LanguageActivity");
        if (AdsConfig.getAppExtraScreen()) {
            startActivity(new Intent(this, SelectCountryActivity.class));
        } else {
            startActivity(new Intent(this, LanguageActivity.class));
        }
        overridePendingTransition(17432576, 17432577);
        finish();
    }*/

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.adsUtils.custom.CustomActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
//        goNext();
        /*if (AdsUtils.isOnlyCustomSplashTabOpen) {
            AdsUtils.isOnlyCustomSplashTabOpen = false;
            goNext();
        }*/
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onStop() {
        Runnable runnable;
        super.onStop();
        Handler handler = this.handler;
        if (handler == null || (runnable = this.runnable) == null) {
            return;
        }
        handler.removeCallbacks(runnable);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.adsUtils.custom.CustomActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        Runnable runnable;
//        ConfigManager.unregisterListener(this);
        Handler handler = this.handler;
        if (handler != null && (runnable = this.runnable) != null) {
            handler.removeCallbacks(runnable);
        }
        super.onDestroy();
    }
}

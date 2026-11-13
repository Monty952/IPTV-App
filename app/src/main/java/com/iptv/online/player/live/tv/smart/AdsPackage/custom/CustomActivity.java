package com.iptv.online.player.live.tv.smart.AdsPackage.custom;

import static android.os.Build.VERSION_CODES_FULL.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.LocaleList;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
//import androidx.media3.exoplayer.upstream.CmcdData;
//import com.adUtils.R;
//import com.adsUtils.adsloader.InterstitialLoader;
//import com.adsUtils.adsloader.RewardLoader;
//import com.adsUtils.adsloader.ScreenAdsLoader;
//import com.adsUtils.adsloader.ScreenTags;
//import com.adsUtils.adsutil.AdsUtils;
//import com.adsUtils.adsutil.NetworkStateReceiver;
//import com.adsUtils.analytics.AnalyticsTracker;
//import com.adsUtils.config.AdsConfig;
//import com.adsUtils.custom.CustomActivity;
//import com.adsUtils.dialog.CustomDialog;
//import com.adsUtils.dialog.DialogFullScreenNativeAd;
import com.iptv.online.player.live.tv.smart.AdsPackage.NetworkStateReceiver;
import com.iptv.online.player.live.tv.smart.AdsPackage.dialog.CustomDialog;

//import io.appmetrica.analytics.coreutils.internal.StringUtils;
import java.util.Locale;
@SuppressWarnings("all")

/* loaded from: classes3.dex */
public class CustomActivity extends AppCompatActivity implements com.iptv.online.player.live.tv.smart.AdsPackage.NetworkStateReceiver.NetworkStateReceiverListener {
    Activity activity;
    OnBackPressedCallback backPressedCallback = new OnBackPressedCallback(true) { // from class: com.adsUtils.custom.CustomActivity.8
        @Override // androidx.activity.OnBackPressedCallback
        public void handleOnBackPressed() {
            CustomActivity.this.setOnBackPressedClick();
        }
    };
    com.iptv.online.player.live.tv.smart.AdsPackage.NetworkStateReceiver networkStateReceiver;
    com.iptv.online.player.live.tv.smart.AdsPackage.dialog.CustomDialog noNetworkDialog;

    /* loaded from: classes3.dex */
    public interface onReplaceFragmentListener {
        void onDismissAds();
    }

    public boolean disableBackPress() {
        return false;
    }

    public boolean statusBarContainColor() {
        return true;
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        View decorView;
        int action = motionEvent.getAction();
        String str = "screen_touch";
        try {
            if (action == 0) {
                decorView = getWindow() != null ? getWindow().getDecorView() : null;
                if (decorView != null) {
                    View findTouchTargetView = findTouchTargetView(decorView, (int) motionEvent.getRawX(), (int) motionEvent.getRawY());
                    if (findTouchTargetView != null) {
//                        str = AnalyticsTracker.extractViewLabel(findTouchTargetView);
                    }
                    if (findTouchTargetView != null) {
                        findTouchTargetView.getClass().getSimpleName();
                    }
//                    AnalyticsTracker.logTouch(getClass().getSimpleName(), "touch_down", str + " (" + ((int) motionEvent.getRawX()) + StringUtils.COMMA + ((int) motionEvent.getRawY()) + ")");
                }
            } else if (action == 1) {
                decorView = getWindow() != null ? getWindow().getDecorView() : null;
                if (decorView != null) {
                    View findTouchTargetView2 = findTouchTargetView(decorView, (int) motionEvent.getRawX(), (int) motionEvent.getRawY());
                    if (findTouchTargetView2 == null) {
//                        AnalyticsTracker.logTouchClick(getClass().getSimpleName(), "screen_touch", "DecorView", motionEvent.getRawX(), motionEvent.getRawY());
//                        AnalyticsTracker.logClick(getClass().getSimpleName(), "screen_touch", "DecorView");
                    } else {
//                        String extractViewLabel = AnalyticsTracker.extractViewLabel(findTouchTargetView2);
                        String simpleName = findTouchTargetView2.getClass().getSimpleName();
//                        AnalyticsTracker.logTouchClick(getClass().getSimpleName(), extractViewLabel, simpleName, motionEvent.getRawX(), motionEvent.getRawY());
//                        AnalyticsTracker.logClick(getClass().getSimpleName(), extractViewLabel, simpleName);
                    }
                }
            }
        } catch (Exception unused) {
        }
        return super.dispatchTouchEvent(motionEvent);
    }

    private View findTouchTargetView(View view, int i, int i2) {
        if (view != null && view.getVisibility() == 0) {
            int[] iArr = new int[2];
            view.getLocationOnScreen(iArr);
            int i3 = iArr[0];
            int i4 = iArr[1];
            int width = view.getWidth() + i3;
            int height = view.getHeight() + i4;
            if (i >= i3 && i <= width && i2 >= i4 && i2 <= height) {
                if (view instanceof ViewGroup) {
                    ViewGroup viewGroup = (ViewGroup) view;
                    for (int childCount = viewGroup.getChildCount() - 1; childCount >= 0; childCount--) {
                        View findTouchTargetView = findTouchTargetView(viewGroup.getChildAt(childCount), i, i2);
                        if (findTouchTargetView != null) {
                            return findTouchTargetView;
                        }
                    }
                }
                return view;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.app.AppCompatActivity, android.app.Activity, android.view.ContextThemeWrapper, android.content.ContextWrapper
    public void attachBaseContext(Context context) {
        super.attachBaseContext(LocaleHelper.setLocale(context));
    }

    @Override // android.view.ContextThemeWrapper
    public void applyOverrideConfiguration(Configuration configuration) {
        String persistedLanguage;
        if (configuration != null && (persistedLanguage = LocaleHelper.getPersistedLanguage(this, LocaleHelper.DEFAULT_LANGUAGE)) != null) {
            Locale locale = LocaleHelper.getLocale(persistedLanguage);
            Locale.setDefault(locale);
            configuration.setLocale(locale);
            configuration.setLocales(new LocaleList(locale));
            configuration.setLayoutDirection(locale);
        }
        super.applyOverrideConfiguration(configuration);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        ThemeHelper.applySavedTheme(this);
        LocaleHelper.updateResources(this, LocaleHelper.getPersistedLanguage(this, LocaleHelper.DEFAULT_LANGUAGE));
        super.onCreate(bundle);
        EdgeToEdge.enable(this);
        getResources().getConfiguration().fontScale = 1.0f;
        this.activity = this;
        boolean z = (getResources().getConfiguration().uiMode & 48) == 32;
        WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView()).setAppearanceLightStatusBars(!z);
        WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView()).setAppearanceLightNavigationBars(!z);
        getOnBackPressedDispatcher().addCallback(this, this.backPressedCallback);
        startNetworkReceiver(this);
//        AnalyticsTracker.logAction(getClass().getSimpleName(), "activity_created", null);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setupSystemBars() {
        boolean isDarkMode = ThemeHelper.isDarkMode(this);
        WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView()).setAppearanceLightStatusBars(!isDarkMode);
        WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView()).setAppearanceLightNavigationBars(!isDarkMode);
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void setContentView(int i) {
        super.setContentView(i);
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void setContentView(View view) {
        super.setContentView(view);
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void setContentView(View view, ViewGroup.LayoutParams layoutParams) {
        super.setContentView(view, layoutParams);
    }

    private void applyEdgeToEdgeInsets() {
        View findViewById = findViewById(16908290);
        if (findViewById != null) {
            enableEdgeToEdge(findViewById);
            ViewCompat.requestApplyInsets(findViewById);
        }
    }

    public void enableEdgeToEdge(View view) {
        ViewCompat.setOnApplyWindowInsetsListener(view, new OnApplyWindowInsetsListener() { // from class: com.adsUtils.custom.CustomActivity$$ExternalSyntheticLambda3
            @Override // androidx.core.view.OnApplyWindowInsetsListener
            public final WindowInsetsCompat onApplyWindowInsets(View view2, WindowInsetsCompat windowInsetsCompat) {
                return CustomActivity.lambda$enableEdgeToEdge$0(view2, windowInsetsCompat);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ WindowInsetsCompat lambda$enableEdgeToEdge$0(View view, WindowInsetsCompat windowInsetsCompat) {
        Insets insets = windowInsetsCompat.getInsets(WindowInsetsCompat.Type.systemBars());
        view.setPadding(insets.left, insets.top, insets.right, insets.bottom);
        return windowInsetsCompat;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void showMaintenanceDialog() {
        if (isFinishing()) {
            return;
        }
        runOnUiThread(new Runnable() { // from class: com.adsUtils.custom.CustomActivity$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                CustomActivity.this.m569x7771c2f2();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$showMaintenanceDialog$1$com-adsUtils-custom-CustomActivity  reason: not valid java name */
    public /* synthetic */ void m569x7771c2f2() {
        CustomDialog customDialog = new CustomDialog(this.activity, new CustomDialog.OnDialogButtonClickListener() { // from class: com.adsUtils.custom.CustomActivity.1
            @Override // com.adsUtils.dialog.CustomDialog.OnDialogButtonClickListener
            public void onClick(View view) {
                CustomActivity.this.finishAffinity();
            }
        });
        customDialog.setDialogTitle("Server under maintenance!");
        customDialog.setDialogMessage("Please try after some time!");
        customDialog.setDialogButtonText("Okay");
        customDialog.show();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void showRedirectDialog() {
        if (isFinishing()) {
            return;
        }
        runOnUiThread(new Runnable() { // from class: com.adsUtils.custom.CustomActivity$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                CustomActivity.this.m571lambda$showRedirectDialog$2$comadsUtilscustomCustomActivity();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$showRedirectDialog$2$com-adsUtils-custom-CustomActivity  reason: not valid java name */
    public /* synthetic */ void m571lambda$showRedirectDialog$2$comadsUtilscustomCustomActivity() {
        CustomDialog customDialog = new CustomDialog(this.activity, new CustomDialog.OnDialogButtonClickListener() { // from class: com.adsUtils.custom.CustomActivity.2
            @Override // com.adsUtils.dialog.CustomDialog.OnDialogButtonClickListener
            public void onClick(View view) {
//                CustomActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(AdsConfig.getNewPackageName())));
                startActivity(new Intent("android.intent.action.VIEW"));
                CustomActivity.this.finishAffinity();
            }
        });
        customDialog.setDialogTitle("Redirect New Application!");
        customDialog.setDialogMessage("Note: if you are facing problem \\nin updating application, kindly \\nre-install it to resolve the issue.");
        customDialog.setDialogButtonText("Install");
        customDialog.show();
    }

    /* JADX INFO: Access modifiers changed from: protected */
/*
    public void showUpdateDialog() {
        if (isFinishing()) {
            return;
        }
        runOnUiThread(new Runnable() { // from class: com.adsUtils.custom.CustomActivity$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                CustomActivity.this.m572lambda$showUpdateDialog$3$comadsUtilscustomCustomActivity();
            }
        });
    }
*/

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$showUpdateDialog$3$com-adsUtils-custom-CustomActivity  reason: not valid java name */
    public /* synthetic */ void m572lambda$showUpdateDialog$3$comadsUtilscustomCustomActivity() {/*
        CustomDialog customDialog = new CustomDialog(this.activity, new CustomDialog.OnDialogButtonClickListener() { // from class: com.adsUtils.custom.CustomActivity.3
            @Override // com.adsUtils.dialog.CustomDialog.OnDialogButtonClickListener
            public void onClick(View view) {
                CustomActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(AdsConfig.getPackageName())));
                CustomActivity.this.finishAffinity();
            }
        });
        customDialog.setDialogTitle(getResources().getString(R.string.update_title));
        customDialog.setDialogMessage(getResources().getString(R.string.update_message));
        customDialog.setDialogButtonText(getResources().getString(R.string.update_btn));
        customDialog.show();*/
    }

    private void showNoNetworkDialog() {
        if (isDestroyed()) {
            return;
        }
        runOnUiThread(new Runnable() { // from class: com.adsUtils.custom.CustomActivity$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                CustomActivity.this.m570lambda$showNoNetworkDialog$4$comadsUtilscustomCustomActivity();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$showNoNetworkDialog$4$com-adsUtils-custom-CustomActivity  reason: not valid java name */
    public /* synthetic */ void m570lambda$showNoNetworkDialog$4$comadsUtilscustomCustomActivity() {
        CustomDialog customDialog = new CustomDialog(this, new CustomDialog.OnDialogButtonClickListener() { // from class: com.adsUtils.custom.CustomActivity.4
            @Override // com.adsUtils.dialog.CustomDialog.OnDialogButtonClickListener
            public void onClick(View view) {
                CustomActivity.this.finishAffinity();
            }
        });
        this.noNetworkDialog = customDialog;
        customDialog.setDialogTitle("No Internet Access!");
        this.noNetworkDialog.setDialogMessage("Please turn on Mobile data or Wifi!");
        this.noNetworkDialog.setDialogButtonText("Okay");
        if (isFinishing() || isDestroyed()) {
            return;
        }
        this.noNetworkDialog.show();
    }

    public void showTabScrollInterstitialAds() {/*
        if (AdsConfig.getIsTabScrollInterstitialEnabled()) {
            InterstitialLoader interstitialLoader = InterstitialLoader.getInstance(this);
            interstitialLoader.isBackInterstitial = false;
            interstitialLoader.isBackCustomInterPlaceHolder = false;
            interstitialLoader.setOnInterstitialCloseListener(new InterstitialLoader.OnInterstitialCloseListener() { // from class: com.adsUtils.custom.CustomActivity.5
                @Override // com.adsUtils.adsloader.InterstitialLoader.OnInterstitialCloseListener
                public void onInterstitialClose() {
                    InterstitialLoader interstitialLoader2 = InterstitialLoader.getInstance(CustomActivity.this);
                    if (interstitialLoader2.isAnyAdReady()) {
                        return;
                    }
                    interstitialLoader2.loadInterstitial(CustomActivity.this);
                }

                @Override // com.adsUtils.adsloader.InterstitialLoader.OnInterstitialCloseListener
                public void onInterstitialCustomShow() {
                    AdsUtils.openCustomTabUrl(CustomActivity.this.activity, AdsConfig.getCustomInterstitialId());
                }
            });
            if (AdsUtils.interTabScrollCounter == AdsConfig.getTabScrollInterstitialCount()) {
                if (interstitialLoader.isAnyAdReady()) {
                    interstitialLoader.showInterstitial(this);
                } else {
                    interstitialLoader.loadInterstitial(this);
                }
                AdsUtils.interTabScrollCounter = 1;
                return;
            }
            AdsUtils.interTabScrollCounter++;
        }*/
    }

    public void openScreen(Intent intent, String str) {
        startActivity(intent);
      /*  if (!AdsConfig.isInterEnabled(str)) {
            startActivity(intent);
        } else {
            startAdsActivity(intent);
        }*/
    }

    public void startAdsActivity(Intent intent) {
        startAdsActivity(intent, false);
    }

    public void startAdsActivity(final Intent intent, final boolean z) {/*
        if (intent == null) {
            return;
        }
        try {
            AnalyticsTracker.logNavigation(getClass().getSimpleName(), intent.getComponent() != null ? intent.getComponent().getClassName() : "unknown");
        } catch (Exception e) {
            Log.e("CustomActivity", "Error logging navigation click event: " + e.getMessage());
        }
        Runnable runnable = new Runnable() { // from class: com.adsUtils.custom.CustomActivity$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                CustomActivity.this.m573lambda$startAdsActivity$5$comadsUtilscustomCustomActivity(intent, z);
            }
        };
        if (!AdsUtils.isNetworkAvailable(this)) {
            Log.w("CustomActBtnClickTag", "network not available");
            runnable.run();
        } else if (!AdsConfig.getIsAdsOn()) {
            Log.w("CustomActBtnClickTag", "ads off");
            runnable.run();
        } else {
            InterstitialLoader interstitialLoader = InterstitialLoader.getInstance(this);
            boolean z2 = false;
            interstitialLoader.isBackInterstitial = false;
            interstitialLoader.isBackCustomInterPlaceHolder = false;
            AdsUtils.isBrowserOpened = false;
            InterstitialLoader.getInstance(this).setOnInterstitialCloseListener(new AnonymousClass6(runnable));
            AdsUtils.interNextClickCounter++;
            AdsUtils.interCustomNextClickCounter++;
            int interstitialClickCount = AdsConfig.getInterstitialClickCount();
            int customInterstitialClickCount = AdsConfig.getCustomInterstitialClickCount();
            if (interstitialClickCount > 0 && AdsUtils.interNextClickCounter % interstitialClickCount == 0) {
                z2 = true;
            }
            if (customInterstitialClickCount > 0 && AdsUtils.interCustomNextClickCounter % customInterstitialClickCount == 0 && !z2) {
                Log.w("CustomActBtnClickTag", "trigger custom interstitial ads link");
                if (!AdsConfig.getIsCustomInterstitialEnabled()) {
                    Log.d("CustomActBtnClickTag", "custom interstitial disable");
                    runnable.run();
                    return;
                }
                Log.d("CustomActBtnClickTag", "show only custom interstitial ads");
                runnable.run();
                InterstitialLoader.getInstance(this).showOnlyCustomInterstitialAds();
            } else if (z2) {
                Log.d("CustomActBtnClickTag", "show network interstitial");
                InterstitialLoader interstitialLoader2 = InterstitialLoader.getInstance(this);
                if (AdsConfig.isOnlyCustomPriority()) {
                    Log.d("CustomActBtnClickTag", "only custom interstitial ads");
                    interstitialLoader2.showInterstitial(this);
                } else if (interstitialLoader2.isAnyAdReady()) {
                    interstitialLoader2.showInterstitial(this);
                } else {
                    Log.d("CustomActBtnClickTag", "interstitial not ready -> direct next");
                    runnable.run();
                    interstitialLoader2.loadInterstitial(this);
                }
            } else {
                Log.d("CustomActBtnClickTag", "direct next, no ad this click");
                runnable.run();
                InterstitialLoader interstitialLoader3 = InterstitialLoader.getInstance(this);
                if (interstitialLoader3.isAnyAdReady()) {
                    return;
                }
                interstitialLoader3.loadInterstitial(this);
            }
        }*/
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$startAdsActivity$5$com-adsUtils-custom-CustomActivity  reason: not valid java name */
    public /* synthetic */ void m573lambda$startAdsActivity$5$comadsUtilscustomCustomActivity(Intent intent, boolean z) {
        try {
            startActivity(intent);
            if (!z || isFinishing() || isDestroyed()) {
                return;
            }
            finish();
        } catch (Exception e) {
            Log.e("CustomActBtnClickTag", "Error launching activity: " + e.getMessage());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.adsUtils.custom.CustomActivity$6  reason: invalid class name */
    /* loaded from: classes3.dex */
/*
    public class AnonymousClass6 implements InterstitialLoader.OnInterstitialCloseListener {
        final */
/* synthetic *//*
 Runnable val$performLaunch;

        AnonymousClass6(Runnable runnable) {
            this.val$performLaunch = runnable;
        }

        @Override // com.adsUtils.adsloader.InterstitialLoader.OnInterstitialCloseListener
        public void onInterstitialClose() {
            Log.d("CustomActBtnClickTag", "close network interstitial: " + CustomActivity.this.activity);
            if (AdsConfig.getIsShowNativeAfterInter()) {
                Log.d("CustomActBtnClickTag", "native after interstitial");
                final DialogFullScreenNativeAd dialogFullScreenNativeAd = new DialogFullScreenNativeAd(CustomActivity.this.activity);
                final Runnable runnable = this.val$performLaunch;
                dialogFullScreenNativeAd.setNativeCloseListener(new View.OnClickListener() { // from class: com.adsUtils.custom.CustomActivity$6$$ExternalSyntheticLambda0
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        AnonymousClass6.lambda$onInterstitialClose$0(DialogFullScreenNativeAd.this, runnable, view);
                    }
                });
                ScreenAdsLoader.startAutoRefresh(CustomActivity.this, dialogFullScreenNativeAd.getNativeAdView(), ScreenTags.NATIVE_FULL_TAG);
            } else {
                Log.d("CustomActBtnClickTag", "native after interstitial off");
                this.val$performLaunch.run();
            }
            InterstitialLoader interstitialLoader = InterstitialLoader.getInstance(CustomActivity.this);
            if (interstitialLoader.isAnyAdReady()) {
                return;
            }
            interstitialLoader.loadInterstitial(CustomActivity.this);
        }

        */
/* JADX INFO: Access modifiers changed from: package-private *//*

        public static */
/* synthetic *//*
 void lambda$onInterstitialClose$0(DialogFullScreenNativeAd dialogFullScreenNativeAd, Runnable runnable, View view) {
            dialogFullScreenNativeAd.dismiss();
            runnable.run();
        }

        @Override // com.adsUtils.adsloader.InterstitialLoader.OnInterstitialCloseListener
        public void onInterstitialCustomShow() {
            Log.d("CustomActBtnClickTag", "show custom interstitial");
            this.val$performLaunch.run();
            boolean customMultiLinkOpenEnable = AdsConfig.getCustomMultiLinkOpenEnable();
            CustomActivity customActivity = CustomActivity.this;
            if (customMultiLinkOpenEnable) {
                AdsUtils.afterInterCustom(customActivity.activity);
            } else {
                AdsUtils.openCustomTabUrl(customActivity.activity, AdsConfig.getCustomInterstitialId());
            }
        }
    }
*/

    public void replaceBtnFragment(onReplaceFragmentListener onreplacefragmentlistener) {/*
        if (!AdsUtils.isNetworkAvailable(this)) {
            Log.w("ReplaceFragTag", "network not available");
            onreplacefragmentlistener.onDismissAds();
        } else if (!AdsConfig.getIsAdsOn()) {
            Log.w("ReplaceFragTag", "ads off");
            onreplacefragmentlistener.onDismissAds();
        } else {
            boolean z = false;
            InterstitialLoader.getInstance(this).isBackInterstitial = false;
            AdsUtils.isBrowserOpened = false;
            InterstitialLoader.getInstance(this).setOnInterstitialCloseListener(new AnonymousClass7(onreplacefragmentlistener));
            AdsUtils.interNextClickCounter++;
            AdsUtils.interCustomNextClickCounter++;
            int interstitialClickCount = AdsConfig.getInterstitialClickCount();
            int customInterstitialClickCount = AdsConfig.getCustomInterstitialClickCount();
            if (interstitialClickCount > 0 && AdsUtils.interNextClickCounter % interstitialClickCount == 0) {
                z = true;
            }
            if (customInterstitialClickCount > 0 && AdsUtils.interCustomNextClickCounter % customInterstitialClickCount == 0 && !z) {
                Log.w("ReplaceFragTag", "trigger custom interstitial ads link");
                if (!AdsConfig.getIsCustomInterstitialEnabled()) {
                    Log.d("ReplaceFragTag", "custom interstitial disable");
                    onreplacefragmentlistener.onDismissAds();
                    return;
                }
                Log.d("ReplaceFragTag", "show only custom interstitial ads");
                InterstitialLoader.getInstance(this).showOnlyCustomInterstitialAds();
            } else if (z) {
                Log.d("ReplaceFragTag", "show network interstitial");
                InterstitialLoader interstitialLoader = InterstitialLoader.getInstance(this);
                if (AdsConfig.isOnlyCustomPriority()) {
                    Log.d("ReplaceFragTag", "only custom interstitial ads");
                    interstitialLoader.showInterstitial(this);
                } else if (interstitialLoader.isAnyAdReady()) {
                    interstitialLoader.showInterstitial(this);
                } else {
                    Log.d("ReplaceFragTag", "interstitial not ready -> direct next");
                    onreplacefragmentlistener.onDismissAds();
                    interstitialLoader.loadInterstitial(this);
                }
            } else {
                Log.d("ReplaceFragTag", "direct next, no ad this click");
                onreplacefragmentlistener.onDismissAds();
                InterstitialLoader interstitialLoader2 = InterstitialLoader.getInstance(this);
                if (interstitialLoader2.isAnyAdReady()) {
                    return;
                }
                interstitialLoader2.loadInterstitial(this);
            }
        }*/
    }

    /* renamed from: com.adsUtils.custom.CustomActivity$7  reason: invalid class name */
    /* loaded from: classes3.dex */
   /* class AnonymousClass7 implements InterstitialLoader.OnInterstitialCloseListener {
        final *//* synthetic *//* onReplaceFragmentListener val$listener;

        AnonymousClass7(onReplaceFragmentListener onreplacefragmentlistener) {
            this.val$listener = onreplacefragmentlistener;
        }

        @Override // com.adsUtils.adsloader.InterstitialLoader.OnInterstitialCloseListener
        public void onInterstitialClose() {
            Log.d("ReplaceFragTag", "close network interstitial: " + CustomActivity.this.activity);
            boolean customMultiLinkOpenEnable = AdsConfig.getCustomMultiLinkOpenEnable();
            CustomActivity customActivity = CustomActivity.this;
            if (customMultiLinkOpenEnable) {
                AdsUtils.afterInterCustom(customActivity.activity);
            } else {
                AdsUtils.openCustomTabUrl(customActivity.activity, AdsConfig.getCustomInterstitialId());
            }
            if (AdsConfig.getIsShowNativeAfterInter()) {
                Log.d("ReplaceFragTag", "native after interstitial");
                final DialogFullScreenNativeAd dialogFullScreenNativeAd = new DialogFullScreenNativeAd(CustomActivity.this.activity);
                final onReplaceFragmentListener onreplacefragmentlistener = this.val$listener;
                dialogFullScreenNativeAd.setNativeCloseListener(new View.OnClickListener() { // from class: com.adsUtils.custom.CustomActivity$7$$ExternalSyntheticLambda0
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        AnonymousClass7.lambda$onInterstitialClose$0(DialogFullScreenNativeAd.this, onreplacefragmentlistener, view);
                    }
                });
                ScreenAdsLoader.startAutoRefresh(CustomActivity.this, dialogFullScreenNativeAd.getNativeAdView(), ScreenTags.NATIVE_FULL_TAG);
            } else {
                Log.d("ReplaceFragTag", "native after interstitial off");
                this.val$listener.onDismissAds();
            }
            InterstitialLoader interstitialLoader = InterstitialLoader.getInstance(CustomActivity.this);
            if (interstitialLoader.isAnyAdReady()) {
                return;
            }
            interstitialLoader.loadInterstitial(CustomActivity.this);
        }

        *//* JADX INFO: Access modifiers changed from: package-private *//*
        public static *//* synthetic *//* void lambda$onInterstitialClose$0(DialogFullScreenNativeAd dialogFullScreenNativeAd, onReplaceFragmentListener onreplacefragmentlistener, View view) {
            dialogFullScreenNativeAd.dismiss();
            onreplacefragmentlistener.onDismissAds();
        }

        @Override // com.adsUtils.adsloader.InterstitialLoader.OnInterstitialCloseListener
        public void onInterstitialCustomShow() {
            Log.d("ReplaceFragTag", "show custom interstitial");
            this.val$listener.onDismissAds();
            boolean customMultiLinkOpenEnable = AdsConfig.getCustomMultiLinkOpenEnable();
            CustomActivity customActivity = CustomActivity.this;
            if (customMultiLinkOpenEnable) {
                AdsUtils.afterInterCustom(customActivity.activity);
            } else {
                AdsUtils.openCustomTabUrl(customActivity.activity, AdsConfig.getCustomInterstitialId());
            }
        }
    }*/

    public void setOnBackPressedClick() {/*
        AnalyticsTracker.logBackPress(getClass().getSimpleName());
        if (disableBackPress()) {
            Log.w("CustomActBackClickTag", "disable back press");
        } else if (!AdsUtils.isNetworkAvailable(this)) {
            Log.w("CustomActBackClickTag", "network not available");
            finish();
        } else if (!AdsConfig.getIsAdsOn()) {
            Log.w("CustomActBackClickTag", "ads off");
            finish();
        } else if (!AdsConfig.getIsInterstitialOnBack()) {
            Log.w("CustomActBackClickTag", "back interstitial off");
            finish();
        } else {
            InterstitialLoader.getInstance(this).setBackPressedAds(new AnonymousClass9());
            boolean z = true;
            AdsUtils.interBackClickCounter++;
            AdsUtils.interCustomBackClickCounter++;
            int interstitialBackCount = AdsConfig.getInterstitialBackCount();
            int customInterstitialClickCountBack = AdsConfig.getCustomInterstitialClickCountBack();
            z = (interstitialBackCount <= 0 || AdsUtils.interBackClickCounter % interstitialBackCount != 0) ? false : false;
            if (AdsConfig.getIsCustomInterstitialOnBack() && customInterstitialClickCountBack > 0 && AdsUtils.interCustomBackClickCounter % customInterstitialClickCountBack == 0 && !z) {
                Log.w("CustomActBackClickTag", "Back Custom Click Counter: " + AdsUtils.interCustomBackClickCounter);
                Log.w("CustomActBackClickTag", "Back Total Custom Interstitial Next: " + AdsConfig.getCustomInterstitialClickCountBack());
                if (!AdsConfig.getIsCustomInterstitialEnabled()) {
                    Log.d("CustomActBackClickTag", "Back Show Interstitial");
                    finish();
                    return;
                }
                Log.d("CustomActBackClickTag", "Show Custom Interstitial");
                InterstitialLoader.getInstance(this).showOnlyCustomInterstitialAds();
                finish();
            } else if (z) {
                InterstitialLoader interstitialLoader = InterstitialLoader.getInstance(this);
                if (AdsConfig.isOnlyCustomPriority()) {
                    Log.d("CustomActBackClickTag", "only custom interstitial ads");
                    interstitialLoader.showInterstitial(this);
                } else if (interstitialLoader.isAnyAdReady()) {
                    interstitialLoader.showInterstitial(this);
                } else {
                    Log.d("CustomActBackClickTag", "interstitial not ready -> direct next");
                    finish();
                    interstitialLoader.loadInterstitial(this);
                }
            } else {
                finish();
                InterstitialLoader interstitialLoader2 = InterstitialLoader.getInstance(this);
                if (interstitialLoader2.isAnyAdReady()) {
                    return;
                }
                interstitialLoader2.loadInterstitial(this);
            }
        }*/
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.adsUtils.custom.CustomActivity$9  reason: invalid class name */
    /* loaded from: classes3.dex */
   /* public class AnonymousClass9 implements InterstitialLoader.OnInterstitialBackCloseListener {
        AnonymousClass9() {
        }

        @Override // com.adsUtils.adsloader.InterstitialLoader.OnInterstitialBackCloseListener
        public void onInterstitialBackClose() {
            Log.d("CustomActBackClickTag", "Inter Back Close");
            if (AdsConfig.getIsAdsOn() && AdsConfig.getIsShowNativeAfterInter()) {
                final DialogFullScreenNativeAd dialogFullScreenNativeAd = new DialogFullScreenNativeAd(CustomActivity.this.activity);
                dialogFullScreenNativeAd.setNativeCloseListener(new View.OnClickListener() { // from class: com.adsUtils.custom.CustomActivity$9$$ExternalSyntheticLambda0
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        AnonymousClass9.this.m574xbe466000(dialogFullScreenNativeAd, view);
                    }
                });
                ScreenAdsLoader.startAutoRefresh(CustomActivity.this, dialogFullScreenNativeAd.getNativeAdView(), ScreenTags.NATIVE_FULL_TAG);
            } else {
                CustomActivity.this.finish();
            }
            InterstitialLoader interstitialLoader = InterstitialLoader.getInstance(CustomActivity.this);
            if (interstitialLoader.isAnyAdReady()) {
                return;
            }
            interstitialLoader.loadInterstitial(CustomActivity.this);
        }

         JADX INFO: Access modifiers changed from: package-private
         renamed from: lambda$onInterstitialBackClose$0$com-adsUtils-custom-CustomActivity$9  reason: not valid java name
        public  synthetic  void m574xbe466000(DialogFullScreenNativeAd dialogFullScreenNativeAd, View view) {
            dialogFullScreenNativeAd.dismiss();
            CustomActivity.this.finish();
        }

        @Override // com.adsUtils.adsloader.InterstitialLoader.OnInterstitialBackCloseListener
        public void onCustomInterstitialBackClose() {
            Log.d("CustomActBackClickTag", "Custom Interstitial Back Close");
            CustomActivity.this.finish();
            AdsUtils.openCustomTabUrl(CustomActivity.this.activity, AdsConfig.getBackCustomInterstitialId());
        }
    }*/

    public void skipBackInterstitial() {
//        AdsUtils.interBackClickCounter = 0;
    }

    @Override // com.adsUtils.adsutil.NetworkStateReceiver.NetworkStateReceiverListener
    public void isNetworkOn() {
        CustomDialog customDialog = this.noNetworkDialog;
        if (customDialog != null) {
            customDialog.dismiss();
        }
    }

    @Override // com.adsUtils.adsutil.NetworkStateReceiver.NetworkStateReceiverListener
    public void isNetworkOff() {
        isNotNeedInternet();
      /*  if (AdsConfig.getIsNeedInternet()) {
            showNoNetworkDialog();
        } else {
            isNotNeedInternet();
        }*/
    }

    public void isNotNeedInternet() {
        CustomDialog customDialog = this.noNetworkDialog;
        if (customDialog != null) {
            customDialog.dismiss();
        }
    }

    public void startNetworkReceiver(Context context) {
        NetworkStateReceiver networkStateReceiver = new NetworkStateReceiver();
        this.networkStateReceiver = networkStateReceiver;
        networkStateReceiver.addListener((NetworkStateReceiver.NetworkStateReceiverListener) context);
        registerNetworkReceiver();
    }

    public void registerNetworkReceiver() {
        registerReceiver(this.networkStateReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
    }

    public void unregisterNetworkReceiver() {
        try {
            unregisterReceiver(this.networkStateReceiver);
        } catch (Exception unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        registerNetworkReceiver();
        super.onResume();
      /*  AnalyticsTracker.logScreen(getClass().getSimpleName(), this);
        AnalyticsTracker.logAction(getClass().getSimpleName(), "activity_resumed", null);
        RewardLoader.getInstance().tryShowPendingDialog(this);
        if (AdsUtils.isBrowserOpened) {
            RewardLoader.getInstance().pauseTimer(this);
            runOnUiThread(new Runnable() { // from class: com.adsUtils.custom.CustomActivity$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    RewardLoader.getInstance().dialogCustomReward.getLeftTimeTextView().setText("Time left: " + (RewardLoader.getInstance().timeLeftInMillis / 1000) + CmcdData.Factory.STREAMING_FORMAT_SS);
                }
            });
        }*/
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onPause() {
        unregisterNetworkReceiver();
        super.onPause();
      /*  AnalyticsTracker.logAction(getClass().getSimpleName(), "activity_paused", null);
        if (AdsUtils.isBrowserOpened) {
            RewardLoader.getInstance().resumeTimer(this);
        }*/
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
       /* AnalyticsTracker.logAction(getClass().getSimpleName(), "activity_destroyed", null);
        InterstitialLoader.getInstance(this).cancelPendingInterstitial();
        if (RewardLoader.getInstance() != null) {
            RewardLoader.getInstance().destroy();
        }*/
        super.onDestroy();
    }
}

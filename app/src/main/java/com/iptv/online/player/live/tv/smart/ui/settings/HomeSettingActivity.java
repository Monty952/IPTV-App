package com.iptv.online.player.live.tv.smart.ui.settings;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.media3.common.MimeTypes;
//import androidx.webkit.internal.AssetHelper;
//import com.adsUtils.adsloader.nativeads.NativeLoader;
//import com.adsUtils.adsloader.nativeads.NativeType;
//import com.adsUtils.analytics.AnalyticsTracker;
//import com.adsUtils.config.AdsConfig;
//import com.adsUtils.custom.CustomActivity;
//import com.facebook.appevents.internal.ViewHierarchyConstants;
import com.iptv.online.player.live.tv.smart.AdsPackage.CustomActivity;
import com.iptv.online.player.live.tv.smart.LanguageActivity;
import com.iptv.online.player.live.tv.smart.R;
import com.iptv.online.player.live.tv.smart.data.local.PlaylistStorageManager;
import java.io.File;
import java.io.FileOutputStream;
@SuppressWarnings("all")

/* loaded from: classes5.dex */
public class HomeSettingActivity extends com.iptv.online.player.live.tv.smart.AdsPackage.CustomActivity {
    private PlaylistStorageManager storageManager;
    private TextView tvCurrentLanguageSubtitle;
    private TextView tvCurrentThemeSubtitle;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.adsUtils.custom.CustomActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_home_setting);
//        NativeLoader.showNativeAds(this, (LinearLayout) findViewById(R.id.llAdsView), NativeType.BIG_NATIVE);
        this.storageManager = new PlaylistStorageManager(this);
        initViews();
        updateSubtitles();
        setupListeners();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.adsUtils.custom.CustomActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        updateSubtitles();
    }

    private void initViews() {
        this.tvCurrentThemeSubtitle = (TextView) findViewById(R.id.tvCurrentThemeSubtitle);
        this.tvCurrentLanguageSubtitle = (TextView) findViewById(R.id.tvCurrentLanguageSubtitle);
        findViewById(R.id.btnSettingBack).setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.settings.HomeSettingActivity$$ExternalSyntheticLambda2
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                HomeSettingActivity.this.lambda$initViews$0(view);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initViews$0(View view) {
        setOnBackPressedClick();
    }

    private void updateSubtitles() {
        int themeMode = this.storageManager.getThemeMode();
        TextView textView = this.tvCurrentThemeSubtitle;
        if (themeMode == 2) {
            textView.setText(R.string.theme_dark);
        } else {
            textView.setText(R.string.theme_light_default);
        }
        this.tvCurrentLanguageSubtitle.setText(getLanguageDisplayName(this.storageManager.getAppLanguage()));
    }

    private String getLanguageDisplayName(String str) {
        return "gu".equalsIgnoreCase(str) ? "ગુજરાતી" : "hi".equalsIgnoreCase(str) ? "हिंदी" : "de".equalsIgnoreCase(str) ? "Deutsch" : "fr".equalsIgnoreCase(str) ? "Français" : "it".equalsIgnoreCase(str) ? "Italiano" : "pt".equalsIgnoreCase(str) ? "Português" : "tr".equalsIgnoreCase(str) ? "Türkçe" : "English";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupListeners$1(View view) {
        showThemeModeDialog();
    }

    private void setupListeners() {
        findViewById(R.id.settingRowTheme).setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.settings.HomeSettingActivity$$ExternalSyntheticLambda3
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                HomeSettingActivity.this.lambda$setupListeners$1(view);
            }
        });
        findViewById(R.id.settingRowLanguage).setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.settings.HomeSettingActivity$$ExternalSyntheticLambda4
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                HomeSettingActivity.this.lambda$setupListeners$2(view);
            }
        });
        findViewById(R.id.settingRowRate).setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.settings.HomeSettingActivity$$ExternalSyntheticLambda5
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                HomeSettingActivity.this.lambda$setupListeners$3(view);
            }
        });
        findViewById(R.id.settingRowShare).setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.settings.HomeSettingActivity$$ExternalSyntheticLambda6
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                HomeSettingActivity.this.lambda$setupListeners$4(view);
            }
        });
        findViewById(R.id.settingRowPrivacy).setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.settings.HomeSettingActivity$$ExternalSyntheticLambda7
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                HomeSettingActivity.this.lambda$setupListeners$5(view);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupListeners$2(View view) {
        Intent intent = new Intent(this, LanguageActivity.class);
        intent.putExtra("EXTRA_FROM_SETTINGS", true);
//        startAdsActivity(intent);
        startActivity(intent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupListeners$3(View view) {
        openPlayStoreForRating();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupListeners$4(View view) {
        shareApp();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupListeners$5(View view) {
        showPrivacyPolicyDialog();
    }

    private void showThemeModeDialog() {
        final AlertDialog create = new AlertDialog.Builder(this, R.style.CustomDialogTheme).setTitle(R.string.select_theme_mode).setSingleChoiceItems(new String[]{getString(R.string.theme_option_light), getString(R.string.theme_option_dark)}, this.storageManager.getThemeMode() == 2 ? 1 : 0, new DialogInterface.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.settings.HomeSettingActivity$$ExternalSyntheticLambda0
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                HomeSettingActivity.this.lambda$showThemeModeDialog$6(dialogInterface, i);
            }
        }).setNegativeButton(R.string.cancel, (DialogInterface.OnClickListener) null).create();
        create.setOnShowListener(new DialogInterface.OnShowListener() { // from class: com.iptv.online.player.live.tv.smart.ui.settings.HomeSettingActivity$$ExternalSyntheticLambda1
            @Override // android.content.DialogInterface.OnShowListener
            public final void onShow(DialogInterface dialogInterface) {
                HomeSettingActivity.this.lambda$showThemeModeDialog$7(create, dialogInterface);
            }
        });
        create.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showThemeModeDialog$6(DialogInterface dialogInterface, int i) {
        dialogInterface.dismiss();
        int i2 = i == 1 ? 2 : 1;
        this.storageManager.setThemeMode(i2);
//        AnalyticsTracker.logSettingsChange("HomeSettingActivity", "theme", i2 == 2 ? "dark" : "light");
        updateSubtitles();
        recreate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showThemeModeDialog$7(AlertDialog alertDialog, DialogInterface dialogInterface) {
        Button button = alertDialog.getButton(-2);
        if (button != null) {
            button.setTextColor(ContextCompat.getColor(this, R.color.text_secondary));
        }
    }

    private void openPlayStoreForRating() {
//        AnalyticsTracker.logAction("HomeSettingActivity", "rate_us", "open_playstore");
        String packageName = getPackageName();
        try {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + packageName)));
        } catch (ActivityNotFoundException unused) {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)));
        }
    }

    private void shareApp() {
//        AnalyticsTracker.logAction("HomeSettingActivity", "share_app", "share");
        String str = "Check out IPTV Player Pro! Stream Live TV, M3U playlists, and Xtream Codes with super fast HD playback.\n\nDownload now: https://play.google.com/store/apps/details?id=" + getPackageName();
        try {
            File file = new File(getCacheDir(), "images");
            if (!file.exists()) {
                file.mkdirs();
            }
            File file2 = new File(file, "banner.png");
            Bitmap decodeResource = BitmapFactory.decodeResource(getResources(), R.drawable.banner);
            if (decodeResource != null) {
                FileOutputStream fileOutputStream = new FileOutputStream(file2);
                decodeResource.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                fileOutputStream.close();
                Uri uriForFile = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", file2);
                Intent intent = new Intent("android.intent.action.SEND");
                intent.setType(MimeTypes.IMAGE_PNG);
                intent.putExtra("android.intent.extra.SUBJECT", "IPTV Player Pro");
                intent.putExtra("android.intent.extra.TEXT", str);
                intent.putExtra("android.intent.extra.STREAM", uriForFile);
                intent.addFlags(1);
                startActivity(Intent.createChooser(intent, "Share IPTV Player Pro via"));
                return;
            }
        } catch (Exception unused) {
        }
        Intent intent2 = new Intent("android.intent.action.SEND");
        intent2.setType("text/plain");
        intent2.putExtra("android.intent.extra.SUBJECT", getString(R.string.app_name));
        intent2.putExtra("android.intent.extra.TEXT", str);
        startActivity(Intent.createChooser(intent2, getString(R.string.share_chooser_title)));
    }

    private void showPrivacyPolicyDialog() {
//        AnalyticsTracker.logAction("HomeSettingActivity", "privacy_policy", ViewHierarchyConstants.VIEW_KEY);
        try {
//            startActivity(new Intent("android.intent.action.VIEW", Uri.parse(getPrivacyPolicy())));
            startActivity(new Intent("android.intent.action.VIEW"));
        } catch (ActivityNotFoundException unused) {
//            startActivity(new Intent("android.intent.action.VIEW", Uri.parse(AdsConfig.getPrivacyPolicy())));
            startActivity(new Intent("android.intent.action.VIEW"));
        }
    }
}

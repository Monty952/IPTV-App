package com.iptv.online.player.live.tv.smart;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.core.graphics.Insets;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
//import com.adsUtils.adsloader.nativeads.NativeLoader;
//import com.adsUtils.adsloader.nativeads.NativeType;
//import com.adsUtils.analytics.AnalyticsTracker;
//import com.adsUtils.config.AdsConfig;
//import com.adsUtils.custom.CustomActivity;
//import com.adsUtils.custom.LocaleHelper;
import com.iptv.online.player.live.tv.smart.AdsPackage.CustomActivity;
import com.iptv.online.player.live.tv.smart.AdsPackage.config.AdsConfig;
import com.iptv.online.player.live.tv.smart.AdsPackage.custom.LocaleHelper;
import com.iptv.online.player.live.tv.smart.data.local.PlaylistStorageManager;
import com.iptv.online.player.live.tv.smart.ui.home.HomeActivity;
@SuppressWarnings("all")

/* loaded from: classes5.dex */
public class LanguageActivity extends com.iptv.online.player.live.tv.smart.AdsPackage.CustomActivity {
    public static final String EXTRA_FROM_SETTINGS = "EXTRA_FROM_SETTINGS";
    private ImageView btnCheckmark;
    private View[] languageItems;
    private LinearLayout languageListContainer;
    private ProgressBar progressBarCheck;
    private int selectedIndex = -1;
    private final String[] languages = {"English", "ગુજરાતી", "हिंदी", "Deutsch", "Français", "Italiano", "Português", "Türkçe"};
    private final String[] languageCodes = {com.iptv.online.player.live.tv.smart.AdsPackage.custom.LocaleHelper.DEFAULT_LANGUAGE, "gu", "hi", "de", "fr", "it", "pt", "tr"};
    private boolean isFromSettings = false;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.adsUtils.custom.CustomActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.isFromSettings = getIntent().getBooleanExtra("EXTRA_FROM_SETTINGS", false);
        setupSystemBars();
        setContentView(R.layout.activity_language);
        ImageView imageView = (ImageView) findViewById(R.id.btnLanguageBack);
        if (imageView != null) {
            imageView.setVisibility(this.isFromSettings ? 0 : 8);
            imageView.setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.LanguageActivity$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    LanguageActivity.this.lambda$onCreate$0(view);
                }
            });
        }
//        NativeLoader.showNativeAds(this, (LinearLayout) findViewById(R.id.llAdsView), NativeType.BIG_NATIVE);
        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.topBar),
                new OnApplyWindowInsetsListener() {
                    @Override
                    public WindowInsetsCompat onApplyWindowInsets(
                            View view,
                            WindowInsetsCompat windowInsetsCompat) {
                        view.setPadding(
                                view.getPaddingLeft(),
                                windowInsetsCompat.getInsets(
                                        WindowInsetsCompat.Type.systemBars()
                                ).top + 16,
                                view.getPaddingRight(),
                                view.getPaddingBottom()
                        );
                        return windowInsetsCompat;
                    }
                }
        );
     /*   ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.topBar), new OnApplyWindowInsetsListener() { // from class: com.iptv.online.player.live.tv.smart.LanguageActivity$$ExternalSyntheticLambda1
            @Override // androidx.core.view.OnApplyWindowInsetsListener
            public final WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat) {
                return view.setPadding(view.getPaddingLeft(), windowInsetsCompat.getInsets(WindowInsetsCompat.Type.systemBars()).top + 16, view.getPaddingRight(), view.getPaddingBottom());
            }
        });*/
        ViewCompat.setOnApplyWindowInsetsListener((ScrollView) findViewById(R.id.scrollView), new OnApplyWindowInsetsListener() { // from class: com.iptv.online.player.live.tv.smart.LanguageActivity$$ExternalSyntheticLambda2
            @Override // androidx.core.view.OnApplyWindowInsetsListener
            public final WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat) {
                return LanguageActivity.lambda$onCreate$2(view, windowInsetsCompat);
            }
        });
        this.languageListContainer = (LinearLayout) findViewById(R.id.languageListContainer);
        this.progressBarCheck = (ProgressBar) findViewById(R.id.progressBarCheck);
        this.btnCheckmark = (ImageView) findViewById(R.id.btnCheckmark);
        this.progressBarCheck.setVisibility(View.GONE);
        this.btnCheckmark.setVisibility(View.GONE);
        String persistedLanguage = LocaleHelper.getPersistedLanguage(this, LocaleHelper.DEFAULT_LANGUAGE);
        int i = 0;
        while (true) {
            String[] strArr = this.languageCodes;
            if (i >= strArr.length) {
                break;
            } else if (strArr[i].equalsIgnoreCase(persistedLanguage)) {
                this.selectedIndex = i;
                break;
            } else {
                i++;
            }
        }
        if (this.selectedIndex == -1) {
            this.selectedIndex = 0;
        }
        buildLanguageList();
        this.btnCheckmark.setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.LanguageActivity$$ExternalSyntheticLambda3
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                LanguageActivity.this.lambda$onCreate$3(view);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$0(View view) {
        setOnBackPressedClick();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ WindowInsetsCompat lambda$onCreate$2(View view, WindowInsetsCompat windowInsetsCompat) {
        Insets insets = windowInsetsCompat.getInsets(WindowInsetsCompat.Type.systemBars());
        view.setPadding(view.getPaddingLeft(), insets.top + 16, view.getPaddingRight(), insets.bottom + 20);
        return windowInsetsCompat;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$3(View view) {
        int i = this.selectedIndex;
        if (i >= 0) {
            String[] strArr = this.languageCodes;
            if (i < strArr.length) {
                String str = strArr[i];
//                AnalyticsTracker.logSelection("LanguageActivity", "language", this.languages[i] + " (" + str + ")");
                LocaleHelper.setLocale(this, str);
                new PlaylistStorageManager(this).setAppLanguage(str);
            }
        }
        if (this.isFromSettings) {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.addFlags(268468224);
            startActivity(intent);
//            startAdsActivity(intent, true);
        } else if (AdsConfig.getSkipIntro()) {
            startActivity(new Intent(this, HomeActivity.class));
//            startAdsActivity(new Intent(this, HomeActivity.class));
        } else if (AdsConfig.getAppExtraScreen()) {
            startActivity(new Intent(this, SelectGenderActivity.class));
//            startAdsActivity(new Intent(this, SelectGenderActivity.class), true);
        } else {
            startActivity(new Intent(this, IntroActivity1.class));
//            startAdsActivity(new Intent(this, IntroActivity1.class), true);
        }
        overridePendingTransition(17432576, 17432577);
    }

    private void buildLanguageList() {
        this.languageItems = new View[this.languages.length];
        LayoutInflater from = LayoutInflater.from(this);
        for (int i = 0; i < this.languages.length; i++) {
            View inflate = from.inflate(R.layout.item_language, (ViewGroup) this.languageListContainer, false);
            ((TextView) inflate.findViewById(R.id.tvLanguageName)).setText(this.languages[i]);
            View findViewById = inflate.findViewById(R.id.radioIndicator);
            if (i == this.selectedIndex) {
                inflate.setBackgroundResource(R.drawable.bg_language_item_selected);
                findViewById.setBackgroundResource(R.drawable.radio_selected);
            } else {
                inflate.setBackgroundResource(R.drawable.bg_language_item_unselected);
                findViewById.setBackgroundResource(R.drawable.radio_unselected);
            }
            int finalI = i;
            inflate.setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.LanguageActivity$$ExternalSyntheticLambda5
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    LanguageActivity.this.lambda$buildLanguageList$4(finalI, view);
                }
            });
            this.languageListContainer.addView(inflate);
            this.languageItems[i] = inflate;
        }
        if (this.selectedIndex >= 0) {
            this.btnCheckmark.setVisibility(View.VISIBLE);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$buildLanguageList$4(int i, View view) {
        onLanguageSelected(i);
    }

    private void onLanguageSelected(int i) {
        if (this.selectedIndex == i) {
            return;
        }
        this.selectedIndex = i;
//        AnalyticsTracker.logClick("LanguageActivity", "language_item", this.languages[i]);
        updateItemSelection(this.selectedIndex);
        showProgressThenCheckmark();
    }

    private void updateItemSelection(int i) {
        int i2 = 0;
        while (true) {
            View[] viewArr = this.languageItems;
            if (i2 >= viewArr.length) {
                return;
            }
            View view = viewArr[i2];
            View findViewById = view.findViewById(R.id.radioIndicator);
            if (i2 == i) {
                view.setBackgroundResource(R.drawable.bg_language_item_selected);
                findViewById.setBackgroundResource(R.drawable.radio_selected);
            } else {
                view.setBackgroundResource(R.drawable.bg_language_item_unselected);
                findViewById.setBackgroundResource(R.drawable.radio_unselected);
            }
            i2++;
        }
    }

    private void showProgressThenCheckmark() {
        this.btnCheckmark.setVisibility(View.GONE);
        this.progressBarCheck.setVisibility(View.VISIBLE);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() { // from class: com.iptv.online.player.live.tv.smart.LanguageActivity$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                LanguageActivity.this.lambda$showProgressThenCheckmark$5();
            }
        }, 1000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showProgressThenCheckmark$5() {
        this.progressBarCheck.setVisibility(View.GONE);
        this.btnCheckmark.setVisibility(View.VISIBLE);
        this.btnCheckmark.setScaleX(0.0f);
        this.btnCheckmark.setScaleY(0.0f);
        this.btnCheckmark.animate().scaleX(1.0f).scaleY(1.0f).setDuration(200L).start();
    }
}

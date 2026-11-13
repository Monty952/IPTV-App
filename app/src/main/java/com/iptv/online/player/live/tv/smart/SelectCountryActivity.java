package com.iptv.online.player.live.tv.smart;

import android.content.Context;
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
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
//import com.adsUtils.adsloader.nativeads.NativeLoader;
//import com.adsUtils.adsloader.nativeads.NativeType;
//import com.adsUtils.analytics.AnalyticsTracker;
//import com.adsUtils.custom.CustomActivity;
//import com.facebook.appevents.UserDataStore;
import com.iptv.online.player.live.tv.smart.AdsPackage.CustomActivity;
import com.iptv.online.player.live.tv.smart.ui.home.HomeActivity;

/* loaded from: classes5.dex */
public class SelectCountryActivity extends com.iptv.online.player.live.tv.smart.AdsPackage.CustomActivity {
    public static final String EXTRA_FROM_SETTINGS = "EXTRA_FROM_SETTINGS";
    private static final String KEY_SAVED_COUNTRY = "saved_country_code";
    private static final String PREFS_NAME = "app_country_prefs";
    private ImageView btnCheckmark;
    private View[] countryItems;
    private LinearLayout countryListContainer;
    private ProgressBar progressBarCheck;
    private int selectedIndex = -1;
    private final String[] countryNames = {"India", "United States", "United Kingdom", "Germany", "France", "Canada", "Australia", "Other"};
    private final String[] countryFlags = {"🇮🇳", "🇺🇸", "🇬🇧", "🇩🇪", "🇫🇷", "🇨🇦", "🇦🇺", "🌐"};
    private final String[] countryCodes = {"IN", "US", "GB", "DE", "FR", "CA", "AU", "OTHER"};
    private boolean isFromSettings = false;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.adsUtils.custom.CustomActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.isFromSettings = getIntent().getBooleanExtra("EXTRA_FROM_SETTINGS", false);
        setupSystemBars();
        setContentView(R.layout.activity_select_country);
//        NativeLoader.showNativeAds(this, (LinearLayout) findViewById(R.id.llAdsView), NativeType.BIG_NATIVE);
        View findViewById = findViewById(R.id.topBar);
        ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);
        if (findViewById != null) {
            ViewCompat.setOnApplyWindowInsetsListener(findViewById, new OnApplyWindowInsetsListener() { // from class: com.iptv.online.player.live.tv.smart.SelectCountryActivity$$ExternalSyntheticLambda0
                @Override // androidx.core.view.OnApplyWindowInsetsListener
                public final WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat) {
                     view.setPadding(view.getPaddingLeft(), windowInsetsCompat.getInsets(WindowInsetsCompat.Type.systemBars()).top + 16, view.getPaddingRight(), view.getPaddingBottom());
                    return windowInsetsCompat;
                }
            });
        }
        if (scrollView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(scrollView, new OnApplyWindowInsetsListener() { // from class: com.iptv.online.player.live.tv.smart.SelectCountryActivity$$ExternalSyntheticLambda1
                @Override // androidx.core.view.OnApplyWindowInsetsListener
                public final WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat) {
                     view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), windowInsetsCompat.getInsets(WindowInsetsCompat.Type.systemBars()).bottom + 20);
                    return windowInsetsCompat;
                }
            });
        }
        this.countryListContainer = (LinearLayout) findViewById(R.id.countryListContainer);
        this.progressBarCheck = (ProgressBar) findViewById(R.id.progressBarCheck);
        this.btnCheckmark = (ImageView) findViewById(R.id.btnCheckmark);
        this.progressBarCheck.setVisibility(View.GONE);
        this.btnCheckmark.setVisibility(View.GONE);
        buildCountryList();
        this.selectedIndex = -1;
        this.btnCheckmark.setVisibility(View.GONE);
        this.btnCheckmark.setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.SelectCountryActivity$$ExternalSyntheticLambda2
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                SelectCountryActivity.this.lambda$onCreate$2(view);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$2(View view) {
        int i = this.selectedIndex;
        if (i >= 0) {
            String[] strArr = this.countryCodes;
            if (i < strArr.length) {
                saveCountry(this, strArr[i]);
                // New Code
                getSharedPreferences("onboarding_prefs", MODE_PRIVATE)
                        .edit()
                        .putBoolean("country_completed", true)
                        .apply();
//                AnalyticsTracker.logSelection("SelectCountryActivity", UserDataStore.COUNTRY, this.countryNames[this.selectedIndex] + " (" + this.countryCodes[this.selectedIndex] + ")");
//                AnalyticsTracker.logClick("SelectCountryActivity", "btn_checkmark_confirm", this.countryCodes[this.selectedIndex]);
            }
        }
        if (this.isFromSettings) {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                    Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            intent.addFlags(268468224);
            startActivity(intent);
//            startAdsActivity(intent);
        } else {
            startActivity(new Intent(this, LanguageActivity.class));
//            startAdsActivity(new Intent(this, LanguageActivity.class));
        }
        overridePendingTransition(17432576, 17432577);
    }

    private void buildCountryList() {
        this.countryItems = new View[this.countryNames.length];
        LayoutInflater from = LayoutInflater.from(this);
        for (int i = 0; i < this.countryNames.length; i++) {
            View inflate = from.inflate(R.layout.item_country, (ViewGroup) this.countryListContainer, false);
            ((TextView) inflate.findViewById(R.id.tvCountryFlag)).setText(this.countryFlags[i]);
            ((TextView) inflate.findViewById(R.id.tvCountryName)).setText(this.countryNames[i]);
            int finalI = i;
            inflate.setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.SelectCountryActivity$$ExternalSyntheticLambda4
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    SelectCountryActivity.this.lambda$buildCountryList$3(finalI, view);
                }
            });
            this.countryListContainer.addView(inflate);
            this.countryItems[i] = inflate;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$buildCountryList$3(int i, View view) {
        onCountrySelected(i);
    }

    private void onCountrySelected(int i) {
        if (this.selectedIndex == i) {
            return;
        }
        this.selectedIndex = i;
        updateItemSelection(i);
        showProgressThenCheckmark();
    }

    private void updateItemSelection(int i) {
        int i2 = 0;
        while (true) {
            View[] viewArr = this.countryItems;
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
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() { // from class: com.iptv.online.player.live.tv.smart.SelectCountryActivity$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                SelectCountryActivity.this.lambda$showProgressThenCheckmark$4();
            }
        }, 1000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showProgressThenCheckmark$4() {
        this.progressBarCheck.setVisibility(View.GONE);
        this.btnCheckmark.setVisibility(View.VISIBLE);
        this.btnCheckmark.setScaleX(0.0f);
        this.btnCheckmark.setScaleY(0.0f);
        this.btnCheckmark.animate().scaleX(1.0f).scaleY(1.0f).setDuration(200L).start();
    }

    public static void saveCountry(Context context, String str) {
        context.getSharedPreferences(PREFS_NAME, 0).edit().putString(KEY_SAVED_COUNTRY, str).apply();
    }

    public static String getSavedCountry(Context context) {
        return context.getSharedPreferences(PREFS_NAME, 0).getString(KEY_SAVED_COUNTRY, "IN");
    }
}

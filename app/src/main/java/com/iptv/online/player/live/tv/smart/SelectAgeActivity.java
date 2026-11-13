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
import androidx.core.graphics.Insets;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
//import com.adsUtils.adsloader.nativeads.NativeLoader;
//import com.adsUtils.adsloader.nativeads.NativeType;
//import com.adsUtils.analytics.AnalyticsTracker;
//import com.adsUtils.custom.CustomActivity;
import com.iptv.online.player.live.tv.smart.AdsPackage.CustomActivity;
import com.iptv.online.player.live.tv.smart.ui.home.HomeActivity;

/* loaded from: classes5.dex */
public class SelectAgeActivity extends com.iptv.online.player.live.tv.smart.AdsPackage.CustomActivity {
    public static final String EXTRA_FROM_SETTINGS = "EXTRA_FROM_SETTINGS";
    private static final String KEY_SAVED_AGE = "saved_age_range";
    private static final String PREFS_NAME = "app_user_prefs";
    private View[] ageItems;
    private LinearLayout ageListContainer;
    private ImageView btnCheckmark;
    private ProgressBar progressBarCheck;
    private int selectedIndex = -1;
    private final String[] ageRanges = {"Under 18", "18 - 24", "25 - 34", "35 - 44", "45 - 54", "55+"};
    private final String[] ageEmojis = {"🎮", "⚡", "🚀", "💼", "🌟", "👑"};
    private final String[] ageKeys = {"UNDER_18", "18_24", "25_34", "35_44", "45_54", "55_PLUS"};
    private boolean isFromSettings = false;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.adsUtils.custom.CustomActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.isFromSettings = getIntent().getBooleanExtra("EXTRA_FROM_SETTINGS", false);
        setupSystemBars();
        setContentView(R.layout.activity_select_age);
//        NativeLoader.showNativeAds(this, (LinearLayout) findViewById(R.id.llAdsView), NativeType.BIG_NATIVE);
        View findViewById = findViewById(R.id.topBar);
        ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);
        if (findViewById != null) {
            ViewCompat.setOnApplyWindowInsetsListener(
                    findViewById,
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
        }
       /* if (findViewById != null) {
            ViewCompat.setOnApplyWindowInsetsListener(findViewById, new OnApplyWindowInsetsListener() { // from class: com.iptv.online.player.live.tv.smart.SelectAgeActivity$$ExternalSyntheticLambda2
                @Override // androidx.core.view.OnApplyWindowInsetsListener
                public final WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat) {
                    return view.setPadding(view.getPaddingLeft(), windowInsetsCompat.getInsets(WindowInsetsCompat.Type.systemBars()).top + 16, view.getPaddingRight(), view.getPaddingBottom());
                }
            });
        }*/
        if (scrollView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(scrollView, new OnApplyWindowInsetsListener() { // from class: com.iptv.online.player.live.tv.smart.SelectAgeActivity$$ExternalSyntheticLambda3
                @Override // androidx.core.view.OnApplyWindowInsetsListener
                public final WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat) {
                    return SelectAgeActivity.lambda$onCreate$1(view, windowInsetsCompat);
                }
            });
        }
        this.ageListContainer = (LinearLayout) findViewById(R.id.ageListContainer);
        this.progressBarCheck = (ProgressBar) findViewById(R.id.progressBarCheck);
        this.btnCheckmark = (ImageView) findViewById(R.id.btnCheckmark);
        this.progressBarCheck.setVisibility(View.GONE);
        this.btnCheckmark.setVisibility(View.GONE);
        buildAgeList();
        this.selectedIndex = -1;
        this.btnCheckmark.setVisibility(View.GONE);
        this.btnCheckmark.setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.SelectAgeActivity$$ExternalSyntheticLambda4
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                SelectAgeActivity.this.lambda$onCreate$2(view);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ WindowInsetsCompat lambda$onCreate$1(View view, WindowInsetsCompat windowInsetsCompat) {
        Insets insets = windowInsetsCompat.getInsets(WindowInsetsCompat.Type.systemBars());
        view.setPadding(view.getPaddingLeft(), insets.top + 16, view.getPaddingRight(), insets.bottom + 20);
        return windowInsetsCompat;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$2(View view) {
        int i = this.selectedIndex;
        if (i >= 0) {
            String[] strArr = this.ageKeys;
            if (i < strArr.length) {
                saveAge(this, strArr[i]);
//                AnalyticsTracker.logSelection("SelectAgeActivity", "age_range", this.ageRanges[this.selectedIndex]);
//                AnalyticsTracker.logClick("SelectAgeActivity", "btn_checkmark_confirm", this.ageKeys[this.selectedIndex]);
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
            startActivity(new Intent(this, SelectInterestActivity.class));
//            startAdsActivity(new Intent(this, SelectInterestActivity.class));
        }
        overridePendingTransition(17432576, 17432577);
    }

    private void buildAgeList() {
        this.ageItems = new View[this.ageRanges.length];
        LayoutInflater from = LayoutInflater.from(this);
        for (int i = 0; i < this.ageRanges.length; i++) {
            View inflate = from.inflate(R.layout.item_age, (ViewGroup) this.ageListContainer, false);
            ((TextView) inflate.findViewById(R.id.tvAgeEmoji)).setText(this.ageEmojis[i]);
            ((TextView) inflate.findViewById(R.id.tvAgeTitle)).setText(this.ageRanges[i]);
            int finalI = i;
            inflate.setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.SelectAgeActivity$$ExternalSyntheticLambda1
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    SelectAgeActivity.this.lambda$buildAgeList$3(finalI, view);
                }
            });
            this.ageListContainer.addView(inflate);
            this.ageItems[i] = inflate;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$buildAgeList$3(int i, View view) {
        onAgeSelected(i);
    }

    private void onAgeSelected(int i) {
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
            View[] viewArr = this.ageItems;
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
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() { // from class: com.iptv.online.player.live.tv.smart.SelectAgeActivity$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                SelectAgeActivity.this.lambda$showProgressThenCheckmark$4();
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

    public static void saveAge(Context context, String str) {
        context.getSharedPreferences(PREFS_NAME, 0).edit().putString(KEY_SAVED_AGE, str).apply();
    }

    public static String getSavedAge(Context context) {
        return context.getSharedPreferences(PREFS_NAME, 0).getString(KEY_SAVED_AGE, null);
    }
}

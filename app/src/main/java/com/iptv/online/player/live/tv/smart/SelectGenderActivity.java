package com.iptv.online.player.live.tv.smart;

import static android.content.Intent.getIntent;

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
public class SelectGenderActivity extends com.iptv.online.player.live.tv.smart.AdsPackage.CustomActivity {
    public static final String EXTRA_FROM_SETTINGS = "EXTRA_FROM_SETTINGS";
    private static final String KEY_SAVED_GENDER = "saved_gender";
    private static final String PREFS_NAME = "app_user_prefs";
    private ImageView btnCheckmark;
    private View[] genderItems;
    private LinearLayout genderListContainer;
    private ProgressBar progressBarCheck;
    private int selectedIndex = -1;
    private final String[] genderTitles = {"Male", "Female", "Non-Binary / Other", "Prefer Not to Say"};
    private final String[] genderEmojis = {"👨", "👩", "⚧️", "🤐"};
    private final String[] genderKeys = {"MALE", "FEMALE", "OTHER", "PREFER_NOT_TO_SAY"};
    private boolean isFromSettings = false;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.adsUtils.custom.CustomActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.isFromSettings = getIntent().getBooleanExtra("EXTRA_FROM_SETTINGS", false);
        setupSystemBars();
        setContentView(R.layout.activity_select_gender);
//        NativeLoader.showNativeAds(this, (LinearLayout) findViewById(R.id.llAdsView), NativeType.BIG_NATIVE);
        View findViewById = findViewById(R.id.topBar);
        ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);
        if (findViewById != null) {
            ViewCompat.setOnApplyWindowInsetsListener(findViewById, new OnApplyWindowInsetsListener() { // from class: com.iptv.online.player.live.tv.smart.SelectGenderActivity$$ExternalSyntheticLambda0
                @Override // androidx.core.view.OnApplyWindowInsetsListener
                public final WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat) {
                     view.setPadding(view.getPaddingLeft(), windowInsetsCompat.getInsets(WindowInsetsCompat.Type.systemBars()).top + 16, view.getPaddingRight(), view.getPaddingBottom());
                    return windowInsetsCompat;
                }
            });
        }
        if (scrollView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(scrollView, new OnApplyWindowInsetsListener() { // from class: com.iptv.online.player.live.tv.smart.SelectGenderActivity$$ExternalSyntheticLambda1
                @Override // androidx.core.view.OnApplyWindowInsetsListener
                public final WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat) {
                    return SelectGenderActivity.lambda$onCreate$1(view, windowInsetsCompat);
                }
            });
        }
        this.genderListContainer = (LinearLayout) findViewById(R.id.genderListContainer);
        this.progressBarCheck = (ProgressBar) findViewById(R.id.progressBarCheck);
        this.btnCheckmark = (ImageView) findViewById(R.id.btnCheckmark);
        this.progressBarCheck.setVisibility(View.GONE);
        this.btnCheckmark.setVisibility(View.GONE);
        buildGenderList();
        this.selectedIndex = -1;
        this.btnCheckmark.setVisibility(View.GONE);
        this.btnCheckmark.setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.SelectGenderActivity$$ExternalSyntheticLambda2
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                SelectGenderActivity.this.lambda$onCreate$2(view);
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
            String[] strArr = this.genderKeys;
            if (i < strArr.length) {
                saveGender(this, strArr[i]);
//                AnalyticsTracker.logSelection("SelectGenderActivity", "gender", this.genderTitles[this.selectedIndex]);
//                AnalyticsTracker.logClick("SelectGenderActivity", "btn_checkmark_confirm", this.genderKeys[this.selectedIndex]);
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
            startActivity(new Intent(this, SelectAgeActivity.class));
//            startAdsActivity(new Intent(this, SelectAgeActivity.class));
        }
        overridePendingTransition(17432576, 17432577);
    }

    private void buildGenderList() {
        this.genderItems = new View[this.genderTitles.length];
        LayoutInflater from = LayoutInflater.from(this);
        for (int i = 0; i < this.genderTitles.length; i++) {
            View inflate = from.inflate(R.layout.item_gender, (ViewGroup) this.genderListContainer, false);
            ((TextView) inflate.findViewById(R.id.tvGenderEmoji)).setText(this.genderEmojis[i]);
            ((TextView) inflate.findViewById(R.id.tvGenderTitle)).setText(this.genderTitles[i]);
            int finalI = i;
            inflate.setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.SelectGenderActivity$$ExternalSyntheticLambda4
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    SelectGenderActivity.this.lambda$buildGenderList$3(finalI, view);
                }
            });
            this.genderListContainer.addView(inflate);
            this.genderItems[i] = inflate;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$buildGenderList$3(int i, View view) {
        onGenderSelected(i);
    }

    private void onGenderSelected(int i) {
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
            View[] viewArr = this.genderItems;
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
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() { // from class: com.iptv.online.player.live.tv.smart.SelectGenderActivity$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                SelectGenderActivity.this.lambda$showProgressThenCheckmark$4();
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

    public static void saveGender(Context context, String str) {
        context.getSharedPreferences(PREFS_NAME, 0).edit().putString(KEY_SAVED_GENDER, str).apply();
    }

    public static String getSavedGender(Context context) {
        return context.getSharedPreferences(PREFS_NAME, 0).getString(KEY_SAVED_GENDER, null);
    }
}

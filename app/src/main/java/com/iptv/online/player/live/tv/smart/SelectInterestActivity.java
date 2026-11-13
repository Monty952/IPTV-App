package com.iptv.online.player.live.tv.smart;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
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
import java.util.HashSet;
import java.util.Set;
@SuppressWarnings("all")

/* loaded from: classes5.dex */
public class SelectInterestActivity extends com.iptv.online.player.live.tv.smart.AdsPackage.CustomActivity {
    public static final String EXTRA_FROM_SETTINGS = "EXTRA_FROM_SETTINGS";
    private static final String KEY_SAVED_INTERESTS = "saved_interests";
    private static final String PREFS_NAME = "app_user_prefs";
    private TextView btnNext;
    private TextView btnSkip;
    private View[] chipViews;
    private FlowLayout flowInterests;
    private final Set<Integer> selectedIndices = new HashSet();
    private final String[] interestTitles = {"Action Movies", "Web Series", "Heist Movies", "CAR Shows", "Reality Shows", "Daily News", "Family Shows", "Comedy"};
    private final String[] interestEmojis = {"🔫", "🗺️", "💰", "🏎️", "⚡", "🎰", "🏠", "💡"};
    private final String[] interestKeys = {"WEAPON_STATS", "MAP_GUIDES", "HEIST_MISSIONS", "SUPERCARS", "DPS_CALC", "DAILY_SPINNER", "SAFEHOUSE_LOCS", "STREET_TIPS"};
    private boolean isFromSettings = false;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.adsUtils.custom.CustomActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.isFromSettings = getIntent().getBooleanExtra("EXTRA_FROM_SETTINGS", false);
        setupSystemBars();
        setContentView(R.layout.activity_select_interest);
//        NativeLoader.showNativeAds(this, (LinearLayout) findViewById(R.id.llAdsView), NativeType.SMALL_NATIVE);
        View findViewById = findViewById(R.id.headerContainer);
        View findViewById2 = findViewById(R.id.bottomBarContainer);
        if (findViewById != null) {
            ViewCompat.setOnApplyWindowInsetsListener(findViewById, new OnApplyWindowInsetsListener() { // from class: com.iptv.online.player.live.tv.smart.SelectInterestActivity$$ExternalSyntheticLambda0
                @Override // androidx.core.view.OnApplyWindowInsetsListener
                public final WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat) {
                    view.setPadding(view.getPaddingLeft(), windowInsetsCompat.getInsets(WindowInsetsCompat.Type.systemBars()).top + 16, view.getPaddingRight(), view.getPaddingBottom());
                    return windowInsetsCompat;
                }
            });
        }
        if (findViewById2 != null) {
            ViewCompat.setOnApplyWindowInsetsListener(findViewById2, new OnApplyWindowInsetsListener() { // from class: com.iptv.online.player.live.tv.smart.SelectInterestActivity$$ExternalSyntheticLambda1
                @Override // androidx.core.view.OnApplyWindowInsetsListener
                public final WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat) {
                    return SelectInterestActivity.lambda$onCreate$1(view, windowInsetsCompat);
                }
            });
        }
        this.flowInterests = (FlowLayout) findViewById(R.id.flowInterests);
        this.btnSkip = (TextView) findViewById(R.id.btnSkip);
        this.btnNext = (TextView) findViewById(R.id.btnNext);
        buildInterestChips();
        this.btnSkip.setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.SelectInterestActivity$$ExternalSyntheticLambda2
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                SelectInterestActivity.this.lambda$onCreate$2(view);
            }
        });
        this.btnNext.setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.SelectInterestActivity$$ExternalSyntheticLambda3
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                SelectInterestActivity.this.lambda$onCreate$3(view);
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
//        AnalyticsTracker.logClick("SelectInterestActivity", "btn_skip", null);
        proceedToNextScreen();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$3(View view) {
//        AnalyticsTracker.logClick("SelectInterestActivity", "btn_next", this.selectedIndices.size() + " items");
        saveSelectedInterests();
        proceedToNextScreen();
    }

    private void buildInterestChips() {
        this.chipViews = new View[this.interestTitles.length];
        LayoutInflater from = LayoutInflater.from(this);
        for (int i = 0; i < this.interestTitles.length; i++) {
            View inflate = from.inflate(R.layout.item_interest_chip, (ViewGroup) this.flowInterests, false);
            ((TextView) inflate.findViewById(R.id.tvEmoji)).setText(this.interestEmojis[i]);
            ((TextView) inflate.findViewById(R.id.tvTitle)).setText(this.interestTitles[i]);
            int finalI = i;
            inflate.setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.SelectInterestActivity$$ExternalSyntheticLambda4
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    SelectInterestActivity.this.lambda$buildInterestChips$4(finalI, view);
                }
            });
            this.flowInterests.addView(inflate);
            this.chipViews[i] = inflate;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$buildInterestChips$4(int i, View view) {
        toggleInterestSelection(i);
    }

    private void toggleInterestSelection(int i) {
        boolean contains = this.selectedIndices.contains(Integer.valueOf(i));
        Set<Integer> set = this.selectedIndices;
        if (contains) {
            set.remove(Integer.valueOf(i));
        } else {
            set.add(Integer.valueOf(i));
            if (i >= 0) {
                String[] strArr = this.interestTitles;
                if (i < strArr.length) {
//                    AnalyticsTracker.logSelection("SelectInterestActivity", "interest_chip", strArr[i]);
                }
            }
        }
        updateChipState(i);
    }

    private void updateChipState(int i) {
        View view = this.chipViews[i];
        if (view == null) {
            return;
        }
        ImageView imageView = (ImageView) view.findViewById(R.id.ivCheckmark);
        TextView textView = (TextView) view.findViewById(R.id.tvEmoji);
        TextView textView2 = (TextView) view.findViewById(R.id.tvTitle);
        if (this.selectedIndices.contains(Integer.valueOf(i))) {
            view.setBackgroundResource(R.drawable.bg_interest_pill_selected);
            imageView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
            textView2.setTextColor(-1);
            return;
        }
        view.setBackgroundResource(R.drawable.bg_interest_pill_unselected);
        imageView.setVisibility(View.GONE);
        textView.setVisibility(View.VISIBLE);
        textView2.setTextColor(ContextCompat.getColor(this, R.color.text_primary));
    }

    private void saveSelectedInterests() {
        HashSet hashSet = new HashSet();
        for (Integer num : this.selectedIndices) {
            int intValue = num.intValue();
            if (intValue >= 0) {
                String[] strArr = this.interestKeys;
                if (intValue < strArr.length) {
                    hashSet.add(strArr[intValue]);
                }
            }
        }
        getSharedPreferences(PREFS_NAME, 0).edit().putStringSet(KEY_SAVED_INTERESTS, hashSet).apply();
    }

    private void proceedToNextScreen() {
        if (this.isFromSettings) {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                    Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            intent.addFlags(268468224);
            startActivity(intent);
//            startAdsActivity(intent);
        } else {
            startActivity(new Intent(this, IntroActivity1.class));
//            startAdsActivity(new Intent(this, IntroActivity1.class));
        }
        finish();
        overridePendingTransition(17432576, 17432577);
    }

    public static Set<String> getSavedInterests(Context context) {
        return context.getSharedPreferences(PREFS_NAME, 0).getStringSet(KEY_SAVED_INTERESTS, new HashSet());
    }
}

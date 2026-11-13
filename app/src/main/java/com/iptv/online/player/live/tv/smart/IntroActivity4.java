package com.iptv.online.player.live.tv.smart;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
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
public class IntroActivity4 extends com.iptv.online.player.live.tv.smart.AdsPackage.CustomActivity {
    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.adsUtils.custom.CustomActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setupSystemBars();
        setContentView(R.layout.activity_intro4);
//        NativeLoader.showNativeAds(this, (LinearLayout) findViewById(R.id.llAdsView), NativeType.BIG_NATIVE);
        View findViewById = findViewById(R.id.layoutEmptySection);
        if (findViewById != null) {
            ViewCompat.setOnApplyWindowInsetsListener(findViewById, new OnApplyWindowInsetsListener() { // from class: com.iptv.online.player.live.tv.smart.IntroActivity4$$ExternalSyntheticLambda0
                @Override // androidx.core.view.OnApplyWindowInsetsListener
                public final WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat) {
                    return IntroActivity4.lambda$onCreate$0(view, windowInsetsCompat);
                }
            });
        }
        findViewById(R.id.btnGetStarted).setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.IntroActivity4$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                IntroActivity4.this.lambda$onCreate$1(view);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ WindowInsetsCompat lambda$onCreate$0(View view, WindowInsetsCompat windowInsetsCompat) {
        Insets insets = windowInsetsCompat.getInsets(WindowInsetsCompat.Type.systemBars());
        view.setPadding(view.getPaddingLeft(), insets.top + 16, view.getPaddingRight(), insets.bottom);
        return windowInsetsCompat;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$1(View view) {
        getSharedPreferences("onboarding_prefs", MODE_PRIVATE)
                .edit()
                .putBoolean("intro_completed", true)
                .apply();
//        AnalyticsTracker.logClick("IntroActivity4", "btn_get_started", null);
        startActivity(new Intent(this, HomeActivity.class));
//        startAdsActivity(new Intent(this, HomeActivity.class));
        overridePendingTransition(17432576, 17432577);
        finish();
    }
}

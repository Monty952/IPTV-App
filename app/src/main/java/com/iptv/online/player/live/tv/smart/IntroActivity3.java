package com.iptv.online.player.live.tv.smart;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
//import com.adsUtils.adsloader.nativeads.NativeLoader;
//import com.adsUtils.adsloader.nativeads.NativeType;
//import com.adsUtils.analytics.AnalyticsTracker;
//import com.adsUtils.custom.CustomActivity;
import com.iptv.online.player.live.tv.smart.AdsPackage.CustomActivity;

/* loaded from: classes5.dex */
public class IntroActivity3 extends com.iptv.online.player.live.tv.smart.AdsPackage.CustomActivity {
    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.adsUtils.custom.CustomActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setupSystemBars();
        setContentView(R.layout.activity_intro3);
//        NativeLoader.showNativeAds(this, (LinearLayout) findViewById(R.id.llAdsView), NativeType.BIG_NATIVE);
        View findViewById = findViewById(R.id.layoutEmptySection);
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
                                    view.getPaddingTop(),
                                    view.getPaddingRight(),
                                    windowInsetsCompat.getInsets(
                                            WindowInsetsCompat.Type.systemBars()
                                    ).bottom
                            );
                            return windowInsetsCompat;
                        }
                    }
            );
        }
        if (findViewById != null) {
            ViewCompat.setOnApplyWindowInsetsListener(findViewById, new OnApplyWindowInsetsListener() { // from class: com.iptv.online.player.live.tv.smart.IntroActivity1$$ExternalSyntheticLambda0
                @Override // androidx.core.view.OnApplyWindowInsetsListener
                public final WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat) {
                    view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), windowInsetsCompat.getInsets(WindowInsetsCompat.Type.systemBars()).bottom);
                    return windowInsetsCompat;
                }
            });
        }
        findViewById(R.id.btnNext).setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.IntroActivity3$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                IntroActivity3.this.lambda$onCreate$1(view);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$1(View view) {
//        AnalyticsTracker.logClick("IntroActivity3", "btn_next", null);
        startActivity(new Intent(this, IntroActivity4.class));
//        startAdsActivity(new Intent(this, IntroActivity4.class));
        overridePendingTransition(17432576, 17432577);
    }
}

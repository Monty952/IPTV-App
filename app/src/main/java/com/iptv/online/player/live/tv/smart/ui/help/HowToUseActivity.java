package com.iptv.online.player.live.tv.smart.ui.help;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
//import com.adsUtils.adsloader.nativeads.NativeLoader;
//import com.adsUtils.adsloader.nativeads.NativeType;
//import com.adsUtils.custom.CustomActivity;
import com.iptv.online.player.live.tv.smart.AdsPackage.CustomActivity;
import com.iptv.online.player.live.tv.smart.R;
import com.iptv.online.player.live.tv.smart.ui.cast.CastToTvActivity;
@SuppressWarnings("all")

/* loaded from: classes5.dex */
public class HowToUseActivity extends com.iptv.online.player.live.tv.smart.AdsPackage.CustomActivity {
    public static final String EXTRA_TAB = "EXTRA_TAB";
    private View contentCast;
    private View contentImportUrl;
    private View contentStream;
    private View contentUploadFile;
    private View indicatorTabCast;
    private View indicatorTabFile;
    private View indicatorTabStream;
    private View indicatorTabUrl;
    private ImageView ivTabCastIcon;
    private ImageView ivTabFileIcon;
    private ImageView ivTabStreamIcon;
    private ImageView ivTabUrlIcon;
    private TextView tvTabCastTitle;
    private TextView tvTabFileTitle;
    private TextView tvTabStreamTitle;
    private TextView tvTabUrlTitle;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public enum Tab {
        IMPORT_URL,
        UPLOAD_FILE,
        STREAM,
        CAST
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.adsUtils.custom.CustomActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_how_to_use);
       /* NativeLoader.showNativeAds(this, (LinearLayout) findViewById(R.id.llAdsView), NativeType.BIG_NATIVE);
        NativeLoader.showNativeAds(this, (LinearLayout) findViewById(R.id.llAdsView1), NativeType.BIG_NATIVE);
        NativeLoader.showNativeAds(this, (LinearLayout) findViewById(R.id.llAdsView2), NativeType.BIG_NATIVE);
        NativeLoader.showNativeAds(this, (LinearLayout) findViewById(R.id.llAdsView3), NativeType.BIG_NATIVE);*/
        initViews();
        setupListeners();
        if ("CAST".equalsIgnoreCase(getIntent().getStringExtra(EXTRA_TAB))) {
            selectTab(Tab.CAST);
        } else {
            selectTab(Tab.IMPORT_URL);
        }
    }

    private void initViews() {
        findViewById(R.id.btnHowBack).setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.help.HowToUseActivity$$ExternalSyntheticLambda4
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                HowToUseActivity.this.lambda$initViews$0(view);
            }
        });
        this.ivTabUrlIcon = (ImageView) findViewById(R.id.ivTabUrlIcon);
        this.tvTabUrlTitle = (TextView) findViewById(R.id.tvTabUrlTitle);
        this.indicatorTabUrl = findViewById(R.id.indicatorTabUrl);
        this.ivTabFileIcon = (ImageView) findViewById(R.id.ivTabFileIcon);
        this.tvTabFileTitle = (TextView) findViewById(R.id.tvTabFileTitle);
        this.indicatorTabFile = findViewById(R.id.indicatorTabFile);
        this.ivTabStreamIcon = (ImageView) findViewById(R.id.ivTabStreamIcon);
        this.tvTabStreamTitle = (TextView) findViewById(R.id.tvTabStreamTitle);
        this.indicatorTabStream = findViewById(R.id.indicatorTabStream);
        this.ivTabCastIcon = (ImageView) findViewById(R.id.ivTabCastIcon);
        this.tvTabCastTitle = (TextView) findViewById(R.id.tvTabCastTitle);
        this.indicatorTabCast = findViewById(R.id.indicatorTabCast);
        this.contentImportUrl = findViewById(R.id.contentImportUrl);
        this.contentUploadFile = findViewById(R.id.contentUploadFile);
        this.contentStream = findViewById(R.id.contentStream);
        this.contentCast = findViewById(R.id.contentCast);
        View findViewById = findViewById(R.id.btnHowCastNow);
        if (findViewById != null) {
            findViewById.setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.help.HowToUseActivity$$ExternalSyntheticLambda5
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    HowToUseActivity.this.lambda$initViews$1(view);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initViews$0(View view) {
        setOnBackPressedClick();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initViews$1(View view) {
//        startAdsActivity(new Intent(this, CastToTvActivity.class));
        startActivity(new Intent(this, CastToTvActivity.class));
        finish();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupListeners$2(View view) {
        selectTab(Tab.IMPORT_URL);
    }

    private void setupListeners() {
        findViewById(R.id.tabImportUrl).setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.help.HowToUseActivity$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                HowToUseActivity.this.lambda$setupListeners$2(view);
            }
        });
        findViewById(R.id.tabUploadFile).setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.help.HowToUseActivity$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                HowToUseActivity.this.lambda$setupListeners$3(view);
            }
        });
        findViewById(R.id.tabStream).setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.help.HowToUseActivity$$ExternalSyntheticLambda2
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                HowToUseActivity.this.lambda$setupListeners$4(view);
            }
        });
        findViewById(R.id.tabCast).setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.help.HowToUseActivity$$ExternalSyntheticLambda3
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                HowToUseActivity.this.lambda$setupListeners$5(view);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupListeners$3(View view) {
        selectTab(Tab.UPLOAD_FILE);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupListeners$4(View view) {
        selectTab(Tab.STREAM);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupListeners$5(View view) {
        selectTab(Tab.CAST);
    }

    private void selectTab(Tab tab) {
//        showTabScrollInterstitialAds();
        int color = ContextCompat.getColor(this, R.color.purple_500);
        int color2 = ContextCompat.getColor(this, R.color.text_secondary);
        this.ivTabUrlIcon.setImageTintList(ColorStateList.valueOf(color2));
        this.tvTabUrlTitle.setTextColor(color2);
        this.indicatorTabUrl.setVisibility(4);
        this.contentImportUrl.setVisibility(View.GONE);
        this.ivTabFileIcon.setImageTintList(ColorStateList.valueOf(color2));
        this.tvTabFileTitle.setTextColor(color2);
        this.indicatorTabFile.setVisibility(4);
        this.contentUploadFile.setVisibility(View.GONE);
        this.ivTabStreamIcon.setImageTintList(ColorStateList.valueOf(color2));
        this.tvTabStreamTitle.setTextColor(color2);
        this.indicatorTabStream.setVisibility(4);
        this.contentStream.setVisibility(View.GONE);
        this.ivTabCastIcon.setImageTintList(ColorStateList.valueOf(color2));
        this.tvTabCastTitle.setTextColor(color2);
        this.indicatorTabCast.setVisibility(4);
        this.contentCast.setVisibility(View.GONE);
        int ordinal = tab.ordinal();
        if (ordinal == 0) {
            this.ivTabUrlIcon.setImageTintList(ColorStateList.valueOf(color));
            this.tvTabUrlTitle.setTextColor(color);
            this.indicatorTabUrl.setVisibility(View.VISIBLE);
            this.contentImportUrl.setVisibility(View.VISIBLE);
        } else if (ordinal == 1) {
            this.ivTabFileIcon.setImageTintList(ColorStateList.valueOf(color));
            this.tvTabFileTitle.setTextColor(color);
            this.indicatorTabFile.setVisibility(View.VISIBLE);
            this.contentUploadFile.setVisibility(View.VISIBLE);
        } else if (ordinal == 2) {
            this.ivTabStreamIcon.setImageTintList(ColorStateList.valueOf(color));
            this.tvTabStreamTitle.setTextColor(color);
            this.indicatorTabStream.setVisibility(View.VISIBLE);
            this.contentStream.setVisibility(View.VISIBLE);
        } else if (ordinal != 3) {
        } else {
            this.ivTabCastIcon.setImageTintList(ColorStateList.valueOf(color));
            this.tvTabCastTitle.setTextColor(color);
            this.indicatorTabCast.setVisibility(View.VISIBLE);
            this.contentCast.setVisibility(View.VISIBLE);
        }
    }
}

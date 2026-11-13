package com.iptv.online.player.live.tv.smart.AdsPackage.custom;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.fragment.app.Fragment;
//import androidx.media3.exoplayer.upstream.CmcdData;
//import com.adsUtils.adsloader.InterstitialLoader;
//import com.adsUtils.adsloader.RewardLoader;
//import com.adsUtils.adsloader.ScreenAdsLoader;
//import com.adsUtils.adsloader.ScreenTags;
//import com.adsUtils.adsutil.AdsUtils;
//import com.adsUtils.analytics.AnalyticsTracker;
//import com.adsUtils.config.AdsConfig;
//import com.adsUtils.custom.CustomActivity;
//import com.adsUtils.custom.CustomFragment;
//import com.adsUtils.dialog.DialogFullScreenNativeAd;

@SuppressWarnings("all")

/* loaded from: classes3.dex */
public class CustomFragment extends Fragment {

    /* loaded from: classes3.dex */
    public interface onReplaceFragmentListener {
        void onDismissAds();
    }

    @Override // androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
    }

    public void openScreen(Intent intent, String str) {
        startActivity(intent);
       /* if (!AdsConfig.isInterEnabled(str)) {
            startActivity(intent);
        } else {
            startAdsActivity(intent);
        }*/
    }

    /*public void startAdsActivity(Intent intent) {
        if (intent != null) {
            AnalyticsTracker.logNavigation(getClass().getSimpleName(), intent.getComponent() != null ? intent.getComponent().getClassName() : "unknown");
        }
        if (!AdsUtils.isNetworkAvailable(requireActivity())) {
            Log.w("CustomActBtnClickTag", "network not available");
            startActivity(intent);
        } else if (!AdsConfig.getIsAdsOn()) {
            Log.w("CustomActBtnClickTag", "ads off");
            startActivity(intent);
        } else {
            boolean z = false;
            InterstitialLoader.getInstance(requireContext()).isBackInterstitial = false;
            AdsUtils.isBrowserOpened = false;
            InterstitialLoader.getInstance(requireContext()).setOnInterstitialCloseListener(new AnonymousClass1(intent));
            AdsUtils.interNextClickCounter++;
            AdsUtils.interCustomNextClickCounter++;
            int interstitialClickCount = AdsConfig.getInterstitialClickCount();
            int customInterstitialClickCount = AdsConfig.getCustomInterstitialClickCount();
            if (interstitialClickCount > 0 && AdsUtils.interNextClickCounter % interstitialClickCount == 0) {
                z = true;
            }
            if (customInterstitialClickCount > 0 && AdsUtils.interCustomNextClickCounter % customInterstitialClickCount == 0 && !z) {
                Log.w("CustomActBtnClickTag", "trigger custom interstitial ads link");
                if (!AdsConfig.getIsCustomInterstitialEnabled()) {
                    Log.d("CustomActBtnClickTag", "custom interstitial disable");
                    startActivity(intent);
                    return;
                }
                Log.d("CustomActBtnClickTag", "show only custom interstitial ads");
                InterstitialLoader.getInstance(requireContext()).showOnlyCustomInterstitialAds();
            } else if (z) {
                Log.d("CustomActBtnClickTag", "show network interstitial");
                InterstitialLoader interstitialLoader = InterstitialLoader.getInstance(requireContext());
                if (AdsConfig.isOnlyCustomPriority()) {
                    Log.d("CustomActBtnClickTag", "only custom interstitial ads");
                    interstitialLoader.showInterstitial(requireActivity());
                } else if (interstitialLoader.isAnyAdReady()) {
                    interstitialLoader.showInterstitial(requireActivity());
                } else {
                    Log.d("CustomActBtnClickTag", "interstitial not ready -> direct next");
                    startActivity(intent);
                    interstitialLoader.loadInterstitial(requireContext());
                }
            } else {
                Log.d("CustomActBtnClickTag", "direct next, no ad this click");
                startActivity(intent);
                InterstitialLoader interstitialLoader2 = InterstitialLoader.getInstance(requireContext());
                if (interstitialLoader2.isAnyAdReady()) {
                    return;
                }
                interstitialLoader2.loadInterstitial(requireContext());
            }
        }
    }*/



    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.adsUtils.custom.CustomFragment$1  reason: invalid class name */
    /* loaded from: classes3.dex */
/*
    public class AnonymousClass1 implements InterstitialLoader.OnInterstitialCloseListener {
        final */
/* synthetic *//*
 Intent val$intent;

        AnonymousClass1(Intent intent) {
            this.val$intent = intent;
        }

        @Override // com.adsUtils.adsloader.InterstitialLoader.OnInterstitialCloseListener
        public void onInterstitialClose() {
            Log.d("CustomActBtnClickTag", "close network interstitial: " + CustomFragment.this.requireActivity());
            if (AdsConfig.getIsShowNativeAfterInter()) {
                Log.d("CustomActBtnClickTag", "native after interstitial");
                final DialogFullScreenNativeAd dialogFullScreenNativeAd = new DialogFullScreenNativeAd(CustomFragment.this.requireActivity());
                final Intent intent = this.val$intent;
                dialogFullScreenNativeAd.setNativeCloseListener(new View.OnClickListener() { // from class: com.adsUtils.custom.CustomFragment$1$$ExternalSyntheticLambda0
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        AnonymousClass1.this.m575x39060c72(dialogFullScreenNativeAd, intent, view);
                    }
                });
                ScreenAdsLoader.startAutoRefresh(CustomFragment.this.requireActivity(), dialogFullScreenNativeAd.getNativeAdView(), ScreenTags.NATIVE_FULL_TAG);
            } else {
                Log.d("CustomActBtnClickTag", "native after interstitial off");
                CustomFragment.this.startActivity(this.val$intent);
            }
            InterstitialLoader interstitialLoader = InterstitialLoader.getInstance(CustomFragment.this.requireActivity());
            if (interstitialLoader.isAnyAdReady()) {
                return;
            }
            interstitialLoader.loadInterstitial(CustomFragment.this.requireActivity());
        }

        */
/* JADX INFO: Access modifiers changed from: package-private *//*

        */
/* renamed from: lambda$onInterstitialClose$0$com-adsUtils-custom-CustomFragment$1  reason: not valid java name *//*

        public */
/* synthetic *//*
 void m575x39060c72(DialogFullScreenNativeAd dialogFullScreenNativeAd, Intent intent, View view) {
            dialogFullScreenNativeAd.dismiss();
            CustomFragment.this.startActivity(intent);
        }

        @Override // com.adsUtils.adsloader.InterstitialLoader.OnInterstitialCloseListener
        public void onInterstitialCustomShow() {
            Log.d("CustomActBtnClickTag", "show custom interstitial");
            CustomFragment.this.startActivity(this.val$intent);
            boolean customMultiLinkOpenEnable = AdsConfig.getCustomMultiLinkOpenEnable();
            CustomFragment customFragment = CustomFragment.this;
            if (customMultiLinkOpenEnable) {
                AdsUtils.afterInterCustom(customFragment.requireActivity());
            } else {
                AdsUtils.openCustomTabUrl(customFragment.requireActivity(), AdsConfig.getCustomInterstitialId());
            }
        }
    }
*/

/*
    public void replaceBtnFragment(CustomActivity.onReplaceFragmentListener onreplacefragmentlistener) {
        if (!AdsUtils.isNetworkAvailable(requireActivity())) {
            Log.w("ReplaceFragTag", "network not available");
            onreplacefragmentlistener.onDismissAds();
        } else if (!AdsConfig.getIsAdsOn()) {
            Log.w("ReplaceFragTag", "ads off");
            onreplacefragmentlistener.onDismissAds();
        } else {
            boolean z = false;
            InterstitialLoader.getInstance(requireContext()).isBackInterstitial = false;
            AdsUtils.isBrowserOpened = false;
            InterstitialLoader.getInstance(requireContext()).setOnInterstitialCloseListener(new AnonymousClass2(onreplacefragmentlistener));
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
                InterstitialLoader.getInstance(requireContext()).showOnlyCustomInterstitialAds();
            } else if (z) {
                Log.d("ReplaceFragTag", "show network interstitial");
                InterstitialLoader interstitialLoader = InterstitialLoader.getInstance(requireContext());
                if (AdsConfig.isOnlyCustomPriority()) {
                    Log.d("ReplaceFragTag", "only custom interstitial ads");
                    interstitialLoader.showInterstitial(requireActivity());
                } else if (interstitialLoader.isAnyAdReady()) {
                    interstitialLoader.showInterstitial(requireActivity());
                } else {
                    Log.d("ReplaceFragTag", "interstitial not ready -> direct next");
                    onreplacefragmentlistener.onDismissAds();
                    interstitialLoader.loadInterstitial(requireContext());
                }
            } else {
                Log.d("ReplaceFragTag", "direct next, no ad this click");
                onreplacefragmentlistener.onDismissAds();
                InterstitialLoader interstitialLoader2 = InterstitialLoader.getInstance(requireContext());
                if (interstitialLoader2.isAnyAdReady()) {
                    return;
                }
                interstitialLoader2.loadInterstitial(requireContext());
            }
        }
    }
*/

    /* renamed from: com.adsUtils.custom.CustomFragment$2  reason: invalid class name */
    /* loaded from: classes3.dex */
/*
    class AnonymousClass2 implements InterstitialLoader.OnInterstitialCloseListener {
        final */
/* synthetic *//*
 CustomActivity.onReplaceFragmentListener val$listener;

        AnonymousClass2(CustomActivity.onReplaceFragmentListener onreplacefragmentlistener) {
            this.val$listener = onreplacefragmentlistener;
        }

        @Override // com.adsUtils.adsloader.InterstitialLoader.OnInterstitialCloseListener
        public void onInterstitialClose() {
            if (AdsConfig.getIsShowNativeAfterInter()) {
                Log.d("ReplaceFragTag", "native after interstitial");
                final DialogFullScreenNativeAd dialogFullScreenNativeAd = new DialogFullScreenNativeAd(CustomFragment.this.requireActivity());
                final CustomActivity.onReplaceFragmentListener onreplacefragmentlistener = this.val$listener;
                dialogFullScreenNativeAd.setNativeCloseListener(new View.OnClickListener() { // from class: com.adsUtils.custom.CustomFragment$2$$ExternalSyntheticLambda0
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        AnonymousClass2.lambda$onInterstitialClose$0(DialogFullScreenNativeAd.this, onreplacefragmentlistener, view);
                    }
                });
                ScreenAdsLoader.startAutoRefresh(CustomFragment.this.requireActivity(), dialogFullScreenNativeAd.getNativeAdView(), ScreenTags.NATIVE_FULL_TAG);
            } else {
                Log.d("ReplaceFragTag", "native after interstitial off");
                this.val$listener.onDismissAds();
            }
            InterstitialLoader interstitialLoader = InterstitialLoader.getInstance(CustomFragment.this.requireContext());
            if (interstitialLoader.isAnyAdReady()) {
                return;
            }
            interstitialLoader.loadInterstitial(CustomFragment.this.requireContext());
        }

        */
/* JADX INFO: Access modifiers changed from: package-private *//*

        public static */
/* synthetic *//*
 void lambda$onInterstitialClose$0(DialogFullScreenNativeAd dialogFullScreenNativeAd, CustomActivity.onReplaceFragmentListener onreplacefragmentlistener, View view) {
            dialogFullScreenNativeAd.dismiss();
            onreplacefragmentlistener.onDismissAds();
        }

        @Override // com.adsUtils.adsloader.InterstitialLoader.OnInterstitialCloseListener
        public void onInterstitialCustomShow() {
            Log.d("ReplaceFragTag", "show custom interstitial");
            this.val$listener.onDismissAds();
            boolean customMultiLinkOpenEnable = AdsConfig.getCustomMultiLinkOpenEnable();
            CustomFragment customFragment = CustomFragment.this;
            if (customMultiLinkOpenEnable) {
                AdsUtils.afterInterCustom(customFragment.requireActivity());
            } else {
                AdsUtils.openCustomTabUrl(customFragment.requireActivity(), AdsConfig.getCustomInterstitialId());
            }
        }
    }
*/

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        /*AnalyticsTracker.logScreen(getClass().getSimpleName(), getActivity());
        RewardLoader.getInstance().tryShowPendingDialog(getActivity());
        if (AdsUtils.isBrowserOpened) {
            RewardLoader.getInstance().pauseTimer(getActivity());
            getActivity().runOnUiThread(new Runnable() { // from class: com.adsUtils.custom.CustomFragment$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    RewardLoader.getInstance().dialogCustomReward.getLeftTimeTextView()
                            .setText("Time left: " + (RewardLoader.getInstance().timeLeftInMillis / 1000) + CmcdData.Factory.STREAMING_FORMAT_SS);
                }
            });
        }*/
    }

    @Override // androidx.fragment.app.Fragment
    public void onPause() {
        super.onPause();
       /* if (AdsUtils.isBrowserOpened) {
            RewardLoader.getInstance().resumeTimer(getActivity());
        }*/
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroy() {
      /*  if (RewardLoader.getInstance() != null) {
            RewardLoader.getInstance().destroy();
        }*/
        super.onDestroy();
    }
}

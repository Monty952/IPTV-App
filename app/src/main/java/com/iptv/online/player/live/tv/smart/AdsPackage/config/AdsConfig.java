package com.iptv.online.player.live.tv.smart.AdsPackage.config;

//import com.adsUtils.adsloader.ScreenTags;
//import com.adsUtils.adsutil.ReferrerHelper;
//import com.adsUtils.model.CustomBannerModel;
//import com.adsUtils.model.CustomNativeModel;
//import com.adsUtils.model.MoreAppModel;
//import com.adsUtils.model.ScreenAdConfig;
//import com.facebook.appevents.AppEventsConstants;
//import com.facebook.internal.NativeProtocol;
//import com.facebook.internal.ServerProtocol;
//import com.onesignal.user.internal.backend.IdentityConstants;
//import io.appmetrica.analytics.coreutils.internal.StringUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.json.JSONArray;
import org.json.JSONObject;

@SuppressWarnings("all")

/* loaded from: classes3.dex */
public class AdsConfig {
    private static JSONObject APP_SETTINGS = new JSONObject();
    private static JSONObject EXTRA = new JSONObject();
    private static JSONObject APP_SCREEN = new JSONObject();
    private static JSONObject ADS_CONFIG = new JSONObject();
    private static JSONObject NTV_BTN_CONFIG = new JSONObject();
    private static JSONObject MORE_APPS_PAGE = new JSONObject();

    public static void setData(JSONObject jSONObject, JSONObject jSONObject2, JSONObject jSONObject3, JSONObject jSONObject4, JSONObject jSONObject5, JSONObject jSONObject6) {
        if (jSONObject == null) {
            jSONObject = new JSONObject();
        }
        APP_SETTINGS = jSONObject;
        if (jSONObject2 == null) {
            jSONObject2 = new JSONObject();
        }
        EXTRA = jSONObject2;
        if (jSONObject3 == null) {
            jSONObject3 = new JSONObject();
        }
        APP_SCREEN = jSONObject3;
        if (jSONObject4 == null) {
            jSONObject4 = new JSONObject();
        }
        ADS_CONFIG = jSONObject4;
        if (jSONObject5 == null) {
            jSONObject5 = new JSONObject();
        }
        NTV_BTN_CONFIG = jSONObject5;
        if (jSONObject6 == null) {
            jSONObject6 = new JSONObject();
        }
        MORE_APPS_PAGE = jSONObject6;
    }

    public static void clear() {
        APP_SETTINGS = new JSONObject();
        EXTRA = new JSONObject();
        APP_SCREEN = new JSONObject();
        ADS_CONFIG = new JSONObject();
        NTV_BTN_CONFIG = new JSONObject();
        MORE_APPS_PAGE = new JSONObject();
    }

    public static ArrayList<Integer> getAdsPriority() {
        ArrayList<Integer> arrayList = new ArrayList<>();
        String optString = APP_SETTINGS.optString("ads_priority", "");
        if (!optString.isEmpty()) {
            for (String str : optString.split(",")) {
                try {
                    arrayList.add(Integer.valueOf(Integer.parseInt(str.trim())));
                } catch (Exception unused) {
                }
            }
        }
        return arrayList;
    }

    public static boolean isOnlyCustomPriority() {
        ArrayList<Integer> adsPriority = getAdsPriority();
        return adsPriority != null && adsPriority.size() == 1 && adsPriority.get(0).intValue() == -1;
    }

    public static boolean getIsAdsOn() {
        return APP_SETTINGS.optBoolean("app_adShowStatus", false);
    }

    public static boolean getIsCustomNativeBannerAdsShow() {
        return APP_SETTINGS.optBoolean("is_custom_native_banner_ads_show", false);
    }

    public static boolean getIsCustomLinkOpenSplash() {
        return APP_SETTINGS.optBoolean("is_custom_link_open_splash", false);
    }

    public static boolean getIsInterstitialOnBack() {
        return APP_SETTINGS.optBoolean("is_interstitial_on_back", false);
    }

    public static boolean getIsCustomInterstitialEnabled() {
        return APP_SETTINGS.optBoolean("is_custom_interstitial", false);
    }

    public static boolean getIsCustomInterstitialOnBack() {
        return APP_SETTINGS.optBoolean("is_custom_interstitial_on_back", false);
    }

    public static int getInterstitialClickCount() {
        return APP_SETTINGS.optInt("interstitial_click_count", 0);
    }

    public static int getInterstitialBackCount() {
        return APP_SETTINGS.optInt("interstitial_back_count", 0);
    }

    public static int getCustomInterstitialClickCount() {
        return APP_SETTINGS.optInt("custom_interstitial_count", 0);
    }

    public static int getCustomInterstitialClickCountBack() {
        return APP_SETTINGS.optInt("custom_interstitial_back_count", 0);
    }

    public static boolean getCustomMultiLinkOpenEnable() {
        return APP_SETTINGS.optBoolean("custom_multi_link_open_enable", false);
    }

    public static int getCustomTabOpenCount() {
        return APP_SETTINGS.optInt("custom_after_inter_link_open_count", 2);
    }

    public static String getBannerPlaceHolder() {
        return APP_SETTINGS.optString("app_BannerAdPlaceHolder", "0");
    }

    public static String getNativePlaceHolder() {
        return APP_SETTINGS.optString("app_NativeAdPlaceHolder", "0");
    }

    public static String getInterstitialPlaceHolder() {
        return APP_SETTINGS.optString("app_interstitialAdPlaceHolder", "0");
    }

    public static int getBannerPreload() {
        return APP_SETTINGS.optInt("app_BannerPreLoad", 0);
    }

    public static int getNativePreload() {
        return APP_SETTINGS.optInt("app_NativePreLoad", 0);
    }

    public static int getInterstitialPreload() {
        return APP_SETTINGS.optInt("app_interstitialPreLoad", 0);
    }

    public static int getAppOpenType() {
        return APP_SETTINGS.optInt("appopentype_appopen_interstitial", 0);
    }

    public static int getAppOpenSpecificType() {
        return APP_SETTINGS.optInt("appopen_frequency", 0);
    }

    public static boolean getIsNeedInternet() {
        return APP_SETTINGS.optBoolean("app_needInternet", false);
    }

    public static boolean getIsTabScrollInterstitialEnabled() {
        return APP_SETTINGS.optBoolean("is_tab_scroll_interstitial_enabled", false);
    }

    public static int getTabScrollInterstitialCount() {
        return APP_SETTINGS.optInt("tab_scroll_interstitial_count", 0);
    }

    public static boolean getIsShowRecyclerItemAd() {
        return APP_SETTINGS.optBoolean("is_show_recyclerview_item_ad", true);
    }

    public static int getNativeAdPosition() {
        return APP_SETTINGS.optInt("native_ad_position", 3);
    }

    public static int getOnboardingDoneShowDuration() {
        return APP_SETTINGS.optInt("onboarding_done_show_duration", 3);
    }

    public static int getIntroNativeNextDelay() {
        return APP_SETTINGS.optInt("intro_native_next_delay", 3);
    }

    public static boolean getIsIntroFullNativeBetween() {
        return APP_SETTINGS.optBoolean("is_intro_full_native_between", false);
    }

    public static int getNativeRefreshSec() {
        return APP_SETTINGS.optInt("native_refresh_sec", 30);
    }

    public static int getNativePreloadQueueSize() {
        return APP_SETTINGS.optInt("native_preload_quque_size", 2);
    }

    public static int getOnboardingCount() {
        return APP_SETTINGS.optInt("onboarding_count", 5);
    }

    public static boolean getIsConsent() {
        return APP_SETTINGS.optBoolean("app_conset", false);
    }

    public static boolean getAppDesign() {
        return APP_SETTINGS.optBoolean("app_design", false);
    }

    public static String getNewNameName() {
        return EXTRA.optString("app_newname", "");
    }

    public static String getNewPackageName() {
        return EXTRA.optString("app_newPackageName", "");
    }

    public static String getPackageName() {
        return EXTRA.optString("app_packageName", "");
    }

    public static boolean getIsRedirectOtherAppDialog() {
        return EXTRA.optBoolean("app_redirectOtherAppStatus", false);
    }

    public static boolean getIsUpdateAppDialog() {
        return EXTRA.optBoolean("app_updateAppDialogStatus", false);
    }

    public static int getCustomRewordTimer() {
        return EXTRA.optInt("custom_reword_timer_second", 20);
    }

    public static int getInterAfterNativeTimerSecond() {
        return EXTRA.optInt("inter_after_native_timer_second", 6);
    }

    public static boolean getIsShowNativeAfterInter() {
        return EXTRA.optBoolean("is_show_native_after_inter", true);
    }

    public static int getNewVersionCode() {
        return EXTRA.optInt("app_versionCode", 0);
    }

    public static String getPrivacyPolicy() {
        return EXTRA.optString("app_PrivacyPolicyLink", "");
    }

    public static String getTermsAndCondition() {
        return EXTRA.optString("app_TermsandCondition", "");
    }

    public static boolean getIsAppMaintenance() {
        return EXTRA.optBoolean("app_maintenance", false);
    }

    public static String getGameUrl() {
        return EXTRA.optString("game_url", "");
    }

    public static String getGameIcon() {
        return EXTRA.optString("game_icon", "");
    }

    public static int getMoreApp() {
        return EXTRA.optInt("more_app_screen", 0);
    }

    public static int getExtraScreen() {
        return EXTRA.optInt("extra_screen", 0);
    }

    public static int getExtraSharedpreferences() {
        return EXTRA.optInt("extra_sharedpreferences", 0);
    }

    public static String getMoreAppsUrl() {
        return EXTRA.optString("more_apps_url", "");
    }

    public static String getNewsUrl() {
        return EXTRA.optString("news_url", "");
    }

    public static String getNewsIcon() {
        return EXTRA.optString("news_icon", "");
    }

    public static String getQuizUrl() {
        return EXTRA.optString("quiz_url", "");
    }

    public static String getQuizIcon() {
        return EXTRA.optString("quiz_icon", "");
    }

    public static String getAppMetricaId() {
        return EXTRA.optString("appmetrica_id", "");
    }

    public static String getOneSignalId() {
        return EXTRA.optString("onesignal_id", "097f3602-2a4e-40f4-bb5a-b003665d1f84");
    }

    public static String getClarityProjectId() {
        return EXTRA.optString("clarity_project_id", "");
    }

    public static String getFbApplicationId() {
        return EXTRA.optString("fb_application_id", "");
    }

    public static String getFbClientId() {
        return EXTRA.optString("fb_client_id", "");
    }

    public static String getNativePosition() {
        return EXTRA.optString("native_position", "6");
    }

    public static int getNativePositionInt() {
        try {
            return Integer.parseInt(EXTRA.optString("native_position", "6"));
        } catch (Exception unused) {
            return EXTRA.optInt("native_position", 6);
        }
    }
    public static boolean getNativeRecycle() {
        return "true".equalsIgnoreCase(EXTRA.optString("native_recycle", "true")) || EXTRA.optBoolean("native_recycle", true);
    }

    public static boolean getAppExtraScreen() {
        return "true".equalsIgnoreCase(EXTRA.optString("app_extra_screen", "false")) || EXTRA.optBoolean("app_extra_screen", false);
    }

    public static boolean getSkipIntro() {
        return "true".equalsIgnoreCase(EXTRA.optString("skip_intro", "false")) || EXTRA.optBoolean("skip_intro", false);
    }

    public static JSONObject getNativeBtnConfigValues() {
        try {
            JSONObject optJSONObject = NTV_BTN_CONFIG.optJSONObject("native_btn_config");
            if (optJSONObject == null) {
                return new JSONObject();
            }
            return new JSONObject(optJSONObject.toString());
        } catch (Exception unused) {
            return new JSONObject();
        }
    }

  /*  public static ScreenAdConfig getScreenAdConfig(String str) {
        JSONObject optJSONObject = APP_SCREEN.optJSONObject("native_banner_ads");
        if (optJSONObject == null) {
            return new ScreenAdConfig(true, "native_banner");
        }
        JSONObject optJSONObject2 = optJSONObject.optJSONObject(str);
        if (optJSONObject2 == null) {
            optJSONObject2 = optJSONObject.optJSONObject(ScreenTags.DEFAULT_TAG);
        }
        if (optJSONObject2 == null) {
            return new ScreenAdConfig(true, "native_banner");
        }
        return new ScreenAdConfig(optJSONObject2.optBoolean("status", true), optJSONObject2.optString(AppEventsConstants.EVENT_PARAM_AD_TYPE, ""));
    }*/

    public static boolean isInterEnabled(String str) {
        JSONObject optJSONObject = APP_SCREEN.optJSONObject("inter_ads");
        if (optJSONObject == null) {
            return true;
        }
        return optJSONObject.optBoolean(str, true);
    }

    public static ArrayList<String> getAdMobAdaptiveBannerIds() {
        JSONArray optJSONArray;
        ArrayList<String> arrayList = new ArrayList<>();
        JSONObject optJSONObject = ADS_CONFIG.optJSONObject("admob_adaptive_banner");
        if (optJSONObject != null && (optJSONArray = optJSONObject.optJSONArray("id")) != null) {
            for (int i = 0; i < optJSONArray.length(); i++) {
                arrayList.add(optJSONArray.optString(i, ""));
            }
        }
        return arrayList;
    }

    public static boolean getAdMobBannerStatus() {
        try {
            JSONObject optJSONObject = ADS_CONFIG.optJSONObject("admob_adaptive_banner");
            if (optJSONObject == null) {
                return false;
            }
            return optJSONObject.optBoolean("status", false);
        } catch (Exception unused) {
            return false;
        }
    }

    public static ArrayList<String> getAdMobAppOpenIds() {
        JSONArray optJSONArray;
        ArrayList<String> arrayList = new ArrayList<>();
        JSONObject optJSONObject = ADS_CONFIG.optJSONObject("admob_appopen");
        if (optJSONObject != null && (optJSONArray = optJSONObject.optJSONArray("id")) != null) {
            for (int i = 0; i < optJSONArray.length(); i++) {
                arrayList.add(optJSONArray.optString(i, ""));
            }
        }
        return arrayList;
    }

    public static boolean getAdMobAppOpenStatus() {
        try {
            JSONObject optJSONObject = ADS_CONFIG.optJSONObject("admob_appopen");
            if (optJSONObject == null) {
                return false;
            }
            return optJSONObject.optBoolean("status", false);
        } catch (Exception unused) {
            return false;
        }
    }

    public static ArrayList<String> getAdMobCollapsibleBannerIds() {
        JSONArray optJSONArray;
        ArrayList<String> arrayList = new ArrayList<>();
        JSONObject optJSONObject = ADS_CONFIG.optJSONObject("admob_collapsible_banner");
        if (optJSONObject != null && (optJSONArray = optJSONObject.optJSONArray("id")) != null) {
            for (int i = 0; i < optJSONArray.length(); i++) {
                arrayList.add(optJSONArray.optString(i));
            }
        }
        return arrayList;
    }

    public static boolean getAdMobCollapsibleBannerStatus() {
        try {
            JSONObject optJSONObject = ADS_CONFIG.optJSONObject("admob_collapsible_banner");
            if (optJSONObject == null) {
                return false;
            }
            return optJSONObject.optBoolean("status", false);
        } catch (Exception unused) {
            return false;
        }
    }

    public static ArrayList<String> getAdMobInterstitialIds() {
        JSONArray optJSONArray;
        ArrayList<String> arrayList = new ArrayList<>();
        JSONObject optJSONObject = ADS_CONFIG.optJSONObject("admob_interstitial");
        if (optJSONObject != null && (optJSONArray = optJSONObject.optJSONArray("id")) != null) {
            for (int i = 0; i < optJSONArray.length(); i++) {
                arrayList.add(optJSONArray.optString(i));
            }
        }
        return arrayList;
    }

    public static boolean getAdMobInterstitialStatus() {
        try {
            JSONObject optJSONObject = ADS_CONFIG.optJSONObject("admob_interstitial");
            if (optJSONObject == null) {
                return false;
            }
            return optJSONObject.optBoolean("status", false);
        } catch (Exception unused) {
            return false;
        }
    }

    public static ArrayList<String> getAdMobNativeIds() {
        JSONArray optJSONArray;
        ArrayList<String> arrayList = new ArrayList<>();
        JSONObject optJSONObject = ADS_CONFIG.optJSONObject("admob_native");
        if (optJSONObject != null && (optJSONArray = optJSONObject.optJSONArray("id")) != null) {
            for (int i = 0; i < optJSONArray.length(); i++) {
                arrayList.add(optJSONArray.optString(i));
            }
        }
        return arrayList;
    }

    public static boolean getAdMobNativeStatus() {
        try {
            JSONObject optJSONObject = ADS_CONFIG.optJSONObject("admob_native");
            if (optJSONObject == null) {
                return false;
            }
            return optJSONObject.optBoolean("status", false);
        } catch (Exception unused) {
            return false;
        }
    }

    public static ArrayList<String> getAdMobRewardedIds() {
        JSONArray optJSONArray;
        ArrayList<String> arrayList = new ArrayList<>();
        JSONObject optJSONObject = ADS_CONFIG.optJSONObject("admob_rewarded");
        if (optJSONObject != null) {
            if (optJSONObject.optBoolean("status", false) && (optJSONArray = optJSONObject.optJSONArray("id")) != null) {
                for (int i = 0; i < optJSONArray.length(); i++) {
                    arrayList.add(optJSONArray.optString(i));
                }
            }
        }
        return arrayList;
    }

    public static ArrayList<String> getFacebookBannerIds() {
        JSONArray optJSONArray;
        ArrayList<String> arrayList = new ArrayList<>();
        JSONObject optJSONObject = ADS_CONFIG.optJSONObject("facebook_banner");
        if (optJSONObject != null && (optJSONArray = optJSONObject.optJSONArray("id")) != null) {
            for (int i = 0; i < optJSONArray.length(); i++) {
                arrayList.add(optJSONArray.optString(i));
            }
        }
        return arrayList;
    }

    public static boolean getFacebookBannerStatus() {
        try {
            JSONObject optJSONObject = ADS_CONFIG.optJSONObject("facebook_banner");
            if (optJSONObject == null) {
                return false;
            }
            return optJSONObject.optBoolean("status", false);
        } catch (Exception unused) {
            return false;
        }
    }

    public static ArrayList<String> getFacebookInterstitialIds() {
        JSONArray optJSONArray;
        ArrayList<String> arrayList = new ArrayList<>();
        JSONObject optJSONObject = ADS_CONFIG.optJSONObject("facebook_interstitial");
        if (optJSONObject != null && (optJSONArray = optJSONObject.optJSONArray("id")) != null) {
            for (int i = 0; i < optJSONArray.length(); i++) {
                arrayList.add(optJSONArray.optString(i));
            }
        }
        return arrayList;
    }

    public static boolean getFacebookInterstitialStatus() {
        try {
            JSONObject optJSONObject = ADS_CONFIG.optJSONObject("facebook_interstitial");
            if (optJSONObject == null) {
                return false;
            }
            return optJSONObject.optBoolean("status", false);
        } catch (Exception unused) {
            return false;
        }
    }

    public static ArrayList<String> getFacebookNativeIds() {
        JSONArray optJSONArray;
        ArrayList<String> arrayList = new ArrayList<>();
        JSONObject optJSONObject = ADS_CONFIG.optJSONObject("facebook_native");
        if (optJSONObject != null && (optJSONArray = optJSONObject.optJSONArray("id")) != null) {
            for (int i = 0; i < optJSONArray.length(); i++) {
                arrayList.add(optJSONArray.optString(i));
            }
        }
        return arrayList;
    }

    public static boolean getFacebookNativeStatus() {
        try {
            JSONObject optJSONObject = ADS_CONFIG.optJSONObject("facebook_native");
            if (optJSONObject == null) {
                return false;
            }
            return optJSONObject.optBoolean("status", false);
        } catch (Exception unused) {
            return false;
        }
    }

    public static ArrayList<String> getFacebookRewardedIds() {
        JSONArray optJSONArray;
        ArrayList<String> arrayList = new ArrayList<>();
        JSONObject optJSONObject = ADS_CONFIG.optJSONObject("facebook_rewarded");
        if (optJSONObject != null) {
            if (optJSONObject.optBoolean("status", false) && (optJSONArray = optJSONObject.optJSONArray("id")) != null) {
                for (int i = 0; i < optJSONArray.length(); i++) {
                    arrayList.add(optJSONArray.optString(i));
                }
            }
        }
        return arrayList;
    }

    /*public static List<CustomBannerModel> getCustomBannerId() {
        JSONArray optJSONArray;
        ArrayList arrayList = new ArrayList();
        JSONObject optJSONObject = ADS_CONFIG.optJSONObject("custom_banner");
        if (optJSONObject != null && (optJSONArray = optJSONObject.optJSONArray("id")) != null) {
            for (int i = 0; i < optJSONArray.length(); i++) {
                JSONObject optJSONObject2 = optJSONArray.optJSONObject(i);
                if (optJSONObject2 != null) {
                    arrayList.add(new CustomBannerModel(optJSONObject2.optString("title", ""), optJSONObject2.optString("body", ""), optJSONObject2.optString("icon", ""), optJSONObject2.optString("image", ""), optJSONObject2.optString("url", ""), optJSONObject2.optString("nt_btn_txt", "")));
                }
            }
        }
        return arrayList;
    }*/

/*    public static List<CustomNativeModel> getCustomNativeId() {
        JSONArray optJSONArray;
        ArrayList arrayList = new ArrayList();
        JSONObject optJSONObject = ADS_CONFIG.optJSONObject("custom_native");
        if (optJSONObject != null && (optJSONArray = optJSONObject.optJSONArray("id")) != null) {
            for (int i = 0; i < optJSONArray.length(); i++) {
                JSONObject optJSONObject2 = optJSONArray.optJSONObject(i);
                if (optJSONObject2 != null) {
                    arrayList.add(new CustomNativeModel(optJSONObject2.optString("title", ""), optJSONObject2.optString("body", ""), optJSONObject2.optString("icon", ""), optJSONObject2.optString("image", ""), optJSONObject2.optString("url", ""), optJSONObject2.optString("btn_Text", "")));
                }
            }
        }
        return arrayList;
    }*/

    public static ArrayList<String> getAfterInterCustomLinks() {
        JSONArray optJSONArray;
        ArrayList<String> arrayList = new ArrayList<>();
        JSONObject optJSONObject = ADS_CONFIG.optJSONObject("after_inter_custom_links_max3_min1");
        if (optJSONObject != null && (optJSONArray = optJSONObject.optJSONArray("id")) != null) {
            for (int i = 0; i < optJSONArray.length(); i++) {
                arrayList.add(optJSONArray.optString(i));
            }
        }
        return arrayList;
    }

    public static String getCustomInterstitialId() {
        JSONArray optJSONArray;
        JSONObject optJSONObject = ADS_CONFIG.optJSONObject("custom_interstitial");
        if (optJSONObject == null || (optJSONArray = optJSONObject.optJSONArray("id")) == null || optJSONArray.length() == 0) {
            return "";
        }
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < optJSONArray.length(); i++) {
            JSONObject optJSONObject2 = optJSONArray.optJSONObject(i);
            if (optJSONObject2 != null) {
                String optString = optJSONObject2.optString("url", "");
                if (!optString.isEmpty()) {
                    arrayList.add(optString);
                }
            }
        }
        if (arrayList.isEmpty()) {
            return "";
        }
        return (String) arrayList.get(new Random().nextInt(arrayList.size()));
    }

    public static String getBackCustomInterstitialId() {
        JSONArray optJSONArray;
        JSONObject optJSONObject = ADS_CONFIG.optJSONObject("back_custom_interstitial");
        if (optJSONObject == null || (optJSONArray = optJSONObject.optJSONArray("id")) == null || optJSONArray.length() == 0) {
            return "";
        }
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < optJSONArray.length(); i++) {
            JSONObject optJSONObject2 = optJSONArray.optJSONObject(i);
            if (optJSONObject2 != null) {
                String optString = optJSONObject2.optString("url", "");
                if (!optString.isEmpty()) {
                    arrayList.add(optString);
                }
            }
        }
        if (arrayList.isEmpty()) {
            return "";
        }
        return (String) arrayList.get(new Random().nextInt(arrayList.size()));
    }

    public static String getCustomRewardAdUrl() {
        JSONArray optJSONArray;
        JSONObject optJSONObject = ADS_CONFIG.optJSONObject("custom_rewarded_ad_url");
        if (optJSONObject == null || (optJSONArray = optJSONObject.optJSONArray("id")) == null || optJSONArray.length() == 0) {
            return "";
        }
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < optJSONArray.length(); i++) {
            JSONObject optJSONObject2 = optJSONArray.optJSONObject(i);
            if (optJSONObject2 != null) {
                String optString = optJSONObject2.optString("url", "");
                if (!optString.isEmpty()) {
                    arrayList.add(optString);
                }
            }
        }
        if (arrayList.isEmpty()) {
            return "";
        }
        return (String) arrayList.get(new Random().nextInt(arrayList.size()));
    }

   /* public static List<MoreAppModel> getMoreAppsPageData() {
        ArrayList arrayList = new ArrayList();
        if (MORE_APPS_PAGE.length() != 0) {
            int i = 1;
            while (true) {
                JSONObject optJSONObject = MORE_APPS_PAGE.optJSONObject("app_order" + i);
                if (optJSONObject == null) {
                    break;
                }
                arrayList.add(new MoreAppModel(optJSONObject.optString("app_adtext", ""), optJSONObject.optString("app_button_text", ""), optJSONObject.optString("app_logo", ""), optJSONObject.optString(NativeProtocol.BRIDGE_ARG_APP_NAME_STRING, ""), optJSONObject.optString("app_text", ""), optJSONObject.optString("app_url", "")));
                i++;
            }
        }
        return arrayList;
    }*/
}

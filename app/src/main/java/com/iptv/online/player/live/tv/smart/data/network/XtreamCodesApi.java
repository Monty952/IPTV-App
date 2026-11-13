package com.iptv.online.player.live.tv.smart.data.network;

import android.os.Handler;
import android.os.Looper;

//import androidx.media3.extractor.text.ttml.TtmlNode;

//import com.google.common.net.HttpHeaders;
//import com.google.firebase.sessions.settings.RemoteSettings;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.iptv.online.player.live.tv.smart.data.model.Category;
import com.iptv.online.player.live.tv.smart.data.model.EpisodeItem;
import com.iptv.online.player.live.tv.smart.data.model.PlaylistItem;
import com.iptv.online.player.live.tv.smart.data.model.PlaylistSource;
import com.iptv.online.player.live.tv.smart.data.model.SeriesInfo;
import com.iptv.online.player.live.tv.smart.data.model.VodCategory;
import com.iptv.online.player.live.tv.smart.data.model.VodItem;
import com.iptv.online.player.live.tv.smart.data.model.XtreamUserInfo;
import com.iptv.online.player.live.tv.smart.data.network.XtreamCodesApi;
import com.iptv.online.player.live.tv.smart.data.parser.M3UParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@SuppressWarnings("all")

/* loaded from: classes5.dex */
public class XtreamCodesApi {
    private static final Gson gson = new Gson();

    /* loaded from: classes5.dex */
    public interface ApiCallback<T> {
        void onError(Exception exc);

        void onSuccess(T t);
    }

    public static void postToMainThread(Runnable runnable) {
        try {
            new Handler(Looper.getMainLooper()).post(runnable);
        } catch (Throwable unused) {
            runnable.run();
        }
    }

    public static void authenticateAndLoad(PlaylistSource playlistSource, ApiCallback<M3UParser.ParseResult> apiCallback) {
        OkHttpClient client = ApiClient.getClient();
        String formatServerUrl = formatServerUrl(playlistSource.getUrl());
        String username = playlistSource.getUsername();
        String password = playlistSource.getPassword();
        client.newCall(new Request.Builder().url(formatServerUrl + "/player_api.php?username="
                + username + "&password=" + password).header("User-Agent",
                ApiClient.DEFAULT_USER_AGENT).build()).enqueue(new AnonymousClass1(apiCallback, formatServerUrl, username, password));
    }

    /* renamed from: com.iptv.online.player.live.tv.smart.data.network.XtreamCodesApi$1 */
    /* loaded from: classes5.dex */
    public static class AnonymousClass1 implements Callback {
        final /* synthetic */ String val$baseUrl;
        final /* synthetic */ ApiCallback val$callback;
        final /* synthetic */ String val$password;
        final /* synthetic */ String val$username;

        AnonymousClass1(ApiCallback apiCallback, String str, String str2, String str3) {
            this.val$callback = apiCallback;
            this.val$baseUrl = str;
            this.val$username = str2;
            this.val$password = str3;
        }

        @Override // okhttp3.Callback
        public void onFailure(Call call, final IOException iOException) {
            final ApiCallback apiCallback = this.val$callback;
            XtreamCodesApi.postToMainThread(new Runnable() { // from class: com.iptv.online.player.live.tv.smart.data.network.XtreamCodesApi$1$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    apiCallback.onError(iOException);
                }
            });
        }

        @Override // okhttp3.Callback
        public void onResponse(Call call, final Response response) throws IOException {
            if (!response.isSuccessful() || response.body() == null) {
                final ApiCallback apiCallback = this.val$callback;
                XtreamCodesApi.postToMainThread(new Runnable() { // from class: com.iptv.online.player.live.tv.smart.data.network.XtreamCodesApi$1$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        XtreamCodesApi.ApiCallback apiCallback2 = apiCallback;
                        Response response2 = response;
                        apiCallback2.onError(new IOException("Server returned error: " + response2.code()));
                    }
                });
                return;
            }
            try {
//                XtreamUserInfo xtreamUserInfo = (XtreamUserInfo) XtreamCodesApi.gson.fromJson(response.body().string(), (Class<Object>) XtreamUserInfo.class);
                XtreamUserInfo xtreamUserInfo = XtreamCodesApi.gson.fromJson(
                        response.body().string(),
                        XtreamUserInfo.class
                );
                if (xtreamUserInfo != null && xtreamUserInfo.getUserInfo() != null && !"Disabled".equalsIgnoreCase(xtreamUserInfo.getUserInfo().getStatus()) && !"Expired".equalsIgnoreCase(xtreamUserInfo.getUserInfo().getStatus())) {
                    XtreamCodesApi.fetchCategoriesAndStreams(this.val$baseUrl, this.val$username, this.val$password, this.val$callback);
                    return;
                }
                final ApiCallback apiCallback2 = this.val$callback;
                XtreamCodesApi.postToMainThread(new Runnable() { // from class: com.iptv.online.player.live.tv.smart.data.network.XtreamCodesApi$1$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        apiCallback2.onError(new Exception("Account is inactive, expired, or invalid credentials."));
                    }
                });
            } catch (Exception e) {
                final ApiCallback apiCallback3 = this.val$callback;
                XtreamCodesApi.postToMainThread(new Runnable() { // from class: com.iptv.online.player.live.tv.smart.data.network.XtreamCodesApi$1$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        apiCallback3.onError(e);
                    }
                });
            }
        }
    }

    public static void fetchCategoriesAndStreams(final String str, final String str2, final String str3, final ApiCallback<M3UParser.ParseResult> apiCallback) {
        ApiClient.getClient().newCall(new Request.Builder().url(str + "/player_api.php?username=" + str2 + "&password=" + str3 + "&action=get_live_categories").header("User-Agent", ApiClient.DEFAULT_USER_AGENT).build()).enqueue(new Callback() { // from class: com.iptv.online.player.live.tv.smart.data.network.XtreamCodesApi.2
            @Override // okhttp3.Callback
            public void onFailure(Call call, IOException iOException) {
                XtreamCodesApi.fetchStreamsOnly(str, str2, str3, new HashMap(), apiCallback);
            }

            @Override // okhttp3.Callback
            public void onResponse(Call call, Response response) throws IOException {
                HashMap hashMap = new HashMap();
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        JsonArray jsonArray = (JsonArray) XtreamCodesApi.gson.fromJson(response.body().string(), JsonArray.class);
                        if (jsonArray != null) {
                            Iterator<JsonElement> it = jsonArray.iterator();
                            while (it.hasNext()) {
                                JsonObject asJsonObject = it.next().getAsJsonObject();
                                String asString = asJsonObject.has("category_id") ? asJsonObject.get("category_id").getAsString() : "";
                                hashMap.put(asString, new Category(asString, asJsonObject.has("category_name") ? asJsonObject.get("category_name").getAsString() : "General"));
                            }
                        }
                    } catch (Exception unused) {
                    }
                }
                XtreamCodesApi.fetchStreamsOnly(str, str2, str3, hashMap, apiCallback);
            }
        });
    }

    public static void fetchStreamsOnly(String str, String str2, String str3, Map<String, Category> map, ApiCallback<M3UParser.ParseResult> apiCallback) {
        ApiClient.getClient().newCall(new Request.Builder().url(str + "/player_api.php?username=" + str2 + "&password=" + str3 + "&action=get_live_streams").header("User-Agent", ApiClient.DEFAULT_USER_AGENT).build()).enqueue(new AnonymousClass3(apiCallback, str, str2, str3, map));
    }

    /* renamed from: com.iptv.online.player.live.tv.smart.data.network.XtreamCodesApi$3 */
    /* loaded from: classes5.dex */
    public static class AnonymousClass3 implements Callback {
        final /* synthetic */ String val$baseUrl;
        final /* synthetic */ ApiCallback val$callback;
        final /* synthetic */ Map val$categoryMap;
        final /* synthetic */ String val$password;
        final /* synthetic */ String val$username;

        AnonymousClass3(ApiCallback apiCallback, String str, String str2, String str3, Map map) {
            this.val$callback = apiCallback;
            this.val$baseUrl = str;
            this.val$username = str2;
            this.val$password = str3;
            this.val$categoryMap = map;
        }

        @Override // okhttp3.Callback
        public void onFailure(Call call, final IOException iOException) {
            final ApiCallback apiCallback = this.val$callback;
            XtreamCodesApi.postToMainThread(new Runnable() { // from class: com.iptv.online.player.live.tv.smart.data.network.XtreamCodesApi$3$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    apiCallback.onError(iOException);
                }
            });
        }

        @Override // okhttp3.Callback
        public void onResponse(Call call, final Response response) throws IOException {
            if (!response.isSuccessful() || response.body() == null) {
                final ApiCallback apiCallback = this.val$callback;
                XtreamCodesApi.postToMainThread(new Runnable() { // from class: com.iptv.online.player.live.tv.smart.data.network.XtreamCodesApi$3$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        XtreamCodesApi.ApiCallback apiCallback2 = apiCallback;
                        Response response2 = response;
                        apiCallback2.onError(new IOException("Failed to fetch live streams: " + response2.code()));
                    }
                });
                return;
            }
            try {
                String string = response.body().string();
                final M3UParser.ParseResult buildXtreamStreams = new ParseResultBuilder().buildXtreamStreams(this.val$baseUrl, this.val$username, this.val$password, (JsonArray) XtreamCodesApi.gson.fromJson(string, JsonArray.class), this.val$categoryMap);
                final ApiCallback apiCallback2 = this.val$callback;
                XtreamCodesApi.postToMainThread(new Runnable() { // from class: com.iptv.online.player.live.tv.smart.data.network.XtreamCodesApi$3$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        apiCallback2.onSuccess(buildXtreamStreams);
                    }
                });
            } catch (Exception e) {
                final ApiCallback apiCallback3 = this.val$callback;
                XtreamCodesApi.postToMainThread(new Runnable() { // from class: com.iptv.online.player.live.tv.smart.data.network.XtreamCodesApi$3$$ExternalSyntheticLambda3
                    @Override // java.lang.Runnable
                    public final void run() {
                        apiCallback3.onError(e);
                    }
                });
            }
        }
    }

    /* loaded from: classes5.dex */
    private static class ParseResultBuilder {
        private ParseResultBuilder() {
        }

        M3UParser.ParseResult buildXtreamStreams(String str, String str2, String str3, JsonArray jsonArray, Map<String, Category> map) {
            M3UParser.ParseResult parseResult = new M3UParser.ParseResult();
            HashMap hashMap = new HashMap(map);
            if (jsonArray != null) {
                Iterator<JsonElement> it = jsonArray.iterator();
                while (it.hasNext()) {
                    JsonElement next = it.next();
                    if (next.isJsonObject()) {
                        JsonObject asJsonObject = next.getAsJsonObject();
                        int asInt = asJsonObject.has("stream_id") ? asJsonObject.get("stream_id").getAsInt() : 0;
                        String asString = asJsonObject.has("name") ? asJsonObject.get("name").getAsString() : "Channel " + asInt;
                        String asString2 = (!asJsonObject.has("stream_icon") || asJsonObject.get("stream_icon").isJsonNull()) ? null : asJsonObject.get("stream_icon").getAsString();
                        String asString3 = (!asJsonObject.has("category_id") || asJsonObject.get("category_id").isJsonNull()) ? "0" : asJsonObject.get("category_id").getAsString();
                        String asString4 = (!asJsonObject.has("container_extension") || asJsonObject.get("container_extension").isJsonNull()) ? "ts" : asJsonObject.get("container_extension").getAsString();
                        String str4 = asString4.isEmpty() ? "ts" : asString4;
                        String str5 = str + "/live/" + str2 + "/" + str3 + "/" + asInt + "." + str4;
                        Category category = (Category) hashMap.get(asString3);
                        String name = category != null ? category.getName() : "General";
                        PlaylistItem playlistItem = new PlaylistItem();
                        Iterator<JsonElement> it2 = it;
                        playlistItem.setId("xtream_" + asInt);
                        playlistItem.setStreamId(asInt);
                        playlistItem.setName(asString);
                        playlistItem.setStreamUrl(str5);
                        playlistItem.setLogoUrl(asString2);
                        playlistItem.setGroupTitle(name);
                        playlistItem.setContainerExtension(str4);
                        parseResult.channels.add(playlistItem);
                        if (category == null) {
                            category = new Category(asString3, name);
                            hashMap.put(asString3, category);
                        }
                        category.addChannel(playlistItem);
                        it = it2;
                    }
                }
            }
            for (Object obj : hashMap.values()) {
                Category category2 = (Category) obj;

                if (category2.getChannelCount() > 0) {
                    parseResult.categories.add(category2);
                }
            }
           /* for (Category category2 : hashMap.values()) {
                if (category2.getChannelCount() > 0) {
                    parseResult.categories.add(category2);
                }
            }*/
            return parseResult;
        }
    }

    public static String formatServerUrl(String str) {
        if (str == null) {
            return "";
        }
        String trim = str.trim();
        if (trim.isEmpty()) {
            return "";
        }
        if (!trim.startsWith("http://") && !trim.startsWith("https://")) {
            trim = "http://" + trim;
        }
        return trim.endsWith("/") ? trim.substring(0, trim.length() - 1) : trim;
    }

    public static void fetchVodCategories(PlaylistSource playlistSource, final ApiCallback<List<VodCategory>> apiCallback) {
        if (playlistSource == null || playlistSource.getType() != PlaylistSource.SourceType.XTREAM_CODES) {
            postToMainThread(new Runnable() { // from class: com.iptv.online.player.live.tv.smart.data.network.XtreamCodesApi$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    apiCallback.onSuccess(XtreamCodesApi.getDemoVodCategories());
                }
            });
            return;
        }
        ApiClient.getClient().newCall(new Request.Builder().url(formatServerUrl(playlistSource.getUrl())
                + "/player_api.php?username=" + playlistSource.getUsername() + "&password="
                + playlistSource.getPassword() + "&action=get_vod_categories").header("User-Agent",
                ApiClient.DEFAULT_USER_AGENT).build()).enqueue(new AnonymousClass4(apiCallback));
    }

    /* renamed from: com.iptv.online.player.live.tv.smart.data.network.XtreamCodesApi$4 */
    /* loaded from: classes5.dex */
    public static class AnonymousClass4 implements Callback {
        final /* synthetic */ ApiCallback val$callback;

        AnonymousClass4(ApiCallback apiCallback) {
            this.val$callback = apiCallback;
        }

        @Override // okhttp3.Callback
        public void onFailure(Call call, IOException iOException) {
            final ApiCallback apiCallback = this.val$callback;
            XtreamCodesApi.postToMainThread(new Runnable() { // from class: com.iptv.online.player.live.tv.smart.data.network.XtreamCodesApi$4$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    apiCallback.onSuccess(XtreamCodesApi.getDemoVodCategories());
                }
            });
        }

        @Override // okhttp3.Callback
        public void onResponse(Call call, Response response) throws IOException {
            final ArrayList arrayList = new ArrayList();
            arrayList.add(new VodCategory("all", "All Movies"));
            if (response.isSuccessful() && response.body() != null) {
                try {
                    JsonArray jsonArray = (JsonArray) XtreamCodesApi.gson.fromJson(response.body().string(), JsonArray.class);
                    if (jsonArray != null) {
                        Iterator<JsonElement> it = jsonArray.iterator();
                        while (it.hasNext()) {
                            JsonElement next = it.next();
                            if (next.isJsonObject()) {
                                JsonObject asJsonObject = next.getAsJsonObject();
                                arrayList.add(new VodCategory(asJsonObject.has("category_id") ? asJsonObject.get("category_id").getAsString() : "", asJsonObject.has("category_name") ? asJsonObject.get("category_name").getAsString() : "General"));
                            }
                        }
                    }
                } catch (Exception unused) {
                }
            }
            final ApiCallback apiCallback = this.val$callback;
            XtreamCodesApi.postToMainThread(new Runnable() { // from class: com.iptv.online.player.live.tv.smart.data.network.XtreamCodesApi$4$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    apiCallback.onSuccess(arrayList);
                }
            });
        }
    }

    public static void fetchVodStreams(PlaylistSource playlistSource, final String str, final ApiCallback<List<VodItem>> apiCallback) {
        if (playlistSource == null || playlistSource.getType() != PlaylistSource.SourceType.XTREAM_CODES) {
            postToMainThread(new Runnable() { // from class: com.iptv.online.player.live.tv.smart.data.network.XtreamCodesApi$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    apiCallback.onSuccess(XtreamCodesApi.getDemoMovies(str));
                }
            });
            return;
        }
        String formatServerUrl = formatServerUrl(playlistSource.getUrl());
        String username = playlistSource.getUsername();
        String password = playlistSource.getPassword();
        String str2 = formatServerUrl + "/player_api.php?username=" + username + "&password=" + password + "&action=get_vod_streams";
        if (str != null && !str.isEmpty() && !"all".equalsIgnoreCase(str)) {
            str2 = str2 + "&category_id=" + str;
        }
        ApiClient.getClient().newCall(new Request.Builder().url(str2).header("User-Agent",
                ApiClient.DEFAULT_USER_AGENT).build()).enqueue(new AnonymousClass5(apiCallback, str, formatServerUrl, username, password));
    }

    /* renamed from: com.iptv.online.player.live.tv.smart.data.network.XtreamCodesApi$5 */
    /* loaded from: classes5.dex */
    public static class AnonymousClass5 implements Callback {
        final /* synthetic */ String val$baseUrl;
        final /* synthetic */ ApiCallback val$callback;
        final /* synthetic */ String val$categoryId;
        final /* synthetic */ String val$password;
        final /* synthetic */ String val$username;

        AnonymousClass5(ApiCallback apiCallback, String str, String str2, String str3, String str4) {
            this.val$callback = apiCallback;
            this.val$categoryId = str;
            this.val$baseUrl = str2;
            this.val$username = str3;
            this.val$password = str4;
        }

        @Override // okhttp3.Callback
        public void onFailure(Call call, IOException iOException) {
            final ApiCallback apiCallback = this.val$callback;
            final String str = this.val$categoryId;
            XtreamCodesApi.postToMainThread(new Runnable() { // from class: com.iptv.online.player.live.tv.smart.data.network.XtreamCodesApi$5$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    apiCallback.onSuccess(XtreamCodesApi.getDemoMovies(str));
                }
            });
        }

        @Override // okhttp3.Callback
        public void onResponse(Call call, Response response) throws IOException {
            String str = "/";
            String str2 = "name";
            String str3 = "stream_id";
            String str4 = "container_extension";
            if (!response.isSuccessful() || response.body() == null) {
                final ApiCallback apiCallback = this.val$callback;
                final String str5 = this.val$categoryId;
                XtreamCodesApi.postToMainThread(new Runnable() { // from class: com.iptv.online.player.live.tv.smart.data.network.XtreamCodesApi$5$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        apiCallback.onSuccess(XtreamCodesApi.getDemoMovies(str5));
                    }
                });
                return;
            }
            try {
                JsonArray jsonArray = (JsonArray) XtreamCodesApi.gson.fromJson(response.body().string(), JsonArray.class);
                final ArrayList arrayList = new ArrayList();
                if (jsonArray != null) {
                    Iterator<JsonElement> it = jsonArray.iterator();
                    while (it.hasNext()) {
                        JsonElement next = it.next();
                        if (next.isJsonObject()) {
                            JsonObject asJsonObject = next.getAsJsonObject();
                            int asInt = asJsonObject.has(str3) ? asJsonObject.get(str3).getAsInt() : 0;
                            String asString = asJsonObject.has(str2) ? asJsonObject.get(str2).getAsString() : "Movie " + asInt;
                            String asString2 = (!asJsonObject.has("stream_icon") || asJsonObject.get("stream_icon").isJsonNull()) ? null : asJsonObject.get("stream_icon").getAsString();
                            String asString3 = (!asJsonObject.has("rating") || asJsonObject.get("rating").isJsonNull()) ? "7.5" : asJsonObject.get("rating").getAsString();
                            String asString4 = (!asJsonObject.has("category_id") || asJsonObject.get("category_id").isJsonNull()) ? "" : asJsonObject.get("category_id").getAsString();
                            String asString5 = (!asJsonObject.has(str4) || asJsonObject.get(str4).isJsonNull()) ? "mp4" : asJsonObject.get(str4).getAsString();
                            if (asString5.isEmpty()) {
                                asString5 = "mp4";
                            }
                            String str6 = str2;
                            String str7 = str3;
                            VodItem vodItem = new VodItem();
                            vodItem.setId("movie_" + asInt);
                            vodItem.setStreamId(asInt);
                            vodItem.setName(asString);
                            vodItem.setStreamIcon(asString2);
                            vodItem.setRating(asString3);
                            vodItem.setCategoryId(asString4);
                            vodItem.setContainerExtension(asString5);
                            vodItem.setStreamUrl(this.val$baseUrl + "/movie/" + this.val$username + str + this.val$password + str + asInt + "." + asString5);
                            vodItem.setSeries(false);
                            arrayList.add(vodItem);
                            str2 = str6;
                            str3 = str7;
                            str = str;
                            str4 = str4;
                        }
                    }
                }
                final ApiCallback apiCallback2 = this.val$callback;
                final String str8 = this.val$categoryId;
                XtreamCodesApi.postToMainThread(new Runnable() { // from class: com.iptv.online.player.live.tv.smart.data.network.XtreamCodesApi$5$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        XtreamCodesApi.AnonymousClass5.lambda$onResponse$2(apiCallback2, arrayList, str8);
                    }
                });
            } catch (Exception unused) {
                final ApiCallback apiCallback3 = this.val$callback;
                final String str9 = this.val$categoryId;
                XtreamCodesApi.postToMainThread(new Runnable() { // from class: com.iptv.online.player.live.tv.smart.data.network.XtreamCodesApi$5$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        apiCallback3.onSuccess(XtreamCodesApi.getDemoMovies(str9));
                    }
                });
            }
        }

        public static /* synthetic */ void lambda$onResponse$2(ApiCallback apiCallback, List list, String str) {
            if (list.isEmpty()) {
                list = XtreamCodesApi.getDemoMovies(str);
            }
            apiCallback.onSuccess(list);
        }
    }

    public static void fetchVodInfo(PlaylistSource playlistSource, int i, final ApiCallback<VodItem> apiCallback) {
        if (playlistSource == null || playlistSource.getType() != PlaylistSource.SourceType.XTREAM_CODES) {
            postToMainThread(new Runnable() { // from class: com.iptv.online.player.live.tv.smart.data.network.XtreamCodesApi$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    apiCallback.onError(new Exception("Not Xtream Codes source"));
                }
            });
            return;
        }
        String formatServerUrl = formatServerUrl(playlistSource.getUrl());
        ApiClient.getClient().newCall(new Request.Builder().url(formatServerUrl + "/player_api.php?username=" + playlistSource.getUsername() + "&password=" + playlistSource.getPassword() + "&action=get_vod_info&vod_id=" + i).header("User-Agent", ApiClient.DEFAULT_USER_AGENT).build()).enqueue(new AnonymousClass6(apiCallback, i, formatServerUrl, playlistSource));
    }

    /* renamed from: com.iptv.online.player.live.tv.smart.data.network.XtreamCodesApi$6 */
    /* loaded from: classes5.dex */
    public static class AnonymousClass6 implements Callback {
        final /* synthetic */ String val$baseUrl;
        final /* synthetic */ ApiCallback val$callback;
        final /* synthetic */ PlaylistSource val$source;
        final /* synthetic */ int val$vodId;

        AnonymousClass6(ApiCallback apiCallback, int i, String str, PlaylistSource playlistSource) {
            this.val$callback = apiCallback;
            this.val$vodId = i;
            this.val$baseUrl = str;
            this.val$source = playlistSource;
        }

        @Override // okhttp3.Callback
        public void onFailure(Call call, final IOException iOException) {
            final ApiCallback apiCallback = this.val$callback;
            XtreamCodesApi.postToMainThread(new Runnable() { // from class: com.iptv.online.player.live.tv.smart.data.network.XtreamCodesApi$6$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    apiCallback.onError(iOException);
                }
            });
        }

        /* JADX WARN: Removed duplicated region for block: B:153:0x008f A[Catch: Exception -> 0x017a, TryCatch #0 {Exception -> 0x017a, blocks: (B:135:0x002a, B:137:0x0042, B:139:0x0048, B:141:0x005c, B:143:0x0067, B:145:0x0071, B:146:0x0075, B:151:0x0086, B:153:0x008f, B:155:0x0099, B:157:0x00a2, B:159:0x00ac, B:161:0x00b5, B:163:0x00bf, B:165:0x00c8, B:167:0x00d2, B:169:0x00db, B:171:0x00e5, B:173:0x00ee, B:175:0x00f9, B:177:0x0102, B:178:0x010a, B:180:0x0113, B:182:0x011e, B:147:0x007a, B:149:0x0080, B:184:0x016f), top: B:191:0x002a }] */
        /* JADX WARN: Removed duplicated region for block: B:154:0x0098  */
        /* JADX WARN: Removed duplicated region for block: B:157:0x00a2 A[Catch: Exception -> 0x017a, TryCatch #0 {Exception -> 0x017a, blocks: (B:135:0x002a, B:137:0x0042, B:139:0x0048, B:141:0x005c, B:143:0x0067, B:145:0x0071, B:146:0x0075, B:151:0x0086, B:153:0x008f, B:155:0x0099, B:157:0x00a2, B:159:0x00ac, B:161:0x00b5, B:163:0x00bf, B:165:0x00c8, B:167:0x00d2, B:169:0x00db, B:171:0x00e5, B:173:0x00ee, B:175:0x00f9, B:177:0x0102, B:178:0x010a, B:180:0x0113, B:182:0x011e, B:147:0x007a, B:149:0x0080, B:184:0x016f), top: B:191:0x002a }] */
        /* JADX WARN: Removed duplicated region for block: B:158:0x00ab  */
        /* JADX WARN: Removed duplicated region for block: B:161:0x00b5 A[Catch: Exception -> 0x017a, TryCatch #0 {Exception -> 0x017a, blocks: (B:135:0x002a, B:137:0x0042, B:139:0x0048, B:141:0x005c, B:143:0x0067, B:145:0x0071, B:146:0x0075, B:151:0x0086, B:153:0x008f, B:155:0x0099, B:157:0x00a2, B:159:0x00ac, B:161:0x00b5, B:163:0x00bf, B:165:0x00c8, B:167:0x00d2, B:169:0x00db, B:171:0x00e5, B:173:0x00ee, B:175:0x00f9, B:177:0x0102, B:178:0x010a, B:180:0x0113, B:182:0x011e, B:147:0x007a, B:149:0x0080, B:184:0x016f), top: B:191:0x002a }] */
        /* JADX WARN: Removed duplicated region for block: B:162:0x00be  */
        /* JADX WARN: Removed duplicated region for block: B:165:0x00c8 A[Catch: Exception -> 0x017a, TryCatch #0 {Exception -> 0x017a, blocks: (B:135:0x002a, B:137:0x0042, B:139:0x0048, B:141:0x005c, B:143:0x0067, B:145:0x0071, B:146:0x0075, B:151:0x0086, B:153:0x008f, B:155:0x0099, B:157:0x00a2, B:159:0x00ac, B:161:0x00b5, B:163:0x00bf, B:165:0x00c8, B:167:0x00d2, B:169:0x00db, B:171:0x00e5, B:173:0x00ee, B:175:0x00f9, B:177:0x0102, B:178:0x010a, B:180:0x0113, B:182:0x011e, B:147:0x007a, B:149:0x0080, B:184:0x016f), top: B:191:0x002a }] */
        /* JADX WARN: Removed duplicated region for block: B:166:0x00d1  */
        /* JADX WARN: Removed duplicated region for block: B:169:0x00db A[Catch: Exception -> 0x017a, TryCatch #0 {Exception -> 0x017a, blocks: (B:135:0x002a, B:137:0x0042, B:139:0x0048, B:141:0x005c, B:143:0x0067, B:145:0x0071, B:146:0x0075, B:151:0x0086, B:153:0x008f, B:155:0x0099, B:157:0x00a2, B:159:0x00ac, B:161:0x00b5, B:163:0x00bf, B:165:0x00c8, B:167:0x00d2, B:169:0x00db, B:171:0x00e5, B:173:0x00ee, B:175:0x00f9, B:177:0x0102, B:178:0x010a, B:180:0x0113, B:182:0x011e, B:147:0x007a, B:149:0x0080, B:184:0x016f), top: B:191:0x002a }] */
        /* JADX WARN: Removed duplicated region for block: B:170:0x00e4  */
        /* JADX WARN: Removed duplicated region for block: B:173:0x00ee A[Catch: Exception -> 0x017a, TryCatch #0 {Exception -> 0x017a, blocks: (B:135:0x002a, B:137:0x0042, B:139:0x0048, B:141:0x005c, B:143:0x0067, B:145:0x0071, B:146:0x0075, B:151:0x0086, B:153:0x008f, B:155:0x0099, B:157:0x00a2, B:159:0x00ac, B:161:0x00b5, B:163:0x00bf, B:165:0x00c8, B:167:0x00d2, B:169:0x00db, B:171:0x00e5, B:173:0x00ee, B:175:0x00f9, B:177:0x0102, B:178:0x010a, B:180:0x0113, B:182:0x011e, B:147:0x007a, B:149:0x0080, B:184:0x016f), top: B:191:0x002a }] */
        /* JADX WARN: Removed duplicated region for block: B:174:0x00f7  */
        /* JADX WARN: Removed duplicated region for block: B:177:0x0102 A[Catch: Exception -> 0x017a, TryCatch #0 {Exception -> 0x017a, blocks: (B:135:0x002a, B:137:0x0042, B:139:0x0048, B:141:0x005c, B:143:0x0067, B:145:0x0071, B:146:0x0075, B:151:0x0086, B:153:0x008f, B:155:0x0099, B:157:0x00a2, B:159:0x00ac, B:161:0x00b5, B:163:0x00bf, B:165:0x00c8, B:167:0x00d2, B:169:0x00db, B:171:0x00e5, B:173:0x00ee, B:175:0x00f9, B:177:0x0102, B:178:0x010a, B:180:0x0113, B:182:0x011e, B:147:0x007a, B:149:0x0080, B:184:0x016f), top: B:191:0x002a }] */
        /* JADX WARN: Removed duplicated region for block: B:180:0x0113 A[Catch: Exception -> 0x017a, TryCatch #0 {Exception -> 0x017a, blocks: (B:135:0x002a, B:137:0x0042, B:139:0x0048, B:141:0x005c, B:143:0x0067, B:145:0x0071, B:146:0x0075, B:151:0x0086, B:153:0x008f, B:155:0x0099, B:157:0x00a2, B:159:0x00ac, B:161:0x00b5, B:163:0x00bf, B:165:0x00c8, B:167:0x00d2, B:169:0x00db, B:171:0x00e5, B:173:0x00ee, B:175:0x00f9, B:177:0x0102, B:178:0x010a, B:180:0x0113, B:182:0x011e, B:147:0x007a, B:149:0x0080, B:184:0x016f), top: B:191:0x002a }] */
        /* JADX WARN: Removed duplicated region for block: B:181:0x011c  */
        @Override // okhttp3.Callback
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public void onResponse(okhttp3.Call r17, final okhttp3.Response r18) throws java.io.IOException {
            /*
                Method dump skipped, instructions count: 403
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: com.iptv.online.player.live.tv.smart.data.network.XtreamCodesApi.AnonymousClass6.onResponse(okhttp3.Call, okhttp3.Response):void");
        }
    }

    public static void fetchSeriesCategories(PlaylistSource playlistSource, final ApiCallback<List<VodCategory>> apiCallback) {
        if (playlistSource == null || playlistSource.getType() != PlaylistSource.SourceType.XTREAM_CODES) {
            postToMainThread(new Runnable() { // from class: com.iptv.online.player.live.tv.smart.data.network.XtreamCodesApi$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    apiCallback.onSuccess(XtreamCodesApi.getDemoSeriesCategories());
                }
            });
            return;
        }
        ApiClient.getClient().newCall(new Request.Builder().url(formatServerUrl(playlistSource.getUrl()) + "/player_api.php?username=" + playlistSource.getUsername() + "&password=" + playlistSource.getPassword() + "&action=get_series_categories").header("User-Agent", ApiClient.DEFAULT_USER_AGENT).build()).enqueue(new AnonymousClass7(apiCallback));
    }

    /* renamed from: com.iptv.online.player.live.tv.smart.data.network.XtreamCodesApi$7 */
    /* loaded from: classes5.dex */
    public static class AnonymousClass7 implements Callback {
        final /* synthetic */ ApiCallback val$callback;

        AnonymousClass7(ApiCallback apiCallback) {
            this.val$callback = apiCallback;
        }

        @Override // okhttp3.Callback
        public void onFailure(Call call, IOException iOException) {
            final ApiCallback apiCallback = this.val$callback;
            XtreamCodesApi.postToMainThread(new Runnable() { // from class: com.iptv.online.player.live.tv.smart.data.network.XtreamCodesApi$7$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    apiCallback.onSuccess(XtreamCodesApi.getDemoSeriesCategories());
                }
            });
        }

        @Override // okhttp3.Callback
        public void onResponse(Call call, Response response) throws IOException {
            final ArrayList arrayList = new ArrayList();
            arrayList.add(new VodCategory("all", "All Series"));
            if (response.isSuccessful() && response.body() != null) {
                try {
                    JsonArray jsonArray = (JsonArray) XtreamCodesApi.gson.fromJson(response.body().string(), JsonArray.class);
                    if (jsonArray != null) {
                        Iterator<JsonElement> it = jsonArray.iterator();
                        while (it.hasNext()) {
                            JsonElement next = it.next();
                            if (next.isJsonObject()) {
                                JsonObject asJsonObject = next.getAsJsonObject();
                                arrayList.add(new VodCategory(asJsonObject.has("category_id") ? asJsonObject.get("category_id").getAsString() : "", asJsonObject.has("category_name") ? asJsonObject.get("category_name").getAsString() : "General"));
                            }
                        }
                    }
                } catch (Exception unused) {
                }
            }
            final ApiCallback apiCallback = this.val$callback;
            XtreamCodesApi.postToMainThread(new Runnable() { // from class: com.iptv.online.player.live.tv.smart.data.network.XtreamCodesApi$7$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    apiCallback.onSuccess(arrayList);
                }
            });
        }
    }

    public static void fetchSeries(PlaylistSource playlistSource, final String str, final ApiCallback<List<VodItem>> apiCallback) {
        if (playlistSource == null || playlistSource.getType() != PlaylistSource.SourceType.XTREAM_CODES) {
            postToMainThread(new Runnable() { // from class: com.iptv.online.player.live.tv.smart.data.network.XtreamCodesApi$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    apiCallback.onSuccess(XtreamCodesApi.getDemoSeries(str));
                }
            });
            return;
        }
        String str2 = formatServerUrl(playlistSource.getUrl()) + "/player_api.php?username=" + playlistSource.getUsername() + "&password=" + playlistSource.getPassword() + "&action=get_series";
        if (str != null && !str.isEmpty() && !"all".equalsIgnoreCase(str)) {
            str2 = str2 + "&category_id=" + str;
        }
        ApiClient.getClient().newCall(new Request.Builder().url(str2).header("User-Agent", ApiClient.DEFAULT_USER_AGENT).build()).enqueue(new AnonymousClass8(apiCallback, str));
    }

    /* renamed from: com.iptv.online.player.live.tv.smart.data.network.XtreamCodesApi$8 */
    /* loaded from: classes5.dex */
    public static class AnonymousClass8 implements Callback {
        final /* synthetic */ ApiCallback val$callback;
        final /* synthetic */ String val$categoryId;

        AnonymousClass8(ApiCallback apiCallback, String str) {
            this.val$callback = apiCallback;
            this.val$categoryId = str;
        }

        @Override // okhttp3.Callback
        public void onFailure(Call call, IOException iOException) {
            final ApiCallback apiCallback = this.val$callback;
            final String str = this.val$categoryId;
            XtreamCodesApi.postToMainThread(new Runnable() { // from class: com.iptv.online.player.live.tv.smart.data.network.XtreamCodesApi$8$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    apiCallback.onSuccess(XtreamCodesApi.getDemoSeries(str));
                }
            });
        }

        @Override // okhttp3.Callback
        public void onResponse(Call call, Response response) throws IOException {
            String str;
            String str2;
            String str3 = "name";
            String str4 = "series_id";
            String str5 = "releaseDate";
            String str6 = "genre";
            String str7 = "category_id";
            String str8 = "plot";
            if (!response.isSuccessful() || response.body() == null) {
                final ApiCallback apiCallback = this.val$callback;
                final String str9 = this.val$categoryId;
                XtreamCodesApi.postToMainThread(new Runnable() { // from class: com.iptv.online.player.live.tv.smart.data.network.XtreamCodesApi$8$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        apiCallback.onSuccess(XtreamCodesApi.getDemoSeries(str9));
                    }
                });
                return;
            }
            try {
                JsonArray jsonArray = (JsonArray) XtreamCodesApi.gson.fromJson(response.body().string(), JsonArray.class);
                final ArrayList arrayList = new ArrayList();
                if (jsonArray != null) {
                    Iterator<JsonElement> it = jsonArray.iterator();
                    while (it.hasNext()) {
                        JsonElement next = it.next();
                        if (next.isJsonObject()) {
                            JsonObject asJsonObject = next.getAsJsonObject();
                            int asInt = asJsonObject.has(str4) ? asJsonObject.get(str4).getAsInt() : 0;
                            String asString = asJsonObject.has(str3) ? asJsonObject.get(str3).getAsString() : "Series " + asInt;
                            String asString2 = (!asJsonObject.has("cover") || asJsonObject.get("cover").isJsonNull()) ? null : asJsonObject.get("cover").getAsString();
                            String asString3 = (!asJsonObject.has("rating") || asJsonObject.get("rating").isJsonNull()) ? "8.2" : asJsonObject.get("rating").getAsString();
                            if (!asJsonObject.has(str8) || asJsonObject.get(str8).isJsonNull()) {
                                str = str3;
                                str2 = null;
                            } else {
                                str = str3;
                                str2 = asJsonObject.get(str8).getAsString();
                            }
                            String str10 = str4;
                            String asString4 = (!asJsonObject.has(str7) || asJsonObject.get(str7).isJsonNull()) ? "" : asJsonObject.get(str7).getAsString();
                            String str11 = str6;
                            String asString5 = (!asJsonObject.has(str6) || asJsonObject.get(str6).isJsonNull()) ? "TV Series" : asJsonObject.get(str6).getAsString();
                            String asString6 = (!asJsonObject.has(str5) || asJsonObject.get(str5).isJsonNull()) ? null : asJsonObject.get(str5).getAsString();
                            String str12 = str5;
                            VodItem vodItem = new VodItem();
                            vodItem.setId("series_" + asInt);
                            vodItem.setSeriesId(asInt);
                            vodItem.setName(asString);
                            vodItem.setStreamIcon(asString2);
                            vodItem.setRating(asString3);
                            vodItem.setPlot(str2);
                            vodItem.setCategoryId(asString4);
                            vodItem.setGenre(asString5);
                            vodItem.setReleaseDate(asString6);
                            vodItem.setSeries(true);
                            arrayList.add(vodItem);
                            str5 = str12;
                            str3 = str;
                            str7 = str7;
                            str4 = str10;
                            str6 = str11;
                            str8 = str8;
                        }
                    }
                }
                final ApiCallback apiCallback2 = this.val$callback;
                final String str13 = this.val$categoryId;
                XtreamCodesApi.postToMainThread(new Runnable() { // from class: com.iptv.online.player.live.tv.smart.data.network.XtreamCodesApi$8$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        XtreamCodesApi.AnonymousClass8.lambda$onResponse$2(apiCallback2, arrayList, str13);
                    }
                });
            } catch (Exception unused) {
                final ApiCallback apiCallback3 = this.val$callback;
                final String str14 = this.val$categoryId;
                XtreamCodesApi.postToMainThread(new Runnable() { // from class: com.iptv.online.player.live.tv.smart.data.network.XtreamCodesApi$8$$ExternalSyntheticLambda3
                    @Override // java.lang.Runnable
                    public final void run() {
                        apiCallback3.onSuccess(XtreamCodesApi.getDemoSeries(str14));
                    }
                });
            }
        }

        public static /* synthetic */ void lambda$onResponse$2(ApiCallback apiCallback, List list, String str) {
            if (list.isEmpty()) {
                list = XtreamCodesApi.getDemoSeries(str);
            }
            apiCallback.onSuccess(list);
        }
    }

    public static void fetchSeriesInfo(PlaylistSource playlistSource, final int i, final ApiCallback<SeriesInfo> apiCallback) {
        if (playlistSource == null || playlistSource.getType() != PlaylistSource.SourceType.XTREAM_CODES) {
            postToMainThread(new Runnable() { // from class: com.iptv.online.player.live.tv.smart.data.network.XtreamCodesApi$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    apiCallback.onSuccess(XtreamCodesApi.getDemoSeriesInfo(i));
                }
            });
            return;
        }
        String formatServerUrl = formatServerUrl(playlistSource.getUrl());
        String username = playlistSource.getUsername();
        String password = playlistSource.getPassword();
        ApiClient.getClient().newCall(new Request.Builder().url(formatServerUrl + "/player_api.php?username=" + username + "&password=" + password + "&action=get_series_info&series_id=" + i).header("User-Agent", ApiClient.DEFAULT_USER_AGENT).build()).enqueue(new AnonymousClass9(apiCallback, i, formatServerUrl, username, password));
    }

    /* renamed from: com.iptv.online.player.live.tv.smart.data.network.XtreamCodesApi$9 */
    /* loaded from: classes5.dex */
    public static class AnonymousClass9 implements Callback {
        final /* synthetic */ String val$baseUrl;
        final /* synthetic */ ApiCallback val$callback;
        final /* synthetic */ String val$password;
        final /* synthetic */ int val$seriesId;
        final /* synthetic */ String val$username;

        AnonymousClass9(ApiCallback apiCallback, int i, String str, String str2, String str3) {
            this.val$callback = apiCallback;
            this.val$seriesId = i;
            this.val$baseUrl = str;
            this.val$username = str2;
            this.val$password = str3;
        }

        @Override // okhttp3.Callback
        public void onFailure(Call call, IOException iOException) {
            final ApiCallback apiCallback = this.val$callback;
            final int i = this.val$seriesId;
            XtreamCodesApi.postToMainThread(new Runnable() { // from class: com.iptv.online.player.live.tv.smart.data.network.XtreamCodesApi$9$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    apiCallback.onSuccess(XtreamCodesApi.getDemoSeriesInfo(i));
                }
            });
        }

        /* JADX WARN: Removed duplicated region for block: B:418:0x0380 A[Catch: Exception -> 0x0395, TryCatch #0 {Exception -> 0x0395, blocks: (B:386:0x025b, B:388:0x02a3, B:390:0x02ad, B:392:0x02b7, B:394:0x02c2, B:396:0x02cd, B:398:0x02d8, B:400:0x02e3, B:402:0x02f0, B:404:0x02f9, B:406:0x0304, B:408:0x030f, B:410:0x031c, B:412:0x0322, B:413:0x0337, B:414:0x036b, B:416:0x0376, B:418:0x0380, B:419:0x0386), top: B:428:0x025b }] */
        @Override // okhttp3.Callback
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public void onResponse(okhttp3.Call r33, okhttp3.Response r34) throws java.io.IOException {
            /*
                Method dump skipped, instructions count: 944
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: com.iptv.online.player.live.tv.smart.data.network.XtreamCodesApi.AnonymousClass9.onResponse(okhttp3.Call, okhttp3.Response):void");
        }
    }

    public static List<VodCategory> getDemoVodCategories() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new VodCategory("all", "All Movies"));
        arrayList.add(new VodCategory("action", "Action & Sci-Fi"));
        arrayList.add(new VodCategory("classics", "Classic Cinema"));
        arrayList.add(new VodCategory("animation", "Animation & Family"));
        arrayList.add(new VodCategory("horror", "Thriller & Horror"));
        arrayList.add(new VodCategory("documentary", "Documentary & Nature"));
        return arrayList;
    }

    public static List<VodItem> getDemoMovies(String str) {
        ArrayList<VodItem> arrayList = new ArrayList();
        VodItem vodItem = new VodItem("vod_m1", "Big Buck Bunny", "https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8", "https://upload.wikimedia.org/wikipedia/commons/thumb/c/c5/Big_buck_bunny_poster_big.jpg/640px-Big_buck_bunny_poster_big.jpg", "Animation / Comedy", false);
        vodItem.setPlot("A large and lovable rabbit deals with bullying forest creatures in this iconic open-source animation masterpiece.");
        vodItem.setRating("8.1");
        vodItem.setReleaseDate("2008");
        vodItem.setDuration("10 min");
        vodItem.setCategoryId("animation");
        vodItem.setDirector("Sacha Goedegebure");
        vodItem.setCast("Jan Morgenstern, Ton Roosendaal");
        vodItem.setBackdropUrl("https://upload.wikimedia.org/wikipedia/commons/thumb/c/c5/Big_buck_bunny_poster_big.jpg/1280px-Big_buck_bunny_poster_big.jpg");
        VodItem vodItem2 = new VodItem("vod_m2", "Sintel", "https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8", "https://upload.wikimedia.org/wikipedia/commons/thumb/8/8f/Sintel_poster.jpg/640px-Sintel_poster.jpg", "Fantasy / Adventure", false);
        vodItem2.setPlot("A lonely young woman searches the dangerous wilderness for a baby dragon she nursed back to health, leading to an emotional confrontation.");
        vodItem2.setRating("8.4");
        vodItem2.setReleaseDate("2010");
        vodItem2.setDuration("15 min");
        vodItem2.setCategoryId("animation");
        vodItem2.setDirector("Colin Levy");
        vodItem2.setCast("Halina Reijn, Thom Hoffman");
        vodItem2.setBackdropUrl("https://upload.wikimedia.org/wikipedia/commons/thumb/8/8f/Sintel_poster.jpg/1280px-Sintel_poster.jpg");
        VodItem vodItem3 = new VodItem("vod_m3", "Tears of Steel", "https://demo.unified-streaming.com/k8s/features/stable/video/tears-of-steel/tears-of-steel.ism/.m3u8", "https://upload.wikimedia.org/wikipedia/commons/thumb/4/4b/Tears_of_Steel_poster.jpg/640px-Tears_of_Steel_poster.jpg", "Sci-Fi / Action", false);
        vodItem3.setPlot("In a dystopian future, scientists and warriors attempt to save the world from an army of giant destructive robots in Amsterdam.");
        vodItem3.setRating("7.8");
        vodItem3.setReleaseDate("2012");
        vodItem3.setDuration("12 min");
        vodItem3.setCategoryId("action");
        vodItem3.setDirector("Ian Hubert");
        vodItem3.setCast("Derek de Lint, Sergio Hasselbaink, Denise Rebergen");
        vodItem3.setBackdropUrl("https://upload.wikimedia.org/wikipedia/commons/thumb/4/4b/Tears_of_Steel_poster.jpg/1280px-Tears_of_Steel_poster.jpg");
        VodItem vodItem4 = new VodItem("vod_m4", "Elephant's Dream", "https://cph-p2p-msl.akamaized.net/hls/live/2000341/test/master.m3u8", "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e8/Elephants_Dream_poster.jpg/640px-Elephants_Dream_poster.jpg", "Sci-Fi / Drama", false);
        vodItem4.setPlot("Two characters journey through a surreal virtual machine, exploring the strange boundaries between mind and technology.");
        vodItem4.setRating("7.5");
        vodItem4.setReleaseDate("2006");
        vodItem4.setDuration("11 min");
        vodItem4.setCategoryId("action");
        vodItem4.setDirector("Bassam Kurdali");
        vodItem4.setCast("Tygo Gernandt, Cas Jansen");
        VodItem vodItem5 = new VodItem("vod_m5", "Charade", "https://ia800504.us.archive.org/11/items/Charade1963_201303/Charade1963_512kb.mp4", "https://upload.wikimedia.org/wikipedia/commons/thumb/2/29/Charade_%281963%29_poster.jpg/640px-Charade_%281963%29_poster.jpg", "Mystery / Romance", false);
        vodItem5.setPlot("A woman is pursued by several men in Paris who want the fortune her murdered husband had stolen. Whom can she trust?");
        vodItem5.setRating("8.0");
        vodItem5.setReleaseDate("1963");
        vodItem5.setDuration("113 min");
        vodItem5.setCategoryId("classics");
        vodItem5.setDirector("Stanley Donen");
        vodItem5.setCast("Cary Grant, Audrey Hepburn, Walter Matthau");
        VodItem vodItem6 = new VodItem("vod_m6", "Night of the Living Dead", "https://ia800300.us.archive.org/34/items/night_of_the_living_dead_1968/night_of_the_living_dead_1968_512kb.mp4", "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d3/Night_of_the_Living_Dead_%281968%29_theatrical_poster.jpg/640px-Night_of_the_Living_Dead_%281968%29_theatrical_poster.jpg", "Horror / Thriller", false);
        vodItem6.setPlot("A ragtag group of Pennsylvanians barricade themselves in an old farmhouse to remain safe from a horde of flesh-eating ghouls.");
        vodItem6.setRating("7.9");
        vodItem6.setReleaseDate("1968");
        vodItem6.setDuration("96 min");
        vodItem6.setCategoryId("horror");
        vodItem6.setDirector("George A. Romero");
        vodItem6.setCast("Duane Jones, Judith O'Dea, Karl Hardman");
        VodItem vodItem7 = new VodItem("vod_m7", "His Girl Friday", "https://ia800201.us.archive.org/22/items/his_girl_friday/his_girl_friday_512kb.mp4", "https://upload.wikimedia.org/wikipedia/commons/thumb/4/45/His_Girl_Friday_%281940_poster%29.jpg/640px-His_Girl_Friday_%281940_poster%29.jpg", "Comedy / Drama", false);
        vodItem7.setPlot("A newspaper editor uses every trick in the book to keep his top reporter ex-wife from remarrying and quitting the paper.");
        vodItem7.setRating("7.9");
        vodItem7.setReleaseDate("1940");
        vodItem7.setDuration("92 min");
        vodItem7.setCategoryId("classics");
        vodItem7.setDirector("Howard Hawks");
        vodItem7.setCast("Cary Grant, Rosalind Russell, Ralph Bellamy");
        VodItem vodItem8 = new VodItem("vod_m8", "The General", "https://ia800301.us.archive.org/27/items/TheGeneral_BusterKeaton/TheGeneral_512kb.mp4", "https://upload.wikimedia.org/wikipedia/commons/thumb/8/87/The_General_%281926%29_poster.jpg/640px-The_General_%281926%29_poster.jpg", "Action / Adventure", false);
        vodItem8.setPlot("When Union spies steal an engineer's beloved locomotive, he pursues it single-handedly through enemy lines.");
        vodItem8.setRating("8.2");
        vodItem8.setReleaseDate("1926");
        vodItem8.setDuration("79 min");
        vodItem8.setCategoryId("action");
        vodItem8.setDirector("Buster Keaton, Clyde Bruckman");
        vodItem8.setCast("Buster Keaton, Marion Mack, Glen Cavender");
        VodItem vodItem9 = new VodItem("vod_m9", "House on Haunted Hill", "https://ia800301.us.archive.org/15/items/house_on_haunted_hill/house_on_haunted_hill_512kb.mp4", "https://upload.wikimedia.org/wikipedia/commons/thumb/8/85/House_on_Haunted_Hill_poster.jpg/640px-House_on_Haunted_Hill_poster.jpg", "Horror / Mystery", false);
        vodItem9.setPlot("An eccentric millionaire offers ten thousand dollars to five people if they can stay in a haunted house all night.");
        vodItem9.setRating("7.6");
        vodItem9.setReleaseDate("1959");
        vodItem9.setDuration("75 min");
        vodItem9.setCategoryId("horror");
        vodItem9.setDirector("William Castle");
        vodItem9.setCast("Vincent Price, Carol Ohmart, Richard Long");
        VodItem vodItem10 = new VodItem("vod_m10", "The Last Man on Earth", "https://ia800302.us.archive.org/1/items/TheLastManOnEarth_762/TheLastManOnEarth.mp4", "https://upload.wikimedia.org/wikipedia/commons/thumb/5/53/The_Last_Man_on_Earth_%281964%29_poster.jpg/640px-The_Last_Man_on_Earth_%281964%29_poster.jpg", "Sci-Fi / Horror", false);
        vodItem10.setPlot("When a disease turns all of humanity into vampires, the last remaining unaffected man must survive the nightly onslaught.");
        vodItem10.setRating("7.7");
        vodItem10.setReleaseDate("1964");
        vodItem10.setDuration("86 min");
        vodItem10.setCategoryId("horror");
        vodItem10.setDirector("Ubaldo Ragona, Sidney Salkow");
        vodItem10.setCast("Vincent Price, Franca Bettoia, Emma Danieli");
        VodItem vodItem11 = new VodItem("vod_m11", "Gulliver's Travels", "https://ia800304.us.archive.org/21/items/gullivers_travels1939/gullivers_travels1939_512kb.mp4", "https://upload.wikimedia.org/wikipedia/commons/thumb/2/23/Gulliver%27s_Travels_1939_poster.jpg/640px-Gulliver%27s_Travels_1939_poster.jpg", "Animation / Family", false);
        vodItem11.setPlot("Lemuel Gulliver washes ashore on the island of Lilliput, a land of miniature people locked in a humorous war.");
        vodItem11.setRating("7.8");
        vodItem11.setReleaseDate("1939");
        vodItem11.setDuration("76 min");
        vodItem11.setCategoryId("animation");
        vodItem11.setDirector("Dave Fleischer");
        vodItem11.setCast("Jessica Dragonette, Lanny Ross, Pinto Colvig");
        VodItem vodItem12 = new VodItem("vod_m12", "Sherlock Holmes: The Sign of Four", "https://ia800303.us.archive.org/23/items/Sherlock_Holmes_Sign_of_Four/Sherlock_Holmes_Sign_of_Four_512kb.mp4", "https://upload.wikimedia.org/wikipedia/commons/thumb/9/91/The_Sign_of_Four_%281932_poster%29.jpg/640px-The_Sign_of_Four_%281932_poster%29.jpg", "Mystery / Crime", false);
        vodItem12.setPlot("Sherlock Holmes investigates the enigmatic disappearance of a treasure and a locked-room murder in Victorian London.");
        vodItem12.setRating("7.9");
        vodItem12.setReleaseDate("1932");
        vodItem12.setDuration("71 min");
        vodItem12.setCategoryId("classics");
        vodItem12.setDirector("Graham Cutts");
        vodItem12.setCast("Arthur Wontner, Ian Hunter, Isla Bevan");
        VodItem vodItem13 = new VodItem("vod_m13", "Extreme Nature Expeditions", "https://content.jwplatform.com/manifests/yp34SRfc.m3u8", "https://upload.wikimedia.org/wikipedia/commons/thumb/c/c7/Everest_North_Face_toward_Base_Camp-edit1.jpg/640px-Everest_North_Face_toward_Base_Camp-edit1.jpg", "Documentary / Nature", false);
        vodItem13.setPlot("A jaw-dropping showcase of extreme alpine expeditions, free-diving across abyssal coral reefs, and wild planetary wonders.");
        vodItem13.setRating("8.6");
        vodItem13.setReleaseDate("2023");
        vodItem13.setDuration("45 min");
        vodItem13.setCategoryId("documentary");
        vodItem13.setDirector("National Geographic Team");
        vodItem13.setCast("Explorers & Wildlife Biologists");
        VodItem vodItem14 = new VodItem("vod_m14", "Nosferatu: A Symphony of Horror", "https://ia800303.us.archive.org/14/items/Nosferatu_1922_512kb/Nosferatu_1922_512kb.mp4", "https://upload.wikimedia.org/wikipedia/commons/thumb/a/a4/Nosferatu_1922_theatrical_poster.jpg/640px-Nosferatu_1922_theatrical_poster.jpg", "Horror / Classics", false);
        vodItem14.setPlot("Vampire Count Orlok expresses interest in a new residence and real estate agent Hutter's wife in this timeless expressionist classic.");
        vodItem14.setRating("8.1");
        vodItem14.setReleaseDate("1922");
        vodItem14.setDuration("94 min");
        vodItem14.setCategoryId("horror");
        vodItem14.setDirector("F.W. Murnau");
        vodItem14.setCast("Max Schreck, Gustav von Wangenheim, Greta Schröder");
        arrayList.add(vodItem);
        arrayList.add(vodItem2);
        arrayList.add(vodItem3);
        arrayList.add(vodItem4);
        arrayList.add(vodItem5);
        arrayList.add(vodItem6);
        arrayList.add(vodItem7);
        arrayList.add(vodItem8);
        arrayList.add(vodItem9);
        arrayList.add(vodItem10);
        arrayList.add(vodItem11);
        arrayList.add(vodItem12);
        arrayList.add(vodItem13);
        arrayList.add(vodItem14);
        if (str != null && !str.isEmpty() && !"all".equalsIgnoreCase(str)) {
            ArrayList arrayList2 = new ArrayList();
            for (VodItem vodItem15 : arrayList) {
                if (str.equalsIgnoreCase(vodItem15.getCategoryId())) {
                    arrayList2.add(vodItem15);
                }
            }
            if (!arrayList2.isEmpty()) {
                return arrayList2;
            }
        }
        return arrayList;
    }

    public static List<VodCategory> getDemoSeriesCategories() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new VodCategory("all", "All Series"));
        arrayList.add(new VodCategory("action", "Action & Adventure"));
        arrayList.add(new VodCategory("scifi", "Sci-Fi & Space"));
        arrayList.add(new VodCategory("mystery", "Mystery & Crime"));
        arrayList.add(new VodCategory("nature", "Nature & Docs"));
        return arrayList;
    }

    public static List<VodItem> getDemoSeries(String str) {
        ArrayList<VodItem> arrayList = new ArrayList();
        VodItem vodItem = new VodItem("series_201", "The Adventures of Robin Hood", "", "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/The_Adventures_of_Robin_Hood_%281955%29.jpg/640px-The_Adventures_of_Robin_Hood_%281955%29.jpg", "Action / Adventure", true);
        vodItem.setSeriesId(201);
        vodItem.setPlot("Robin of Locksley and his Merry Men wage a heroic guerrilla war against the oppressive Sheriff of Nottingham in Sherwood Forest.");
        vodItem.setRating("8.6");
        vodItem.setReleaseDate("Classic Series");
        vodItem.setCategoryId("action");
        VodItem vodItem2 = new VodItem("series_202", "Flash Gordon: Space Voyages", "", "https://upload.wikimedia.org/wikipedia/commons/thumb/8/87/Flash_Gordon_Serial_Poster.jpg/640px-Flash_Gordon_Serial_Poster.jpg", "Sci-Fi / Space", true);
        vodItem2.setSeriesId(202);
        vodItem2.setPlot("Flash Gordon and Dale Arden journey through planetary perils, robotic armies, and stellar conflicts to save Earth.");
        vodItem2.setRating("8.7");
        vodItem2.setReleaseDate("Classic Serial");
        vodItem2.setCategoryId("scifi");
        VodItem vodItem3 = new VodItem("series_203", "Sherlock Holmes Mysteries", "", "https://upload.wikimedia.org/wikipedia/commons/thumb/9/91/The_Sign_of_Four_%281932_poster%29.jpg/640px-The_Sign_of_Four_%281932_poster%29.jpg", "Mystery / Crime", true);
        vodItem3.setSeriesId(203);
        vodItem3.setPlot("The world's greatest consulting detective and Dr. John Watson unravel cunning crimes and international conspiracies across Victorian England.");
        vodItem3.setRating("8.9");
        vodItem3.setReleaseDate("Classic Crime");
        vodItem3.setCategoryId("mystery");
        VodItem vodItem4 = new VodItem("series_204", "Cosmos & Open Universe", "", "https://upload.wikimedia.org/wikipedia/commons/thumb/4/4b/Tears_of_Steel_poster.jpg/640px-Tears_of_Steel_poster.jpg", "Sci-Fi / Space", true);
        vodItem4.setSeriesId(204);
        vodItem4.setPlot("A serialized deep dive into cosmic singularities, hyperspace propulsion, and alien civilizations in high-definition HLS.");
        vodItem4.setRating("9.2");
        vodItem4.setReleaseDate("2024");
        vodItem4.setCategoryId("scifi");
        VodItem vodItem5 = new VodItem("series_205", "Wild Earth & Extreme Expeditions", "", "https://upload.wikimedia.org/wikipedia/commons/thumb/c/c7/Everest_North_Face_toward_Base_Camp-edit1.jpg/640px-Everest_North_Face_toward_Base_Camp-edit1.jpg", "Nature / Documentary", true);
        vodItem5.setSeriesId(205);
        vodItem5.setPlot("Experience uncharted jungles, volcanic summits, and deep-ocean biospheres captured across global natural habitats.");
        vodItem5.setRating("9.0");
        vodItem5.setReleaseDate("2024");
        vodItem5.setCategoryId("nature");
        arrayList.add(vodItem);
        arrayList.add(vodItem2);
        arrayList.add(vodItem3);
        arrayList.add(vodItem4);
        arrayList.add(vodItem5);
        if (str != null && !str.isEmpty() && !"all".equalsIgnoreCase(str)) {
            ArrayList arrayList2 = new ArrayList();
            for (VodItem vodItem6 : arrayList) {
                if (str.equalsIgnoreCase(vodItem6.getCategoryId())) {
                    arrayList2.add(vodItem6);
                }
            }
            if (!arrayList2.isEmpty()) {
                return arrayList2;
            }
        }
        return arrayList;
    }

    public static SeriesInfo getDemoSeriesInfo(int i) {
        SeriesInfo seriesInfo = new SeriesInfo();
        VodItem vodItem = new VodItem();
        vodItem.setSeriesId(i);
        ArrayList arrayList = new ArrayList();
        arrayList.add("Season 1");
        arrayList.add("Season 2");
        HashMap hashMap = new HashMap();
        if (i == 201) {
            vodItem.setName("The Adventures of Robin Hood");
            vodItem.setStreamIcon("https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/The_Adventures_of_Robin_Hood_%281955%29.jpg/640px-The_Adventures_of_Robin_Hood_%281955%29.jpg");
            vodItem.setRating("8.6");
            vodItem.setGenre("Action / Adventure");
            vodItem.setPlot("Robin Hood and his band of Merry Men fight injustice in 12th-century England, defying the villainous Sheriff of Nottingham.");
            vodItem.setCast("Richard Greene, Bernadette O'Farrell, Alan Wheatley");
            vodItem.setDirector("Ralph Smart");
            ArrayList arrayList2 = new ArrayList();
            EpisodeItem episodeItem = new EpisodeItem("ep_201_1", 1, 1, "The Coming of Robin Hood", "https://ia800304.us.archive.org/31/items/TheAdventuresOfRobinHoodEpisode1/RobinHood1_512kb.mp4");
            episodeItem.setPlot("Robin of Locksley returns from the Crusades to find his estate seized and takes refuge in Sherwood Forest.");
            episodeItem.setDuration("26 min");
            episodeItem.setRating("8.7");
            episodeItem.setCoverUrl("https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/The_Adventures_of_Robin_Hood_%281955%29.jpg/640px-The_Adventures_of_Robin_Hood_%281955%29.jpg");
            EpisodeItem episodeItem2 = new EpisodeItem("ep_201_2", 1, 2, "The Moneylender", "https://ia800304.us.archive.org/31/items/TheAdventuresOfRobinHoodEpisode1/RobinHood1_512kb.mp4");
            episodeItem2.setPlot("Robin foils an extortion plot by disguised agents of the Sheriff threatening Nottingham peasants.");
            episodeItem2.setDuration("25 min");
            episodeItem2.setRating("8.5");
            episodeItem2.setCoverUrl("https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/The_Adventures_of_Robin_Hood_%281955%29.jpg/640px-The_Adventures_of_Robin_Hood_%281955%29.jpg");
            arrayList2.add(episodeItem);
            arrayList2.add(episodeItem2);
            hashMap.put("Season 1", arrayList2);
            ArrayList arrayList3 = new ArrayList();
            EpisodeItem episodeItem3 = new EpisodeItem("ep_201_3", 2, 1, "The Secret Room", "https://ia800304.us.archive.org/31/items/TheAdventuresOfRobinHoodEpisode1/RobinHood1_512kb.mp4");
            episodeItem3.setPlot("A fugitive seeking Robin's help leads the Merry Men directly into a trap inside Nottingham Castle.");
            episodeItem3.setDuration("26 min");
            episodeItem3.setRating("8.8");
            episodeItem3.setCoverUrl("https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/The_Adventures_of_Robin_Hood_%281955%29.jpg/640px-The_Adventures_of_Robin_Hood_%281955%29.jpg");
            arrayList3.add(episodeItem3);
            hashMap.put("Season 2", arrayList3);
        } else if (i == 202) {
            vodItem.setName("Flash Gordon: Space Voyages");
            vodItem.setStreamIcon("https://upload.wikimedia.org/wikipedia/commons/thumb/8/87/Flash_Gordon_Serial_Poster.jpg/640px-Flash_Gordon_Serial_Poster.jpg");
            vodItem.setRating("8.7");
            vodItem.setGenre("Sci-Fi / Space");
            vodItem.setPlot("Flash Gordon and Dale Arden crash land on the hostile world of Mongo, fighting warlords to prevent Earth's annihilation.");
            vodItem.setCast("Buster Crabbe, Jean Rogers, Charles Middleton");
            vodItem.setDirector("Frederick Stephani");
            ArrayList arrayList4 = new ArrayList();
            EpisodeItem episodeItem4 = new EpisodeItem("ep_202_1", 1, 1, "Chapter 1: Planet of Peril", "https://ia800305.us.archive.org/11/items/FlashGordonChapter1/FlashGordon1_512kb.mp4");
            episodeItem4.setPlot("Flash Gordon, Dale Arden, and Dr. Zarkov blast off in a rocket to stop a collision between planet Mongo and Earth.");
            episodeItem4.setDuration("20 min");
            episodeItem4.setRating("8.9");
            episodeItem4.setCoverUrl("https://upload.wikimedia.org/wikipedia/commons/thumb/8/87/Flash_Gordon_Serial_Poster.jpg/640px-Flash_Gordon_Serial_Poster.jpg");
            EpisodeItem episodeItem5 = new EpisodeItem("ep_202_2", 1, 2, "Chapter 2: The Tunnel of Terror", "https://ia800305.us.archive.org/11/items/FlashGordonChapter1/FlashGordon1_512kb.mp4");
            episodeItem5.setPlot("Flash must escape subterranean labyrinths before the emperor's robotic enforcers discover their location.");
            episodeItem5.setDuration("19 min");
            episodeItem5.setRating("8.6");
            episodeItem5.setCoverUrl("https://upload.wikimedia.org/wikipedia/commons/thumb/8/87/Flash_Gordon_Serial_Poster.jpg/640px-Flash_Gordon_Serial_Poster.jpg");
            arrayList4.add(episodeItem4);
            arrayList4.add(episodeItem5);
            hashMap.put("Season 1", arrayList4);
            ArrayList arrayList5 = new ArrayList();
            EpisodeItem episodeItem6 = new EpisodeItem("ep_202_3", 2, 1, "Chapter 3: The Sky City", "https://ia800305.us.archive.org/11/items/FlashGordonChapter1/FlashGordon1_512kb.mp4");
            episodeItem6.setPlot("Flash reaches the suspended cloud kingdom of Hawkmen in search of powerful allies.");
            episodeItem6.setDuration("20 min");
            episodeItem6.setRating("9.0");
            episodeItem6.setCoverUrl("https://upload.wikimedia.org/wikipedia/commons/thumb/8/87/Flash_Gordon_Serial_Poster.jpg/640px-Flash_Gordon_Serial_Poster.jpg");
            arrayList5.add(episodeItem6);
            hashMap.put("Season 2", arrayList5);
        } else {
            vodItem.setName("Cosmos & Open Universe");
            vodItem.setStreamIcon("https://upload.wikimedia.org/wikipedia/commons/thumb/4/4b/Tears_of_Steel_poster.jpg/640px-Tears_of_Steel_poster.jpg");
            vodItem.setRating("9.2");
            vodItem.setGenre("Sci-Fi / Space");
            vodItem.setPlot("An acclaimed multi-part exploration of interstellar mysteries, quantum warps, and synthetic frontiers in 4K HLS.");
            vodItem.setCast("Elena Vance, Marcus Thorne, Dr. Sarah Lin");
            vodItem.setDirector("Open Source Filmmakers");
            ArrayList arrayList6 = new ArrayList();
            EpisodeItem episodeItem7 = new EpisodeItem("ep_204_1", 1, 1, "Episode 1: The Frontier Singularity", "https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8");
            episodeItem7.setPlot("An anomalous signal discovered at the outer boundary of the solar system unlocks forgotten cosmic secrets.");
            episodeItem7.setDuration("10 min");
            episodeItem7.setRating("9.1");
            episodeItem7.setCoverUrl("https://upload.wikimedia.org/wikipedia/commons/thumb/c/c5/Big_buck_bunny_poster_big.jpg/640px-Big_buck_bunny_poster_big.jpg");
            EpisodeItem episodeItem8 = new EpisodeItem("ep_204_2", 1, 2, "Episode 2: The Dragon's Ascent", "https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8");
            episodeItem8.setPlot("Through snow-capped mountain passes and ancient ruins, the crew uncovers the origin of hyper-drives.");
            episodeItem8.setDuration("15 min");
            episodeItem8.setRating("9.3");
            episodeItem8.setCoverUrl("https://upload.wikimedia.org/wikipedia/commons/thumb/8/8f/Sintel_poster.jpg/640px-Sintel_poster.jpg");
            arrayList6.add(episodeItem7);
            arrayList6.add(episodeItem8);
            hashMap.put("Season 1", arrayList6);
            ArrayList arrayList7 = new ArrayList();
            EpisodeItem episodeItem9 = new EpisodeItem("ep_204_3", 2, 1, "Episode 3: Iron Horizon", "https://demo.unified-streaming.com/k8s/features/stable/video/tears-of-steel/tears-of-steel.ism/.m3u8");
            episodeItem9.setPlot("The mechanical vanguard emerges from deep temporal stasis to challenge human dominion.");
            episodeItem9.setDuration("12 min");
            episodeItem9.setRating("9.0");
            episodeItem9.setCoverUrl("https://upload.wikimedia.org/wikipedia/commons/thumb/4/4b/Tears_of_Steel_poster.jpg/640px-Tears_of_Steel_poster.jpg");
            arrayList7.add(episodeItem9);
            hashMap.put("Season 2", arrayList7);
        }
        seriesInfo.setSeriesItem(vodItem);
        seriesInfo.setSeasons(arrayList);
        seriesInfo.setEpisodes(hashMap);
        return seriesInfo;
    }
}
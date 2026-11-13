package com.iptv.online.player.live.tv.smart.data.local;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatDelegate;
//import com.adsUtils.custom.LocaleHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iptv.online.player.live.tv.smart.AdsPackage.custom.LocaleHelper;
import com.iptv.online.player.live.tv.smart.data.model.PlaylistItem;
import com.iptv.online.player.live.tv.smart.data.model.PlaylistSource;
import com.iptv.online.player.live.tv.smart.data.model.WatchProgress;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@SuppressWarnings("all")
/* loaded from: classes5.dex */
public class PlaylistStorageManager {
    private static final String KEY_ACTIVE_SOURCE_ID = "active_source_id";
    private static final String KEY_APP_LANGUAGE = "app_language";
    private static final String KEY_CACHED_CHANNELS = "cached_channels_json";
    private static final String KEY_FAVORITES = "favorite_channel_ids";
    private static final String KEY_HAS_OPENED_BEFORE = "has_opened_before";
    private static final String KEY_LOCKED_CATEGORIES = "locked_categories";
    private static final String KEY_PARENTAL_PIN = "parental_pin";
    private static final String KEY_PLAYLIST_SOURCES = "playlist_sources";
    private static final String KEY_RECENTS = "recent_channels_json";
    private static final String KEY_THEME_MODE = "app_theme_mode";
    private static final String KEY_WATCH_PROGRESS = "vod_watch_progress_json";
    private static final String PREF_NAME = "iptv_player_prefs";
    public static final int THEME_DARK = 2;
    public static final int THEME_LIGHT = 1;
    public static final int THEME_SYSTEM = 3;
    private final Context context;
    private final Gson gson;
    private final SharedPreferences prefs;

    public PlaylistStorageManager(Context context) {
        Context applicationContext = context.getApplicationContext();
        this.context = applicationContext;
        this.prefs = applicationContext.getSharedPreferences(PREF_NAME, 0);
        this.gson = new Gson();
        initializeDefaultsIfNeeded();
    }

    public int getThemeMode() {
        return this.prefs.getInt(KEY_THEME_MODE, 1) != 2 ? 1 : 2;
    }

    public void setThemeMode(int i) {
        int i2 = i != 2 ? 1 : 2;
        this.prefs.edit().putInt(KEY_THEME_MODE, i2).apply();
        applyTheme(i2);
    }

    public static void applyTheme(int i) {
        if (i == 2) {
            AppCompatDelegate.setDefaultNightMode(2);
        } else {
            AppCompatDelegate.setDefaultNightMode(1);
        }
    }

    public String getAppLanguage() {
        String persistedLanguage = com.iptv.online.player.live.tv.smart.AdsPackage.custom.LocaleHelper.getPersistedLanguage(this.context, null);
        return (persistedLanguage == null || persistedLanguage.isEmpty()) ? this.prefs.getString(KEY_APP_LANGUAGE, com.iptv.online.player.live.tv.smart.AdsPackage.custom.LocaleHelper.DEFAULT_LANGUAGE) : persistedLanguage;
    }

    public void setAppLanguage(String str) {
        if (str == null || str.isEmpty()) {
            str = com.iptv.online.player.live.tv.smart.AdsPackage.custom.LocaleHelper.DEFAULT_LANGUAGE;
        }
        this.prefs.edit().putString(KEY_APP_LANGUAGE, str).apply();
        com.iptv.online.player.live.tv.smart.AdsPackage.custom.LocaleHelper.persistLanguage(this.context, str);
    }

    private void initializeDefaultsIfNeeded() {
        List<PlaylistSource> playlistSources = getPlaylistSources();
        if (playlistSources.isEmpty()) {
            PlaylistSource createDemoSource = PlaylistSource.createDemoSource();
            playlistSources.add(createDemoSource);
            savePlaylistSources(playlistSources);
            setActiveSourceId(createDemoSource.getId());
        }
    }

    public List<PlaylistSource> getPlaylistSources() {
        String string = this.prefs.getString(KEY_PLAYLIST_SOURCES, null);
        if (string == null) {
            return new ArrayList();
        }
        List<PlaylistSource> list = (List) this.gson.fromJson(string, new TypeToken<List<PlaylistSource>>() { // from class: com.iptv.online.player.live.tv.smart.data.local.PlaylistStorageManager.1
        }.getType());
        return list != null ? list : new ArrayList();
    }

    public void savePlaylistSources(List<PlaylistSource> list) {
        this.prefs.edit().putString(KEY_PLAYLIST_SOURCES, this.gson.toJson(list)).apply();
    }

    public void addOrUpdateSource(PlaylistSource playlistSource) {
        List<PlaylistSource> playlistSources = getPlaylistSources();
        int i = 0;
        while (true) {
            if (i < playlistSources.size()) {
                if (playlistSources.get(i).getId().equals(playlistSource.getId())) {
                    playlistSources.set(i, playlistSource);
                    break;
                }
                i++;
            } else {
                playlistSources.add(playlistSource);
                break;
            }
        }
        savePlaylistSources(playlistSources);
    }

    public void deleteSource(String str) {
        List<PlaylistSource> playlistSources = getPlaylistSources();
        int i = 0;
        while (true) {
            if (i >= playlistSources.size()) {
                break;
            } else if (playlistSources.get(i).getId().equals(str)) {
                playlistSources.remove(i);
                break;
            } else {
                i++;
            }
        }
        savePlaylistSources(playlistSources);
        deleteCachedChannels(str);
        if (!str.equals(getActiveSourceId()) || playlistSources.isEmpty()) {
            return;
        }
        setActiveSourceId(playlistSources.get(0).getId());
    }

    public String getActiveSourceId() {
        return this.prefs.getString(KEY_ACTIVE_SOURCE_ID, "demo_playlist_id");
    }

    public void setActiveSourceId(String str) {
        this.prefs.edit().putString(KEY_ACTIVE_SOURCE_ID, str).apply();
    }

    public PlaylistSource getActiveSource() {
        String activeSourceId = getActiveSourceId();
        List<PlaylistSource> playlistSources = getPlaylistSources();
        for (PlaylistSource playlistSource : playlistSources) {
            if (playlistSource.getId().equals(activeSourceId)) {
                return playlistSource;
            }
        }
        return playlistSources.isEmpty() ? PlaylistSource.createDemoSource() : playlistSources.get(0);
    }

    public void saveCachedChannels(final String str, final List<PlaylistItem> list) {
        if (str == null || list == null) {
            return;
        }
        new Thread(new Runnable() { // from class: com.iptv.online.player.live.tv.smart.data.local.PlaylistStorageManager$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                PlaylistStorageManager.this.lambda$saveCachedChannels$0(str, list);
            }
        }).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$saveCachedChannels$0(String str, List list) {
        try {
            FileWriter fileWriter = new FileWriter(new File(this.context.getFilesDir(), "channels_" + str.replaceAll("[^a-zA-Z0-9_-]", "_") + ".json"));
            this.gson.toJson(list, fileWriter);
            fileWriter.close();
        } catch (Exception unused) {
        }
    }

    public void saveCachedChannels(List<PlaylistItem> list) {
        saveCachedChannels(getActiveSourceId(), list);
    }

    public List<PlaylistItem> getCachedChannels(String str) {
        if (str == null) {
            return new ArrayList();
        }
        try {
            File file = new File(this.context.getFilesDir(), "channels_" + str.replaceAll("[^a-zA-Z0-9_-]", "_") + ".json");
            if (file.exists()) {
                FileReader fileReader = new FileReader(file);
                List<PlaylistItem> list = (List) this.gson.fromJson(fileReader, new TypeToken<List<PlaylistItem>>() { // from class: com.iptv.online.player.live.tv.smart.data.local.PlaylistStorageManager.2
                }.getType());
                if (list != null) {
                    fileReader.close();
                    return list;
                }
                fileReader.close();
            }
        } catch (Exception unused) {
        }
        String string = this.prefs.getString("cached_channels_" + str, null);
        if (string != null) {
            List<PlaylistItem> list2 = (List) this.gson.fromJson(string, new TypeToken<List<PlaylistItem>>() { // from class: com.iptv.online.player.live.tv.smart.data.local.PlaylistStorageManager.3
            }.getType());
            if (list2 != null) {
                return list2;
            }
        }
        return new ArrayList();
    }

    public List<PlaylistItem> getCachedChannels() {
        return getCachedChannels(getActiveSourceId());
    }

    public void deleteCachedChannels(String str) {
        if (str == null) {
            return;
        }
        try {
            File file = new File(this.context.getFilesDir(), "channels_" + str.replaceAll("[^a-zA-Z0-9_-]", "_") + ".json");
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception unused) {
        }
        this.prefs.edit().remove("cached_channels_" + str).apply();
    }

    public Set<String> getFavoriteIds() {
        return this.prefs.getStringSet(KEY_FAVORITES, new HashSet());
    }

    public void toggleFavorite(String str) {
        HashSet hashSet = new HashSet(getFavoriteIds());
        if (hashSet.contains(str)) {
            hashSet.remove(str);
        } else {
            hashSet.add(str);
        }
        this.prefs.edit().putStringSet(KEY_FAVORITES, hashSet).apply();
    }

    public boolean isFavorite(String str) {
        return getFavoriteIds().contains(str);
    }

    public List<PlaylistItem> getRecentChannels() {
        String string = this.prefs.getString(KEY_RECENTS, null);
        if (string == null) {
            return new ArrayList();
        }
        List<PlaylistItem> list = (List) this.gson.fromJson(string, new TypeToken<List<PlaylistItem>>() { // from class: com.iptv.online.player.live.tv.smart.data.local.PlaylistStorageManager.4
        }.getType());
        return list != null ? list : new ArrayList();
    }

    public void addRecentChannel(PlaylistItem playlistItem) {
        List<PlaylistItem> recentChannels = getRecentChannels();
        recentChannels.remove(playlistItem);
        recentChannels.add(0, playlistItem);
        if (recentChannels.size() > 20) {
            recentChannels = recentChannels.subList(0, 20);
        }
        this.prefs.edit().putString(KEY_RECENTS, this.gson.toJson(recentChannels)).apply();
    }

    public String getParentalPin() {
        return this.prefs.getString(KEY_PARENTAL_PIN, null);
    }

    public void setParentalPin(String str) {
        this.prefs.edit().putString(KEY_PARENTAL_PIN, str).apply();
    }

    public void removeParentalPin() {
        this.prefs.edit().remove(KEY_PARENTAL_PIN).apply();
    }

    public boolean isParentalControlEnabled() {
        return (getParentalPin() == null || getParentalPin().trim().isEmpty()) ? false : true;
    }

    public Set<String> getLockedCategories() {
        return this.prefs.getStringSet(KEY_LOCKED_CATEGORIES, new HashSet());
    }

    public void setCategoryLocked(String str, boolean z) {
        HashSet hashSet = new HashSet(getLockedCategories());
        if (z) {
            hashSet.add(str.toLowerCase());
        } else {
            hashSet.remove(str.toLowerCase());
        }
        this.prefs.edit().putStringSet(KEY_LOCKED_CATEGORIES, hashSet).apply();
    }

    public boolean isCategoryLocked(String str) {
        if (isParentalControlEnabled()) {
            String lowerCase = str.toLowerCase();
            if (lowerCase.contains("xxx") || lowerCase.contains("adult") || lowerCase.contains("for adults") || lowerCase.contains("+18") || lowerCase.contains("18+")) {
                return true;
            }
            return getLockedCategories().contains(lowerCase);
        }
        return false;
    }

    public List<WatchProgress> getContinueWatchingList() {
        String string = this.prefs.getString(KEY_WATCH_PROGRESS, null);
        if (string == null) {
            return new ArrayList();
        }
        try {
            List<WatchProgress> list = (List) this.gson.fromJson(string, new TypeToken<List<WatchProgress>>() { // from class: com.iptv.online.player.live.tv.smart.data.local.PlaylistStorageManager.5
            }.getType());
            return list != null ? list : new ArrayList();
        } catch (Exception unused) {
            return new ArrayList();
        }
    }

    public WatchProgress getWatchProgress(String str) {
        if (str == null) {
            return null;
        }
        for (WatchProgress watchProgress : getContinueWatchingList()) {
            if (str.equals(watchProgress.getContentId())) {
                return watchProgress;
            }
        }
        return null;
    }

    public void saveWatchProgress(WatchProgress watchProgress) {
        if (watchProgress == null || watchProgress.getContentId() == null) {
            return;
        }
        List<WatchProgress> continueWatchingList = getContinueWatchingList();
        int i = 0;
        while (true) {
            if (i >= continueWatchingList.size()) {
                break;
            } else if (continueWatchingList.get(i).getContentId().equals(watchProgress.getContentId())) {
                continueWatchingList.remove(i);
                break;
            } else {
                i++;
            }
        }
        if (watchProgress.getProgressPercentage() < 95 && watchProgress.getPositionMs() > 5000) {
            watchProgress.setLastWatchedTime(System.currentTimeMillis());
            continueWatchingList.add(0, watchProgress);
            if (continueWatchingList.size() > 30) {
                continueWatchingList = continueWatchingList.subList(0, 30);
            }
        }
        this.prefs.edit().putString(KEY_WATCH_PROGRESS, this.gson.toJson(continueWatchingList)).apply();
    }

    public void deleteWatchProgress(String str) {
        if (str == null) {
            return;
        }
        List<WatchProgress> continueWatchingList = getContinueWatchingList();
        for (int i = 0; i < continueWatchingList.size(); i++) {
            if (continueWatchingList.get(i).getContentId().equals(str)) {
                continueWatchingList.remove(i);
                this.prefs.edit().putString(KEY_WATCH_PROGRESS, this.gson.toJson(continueWatchingList)).apply();
                return;
            }
        }
    }

    public boolean hasOpenedBefore() {
        return this.prefs.getBoolean(KEY_HAS_OPENED_BEFORE, false);
    }

    public void setHasOpenedBefore(boolean z) {
        this.prefs.edit().putBoolean(KEY_HAS_OPENED_BEFORE, z).apply();
    }
}

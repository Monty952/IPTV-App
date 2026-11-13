package com.iptv.online.player.live.tv.smart.data.model;

import java.io.Serializable;
import java.util.UUID;

/* loaded from: classes5.dex */
public class PlaylistSource implements Serializable {
    private int channelCount;
    private String id = UUID.randomUUID().toString();
    private long lastUpdated = System.currentTimeMillis();
    private String localFilePath;
    private String name;
    private String password;
    private SourceType type;
    private String url;
    private String username;

    /* loaded from: classes5.dex */
    public enum SourceType {
        M3U_URL,
        XTREAM_CODES,
        LOCAL_FILE,
        SINGLE_STREAM,
        DEMO
    }

    public static PlaylistSource createM3USource(String str, String str2) {
        PlaylistSource playlistSource = new PlaylistSource();
        playlistSource.setName(str);
        playlistSource.setType(SourceType.M3U_URL);
        playlistSource.setUrl(str2);
        return playlistSource;
    }

    public static PlaylistSource createLocalFileSource(String str, String str2) {
        PlaylistSource playlistSource = new PlaylistSource();
        playlistSource.setName(str);
        playlistSource.setType(SourceType.LOCAL_FILE);
        playlistSource.setUrl(str2);
        playlistSource.setLocalFilePath(str2);
        return playlistSource;
    }

    public static PlaylistSource createSingleStreamSource(String str, String str2) {
        PlaylistSource playlistSource = new PlaylistSource();
        playlistSource.setName(str);
        playlistSource.setType(SourceType.SINGLE_STREAM);
        playlistSource.setUrl(str2);
        playlistSource.setChannelCount(1);
        return playlistSource;
    }

    public static PlaylistSource createXtreamSource(String str, String str2, String str3, String str4) {
        PlaylistSource playlistSource = new PlaylistSource();
        playlistSource.setName(str);
        playlistSource.setType(SourceType.XTREAM_CODES);
        playlistSource.setUrl(str2);
        playlistSource.setUsername(str3);
        playlistSource.setPassword(str4);
        return playlistSource;
    }

    public static PlaylistSource createDemoSource() {
        PlaylistSource playlistSource = new PlaylistSource();
        playlistSource.setId("demo_playlist_id");
        playlistSource.setName("Free Legal Live TV (Demo)");
        playlistSource.setType(SourceType.DEMO);
        playlistSource.setUrl("https://iptv-org.github.io/iptv/index.m3u");
        return playlistSource;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String str) {
        this.id = str;
    }

    public String getName() {
        String str = this.name;
        return (str == null || str.trim().isEmpty()) ? "Playlist" : this.name.trim();
    }

    public void setName(String str) {
        this.name = str;
    }

    public SourceType getType() {
        SourceType sourceType = this.type;
        return sourceType != null ? sourceType : SourceType.M3U_URL;
    }

    public void setType(SourceType sourceType) {
        this.type = sourceType;
    }

    public String getUrl() {
        String str = this.url;
        return str != null ? str : "";
    }

    public void setUrl(String str) {
        this.url = str;
    }

    public String getLocalFilePath() {
        return this.localFilePath;
    }

    public void setLocalFilePath(String str) {
        this.localFilePath = str;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String str) {
        this.username = str;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String str) {
        this.password = str;
    }

    public long getLastUpdated() {
        return this.lastUpdated;
    }

    public void setLastUpdated(long j) {
        this.lastUpdated = j;
    }

    public int getChannelCount() {
        return this.channelCount;
    }

    public void setChannelCount(int i) {
        this.channelCount = i;
    }

    public boolean isFileBased() {
        return this.type == SourceType.LOCAL_FILE;
    }

    public boolean isUrlBased() {
        return this.type == SourceType.M3U_URL || this.type == SourceType.XTREAM_CODES || this.type == SourceType.DEMO || this.type == SourceType.SINGLE_STREAM;
    }
}

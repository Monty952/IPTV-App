package com.iptv.online.player.live.tv.smart.data.model;

import java.io.Serializable;
import java.util.Locale;

/* loaded from: classes5.dex */
public class WatchProgress implements Serializable {
    private String contentId;
    private long durationMs;
    private int episodeNumber;
    private boolean isSeries;
    private long lastWatchedTime = System.currentTimeMillis();
    private long positionMs;
    private String posterUrl;
    private int seasonNumber;
    private String seriesName;
    private String streamUrl;
    private String title;

    public WatchProgress() {
    }

    public WatchProgress(String str, String str2, String str3, String str4, long j, long j2) {
        this.contentId = str;
        this.title = str2;
        this.posterUrl = str3;
        this.streamUrl = str4;
        this.positionMs = j;
        this.durationMs = j2;
    }

    public String getContentId() {
        return this.contentId;
    }

    public void setContentId(String str) {
        this.contentId = str;
    }

    public String getTitle() {
        String str = this.title;
        return str != null ? str : "Unknown Title";
    }

    public void setTitle(String str) {
        this.title = str;
    }

    public String getPosterUrl() {
        return this.posterUrl;
    }

    public void setPosterUrl(String str) {
        this.posterUrl = str;
    }

    public String getStreamUrl() {
        return this.streamUrl;
    }

    public void setStreamUrl(String str) {
        this.streamUrl = str;
    }

    public long getPositionMs() {
        return this.positionMs;
    }

    public void setPositionMs(long j) {
        this.positionMs = j;
    }

    public long getDurationMs() {
        return this.durationMs;
    }

    public void setDurationMs(long j) {
        this.durationMs = j;
    }

    public long getLastWatchedTime() {
        return this.lastWatchedTime;
    }

    public void setLastWatchedTime(long j) {
        this.lastWatchedTime = j;
    }

    public boolean isSeries() {
        return this.isSeries;
    }

    public void setSeries(boolean z) {
        this.isSeries = z;
    }

    public String getSeriesName() {
        return this.seriesName;
    }

    public void setSeriesName(String str) {
        this.seriesName = str;
    }

    public int getSeasonNumber() {
        return this.seasonNumber;
    }

    public void setSeasonNumber(int i) {
        this.seasonNumber = i;
    }

    public int getEpisodeNumber() {
        return this.episodeNumber;
    }

    public void setEpisodeNumber(int i) {
        this.episodeNumber = i;
    }

    public int getProgressPercentage() {
        long j = this.durationMs;
        if (j <= 0) {
            return 0;
        }
        return Math.min(100, Math.max(0, (int) ((this.positionMs * 100) / j)));
    }

    public String getFormattedPosition() {
        return formatTime(this.positionMs);
    }

    public String getFormattedDuration() {
        return formatTime(this.durationMs);
    }

    public static String formatTime(long j) {
        if (j <= 0) {
            return "00:00";
        }
        long j2 = j / 1000;
        long j3 = j2 % 60;
        long j4 = (j2 / 60) % 60;
        long j5 = j2 / 3600;
        if (j5 > 0) {
            return String.format(Locale.US, "%d:%02d:%02d", Long.valueOf(j5), Long.valueOf(j4), Long.valueOf(j3));
        }
        return String.format(Locale.US, "%02d:%02d", Long.valueOf(j4), Long.valueOf(j3));
    }
}

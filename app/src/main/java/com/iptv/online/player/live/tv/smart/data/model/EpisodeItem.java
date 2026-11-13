package com.iptv.online.player.live.tv.smart.data.model;

import java.io.Serializable;

/* loaded from: classes5.dex */
public class EpisodeItem implements Serializable {
    private String containerExtension;
    private String coverUrl;
    private String duration;
    private int episodeNum;
    private String id;
    private String plot;
    private String rating;
    private String releaseDate;
    private int seasonNum;
    private String streamUrl;
    private String title;

    public EpisodeItem() {
    }

    public EpisodeItem(String str, int i, int i2, String str2, String str3) {
        this.id = str;
        this.seasonNum = i;
        this.episodeNum = i2;
        this.title = str2;
        this.streamUrl = str3;
    }

    public String getId() {
        String str = this.id;
        return str != null ? str : this.seasonNum + "x" + this.episodeNum;
    }

    public void setId(String str) {
        this.id = str;
    }

    public int getEpisodeNum() {
        return this.episodeNum;
    }

    public void setEpisodeNum(int i) {
        this.episodeNum = i;
    }

    public int getSeasonNum() {
        return this.seasonNum;
    }

    public void setSeasonNum(int i) {
        this.seasonNum = i;
    }

    public String getTitle() {
        String str = this.title;
        return (str == null || str.trim().isEmpty()) ? "Episode " + this.episodeNum : this.title.trim();
    }

    public void setTitle(String str) {
        this.title = str;
    }

    public String getContainerExtension() {
        String str = this.containerExtension;
        return (str == null || str.isEmpty()) ? "mp4" : this.containerExtension;
    }

    public void setContainerExtension(String str) {
        this.containerExtension = str;
    }

    public String getPlot() {
        String str = this.plot;
        return (str == null || str.trim().isEmpty()) ? "No episode description available." : this.plot.trim();
    }

    public void setPlot(String str) {
        this.plot = str;
    }

    public String getDuration() {
        String str = this.duration;
        return (str == null || str.isEmpty()) ? "" : this.duration;
    }

    public void setDuration(String str) {
        this.duration = str;
    }

    public String getReleaseDate() {
        String str = this.releaseDate;
        return str != null ? str : "";
    }

    public void setReleaseDate(String str) {
        this.releaseDate = str;
    }

    public String getRating() {
        String str = this.rating;
        return str != null ? str : "N/A";
    }

    public void setRating(String str) {
        this.rating = str;
    }

    public String getStreamUrl() {
        String str = this.streamUrl;
        return str != null ? str : "";
    }

    public void setStreamUrl(String str) {
        this.streamUrl = str;
    }

    public String getCoverUrl() {
        return this.coverUrl;
    }

    public void setCoverUrl(String str) {
        this.coverUrl = str;
    }
}

package com.iptv.online.player.live.tv.smart.data.model;

import java.io.Serializable;
import java.util.Objects;

/* loaded from: classes5.dex */
public class VodItem implements Serializable {
    private String backdropUrl;
    private String cast;
    private String categoryId;
    private String categoryName;
    private String containerExtension;
    private String director;
    private String duration;
    private String genre;
    private String id;
    private boolean isSeries;
    private String name;
    private String plot;
    private String rating;
    private String releaseDate;
    private int seriesId;
    private String streamIcon;
    private int streamId;
    private String streamUrl;

    public VodItem() {
    }

    public VodItem(String str, String str2, String str3, String str4, String str5, boolean z) {
        this.id = str;
        this.name = str2;
        this.streamUrl = str3;
        this.streamIcon = str4;
        this.genre = str5;
        this.isSeries = z;
    }

    public String getId() {
        String str = this.id;
        if (str != null) {
            return str;
        }
        return String.valueOf(this.isSeries ? this.seriesId : this.streamId);
    }

    public void setId(String str) {
        this.id = str;
    }

    public String getName() {
        String str = this.name;
        return (str == null || str.trim().isEmpty()) ? "Unknown Title" : this.name.trim();
    }

    public void setName(String str) {
        this.name = str;
    }

    public int getStreamId() {
        return this.streamId;
    }

    public void setStreamId(int i) {
        this.streamId = i;
    }

    public int getSeriesId() {
        return this.seriesId;
    }

    public void setSeriesId(int i) {
        this.seriesId = i;
    }

    public String getCategoryId() {
        return this.categoryId;
    }

    public void setCategoryId(String str) {
        this.categoryId = str;
    }

    public String getCategoryName() {
        String str = this.categoryName;
        return (str == null || str.trim().isEmpty()) ? "General" : this.categoryName.trim();
    }

    public void setCategoryName(String str) {
        this.categoryName = str;
    }

    public String getStreamIcon() {
        return this.streamIcon;
    }

    public void setStreamIcon(String str) {
        this.streamIcon = str;
    }

    public String getBackdropUrl() {
        String str = this.backdropUrl;
        return (str == null || str.isEmpty()) ? this.streamIcon : this.backdropUrl;
    }

    public void setBackdropUrl(String str) {
        this.backdropUrl = str;
    }

    public String getRating() {
        String str = this.rating;
        return (str == null || str.trim().isEmpty()) ? "N/A" : this.rating.trim();
    }

    public void setRating(String str) {
        this.rating = str;
    }

    public String getReleaseDate() {
        String str = this.releaseDate;
        return str != null ? str : "";
    }

    public void setReleaseDate(String str) {
        this.releaseDate = str;
    }

    public String getPlot() {
        String str = this.plot;
        return (str == null || str.trim().isEmpty()) ? "No synopsis available for this title." : this.plot.trim();
    }

    public void setPlot(String str) {
        this.plot = str;
    }

    public String getCast() {
        String str = this.cast;
        return str != null ? str : "";
    }

    public void setCast(String str) {
        this.cast = str;
    }

    public String getDirector() {
        String str = this.director;
        return str != null ? str : "";
    }

    public void setDirector(String str) {
        this.director = str;
    }

    public String getGenre() {
        String str = this.genre;
        return (str == null || str.trim().isEmpty()) ? "Action / Drama" : this.genre.trim();
    }

    public void setGenre(String str) {
        this.genre = str;
    }

    public String getDuration() {
        String str = this.duration;
        return str != null ? str : "";
    }

    public void setDuration(String str) {
        this.duration = str;
    }

    public String getContainerExtension() {
        String str = this.containerExtension;
        return (str == null || str.isEmpty()) ? "mp4" : this.containerExtension;
    }

    public void setContainerExtension(String str) {
        this.containerExtension = str;
    }

    public boolean isSeries() {
        return this.isSeries;
    }

    public void setSeries(boolean z) {
        this.isSeries = z;
    }

    public String getStreamUrl() {
        String str = this.streamUrl;
        return str != null ? str : "";
    }

    public void setStreamUrl(String str) {
        this.streamUrl = str;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj != null && getClass() == obj.getClass()) {
            VodItem vodItem = (VodItem) obj;
            if (this.isSeries == vodItem.isSeries && Objects.equals(getId(), vodItem.getId())) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(getId(), Boolean.valueOf(this.isSeries));
    }
}

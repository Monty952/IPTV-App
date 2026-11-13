package com.iptv.online.player.live.tv.smart.data.model;

import com.iptv.online.player.live.tv.smart.utils.CountryUtils;
import java.io.Serializable;
import java.util.Objects;

/* loaded from: classes5.dex */
public class PlaylistItem implements Serializable {
    private String containerExtension;
    private String countryCode;
    private String countryName;
    private String groupTitle;
    private String id;
    private boolean isFavorite;
    private String logoUrl;
    private String name;
    private String referrer;
    private int streamId;
    private String streamUrl;
    private String tvgId;
    private String tvgName;
    private String userAgent;

    public PlaylistItem() {
    }

    public PlaylistItem(String str, String str2, String str3, String str4, String str5) {
        this.id = str;
        this.name = str2;
        this.streamUrl = str3;
        this.logoUrl = str4;
        this.groupTitle = (str5 == null || str5.trim().isEmpty()) ? "Uncategorized" : str5.trim();
    }

    public String getId() {
        String str = this.id;
        if (str != null) {
            return str;
        }
        String str2 = this.streamUrl;
        return str2 != null ? str2 : this.name;
    }

    public void setId(String str) {
        this.id = str;
    }

    public String getName() {
        String str = this.name;
        return (str == null || str.trim().isEmpty()) ? "Unknown Channel" : this.name.trim();
    }

    public void setName(String str) {
        this.name = str;
    }

    public String getStreamUrl() {
        String str = this.streamUrl;
        return str != null ? str.trim() : "";
    }

    public void setStreamUrl(String str) {
        this.streamUrl = str;
    }

    public String getLogoUrl() {
        return this.logoUrl;
    }

    public void setLogoUrl(String str) {
        this.logoUrl = str;
    }

    public String getGroupTitle() {
        String str = this.groupTitle;
        return (str == null || str.trim().isEmpty()) ? "General" : this.groupTitle.trim();
    }

    public void setGroupTitle(String str) {
        this.groupTitle = str;
    }

    public String getTvgId() {
        return this.tvgId;
    }

    public void setTvgId(String str) {
        this.tvgId = str;
    }

    public String getTvgName() {
        return this.tvgName;
    }

    public void setTvgName(String str) {
        this.tvgName = str;
    }

    public String getUserAgent() {
        return this.userAgent;
    }

    public void setUserAgent(String str) {
        this.userAgent = str;
    }

    public String getReferrer() {
        return this.referrer;
    }

    public void setReferrer(String str) {
        this.referrer = str;
    }

    public boolean isFavorite() {
        return this.isFavorite;
    }

    public void setFavorite(boolean z) {
        this.isFavorite = z;
    }

    public int getStreamId() {
        return this.streamId;
    }

    public void setStreamId(int i) {
        this.streamId = i;
    }

    public String getContainerExtension() {
        return this.containerExtension;
    }

    public void setContainerExtension(String str) {
        this.containerExtension = str;
    }

    public String getCountryCode() {
        String str;
        String str2 = this.countryCode;
        if ((str2 == null || str2.trim().isEmpty()) && ((str = this.tvgId) != null || this.groupTitle != null)) {
            this.countryCode = CountryUtils.extractCountryCode(null, str, this.groupTitle);
        }
        String str3 = this.countryCode;
        return str3 != null ? str3 : "";
    }

    public void setCountryCode(String str) {
        this.countryCode = str;
    }

    public String getCountryName() {
        String str = this.countryName;
        if (str == null || str.trim().isEmpty()) {
            String countryCode = getCountryCode();
            if (!countryCode.isEmpty()) {
                this.countryName = CountryUtils.getCountryNameFromCode(countryCode);
            }
        }
        String str2 = this.countryName;
        return (str2 == null || str2.trim().isEmpty()) ? "Other" : this.countryName;
    }

    public void setCountryName(String str) {
        this.countryName = str;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        PlaylistItem playlistItem = (PlaylistItem) obj;
        return Objects.equals(getId(), playlistItem.getId()) || Objects.equals(getStreamUrl(), playlistItem.getStreamUrl());
    }

    public int hashCode() {
        return Objects.hash(getId(), getStreamUrl());
    }
}

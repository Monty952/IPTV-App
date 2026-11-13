package com.iptv.online.player.live.tv.smart.ui.dialogs;

/* loaded from: classes5.dex */
public class CountryFilterItem {
    private final int channelCount;
    private final String countryCode;
    private final String countryName;
    private final String flagEmoji;
    private boolean isSelected;
    private boolean isUserCountry = false;

    public CountryFilterItem(String str, String str2, String str3, int i, boolean z) {
        this.countryCode = str;
        this.countryName = str2;
        this.flagEmoji = str3;
        this.channelCount = i;
        this.isSelected = z;
    }

    public String getCountryCode() {
        return this.countryCode;
    }

    public String getCountryName() {
        return this.countryName;
    }

    public String getFlagEmoji() {
        return this.flagEmoji;
    }

    public int getChannelCount() {
        return this.channelCount;
    }

    public boolean isSelected() {
        return this.isSelected;
    }

    public void setSelected(boolean z) {
        this.isSelected = z;
    }

    public boolean isUserCountry() {
        return this.isUserCountry;
    }

    public void setUserCountry(boolean z) {
        this.isUserCountry = z;
    }
}

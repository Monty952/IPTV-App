package com.iptv.online.player.live.tv.smart.data.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/* loaded from: classes5.dex */
public class Category implements Serializable {
    private int channelCount;
    private List<PlaylistItem> channels = new ArrayList();
    private String id;
    private boolean isLocked;
    private String name;

    public Category() {
    }

    public Category(String str, String str2) {
        this.id = str;
        this.name = str2;
    }

    public Category(String str) {
        this.id = str;
        this.name = str;
    }

    public String getId() {
        String str = this.id;
        return str != null ? str : this.name;
    }

    public void setId(String str) {
        this.id = str;
    }

    public String getName() {
        String str = this.name;
        return str != null ? str : "General";
    }

    public void setName(String str) {
        this.name = str;
    }

    public int getChannelCount() {
        List<PlaylistItem> list = this.channels;
        return (list == null || list.isEmpty()) ? this.channelCount : this.channels.size();
    }

    public void setChannelCount(int i) {
        this.channelCount = i;
    }

    public boolean isLocked() {
        return this.isLocked;
    }

    public void setLocked(boolean z) {
        this.isLocked = z;
    }

    public List<PlaylistItem> getChannels() {
        if (this.channels == null) {
            this.channels = new ArrayList();
        }
        return this.channels;
    }

    public void setChannels(List<PlaylistItem> list) {
        this.channels = list;
        if (list != null) {
            this.channelCount = list.size();
        }
    }

    public void addChannel(PlaylistItem playlistItem) {
        if (this.channels == null) {
            this.channels = new ArrayList();
        }
        this.channels.add(playlistItem);
        this.channelCount = this.channels.size();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return Objects.equals(getName().toLowerCase(), ((Category) obj).getName().toLowerCase());
    }

    public int hashCode() {
        return Objects.hash(getName().toLowerCase());
    }
}

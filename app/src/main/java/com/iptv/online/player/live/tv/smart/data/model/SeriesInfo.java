package com.iptv.online.player.live.tv.smart.data.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: classes5.dex */
public class SeriesInfo implements Serializable {
    private VodItem seriesItem;
    private List<String> seasons = new ArrayList();
    private Map<String, List<EpisodeItem>> episodes = new HashMap();

    public SeriesInfo() {
    }

    public SeriesInfo(VodItem vodItem) {
        this.seriesItem = vodItem;
    }

    public VodItem getSeriesItem() {
        return this.seriesItem;
    }

    public void setSeriesItem(VodItem vodItem) {
        this.seriesItem = vodItem;
    }

    public List<String> getSeasons() {
        List<String> list = this.seasons;
        return list != null ? list : new ArrayList();
    }

    public void setSeasons(List<String> list) {
        this.seasons = list;
    }

    public Map<String, List<EpisodeItem>> getEpisodes() {
        Map<String, List<EpisodeItem>> map = this.episodes;
        return map != null ? map : new HashMap();
    }

    public void setEpisodes(Map<String, List<EpisodeItem>> map) {
        this.episodes = map;
    }

    public List<EpisodeItem> getEpisodesForSeason(String str) {
        Map<String, List<EpisodeItem>> map = this.episodes;
        if (map == null || str == null) {
            return new ArrayList();
        }
        List<EpisodeItem> list = map.get(str);
        return list != null ? list : new ArrayList();
    }
}

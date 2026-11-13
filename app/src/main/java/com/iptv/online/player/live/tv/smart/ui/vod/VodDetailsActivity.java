package com.iptv.online.player.live.tv.smart.ui.vod;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
//import com.adsUtils.adsloader.nativeads.NativeLoader;
//import com.adsUtils.adsloader.nativeads.NativeType;
//import com.adsUtils.analytics.AnalyticsTracker;
//import com.adsUtils.custom.CustomActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.button.MaterialButton;
import com.iptv.online.player.live.tv.smart.AdsPackage.CustomActivity;
import com.iptv.online.player.live.tv.smart.R;
import com.iptv.online.player.live.tv.smart.data.local.PlaylistStorageManager;
import com.iptv.online.player.live.tv.smart.data.model.EpisodeItem;
import com.iptv.online.player.live.tv.smart.data.model.PlaylistSource;
import com.iptv.online.player.live.tv.smart.data.model.SeriesInfo;
import com.iptv.online.player.live.tv.smart.data.model.VodCategory;
import com.iptv.online.player.live.tv.smart.data.model.VodItem;
import com.iptv.online.player.live.tv.smart.data.model.WatchProgress;
import com.iptv.online.player.live.tv.smart.data.network.XtreamCodesApi;
import com.iptv.online.player.live.tv.smart.ui.vod.EpisodeAdapter;
import com.iptv.online.player.live.tv.smart.ui.vod.VodCategoryChipAdapter;
import java.util.ArrayList;
@SuppressWarnings("all")

/* loaded from: classes5.dex */
public class VodDetailsActivity extends com.iptv.online.player.live.tv.smart.AdsPackage.CustomActivity {
    public static final String EXTRA_VOD_ITEM = "extra_vod_item";
    private PlaylistSource activeSource;
    private MaterialButton btnPlay;
    private MaterialButton btnRestart;
    private EpisodeAdapter episodeAdapter;
    private WatchProgress existingProgress;
    private ImageView ivBackdrop;
    private ImageView ivPoster;
    private LinearLayout layoutCast;
    private LinearLayout layoutSeriesSection;
    private ProgressBar progressBar;
    private RecyclerView rvEpisodes;
    private RecyclerView rvSeasons;
    private VodCategoryChipAdapter seasonChipAdapter;
    private SeriesInfo seriesInfo;
    private PlaylistStorageManager storageManager;
    private TextView tvCast;
    private TextView tvDirector;
    private TextView tvDuration;
    private TextView tvGenre;
    private TextView tvPlot;
    private TextView tvRating;
    private TextView tvTitle;
    private TextView tvYear;
    private VodItem vodItem;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.adsUtils.custom.CustomActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_vod_details);
//        NativeLoader.showNativeAds(this, (LinearLayout) findViewById(R.id.llAdsView), NativeType.BIG_NATIVE);
        PlaylistStorageManager playlistStorageManager = new PlaylistStorageManager(this);
        this.storageManager = playlistStorageManager;
        this.activeSource = playlistStorageManager.getActiveSource();
        parseIntent();
        initViews();
        bindItemData();
        setupMovieDetailsIfNeeded();
        setupSeriesSectionIfNeeded();
    }

    private void setupMovieDetailsIfNeeded() {
        VodItem vodItem;
        PlaylistSource playlistSource = this.activeSource;
        if (playlistSource == null || playlistSource.getType() != PlaylistSource.SourceType.XTREAM_CODES || (vodItem = this.vodItem) == null || vodItem.isSeries() || this.vodItem.getStreamId() <= 0) {
            return;
        }
        XtreamCodesApi.fetchVodInfo(this.activeSource, this.vodItem.getStreamId(), new XtreamCodesApi.ApiCallback<VodItem>() { // from class: com.iptv.online.player.live.tv.smart.ui.vod.VodDetailsActivity.1
            @Override // com.iptv.online.player.live.tv.smart.data.network.XtreamCodesApi.ApiCallback
            public void onError(Exception exc) {
            }

            @Override // com.iptv.online.player.live.tv.smart.data.network.XtreamCodesApi.ApiCallback
            public void onSuccess(VodItem vodItem2) {
                if (vodItem2 != null) {
                    if (vodItem2.getStreamUrl() != null && !vodItem2.getStreamUrl().isEmpty()) {
                        VodDetailsActivity.this.vodItem.setStreamUrl(vodItem2.getStreamUrl());
                    }
                    if (vodItem2.getPlot() != null && !vodItem2.getPlot().isEmpty()) {
                        VodDetailsActivity.this.vodItem.setPlot(vodItem2.getPlot());
                    }
                    if (vodItem2.getCast() != null && !vodItem2.getCast().isEmpty()) {
                        VodDetailsActivity.this.vodItem.setCast(vodItem2.getCast());
                    }
                    if (vodItem2.getDirector() != null && !vodItem2.getDirector().isEmpty()) {
                        VodDetailsActivity.this.vodItem.setDirector(vodItem2.getDirector());
                    }
                    if (vodItem2.getGenre() != null && !vodItem2.getGenre().isEmpty()) {
                        VodDetailsActivity.this.vodItem.setGenre(vodItem2.getGenre());
                    }
                    if (vodItem2.getDuration() != null && !vodItem2.getDuration().isEmpty()) {
                        VodDetailsActivity.this.vodItem.setDuration(vodItem2.getDuration());
                    }
                    if (vodItem2.getRating() != null && !vodItem2.getRating().isEmpty()) {
                        VodDetailsActivity.this.vodItem.setRating(vodItem2.getRating());
                    }
                    VodDetailsActivity.this.bindItemData();
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.adsUtils.custom.CustomActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        updatePlayAndResumeButton();
    }

    private void parseIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            this.vodItem = (VodItem) intent.getSerializableExtra(EXTRA_VOD_ITEM);
        }
        if (this.vodItem == null) {
            Toast.makeText(this, R.string.vod_err_content_not_found, 0).show();
            finish();
        }
    }

    private void initViews() {
        this.ivBackdrop = (ImageView) findViewById(R.id.ivDetailsBackdrop);
        this.ivPoster = (ImageView) findViewById(R.id.ivDetailsPoster);
        this.tvTitle = (TextView) findViewById(R.id.tvDetailsTitle);
        this.tvRating = (TextView) findViewById(R.id.tvDetailsRating);
        this.tvYear = (TextView) findViewById(R.id.tvDetailsYear);
        this.tvDuration = (TextView) findViewById(R.id.tvDetailsDuration);
        this.tvGenre = (TextView) findViewById(R.id.tvDetailsGenre);
        this.tvPlot = (TextView) findViewById(R.id.tvDetailsPlot);
        this.tvCast = (TextView) findViewById(R.id.tvDetailsCast);
        this.tvDirector = (TextView) findViewById(R.id.tvDetailsDirector);
        this.layoutCast = (LinearLayout) findViewById(R.id.layoutDetailsCast);
        this.btnPlay = (MaterialButton) findViewById(R.id.btnDetailsPlay);
        this.btnRestart = (MaterialButton) findViewById(R.id.btnDetailsRestart);
        this.progressBar = (ProgressBar) findViewById(R.id.detailsProgressBar);
        this.layoutSeriesSection = (LinearLayout) findViewById(R.id.layoutSeriesSection);
        this.rvSeasons = (RecyclerView) findViewById(R.id.rvSeasons);
        this.rvEpisodes = (RecyclerView) findViewById(R.id.rvEpisodes);
        findViewById(R.id.btnDetailsBack).setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.vod.VodDetailsActivity$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                VodDetailsActivity.this.lambda$initViews$0(view);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initViews$0(View view) {
        setOnBackPressedClick();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void bindItemData() {
        VodItem vodItem = this.vodItem;
        if (vodItem == null) {
            return;
        }
        this.tvTitle.setText(vodItem.getName());
        this.tvRating.setText(this.vodItem.getRating());
        this.tvYear.setText(this.vodItem.getReleaseDate().isEmpty() ? "HD" : this.vodItem.getReleaseDate());
        TextView textView = this.tvDuration;
        boolean isEmpty = this.vodItem.getDuration().isEmpty();
        VodItem vodItem2 = this.vodItem;
        textView.setText(isEmpty ? vodItem2.isSeries() ? "Multi-Episode" : "Movie" : vodItem2.getDuration());
        this.tvGenre.setText(this.vodItem.getGenre());
        this.tvPlot.setText(this.vodItem.getPlot());
        if (!this.vodItem.getCast().isEmpty() || !this.vodItem.getDirector().isEmpty()) {
            this.layoutCast.setVisibility(View.VISIBLE);
            this.tvCast.setText("Cast: " + (this.vodItem.getCast().isEmpty() ? "N/A" : this.vodItem.getCast()));
            this.tvDirector.setText("Director: " + (this.vodItem.getDirector().isEmpty() ? "N/A" : this.vodItem.getDirector()));
        } else {
            this.layoutCast.setVisibility(View.GONE);
        }
        String streamIcon = this.vodItem.getStreamIcon();
        RequestBuilder<Drawable> load = Glide.with((FragmentActivity) this).load(this.vodItem.getBackdropUrl());
        this.vodItem.isSeries();
        this.vodItem.isSeries();
        load.placeholder(R.drawable.ic_cast_channels).error(R.drawable.ic_cast_channels).diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop().into(this.ivBackdrop);
        RequestBuilder<Drawable> load2 = Glide.with((FragmentActivity) this).load(streamIcon);
        this.vodItem.isSeries();
        this.vodItem.isSeries();
        load2.placeholder(R.drawable.ic_cast_channels).error(R.drawable.ic_cast_channels).diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop().into(this.ivPoster);
        updatePlayAndResumeButton();
    }

    private void updatePlayAndResumeButton() {
        VodItem vodItem = this.vodItem;
        if (vodItem == null) {
            return;
        }
        this.existingProgress = this.storageManager.getWatchProgress(vodItem.getId());
        if (this.vodItem.isSeries()) {
            this.btnPlay.setText(R.string.vod_browse_episodes);
            this.btnRestart.setVisibility(View.GONE);
            this.btnPlay.setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.vod.VodDetailsActivity$$ExternalSyntheticLambda1
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    VodDetailsActivity.this.lambda$updatePlayAndResumeButton$1(view);
                }
            });
            return;
        }
        WatchProgress watchProgress = this.existingProgress;
        if (watchProgress != null && watchProgress.getPositionMs() > 5000) {
            this.btnPlay.setText(getString(R.string.vod_resume_from, new Object[]{this.existingProgress.getFormattedPosition()}));
            this.btnRestart.setVisibility(View.VISIBLE);
            this.btnPlay.setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.vod.VodDetailsActivity$$ExternalSyntheticLambda2
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    VodDetailsActivity.this.lambda$updatePlayAndResumeButton$2(view);
                }
            });
            this.btnRestart.setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.vod.VodDetailsActivity$$ExternalSyntheticLambda3
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    VodDetailsActivity.this.lambda$updatePlayAndResumeButton$3(view);
                }
            });
            return;
        }
        this.btnPlay.setText(R.string.vod_play_movie);
        this.btnRestart.setVisibility(View.GONE);
        this.btnPlay.setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.vod.VodDetailsActivity$$ExternalSyntheticLambda4
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                VodDetailsActivity.this.lambda$updatePlayAndResumeButton$4(view);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updatePlayAndResumeButton$1(View view) {
        RecyclerView recyclerView = this.rvEpisodes;
        if (recyclerView != null) {
            recyclerView.requestFocus();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updatePlayAndResumeButton$2(View view) {
        launchPlayer(this.vodItem.getStreamUrl(), this.vodItem.getName(), this.existingProgress.getPositionMs());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updatePlayAndResumeButton$3(View view) {
        launchPlayer(this.vodItem.getStreamUrl(), this.vodItem.getName(), 0L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updatePlayAndResumeButton$4(View view) {
        launchPlayer(this.vodItem.getStreamUrl(), this.vodItem.getName(), 0L);
    }

    private void setupSeriesSectionIfNeeded() {
        VodItem vodItem = this.vodItem;
        if (vodItem == null || !vodItem.isSeries()) {
            this.layoutSeriesSection.setVisibility(View.GONE);
            return;
        }
        this.layoutSeriesSection.setVisibility(View.VISIBLE);
        this.progressBar.setVisibility(View.VISIBLE);
        VodCategoryChipAdapter vodCategoryChipAdapter = new VodCategoryChipAdapter(new VodCategoryChipAdapter.OnCategorySelectListener() { // from class: com.iptv.online.player.live.tv.smart.ui.vod.VodDetailsActivity$$ExternalSyntheticLambda5
            @Override // com.iptv.online.player.live.tv.smart.ui.vod.VodCategoryChipAdapter.OnCategorySelectListener
            public final void onCategorySelected(VodCategory vodCategory, int i) {
                VodDetailsActivity.this.lambda$setupSeriesSectionIfNeeded$5(vodCategory, i);
            }
        });
        this.seasonChipAdapter = vodCategoryChipAdapter;
        this.rvSeasons.setAdapter(vodCategoryChipAdapter);
        EpisodeAdapter episodeAdapter = new EpisodeAdapter(this, new EpisodeAdapter.OnEpisodeClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.vod.VodDetailsActivity$$ExternalSyntheticLambda6
            @Override // com.iptv.online.player.live.tv.smart.ui.vod.EpisodeAdapter.OnEpisodeClickListener
            public final void onEpisodeClick(EpisodeItem episodeItem, int i) {
                VodDetailsActivity.this.lambda$setupSeriesSectionIfNeeded$6(episodeItem, i);
            }
        });
        this.episodeAdapter = episodeAdapter;
        this.rvEpisodes.setAdapter(episodeAdapter);
        XtreamCodesApi.fetchSeriesInfo(this.activeSource, this.vodItem.getSeriesId(), new XtreamCodesApi.ApiCallback<SeriesInfo>() { // from class: com.iptv.online.player.live.tv.smart.ui.vod.VodDetailsActivity.2
            @Override // com.iptv.online.player.live.tv.smart.data.network.XtreamCodesApi.ApiCallback
            public void onSuccess(SeriesInfo seriesInfo) {
                VodDetailsActivity.this.progressBar.setVisibility(View.GONE);
                VodDetailsActivity.this.seriesInfo = seriesInfo;
                if (seriesInfo == null || seriesInfo.getSeasons().isEmpty()) {
                    return;
                }
                ArrayList arrayList = new ArrayList();
                for (String str : seriesInfo.getSeasons()) {
                    arrayList.add(new VodCategory(str, str));
                }
                VodDetailsActivity.this.seasonChipAdapter.setCategories(arrayList);
                if (arrayList.isEmpty()) {
                    return;
                }
                VodDetailsActivity.this.episodeAdapter.setEpisodes(seriesInfo.getEpisodesForSeason(((VodCategory) arrayList.get(0)).getCategoryName()));
            }

            @Override // com.iptv.online.player.live.tv.smart.data.network.XtreamCodesApi.ApiCallback
            public void onError(Exception exc) {
                VodDetailsActivity.this.progressBar.setVisibility(View.GONE);
                VodDetailsActivity vodDetailsActivity = VodDetailsActivity.this;
                Toast.makeText(vodDetailsActivity, vodDetailsActivity.getString(R.string.vod_err_load_episodes, new Object[]{exc.getMessage()}), 0).show();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupSeriesSectionIfNeeded$5(VodCategory vodCategory, int i) {
//        AnalyticsTracker.logAction("VodDetailsActivity", "select_season", vodCategory.getCategoryName());
        SeriesInfo seriesInfo = this.seriesInfo;
        if (seriesInfo != null) {
            this.episodeAdapter.setEpisodes(seriesInfo.getEpisodesForSeason(vodCategory.getCategoryName()));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupSeriesSectionIfNeeded$6(EpisodeItem episodeItem, int i) {
        launchEpisodePlayer(episodeItem);
    }

    private void launchPlayer(String str, String str2, long j) {
        if (str == null || str.trim().isEmpty()) {
            Toast.makeText(this, R.string.vod_err_stream_unavailable, 0).show();
            return;
        }
//        AnalyticsTracker.logPlayback("VodDetailsActivity", str2, "VOD_MOVIE", str, this.vodItem.getGenre());
        Intent intent = new Intent(this, VodPlayerActivity.class);
        intent.putExtra(VodPlayerActivity.EXTRA_STREAM_URL, str);
        intent.putExtra(VodPlayerActivity.EXTRA_TITLE, str2);
        intent.putExtra(VodPlayerActivity.EXTRA_POSTER_URL, this.vodItem.getStreamIcon());
        intent.putExtra(VodPlayerActivity.EXTRA_CONTENT_ID, this.vodItem.getId());
        intent.putExtra(VodPlayerActivity.EXTRA_RESUME_POSITION, j);
//        startAdsActivity(intent);
        startActivity(intent);
    }

    private void launchEpisodePlayer(EpisodeItem episodeItem) {
        if (episodeItem == null || episodeItem.getStreamUrl().isEmpty()) {
            Toast.makeText(this, R.string.vod_err_episode_unavailable, 0).show();
            return;
        }
        String str = this.vodItem.getId() + "_s" + episodeItem.getSeasonNum() + "e" + episodeItem.getEpisodeNum();
        WatchProgress watchProgress = this.storageManager.getWatchProgress(str);
        long positionMs = watchProgress != null ? watchProgress.getPositionMs() : 0L;
//        AnalyticsTracker.logPlayback("VodDetailsActivity", episodeItem.getTitle(), "VOD_EPISODE", episodeItem.getStreamUrl(), this.vodItem.getName());
        Intent intent = new Intent(this, VodPlayerActivity.class);
        intent.putExtra(VodPlayerActivity.EXTRA_STREAM_URL, episodeItem.getStreamUrl());
        intent.putExtra(VodPlayerActivity.EXTRA_TITLE, this.vodItem.getName());
        intent.putExtra(VodPlayerActivity.EXTRA_SUBTITLE, "Season " + episodeItem.getSeasonNum() + " • Episode " + episodeItem.getEpisodeNum() + " (" + episodeItem.getTitle() + ")");
        intent.putExtra(VodPlayerActivity.EXTRA_POSTER_URL, episodeItem.getCoverUrl() != null ? episodeItem.getCoverUrl() : this.vodItem.getStreamIcon());
        intent.putExtra(VodPlayerActivity.EXTRA_CONTENT_ID, str);
        intent.putExtra(VodPlayerActivity.EXTRA_RESUME_POSITION, positionMs);
//        startAdsActivity(intent);
        startActivity(intent);
    }
}

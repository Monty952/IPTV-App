package com.iptv.online.player.live.tv.smart.ui.vod;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
//import androidx.media3.extractor.text.ttml.TtmlNode;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
//import com.adsUtils.adsloader.bannerads.BannerLoader;
//import com.adsUtils.adsloader.bannerads.BannerType;
//import com.adsUtils.analytics.AnalyticsTracker;
//import com.adsUtils.custom.CustomActivity;
//import com.iptv.online.player.live.tv.smart.AdsPackage.CustomActivity;
import com.iptv.online.player.live.tv.smart.R;
import com.iptv.online.player.live.tv.smart.data.local.PlaylistStorageManager;
import com.iptv.online.player.live.tv.smart.data.model.PlaylistItem;
import com.iptv.online.player.live.tv.smart.data.model.PlaylistSource;
import com.iptv.online.player.live.tv.smart.data.model.VodCategory;
import com.iptv.online.player.live.tv.smart.data.model.VodItem;
import com.iptv.online.player.live.tv.smart.data.model.WatchProgress;
import com.iptv.online.player.live.tv.smart.data.network.XtreamCodesApi;
import com.iptv.online.player.live.tv.smart.ui.vod.ContinueWatchingAdapter;
import com.iptv.online.player.live.tv.smart.ui.vod.VodCategoryChipAdapter;
import com.iptv.online.player.live.tv.smart.ui.vod.VodPosterAdapter;
import java.util.ArrayList;
import java.util.List;
@SuppressWarnings("all")

/* loaded from: classes5.dex */
public class VodMainActivity extends com.iptv.online.player.live.tv.smart.AdsPackage.CustomActivity {
    private PlaylistSource activeSource;
    private VodCategoryChipAdapter categoryAdapter;
    private ContinueWatchingAdapter continueAdapter;
    private EditText etSearch;
    private LinearLayout layoutEmpty;
    private LinearLayout layoutSearch;
    private VodPosterAdapter posterAdapter;
    private ProgressBar progressBar;
    private RecyclerView rvCategories;
    private RecyclerView rvItems;
    private PlaylistStorageManager storageManager;
    private SwipeRefreshLayout swipeRefresh;
    private TextView tabContinue;
    private TextView tabMovies;
    private TextView tabSeries;
    private TextView tvEmptySubtitle;
    private TextView tvEmptyTitle;
    private VodTab currentTab = VodTab.MOVIES;
    private String currentCategoryId = "all";

    /* loaded from: classes5.dex */
    public enum VodTab {
        MOVIES,
        SERIES,
        CONTINUE_WATCHING
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.adsUtils.custom.CustomActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_vod_main);
        View findViewById = findViewById(R.id.llAdsView);
        if (findViewById != null && (findViewById instanceof ViewGroup)) {
//            BannerLoader.showBannerAds(this, (ViewGroup) findViewById, BannerType.FIXED_BANNER);
        }
        PlaylistStorageManager playlistStorageManager = new PlaylistStorageManager(this);
        this.storageManager = playlistStorageManager;
        this.activeSource = playlistStorageManager.getActiveSource();
        initViews();
        setupRecyclerViews();
        setupListeners();
        switchTab(VodTab.MOVIES);
        Log.d("TAG", "onCreate: of VodMainActivity");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.adsUtils.custom.CustomActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        if (this.currentTab == VodTab.CONTINUE_WATCHING) {
            loadContinueWatching();
            return;
        }
        VodPosterAdapter vodPosterAdapter = this.posterAdapter;
        if (vodPosterAdapter != null) {
            vodPosterAdapter.updateAdConfig();
            this.posterAdapter.notifyDataSetChanged();
        }
    }

    private void initViews() {
        this.tabMovies = (TextView) findViewById(R.id.tabVodMovies);
        this.tabSeries = (TextView) findViewById(R.id.tabVodSeries);
        this.tabContinue = (TextView) findViewById(R.id.tabVodContinue);
        this.rvCategories = (RecyclerView) findViewById(R.id.rvVodCategories);
        this.rvItems = (RecyclerView) findViewById(R.id.rvVodItems);
        this.swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshVod);
        this.progressBar = (ProgressBar) findViewById(R.id.vodProgressBar);
        this.layoutEmpty = (LinearLayout) findViewById(R.id.layoutVodEmpty);
        this.tvEmptyTitle = (TextView) findViewById(R.id.tvVodEmptyTitle);
        this.tvEmptySubtitle = (TextView) findViewById(R.id.tvVodEmptySubtitle);
        this.layoutSearch = (LinearLayout) findViewById(R.id.layoutVodSearch);
        this.etSearch = (EditText) findViewById(R.id.etVodSearch);
        this.swipeRefresh.setColorSchemeResources(R.color.brand_blue, R.color.brand_purple);
    }

    private void setupRecyclerViews() {
        VodCategoryChipAdapter vodCategoryChipAdapter = new VodCategoryChipAdapter(new VodCategoryChipAdapter.OnCategorySelectListener() { // from class: com.iptv.online.player.live.tv.smart.ui.vod.VodMainActivity$$ExternalSyntheticLambda8
            @Override // com.iptv.online.player.live.tv.smart.ui.vod.VodCategoryChipAdapter.OnCategorySelectListener
            public final void onCategorySelected(VodCategory vodCategory, int i) {
                VodMainActivity.this.lambda$setupRecyclerViews$0(vodCategory, i);
            }
        });
        this.categoryAdapter = vodCategoryChipAdapter;
        this.rvCategories.setAdapter(vodCategoryChipAdapter);
        setupItemsLayoutManager();
        this.posterAdapter = new VodPosterAdapter(this, this.storageManager, new VodPosterAdapter.OnVodItemClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.vod.VodMainActivity$$ExternalSyntheticLambda9
            @Override // com.iptv.online.player.live.tv.smart.ui.vod.VodPosterAdapter.OnVodItemClickListener
            public final void onVodItemClick(VodItem vodItem, int i) {
                VodMainActivity.this.lambda$setupRecyclerViews$1(vodItem, i);
            }
        });
        this.continueAdapter = new ContinueWatchingAdapter(this, new ContinueWatchingAdapter.OnContinueWatchingListener() { // from class: com.iptv.online.player.live.tv.smart.ui.vod.VodMainActivity.1
            @Override // com.iptv.online.player.live.tv.smart.ui.vod.ContinueWatchingAdapter.OnContinueWatchingListener
            public void onResumeClick(WatchProgress watchProgress) {
//                AnalyticsTracker.logAction("VodMainActivity", "resume_watch", watchProgress.getTitle());
                Intent intent = new Intent(VodMainActivity.this, VodPlayerActivity.class);
                intent.putExtra(VodPlayerActivity.EXTRA_STREAM_URL, watchProgress.getStreamUrl());
                intent.putExtra(VodPlayerActivity.EXTRA_TITLE, watchProgress.getTitle());
                intent.putExtra(VodPlayerActivity.EXTRA_POSTER_URL, watchProgress.getPosterUrl());
                intent.putExtra(VodPlayerActivity.EXTRA_CONTENT_ID, watchProgress.getContentId());
                intent.putExtra(VodPlayerActivity.EXTRA_RESUME_POSITION, watchProgress.getPositionMs());
                VodMainActivity.this.startActivity(intent);
//                VodMainActivity.this.startAdsActivity(intent);
            }

            @Override // com.iptv.online.player.live.tv.smart.ui.vod.ContinueWatchingAdapter.OnContinueWatchingListener
            public void onDeleteClick(WatchProgress watchProgress, int i) {
                if (VodMainActivity.this.storageManager != null && watchProgress != null) {
//                    AnalyticsTracker.logAction("VodMainActivity", "delete_watch_progress", watchProgress.getTitle());
                    VodMainActivity.this.storageManager.deleteWatchProgress(watchProgress.getContentId());
                }
                VodMainActivity.this.continueAdapter.removeItem(i);
                if (VodMainActivity.this.continueAdapter.getItemCount() == 0) {
                    VodMainActivity.this.checkEmptyState(true, "No Watched Content", "Start playing any movie or TV series to track watch progress here!");
                }
            }
        });
        this.rvItems.setAdapter(this.posterAdapter);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupRecyclerViews$0(VodCategory vodCategory, int i) {
//        AnalyticsTracker.logAction("VodMainActivity", "select_category", vodCategory.getCategoryName());
        this.currentCategoryId = vodCategory.getCategoryId();
        loadContentForCurrentTab(false);
//        showTabScrollInterstitialAds();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupRecyclerViews$1(VodItem vodItem, int i) {
//        AnalyticsTracker.logAction("VodMainActivity", "select_vod_item", vodItem.getName());
        Intent intent = new Intent(this, VodDetailsActivity.class);
        intent.putExtra(VodDetailsActivity.EXTRA_VOD_ITEM, vodItem);
        startActivity(intent);
//        startAdsActivity(intent);
    }

    private void setupItemsLayoutManager() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() { // from class: com.iptv.online.player.live.tv.smart.ui.vod.VodMainActivity.2
            @Override // androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
            public int getSpanSize(int i) {
                return (VodMainActivity.this.posterAdapter == null || !VodMainActivity.this.posterAdapter.isAdPosition(i)) ? 1 : 2;
            }
        });
        this.rvItems.setLayoutManager(gridLayoutManager);
    }

    private int calculateContinueSpanCount() {
        int i = getResources().getConfiguration().orientation;
        int i2 = getResources().getConfiguration().screenWidthDp;
        if (i2 >= 900) {
            return 3;
        }
        return (i2 >= 600 || i == 2) ? 2 : 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupListeners$2(View view) {
        setOnBackPressedClick();
    }

    private void setupListeners() {
        findViewById(R.id.btnVodBack).setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.vod.VodMainActivity$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                VodMainActivity.this.lambda$setupListeners$2(view);
            }
        });
        findViewById(R.id.btnVodSearchToggle).setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.vod.VodMainActivity$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                VodMainActivity.this.lambda$setupListeners$3(view);
            }
        });
        findViewById(R.id.btnVodClearSearch).setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.vod.VodMainActivity$$ExternalSyntheticLambda2
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                VodMainActivity.this.lambda$setupListeners$4(view);
            }
        });
        this.etSearch.addTextChangedListener(new TextWatcher() { // from class: com.iptv.online.player.live.tv.smart.ui.vod.VodMainActivity.3
            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
            }

            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (VodMainActivity.this.posterAdapter != null) {
                    VodMainActivity.this.posterAdapter.getFilter().filter(charSequence);
                }
            }
        });
        this.etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: com.iptv.online.player.live.tv.smart.ui.vod.VodMainActivity$$ExternalSyntheticLambda3
            @Override // android.widget.TextView.OnEditorActionListener
            public final boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean lambda$setupListeners$5;
                lambda$setupListeners$5 = VodMainActivity.this.lambda$setupListeners$5(textView, i, keyEvent);
                return lambda$setupListeners$5;
            }
        });
        this.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() { // from class: com.iptv.online.player.live.tv.smart.ui.vod.VodMainActivity$$ExternalSyntheticLambda4
            @Override // androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
            public final void onRefresh() {
                VodMainActivity.this.lambda$setupListeners$6();
            }
        });
        this.tabMovies.setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.vod.VodMainActivity$$ExternalSyntheticLambda5
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                VodMainActivity.this.lambda$setupListeners$7(view);
            }
        });
        this.tabSeries.setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.vod.VodMainActivity$$ExternalSyntheticLambda6
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                VodMainActivity.this.lambda$setupListeners$8(view);
            }
        });
        this.tabContinue.setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.vod.VodMainActivity$$ExternalSyntheticLambda7
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                VodMainActivity.this.lambda$setupListeners$9(view);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupListeners$3(View view) {
        int visibility = this.layoutSearch.getVisibility();
        LinearLayout linearLayout = this.layoutSearch;
        if (visibility == 0) {
            linearLayout.setVisibility(View.GONE);
            this.etSearch.setText("");
            return;
        }
        linearLayout.setVisibility(View.VISIBLE);
        this.etSearch.requestFocus();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupListeners$4(View view) {
        this.etSearch.setText("");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$setupListeners$5(TextView textView, int i, KeyEvent keyEvent) {
        if (i != 3 || this.posterAdapter == null) {
            return false;
        }
        String obj = this.etSearch.getText().toString();
//        AnalyticsTracker.logSearch("VodMainActivity", obj);
        this.posterAdapter.getFilter().filter(obj);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupListeners$6() {
        loadContentForCurrentTab(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupListeners$7(View view) {
        switchTab(VodTab.MOVIES);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupListeners$8(View view) {
        switchTab(VodTab.SERIES);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupListeners$9(View view) {
        switchTab(VodTab.CONTINUE_WATCHING);
    }

    private void switchTab(VodTab vodTab) {
//        AnalyticsTracker.logAction("VodMainActivity", "switch_tab", vodTab.name());
//        showTabScrollInterstitialAds();
        this.currentTab = vodTab;
        this.currentCategoryId = "all";
        int color = ContextCompat.getColor(this, R.color.text_secondary);
        int color2 = ContextCompat.getColor(this, R.color.white);
        this.tabMovies.setBackground(null);
        this.tabMovies.setTextColor(color);
        this.tabSeries.setBackground(null);
        this.tabSeries.setTextColor(color);
        this.tabContinue.setBackground(null);
        this.tabContinue.setTextColor(color);
        if (vodTab == VodTab.MOVIES) {
            this.tabMovies.setBackgroundResource(R.drawable.bg_segmented_tab_selected);
            this.tabMovies.setTextColor(color2);
            this.rvCategories.setVisibility(View.VISIBLE);
            setupItemsLayoutManager();
            this.rvItems.setAdapter(this.posterAdapter);
            loadMovieCategories();
        } else if (vodTab == VodTab.SERIES) {
            this.tabSeries.setBackgroundResource(R.drawable.bg_segmented_tab_selected);
            this.tabSeries.setTextColor(color2);
            this.rvCategories.setVisibility(View.VISIBLE);
            setupItemsLayoutManager();
            this.rvItems.setAdapter(this.posterAdapter);
            loadSeriesCategories();
        } else {
            this.tabContinue.setBackgroundResource(R.drawable.bg_segmented_tab_selected);
            this.tabContinue.setTextColor(color2);
            this.rvCategories.setVisibility(View.GONE);
            this.rvItems.setLayoutManager(new GridLayoutManager(this, calculateContinueSpanCount()));
            this.rvItems.setAdapter(this.continueAdapter);
            loadContinueWatching();
        }
    }

    private void loadMovieCategories() {
        this.progressBar.setVisibility(View.VISIBLE);
        this.layoutEmpty.setVisibility(View.GONE);
        XtreamCodesApi.fetchVodCategories(this.activeSource, new XtreamCodesApi.ApiCallback<List<VodCategory>>() { // from class: com.iptv.online.player.live.tv.smart.ui.vod.VodMainActivity.4
            @Override // com.iptv.online.player.live.tv.smart.data.network.XtreamCodesApi.ApiCallback
            public void onSuccess(List<VodCategory> list) {
                VodMainActivity.this.categoryAdapter.setCategories(list);
                VodMainActivity.this.loadContentForCurrentTab(false);
            }

            @Override // com.iptv.online.player.live.tv.smart.data.network.XtreamCodesApi.ApiCallback
            public void onError(Exception exc) {
                VodMainActivity.this.loadContentForCurrentTab(false);
            }
        });
    }

    private void loadSeriesCategories() {
        this.progressBar.setVisibility(View.VISIBLE);
        this.layoutEmpty.setVisibility(View.GONE);
        XtreamCodesApi.fetchSeriesCategories(this.activeSource, new XtreamCodesApi.ApiCallback<List<VodCategory>>() { // from class: com.iptv.online.player.live.tv.smart.ui.vod.VodMainActivity.5
            @Override // com.iptv.online.player.live.tv.smart.data.network.XtreamCodesApi.ApiCallback
            public void onSuccess(List<VodCategory> list) {
                VodMainActivity.this.categoryAdapter.setCategories(list);
                VodMainActivity.this.loadContentForCurrentTab(false);
            }

            @Override // com.iptv.online.player.live.tv.smart.data.network.XtreamCodesApi.ApiCallback
            public void onError(Exception exc) {
                VodMainActivity.this.loadContentForCurrentTab(false);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadContentForCurrentTab(boolean z) {
        if (!z) {
            this.progressBar.setVisibility(View.VISIBLE);
        }
        this.layoutEmpty.setVisibility(View.GONE);
        if (this.currentTab == VodTab.MOVIES) {
            XtreamCodesApi.fetchVodStreams(this.activeSource, this.currentCategoryId, new XtreamCodesApi.ApiCallback<List<VodItem>>() { // from class: com.iptv.online.player.live.tv.smart.ui.vod.VodMainActivity.6
                @Override // com.iptv.online.player.live.tv.smart.data.network.XtreamCodesApi.ApiCallback
                public void onSuccess(List<VodItem> list) {
                    VodMainActivity.this.progressBar.setVisibility(View.GONE);
                    VodMainActivity.this.swipeRefresh.setRefreshing(false);
                    List<VodItem> mergePlaylistVOD = VodMainActivity.this.mergePlaylistVOD(list, false);
                    VodMainActivity.this.posterAdapter.setItems(mergePlaylistVOD);
                    VodMainActivity.this.checkEmptyState(mergePlaylistVOD.isEmpty(), "No Movies Found", "No movies available in this category.");
                }

                @Override // com.iptv.online.player.live.tv.smart.data.network.XtreamCodesApi.ApiCallback
                public void onError(Exception exc) {
                    VodMainActivity.this.progressBar.setVisibility(View.GONE);
                    VodMainActivity.this.swipeRefresh.setRefreshing(false);
                    List<VodItem> mergePlaylistVOD = VodMainActivity.this.mergePlaylistVOD(null, false);
                    boolean isEmpty = mergePlaylistVOD.isEmpty();
                    VodMainActivity vodMainActivity = VodMainActivity.this;
                    if (!isEmpty) {
                        vodMainActivity.posterAdapter.setItems(mergePlaylistVOD);
                        VodMainActivity.this.checkEmptyState(false, "", "");
                        return;
                    }
                    vodMainActivity.checkEmptyState(true, "Error Loading Movies", exc.getMessage());
                }
            });
        } else if (this.currentTab == VodTab.SERIES) {
            XtreamCodesApi.fetchSeries(this.activeSource, this.currentCategoryId, new XtreamCodesApi.ApiCallback<List<VodItem>>() { // from class: com.iptv.online.player.live.tv.smart.ui.vod.VodMainActivity.7
                @Override // com.iptv.online.player.live.tv.smart.data.network.XtreamCodesApi.ApiCallback
                public void onSuccess(List<VodItem> list) {
                    VodMainActivity.this.progressBar.setVisibility(View.GONE);
                    VodMainActivity.this.swipeRefresh.setRefreshing(false);
                    List<VodItem> mergePlaylistVOD = VodMainActivity.this.mergePlaylistVOD(list, true);
                    VodMainActivity.this.posterAdapter.setItems(mergePlaylistVOD);
                    VodMainActivity.this.checkEmptyState(mergePlaylistVOD.isEmpty(), "No TV Series Found", "No TV shows available in this category.");
                }

                @Override // com.iptv.online.player.live.tv.smart.data.network.XtreamCodesApi.ApiCallback
                public void onError(Exception exc) {
                    VodMainActivity.this.progressBar.setVisibility(View.GONE);
                    VodMainActivity.this.swipeRefresh.setRefreshing(false);
                    List<VodItem> mergePlaylistVOD = VodMainActivity.this.mergePlaylistVOD(null, true);
                    boolean isEmpty = mergePlaylistVOD.isEmpty();
                    VodMainActivity vodMainActivity = VodMainActivity.this;
                    if (!isEmpty) {
                        vodMainActivity.posterAdapter.setItems(mergePlaylistVOD);
                        VodMainActivity.this.checkEmptyState(false, "", "");
                        return;
                    }
                    vodMainActivity.checkEmptyState(true, "Error Loading Series", exc.getMessage());
                }
            });
        } else {
            loadContinueWatching();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public List<VodItem> mergePlaylistVOD(List<VodItem> list, boolean z) {
        List<PlaylistItem> cachedChannels;
        ArrayList arrayList = new ArrayList(list != null ? list : new ArrayList<>());
        PlaylistStorageManager playlistStorageManager = this.storageManager;
        if (playlistStorageManager != null && (cachedChannels = playlistStorageManager.getCachedChannels()) != null) {
            for (PlaylistItem playlistItem : cachedChannels) {
                String lowerCase = playlistItem.getGroupTitle() != null ? playlistItem.getGroupTitle().toLowerCase() : "";
                String lowerCase2 = playlistItem.getName() != null ? playlistItem.getName().toLowerCase() : "";
                String lowerCase3 = playlistItem.getStreamUrl() != null ? playlistItem.getStreamUrl().toLowerCase() : "";
                boolean z2 = false;
                boolean z3 = lowerCase.contains("series") || lowerCase.contains("season") || lowerCase.contains("episode") || lowerCase2.contains("s01") || lowerCase2.contains("s02");
                z2 = (lowerCase.contains("movie") || lowerCase.contains("vod") || lowerCase.contains("cinema") || lowerCase.contains("film") || lowerCase3.endsWith(".mp4") || lowerCase3.endsWith(".mkv") || lowerCase3.endsWith(".avi")) ? true : true;
                if (z && z3) {
                    VodItem vodItem = new VodItem("m3u_" + playlistItem.getId(), playlistItem.getName(), playlistItem.getStreamUrl(), playlistItem.getLogoUrl(), playlistItem.getGroupTitle(), true);
                    if (!arrayList.contains(vodItem)) {
                        arrayList.add(vodItem);
                    }
                } else if (!z && z2 && !z3) {
                    VodItem vodItem2 = new VodItem("m3u_" + playlistItem.getId(), playlistItem.getName(), playlistItem.getStreamUrl(), playlistItem.getLogoUrl(), playlistItem.getGroupTitle(), false);
                    if (!arrayList.contains(vodItem2)) {
                        arrayList.add(vodItem2);
                    }
                }
            }
        }
        return arrayList;
    }

    private void loadContinueWatching() {
        this.progressBar.setVisibility(View.GONE);
        this.swipeRefresh.setRefreshing(false);
        List<WatchProgress> continueWatchingList = this.storageManager.getContinueWatchingList();
        this.continueAdapter.setItems(continueWatchingList);
        checkEmptyState(continueWatchingList.isEmpty(), "No Watched Content", "Start playing any movie or TV series to track watch progress here!");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkEmptyState(boolean z, String str, String str2) {
        LinearLayout linearLayout = this.layoutEmpty;
        if (z) {
            linearLayout.setVisibility(View.VISIBLE);
            this.tvEmptyTitle.setText(str);
            this.tvEmptySubtitle.setText(str2);
            return;
        }
        linearLayout.setVisibility(View.GONE);
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.activity.ComponentActivity, android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        if (this.rvItems != null) {
            if (this.currentTab == VodTab.CONTINUE_WATCHING) {
                this.rvItems.setLayoutManager(new GridLayoutManager(this, calculateContinueSpanCount()));
            } else {
                setupItemsLayoutManager();
            }
        }
    }
}

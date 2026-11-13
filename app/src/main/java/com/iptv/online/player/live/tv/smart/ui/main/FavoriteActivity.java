package com.iptv.online.player.live.tv.smart.ui.main;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
//import com.adsUtils.adsloader.bannerads.BannerLoader;
//import com.adsUtils.adsloader.bannerads.BannerType;
//import com.adsUtils.analytics.AnalyticsTracker;
//import com.adsUtils.custom.CustomActivity;
import com.iptv.online.player.live.tv.smart.AdsPackage.CustomActivity;
import com.iptv.online.player.live.tv.smart.R;
import com.iptv.online.player.live.tv.smart.data.local.PlaylistStorageManager;
import com.iptv.online.player.live.tv.smart.data.model.PlaylistItem;
import com.iptv.online.player.live.tv.smart.ui.adapters.ChannelAdapter;
import com.iptv.online.player.live.tv.smart.ui.player.PlayerActivity;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
@SuppressWarnings("all")

/* loaded from: classes5.dex */
public class FavoriteActivity extends com.iptv.online.player.live.tv.smart.AdsPackage.CustomActivity implements ChannelAdapter.OnChannelClickListener {
    private ChannelAdapter channelAdapter;
    private EditText etSearchQuery;
    private List<PlaylistItem> favoriteList = new ArrayList();
    private ProgressBar favoriteProgressBar;
    private LinearLayout layoutEmptyState;
    private LinearLayout layoutSearch;
    private RecyclerView rvFavoriteChannels;
    private PlaylistStorageManager storageManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView tvFavoriteSubtitle;
    private TextView tvFavoriteTitle;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.adsUtils.custom.CustomActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_favorite);
        View findViewById = findViewById(R.id.llAdsView);
        if (findViewById != null && (findViewById instanceof ViewGroup)) {
//            BannerLoader.showBannerAds(this, (ViewGroup) findViewById, BannerType.FIXED_BANNER);
        }
        this.storageManager = new PlaylistStorageManager(this);
        initViews();
        setupRecyclerView();
        setupListeners();
        loadFavorites();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.adsUtils.custom.CustomActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        ChannelAdapter channelAdapter = this.channelAdapter;
        if (channelAdapter != null) {
            channelAdapter.updateAdConfig();
            this.channelAdapter.notifyDataSetChanged();
        }
        loadFavorites();
    }

    private void initViews() {
        this.tvFavoriteTitle = (TextView) findViewById(R.id.tvFavoriteTitle);
        this.tvFavoriteSubtitle = (TextView) findViewById(R.id.tvFavoriteSubtitle);
        this.rvFavoriteChannels = (RecyclerView) findViewById(R.id.rvFavoriteChannels);
        this.swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        this.layoutEmptyState = (LinearLayout) findViewById(R.id.layoutEmptyState);
        this.layoutSearch = (LinearLayout) findViewById(R.id.layoutSearch);
        this.etSearchQuery = (EditText) findViewById(R.id.etSearchQuery);
        this.favoriteProgressBar = (ProgressBar) findViewById(R.id.favoriteProgressBar);
        this.swipeRefreshLayout.setColorSchemeResources(R.color.star_gold, R.color.brand_blue);
    }

    private void setupRecyclerView() {
        setupLayoutManager();
        ChannelAdapter channelAdapter = new ChannelAdapter(this, this.storageManager, this);
        this.channelAdapter = channelAdapter;
        this.rvFavoriteChannels.setAdapter(channelAdapter);
    }

    private void setupLayoutManager() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() { // from class: com.iptv.online.player.live.tv.smart.ui.main.FavoriteActivity.1
            @Override // androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
            public int getSpanSize(int i) {
                return (FavoriteActivity.this.channelAdapter == null || !FavoriteActivity.this.channelAdapter.isAdPosition(i)) ? 1 : 2;
            }
        });
        this.rvFavoriteChannels.setLayoutManager(gridLayoutManager);
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.activity.ComponentActivity, android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        setupLayoutManager();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupListeners$0(View view) {
        setOnBackPressedClick();
    }

    private void setupListeners() {
        findViewById(R.id.btnFavoriteBack).setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.main.FavoriteActivity$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                FavoriteActivity.this.lambda$setupListeners$0(view);
            }
        });
        ((ImageButton) findViewById(R.id.btnSearchToggle)).setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.main.FavoriteActivity$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                FavoriteActivity.this.lambda$setupListeners$1(view);
            }
        });
        this.etSearchQuery.addTextChangedListener(new TextWatcher() { // from class: com.iptv.online.player.live.tv.smart.ui.main.FavoriteActivity.2
            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
            }

            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (FavoriteActivity.this.channelAdapter != null) {
                    FavoriteActivity.this.channelAdapter.getFilter().filter(charSequence);
                }
                if (charSequence == null || charSequence.length() < 2) {
                    return;
                }
//                AnalyticsTracker.logSearch("FavoriteActivity", charSequence.toString());
            }
        });
        this.etSearchQuery.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: com.iptv.online.player.live.tv.smart.ui.main.FavoriteActivity$$ExternalSyntheticLambda2
            @Override // android.widget.TextView.OnEditorActionListener
            public final boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean lambda$setupListeners$2;
                lambda$setupListeners$2 = FavoriteActivity.this.lambda$setupListeners$2(textView, i, keyEvent);
                return lambda$setupListeners$2;
            }
        });
        this.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() { // from class: com.iptv.online.player.live.tv.smart.ui.main.FavoriteActivity$$ExternalSyntheticLambda3
            @Override // androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
            public final void onRefresh() {
                FavoriteActivity.this.loadFavorites();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupListeners$1(View view) {
        int visibility = this.layoutSearch.getVisibility();
        LinearLayout linearLayout = this.layoutSearch;
        if (visibility == 0) {
            linearLayout.setVisibility(View.GONE);
            this.etSearchQuery.setText("");
            ChannelAdapter channelAdapter = this.channelAdapter;
            if (channelAdapter != null) {
                channelAdapter.getFilter().filter("");
                return;
            }
            return;
        }
        linearLayout.setVisibility(View.VISIBLE);
        this.etSearchQuery.requestFocus();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$setupListeners$2(TextView textView, int i, KeyEvent keyEvent) {
        if (i == 3) {
            ChannelAdapter channelAdapter = this.channelAdapter;
            if (channelAdapter != null) {
                channelAdapter.getFilter().filter(this.etSearchQuery.getText().toString());
                return true;
            }
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadFavorites() {
        this.favoriteProgressBar.setVisibility(View.VISIBLE);
        this.layoutEmptyState.setVisibility(View.GONE);
        List<PlaylistItem> cachedChannels = this.storageManager.getCachedChannels(this.storageManager.getActiveSource().getId());
        Set<String> favoriteIds = this.storageManager.getFavoriteIds();
        this.favoriteList.clear();
        for (PlaylistItem playlistItem : cachedChannels) {
            if (favoriteIds.contains(playlistItem.getId())) {
                this.favoriteList.add(playlistItem);
            }
        }
        this.favoriteProgressBar.setVisibility(View.GONE);
        this.swipeRefreshLayout.setRefreshing(false);
        this.channelAdapter.setChannels(this.favoriteList);
        updateSubtitleCount();
        checkEmptyState();
    }

    private void updateSubtitleCount() {
        int size = this.favoriteList.size();
        this.tvFavoriteSubtitle.setText(size + (size == 1 ? " Channel" : " Channels"));
    }

    private void checkEmptyState() {
        int itemCount = this.channelAdapter.getItemCount();
        LinearLayout linearLayout = this.layoutEmptyState;
        if (itemCount == 0) {
            linearLayout.setVisibility(View.VISIBLE);
            this.rvFavoriteChannels.setVisibility(View.GONE);
            return;
        }
        linearLayout.setVisibility(View.GONE);
        this.rvFavoriteChannels.setVisibility(View.VISIBLE);
    }

    @Override // com.iptv.online.player.live.tv.smart.ui.adapters.ChannelAdapter.OnChannelClickListener
    public void onChannelClick(PlaylistItem playlistItem, int i) {
        if (playlistItem == null) {
            return;
        }
//        AnalyticsTracker.logAction("FavoriteActivity", "click_favorite_channel", playlistItem.getName());
        PlayerActivity.setPlaybackList(this.channelAdapter.getCurrentChannels());
        Intent intent = new Intent(this, PlayerActivity.class);
        intent.putExtra(PlayerActivity.EXTRA_CHANNEL, playlistItem);
        intent.putExtra(PlayerActivity.EXTRA_CHANNEL_INDEX, i);
//        startAdsActivity(intent);
        startActivity(intent);
    }

    @Override // com.iptv.online.player.live.tv.smart.ui.adapters.ChannelAdapter.OnChannelClickListener
    public void onFavoriteToggle(PlaylistItem playlistItem, int i) {
        if (this.storageManager.isFavorite(playlistItem.getId())) {
            return;
        }
//        AnalyticsTracker.logFavorite("FavoriteActivity", playlistItem.getName(), false);
        this.favoriteList.remove(playlistItem);
        this.channelAdapter.setChannels(this.favoriteList);
        updateSubtitleCount();
        checkEmptyState();
    }
}

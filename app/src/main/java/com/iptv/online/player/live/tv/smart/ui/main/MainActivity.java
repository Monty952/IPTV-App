package com.iptv.online.player.live.tv.smart.ui.main;

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
import android.widget.Toast;
//import androidx.media3.extractor.text.ttml.TtmlNode;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
//import com.adsUtils.adsloader.bannerads.BannerLoader;
//import com.adsUtils.adsloader.bannerads.BannerType;
//import com.adsUtils.analytics.AnalyticsTracker;
//import com.adsUtils.custom.CustomActivity;
//import com.google.common.net.HttpHeaders;
import com.iptv.online.player.live.tv.smart.R;
import com.iptv.online.player.live.tv.smart.data.local.PlaylistStorageManager;
import com.iptv.online.player.live.tv.smart.data.model.Category;
import com.iptv.online.player.live.tv.smart.data.model.PlaylistItem;
import com.iptv.online.player.live.tv.smart.data.model.PlaylistSource;
import com.iptv.online.player.live.tv.smart.data.network.ApiClient;
import com.iptv.online.player.live.tv.smart.data.network.XtreamCodesApi;
import com.iptv.online.player.live.tv.smart.data.parser.M3UParser;
import com.iptv.online.player.live.tv.smart.ui.adapters.CategoryAdapter;
import com.iptv.online.player.live.tv.smart.ui.dialogs.AddPlaylistDialog;
import com.iptv.online.player.live.tv.smart.ui.dialogs.PinLockDialog;
import com.iptv.online.player.live.tv.smart.ui.vod.VodMainActivity;

import org.chromium.base.Callback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import okhttp3.Call;
//import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.http.HttpHeaders;



@SuppressWarnings("all")

/* loaded from: classes5.dex */
public class MainActivity extends com.iptv.online.player.live.tv.smart.AdsPackage.CustomActivity implements CategoryAdapter.OnCategoryClickListener {
    private CategoryAdapter categoryAdapter;
    private EditText etSearchQuery;
    private LinearLayout layoutEmptyState;
    private LinearLayout layoutSearch;
    private ProgressBar mainProgressBar;
    private RecyclerView rvCategories;
    private PlaylistStorageManager storageManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView tvActivePlaylistName;
    private List<PlaylistItem> allChannels = new ArrayList();
    private List<Category> allCategories = new ArrayList();
    private final Set<String> unlockedCategories = new HashSet();
    private String currentActiveSourceId = null;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.adsUtils.custom.CustomActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_main);
        View findViewById = findViewById(R.id.llAdsView);
        if (findViewById != null && (findViewById instanceof ViewGroup)) {
//            BannerLoader.showBannerAds(this, (ViewGroup) findViewById, BannerType.FIXED_BANNER);
        }
        PlaylistStorageManager playlistStorageManager = new PlaylistStorageManager(this);
        this.storageManager = playlistStorageManager;
        this.currentActiveSourceId = playlistStorageManager.getActiveSourceId();
        initViews();
        setupRecyclerView();
        setupListeners();
        loadPlaylist(false);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.adsUtils.custom.CustomActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        String activeSourceId = this.storageManager.getActiveSourceId();
        String str = this.currentActiveSourceId;
        if (str == null || !str.equals(activeSourceId)) {
            this.currentActiveSourceId = activeSourceId;
            loadPlaylist(false);
            return;
        }
        refreshSpecialCategories();
        CategoryAdapter categoryAdapter = this.categoryAdapter;
        if (categoryAdapter != null) {
            categoryAdapter.updateAdConfig();
            this.categoryAdapter.notifyDataSetChanged();
        }
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.activity.ComponentActivity, android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        setupLayoutManager();
    }

    private void initViews() {
        this.tvActivePlaylistName = (TextView) findViewById(R.id.tvActivePlaylistName);
        this.rvCategories = (RecyclerView) findViewById(R.id.rvCategories);
        this.swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        this.layoutEmptyState = (LinearLayout) findViewById(R.id.layoutEmptyState);
        this.layoutSearch = (LinearLayout) findViewById(R.id.layoutSearch);
        this.etSearchQuery = (EditText) findViewById(R.id.etSearchQuery);
        this.mainProgressBar = (ProgressBar) findViewById(R.id.mainProgressBar);
        this.swipeRefreshLayout.setColorSchemeResources(R.color.purple_accent, R.color.cyan_accent);
    }

    private void setupRecyclerView() {
        setupLayoutManager();
        CategoryAdapter categoryAdapter = new CategoryAdapter(this, this.storageManager, this);
        this.categoryAdapter = categoryAdapter;
        this.rvCategories.setAdapter(categoryAdapter);
    }

    private void setupLayoutManager() {
        int i = getResources().getConfiguration().orientation;
        int i2;
        if (getResources().getConfiguration().screenWidthDp < 600 && i != 2) {
            i2 = 1;
        } else {
            i2 = 2;
        }
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, i2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() { // from class: com.iptv.online.player.live.tv.smart.ui.main.MainActivity.1
            @Override // androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
            public int getSpanSize(int i3) {
                if (MainActivity.this.categoryAdapter == null || !MainActivity.this.categoryAdapter.isAdPosition(i3)) {
                    return 1;
                }
                return i2;
            }
        });
        this.rvCategories.setLayoutManager(gridLayoutManager);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupListeners$0() {
        loadPlaylist(true);
    }

    private void setupListeners() {
        this.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() { // from class: com.iptv.online.player.live.tv.smart.ui.main.MainActivity$$ExternalSyntheticLambda13
            @Override // androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
            public final void onRefresh() {
                MainActivity.this.lambda$setupListeners$0();
            }
        });
        findViewById(R.id.btnOpenVod).setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.main.MainActivity$$ExternalSyntheticLambda14
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MainActivity.this.lambda$setupListeners$1(view);
            }
        });
        findViewById(R.id.btnSearchToggle).setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.main.MainActivity$$ExternalSyntheticLambda15
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MainActivity.this.lambda$setupListeners$2(view);
            }
        });
        findViewById(R.id.btnClearSearch).setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.main.MainActivity$$ExternalSyntheticLambda16
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MainActivity.this.lambda$setupListeners$3(view);
            }
        });
        this.etSearchQuery.addTextChangedListener(new TextWatcher() { // from class: com.iptv.online.player.live.tv.smart.ui.main.MainActivity.2
            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
            }

            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (MainActivity.this.categoryAdapter != null) {
                    MainActivity.this.categoryAdapter.getFilter().filter(charSequence);
                }
            }
        });
        this.etSearchQuery.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: com.iptv.online.player.live.tv.smart.ui.main.MainActivity$$ExternalSyntheticLambda17
            @Override // android.widget.TextView.OnEditorActionListener
            public final boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean lambda$setupListeners$4;
                lambda$setupListeners$4 = MainActivity.this.lambda$setupListeners$4(textView, i, keyEvent);
                return lambda$setupListeners$4;
            }
        });
        findViewById(R.id.btnRefresh).setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.main.MainActivity$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MainActivity.this.lambda$setupListeners$5(view);
            }
        });
        findViewById(R.id.btnAddPlaylist).setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.main.MainActivity$$ExternalSyntheticLambda2
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MainActivity.this.lambda$setupListeners$7(view);
            }
        });
        findViewById(R.id.btnSettings).setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.main.MainActivity$$ExternalSyntheticLambda3
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MainActivity.this.lambda$setupListeners$8(view);
            }
        });
        findViewById(R.id.btnEmptyAdd).setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.main.MainActivity$$ExternalSyntheticLambda4
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MainActivity.this.lambda$setupListeners$10(view);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupListeners$1(View view) {
        startActivity(new Intent(this, VodMainActivity.class));
//        startAdsActivity(new Intent(this, VodMainActivity.class));
        new Intent(this, VodMainActivity.class);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupListeners$2(View view) {
        int visibility = this.layoutSearch.getVisibility();
        LinearLayout linearLayout = this.layoutSearch;
        if (visibility == 0) {
            linearLayout.setVisibility(View.GONE);
            this.etSearchQuery.setText("");
            return;
        }
        linearLayout.setVisibility(View.VISIBLE);
        this.etSearchQuery.requestFocus();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupListeners$3(View view) {
        this.etSearchQuery.setText("");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$setupListeners$4(TextView textView, int i, KeyEvent keyEvent) {
        if (i == 3) {
            String obj = this.etSearchQuery.getText().toString();
//            AnalyticsTracker.logSearch("MainActivity", obj);
            CategoryAdapter categoryAdapter = this.categoryAdapter;
            if (categoryAdapter != null) {
                categoryAdapter.getFilter().filter(obj);
                return true;
            }
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupListeners$5(View view) {
//        AnalyticsTracker.logPlaylistAction("MainActivity", "refresh_playlist", this.tvActivePlaylistName.getText().toString());
        loadPlaylist(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupListeners$7(View view) {
        new AddPlaylistDialog(this, this.storageManager, new AddPlaylistDialog.OnPlaylistAddedListener() { // from class: com.iptv.online.player.live.tv.smart.ui.main.MainActivity$$ExternalSyntheticLambda7
            @Override // com.iptv.online.player.live.tv.smart.ui.dialogs.AddPlaylistDialog.OnPlaylistAddedListener
            public final void onPlaylistAdded(PlaylistSource playlistSource) {
                MainActivity.this.lambda$setupListeners$6(playlistSource);
            }
        }).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupListeners$6(PlaylistSource playlistSource) {
        this.tvActivePlaylistName.setText(playlistSource.getName());
        loadPlaylist(true);
//        showTabScrollInterstitialAds();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupListeners$8(View view) {
        SettingMainActivity.setAvailableCategories(this.allCategories);

        startActivity(new Intent(this, SettingMainActivity.class));
//        startAdsActivity(new Intent(this, SettingMainActivity.class));
//        new Intent(this, SettingMainActivity.class);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupListeners$10(View view) {
        new AddPlaylistDialog(this, this.storageManager, new AddPlaylistDialog.OnPlaylistAddedListener() { // from class: com.iptv.online.player.live.tv.smart.ui.main.MainActivity$$ExternalSyntheticLambda12
            @Override // com.iptv.online.player.live.tv.smart.ui.dialogs.AddPlaylistDialog.OnPlaylistAddedListener
            public final void onPlaylistAdded(PlaylistSource playlistSource) {
                MainActivity.this.lambda$setupListeners$9(playlistSource);
            }
        }).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupListeners$9(PlaylistSource playlistSource) {
        this.tvActivePlaylistName.setText(playlistSource.getName());
        loadPlaylist(true);
    }

    private void loadPlaylist(boolean z) {
        PlaylistSource activeSource = this.storageManager.getActiveSource();
        this.tvActivePlaylistName.setText(activeSource.getName());
        if (!z) {
            List<PlaylistItem> cachedChannels = this.storageManager.getCachedChannels(activeSource.getId());
            if (!cachedChannels.isEmpty()) {
                this.mainProgressBar.setVisibility(View.GONE);
                this.swipeRefreshLayout.setRefreshing(false);
                populateCategories(cachedChannels);
                return;
            }
        }
        this.mainProgressBar.setVisibility(View.VISIBLE);
        this.layoutEmptyState.setVisibility(View.GONE);
        if (activeSource.getType() == PlaylistSource.SourceType.SINGLE_STREAM) {
            loadSingleStreamPlaylist(activeSource);
        } else if (activeSource.getType() == PlaylistSource.SourceType.LOCAL_FILE) {
            loadLocalFilePlaylist(activeSource);
        } else if (activeSource.getType() == PlaylistSource.SourceType.XTREAM_CODES) {
            loadXtreamPlaylist(activeSource);
        } else {
            loadM3UPlaylist(activeSource);
        }
//        showTabScrollInterstitialAds();
    }

    private void loadSingleStreamPlaylist(PlaylistSource playlistSource) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new PlaylistItem("stream_" + playlistSource.getId(), playlistSource.getName(), playlistSource.getUrl(), null, "Live Stream"));
        playlistSource.setChannelCount(1);
        this.storageManager.addOrUpdateSource(playlistSource);
        this.storageManager.saveCachedChannels(playlistSource.getId(), arrayList);
        this.mainProgressBar.setVisibility(View.GONE);
        this.swipeRefreshLayout.setRefreshing(false);
        populateCategories(arrayList);
    }

    private void loadLocalFilePlaylist(final PlaylistSource playlistSource) {
        new Thread(new Runnable() { // from class: com.iptv.online.player.live.tv.smart.ui.main.MainActivity$$ExternalSyntheticLambda11
            @Override // java.lang.Runnable
            public final void run() {
                MainActivity.this.lambda$loadLocalFilePlaylist$14(playlistSource);
            }
        }).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:14:0x0046 A[Catch: Exception -> 0x00a2, TryCatch #0 {Exception -> 0x00a2, blocks: (B:3:0x0002, B:5:0x000e, B:8:0x001b, B:10:0x0021, B:14:0x0046, B:16:0x0052, B:18:0x0058, B:20:0x0062, B:21:0x0090, B:23:0x0099, B:12:0x0034), top: B:28:0x0002 }] */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0099 A[Catch: Exception -> 0x00a2, TRY_LEAVE, TryCatch #0 {Exception -> 0x00a2, blocks: (B:3:0x0002, B:5:0x000e, B:8:0x001b, B:10:0x0021, B:14:0x0046, B:16:0x0052, B:18:0x0058, B:20:0x0062, B:21:0x0090, B:23:0x0099, B:12:0x0034), top: B:28:0x0002 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */

    // New Code
    /* Loads and parses a local playlist on a background thread. */
    public void lambda$loadLocalFilePlaylist$14(final PlaylistSource playlistSource) {
        try {
            String url = playlistSource.getUrl();
            String localFilePath = playlistSource.getLocalFilePath();
            java.io.InputStream inputStream = null;

            // Open the selected content/file URI.
            if (url != null &&
                    (url.startsWith("content://") || url.startsWith("file://"))) {
                inputStream = getContentResolver().openInputStream(
                        android.net.Uri.parse(url)
                );
            } else if (localFilePath != null && !localFilePath.isEmpty()) {
                inputStream = getContentResolver().openInputStream(
                        android.net.Uri.parse(localFilePath)
                );
            }

            // No file stream could be opened.
            if (inputStream == null) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        lambda$loadLocalFilePlaylist$12();
                    }
                });
                return;
            }

            // Parse the playlist and close the stream afterward.
            final M3UParser.ParseResult parseResult;

            try (java.io.InputStream stream = inputStream) {
                parseResult = M3UParser.parse(stream);
            }

            // If the file contains no channel entries, preserve
            // the original single-stream fallback behavior.
            if (parseResult.channels.isEmpty()
                    && url != null
                    && !url.isEmpty()) {
                PlaylistItem item = new PlaylistItem(
                        "ch_1_" + Math.abs(url.hashCode()),
                        playlistSource.getName(),
                        url,
                        null,
                        "Live Stream"
                );
                parseResult.channels.add(item);
            }

            // Update the UI after parsing has finished.
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    lambda$loadLocalFilePlaylist$11(
                            playlistSource,
                            parseResult
                    );
                }
            });
        } catch (final Exception e) {
            // Display the existing error message on the main thread.
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    lambda$loadLocalFilePlaylist$13(e);
                }
            });
        }
    }

    // Old Code
//    public /* synthetic */ void lambda$loadLocalFilePlaylist$14(final com.iptv.online.player.live.tv.smart.data.model.PlaylistSource r10) {
//        /*
//            r9 = this;
//            java.lang.String r0 = "ch_1_"
//            java.lang.String r1 = r10.getUrl()     // Catch: java.lang.Exception -> La2
//            java.lang.String r2 = "content://"
//            boolean r1 = r1.startsWith(r2)     // Catch: java.lang.Exception -> La2
//            if (r1 != 0) goto L34
//            java.lang.String r1 = r10.getUrl()     // Catch: java.lang.Exception -> La2
//            java.lang.String r2 = "file://"
//            boolean r1 = r1.startsWith(r2)     // Catch: java.lang.Exception -> La2
//            if (r1 == 0) goto L1b
//            goto L34
//        L1b:
//            java.lang.String r1 = r10.getLocalFilePath()     // Catch: java.lang.Exception -> La2
//            if (r1 == 0) goto L32
//            android.content.ContentResolver r1 = r9.getContentResolver()     // Catch: java.lang.Exception -> La2
//            java.lang.String r2 = r10.getLocalFilePath()     // Catch: java.lang.Exception -> La2
//            android.net.Uri r2 = android.net.Uri.parse(r2)     // Catch: java.lang.Exception -> La2
//            java.io.InputStream r1 = r1.openInputStream(r2)     // Catch: java.lang.Exception -> La2
//            goto L44
//        L32:
//            r1 = 0
//            goto L44
//        L34:
//            android.content.ContentResolver r1 = r9.getContentResolver()     // Catch: java.lang.Exception -> La2
//            java.lang.String r2 = r10.getUrl()     // Catch: java.lang.Exception -> La2
//            android.net.Uri r2 = android.net.Uri.parse(r2)     // Catch: java.lang.Exception -> La2
//            java.io.InputStream r1 = r1.openInputStream(r2)     // Catch: java.lang.Exception -> La2
//        L44:
//            if (r1 == 0) goto L99
//            com.iptv.online.player.live.tv.smart.data.parser.M3UParser$ParseResult r1 = com.iptv.online.player.live.tv.smart.data.parser.M3UParser.parse(r1)     // Catch: java.lang.Exception -> La2
//            java.util.List<com.iptv.online.player.live.tv.smart.data.model.PlaylistItem> r2 = r1.channels     // Catch: java.lang.Exception -> La2
//            boolean r2 = r2.isEmpty()     // Catch: java.lang.Exception -> La2
//            if (r2 == 0) goto L90
//            java.lang.String r2 = r10.getUrl()     // Catch: java.lang.Exception -> La2
//            if (r2 == 0) goto L90
//            java.lang.String r2 = r10.getUrl()     // Catch: java.lang.Exception -> La2
//            boolean r2 = r2.isEmpty()     // Catch: java.lang.Exception -> La2
//            if (r2 != 0) goto L90
//            com.iptv.online.player.live.tv.smart.data.model.PlaylistItem r3 = new com.iptv.online.player.live.tv.smart.data.model.PlaylistItem     // Catch: java.lang.Exception -> La2
//            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch: java.lang.Exception -> La2
//            r2.<init>(r0)     // Catch: java.lang.Exception -> La2
//            java.lang.String r0 = r10.getUrl()     // Catch: java.lang.Exception -> La2
//            int r0 = r0.hashCode()     // Catch: java.lang.Exception -> La2
//            int r0 = java.lang.Math.abs(r0)     // Catch: java.lang.Exception -> La2
//            java.lang.StringBuilder r0 = r2.append(r0)     // Catch: java.lang.Exception -> La2
//            java.lang.String r4 = r0.toString()     // Catch: java.lang.Exception -> La2
//            java.lang.String r5 = r10.getName()     // Catch: java.lang.Exception -> La2
//            java.lang.String r6 = r10.getUrl()     // Catch: java.lang.Exception -> La2
//            java.lang.String r8 = "Live Stream"
//            r7 = 0
//            r3.<init>(r4, r5, r6, r7, r8)     // Catch: java.lang.Exception -> La2
//            java.util.List<com.iptv.online.player.live.tv.smart.data.model.PlaylistItem> r0 = r1.channels     // Catch: java.lang.Exception -> La2
//            r0.add(r3)     // Catch: java.lang.Exception -> La2
//        L90:
//            com.iptv.online.player.live.tv.smart.ui.main.MainActivity$$ExternalSyntheticLambda0 r0 = new com.iptv.online.player.live.tv.smart.ui.main.MainActivity$$ExternalSyntheticLambda0     // Catch: java.lang.Exception -> La2
//            r0.<init>()     // Catch: java.lang.Exception -> La2
//            r9.runOnUiThread(r0)     // Catch: java.lang.Exception -> La2
//            return
//        L99:
//            com.iptv.online.player.live.tv.smart.ui.main.MainActivity$$ExternalSyntheticLambda9 r10 = new com.iptv.online.player.live.tv.smart.ui.main.MainActivity$$ExternalSyntheticLambda9     // Catch: java.lang.Exception -> La2
//            r10.<init>()     // Catch: java.lang.Exception -> La2
//            r9.runOnUiThread(r10)     // Catch: java.lang.Exception -> La2
//            return
//        La2:
//            r0 = move-exception
//            r10 = r0
//            com.iptv.online.player.live.tv.smart.ui.main.MainActivity$$ExternalSyntheticLambda10 r0 = new com.iptv.online.player.live.tv.smart.ui.main.MainActivity$$ExternalSyntheticLambda10
//            r0.<init>()
//            r9.runOnUiThread(r0)
//            return
//        */
//        throw new UnsupportedOperationException("Method not decompiled: com.iptv.online.player.live.tv.smart.ui.main.MainActivity.lambda$loadLocalFilePlaylist$14(com.iptv.online.player.live.tv.smart.data.model.PlaylistSource):void");
//    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadLocalFilePlaylist$11(PlaylistSource playlistSource, M3UParser.ParseResult parseResult) {
        this.mainProgressBar.setVisibility(View.GONE);
        this.swipeRefreshLayout.setRefreshing(false);
        this.storageManager.saveCachedChannels(playlistSource.getId(), parseResult.channels);
        playlistSource.setChannelCount(parseResult.channels.size());
        this.storageManager.addOrUpdateSource(playlistSource);
        populateCategories(parseResult.channels);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadLocalFilePlaylist$12() {
        this.mainProgressBar.setVisibility(View.GONE);
        this.swipeRefreshLayout.setRefreshing(false);
        checkEmptyState();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadLocalFilePlaylist$13(Exception exc) {
        this.mainProgressBar.setVisibility(View.GONE);
        this.swipeRefreshLayout.setRefreshing(false);
        Toast.makeText(this, getString(R.string.err_load_local_file, new Object[]{exc.getMessage()}), 0).show();
        checkEmptyState();
    }

    private void loadXtreamPlaylist(final PlaylistSource playlistSource) {
        XtreamCodesApi.authenticateAndLoad(playlistSource, new XtreamCodesApi.ApiCallback<M3UParser.ParseResult>() { // from class: com.iptv.online.player.live.tv.smart.ui.main.MainActivity.3
            @Override // com.iptv.online.player.live.tv.smart.data.network.XtreamCodesApi.ApiCallback
            public void onSuccess(M3UParser.ParseResult parseResult) {
                MainActivity.this.mainProgressBar.setVisibility(View.GONE);
                MainActivity.this.swipeRefreshLayout.setRefreshing(false);
                MainActivity.this.storageManager.saveCachedChannels(playlistSource.getId(), parseResult.channels);
                playlistSource.setChannelCount(parseResult.channels.size());
                MainActivity.this.storageManager.addOrUpdateSource(playlistSource);
                MainActivity.this.populateCategories(parseResult.channels);
            }

            @Override // com.iptv.online.player.live.tv.smart.data.network.XtreamCodesApi.ApiCallback
            public void onError(Exception exc) {
                MainActivity.this.mainProgressBar.setVisibility(View.GONE);
                MainActivity.this.swipeRefreshLayout.setRefreshing(false);
                MainActivity mainActivity = MainActivity.this;
                Toast.makeText(mainActivity, mainActivity.getString(R.string.err_connect_xtream, new Object[]{exc.getMessage()}), 1).show();
                MainActivity.this.checkEmptyState();
            }
        });
    }

    private void loadM3UPlaylist(PlaylistSource playlistSource) {
        String trim = playlistSource.getUrl() != null ? playlistSource.getUrl().trim() : "";
        if (!trim.startsWith("http://") && !trim.startsWith("https://")) {
            trim = "https://" + trim;
        }
        ApiClient.getClient().newCall(new Request.Builder().url(trim).header("User-Agent",
                ApiClient.DEFAULT_USER_AGENT).build()).enqueue((okhttp3.Callback) new AnonymousClass4(playlistSource));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.iptv.online.player.live.tv.smart.ui.main.MainActivity$4  reason: invalid class name */
    /* loaded from: classes5.dex */
    public class AnonymousClass4 implements okhttp3.Callback {
        final /* synthetic */ PlaylistSource val$source;

        AnonymousClass4(PlaylistSource playlistSource) {
            this.val$source = playlistSource;
        }

        // okhttp3.Callback
        public void onFailure(Call call, final IOException iOException) {
            MainActivity.this.runOnUiThread(new Runnable() { // from class: com.iptv.online.player.live.tv.smart.ui.main.MainActivity$4$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    MainActivity.AnonymousClass4.this.lambda$onFailure$0(iOException);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onFailure$0(IOException iOException) {
            MainActivity.this.mainProgressBar.setVisibility(View.GONE);
            MainActivity.this.swipeRefreshLayout.setRefreshing(false);
            MainActivity mainActivity = MainActivity.this;
            Toast.makeText(mainActivity, mainActivity.getString(R.string.err_download_playlist, new Object[]{iOException.getMessage()}), 1).show();
            MainActivity.this.checkEmptyState();
        }

        // okhttp3.Callback
        public void onResponse(Call call, final Response response) {
            try {
                if (response.isSuccessful() && response.body() != null) {
                    final M3UParser.ParseResult parse = M3UParser.parse(response.body().byteStream());
                    if (parse.channels.isEmpty() && !this.val$source.getUrl().isEmpty()) {
                        parse.channels.add(new PlaylistItem("ch_1_" + Math.abs(this.val$source.getUrl().hashCode()), this.val$source.getName(), this.val$source.getUrl(), null, "Live Stream"));
                    }
                    MainActivity mainActivity = MainActivity.this;
                    final PlaylistSource playlistSource = this.val$source;
                    mainActivity.runOnUiThread(new Runnable() { // from class: com.iptv.online.player.live.tv.smart.ui.main.MainActivity$4$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            MainActivity.AnonymousClass4.this.lambda$onResponse$1(playlistSource, parse);
                        }
                    });
                    return;
                }
                MainActivity.this.runOnUiThread(new Runnable() { // from class: com.iptv.online.player.live.tv.smart.ui.main.MainActivity$4$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        MainActivity.AnonymousClass4.this.lambda$onResponse$2(response);
                    }
                });
            } catch (Exception e) {
                MainActivity.this.runOnUiThread(new Runnable() { // from class: com.iptv.online.player.live.tv.smart.ui.main.MainActivity$4$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        MainActivity.AnonymousClass4.this.lambda$onResponse$3(e);
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onResponse$1(PlaylistSource playlistSource, M3UParser.ParseResult parseResult) {
            MainActivity.this.mainProgressBar.setVisibility(View.GONE);
            MainActivity.this.swipeRefreshLayout.setRefreshing(false);
            MainActivity.this.storageManager.saveCachedChannels(playlistSource.getId(), parseResult.channels);
            playlistSource.setChannelCount(parseResult.channels.size());
            MainActivity.this.storageManager.addOrUpdateSource(playlistSource);
            MainActivity.this.populateCategories(parseResult.channels);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onResponse$2(Response response) {
            MainActivity.this.mainProgressBar.setVisibility(View.GONE);
            MainActivity.this.swipeRefreshLayout.setRefreshing(false);
            MainActivity mainActivity = MainActivity.this;
            Toast.makeText(mainActivity, mainActivity.getString(R.string.err_server_code, new Object[]{Integer.valueOf(response.code())}), 0).show();
            MainActivity.this.checkEmptyState();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onResponse$3(Exception exc) {
            MainActivity.this.mainProgressBar.setVisibility(View.GONE);
            MainActivity.this.swipeRefreshLayout.setRefreshing(false);
            MainActivity mainActivity = MainActivity.this;
            Toast.makeText(mainActivity, mainActivity.getString(R.string.err_parse_m3u, new Object[]{exc.getMessage()}), 0).show();
            Log.d("TAG", "lambda$onResponse$3: " + mainActivity.getString(R.string.err_parse_m3u, new Object[]{exc.getMessage()}));
            MainActivity.this.checkEmptyState();
        }

      /*  @Override
        public void zza() {

        }*/
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void populateCategories(List<PlaylistItem> list) {
        this.allChannels = new ArrayList(list);
        HashMap hashMap = new HashMap();
        for (PlaylistItem playlistItem : list) {
            String groupTitle = playlistItem.getGroupTitle();
            if (!hashMap.containsKey(groupTitle)) {
                hashMap.put(groupTitle, new ArrayList());
            }
            ((List) hashMap.get(groupTitle)).add(playlistItem);
        }
        this.allCategories.clear();
        Category category = new Category("all", getString(R.string.all_channels));
        category.setChannels(this.allChannels);
        this.allCategories.add(category);
        List<PlaylistItem> favoriteChannels = getFavoriteChannels();
        Category category2 = new Category("favorites", "⭐ " + getString(R.string.favorites));
        category2.setChannels(favoriteChannels);
        this.allCategories.add(category2);
        List<PlaylistItem> recentChannels = this.storageManager.getRecentChannels();
        Category category3 = new Category("recent", "🕒 " + getString(R.string.recent));
        category3.setChannels(recentChannels);
        this.allCategories.add(category3);
        ArrayList<String> arrayList = new ArrayList(hashMap.keySet());
        Collections.sort(arrayList, String.CASE_INSENSITIVE_ORDER);
        for (String str : arrayList) {
            Category category4 = new Category(str, str);
            category4.setChannels((List) hashMap.get(str));
            this.allCategories.add(category4);
        }
        this.categoryAdapter.setCategories(this.allCategories);
        checkEmptyState();
    }

    private void refreshSpecialCategories() {
        for (Category category : this.allCategories) {
            if ("favorites".equalsIgnoreCase(category.getId())) {
                category.setChannels(getFavoriteChannels());
            } else if ("recent".equalsIgnoreCase(category.getId())) {
                category.setChannels(this.storageManager.getRecentChannels());
            }
        }
        CategoryAdapter categoryAdapter = this.categoryAdapter;
        if (categoryAdapter != null) {
            categoryAdapter.notifyDataSetChanged();
        }
    }

    private List<PlaylistItem> getFavoriteChannels() {
        ArrayList arrayList = new ArrayList();
        Set<String> favoriteIds = this.storageManager.getFavoriteIds();
        for (PlaylistItem playlistItem : this.allChannels) {
            if (favoriteIds.contains(playlistItem.getId())) {
                arrayList.add(playlistItem);
            }
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkEmptyState() {
        int itemCount = this.categoryAdapter.getItemCount();
        LinearLayout linearLayout = this.layoutEmptyState;
        if (itemCount == 0) {
            linearLayout.setVisibility(View.VISIBLE);
            this.rvCategories.setVisibility(View.GONE);
            return;
        }
        linearLayout.setVisibility(View.GONE);
        this.rvCategories.setVisibility(View.VISIBLE);
    }

    @Override // com.iptv.online.player.live.tv.smart.ui.adapters.CategoryAdapter.OnCategoryClickListener
    public void onCategoryClick(final Category category, int i) {
        if (category == null) {
            return;
        }
        if (this.storageManager.isCategoryLocked(category.getName()) && !this.unlockedCategories.contains(category.getName())) {
            new PinLockDialog(this, this.storageManager, PinLockDialog.Mode.UNLOCK, new PinLockDialog.OnPinVerifiedListener() { // from class: com.iptv.online.player.live.tv.smart.ui.main.MainActivity$$ExternalSyntheticLambda8
                @Override // com.iptv.online.player.live.tv.smart.ui.dialogs.PinLockDialog.OnPinVerifiedListener
                public final void onPinVerified() {
                    MainActivity.this.lambda$onCategoryClick$15(category);
                }
            }).show();
        } else {
            openCategoryChannels(category);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCategoryClick$15(Category category) {
        this.unlockedCategories.add(category.getName());
        openCategoryChannels(category);
    }

    @Override // com.iptv.online.player.live.tv.smart.ui.adapters.CategoryAdapter.OnCategoryClickListener
    public void onCategoryLongClick(final Category category, final int i) {
        if (category == null) {
            return;
        }
        String id = category.getId();
        if ("favorites".equalsIgnoreCase(id) || "recent".equalsIgnoreCase(id)) {
            return;
        }
        if (!this.storageManager.isParentalControlEnabled()) {
            Toast.makeText(this, R.string.err_set_pin_first, 0).show();
            new PinLockDialog(this, this.storageManager, PinLockDialog.Mode.SET_NEW_PIN,
                    new PinLockDialog.OnPinVerifiedListener() { // from class: com.iptv.online.player.live.tv.smart.ui.main.MainActivity$$ExternalSyntheticLambda5
                @Override // com.iptv.online.player.live.tv.smart.ui.dialogs.PinLockDialog.OnPinVerifiedListener
                public final void onPinVerified() {
                    MainActivity.this.lambda$onCategoryLongClick$16(category, i);
                }
            }).show();
        } else if (this.storageManager.isCategoryLocked(category.getName())) {
            new PinLockDialog(this, this.storageManager, PinLockDialog.Mode.UNLOCK,
                    new PinLockDialog.OnPinVerifiedListener() { // from class: com.iptv.online.player.live.tv.smart.ui.main.MainActivity$$ExternalSyntheticLambda6
                @Override // com.iptv.online.player.live.tv.smart.ui.dialogs.PinLockDialog.OnPinVerifiedListener
                public final void onPinVerified() {
                    MainActivity.this.lambda$onCategoryLongClick$17(category, i);
                }
            }).show();
        } else {
            this.storageManager.setCategoryLocked(category.getName(), true);
            this.unlockedCategories.remove(category.getName());
            CategoryAdapter categoryAdapter = this.categoryAdapter;
            if (categoryAdapter != null) {
                categoryAdapter.notifyItemChanged(i);
            }
//            Toast.makeText(this, getString(R.string.msg_category_locked, new Object[]{category.getName()}), 0).show();
            Toast.makeText(
                    this,
                    getString(Integer.parseInt("�� %1$s locked!"), category.getName()),
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCategoryLongClick$16(Category category, int i) {
        this.storageManager.setCategoryLocked(category.getName(), true);
        CategoryAdapter categoryAdapter = this.categoryAdapter;
        if (categoryAdapter != null) {
            categoryAdapter.notifyItemChanged(i);
        }
//        Toast.makeText(this, getString(R.string.msg_category_locked, new Object[]{category.getName()}), 0).show();
        Toast.makeText(
                this,
                getString(Integer.parseInt("�� %1$s locked!"), category.getName()),
                Toast.LENGTH_SHORT
        ).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCategoryLongClick$17(Category category, int i) {
        this.storageManager.setCategoryLocked(category.getName(), false);
        this.unlockedCategories.remove(category.getName());
        CategoryAdapter categoryAdapter = this.categoryAdapter;
        if (categoryAdapter != null) {
            categoryAdapter.notifyItemChanged(i);
        }
//        Toast.makeText(this, getString(R.string.msg_category_unlocked, new Object[]{category.getName()}), 0).show();
        Toast.makeText(
                this,
                getString(Integer.parseInt("�� %1$s unlocked"), category.getName()),
                Toast.LENGTH_SHORT
        ).show();
    }

    private void openCategoryChannels(Category category) {
        List<PlaylistItem> channels;
//        AnalyticsTracker.logAction("MainActivity", "select_category", category.getName());
        if ("favorites".equalsIgnoreCase(category.getId())) {
            channels = getFavoriteChannels();
        } else if ("recent".equalsIgnoreCase(category.getId())) {
            channels = this.storageManager.getRecentChannels();
        } else if ("all".equalsIgnoreCase(category.getId())) {
            channels = this.allChannels;
        } else {
            channels = category.getChannels();
        }
        ChannelActivity.setCategoryChannels(channels);
        Intent intent = new Intent(this, ChannelActivity.class);
        intent.putExtra(ChannelActivity.EXTRA_CATEGORY_ID, category.getId());
        intent.putExtra(ChannelActivity.EXTRA_CATEGORY_NAME, category.getName());
        startActivity(intent);
//        startAdsActivity(intent);
    }

}

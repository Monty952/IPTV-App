package com.iptv.online.player.live.tv.smart.ui.home;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
//import com.adsUtils.adsloader.nativeads.NativeLoader;
//import com.adsUtils.adsloader.nativeads.NativeType;
//import com.adsUtils.analytics.AnalyticsTracker;
//import com.adsUtils.custom.CustomActivity;
import com.google.android.material.button.MaterialButton;
import com.iptv.online.player.live.tv.smart.AdsPackage.custom.CustomActivity;
import com.iptv.online.player.live.tv.smart.R;
import com.iptv.online.player.live.tv.smart.data.local.PlaylistStorageManager;
import com.iptv.online.player.live.tv.smart.data.model.PlaylistItem;
import com.iptv.online.player.live.tv.smart.data.model.PlaylistSource;
import com.iptv.online.player.live.tv.smart.data.parser.M3UParser;
import com.iptv.online.player.live.tv.smart.ui.adapters.HomePlaylistAdapter;
import com.iptv.online.player.live.tv.smart.ui.cast.CastToTvActivity;
import com.iptv.online.player.live.tv.smart.ui.dialogs.AddPlaylistDialog;
import com.iptv.online.player.live.tv.smart.ui.dialogs.DeletePlaylistDialog;
import com.iptv.online.player.live.tv.smart.ui.help.HowToUseActivity;
import com.iptv.online.player.live.tv.smart.ui.home.HomeActivity;
import com.iptv.online.player.live.tv.smart.ui.main.MainActivity;
import com.iptv.online.player.live.tv.smart.ui.player.PlayerActivity;
import com.iptv.online.player.live.tv.smart.ui.settings.HomeSettingActivity;
import com.iptv.online.player.live.tv.smart.ui.vod.VodMainActivity;
//import com.onesignal.Continue;
//import com.onesignal.OneSignal;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
@SuppressWarnings("all")

/* loaded from: classes5.dex */
public class HomeActivity extends CustomActivity {
    private HomePlaylistAdapter adapter;
    private ImageButton btnPlusFab;
    private View dimBackdrop;
    private ProgressBar homeProgressBar;
    private View layoutActionMenu;
    private View layoutEmptyPlaylists;
    private RecyclerView rvHomePlaylists;
    private PlaylistStorageManager storageManager;
    private TextView tabAll;
    private TextView tabDevice;
    private TextView tabFile;
    private TextView tabStream;
    private TextView tabUrl;
    private TabFilter currentTab = TabFilter.ALL;
    private boolean isMenuOpen = false;
    // NEW: Views and state for toggling between Home and Channels fragment
    private View layoutHomeContent;
    private View fragmentContainer;
    private android.widget.ImageView ivNavHomeIcon;
    private TextView tvNavHomeLabel;
    private android.widget.ImageView ivNavChannelsIcon;
    private TextView tvNavChannelsLabel;
    private com.iptv.online.player.live.tv.smart.ui.main.ChannelsFragment channelsFragment;
    private boolean isChannelsTab = false;
    private final ActivityResultLauncher<String> filePickerLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback() { // from class: com.iptv.online.player.live.tv.smart.ui.home.HomeActivity$$ExternalSyntheticLambda4
        @Override // androidx.activity.result.ActivityResultCallback
        public final void onActivityResult(Object obj) {
            HomeActivity.this.handlePickedFileUri((Uri) obj);
        }
    });
    private final ActivityResultLauncher<String> notificationPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback() { // from class: com.iptv.online.player.live.tv.smart.ui.home.HomeActivity$$ExternalSyntheticLambda5
        @Override // androidx.activity.result.ActivityResultCallback
        public final void onActivityResult(Object obj) {
            Boolean bool = (Boolean) obj;
//            OneSignal.getNotifications().requestPermission(true, Continue.none());
        }
    });

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public enum TabFilter {
        ALL,
        URL,
        FILE,
        STREAM,
        DEVICE
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.adsUtils.custom.CustomActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_home);
//        NativeLoader.showNativeAds(this, (LinearLayout) findViewById(R.id.llAdsView), NativeType.SMALL_NATIVE);
        PlaylistStorageManager playlistStorageManager = new PlaylistStorageManager(this);
        this.storageManager = playlistStorageManager;
        playlistStorageManager.setHasOpenedBefore(true);
        requestNotificationPermission();
        initViews();
        setupTabs();
        setupBottomNav();
        setupActionMenu();
        setupRecyclerView();
        setupBackPress();
    }

    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= 33) {
            if (ContextCompat.checkSelfPermission(this, "android.permission.POST_NOTIFICATIONS") != 0) {
                this.notificationPermissionLauncher.launch("android.permission.POST_NOTIFICATIONS");
                return;
            }
            return;
        }
        try {
//            OneSignal.getNotifications().requestPermission(true, Continue.none());
        } catch (Exception unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.adsUtils.custom.CustomActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        loadPlaylistsForCurrentTab();
    }

    private void initViews() {
        this.tabAll = (TextView) findViewById(R.id.tabAll);
        this.tabUrl = (TextView) findViewById(R.id.tabUrl);
        this.tabFile = (TextView) findViewById(R.id.tabFile);
        this.tabStream = (TextView) findViewById(R.id.tabStream);
        this.tabDevice = (TextView) findViewById(R.id.tabDevice);
        this.layoutEmptyPlaylists = findViewById(R.id.layoutEmptyPlaylists);
        this.rvHomePlaylists = (RecyclerView) findViewById(R.id.rvHomePlaylists);
        this.homeProgressBar = (ProgressBar) findViewById(R.id.homeProgressBar);
        this.dimBackdrop = findViewById(R.id.dimBackdrop);
        this.layoutActionMenu = findViewById(R.id.layoutActionMenu);
        this.btnPlusFab = (ImageButton) findViewById(R.id.btnPlusFab);
        // NEW: Initialize Home layout container, fragment container, and bottom nav widgets
        this.layoutHomeContent = findViewById(R.id.layoutHomeContent);
        this.fragmentContainer = findViewById(R.id.fragmentContainer);
        this.ivNavHomeIcon = (android.widget.ImageView) findViewById(R.id.ivNavHomeIcon);
        this.tvNavHomeLabel = (TextView) findViewById(R.id.tvNavHomeLabel);
        this.ivNavChannelsIcon = (android.widget.ImageView) findViewById(R.id.ivNavChannelsIcon);
        this.tvNavChannelsLabel = (TextView) findViewById(R.id.tvNavChannelsLabel);
        findViewById(R.id.btnHomeVod).setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.home.HomeActivity$$ExternalSyntheticLambda23
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                HomeActivity.this.lambda$initViews$1(view);
                Log.d("TAG", "onClick: btnHomeVod");

            }
        });
        findViewById(R.id.btnCast).setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.home.HomeActivity$$ExternalSyntheticLambda24
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                HomeActivity.this.lambda$initViews$2(view);
            }
        });
        findViewById(R.id.btnHelp).setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.home.HomeActivity$$ExternalSyntheticLambda25
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                HomeActivity.this.lambda$initViews$3(view);
            }
        });
        findViewById(R.id.btnHomeSettings).setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.home.HomeActivity$$ExternalSyntheticLambda26
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                HomeActivity.this.lambda$initViews$4(view);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initViews$1(View view) {
        startActivity(new Intent(this, VodMainActivity.class));
//        startAdsActivity(new Intent(this, VodMainActivity.class));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initViews$2(View view) {
        startActivity(new Intent(this, CastToTvActivity.class));
//        startAdsActivity(new Intent(this, CastToTvActivity.class));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initViews$3(View view) {
        startActivity(new Intent(this, HowToUseActivity.class));
//        startAdsActivity(new Intent(this, HowToUseActivity.class));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initViews$4(View view) {
        showSettingsDialog();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupTabs$5(View view) {
        switchTab(TabFilter.ALL);
    }

    private void setupTabs() {
        this.tabAll.setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.home.HomeActivity$$ExternalSyntheticLambda6
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                HomeActivity.this.lambda$setupTabs$5(view);
            }
        });
        this.tabUrl.setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.home.HomeActivity$$ExternalSyntheticLambda7
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                HomeActivity.this.lambda$setupTabs$6(view);
            }
        });
        this.tabFile.setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.home.HomeActivity$$ExternalSyntheticLambda8
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                HomeActivity.this.lambda$setupTabs$7(view);
            }
        });
        this.tabStream.setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.home.HomeActivity$$ExternalSyntheticLambda9
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                HomeActivity.this.lambda$setupTabs$8(view);
            }
        });
        this.tabDevice.setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.home.HomeActivity$$ExternalSyntheticLambda10
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                HomeActivity.this.lambda$setupTabs$9(view);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupTabs$6(View view) {
        switchTab(TabFilter.URL);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupTabs$7(View view) {
        switchTab(TabFilter.FILE);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupTabs$8(View view) {
        switchTab(TabFilter.STREAM);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupTabs$9(View view) {
        switchTab(TabFilter.DEVICE);
    }

    private void switchTab(TabFilter tabFilter) {
        TextView textView;
        this.currentTab = tabFilter;
//        AnalyticsTracker.logAction("HomeActivity", "switch_tab", tabFilter.name());
        Log.d("HomeActivity", "switchTab: " + tabFilter.name());
        int color = ContextCompat.getColor(this, R.color.text_secondary);
        int color2 = ContextCompat.getColor(this, R.color.white);
        this.tabAll.setBackground(null);
        this.tabAll.setTextColor(color);
        this.tabUrl.setBackground(null);
        this.tabUrl.setTextColor(color);
        this.tabFile.setBackground(null);
        this.tabFile.setTextColor(color);
        this.tabStream.setBackground(null);
        this.tabStream.setTextColor(color);
        this.tabDevice.setBackground(null);
        this.tabDevice.setTextColor(color);
        if (tabFilter == TabFilter.ALL) {
            textView = this.tabAll;
        } else if (tabFilter == TabFilter.URL) {
            textView = this.tabUrl;
        } else if (tabFilter == TabFilter.FILE) {
            textView = this.tabFile;
        } else if (tabFilter == TabFilter.STREAM) {
            textView = this.tabStream;
        } else {
            textView = this.tabDevice;
        }
        textView.setBackgroundResource(R.drawable.bg_segmented_tab_selected);
        textView.setTextColor(color2);
        loadPlaylistsForCurrentTab();
        showTabScrollInterstitialAds();
    }

    private void setupRecyclerView() {
        int i = getResources().getConfiguration().orientation;
        int i2 = 2;
        if (getResources().getConfiguration().screenWidthDp < 600 && i != 2) {
            i2 = 1;
        }
        RecyclerView recyclerView = this.rvHomePlaylists;
        if (i2 > 1) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, i2));
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
        HomePlaylistAdapter homePlaylistAdapter = new HomePlaylistAdapter(this, new AnonymousClass1());
        this.adapter = homePlaylistAdapter;
        this.rvHomePlaylists.setAdapter(homePlaylistAdapter);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.iptv.online.player.live.tv.smart.ui.home.HomeActivity$1  reason: invalid class name */
    /* loaded from: classes5.dex */
    public class AnonymousClass1 implements HomePlaylistAdapter.OnHomePlaylistClickListener {
        AnonymousClass1() {
        }

        @Override // com.iptv.online.player.live.tv.smart.ui.adapters.HomePlaylistAdapter.OnHomePlaylistClickListener
        public void onPlaylistClick(PlaylistSource playlistSource) {
//            AnalyticsTracker.logPlaylistAction("HomeActivity", "select_playlist", playlistSource.getName());
            Log.d("HomeActivity", "select_playlist: " + playlistSource.getName());
            HomeActivity.this.storageManager.setActiveSourceId(playlistSource.getId());
            HomeActivity.this.showChannelsTab(); // CHANGED: Switch to Channels fragment instead of opening MainActivity
        }

        @Override // com.iptv.online.player.live.tv.smart.ui.adapters.HomePlaylistAdapter.OnHomePlaylistClickListener
        public void onPlaylistDelete(final PlaylistSource playlistSource) {
//            AnalyticsTracker.logPlaylistAction("HomeActivity", "delete_playlist", playlistSource.getName());
            Log.d("HomeActivity", "delete_playlist: " + playlistSource.getName());

            new DeletePlaylistDialog(HomeActivity.this, playlistSource, new DeletePlaylistDialog.OnDeleteConfirmListener() { // from class: com.iptv.online.player.live.tv.smart.ui.home.HomeActivity$1$$ExternalSyntheticLambda0
                @Override // com.iptv.online.player.live.tv.smart.ui.dialogs.DeletePlaylistDialog.OnDeleteConfirmListener
                public final void onDeleteConfirmed() {
                    HomeActivity.AnonymousClass1.this.lambda$onPlaylistDelete$0(playlistSource);
                }
            }).show();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onPlaylistDelete$0(PlaylistSource playlistSource) {
            HomeActivity.this.storageManager.deleteSource(playlistSource.getId());
            HomeActivity.this.loadPlaylistsForCurrentTab();
            HomeActivity.this.showTabScrollInterstitialAds();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadPlaylistsForCurrentTab() {
        List<PlaylistSource> playlistSources = this.storageManager.getPlaylistSources();
        ArrayList arrayList = new ArrayList();
        for (PlaylistSource playlistSource : playlistSources) {
            if (this.currentTab == TabFilter.ALL) {
                arrayList.add(playlistSource);
            } else if (this.currentTab == TabFilter.URL && (playlistSource.getType() == PlaylistSource.SourceType.M3U_URL || playlistSource.getType() == PlaylistSource.SourceType.DEMO)) {
                arrayList.add(playlistSource);
            } else if (this.currentTab == TabFilter.FILE && playlistSource.getType() == PlaylistSource.SourceType.LOCAL_FILE) {
                arrayList.add(playlistSource);
            } else if (this.currentTab == TabFilter.STREAM && playlistSource.getType() == PlaylistSource.SourceType.SINGLE_STREAM) {
                arrayList.add(playlistSource);
            } else if (this.currentTab == TabFilter.DEVICE && playlistSource.getType() == PlaylistSource.SourceType.XTREAM_CODES) {
                arrayList.add(playlistSource);
            }
        }
        boolean isEmpty = arrayList.isEmpty();
        View view = this.layoutEmptyPlaylists;
        if (isEmpty) {
            view.setVisibility(View.VISIBLE);
            this.rvHomePlaylists.setVisibility(View.GONE);
            return;
        }
        view.setVisibility(View.GONE);
        this.rvHomePlaylists.setVisibility(View.VISIBLE);
        this.adapter.setPlaylists(arrayList);
    }

    private void setupBottomNav() {
        findViewById(R.id.navTabHome).setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.home.HomeActivity$$ExternalSyntheticLambda27
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                HomeActivity.this.lambda$setupBottomNav$10(view);
            }
        });
        findViewById(R.id.navTabChannels).setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.home.HomeActivity$$ExternalSyntheticLambda28
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                HomeActivity.this.lambda$setupBottomNav$11(view);
            }
        });
        this.btnPlusFab.setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.home.HomeActivity$$ExternalSyntheticLambda29
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                HomeActivity.this.lambda$setupBottomNav$12(view);
            }
        });
        this.dimBackdrop.setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.home.HomeActivity$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                HomeActivity.this.lambda$setupBottomNav$13(view);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupBottomNav$10(View view) {
        // CHANGED: Switch to Home tab
        showHomeTab();
    }


    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupBottomNav$11(View view) {
        if (this.isMenuOpen) {
            closeActionMenu();
        }
        openMainActivity();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupBottomNav$12(View view) {
        if (this.isMenuOpen) {
            closeActionMenu();
        } else {
            openActionMenu();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupBottomNav$13(View view) {
        closeActionMenu();
    }

    private void openActionMenu() {
        this.isMenuOpen = true;
        this.btnPlusFab.setImageResource(R.drawable.ic_close_white);
        this.btnPlusFab.animate().rotation(90.0f).setDuration(200L).start();
        this.dimBackdrop.setVisibility(View.VISIBLE);
        this.dimBackdrop.setAlpha(0.0f);
        this.dimBackdrop.animate().alpha(1.0f).setDuration(200L).start();
        this.layoutActionMenu.setVisibility(View.VISIBLE);
        this.layoutActionMenu.setTranslationY(200.0f);
        this.layoutActionMenu.setAlpha(0.0f);
        this.layoutActionMenu.animate().translationY(0.0f).alpha(1.0f).setDuration(250L).start();
    }

    private void closeActionMenu() {
        this.isMenuOpen = false;
        this.btnPlusFab.setImageResource(R.drawable.ic_add);
        this.btnPlusFab.animate().rotation(0.0f).setDuration(200L).start();
        this.dimBackdrop.animate().alpha(0.0f).setDuration(180L).withEndAction(new Runnable() { // from class: com.iptv.online.player.live.tv.smart.ui.home.HomeActivity$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                HomeActivity.this.lambda$closeActionMenu$14();
            }
        }).start();
        this.layoutActionMenu.animate().translationY(200.0f).alpha(0.0f).setDuration(180L).withEndAction(new Runnable() { // from class: com.iptv.online.player.live.tv.smart.ui.home.HomeActivity$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                HomeActivity.this.lambda$closeActionMenu$15();
            }
        }).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$closeActionMenu$14() {
        this.dimBackdrop.setVisibility(View.GONE);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$closeActionMenu$15() {
        this.layoutActionMenu.setVisibility(View.GONE);
    }

    private void setupActionMenu() {
        findViewById(R.id.btnActionImportUrl).setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.home.HomeActivity$$ExternalSyntheticLambda16
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                HomeActivity.this.lambda$setupActionMenu$17(view);
            }
        });
        findViewById(R.id.btnActionUploadFile).setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.home.HomeActivity$$ExternalSyntheticLambda17
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                HomeActivity.this.lambda$setupActionMenu$18(view);
            }
        });
        findViewById(R.id.btnActionSingleStream).setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.home.HomeActivity$$ExternalSyntheticLambda18
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                HomeActivity.this.lambda$setupActionMenu$19(view);
            }
        });
        findViewById(R.id.btnActionLoadDevice).setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.home.HomeActivity$$ExternalSyntheticLambda19
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                HomeActivity.this.lambda$setupActionMenu$20(view);
            }
        });
        findViewById(R.id.btnActionXtreamApi).setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.home.HomeActivity$$ExternalSyntheticLambda20
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                HomeActivity.this.lambda$setupActionMenu$22(view);
            }
        });
        findViewById(R.id.btnActionVod).setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.home.HomeActivity$$ExternalSyntheticLambda21
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                HomeActivity.this.lambda$setupActionMenu$23(view);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupActionMenu$17(View view) {
        closeActionMenu();
        new AddPlaylistDialog(this, this.storageManager, new AddPlaylistDialog.OnPlaylistAddedListener() { // from class: com.iptv.online.player.live.tv.smart.ui.home.HomeActivity$$ExternalSyntheticLambda12
            @Override // com.iptv.online.player.live.tv.smart.ui.dialogs.AddPlaylistDialog.OnPlaylistAddedListener
            public final void onPlaylistAdded(PlaylistSource playlistSource) {
                HomeActivity.this.lambda$setupActionMenu$16(playlistSource);
            }
        }).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupActionMenu$16(PlaylistSource playlistSource) {
        loadPlaylistsForCurrentTab();
        openMainActivity();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupActionMenu$18(View view) {
        closeActionMenu();
        try {
            this.filePickerLauncher.launch("*/*");
        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.err_open_file_picker, new Object[]{e.getMessage()}), 0).show();
        }
        showTabScrollInterstitialAds();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupActionMenu$19(View view) {
        closeActionMenu();
        showSingleStreamDialog();
        showTabScrollInterstitialAds();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupActionMenu$20(View view) {
        closeActionMenu();
        try {
            this.filePickerLauncher.launch("*/*");
        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.err_open_file_picker, new Object[]{e.getMessage()}), 0).show();
        }
        showTabScrollInterstitialAds();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupActionMenu$22(View view) {
        closeActionMenu();
        new AddPlaylistDialog(this, this.storageManager, new AddPlaylistDialog.OnPlaylistAddedListener() { // from class: com.iptv.online.player.live.tv.smart.ui.home.HomeActivity$$ExternalSyntheticLambda15
            @Override // com.iptv.online.player.live.tv.smart.ui.dialogs.AddPlaylistDialog.OnPlaylistAddedListener
            public final void onPlaylistAdded(PlaylistSource playlistSource) {
                HomeActivity.this.lambda$setupActionMenu$21(playlistSource);
            }
        }).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupActionMenu$21(PlaylistSource playlistSource) {
        loadPlaylistsForCurrentTab();
        openMainActivity();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupActionMenu$23(View view) {
        closeActionMenu();
        startActivity(new Intent(this, VodMainActivity.class));
//        startAdsActivity(new Intent(this, VodMainActivity.class));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handlePickedFileUri(final Uri uri) {
        if (uri == null) {
            return;
        }
        this.homeProgressBar.setVisibility(View.VISIBLE);
        new Thread(new Runnable() { // from class: com.iptv.online.player.live.tv.smart.ui.home.HomeActivity$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                HomeActivity.this.lambda$handlePickedFileUri$26(uri);
            }
        }).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handlePickedFileUri$26(Uri uri) {
        Log.d("TAG", "lambda$handlePickedFileUri$26: " );
        try {
            InputStream openInputStream = getContentResolver().openInputStream(uri);
            if (openInputStream != null) {
                final M3UParser.ParseResult parse = M3UParser.parse(openInputStream);
                Log.d("TAG", "lambda$handlePickedFileUri$26: " );
                String str = "Local M3U Playlist";
                String lastPathSegment = uri.getLastPathSegment();
                if (lastPathSegment != null) {
                    int lastIndexOf = lastPathSegment.lastIndexOf(47);
                    if (lastIndexOf != -1) {
                        lastPathSegment = lastPathSegment.substring(lastIndexOf + 1);
                    }
                    if (!lastPathSegment.isEmpty()) {
                        str = lastPathSegment;
                    }
                }
                PlaylistSource createLocalFileSource = PlaylistSource.createLocalFileSource(str, uri.toString());
                createLocalFileSource.setChannelCount(parse.channels.size());
                this.storageManager.saveCachedChannels(parse.channels);
                this.storageManager.addOrUpdateSource(createLocalFileSource);
                this.storageManager.setActiveSourceId(createLocalFileSource.getId());
                runOnUiThread(new Runnable() { // from class: com.iptv.online.player.live.tv.smart.ui.home.HomeActivity$$ExternalSyntheticLambda11
                    @Override // java.lang.Runnable
                    public final void run() {
                        HomeActivity.this.lambda$handlePickedFileUri$24(parse);
                    }
                });
            }
        } catch (Exception e) {
            runOnUiThread(new Runnable() { // from class: com.iptv.online.player.live.tv.smart.ui.home.HomeActivity$$ExternalSyntheticLambda22
                @Override // java.lang.Runnable
                public final void run() {
                    HomeActivity.this.lambda$handlePickedFileUri$25(e);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handlePickedFileUri$24(M3UParser.ParseResult parseResult) {
        this.homeProgressBar.setVisibility(View.GONE);
        // New Code
        int channelCount = parseResult.channels.size();
        Toast.makeText(
                this,
                "Loaded " + channelCount + " channels",
                Toast.LENGTH_SHORT
        ).show();

        // Old Code
//        Toast.makeText(this, getString(Integer.parseInt("Loaded %1$d channels"), new Object[]{Integer.valueOf(parseResult.channels.size())}), 0).show();
        loadPlaylistsForCurrentTab();
        openMainActivity();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handlePickedFileUri$25(Exception exc) {
        this.homeProgressBar.setVisibility(View.GONE);
        Toast.makeText(this, getString(R.string.err_parse_m3u, new Object[]{exc.getMessage()}), 1).show();
    }

    private void showSingleStreamDialog() {
        final Dialog dialog = new Dialog(this, R.style.CustomDialogTheme);
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.dialog_single_stream);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(-1, -2);
        }
        final EditText editText = (EditText) dialog.findViewById(R.id.etSingleStreamName);
        final EditText editText2 = (EditText) dialog.findViewById(R.id.etSingleStreamUrl);
        ((MaterialButton) dialog.findViewById(R.id.btnCancelSingleStream)).setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.home.HomeActivity$$ExternalSyntheticLambda13
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                dialog.dismiss();
            }
        });
        ((MaterialButton) dialog.findViewById(R.id.btnPlaySingleStream)).setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.home.HomeActivity$$ExternalSyntheticLambda14
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                HomeActivity.this.lambda$showSingleStreamDialog$28(editText2, editText, dialog, view);
            }
        });
        dialog.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showSingleStreamDialog$28(EditText editText, EditText editText2, Dialog dialog, View view) {
        String trim = editText.getText().toString().trim();
        String trim2 = editText2.getText().toString().trim();
        if (trim.isEmpty()) {
            Toast.makeText(this, getString(R.string.err_enter_stream_url), 0).show();
            return;
        }
        if (trim2.isEmpty()) {
            trim2 = "Live Stream";
        }
        String str = trim2;
//        AnalyticsTracker.logPlaylistAction("HomeActivity", "play_single_stream", str);
        PlaylistItem playlistItem = new PlaylistItem("single_" + System.currentTimeMillis(), str, trim, null, "Live Stream");
        this.storageManager.addOrUpdateSource(PlaylistSource.createSingleStreamSource(str, trim));
        this.storageManager.addRecentChannel(playlistItem);
        dialog.dismiss();
        PlayerActivity.setPlaybackList(Collections.singletonList(playlistItem));
        Intent intent = new Intent(this, PlayerActivity.class);
        intent.putExtra(PlayerActivity.EXTRA_CHANNEL, playlistItem);
        intent.putExtra(PlayerActivity.EXTRA_CHANNEL_INDEX, 0);
        startActivity(intent);
//        startAdsActivity(intent);
    }

    private void showHelpDialog() {
        new AlertDialog.Builder(this, R.style.CustomDialogTheme).setTitle(R.string.iptv_help_formats).setMessage(R.string.iptv_help_message).setPositiveButton(R.string.btn_got_it, (DialogInterface.OnClickListener) null).show();
    }

    private void showSettingsDialog() {
        startActivity(new Intent(this, HomeSettingActivity.class));
//        startAdsActivity(new Intent(this, HomeSettingActivity.class));
    }

    /* JADX INFO: Access modifiers changed from: private */
    // NEW: Switch to Home tab showing playlists
    public void showHomeTab() {

        if (this.isMenuOpen) {
            closeActionMenu();
        }
        this.isChannelsTab = false;
        if (this.layoutHomeContent != null) {
            this.layoutHomeContent.setVisibility(View.VISIBLE);
        }
        if (this.fragmentContainer != null) {
            this.fragmentContainer.setVisibility(View.GONE);
        }
        int purple = ContextCompat.getColor(this, R.color.purple_500);
        int secondary = ContextCompat.getColor(this, R.color.text_secondary);
        if (this.ivNavHomeIcon != null) {
            this.ivNavHomeIcon.setColorFilter(purple);
        }
        if (this.tvNavHomeLabel != null) {
            this.tvNavHomeLabel.setTextColor(purple);
        }
        if (this.ivNavChannelsIcon != null) {
            this.ivNavChannelsIcon.setColorFilter(secondary);
        }
        if (this.tvNavChannelsLabel != null) {
            this.tvNavChannelsLabel.setTextColor(secondary);
        }
    }

    // NEW: Switch to Channels fragment showing active playlist categories/channels
    public void showChannelsTab() {
        if (this.isMenuOpen) {
            closeActionMenu();
        }
        this.isChannelsTab = true;
        if (this.layoutHomeContent != null) {
            this.layoutHomeContent.setVisibility(View.GONE);
        }
        if (this.fragmentContainer != null) {
            this.fragmentContainer.setVisibility(View.VISIBLE);
        }
        int purple = ContextCompat.getColor(this, R.color.purple_500);
        int secondary = ContextCompat.getColor(this, R.color.text_secondary);
        if (this.ivNavHomeIcon != null) {
            this.ivNavHomeIcon.setColorFilter(secondary);
        }
        if (this.tvNavHomeLabel != null) {
            this.tvNavHomeLabel.setTextColor(secondary);
        }
        if (this.ivNavChannelsIcon != null) {
            this.ivNavChannelsIcon.setColorFilter(purple);
        }
        if (this.tvNavChannelsLabel != null) {
            this.tvNavChannelsLabel.setTextColor(purple);
        }

        if (this.channelsFragment == null) {
            this.channelsFragment = new com.iptv.online.player.live.tv.smart.ui.main.ChannelsFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, this.channelsFragment)
                    .commit();
        } else {
            this.channelsFragment.loadPlaylist(false);
        }
    }

    // CHANGED: Display ChannelsFragment inside container instead of starting MainActivity
    public void openMainActivity() {
        showChannelsTab();
    }





    @Override // androidx.appcompat.app.AppCompatActivity, androidx.activity.ComponentActivity, android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        int i = 2;
        if (configuration.screenWidthDp < 600 && configuration.orientation != 2) {
            i = 1;
        }
        RecyclerView recyclerView = this.rvHomePlaylists;
        if (recyclerView != null) {
            if (i > 1) {
                recyclerView.setLayoutManager(new GridLayoutManager(this, i));
            } else {
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
            }
        }
    }

    private void setupBackPress() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) { // from class: com.iptv.online.player.live.tv.smart.ui.home.HomeActivity.2
            @Override // androidx.activity.OnBackPressedCallback
            public void handleOnBackPressed() {
                HomeActivity.this.handleAppExitBackPress();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleAppExitBackPress() {
//        AnalyticsTracker.logBackPress("HomeActivity");
        if (this.isMenuOpen) {
            closeActionMenu();
            return;
        }
        // CHANGED: If currently on Channels tab, go back to Home tab
        if (this.isChannelsTab) {
            showHomeTab();
            return;
        }
        finishAffinity();
        finishAndRemoveTask();
    }
    @Override // com.adsUtils.custom.CustomActivity
    public void setOnBackPressedClick() {
        handleAppExitBackPress();
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        handleAppExitBackPress();
    }
}

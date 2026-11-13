package com.iptv.online.player.live.tv.smart.ui.player;

import android.app.PictureInPictureParams;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Rational;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.media3.common.MediaItem;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.PlaybackException;
import androidx.media3.common.Player;
import androidx.media3.common.TrackGroup;
import androidx.media3.common.TrackSelectionOverride;
import androidx.media3.common.Tracks;
import androidx.media3.datasource.okhttp.OkHttpDataSource;
import androidx.media3.exoplayer.DefaultLoadControl;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory;
import androidx.media3.ui.PlayerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.PathInterpolatorCompat;
//import com.adsUtils.adsloader.nativeads.NativeLoader;
//import com.adsUtils.adsloader.nativeads.NativeType;
//import com.adsUtils.analytics.AnalyticsTracker;
//import com.adsUtils.custom.CustomActivity;
//import com.google.android.gms.common.ConnectionResult;
import com.google.common.collect.UnmodifiableIterator;
import com.google.common.net.HttpHeaders;
import com.iptv.online.player.live.tv.smart.AdsPackage.CustomActivity;
import com.iptv.online.player.live.tv.smart.R;
import com.iptv.online.player.live.tv.smart.data.local.PlaylistStorageManager;
import com.iptv.online.player.live.tv.smart.data.model.PlaylistItem;
import com.iptv.online.player.live.tv.smart.data.network.ApiClient;
import com.iptv.online.player.live.tv.smart.ui.adapters.PlaylistDrawerAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@SuppressWarnings("all")

/* loaded from: classes5.dex */
public class PlayerActivity extends com.iptv.online.player.live.tv.smart.AdsPackage.CustomActivity implements Player.Listener {
    public static final String EXTRA_CHANNEL = "extra_channel";
    public static final String EXTRA_CHANNEL_INDEX = "extra_channel_index";
    public static final String EXTRA_CHANNEL_LIST = "extra_channel_list";
    private static List<PlaylistItem> sPlaybackList;
    private AudioManager audioManager;
    private ImageButton btnAspectRatio;
    private ImageButton btnFavorite;
    private ImageButton btnOrientation;
    private ImageButton btnPlayPause;
    private View cardGestureFeedback;
    private PlaylistItem currentChannel;
    private PlaylistDrawerAdapter drawerAdapter;
    private ImageView ivGestureIcon;
    private RelativeLayout layoutControls;
    private LinearLayout layoutError;
    private LinearLayout layoutSideChannelsDrawer;
    private RelativeLayout layoutVodAdContainer;
    private LinearLayout llAdsView;
    private ProgressBar pbGestureProgress;
    private ExoPlayer player;
    private View playerBottomBar;
    private ProgressBar playerBuffering;
    private PlayerView playerView;
    private RecyclerView rvDrawerChannels;
    private PlaylistStorageManager storageManager;
    private TextView tvCategory;
    private TextView tvChannelTitle;
    private TextView tvGesturePercent;
    private TextView tvGestureTitle;
    private TextView tvPlayerErrorMsg;
    private List<PlaylistItem> channelList = new ArrayList();
    private int currentChannelIndex = 0;
    private final Handler hideHandler = new Handler(Looper.getMainLooper());
    private final Runnable hideControlsRunnable = new Runnable() { // from class: com.iptv.online.player.live.tv.smart.ui.player.PlayerActivity$$ExternalSyntheticLambda6
        @Override // java.lang.Runnable
        public final void run() {
            PlayerActivity.this.hideControls();
        }
    };
    private int currentAspectMode = 0;
    private final int[] resizeModes = {0, 3, 4};
    private final int[] resizeModeNameResIds = {R.string.fit_screen, R.string.fill_screen, R.string.zoom};
    private float touchStartY = 0.0f;
    private float touchStartX = 0.0f;
    private boolean isAdjustingBrightness = false;
    private boolean isAdjustingVolume = false;
    private int initialVolume = 0;
    private float initialBrightness = 0.0f;

    public static void setPlaybackList(List<PlaylistItem> list) {
        sPlaybackList = list != null ? new ArrayList(list) : null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.adsUtils.custom.CustomActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_player);
        this.storageManager = new PlaylistStorageManager(this);
        this.audioManager = (AudioManager) getSystemService(MimeTypes.BASE_TYPE_AUDIO);
        hideSystemUI();
        parseIntentData();
        initViews();
        setupSideDrawer();
        setupPlayer();
        updateOrientationLayout(getResources().getConfiguration().orientation);
        playChannel(this.currentChannel);
    }

    private void hideSystemUI() {
        if (Build.VERSION.SDK_INT >= 30) {
            WindowInsetsController insetsController = getWindow().getInsetsController();
            if (insetsController != null) {
                insetsController.hide(WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
                insetsController.setSystemBarsBehavior(2);
                return;
            }
            return;
        }
        getWindow().setFlags(1024, 1024);
    }

    private void parseIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            this.currentChannel = (PlaylistItem) intent.getSerializableExtra(EXTRA_CHANNEL);
            this.currentChannelIndex = intent.getIntExtra(EXTRA_CHANNEL_INDEX, 0);
            List<PlaylistItem> list = sPlaybackList;
            if (list != null && !list.isEmpty()) {
                this.channelList = new ArrayList(sPlaybackList);
            } else {
                List<PlaylistItem> list2 = (List) intent.getSerializableExtra(EXTRA_CHANNEL_LIST);
                if (list2 != null && !list2.isEmpty()) {
                    this.channelList = list2;
                } else {
                    PlaylistStorageManager playlistStorageManager = this.storageManager;
                    if (playlistStorageManager != null) {
                        this.channelList = playlistStorageManager.getCachedChannels();
                    }
                }
            }
        }
        if (this.currentChannel != null || this.channelList.isEmpty()) {
            return;
        }
        int i = this.currentChannelIndex;
        if (i >= 0 && i < this.channelList.size()) {
            this.currentChannel = this.channelList.get(this.currentChannelIndex);
        } else {
            this.currentChannel = this.channelList.get(0);
        }
    }

    private void initViews() {
        this.playerView = (PlayerView) findViewById(R.id.playerView);
        this.layoutControls = (RelativeLayout) findViewById(R.id.layoutPlayerControls);
        this.playerBuffering = (ProgressBar) findViewById(R.id.playerBuffering);
        this.layoutError = (LinearLayout) findViewById(R.id.layoutPlayerError);
        this.tvPlayerErrorMsg = (TextView) findViewById(R.id.tvPlayerErrorMsg);
        this.tvChannelTitle = (TextView) findViewById(R.id.tvPlayerChannelTitle);
        this.tvCategory = (TextView) findViewById(R.id.tvPlayerCategory);
        this.btnPlayPause = (ImageButton) findViewById(R.id.btnPlayPause);
        this.btnFavorite = (ImageButton) findViewById(R.id.btnPlayerFavorite);
        this.btnAspectRatio = (ImageButton) findViewById(R.id.btnAspectRatio);
        this.btnOrientation = (ImageButton) findViewById(R.id.btnOrientation);
        this.playerBottomBar = findViewById(R.id.playerBottomBar);
        this.layoutVodAdContainer = (RelativeLayout) findViewById(R.id.layoutVodAdContainer);
        this.llAdsView = (LinearLayout) findViewById(R.id.llAdsView);
        this.cardGestureFeedback = findViewById(R.id.cardGestureFeedback);
        this.ivGestureIcon = (ImageView) findViewById(R.id.ivGestureIcon);
        this.tvGestureTitle = (TextView) findViewById(R.id.tvGestureTitle);
        this.pbGestureProgress = (ProgressBar) findViewById(R.id.pbGestureProgress);
        this.tvGesturePercent = (TextView) findViewById(R.id.tvGesturePercent);
        this.layoutSideChannelsDrawer = (LinearLayout) findViewById(R.id.layoutSideChannelsDrawer);
        this.rvDrawerChannels = (RecyclerView) findViewById(R.id.rvDrawerChannels);
        findViewById(R.id.btnPlayerBack).setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.player.PlayerActivity$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                PlayerActivity.this.lambda$initViews$0(view);
            }
        });
        this.btnPlayPause.setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.player.PlayerActivity$$ExternalSyntheticLambda15
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                PlayerActivity.this.lambda$initViews$1(view);
            }
        });
        findViewById(R.id.btnNextChannel).setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.player.PlayerActivity$$ExternalSyntheticLambda16
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                PlayerActivity.this.lambda$initViews$2(view);
            }
        });
        findViewById(R.id.btnPrevChannel).setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.player.PlayerActivity$$ExternalSyntheticLambda17
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                PlayerActivity.this.lambda$initViews$3(view);
            }
        });
        this.btnAspectRatio.setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.player.PlayerActivity$$ExternalSyntheticLambda18
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                PlayerActivity.this.lambda$initViews$4(view);
            }
        });
        findViewById(R.id.btnAudioTracks).setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.player.PlayerActivity$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                PlayerActivity.this.lambda$initViews$5(view);
            }
        });
        findViewById(R.id.btnSubtitles).setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.player.PlayerActivity$$ExternalSyntheticLambda2
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                PlayerActivity.this.lambda$initViews$6(view);
            }
        });
        findViewById(R.id.btnPip).setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.player.PlayerActivity$$ExternalSyntheticLambda3
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                PlayerActivity.this.lambda$initViews$7(view);
            }
        });
        ImageButton imageButton = this.btnOrientation;
        if (imageButton != null) {
            imageButton.setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.player.PlayerActivity$$ExternalSyntheticLambda4
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    PlayerActivity.this.lambda$initViews$8(view);
                }
            });
        }
        RelativeLayout relativeLayout = this.layoutVodAdContainer;
        if (relativeLayout != null) {
            relativeLayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener() { // from class: com.iptv.online.player.live.tv.smart.ui.player.PlayerActivity$$ExternalSyntheticLambda5
                @Override // android.view.View.OnLayoutChangeListener
                public final void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
                    PlayerActivity.this.lambda$initViews$9(view, i, i2, i3, i4, i5, i6, i7, i8);
                }
            });
        }
        findViewById(R.id.btnPlayerChannelsDrawer).setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.player.PlayerActivity$$ExternalSyntheticLambda10
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                PlayerActivity.this.lambda$initViews$10(view);
            }
        });
        findViewById(R.id.btnCloseDrawer).setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.player.PlayerActivity$$ExternalSyntheticLambda11
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                PlayerActivity.this.lambda$initViews$11(view);
            }
        });
        EditText editText = (EditText) findViewById(R.id.etDrawerSearch);
        if (editText != null) {
            editText.addTextChangedListener(new TextWatcher() { // from class: com.iptv.online.player.live.tv.smart.ui.player.PlayerActivity.1
                @Override // android.text.TextWatcher
                public void afterTextChanged(Editable editable) {
                }

                @Override // android.text.TextWatcher
                public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                }

                @Override // android.text.TextWatcher
                public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                    if (PlayerActivity.this.drawerAdapter != null) {
                        PlayerActivity.this.drawerAdapter.filter(charSequence);
                    }
                    if (charSequence == null || charSequence.length() < 2) {
                        return;
                    }
//                    AnalyticsTracker.logSearch("PlayerActivity", charSequence.toString());
                }
            });
        }
        this.btnFavorite.setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.player.PlayerActivity$$ExternalSyntheticLambda12
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                PlayerActivity.this.lambda$initViews$12(view);
            }
        });
        findViewById(R.id.btnRetryPlayback).setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.player.PlayerActivity$$ExternalSyntheticLambda13
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                PlayerActivity.this.lambda$initViews$13(view);
            }
        });
        this.playerView.setOnTouchListener(new View.OnTouchListener() { // from class: com.iptv.online.player.live.tv.smart.ui.player.PlayerActivity$$ExternalSyntheticLambda14
            @Override // android.view.View.OnTouchListener
            public final boolean onTouch(View view, MotionEvent motionEvent) {
                boolean handlePlayerTouch;
                handlePlayerTouch = PlayerActivity.this.handlePlayerTouch(view, motionEvent);
                return handlePlayerTouch;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initViews$0(View view) {
        setOnBackPressedClick();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initViews$1(View view) {
        togglePlayPause();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initViews$2(View view) {
        switchChannel(1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initViews$3(View view) {
        switchChannel(-1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initViews$4(View view) {
        cycleAspectRatio();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initViews$5(View view) {
        showAudioTracksDialog();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initViews$6(View view) {
        showSubtitlesDialog();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initViews$7(View view) {
        enterPipMode();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initViews$8(View view) {
        toggleOrientation();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initViews$9(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        View view2;
        int i9 = i4 - i2;
        if (getResources().getConfiguration().orientation != 1 || i9 <= 0 || (view2 = this.playerBottomBar) == null || !(view2.getLayoutParams() instanceof RelativeLayout.LayoutParams)) {
            return;
        }
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.playerBottomBar.getLayoutParams();
        if (layoutParams.bottomMargin != i9) {
            layoutParams.bottomMargin = i9;
            this.playerBottomBar.setLayoutParams(layoutParams);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initViews$10(View view) {
        toggleChannelsDrawer();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initViews$11(View view) {
        this.layoutSideChannelsDrawer.setVisibility(View.GONE);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initViews$12(View view) {
        PlaylistItem playlistItem = this.currentChannel;
        if (playlistItem != null) {
            this.storageManager.toggleFavorite(playlistItem.getId());
            boolean isFavorite = this.storageManager.isFavorite(this.currentChannel.getId());
            updateFavoriteButton();
//            AnalyticsTracker.logFavorite("PlayerActivity", this.currentChannel.getName(), isFavorite);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initViews$13(View view) {
        PlaylistItem playlistItem = this.currentChannel;
        if (playlistItem != null) {
            playChannel(playlistItem);
        }
    }

    private void setupSideDrawer() {
        this.rvDrawerChannels.setLayoutManager(new LinearLayoutManager(this));
        PlaylistDrawerAdapter playlistDrawerAdapter = new PlaylistDrawerAdapter(this, new PlaylistDrawerAdapter.OnDrawerChannelClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.player.PlayerActivity$$ExternalSyntheticLambda8
            @Override // com.iptv.online.player.live.tv.smart.ui.adapters.PlaylistDrawerAdapter.OnDrawerChannelClickListener
            public final void onChannelClick(PlaylistItem playlistItem, int i) {
                PlayerActivity.this.lambda$setupSideDrawer$14(playlistItem, i);
            }
        });
        this.drawerAdapter = playlistDrawerAdapter;
        this.rvDrawerChannels.setAdapter(playlistDrawerAdapter);
        PlaylistItem playlistItem = this.currentChannel;
        if (playlistItem != null) {
            this.drawerAdapter.setChannels(this.channelList, playlistItem.getId());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupSideDrawer$14(PlaylistItem playlistItem, int i) {
        this.currentChannelIndex = i;
        playChannel(playlistItem);
        this.layoutSideChannelsDrawer.setVisibility(View.GONE);
//        showTabScrollInterstitialAds();
    }

    private void setupPlayer() {
        String str;
        DefaultLoadControl build = new DefaultLoadControl.Builder().setBufferDurationsMs(PathInterpolatorCompat.MAX_NUM_POINTS, 30000, 1500, DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_MS).build();
        PlaylistItem playlistItem = this.currentChannel;
        if (playlistItem != null && playlistItem.getUserAgent() != null) {
            str = this.currentChannel.getUserAgent();
        } else {
            str = ApiClient.DEFAULT_USER_AGENT;
        }
        HashMap hashMap = new HashMap();
        hashMap.put("User-Agent", str);
        PlaylistItem playlistItem2 = this.currentChannel;
        if (playlistItem2 != null && playlistItem2.getReferrer() != null) {
            hashMap.put(HttpHeaders.REFERER, this.currentChannel.getReferrer());
        }
        ExoPlayer build2 = new ExoPlayer.Builder(this).setMediaSourceFactory(new DefaultMediaSourceFactory(new OkHttpDataSource.Factory(ApiClient.getClient()).setUserAgent(str).setDefaultRequestProperties((Map<String, String>) hashMap))).setLoadControl(build).build();
        this.player = build2;
        this.playerView.setPlayer(build2);
        this.player.addListener(this);
    }

    private void playChannel(PlaylistItem playlistItem) {
        if (playlistItem == null || playlistItem.getStreamUrl().isEmpty()) {
            showError("Invalid channel stream URL");
            return;
        }
        this.currentChannel = playlistItem;
        this.tvChannelTitle.setText(playlistItem.getName());
        this.tvCategory.setText(playlistItem.getGroupTitle());
        updateFavoriteButton();
        PlaylistDrawerAdapter playlistDrawerAdapter = this.drawerAdapter;
        if (playlistDrawerAdapter != null) {
            playlistDrawerAdapter.setCurrentPlayingId(playlistItem.getId());
        }
        this.layoutError.setVisibility(View.GONE);
        this.playerBuffering.setVisibility(View.VISIBLE);
        MediaItem fromUri = MediaItem.fromUri(Uri.parse(playlistItem.getStreamUrl()));
        this.player.stop();
        this.player.setMediaItem(fromUri);
        this.player.prepare();
        this.player.setPlayWhenReady(true);
        this.storageManager.addRecentChannel(playlistItem);
        scheduleControlsHide();
//        AnalyticsTracker.logPlayback("PlayerActivity", playlistItem.getName(), "live_channel", playlistItem.getStreamUrl(), playlistItem.getGroupTitle());
    }

    private void togglePlayPause() {
        ExoPlayer exoPlayer = this.player;
        if (exoPlayer == null) {
            return;
        }
        boolean isPlaying = exoPlayer.isPlaying();
        ExoPlayer exoPlayer2 = this.player;
        if (isPlaying) {
            exoPlayer2.pause();
            this.btnPlayPause.setImageResource(R.drawable.ic_play);
//            AnalyticsTracker.logPlayerControl("PlayerActivity", "play_pause", "pause");
        } else {
            exoPlayer2.play();
            this.btnPlayPause.setImageResource(R.drawable.ic_pause);
//            AnalyticsTracker.logPlayerControl("PlayerActivity", "play_pause", "play");
        }
        scheduleControlsHide();
    }

    private void switchChannel(int i) {
        if (this.channelList.isEmpty()) {
            return;
        }
        this.currentChannelIndex = ((this.currentChannelIndex + i) + this.channelList.size()) % this.channelList.size();
//        AnalyticsTracker.logPlayerControl("PlayerActivity", "switch_channel", i > 0 ? "next" : "previous");
        playChannel(this.channelList.get(this.currentChannelIndex));
    }

    private void cycleAspectRatio() {
        int[] iArr = this.resizeModes;
        int length = (this.currentAspectMode + 1) % iArr.length;
        this.currentAspectMode = length;
        this.playerView.setResizeMode(iArr[length]);
        String string = getString(this.resizeModeNameResIds[this.currentAspectMode]);
        Toast.makeText(this, getString(R.string.msg_aspect_ratio, new Object[]{string}), 0).show();
//        AnalyticsTracker.logPlayerControl("PlayerActivity", "aspect_ratio", string);
        scheduleControlsHide();
    }

    private void updateFavoriteButton() {
        PlaylistItem playlistItem = this.currentChannel;
        if (playlistItem != null) {
            this.btnFavorite.setImageResource(this.storageManager.isFavorite(playlistItem.getId()) ? R.drawable.ic_star_filled : R.drawable.ic_star_outline);
        }
    }

    private void toggleChannelsDrawer() {
        int indexOf;
        int visibility = this.layoutSideChannelsDrawer.getVisibility();
        LinearLayout linearLayout = this.layoutSideChannelsDrawer;
        if (visibility == 0) {
            linearLayout.setVisibility(View.GONE);
//            AnalyticsTracker.logAction("PlayerActivity", "close_channels_drawer", null);
            return;
        }
        linearLayout.setVisibility(View.VISIBLE);
//        AnalyticsTracker.logAction("PlayerActivity", "open_channels_drawer", null);
        PlaylistItem playlistItem = this.currentChannel;
        if (playlistItem == null || (indexOf = this.channelList.indexOf(playlistItem)) < 0) {
            return;
        }
        this.rvDrawerChannels.scrollToPosition(indexOf);
    }

    private void enterPipMode() {
        if (Build.VERSION.SDK_INT >= 26) {
            hideControls();
            PictureInPictureParams.Builder builder = new PictureInPictureParams.Builder();
            builder.setAspectRatio(new Rational(16, 9));
            enterPictureInPictureMode(builder.build());
//            AnalyticsTracker.logPlayerControl("PlayerActivity", "pip_mode", "entered");
            return;
        }
        Toast.makeText(this, R.string.msg_pip_requires_android_8, 0).show();
    }

    private void toggleOrientation() {
        if (getResources().getConfiguration().orientation == 2) {
            setRequestedOrientation(1);
            updateOrientationLayout(1);
//            AnalyticsTracker.logPlayerControl("PlayerActivity", "orientation", "portrait");
            return;
        }
        setRequestedOrientation(6);
        updateOrientationLayout(2);
//        AnalyticsTracker.logPlayerControl("PlayerActivity", "orientation", "landscape");
    }

    private void updateOrientationLayout(int i) {
        int i2;
        RelativeLayout relativeLayout = this.layoutVodAdContainer;
        if (i == 2) {
            if (relativeLayout != null) {
                relativeLayout.setVisibility(View.GONE);
            }
            LinearLayout linearLayout = this.llAdsView;
            if (linearLayout != null) {
                linearLayout.setVisibility(View.GONE);
            }
            View view = this.playerBottomBar;
            if (view != null && (view.getLayoutParams() instanceof RelativeLayout.LayoutParams)) {
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.playerBottomBar.getLayoutParams();
                layoutParams.bottomMargin = 0;
                this.playerBottomBar.setLayoutParams(layoutParams);
            }
            ImageButton imageButton = this.btnOrientation;
            if (imageButton != null) {
                imageButton.setImageResource(R.drawable.ic_fullscreen_exit);
                return;
            }
            return;
        }
        if (relativeLayout != null) {
            relativeLayout.setVisibility(View.VISIBLE);
        }
        LinearLayout linearLayout2 = this.llAdsView;
        if (linearLayout2 != null) {
            linearLayout2.setVisibility(View.VISIBLE);
            loadNativeAd();
        }
        View view2 = this.playerBottomBar;
        if (view2 != null && (view2.getLayoutParams() instanceof RelativeLayout.LayoutParams)) {
            RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) this.playerBottomBar.getLayoutParams();
            RelativeLayout relativeLayout2 = this.layoutVodAdContainer;
            if (relativeLayout2 != null && relativeLayout2.getHeight() > 0) {
                i2 = this.layoutVodAdContainer.getHeight();
            } else {
                i2 = (int) (getResources().getDisplayMetrics().density * 140.0f);
            }
            layoutParams2.bottomMargin = i2;
            this.playerBottomBar.setLayoutParams(layoutParams2);
        }
        ImageButton imageButton2 = this.btnOrientation;
        if (imageButton2 != null) {
            imageButton2.setImageResource(R.drawable.ic_fullscreen);
        }
    }

    private void loadNativeAd() {
        LinearLayout linearLayout = this.llAdsView;
        if (linearLayout == null || linearLayout.getChildCount() != 0) {
            return;
        }
//        NativeLoader.showNativeAds(this, this.llAdsView, NativeType.BIG_NATIVE);
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.activity.ComponentActivity, android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        hideSystemUI();
        updateOrientationLayout(configuration.orientation);
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onPictureInPictureModeChanged(boolean z, Configuration configuration) {
        super.onPictureInPictureModeChanged(z, configuration);
        RelativeLayout relativeLayout = this.layoutControls;
        if (z) {
            relativeLayout.setVisibility(View.GONE);
            this.layoutSideChannelsDrawer.setVisibility(View.GONE);
            RelativeLayout relativeLayout2 = this.layoutVodAdContainer;
            if (relativeLayout2 != null) {
                relativeLayout2.setVisibility(View.GONE);
                return;
            }
            return;
        }
        relativeLayout.setVisibility(View.VISIBLE);
        scheduleControlsHide();
        updateOrientationLayout(getResources().getConfiguration().orientation);
    }

    private void showAudioTracksDialog() {
        ExoPlayer exoPlayer = this.player;
        if (exoPlayer == null) {
            return;
        }
        Tracks currentTracks = exoPlayer.getCurrentTracks();
        final ArrayList arrayList = new ArrayList();
        final ArrayList arrayList2 = new ArrayList();
        UnmodifiableIterator<Tracks.Group> it = currentTracks.getGroups().iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            Tracks.Group next = it.next();
            if (next.getType() == 1) {
                TrackGroup mediaTrackGroup = next.getMediaTrackGroup();
                for (int i = 0; i < mediaTrackGroup.length; i++) {
                    String str = mediaTrackGroup.getFormat(i).label;
                    String str2 = mediaTrackGroup.getFormat(i).language;
                    if (str == null) {
                        str = str2 != null ? str2 : "Audio Track " + (arrayList.size() + 1);
                    }
                    arrayList.add(str);
                    arrayList2.add(new TrackSelectionOverride(mediaTrackGroup, i));
                }
            }
        }
        if (arrayList.isEmpty()) {
            Toast.makeText(this, R.string.msg_no_audio_tracks, 0).show();
        } else {
            new AlertDialog.Builder(this, R.style.CustomDialogTheme).setTitle(R.string.audio_tracks).setItems((CharSequence[]) arrayList.toArray(new String[0]), new DialogInterface.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.player.PlayerActivity$$ExternalSyntheticLambda7
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i2) {
                    PlayerActivity.this.lambda$showAudioTracksDialog$15(arrayList, arrayList2, dialogInterface, i2);
                }
            }).setNegativeButton(R.string.cancel, (DialogInterface.OnClickListener) null).show();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showAudioTracksDialog$15(List list, List list2, DialogInterface dialogInterface, int i) {
//        AnalyticsTracker.logPlayerControl("PlayerActivity", "audio_track", (String) list.get(i));
        ExoPlayer exoPlayer = this.player;
        exoPlayer.setTrackSelectionParameters(exoPlayer.getTrackSelectionParameters().buildUpon().setOverrideForType((TrackSelectionOverride) list2.get(i)).build());
    }

    private void showSubtitlesDialog() {
        ExoPlayer exoPlayer = this.player;
        if (exoPlayer == null) {
            return;
        }
        Tracks currentTracks = exoPlayer.getCurrentTracks();
        final ArrayList arrayList = new ArrayList();
        final ArrayList arrayList2 = new ArrayList();
        arrayList.add(getString(R.string.off));
        arrayList2.add(null);
        UnmodifiableIterator<Tracks.Group> it = currentTracks.getGroups().iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            Tracks.Group next = it.next();
            if (next.getType() == 3) {
                TrackGroup mediaTrackGroup = next.getMediaTrackGroup();
                for (int i = 0; i < mediaTrackGroup.length; i++) {
                    String str = mediaTrackGroup.getFormat(i).label;
                    String str2 = mediaTrackGroup.getFormat(i).language;
                    if (str == null) {
                        str = str2 != null ? str2 : "Subtitle " + arrayList.size();
                    }
                    arrayList.add(str);
                    arrayList2.add(new TrackSelectionOverride(mediaTrackGroup, i));
                }
            }
        }
        if (arrayList.size() <= 1) {
            Toast.makeText(this, R.string.msg_no_subtitles, 0).show();
        } else {
            new AlertDialog.Builder(this, R.style.CustomDialogTheme).setTitle(R.string.subtitles).setItems((CharSequence[]) arrayList.toArray(new String[0]), new DialogInterface.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.player.PlayerActivity$$ExternalSyntheticLambda9
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i2) {
                    PlayerActivity.this.lambda$showSubtitlesDialog$16(arrayList, arrayList2, dialogInterface, i2);
                }
            }).setNegativeButton(R.string.cancel, (DialogInterface.OnClickListener) null).show();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showSubtitlesDialog$16(List list, List list2, DialogInterface dialogInterface, int i) {
//        AnalyticsTracker.logPlayerControl("PlayerActivity", "subtitle", (String) list.get(i));
        ExoPlayer exoPlayer = this.player;
        if (i == 0) {
            exoPlayer.setTrackSelectionParameters(exoPlayer.getTrackSelectionParameters().buildUpon().setTrackTypeDisabled(3, true).build());
        } else {
            exoPlayer.setTrackSelectionParameters(exoPlayer.getTrackSelectionParameters().buildUpon().setTrackTypeDisabled(3, false).setOverrideForType((TrackSelectionOverride) list2.get(i)).build());
        }
    }

    @Override // androidx.appcompat.app.AppCompatActivity, android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i != 4) {
            if (i == 23 || i == 66) {
                if (this.layoutControls.getVisibility() != 0) {
                    showControls();
                    return true;
                }
            } else {
                if (i != 82) {
                    if (i != 85 && i != 126 && i != 127) {
                        switch (i) {
                            case 19:
                                switchChannel(-1);
                                return true;
                            case 20:
                                switchChannel(1);
                                return true;
                        }
                    }
                    togglePlayPause();
                    return true;
                }
                toggleChannelsDrawer();
                return true;
            }
        } else if (this.layoutSideChannelsDrawer.getVisibility() == 0) {
            this.layoutSideChannelsDrawer.setVisibility(View.GONE);
            return true;
        }
        return super.onKeyDown(i, keyEvent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean handlePlayerTouch(View view, MotionEvent motionEvent) {
        AudioManager audioManager;
        AudioManager audioManager2;
        int i = getResources().getDisplayMetrics().widthPixels;
        int i2 = getResources().getDisplayMetrics().heightPixels;
        int action = motionEvent.getAction();
        if (action == 0) {
            this.touchStartX = motionEvent.getX();
            this.touchStartY = motionEvent.getY();
            float f = this.touchStartX;
            float f2 = i / 2.0f;
            this.isAdjustingBrightness = f < f2;
            boolean z = f >= f2;
            this.isAdjustingVolume = z;
            if (z && (audioManager = this.audioManager) != null) {
                this.initialVolume = audioManager.getStreamVolume(3);
            }
            if (this.isAdjustingBrightness) {
                WindowManager.LayoutParams attributes = getWindow().getAttributes();
                this.initialBrightness = attributes.screenBrightness >= 0.0f ? attributes.screenBrightness : 0.5f;
            }
            return true;
        } else if (action == 1) {
            float abs = Math.abs(motionEvent.getX() - this.touchStartX);
            float abs2 = Math.abs(motionEvent.getY() - this.touchStartY);
            if (abs < 20.0f && abs2 < 20.0f) {
                toggleControls();
            }
            View view2 = this.cardGestureFeedback;
            if (view2 != null) {
                view2.setVisibility(View.GONE);
            }
            return true;
        } else if (action != 2) {
            return false;
        } else {
            float y = this.touchStartY - motionEvent.getY();
            if (Math.abs(y) > 30.0f) {
                if (this.isAdjustingVolume && (audioManager2 = this.audioManager) != null) {
                    int streamMaxVolume = audioManager2.getStreamMaxVolume(3);
                    float f3 = y / i2;
                    float f4 = streamMaxVolume;
                    int max = Math.max(0, Math.min(streamMaxVolume, this.initialVolume + ((int) (f3 * f4 * 2.0f))));
                    this.audioManager.setStreamVolume(3, max, 0);
                    showGestureFeedback(true, (int) ((max / f4) * 100.0f));
                } else if (this.isAdjustingBrightness) {
                    float max2 = Math.max(0.01f, Math.min(1.0f, this.initialBrightness + ((y / i2) * 1.5f)));
                    WindowManager.LayoutParams attributes2 = getWindow().getAttributes();
                    attributes2.screenBrightness = max2;
                    getWindow().setAttributes(attributes2);
                    showGestureFeedback(false, (int) (max2 * 100.0f));
                }
            }
            return true;
        }
    }

    private void showGestureFeedback(boolean z, int i) {
        if (this.cardGestureFeedback == null) {
            return;
        }
        ImageView imageView = this.ivGestureIcon;
        if (z) {
            imageView.setImageResource(R.drawable.ic_volume);
            this.ivGestureIcon.setColorFilter(ContextCompat.getColor(this, R.color.brand_blue));
            this.tvGestureTitle.setText(R.string.volume);
        } else {
            imageView.setImageResource(R.drawable.ic_brightness);
            this.ivGestureIcon.setColorFilter(ContextCompat.getColor(this, R.color.warning_amber));
            this.tvGestureTitle.setText(R.string.brightness);
        }
        this.pbGestureProgress.setProgress(i);
        this.tvGesturePercent.setText(getString(R.string.format_volume_percent, new Object[]{Integer.valueOf(i)}));
        this.cardGestureFeedback.setVisibility(View.VISIBLE);
    }

    private void toggleControls() {
        if (this.layoutControls.getVisibility() == 0) {
            hideControls();
        } else {
            showControls();
        }
    }

    private void showControls() {
        this.layoutControls.setVisibility(View.VISIBLE);
        scheduleControlsHide();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hideControls() {
        this.layoutControls.setVisibility(View.GONE);
        View view = this.cardGestureFeedback;
        if (view != null) {
            view.setVisibility(View.GONE);
        }
    }

    private void scheduleControlsHide() {
        this.hideHandler.removeCallbacks(this.hideControlsRunnable);
        this.hideHandler.postDelayed(this.hideControlsRunnable, 5000L);
    }

    private void showError(String str) {
        this.playerBuffering.setVisibility(View.GONE);
        this.layoutError.setVisibility(View.VISIBLE);
        TextView textView = this.tvPlayerErrorMsg;
        if (str == null) {
            str = getString(R.string.playback_error);
        }
        textView.setText(str);
    }

    @Override // androidx.media3.common.Player.Listener
    public void onPlaybackStateChanged(int i) {
        if (i == 2) {
            this.playerBuffering.setVisibility(View.VISIBLE);
            this.layoutError.setVisibility(View.GONE);
        } else if (i == 3) {
            this.playerBuffering.setVisibility(View.GONE);
            this.layoutError.setVisibility(View.GONE);
            this.btnPlayPause.setImageResource(this.player.isPlaying() ? R.drawable.ic_pause : R.drawable.ic_play);
        } else if (i == 4) {
            this.playerBuffering.setVisibility(View.GONE);
        }
    }

    @Override // androidx.media3.common.Player.Listener
    public void onIsPlayingChanged(boolean z) {
        this.btnPlayPause.setImageResource(z ? R.drawable.ic_pause : R.drawable.ic_play);
    }

    @Override // androidx.media3.common.Player.Listener
    public void onPlayerError(PlaybackException playbackException) {
        showError("Stream error: " + playbackException.getErrorCodeName() + "\n" + (playbackException.getMessage() != null ? playbackException.getMessage() : ""));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.adsUtils.custom.CustomActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onPause() {
        super.onPause();
        if (this.player == null || isInPictureInPictureMode()) {
            return;
        }
        this.player.pause();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.adsUtils.custom.CustomActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        if (this.player != null && !isInPictureInPictureMode()) {
            this.player.play();
        }
        updateOrientationLayout(getResources().getConfiguration().orientation);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.adsUtils.custom.CustomActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        sPlaybackList = null;
        this.hideHandler.removeCallbacks(this.hideControlsRunnable);
        ExoPlayer exoPlayer = this.player;
        if (exoPlayer != null) {
            exoPlayer.release();
            this.player = null;
        }
    }
}

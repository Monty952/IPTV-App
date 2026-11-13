package com.iptv.online.player.live.tv.smart.ui.vod;

import android.app.PictureInPictureParams;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Rational;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.OptIn;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.media3.common.MediaItem;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.PlaybackException;
import androidx.media3.common.PlaybackParameters;
import androidx.media3.common.Player;
import androidx.media3.common.TrackGroup;
import androidx.media3.common.TrackSelectionOverride;
import androidx.media3.common.Tracks;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.datasource.DefaultDataSource;
import androidx.media3.datasource.DefaultHttpDataSource;
import androidx.media3.exoplayer.DefaultLoadControl;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory;
import androidx.media3.extractor.DefaultExtractorsFactory;
import androidx.media3.ui.PlayerView;
import androidx.vectordrawable.graphics.drawable.PathInterpolatorCompat;
//import com.adsUtils.adsloader.nativeads.NativeLoader;
//import com.adsUtils.adsloader.nativeads.NativeType;
//import com.adsUtils.analytics.AnalyticsTracker;
//import com.adsUtils.custom.CustomActivity;
//import com.google.android.gms.common.ConnectionResult;
import com.google.common.collect.UnmodifiableIterator;
import com.iptv.online.player.live.tv.smart.AdsPackage.CustomActivity;
import com.iptv.online.player.live.tv.smart.R;
import com.iptv.online.player.live.tv.smart.data.local.PlaylistStorageManager;
import com.iptv.online.player.live.tv.smart.data.model.WatchProgress;
import com.iptv.online.player.live.tv.smart.data.network.ApiClient;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/* loaded from: classes5.dex */
public class VodPlayerActivity extends com.iptv.online.player.live.tv.smart.AdsPackage.CustomActivity implements Player.Listener {
    public static final String EXTRA_CONTENT_ID = "extra_content_id";
    public static final String EXTRA_POSTER_URL = "extra_poster_url";
    public static final String EXTRA_RESUME_POSITION = "extra_resume_position";
    public static final String EXTRA_STREAM_URL = "extra_stream_url";
    public static final String EXTRA_SUBTITLE = "extra_subtitle";
    public static final String EXTRA_TITLE = "extra_title";
    private AudioManager audioManager;
    private ImageButton btnAspectRatio;
    private ImageButton btnForward;
    private ImageButton btnPlayPause;
    private ImageButton btnRewind;
    private ImageButton btnSpeed;
    private View cardGestureFeedback;
    private String contentId;
    private ImageView ivGestureIcon;
    private RelativeLayout layoutControls;
    private LinearLayout layoutError;
    private View layoutVodAdContainer;
    private LinearLayout llAdsView;
    private ProgressBar pbGestureProgress;
    private ExoPlayer player;
    private ProgressBar playerBuffering;
    private PlayerView playerView;
    private String posterUrl;
    private SeekBar seekBar;
    private PlaylistStorageManager storageManager;
    private String streamUrl;
    private String subtitle;
    private String title;
    private TextView tvCurrentTime;
    private TextView tvGesturePercent;
    private TextView tvGestureTitle;
    private TextView tvPlayerErrorMsg;
    private TextView tvSubtitle;
    private TextView tvTitle;
    private TextView tvTotalTime;
    private View vodPlayerBottomBar;
    private long initialResumePosition = 0;
    private final Handler progressHandler = new Handler(Looper.getMainLooper());
    private final Handler hideHandler = new Handler(Looper.getMainLooper());
    private final Runnable hideControlsRunnable = new Runnable() { // from class: com.iptv.online.player.live.tv.smart.ui.vod.VodPlayerActivity$$ExternalSyntheticLambda5
        @Override // java.lang.Runnable
        public final void run() {
            VodPlayerActivity.this.hideControls();
        }
    };
    private boolean isUserSeeking = false;
    private int currentAspectMode = 0;
    private final int[] resizeModes = {0, 3, 4};
    private final int[] resizeModeNameResIds = {R.string.fit_screen, R.string.fill_screen, R.string.zoom};
    private final float[] speeds = {0.75f, 1.0f, 1.25f, 1.5f, 2.0f};
    private final String[] speedNames = {"0.75x", "1.0x (Normal)", "1.25x", "1.5x", "2.0x"};
    private int currentSpeedIndex = 1;
    private float touchStartY = 0.0f;
    private float touchStartX = 0.0f;
    private boolean isAdjustingBrightness = false;
    private boolean isAdjustingVolume = false;
    private int initialVolume = 0;
    private float initialBrightness = 0.0f;
    private int currentDataSourceAttempt = 0;
    private final String[] userAgentFallbacks = {ApiClient.DEFAULT_USER_AGENT, "VLC/3.0.18 LibVLC/3.0.18", "IPTVSmartersPlayer", "Lavf/58.29.100"};

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.adsUtils.custom.CustomActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_vod_player);
        this.storageManager = new PlaylistStorageManager(this);
        this.audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        hideSystemUI();
        parseIntent();
        initViews();
        setupPlayer();
        startProgressUpdater();
    }

    private void hideSystemUI() {
        if (Build.VERSION.SDK_INT >= 30) {
            WindowInsetsController insetsController = getWindow().getInsetsController();
            if (insetsController != null) {
                insetsController.hide(WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
                insetsController.setSystemBarsBehavior(WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
                return;
            }
            return;
        }
        getWindow().setFlags(1024, 1024);
    }

    private void parseIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            this.streamUrl = intent.getStringExtra(EXTRA_STREAM_URL);
            this.title = intent.getStringExtra(EXTRA_TITLE);
            this.subtitle = intent.getStringExtra(EXTRA_SUBTITLE);
            this.posterUrl = intent.getStringExtra(EXTRA_POSTER_URL);
            this.contentId = intent.getStringExtra(EXTRA_CONTENT_ID);
            this.initialResumePosition = intent.getLongExtra(EXTRA_RESUME_POSITION, 0L);
        }
        if (this.title == null) {
            this.title = "Movie";
        }
    }

    private void initViews() {
        this.playerView = (PlayerView) findViewById(R.id.vodPlayerView);
        this.layoutControls = (RelativeLayout) findViewById(R.id.layoutVodControls);
        this.playerBuffering = (ProgressBar) findViewById(R.id.vodBuffering);
        this.layoutError = (LinearLayout) findViewById(R.id.layoutVodError);
        this.tvPlayerErrorMsg = (TextView) findViewById(R.id.tvVodErrorMsg);
        this.tvTitle = (TextView) findViewById(R.id.tvVodPlayerTitle);
        this.tvSubtitle = (TextView) findViewById(R.id.tvVodPlayerSubtitle);
        this.btnPlayPause = (ImageButton) findViewById(R.id.btnVodPlayPause);
        this.btnRewind = (ImageButton) findViewById(R.id.btnVodRewind);
        this.btnForward = (ImageButton) findViewById(R.id.btnVodForward);
        this.seekBar = (SeekBar) findViewById(R.id.vodSeekBar);
        this.tvCurrentTime = (TextView) findViewById(R.id.tvVodCurrentTime);
        this.tvTotalTime = (TextView) findViewById(R.id.tvVodTotalTime);
        this.btnSpeed = (ImageButton) findViewById(R.id.btnVodSpeed);
        this.btnAspectRatio = (ImageButton) findViewById(R.id.btnVodAspectRatio);
        this.cardGestureFeedback = findViewById(R.id.cardVodGestureFeedback);
        this.ivGestureIcon = (ImageView) findViewById(R.id.ivVodGestureIcon);
        this.tvGestureTitle = (TextView) findViewById(R.id.tvVodGestureTitle);
        this.pbGestureProgress = (ProgressBar) findViewById(R.id.pbVodGestureProgress);
        this.tvGesturePercent = (TextView) findViewById(R.id.tvVodGesturePercent);
        this.tvTitle.setText(this.title);
        String str = this.subtitle;
        if (str != null && !str.isEmpty()) {
            this.tvSubtitle.setVisibility(View.VISIBLE);
            this.tvSubtitle.setText(this.subtitle);
        } else {
            this.tvSubtitle.setVisibility(View.GONE);
        }
        findViewById(R.id.btnVodPlayerBack).setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.vod.VodPlayerActivity$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                VodPlayerActivity.this.lambda$initViews$0(view);
            }
        });
        this.btnPlayPause.setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.vod.VodPlayerActivity$$ExternalSyntheticLambda8
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                VodPlayerActivity.this.lambda$initViews$1(view);
            }
        });
        this.btnRewind.setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.vod.VodPlayerActivity$$ExternalSyntheticLambda9
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                VodPlayerActivity.this.lambda$initViews$2(view);
            }
        });
        this.btnForward.setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.vod.VodPlayerActivity$$ExternalSyntheticLambda10
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                VodPlayerActivity.this.lambda$initViews$3(view);
            }
        });
        this.btnSpeed.setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.vod.VodPlayerActivity$$ExternalSyntheticLambda11
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                VodPlayerActivity.this.lambda$initViews$4(view);
            }
        });
        this.btnAspectRatio.setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.vod.VodPlayerActivity$$ExternalSyntheticLambda12
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                VodPlayerActivity.this.lambda$initViews$5(view);
            }
        });
        findViewById(R.id.btnVodAudio).setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.vod.VodPlayerActivity$$ExternalSyntheticLambda13
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                VodPlayerActivity.this.lambda$initViews$6(view);
            }
        });
        findViewById(R.id.btnVodPip).setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.vod.VodPlayerActivity$$ExternalSyntheticLambda14
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                VodPlayerActivity.this.lambda$initViews$7(view);
            }
        });
        findViewById(R.id.btnVodOrientation).setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.vod.VodPlayerActivity$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                VodPlayerActivity.this.lambda$initViews$8(view);
            }
        });
        findViewById(R.id.btnVodRetry).setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.vod.VodPlayerActivity$$ExternalSyntheticLambda2
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                VodPlayerActivity.this.lambda$initViews$9(view);
            }
        });
        this.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { // from class: com.iptv.online.player.live.tv.smart.ui.vod.VodPlayerActivity.1
            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                if (!z || VodPlayerActivity.this.player == null) {
                    return;
                }
                VodPlayerActivity.this.tvCurrentTime.setText(VodPlayerActivity.this.formatTime(i));
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar seekBar) {
                VodPlayerActivity.this.isUserSeeking = true;
                VodPlayerActivity.this.hideHandler.removeCallbacks(VodPlayerActivity.this.hideControlsRunnable);
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (VodPlayerActivity.this.player != null) {
                    VodPlayerActivity.this.player.seekTo(seekBar.getProgress());
                }
                VodPlayerActivity.this.isUserSeeking = false;
                VodPlayerActivity.this.scheduleControlsHide();
            }
        });
        this.playerView.setOnTouchListener(new View.OnTouchListener() { // from class: com.iptv.online.player.live.tv.smart.ui.vod.VodPlayerActivity$$ExternalSyntheticLambda6
            @Override // android.view.View.OnTouchListener
            public final boolean onTouch(View view, MotionEvent motionEvent) {
                boolean handlePlayerTouch;
                handlePlayerTouch = VodPlayerActivity.this.handlePlayerTouch(view, motionEvent);
                return handlePlayerTouch;
            }
        });
        this.layoutVodAdContainer = findViewById(R.id.layoutVodAdContainer);
        this.llAdsView = (LinearLayout) findViewById(R.id.llAdsView);
        this.vodPlayerBottomBar = findViewById(R.id.vodPlayerBottomBar);
        View view = this.layoutVodAdContainer;
        if (view != null) {
            view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() { // from class: com.iptv.online.player.live.tv.smart.ui.vod.VodPlayerActivity$$ExternalSyntheticLambda7
                @Override // android.view.View.OnLayoutChangeListener
                public final void onLayoutChange(View view2, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
                    VodPlayerActivity.this.lambda$initViews$10(view2, i, i2, i3, i4, i5, i6, i7, i8);
                }
            });
        }
        updateOrientationLayout(getResources().getConfiguration().orientation);
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
        seekRelative(-10000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initViews$3(View view) {
        seekRelative(10000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initViews$4(View view) {
        showSpeedDialog();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initViews$5(View view) {
        cycleAspectRatio();
    }

    /* JADX INFO: Access modifiers changed from: private */
    @OptIn(markerClass = UnstableApi.class)
    public /* synthetic */ void lambda$initViews$6(View view) {
        showAudioTracksDialog();
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
    public /* synthetic */ void lambda$initViews$9(View view) {
        this.currentDataSourceAttempt = 0;
        setupPlayer();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initViews$10(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        View view2;
        int i9 = i4 - i2;
        if (getResources().getConfiguration().orientation != 1 || i9 <= 0 || (view2 = this.vodPlayerBottomBar) == null || !(view2.getLayoutParams() instanceof RelativeLayout.LayoutParams)) {
            return;
        }
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.vodPlayerBottomBar.getLayoutParams();
        if (layoutParams.bottomMargin != i9) {
            layoutParams.bottomMargin = i9;
            this.vodPlayerBottomBar.setLayoutParams(layoutParams);
        }
    }

    @OptIn(markerClass = UnstableApi.class)
    private void setupPlayer() {
        String str = this.streamUrl;
        if (str == null || str.trim().isEmpty()) {
            showError("Invalid stream link");
            return;
        }
        ExoPlayer exoPlayer = this.player;
        if (exoPlayer != null) {
            try {
                exoPlayer.stop();
                this.player.release();
            } catch (Exception unused) {
            }
            this.player = null;
        }
        String trim = this.streamUrl.trim();
        if (this.currentDataSourceAttempt >= 2) {
            if (trim.startsWith("http://")) {
                trim = "https://" + trim.substring(7);
            } else if (trim.startsWith("https://")) {
                trim = "http://" + trim.substring(8);
            }
        }
        String[] strArr = this.userAgentFallbacks;
        ExoPlayer build = new ExoPlayer.Builder(this)
                .setMediaSourceFactory(new DefaultMediaSourceFactory(new DefaultDataSource.Factory(
                        this, new DefaultHttpDataSource.Factory().setUserAgent(strArr[Math.min(this.currentDataSourceAttempt, strArr.length - 1)])
                        .setAllowCrossProtocolRedirects(true).setConnectTimeoutMs(30000).setReadTimeoutMs(45000)),
                        new DefaultExtractorsFactory().setConstantBitrateSeekingEnabled(true)))
                .setLoadControl(new DefaultLoadControl.Builder()
                        .setBufferDurationsMs(3000, 30000,
                                1500, DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_MS).build()).build();
        this.player = build;
        this.playerView.setPlayer(build);
        this.player.addListener(this);
        this.layoutError.setVisibility(View.VISIBLE);
        this.playerBuffering.setVisibility(View.GONE);
        MediaItem.Builder uri = new MediaItem.Builder().setUri(Uri.parse(trim));
        String lowerCase = trim.toLowerCase();
        if (lowerCase.contains(".m3u8") || lowerCase.contains("/hls/")) {
            uri.setMimeType(MimeTypes.APPLICATION_M3U8);
        } else if (lowerCase.endsWith(".mp4")) {
            uri.setMimeType(MimeTypes.VIDEO_MP4);
        }
        this.player.setMediaItem(uri.build());
        this.player.prepare();
        long j = this.initialResumePosition;
        if (j > 0) {
            this.player.seekTo(j);
        }
        this.player.setPlayWhenReady(true);
        scheduleControlsHide();
//        AnalyticsTracker.logPlayback("VodPlayerActivity", this.title, "vod", trim, this.subtitle);
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
//            AnalyticsTracker.logPlayerControl("VodPlayerActivity", "play_pause", "pause");
        } else {
            exoPlayer2.play();
            this.btnPlayPause.setImageResource(R.drawable.ic_pause);
//            AnalyticsTracker.logPlayerControl("VodPlayerActivity", "play_pause", "play");
        }
        scheduleControlsHide();
    }

    private void seekRelative(long j) {
        ExoPlayer exoPlayer = this.player;
        if (exoPlayer == null) {
            return;
        }
        this.player.seekTo(Math.max(0L, Math.min(this.player.getDuration(), exoPlayer.getCurrentPosition() + j)));
        scheduleControlsHide();
//        AnalyticsTracker.logPlayerControl("VodPlayerActivity", "seek", String.valueOf(j));
    }

    private void showSpeedDialog() {
        new AlertDialog.Builder(this, R.style.CustomDialogTheme).setTitle(R.string.vod_speed_title).setSingleChoiceItems(this.speedNames, this.currentSpeedIndex, new DialogInterface.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.vod.VodPlayerActivity$$ExternalSyntheticLambda3
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                VodPlayerActivity.this.lambda$showSpeedDialog$11(dialogInterface, i);
            }
        }).setNegativeButton(R.string.cancel, (DialogInterface.OnClickListener) null).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showSpeedDialog$11(DialogInterface dialogInterface, int i) {
        this.currentSpeedIndex = i;
        ExoPlayer exoPlayer = this.player;
        if (exoPlayer != null) {
            exoPlayer.setPlaybackParameters(new PlaybackParameters(this.speeds[i]));
        }
        dialogInterface.dismiss();
        Toast.makeText(this, getString(R.string.vod_speed_format, new Object[]{this.speedNames[i]}),Toast.LENGTH_SHORT ).show();
//        AnalyticsTracker.logPlayerControl("VodPlayerActivity", "playback_speed", this.speedNames[i]);
        scheduleControlsHide();
    }

    @OptIn(markerClass = UnstableApi.class)
    private void cycleAspectRatio() {
        int[] iArr = this.resizeModes;
        int length = (this.currentAspectMode + 1) % iArr.length;
        this.currentAspectMode = length;
        this.playerView.setResizeMode(iArr[length]);
        String string = getString(this.resizeModeNameResIds[this.currentAspectMode]);
        Toast.makeText(this, getString(R.string.msg_aspect_ratio, new Object[]{string}), Toast.LENGTH_SHORT).show();
//        AnalyticsTracker.logPlayerControl("VodPlayerActivity", "aspect_ratio", string);
        scheduleControlsHide();
    }

    @UnstableApi
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
                        str = str2 != null ? str2 : "Audio " + (arrayList.size() + 1);
                    }
                    arrayList.add(str);
                    arrayList2.add(new TrackSelectionOverride(mediaTrackGroup, i));
                }
            }
        }
        if (arrayList.isEmpty()) {
            Toast.makeText(this, R.string.msg_no_audio_tracks, Toast.LENGTH_SHORT).show();
        } else {
            new AlertDialog.Builder(this, R.style.CustomDialogTheme).setTitle(R.string.audio_tracks).setItems((CharSequence[]) arrayList.toArray(new String[0]), new DialogInterface.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.vod.VodPlayerActivity$$ExternalSyntheticLambda4
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i2) {
                    VodPlayerActivity.this.lambda$showAudioTracksDialog$12(arrayList, arrayList2, dialogInterface, i2);
                }
            }).setNegativeButton(R.string.cancel, (DialogInterface.OnClickListener) null).show();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showAudioTracksDialog$12(List list, List list2, DialogInterface dialogInterface, int i) {
//        AnalyticsTracker.logPlayerControl("VodPlayerActivity", "audio_track", (String) list.get(i));
        ExoPlayer exoPlayer = this.player;
        exoPlayer.setTrackSelectionParameters(exoPlayer.getTrackSelectionParameters().buildUpon().setOverrideForType((TrackSelectionOverride) list2.get(i)).build());
    }

    private void enterPipMode() {
        if (Build.VERSION.SDK_INT >= 26) {
            hideControls();
            PictureInPictureParams.Builder builder = new PictureInPictureParams.Builder();
            builder.setAspectRatio(new Rational(16, 9));
            enterPictureInPictureMode(builder.build());
//            AnalyticsTracker.logPlayerControl("VodPlayerActivity", "pip_mode", "entered");
            return;
        }
        Toast.makeText(this, R.string.msg_pip_requires_android_8, Toast.LENGTH_SHORT).show();
    }

    private void toggleOrientation() {
        if (getResources().getConfiguration().orientation == 2) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            updateOrientationLayout(1);
//            AnalyticsTracker.logPlayerControl("VodPlayerActivity", "orientation", "portrait");
            return;
        }
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        updateOrientationLayout(2);
//        AnalyticsTracker.logPlayerControl("VodPlayerActivity", "orientation", "landscape");
    }

    private void startProgressUpdater() {
        this.progressHandler.postDelayed(new Runnable() { // from class: com.iptv.online.player.live.tv.smart.ui.vod.VodPlayerActivity.2
            @Override // java.lang.Runnable
            public void run() {
                if (VodPlayerActivity.this.player != null && VodPlayerActivity.this.player.isPlaying() && !VodPlayerActivity.this.isUserSeeking) {
                    long currentPosition = VodPlayerActivity.this.player.getCurrentPosition();
                    long duration = VodPlayerActivity.this.player.getDuration();
                    if (duration > 0) {
                        VodPlayerActivity.this.seekBar.setMax((int) duration);
                        VodPlayerActivity.this.seekBar.setProgress((int) currentPosition);
                        VodPlayerActivity.this.tvCurrentTime.setText(VodPlayerActivity.this.formatTime(currentPosition));
                        VodPlayerActivity.this.tvTotalTime.setText(VodPlayerActivity.this.formatTime(duration));
                    }
                    VodPlayerActivity.this.saveWatchProgress();
                }
                VodPlayerActivity.this.progressHandler.postDelayed(this, 1000L);
            }
        }, 1000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    @OptIn(markerClass = UnstableApi.class)
    public void saveWatchProgress() {
        ExoPlayer exoPlayer = this.player;
        if (exoPlayer == null || this.contentId == null) {
            return;
        }
        long currentPosition = exoPlayer.getCurrentPosition();
        long duration = this.player.getDuration();
        if (duration <= 5000 || currentPosition <= ExoPlayer.DEFAULT_DETACH_SURFACE_TIMEOUT_MS) {
            return;
        }
        this.storageManager.saveWatchProgress(new WatchProgress(this.contentId, this.title, this.posterUrl, this.streamUrl, currentPosition, duration));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String formatTime(long j) {
        if (j <= 0) {
            return "00:00";
        }
        long j2 = j / 1000;
        long j3 = j2 % 60;
        long j4 = (j2 / 60) % 60;
        long j5 = j2 / 3600;
        if (j5 > 0) {
            return String.format(Locale.US, "%d:%02d:%02d", Long.valueOf(j5), Long.valueOf(j4), Long.valueOf(j3));
        }
        return String.format(Locale.US, "%02d:%02d", Long.valueOf(j4), Long.valueOf(j3));
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
        if (this.layoutControls.getVisibility() == View.VISIBLE) {
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

    /* JADX INFO: Access modifiers changed from: private */
    public void scheduleControlsHide() {
        this.hideHandler.removeCallbacks(this.hideControlsRunnable);
        this.hideHandler.postDelayed(this.hideControlsRunnable, 5000L);
    }

    private void showError(String str) {
        this.playerBuffering.setVisibility(View.GONE);
        this.layoutError.setVisibility(View.VISIBLE);
        TextView textView = this.tvPlayerErrorMsg;
        if (str == null) {
            str = "Playback error";
        }
        textView.setText(str);
    }

    @Override // androidx.media3.common.Player.Listener
    public void onPlaybackStateChanged(int i) {
        if (i == 2) {
            this.playerBuffering.setVisibility(View.VISIBLE);
            this.layoutError.setVisibility(View.GONE);
        } else if (i != 3) {
            if (i == 4) {
                this.playerBuffering.setVisibility(View.GONE);
                String str = this.contentId;
                if (str != null) {
                    this.storageManager.deleteWatchProgress(str);
                }
            }
        } else {
            this.currentDataSourceAttempt = 0;
            this.playerBuffering.setVisibility(View.GONE);
            this.layoutError.setVisibility(View.GONE);
            this.btnPlayPause.setImageResource(this.player.isPlaying() ? R.drawable.ic_pause : R.drawable.ic_play);
            long duration = this.player.getDuration();
            if (duration > 0) {
                this.seekBar.setMax((int) duration);
                this.tvTotalTime.setText(formatTime(duration));
            }
        }
    }

    @Override // androidx.media3.common.Player.Listener
    public void onIsPlayingChanged(boolean z) {
        this.btnPlayPause.setImageResource(z ? R.drawable.ic_pause : R.drawable.ic_play);
    }

    @Override // androidx.media3.common.Player.Listener
    public void onPlayerError(PlaybackException playbackException) {
        int i = this.currentDataSourceAttempt;
        if (i >= this.userAgentFallbacks.length - 1) {
            showError("Stream error: " + playbackException.getErrorCodeName() + "\n" + (playbackException.getMessage() != null ? playbackException.getMessage() : ""));
            return;
        }
        this.currentDataSourceAttempt = i + 1;
        setupPlayer();
    }

    @Override // androidx.appcompat.app.AppCompatActivity, android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i != 66) {
            if (i != 85 && i != 126 && i != 127) {
                switch (i) {
                    case 21:
                        seekRelative(-10000L);
                        return true;
                    case 22:
                        seekRelative(10000L);
                        return true;
                    case 23:
                        break;
                    default:
                        return super.onKeyDown(i, keyEvent);
                }
            } else {
                togglePlayPause();
                return true;
            }
        }
        if (this.layoutControls.getVisibility() != View.VISIBLE) {
            showControls();
            return true;
        }
        return super.onKeyDown(i, keyEvent);
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.activity.ComponentActivity, android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        hideSystemUI();
        updateOrientationLayout(configuration.orientation);
    }

    private void updateOrientationLayout(int i) {
        int i2;
        View view = this.layoutVodAdContainer;
        if (i == 2) {
            if (view != null) {
                view.setVisibility(View.GONE);
            }
            LinearLayout linearLayout = this.llAdsView;
            if (linearLayout != null) {
                linearLayout.setVisibility(View.GONE);
            }
            View view2 = this.vodPlayerBottomBar;
            if (view2 == null || !(view2.getLayoutParams() instanceof RelativeLayout.LayoutParams)) {
                return;
            }
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.vodPlayerBottomBar.getLayoutParams();
            layoutParams.bottomMargin = 0;
            this.vodPlayerBottomBar.setLayoutParams(layoutParams);
            return;
        }
        if (view != null) {
            view.setVisibility(View.VISIBLE);
        }
        LinearLayout linearLayout2 = this.llAdsView;
        if (linearLayout2 != null) {
            linearLayout2.setVisibility(View.VISIBLE);
            loadNativeAd();
        }
        View view3 = this.vodPlayerBottomBar;
        if (view3 == null || !(view3.getLayoutParams() instanceof RelativeLayout.LayoutParams)) {
            return;
        }
        RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) this.vodPlayerBottomBar.getLayoutParams();
        View view4 = this.layoutVodAdContainer;
        if (view4 != null && view4.getHeight() > 0) {
            i2 = this.layoutVodAdContainer.getHeight();
        } else {
            i2 = (int) (getResources().getDisplayMetrics().density * 140.0f);
        }
        layoutParams2.bottomMargin = i2;
        this.vodPlayerBottomBar.setLayoutParams(layoutParams2);
    }

    private void loadNativeAd() {
        LinearLayout linearLayout = this.llAdsView;
        if (linearLayout == null || linearLayout.getChildCount() != 0) {
            return;
        }
//        NativeLoader.showNativeAds(this, this.llAdsView, NativeType.BIG_NATIVE);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.adsUtils.custom.CustomActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onPause() {
        super.onPause();
        saveWatchProgress();
        if (this.player == null || isInPictureInPictureMode()) {
            return;
        }
        this.player.pause();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onStop() {
        super.onStop();
        saveWatchProgress();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.adsUtils.custom.CustomActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        saveWatchProgress();
        this.progressHandler.removeCallbacksAndMessages(null);
        this.hideHandler.removeCallbacks(this.hideControlsRunnable);
        ExoPlayer exoPlayer = this.player;
        if (exoPlayer != null) {
            exoPlayer.release();
            this.player = null;
        }
    }
}

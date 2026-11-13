package com.iptv.online.player.live.tv.smart.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.iptv.online.player.live.tv.smart.R;
import com.iptv.online.player.live.tv.smart.data.local.PlaylistStorageManager;
import com.iptv.online.player.live.tv.smart.data.model.PlaylistSource;
@SuppressWarnings("all")

/* loaded from: classes5.dex */
public class AddPlaylistDialog extends Dialog {
    private MaterialButton btnCancel;
    private MaterialButton btnQuickDemo;
    private MaterialButton btnSave;
    private EditText etM3UUrl;
    private EditText etPlaylistName;
    private EditText etXtreamPassword;
    private EditText etXtreamServer;
    private EditText etXtreamUser;
    private boolean isM3UType;
    private LinearLayout layoutM3UFields;
    private LinearLayout layoutXtreamFields;
    private final OnPlaylistAddedListener listener;
    private final PlaylistStorageManager storageManager;
    private MaterialButtonToggleGroup toggleSourceType;

    /* loaded from: classes5.dex */
    public interface OnPlaylistAddedListener {
        void onPlaylistAdded(PlaylistSource playlistSource);
    }

    public AddPlaylistDialog(Context context, PlaylistStorageManager playlistStorageManager, OnPlaylistAddedListener onPlaylistAddedListener) {
        super(context, R.style.CustomDialogTheme);
        this.isM3UType = true;
        this.storageManager = playlistStorageManager;
        this.listener = onPlaylistAddedListener;
    }

    @Override // android.app.Dialog
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        setContentView(R.layout.dialog_add_playlist);
        if (getWindow() != null) {
            getWindow().setLayout(-1, -2);
        }
        initViews();
        setupListeners();
    }

    private void initViews() {
        this.toggleSourceType = (MaterialButtonToggleGroup) findViewById(R.id.toggleSourceType);
        this.layoutM3UFields = (LinearLayout) findViewById(R.id.layoutM3UFields);
        this.layoutXtreamFields = (LinearLayout) findViewById(R.id.layoutXtreamFields);
        this.etPlaylistName = (EditText) findViewById(R.id.etPlaylistName);
        this.etM3UUrl = (EditText) findViewById(R.id.etM3UUrl);
        this.etXtreamServer = (EditText) findViewById(R.id.etXtreamServer);
        this.etXtreamUser = (EditText) findViewById(R.id.etXtreamUser);
        this.etXtreamPassword = (EditText) findViewById(R.id.etXtreamPassword);
        this.btnQuickDemo = (MaterialButton) findViewById(R.id.btnQuickDemo);
        this.btnCancel = (MaterialButton) findViewById(R.id.btnCancelAdd);
        this.btnSave = (MaterialButton) findViewById(R.id.btnSavePlaylist);
    }

    private void setupListeners() {
        this.toggleSourceType.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() { // from class: com.iptv.online.player.live.tv.smart.ui.dialogs.AddPlaylistDialog$$ExternalSyntheticLambda0
            @Override // com.google.android.material.button.MaterialButtonToggleGroup.OnButtonCheckedListener
            public final void onButtonChecked(MaterialButtonToggleGroup materialButtonToggleGroup, int i, boolean z) {
                AddPlaylistDialog.this.lambda$setupListeners$0(materialButtonToggleGroup, i, z);
            }
        });
        this.btnQuickDemo.setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.dialogs.AddPlaylistDialog$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                AddPlaylistDialog.this.lambda$setupListeners$1(view);
            }
        });
        this.btnCancel.setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.dialogs.AddPlaylistDialog$$ExternalSyntheticLambda2
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                AddPlaylistDialog.this.lambda$setupListeners$2(view);
            }
        });
        this.btnSave.setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.dialogs.AddPlaylistDialog$$ExternalSyntheticLambda3
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                AddPlaylistDialog.this.lambda$setupListeners$3(view);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupListeners$0(MaterialButtonToggleGroup materialButtonToggleGroup, int i, boolean z) {
        if (z) {
            if (i == R.id.btnTypeM3U) {
                this.isM3UType = true;
                this.layoutM3UFields.setVisibility(View.VISIBLE);
                this.layoutXtreamFields.setVisibility(View.GONE);
                return;
            }
            this.isM3UType = false;
            this.layoutM3UFields.setVisibility(View.GONE);
            this.layoutXtreamFields.setVisibility(View.VISIBLE);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupListeners$1(View view) {
        PlaylistSource createDemoSource = PlaylistSource.createDemoSource();
        this.storageManager.addOrUpdateSource(createDemoSource);
        this.storageManager.setActiveSourceId(createDemoSource.getId());
        OnPlaylistAddedListener onPlaylistAddedListener = this.listener;
        if (onPlaylistAddedListener != null) {
            onPlaylistAddedListener.onPlaylistAdded(createDemoSource);
        }
        dismiss();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupListeners$2(View view) {
        dismiss();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupListeners$3(View view) {
        PlaylistSource createXtreamSource;
        String trim = this.etPlaylistName.getText().toString().trim();
        if (trim.isEmpty()) {
            trim = this.isM3UType ? "M3U Playlist" : "Xtream Account";
        }
        if (this.isM3UType) {
            String trim2 = this.etM3UUrl.getText().toString().trim();
            if (trim2.isEmpty()) {
                Toast.makeText(getContext(), getContext().getString(R.string.err_enter_valid_m3u_url), 0).show();
                return;
            }
            createXtreamSource = PlaylistSource.createM3USource(trim, trim2);
        } else {
            String trim3 = this.etXtreamServer.getText().toString().trim();
            String trim4 = this.etXtreamUser.getText().toString().trim();
            String trim5 = this.etXtreamPassword.getText().toString().trim();
            if (trim3.isEmpty() || trim4.isEmpty() || trim5.isEmpty()) {
                Toast.makeText(getContext(), getContext().getString(R.string.err_fill_xtream_credentials), 0).show();
                return;
            }
            createXtreamSource = PlaylistSource.createXtreamSource(trim, trim3, trim4, trim5);
        }
        this.storageManager.addOrUpdateSource(createXtreamSource);
        this.storageManager.setActiveSourceId(createXtreamSource.getId());
        OnPlaylistAddedListener onPlaylistAddedListener = this.listener;
        if (onPlaylistAddedListener != null) {
            onPlaylistAddedListener.onPlaylistAdded(createXtreamSource);
        }
        dismiss();
    }
}

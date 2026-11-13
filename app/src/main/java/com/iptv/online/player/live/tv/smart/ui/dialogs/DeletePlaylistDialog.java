package com.iptv.online.player.live.tv.smart.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.iptv.online.player.live.tv.smart.R;
import com.iptv.online.player.live.tv.smart.data.model.PlaylistSource;

/* loaded from: classes5.dex */
public class DeletePlaylistDialog extends Dialog {
    private final OnDeleteConfirmListener listener;
    private final PlaylistSource source;

    /* loaded from: classes5.dex */
    public interface OnDeleteConfirmListener {
        void onDeleteConfirmed();
    }

    public DeletePlaylistDialog(Context context, PlaylistSource playlistSource, OnDeleteConfirmListener onDeleteConfirmListener) {
        super(context, R.style.CustomDialogTheme);
        this.source = playlistSource;
        this.listener = onDeleteConfirmListener;
    }

    @Override // android.app.Dialog
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        setContentView(R.layout.dialog_delete_playlist);
        if (getWindow() != null) {
            getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        initViews();
    }

    @Override // android.app.Dialog
    protected void onStart() {
        super.onStart();
        if (getWindow() != null) {
            getWindow().setLayout(Math.min((int) (getContext().getResources().getDisplayMetrics().widthPixels * 0.9f), (int) (getContext().getResources().getDisplayMetrics().density * 420.0f)), -2);
        }
    }

    private void initViews() {
        String str;
        ImageView imageView = (ImageView) findViewById(R.id.ivDeleteSourceIcon);
        TextView textView = (TextView) findViewById(R.id.tvDeleteSourceName);
        TextView textView2 = (TextView) findViewById(R.id.tvDeleteSourceInfo);
        PlaylistSource playlistSource = this.source;
        if (playlistSource != null) {
            textView.setText(playlistSource.getName());
            if (this.source.getType() == PlaylistSource.SourceType.LOCAL_FILE) {
                imageView.setImageResource(R.drawable.ic_doc);
                str = "Local File";
            } else if (this.source.getType() == PlaylistSource.SourceType.XTREAM_CODES) {
                imageView.setImageResource(R.drawable.ic_server);
                str = "Xtream Codes";
            } else if (this.source.getType() == PlaylistSource.SourceType.SINGLE_STREAM) {
                imageView.setImageResource(R.drawable.ic_play);
                str = "Single Stream";
            } else {
                imageView.setImageResource(R.drawable.ic_link);
                str = "M3U Playlist";
            }
            int channelCount = this.source.getChannelCount();
            textView2.setText(str + " • " + channelCount + (channelCount == 1 ? " channel" : " channels"));
        }
        findViewById(R.id.btnCancelDelete).setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.dialogs.DeletePlaylistDialog$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                DeletePlaylistDialog.this.lambda$initViews$0(view);
            }
        });
        findViewById(R.id.btnConfirmDelete).setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.dialogs.DeletePlaylistDialog$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                DeletePlaylistDialog.this.lambda$initViews$1(view);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initViews$0(View view) {
        dismiss();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initViews$1(View view) {
        dismiss();
        OnDeleteConfirmListener onDeleteConfirmListener = this.listener;
        if (onDeleteConfirmListener != null) {
            onDeleteConfirmListener.onDeleteConfirmed();
        }
    }
}

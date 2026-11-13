package com.iptv.online.player.live.tv.smart.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.iptv.online.player.live.tv.smart.R;
import com.iptv.online.player.live.tv.smart.data.model.PlaylistSource;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes5.dex */
public class PlaylistManageAdapter extends RecyclerView.Adapter<PlaylistManageAdapter.ManageViewHolder> {
    private String activeSourceId;
    private final OnPlaylistActionCallback callback;
    private List<PlaylistSource> sourceList = new ArrayList();

    /* loaded from: classes5.dex */
    public interface OnPlaylistActionCallback {
        void onDeletePlaylist(PlaylistSource playlistSource);

        void onSelectPlaylist(PlaylistSource playlistSource);
    }

    public PlaylistManageAdapter(OnPlaylistActionCallback onPlaylistActionCallback) {
        this.callback = onPlaylistActionCallback;
    }

    public void setSources(List<PlaylistSource> list, String str) {
        this.sourceList = new ArrayList(list);
        this.activeSourceId = str;
        notifyDataSetChanged();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public ManageViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ManageViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_playlist_manage, viewGroup, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(ManageViewHolder manageViewHolder, int i) {
        String str;
        final PlaylistSource playlistSource = this.sourceList.get(i);
        manageViewHolder.tvName.setText(playlistSource.getName());
        if (playlistSource.getType() == PlaylistSource.SourceType.XTREAM_CODES) {
            str = "Xtream Codes • " + (playlistSource.getChannelCount() > 0 ? playlistSource.getChannelCount() + " channels" : "Live Streams");
            manageViewHolder.ivTypeIcon.setImageResource(R.drawable.ic_server);
        } else if (playlistSource.getType() == PlaylistSource.SourceType.DEMO) {
            str = "Free Legal Demo • " + (playlistSource.getChannelCount() > 0 ? playlistSource.getChannelCount() + " channels" : "Auto");
            manageViewHolder.ivTypeIcon.setImageResource(R.drawable.ic_tv);
        } else {
            str = "M3U URL • " + (playlistSource.getChannelCount() > 0 ? playlistSource.getChannelCount() + " channels" : "Remote Playlist");
            manageViewHolder.ivTypeIcon.setImageResource(R.drawable.ic_link);
        }
        manageViewHolder.tvInfo.setText(str);
        boolean equals = playlistSource.getId().equals(this.activeSourceId);
        manageViewHolder.ivActive.setVisibility(equals ? View.VISIBLE : View.GONE);
        manageViewHolder.itemView.setSelected(equals);
        manageViewHolder.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.adapters.PlaylistManageAdapter$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                PlaylistManageAdapter.this.lambda$onBindViewHolder$0(playlistSource, view);
            }
        });
        manageViewHolder.btnDelete.setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.adapters.PlaylistManageAdapter$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                PlaylistManageAdapter.this.lambda$onBindViewHolder$1(playlistSource, view);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onBindViewHolder$0(PlaylistSource playlistSource, View view) {
        OnPlaylistActionCallback onPlaylistActionCallback = this.callback;
        if (onPlaylistActionCallback != null) {
            onPlaylistActionCallback.onSelectPlaylist(playlistSource);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onBindViewHolder$1(PlaylistSource playlistSource, View view) {
        OnPlaylistActionCallback onPlaylistActionCallback = this.callback;
        if (onPlaylistActionCallback != null) {
            onPlaylistActionCallback.onDeletePlaylist(playlistSource);
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.sourceList.size();
    }

    /* loaded from: classes5.dex */
    public static class ManageViewHolder extends RecyclerView.ViewHolder {
        ImageButton btnDelete;
        ImageView ivActive;
        ImageView ivTypeIcon;
        TextView tvInfo;
        TextView tvName;

        public ManageViewHolder(View view) {
            super(view);
            this.ivTypeIcon = (ImageView) view.findViewById(R.id.ivSourceTypeIcon);
            this.tvName = (TextView) view.findViewById(R.id.tvSourceName);
            this.tvInfo = (TextView) view.findViewById(R.id.tvSourceInfo);
            this.ivActive = (ImageView) view.findViewById(R.id.ivActiveSourceCheck);
            this.btnDelete = (ImageButton) view.findViewById(R.id.btnDeleteSource);
        }
    }
}

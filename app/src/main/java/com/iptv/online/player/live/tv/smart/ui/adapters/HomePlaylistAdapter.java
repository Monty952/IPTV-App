package com.iptv.online.player.live.tv.smart.ui.adapters;

import android.content.Context;
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
public class HomePlaylistAdapter extends RecyclerView.Adapter<HomePlaylistAdapter.PlaylistViewHolder> {
    private final Context context;
    private final OnHomePlaylistClickListener listener;
    private List<PlaylistSource> playlistList = new ArrayList();

    /* loaded from: classes5.dex */
    public interface OnHomePlaylistClickListener {
        void onPlaylistClick(PlaylistSource playlistSource);

        void onPlaylistDelete(PlaylistSource playlistSource);
    }

    public HomePlaylistAdapter(Context context, OnHomePlaylistClickListener onHomePlaylistClickListener) {
        this.context = context;
        this.listener = onHomePlaylistClickListener;
    }

    public void setPlaylists(List<PlaylistSource> list) {
        this.playlistList = new ArrayList(list);
        notifyDataSetChanged();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public PlaylistViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new PlaylistViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_playlist_card, viewGroup, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(PlaylistViewHolder playlistViewHolder, int i) {
        String str;
        final PlaylistSource playlistSource = this.playlistList.get(i);
        playlistViewHolder.tvName.setText(playlistSource.getName());
        if (playlistSource.getType() == PlaylistSource.SourceType.LOCAL_FILE) {
            str = "Local File • " + (playlistSource.getChannelCount() > 0 ? playlistSource.getChannelCount() + " Channels" : "M3U File");
            playlistViewHolder.ivIcon.setImageResource(R.drawable.ic_cloud_upload);
        } else if (playlistSource.getType() == PlaylistSource.SourceType.XTREAM_CODES) {
            str = "Xtream Codes API • " + (playlistSource.getChannelCount() > 0 ? playlistSource.getChannelCount() + " Channels" : "Live Streams");
            playlistViewHolder.ivIcon.setImageResource(R.drawable.ic_server);
        } else if (playlistSource.getType() == PlaylistSource.SourceType.SINGLE_STREAM) {
            playlistViewHolder.ivIcon.setImageResource(R.drawable.ic_single_play);
            str = "Single Stream • Live Video";
        } else if (playlistSource.getType() == PlaylistSource.SourceType.DEMO) {
            str = "Legal Demo Channels • " + (playlistSource.getChannelCount() > 0 ? playlistSource.getChannelCount() + " Channels" : "Free Live TV");
            playlistViewHolder.ivIcon.setImageResource(R.drawable.ic_tv);
        } else {
            str = "M3U Playlist URL • " + (playlistSource.getChannelCount() > 0 ? playlistSource.getChannelCount() + " Channels" : "Remote M3U");
            playlistViewHolder.ivIcon.setImageResource(R.drawable.ic_link);
        }
        playlistViewHolder.tvDetails.setText(str);
        playlistViewHolder.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.adapters.HomePlaylistAdapter$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                HomePlaylistAdapter.this.lambda$onBindViewHolder$0(playlistSource, view);
            }
        });
        playlistViewHolder.btnMenu.setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.adapters.HomePlaylistAdapter$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                HomePlaylistAdapter.this.lambda$onBindViewHolder$1(playlistSource, view);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onBindViewHolder$0(PlaylistSource playlistSource, View view) {
        OnHomePlaylistClickListener onHomePlaylistClickListener = this.listener;
        if (onHomePlaylistClickListener != null) {
            onHomePlaylistClickListener.onPlaylistClick(playlistSource);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onBindViewHolder$1(PlaylistSource playlistSource, View view) {
        OnHomePlaylistClickListener onHomePlaylistClickListener = this.listener;
        if (onHomePlaylistClickListener != null) {
            onHomePlaylistClickListener.onPlaylistDelete(playlistSource);
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.playlistList.size();
    }

    /* loaded from: classes5.dex */
    public static class PlaylistViewHolder extends RecyclerView.ViewHolder {
        ImageButton btnMenu;
        ImageView ivIcon;
        TextView tvDetails;
        TextView tvName;

        public PlaylistViewHolder(View view) {
            super(view);
            this.ivIcon = (ImageView) view.findViewById(R.id.ivPlaylistCardIcon);
            this.tvName = (TextView) view.findViewById(R.id.tvPlaylistCardName);
            this.tvDetails = (TextView) view.findViewById(R.id.tvPlaylistCardDetails);
            this.btnMenu = (ImageButton) view.findViewById(R.id.btnPlaylistCardMenu);
        }
    }
}

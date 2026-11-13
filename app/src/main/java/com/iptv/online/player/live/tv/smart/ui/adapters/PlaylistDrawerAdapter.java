package com.iptv.online.player.live.tv.smart.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.iptv.online.player.live.tv.smart.R;
import com.iptv.online.player.live.tv.smart.data.model.PlaylistItem;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes5.dex */
public class PlaylistDrawerAdapter extends RecyclerView.Adapter<PlaylistDrawerAdapter.DrawerViewHolder> {
    private List<PlaylistItem> channelList = new ArrayList();
    private List<PlaylistItem> channelListFull = new ArrayList();
    private final Context context;
    private String currentPlayingId;
    private final OnDrawerChannelClickListener listener;

    /* loaded from: classes5.dex */
    public interface OnDrawerChannelClickListener {
        void onChannelClick(PlaylistItem playlistItem, int i);
    }

    public PlaylistDrawerAdapter(Context context, OnDrawerChannelClickListener onDrawerChannelClickListener) {
        this.context = context;
        this.listener = onDrawerChannelClickListener;
    }

    public void setChannels(List<PlaylistItem> list, String str) {
        this.channelList = new ArrayList(list);
        this.channelListFull = new ArrayList(list);
        this.currentPlayingId = str;
        notifyDataSetChanged();
    }

    public void setCurrentPlayingId(String str) {
        this.currentPlayingId = str;
        notifyDataSetChanged();
    }

    public void filter(CharSequence charSequence) {
        if (charSequence == null || charSequence.toString().trim().isEmpty()) {
            this.channelList = new ArrayList(this.channelListFull);
        } else {
            String trim = charSequence.toString().toLowerCase().trim();
            ArrayList arrayList = new ArrayList();
            for (PlaylistItem playlistItem : this.channelListFull) {
                if (playlistItem.getName().toLowerCase().contains(trim) || playlistItem.getGroupTitle().toLowerCase().contains(trim)) {
                    arrayList.add(playlistItem);
                }
            }
            this.channelList = arrayList;
        }
        notifyDataSetChanged();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public DrawerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new DrawerViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_player_channel, viewGroup, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(final DrawerViewHolder drawerViewHolder, int i) {
        final PlaylistItem playlistItem = this.channelList.get(i);
        drawerViewHolder.tvName.setText(playlistItem.getName());
        drawerViewHolder.tvCategory.setText(playlistItem.getGroupTitle());
        boolean equals = playlistItem.getId().equals(this.currentPlayingId);
//        drawerViewHolder.ivNowPlaying.setVisibility(equals ? 0 : 8);
        drawerViewHolder.ivNowPlaying.setVisibility(equals ? View.VISIBLE : View.GONE);
        drawerViewHolder.itemView.setSelected(equals);
        if (playlistItem.getLogoUrl() != null && !playlistItem.getLogoUrl().trim().isEmpty()) {
            Glide.with(this.context).load(playlistItem.getLogoUrl()).placeholder(R.drawable.ic_channel_placeholder).error(R.drawable.ic_channel_placeholder).into(drawerViewHolder.ivLogo);
        } else {
            drawerViewHolder.ivLogo.setImageResource(R.drawable.ic_channel_placeholder);
        }
        drawerViewHolder.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.adapters.PlaylistDrawerAdapter$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                PlaylistDrawerAdapter.this.lambda$onBindViewHolder$0(playlistItem, drawerViewHolder, view);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onBindViewHolder$0(PlaylistItem playlistItem, DrawerViewHolder drawerViewHolder, View view) {
        OnDrawerChannelClickListener onDrawerChannelClickListener = this.listener;
        if (onDrawerChannelClickListener != null) {
            onDrawerChannelClickListener.onChannelClick(playlistItem, drawerViewHolder.getAdapterPosition());
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.channelList.size();
    }

    /* loaded from: classes5.dex */
    public static class DrawerViewHolder extends RecyclerView.ViewHolder {
        ImageView ivLogo;
        ImageView ivNowPlaying;
        TextView tvCategory;
        TextView tvName;

        public DrawerViewHolder(View view) {
            super(view);
            this.ivLogo = (ImageView) view.findViewById(R.id.ivPlayerChannelLogo);
            this.tvName = (TextView) view.findViewById(R.id.tvPlayerChannelName);
            this.tvCategory = (TextView) view.findViewById(R.id.tvPlayerChannelCategory);
            this.ivNowPlaying = (ImageView) view.findViewById(R.id.ivNowPlaying);
        }
    }
}

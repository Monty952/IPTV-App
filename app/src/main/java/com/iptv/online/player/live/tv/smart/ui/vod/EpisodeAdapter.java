package com.iptv.online.player.live.tv.smart.ui.vod;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.iptv.online.player.live.tv.smart.R;
import com.iptv.online.player.live.tv.smart.data.model.EpisodeItem;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes5.dex */
public class EpisodeAdapter extends RecyclerView.Adapter<EpisodeAdapter.ViewHolder> {
    private final Context context;
    private List<EpisodeItem> episodes = new ArrayList();
    private final OnEpisodeClickListener listener;

    /* loaded from: classes5.dex */
    public interface OnEpisodeClickListener {
        void onEpisodeClick(EpisodeItem episodeItem, int i);
    }

    public EpisodeAdapter(Context context, OnEpisodeClickListener onEpisodeClickListener) {
        this.context = context;
        this.listener = onEpisodeClickListener;
    }

    public void setEpisodes(List<EpisodeItem> list) {
        this.episodes = list != null ? new ArrayList(list) : new ArrayList();
        notifyDataSetChanged();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_episode, viewGroup, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        EpisodeItem episodeItem = this.episodes.get(i);
        viewHolder.tvTitle.setText(episodeItem.getEpisodeNum() + ". " + episodeItem.getTitle());
        viewHolder.tvPlot.setText(episodeItem.getPlot());
        viewHolder.tvDuration.setText(episodeItem.getDuration().isEmpty() ? "HD" : episodeItem.getDuration());
        if (episodeItem.getCoverUrl() != null && !episodeItem.getCoverUrl().isEmpty()) {
            Glide.with(this.context).load(episodeItem.getCoverUrl()).placeholder(R.drawable.ic_series).error(R.drawable.ic_series).diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop().into(viewHolder.ivThumb);
        } else {
            viewHolder.ivThumb.setImageResource(R.drawable.ic_series);
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.vod.EpisodeAdapter$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                EpisodeAdapter.this.lambda$onBindViewHolder$0(viewHolder, view);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onBindViewHolder$0(ViewHolder viewHolder, View view) {
        int adapterPosition;
        if (this.listener == null || (adapterPosition = viewHolder.getAdapterPosition()) < 0 || adapterPosition >= this.episodes.size()) {
            return;
        }
        this.listener.onEpisodeClick(this.episodes.get(adapterPosition), adapterPosition);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.episodes.size();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes5.dex */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivThumb;
        TextView tvDuration;
        TextView tvPlot;
        TextView tvTitle;

        ViewHolder(View view) {
            super(view);
            this.ivThumb = (ImageView) view.findViewById(R.id.ivEpisodeThumb);
            this.tvTitle = (TextView) view.findViewById(R.id.tvEpisodeNumberTitle);
            this.tvPlot = (TextView) view.findViewById(R.id.tvEpisodePlot);
            this.tvDuration = (TextView) view.findViewById(R.id.tvEpisodeDuration);
        }
    }
}

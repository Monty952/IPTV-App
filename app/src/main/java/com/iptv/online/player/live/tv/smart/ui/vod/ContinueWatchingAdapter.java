package com.iptv.online.player.live.tv.smart.ui.vod;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.button.MaterialButton;
import com.iptv.online.player.live.tv.smart.R;
import com.iptv.online.player.live.tv.smart.data.model.WatchProgress;
import java.util.ArrayList;
import java.util.List;
@SuppressWarnings("all")

/* loaded from: classes5.dex */
public class ContinueWatchingAdapter extends RecyclerView.Adapter<ContinueWatchingAdapter.ViewHolder> {
    private final Context context;
    private List<WatchProgress> items = new ArrayList();
    private final OnContinueWatchingListener listener;

    /* loaded from: classes5.dex */
    public interface OnContinueWatchingListener {
        void onDeleteClick(WatchProgress watchProgress, int i);

        void onResumeClick(WatchProgress watchProgress);
    }

    public static /* synthetic */ void $r8$lambda$Rr3dBf9P_MsxA7EZD9JetxUKT_Q(ContinueWatchingAdapter continueWatchingAdapter, WatchProgress watchProgress, View view) {
        continueWatchingAdapter.lambda$onBindViewHolder$1(watchProgress, view);
    }

    public static /* synthetic */ void $r8$lambda$ZoMPf9HQlQOvG2GfQ2oSDuQFtaw(ContinueWatchingAdapter continueWatchingAdapter, WatchProgress watchProgress, View view) {
        continueWatchingAdapter.lambda$onBindViewHolder$0(watchProgress, view);
    }

    public static /* synthetic */ void $r8$lambda$rwN90XcGiadU2TxZCjc57UypEhg(ContinueWatchingAdapter continueWatchingAdapter, ViewHolder viewHolder, View view) {
        continueWatchingAdapter.lambda$onBindViewHolder$2(viewHolder, view);
    }

    public ContinueWatchingAdapter(Context context, OnContinueWatchingListener onContinueWatchingListener) {
        this.context = context;
        this.listener = onContinueWatchingListener;
    }

    public void setItems(List<WatchProgress> list) {
        this.items = list != null ? new ArrayList(list) : new ArrayList();
        notifyDataSetChanged();
    }

    public void removeItem(int i) {
        if (i < 0 || i >= this.items.size()) {
            return;
        }
        this.items.remove(i);
        notifyItemRemoved(i);
        notifyItemRangeChanged(i, this.items.size());
    }

    public List<WatchProgress> getItems() {
        return this.items;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_continue_watching, viewGroup, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        final WatchProgress watchProgress = this.items.get(i);
        viewHolder.tvTitle.setText(watchProgress.getTitle());
        viewHolder.tvTimeRemaining.setText("Resume from " + watchProgress.getFormattedPosition() + " / " + watchProgress.getFormattedDuration());
        viewHolder.pbProgress.setProgress(watchProgress.getProgressPercentage());
        viewHolder.tvPercent.setText(watchProgress.getProgressPercentage() + "% watched");
        if (watchProgress.getPosterUrl() != null && !watchProgress.getPosterUrl().isEmpty()) {
            Glide.with(this.context).load(watchProgress.getPosterUrl()).placeholder(R.drawable.ic_cast_channels).error(R.drawable.ic_cast_channels).diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop().into(viewHolder.ivThumb);
        } else {
            viewHolder.ivThumb.setImageResource(R.drawable.ic_cast_channels);
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.vod.ContinueWatchingAdapter$$ExternalSyntheticLambda0
            {
            }

            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ContinueWatchingAdapter.$r8$lambda$ZoMPf9HQlQOvG2GfQ2oSDuQFtaw(ContinueWatchingAdapter.this, watchProgress, view);
            }
        });
        viewHolder.btnResume.setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.vod.ContinueWatchingAdapter$$ExternalSyntheticLambda1
            {

            }

            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ContinueWatchingAdapter.$r8$lambda$Rr3dBf9P_MsxA7EZD9JetxUKT_Q(ContinueWatchingAdapter.this, watchProgress, view);
            }
        });
        viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.vod.ContinueWatchingAdapter$$ExternalSyntheticLambda2
            {

            }

            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ContinueWatchingAdapter.$r8$lambda$rwN90XcGiadU2TxZCjc57UypEhg(ContinueWatchingAdapter.this, viewHolder, view);
            }
        });
    }

    public /* synthetic */ void lambda$onBindViewHolder$0(WatchProgress watchProgress, View view) {
        OnContinueWatchingListener onContinueWatchingListener = this.listener;
        if (onContinueWatchingListener != null) {
            onContinueWatchingListener.onResumeClick(watchProgress);
        }
    }

    public /* synthetic */ void lambda$onBindViewHolder$1(WatchProgress watchProgress, View view) {
        OnContinueWatchingListener onContinueWatchingListener = this.listener;
        if (onContinueWatchingListener != null) {
            onContinueWatchingListener.onResumeClick(watchProgress);
        }
    }

    public /* synthetic */ void lambda$onBindViewHolder$2(ViewHolder viewHolder, View view) {
        int adapterPosition;
        if (this.listener == null || (adapterPosition = viewHolder.getAdapterPosition()) < 0 || adapterPosition >= this.items.size()) {
            return;
        }
        this.listener.onDeleteClick(this.items.get(adapterPosition), adapterPosition);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.items.size();
    }

    /* loaded from: classes5.dex */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageButton btnDelete;
        MaterialButton btnResume;
        ImageView ivThumb;
        ProgressBar pbProgress;
        TextView tvPercent;
        TextView tvTimeRemaining;
        TextView tvTitle;

        ViewHolder(View view) {
            super(view);
            this.ivThumb = (ImageView) view.findViewById(R.id.ivContinueThumb);
            this.tvTitle = (TextView) view.findViewById(R.id.tvContinueTitle);
            this.tvTimeRemaining = (TextView) view.findViewById(R.id.tvContinueTimeRemaining);
            this.tvPercent = (TextView) view.findViewById(R.id.tvContinuePercent);
            this.pbProgress = (ProgressBar) view.findViewById(R.id.pbContinueProgress);
            this.btnDelete = (ImageButton) view.findViewById(R.id.btnContinueDelete);
            this.btnResume = (MaterialButton) view.findViewById(R.id.btnContinueResume);
        }
    }
}
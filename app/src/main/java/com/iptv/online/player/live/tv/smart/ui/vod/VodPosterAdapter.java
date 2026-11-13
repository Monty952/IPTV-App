package com.iptv.online.player.live.tv.smart.ui.vod;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
//import com.adsUtils.adsloader.nativeads.NativeLoader;
//import com.adsUtils.adsloader.nativeads.NativeType;
//import com.adsUtils.config.AdsConfig;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.iptv.online.player.live.tv.smart.R;
import com.iptv.online.player.live.tv.smart.data.local.PlaylistStorageManager;
import com.iptv.online.player.live.tv.smart.data.model.VodItem;
import com.iptv.online.player.live.tv.smart.data.model.WatchProgress;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes5.dex */
public class VodPosterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    public static final int VIEW_TYPE_AD = 1;
    public static final int VIEW_TYPE_ITEM = 0;
    private final Context context;
    private boolean isNativeRecycle;
    private final OnVodItemClickListener listener;
    private int nativePosition;
    private final PlaylistStorageManager storageManager;
    private List<VodItem> fullList = new ArrayList();
    private List<VodItem> filteredList = new ArrayList();

    /* loaded from: classes5.dex */
    public interface OnVodItemClickListener {
        void onVodItemClick(VodItem vodItem, int i);
    }

    public VodPosterAdapter(Context context, PlaylistStorageManager playlistStorageManager, OnVodItemClickListener onVodItemClickListener) {
        this.context = context;
        this.storageManager = playlistStorageManager;
        this.listener = onVodItemClickListener;
        updateAdConfig();
    }

    public void updateAdConfig() {
//        this.isNativeRecycle = AdsConfig.getNativeRecycle() && AdsConfig.getIsAdsOn();
//        this.nativePosition = AdsConfig.getNativePositionInt();
    }

    public void setItems(List<VodItem> list) {
        updateAdConfig();
        this.fullList = list != null ? new ArrayList(list) : new ArrayList();
        this.filteredList = new ArrayList(this.fullList);
        notifyDataSetChanged();
    }

    public List<VodItem> getFilteredList() {
        return this.filteredList;
    }

    public boolean isAdPosition(int i) {
        int i2;
        return this.isNativeRecycle && (i2 = this.nativePosition) > 0 && (i + 1) % (i2 + 1) == 0;
    }

    public int getRealPosition(int i) {
        int i2;
        return (!this.isNativeRecycle || (i2 = this.nativePosition) <= 0) ? i : i - ((i + 1) / (i2 + 1));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int i) {
        return isAdPosition(i) ? 1 : 0;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == 1) {
            return new AdViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_vod_native_ad, viewGroup, false));
        }
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_vod_poster, viewGroup, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof AdViewHolder) {
            AdViewHolder adViewHolder = (AdViewHolder) viewHolder;
            Context context = this.context;
            if (context instanceof Activity) {
                adViewHolder.bind((Activity) context);
            }
        } else if (viewHolder instanceof ViewHolder) {
            final ViewHolder viewHolder2 = (ViewHolder) viewHolder;
            int realPosition = getRealPosition(i);
            if (realPosition < 0 || realPosition >= this.filteredList.size()) {
                return;
            }
            VodItem vodItem = this.filteredList.get(realPosition);
            viewHolder2.tvTitle.setText(vodItem.getName());
            viewHolder2.tvGenre.setText(vodItem.getGenre());
            viewHolder2.tvYear.setText(vodItem.getReleaseDate().isEmpty() ? "HD" : vodItem.getReleaseDate());
            viewHolder2.tvRating.setText(vodItem.getRating());
            viewHolder2.tvTypeBadge.setText(vodItem.isSeries() ? "SERIES" : "MOVIE");
            viewHolder2.tvTypeBadge.setBackgroundResource(vodItem.isSeries() ? R.drawable.bg_blue_purple_gradient : R.drawable.bg_badge_live);
            String streamIcon = vodItem.getStreamIcon();
            if (streamIcon != null && !streamIcon.trim().isEmpty()) {
                Glide.with(this.context).load(streamIcon).placeholder(vodItem.isSeries() ? R.drawable.ic_series : R.drawable.ic_movie).error(vodItem.isSeries() ? R.drawable.ic_series : R.drawable.ic_movie).diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop().into(viewHolder2.ivPoster);
            } else {
                viewHolder2.ivPoster.setImageResource(vodItem.isSeries() ? R.drawable.ic_series : R.drawable.ic_movie);
            }
            PlaylistStorageManager playlistStorageManager = this.storageManager;
            if (playlistStorageManager != null) {
                WatchProgress watchProgress = playlistStorageManager.getWatchProgress(vodItem.getId());
                if (watchProgress != null && watchProgress.getProgressPercentage() > 0) {
                    viewHolder2.pbProgress.setVisibility(View.VISIBLE);
                    viewHolder2.pbProgress.setProgress(watchProgress.getProgressPercentage());
                } else {
                    viewHolder2.pbProgress.setVisibility(View.GONE);
                }
            } else {
                viewHolder2.pbProgress.setVisibility(View.GONE);
            }
            viewHolder2.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.vod.VodPosterAdapter$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    VodPosterAdapter.this.lambda$onBindViewHolder$0(viewHolder2, view);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onBindViewHolder$0(ViewHolder viewHolder, View view) {
        int realPosition;
        if (this.listener == null || (realPosition = getRealPosition(viewHolder.getAdapterPosition())) < 0 || realPosition >= this.filteredList.size()) {
            return;
        }
        this.listener.onVodItemClick(this.filteredList.get(realPosition), realPosition);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        int i;
        int size = this.filteredList.size();
        if (size == 0) {
            return 0;
        }
        return (!this.isNativeRecycle || (i = this.nativePosition) <= 0) ? size : size + (size / i);
    }

    @Override // android.widget.Filterable
    public Filter getFilter() {
        return new Filter() { // from class: com.iptv.online.player.live.tv.smart.ui.vod.VodPosterAdapter.1
            @Override // android.widget.Filter
            protected Filter.FilterResults performFiltering(CharSequence charSequence) {
                ArrayList arrayList = new ArrayList();
                if (charSequence == null || charSequence.toString().trim().isEmpty()) {
                    arrayList.addAll(VodPosterAdapter.this.fullList);
                } else {
                    String trim = charSequence.toString().toLowerCase().trim();
                    for (VodItem vodItem : VodPosterAdapter.this.fullList) {
                        if (vodItem.getName().toLowerCase().contains(trim) || vodItem.getGenre().toLowerCase().contains(trim) || vodItem.getDirector().toLowerCase().contains(trim) || vodItem.getCast().toLowerCase().contains(trim)) {
                            arrayList.add(vodItem);
                        }
                    }
                }
                Filter.FilterResults filterResults = new Filter.FilterResults();
                filterResults.values = arrayList;
                return filterResults;
            }

            @Override // android.widget.Filter
            protected void publishResults(CharSequence charSequence, Filter.FilterResults filterResults) {
                VodPosterAdapter.this.filteredList = (List) filterResults.values;
                VodPosterAdapter.this.notifyDataSetChanged();
            }
        };
    }

    /* loaded from: classes5.dex */
    static class AdViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llAdsView;

        AdViewHolder(View view) {
            super(view);
            this.llAdsView = (LinearLayout) view.findViewById(R.id.llAdsView);
        }

        void bind(Activity activity) {
            LinearLayout linearLayout;
            if (activity == null || (linearLayout = this.llAdsView) == null || linearLayout.getChildCount() != 0) {
                return;
            }
//            NativeLoader.showNativeAds(activity, this.llAdsView, NativeType.BIG_NATIVE);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes5.dex */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPoster;
        ProgressBar pbProgress;
        TextView tvGenre;
        TextView tvRating;
        TextView tvTitle;
        TextView tvTypeBadge;
        TextView tvYear;

        ViewHolder(View view) {
            super(view);
            this.ivPoster = (ImageView) view.findViewById(R.id.ivVodPoster);
            this.tvTitle = (TextView) view.findViewById(R.id.tvVodTitle);
            this.tvGenre = (TextView) view.findViewById(R.id.tvVodGenre);
            this.tvYear = (TextView) view.findViewById(R.id.tvVodYear);
            this.tvRating = (TextView) view.findViewById(R.id.tvVodRating);
            this.tvTypeBadge = (TextView) view.findViewById(R.id.tvVodTypeBadge);
            this.pbProgress = (ProgressBar) view.findViewById(R.id.pbPosterProgress);
        }
    }
}

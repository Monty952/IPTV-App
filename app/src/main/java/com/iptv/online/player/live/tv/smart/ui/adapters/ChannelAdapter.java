package com.iptv.online.player.live.tv.smart.ui.adapters;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
//import com.adsUtils.adsloader.nativeads.NativeLoader;
//import com.adsUtils.adsloader.nativeads.NativeType;
//import com.adsUtils.config.AdsConfig;
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.load.DataSource;
//import com.bumptech.glide.load.engine.DiskCacheStrategy;
//import com.bumptech.glide.load.engine.GlideException;
//import com.bumptech.glide.request.RequestListener;
//import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.iptv.online.player.live.tv.smart.R;
import com.iptv.online.player.live.tv.smart.data.local.PlaylistStorageManager;
import com.iptv.online.player.live.tv.smart.data.model.PlaylistItem;
import com.iptv.online.player.live.tv.smart.ui.adapters.ChannelAdapter;
import com.iptv.online.player.live.tv.smart.utils.CountryUtils;

import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

@SuppressWarnings("all")

/* loaded from: classes5.dex */
public class ChannelAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    private static final int[] BANNER_BACKGROUNDS = {R.drawable.bg_channel_banner_blue, R.drawable.bg_channel_banner_blue};
    public static final int VIEW_TYPE_AD = 1;
    public static final int VIEW_TYPE_ITEM = 0;
    private final Context context;
    private boolean isNativeRecycle;
    private final OnChannelClickListener listener;
    private int nativePosition;
    private final PlaylistStorageManager storageManager;
    private List<PlaylistItem> channelsList = new ArrayList();
    private List<PlaylistItem> channelsListFull = new ArrayList();
    private String currentSearchQuery = "";
    private String currentCountryCode = null;

    /* loaded from: classes5.dex */
    public interface OnChannelClickListener {
        void onChannelClick(PlaylistItem playlistItem, int i);

        void onFavoriteToggle(PlaylistItem playlistItem, int i);
    }

    public ChannelAdapter(Context context, PlaylistStorageManager playlistStorageManager, OnChannelClickListener onChannelClickListener) {
        this.context = context;
        this.storageManager = playlistStorageManager;
        this.listener = onChannelClickListener;
        updateAdConfig();
    }

    public void updateAdConfig() {
//        this.isNativeRecycle = AdsConfig.getNativeRecycle() && AdsConfig.getIsAdsOn();
//        this.nativePosition = AdsConfig.getNativePositionInt();
    }

    public void setChannels(List<PlaylistItem> list) {
        String str;
        updateAdConfig();
        this.channelsList = new ArrayList(list);
        this.channelsListFull = new ArrayList(list);
        if (this.currentCountryCode != null || ((str = this.currentSearchQuery) != null && !str.isEmpty())) {
            applyCombinedFilter();
        } else {
            notifyDataSetChanged();
        }
    }

    public void setCountryFilter(String str) {
        // Save the selected country.
        // Null or ALL means show every country.
        this.currentCountryCode = (str == null || "ALL".equalsIgnoreCase(str)) ? null : str.trim();
        applyCombinedFilter();
    }

    public String getCountryFilter() {
        return this.currentCountryCode;
    }

    // New Code
    public void applyCombinedFilter() {
        ArrayList<PlaylistItem> filteredList = new ArrayList<>();
        String searchQuery = this.currentSearchQuery != null
                ? this.currentSearchQuery.trim().toLowerCase()
                : "";
        for (PlaylistItem item : this.channelsListFull) {
            if (item == null) {
                continue;
            }
            // Check selected country.
            String itemCountry = item.getCountryCode();
            boolean matchesCountry =
                    this.currentCountryCode == null
                            || (itemCountry != null
                            && this.currentCountryCode.equalsIgnoreCase(itemCountry.trim()));
            // Check search query.
            boolean matchesSearch = true;
            if (!searchQuery.isEmpty()) {
                String name = item.getName() != null
                        ? item.getName().toLowerCase()
                        : "";
                String group = item.getGroupTitle() != null
                        ? item.getGroupTitle().toLowerCase()
                        : "";
                String country = item.getCountryName() != null
                        ? item.getCountryName().toLowerCase()
                        : "";
                matchesSearch = name.contains(searchQuery)
                        || group.contains(searchQuery)
                        || country.contains(searchQuery);
            }
            if (matchesCountry && matchesSearch) {
                filteredList.add(item);
            }
        }
        this.channelsList.clear();
        this.channelsList.addAll(filteredList);
        notifyDataSetChanged();
    }

    // Old code
  /*  public void applyCombinedFilter() {
        ArrayList arrayList = new ArrayList();
        String str = this.currentSearchQuery;
        String trim = str != null ? str.toLowerCase().trim() : "";
        for (PlaylistItem playlistItem : this.channelsListFull) {
            String str2 = this.currentCountryCode;
            boolean z = true;
            boolean equalsIgnoreCase = str2 != null ? str2.equalsIgnoreCase(playlistItem.getCountryCode()) : true;
            if (!trim.isEmpty() && !playlistItem.getName().toLowerCase().contains(trim) && !playlistItem.getGroupTitle().toLowerCase().contains(trim) && !playlistItem.getCountryName().toLowerCase().contains(trim)) {
                z = false;
            }
            if (equalsIgnoreCase && z) {
                arrayList.add(playlistItem);
            }
        }
        this.channelsList.clear();
        this.channelsList.addAll(arrayList);
        notifyDataSetChanged();
    }*/

    public List<PlaylistItem> getCurrentChannels() {
        return this.channelsList;
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
//            return new AdViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_channel_native_ad, viewGroup, false));
        }
        return new ChannelViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_channel, viewGroup, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        String upperCase;
        if (viewHolder instanceof AdViewHolder) {
            AdViewHolder adViewHolder = (AdViewHolder) viewHolder;
            Context context = this.context;
            if (context instanceof Activity) {
                adViewHolder.bind((Activity) context);
            } else if (viewHolder.itemView.getContext() instanceof Activity) {
                adViewHolder.bind((Activity) viewHolder.itemView.getContext());
            }
        } else if (viewHolder instanceof ChannelViewHolder) {
            final ChannelViewHolder channelViewHolder = (ChannelViewHolder) viewHolder;
            int realPosition = getRealPosition(i);
            if (realPosition < 0 || realPosition >= this.channelsList.size()) {
                return;
            }
            PlaylistItem playlistItem = this.channelsList.get(realPosition);
            channelViewHolder.tvChannelName.setText(playlistItem.getName());
            String countryFlag = CountryUtils.getCountryFlag(playlistItem.getCountryCode());
            if (countryFlag != null && !countryFlag.isEmpty() && !"🌐".equals(countryFlag)) {
                channelViewHolder.tvChannelCategory.setText(countryFlag + " " + playlistItem.getGroupTitle());
            } else {
                channelViewHolder.tvChannelCategory.setText(playlistItem.getGroupTitle());
            }
            int[] iArr = BANNER_BACKGROUNDS;
            channelViewHolder.layoutChannelBanner.setBackgroundResource(iArr[Math.abs(realPosition) % iArr.length]);
            String groupTitle = playlistItem.getGroupTitle();
            if (groupTitle == null || groupTitle.trim().isEmpty() || "General".equalsIgnoreCase(groupTitle)) {
                groupTitle = playlistItem.getName();
            }
            if (groupTitle.length() > 10) {
                upperCase = groupTitle.substring(0, 10).toUpperCase();
            } else {
                upperCase = groupTitle.toUpperCase();
            }
            channelViewHolder.tvBannerCenterText.setText(upperCase);
            channelViewHolder.btnFavorite.setImageResource(this.storageManager.isFavorite(playlistItem.getId()) ? R.drawable.ic_star_filled : R.drawable.ic_star_outline);
            if (playlistItem.getLogoUrl() != null && !playlistItem.getLogoUrl().trim().isEmpty()) {
                channelViewHolder.ivChannelLogo.setVisibility(View.VISIBLE);
                channelViewHolder.tvBannerCenterText.setVisibility(View.GONE);
                Glide.with(this.context).load(playlistItem.getLogoUrl()).diskCacheStrategy(DiskCacheStrategy.ALL).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, com.bumptech.glide.request.target.Target<Drawable> target, boolean isFirstResource) {
                        channelViewHolder.ivChannelLogo.setVisibility(View.GONE);
                        channelViewHolder.tvBannerCenterText.setVisibility(View.VISIBLE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, com.bumptech.glide.request.target.Target<Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
                        channelViewHolder.ivChannelLogo.setVisibility(View.VISIBLE);
                        channelViewHolder.tvBannerCenterText.setVisibility(View.GONE);
                        return false;
                    } // from class: com.iptv.online.player.live.tv.smart.ui.adapters.ChannelAdapter.1
                  /*  @Override // com.bumptech.glide.request.RequestListener
                    public boolean onLoadFailed(GlideException glideException, Object obj, Target target, boolean z) {
                        channelViewHolder.ivChannelLogo.setVisibility(View.GONE);
                        channelViewHolder.tvBannerCenterText.setVisibility(View.VISIBLE);
                        return false;
                    }

                    @Override // com.bumptech.glide.request.RequestListener
                    public boolean onResourceReady(Drawable drawable, Object obj, Target target, DataSource dataSource, boolean z) {
                        channelViewHolder.ivChannelLogo.setVisibility(View.VISIBLE);
                        channelViewHolder.tvBannerCenterText.setVisibility(View.GONE);
                        return false;
                    }*/
                }).into(channelViewHolder.ivChannelLogo);
            } else {
                channelViewHolder.ivChannelLogo.setVisibility(View.GONE);
                channelViewHolder.tvBannerCenterText.setVisibility(View.VISIBLE);
            }
            channelViewHolder.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.adapters.ChannelAdapter$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    ChannelAdapter.this.lambda$onBindViewHolder$0(channelViewHolder, view);
                }
            });
            channelViewHolder.btnFavorite.setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.adapters.ChannelAdapter$$ExternalSyntheticLambda1
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    ChannelAdapter.this.lambda$onBindViewHolder$1(channelViewHolder, view);
                }
            });
            channelViewHolder.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() { // from class: com.iptv.online.player.live.tv.smart.ui.adapters.ChannelAdapter$$ExternalSyntheticLambda2
                @Override // android.view.View.OnFocusChangeListener
                public final void onFocusChange(View view, boolean z) {
                    ChannelAdapter.lambda$onBindViewHolder$2(channelViewHolder, view, z);

                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onBindViewHolder$0(ChannelViewHolder channelViewHolder, View view) {
        int realPosition;
        if (this.listener == null || (realPosition = getRealPosition(channelViewHolder.getAdapterPosition())) < 0 || realPosition >= this.channelsList.size()) {
            return;
        }
        this.listener.onChannelClick(this.channelsList.get(realPosition), realPosition);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onBindViewHolder$1(ChannelViewHolder channelViewHolder, View view) {
        int realPosition = getRealPosition(channelViewHolder.getAdapterPosition());
        if (realPosition < 0 || realPosition >= this.channelsList.size()) {
            return;
        }
        PlaylistItem playlistItem = this.channelsList.get(realPosition);
        this.storageManager.toggleFavorite(playlistItem.getId());
        channelViewHolder.btnFavorite.setImageResource(this.storageManager.isFavorite(playlistItem.getId()) ? R.drawable.ic_star_filled : R.drawable.ic_star_outline);
        OnChannelClickListener onChannelClickListener = this.listener;
        if (onChannelClickListener != null) {
            onChannelClickListener.onFavoriteToggle(playlistItem, realPosition);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$onBindViewHolder$2(ChannelViewHolder channelViewHolder, View view, boolean z) {
        float f = z ? 1.06f : 1.0f;
        channelViewHolder.cardView.setCardElevation(z ? 12.0f : 2.0f);
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(view, "scaleX", f);
        ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(view, "scaleY", f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(ofFloat, ofFloat2);
        animatorSet.setDuration(150L);
        animatorSet.start();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        int i;
        int size = this.channelsList.size();
        if (size == 0) {
            return 0;
        }
        return (!this.isNativeRecycle || (i = this.nativePosition) <= 0) ? size : size + (size / i);
    }

    @Override // android.widget.Filterable
    public Filter getFilter() {
        return new Filter() { // from class: com.iptv.online.player.live.tv.smart.ui.adapters.ChannelAdapter.2
            @Override // android.widget.Filter
            protected Filter.FilterResults performFiltering(CharSequence charSequence) {
                ChannelAdapter.this.currentSearchQuery = charSequence != null ? charSequence.toString() : "";
                ArrayList arrayList = new ArrayList();
                String trim = ChannelAdapter.this.currentSearchQuery.toLowerCase().trim();
                for (PlaylistItem playlistItem : ChannelAdapter.this.channelsListFull) {
                    boolean z = true;
                    boolean equalsIgnoreCase = ChannelAdapter.this.currentCountryCode != null ? ChannelAdapter.this.currentCountryCode.equalsIgnoreCase(playlistItem.getCountryCode()) : true;
                    if (!trim.isEmpty() && !playlistItem.getName().toLowerCase().contains(trim) && !playlistItem.getGroupTitle().toLowerCase().contains(trim) && !playlistItem.getCountryName().toLowerCase().contains(trim)) {
                        z = false;
                    }
                    if (equalsIgnoreCase && z) {
                        arrayList.add(playlistItem);
                    }
                }
                Filter.FilterResults filterResults = new Filter.FilterResults();
                filterResults.values = arrayList;
                filterResults.count = arrayList.size();
                return filterResults;
            }

            @Override // android.widget.Filter
            protected void publishResults(CharSequence charSequence, Filter.FilterResults filterResults) {
                ChannelAdapter.this.channelsList.clear();
                if (filterResults.values != null) {
                    ChannelAdapter.this.channelsList.addAll((List) filterResults.values);
                }
                ChannelAdapter.this.notifyDataSetChanged();
            }
        };
    }

    /* loaded from: classes5.dex */
    public static class AdViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llAdsView;

        public AdViewHolder(View view) {
            super(view);
            this.llAdsView = (LinearLayout) view.findViewById(R.id.llAdsView);
        }

        public void bind(Activity activity) {
            LinearLayout linearLayout;
            if (activity == null || (linearLayout = this.llAdsView) == null || linearLayout.getChildCount() != 0) {
                return;
            }
//            NativeLoader.showNativeAds(activity, this.llAdsView, NativeType.BIG_NATIVE);
        }
    }

    /* loaded from: classes5.dex */
    public static class ChannelViewHolder extends RecyclerView.ViewHolder {
        ImageButton btnFavorite;
        CardView cardView;
        ImageView ivChannelLogo;
        RelativeLayout layoutChannelBanner;
        TextView tvBannerCenterText;
        TextView tvChannelCategory;
        TextView tvChannelName;
        TextView tvLiveBadge;

        public ChannelViewHolder(View view) {
            super(view);
            this.cardView = (CardView) view.findViewById(R.id.cardChannel);
            this.layoutChannelBanner = (RelativeLayout) view.findViewById(R.id.layoutChannelBanner);
            this.tvBannerCenterText = (TextView) view.findViewById(R.id.tvBannerCenterText);
            this.ivChannelLogo = (ImageView) view.findViewById(R.id.ivChannelLogo);
            this.tvLiveBadge = (TextView) view.findViewById(R.id.tvLiveBadge);
            this.tvChannelName = (TextView) view.findViewById(R.id.tvChannelName);
            this.tvChannelCategory = (TextView) view.findViewById(R.id.tvChannelCategory);
            this.btnFavorite = (ImageButton) view.findViewById(R.id.btnFavorite);
        }
    }
}

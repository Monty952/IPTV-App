package com.iptv.online.player.live.tv.smart.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
//import androidx.media3.extractor.text.ttml.TtmlNode;
//import androidx.recyclerview.widget.RecyclerView;
//import com.adsUtils.adsloader.nativeads.NativeLoader;
//import com.adsUtils.adsloader.nativeads.NativeType;
//import com.adsUtils.config.AdsConfig;
import com.iptv.online.player.live.tv.smart.R;
import com.iptv.online.player.live.tv.smart.data.local.PlaylistStorageManager;
import com.iptv.online.player.live.tv.smart.data.model.Category;
import java.util.ArrayList;
import java.util.List;
@SuppressWarnings("all")

/* loaded from: classes5.dex */
public class CategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    public static final int VIEW_TYPE_AD = 1;
    public static final int VIEW_TYPE_ITEM = 0;
    private List<Category> categories;
    private List<Category> categoriesFull;
    private final Context context;
    private boolean isNativeRecycle;
    private final OnCategoryClickListener listener;
    private int nativePosition;
    private final PlaylistStorageManager storageManager;

    /* loaded from: classes5.dex */
    public interface OnCategoryClickListener {
        void onCategoryClick(Category category, int i);

        default void onCategoryLongClick(Category category, int i) {
        }
    }

    public CategoryAdapter(Context context, PlaylistStorageManager playlistStorageManager, OnCategoryClickListener onCategoryClickListener) {
        this.categories = new ArrayList();
        this.categoriesFull = new ArrayList();
        this.context = context;
        this.storageManager = playlistStorageManager;
        this.listener = onCategoryClickListener;
        updateAdConfig();
    }

    public CategoryAdapter(PlaylistStorageManager playlistStorageManager, OnCategoryClickListener onCategoryClickListener) {
        this(null, playlistStorageManager, onCategoryClickListener);
    }

    public void updateAdConfig() {
//        this.isNativeRecycle = AdsConfig.getNativeRecycle() && AdsConfig.getIsAdsOn();
//        this.nativePosition = AdsConfig.getNativePositionInt();
    }

    public void setCategories(List<Category> list) {
        updateAdConfig();
        this.categories = new ArrayList(list);
        this.categoriesFull = new ArrayList(list);
        notifyDataSetChanged();
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
            return new AdViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_category_native_ad, viewGroup, false));
        }
        return new CategoryViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_category, viewGroup, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof AdViewHolder) {
            AdViewHolder adViewHolder = (AdViewHolder) viewHolder;
            Context context = this.context;
            if (context instanceof Activity) {
                adViewHolder.bind((Activity) context);
            } else if (viewHolder.itemView.getContext() instanceof Activity) {
                adViewHolder.bind((Activity) viewHolder.itemView.getContext());
            }
        } else if (viewHolder instanceof CategoryViewHolder) {
            final CategoryViewHolder categoryViewHolder = (CategoryViewHolder) viewHolder;
            int realPosition = getRealPosition(i);
            if (realPosition < 0 || realPosition >= this.categories.size()) {
                return;
            }
            Category category = this.categories.get(realPosition);
            categoryViewHolder.tvCategoryName.setText(category.getName());
            int channelCount = category.getChannelCount();
            categoryViewHolder.tvCategoryCount.setText(channelCount + (channelCount == 1 ? " Channel" : " Channels"));
            String id = category.getId();
            if ("all".equalsIgnoreCase(id)) {
                categoryViewHolder.ivCategoryIcon.setImageResource(R.drawable.ic_tv);
                categoryViewHolder.ivCategoryIcon.setColorFilter(ContextCompat.getColor(categoryViewHolder.itemView.getContext(), R.color.brand_blue));
            } else if ("favorites".equalsIgnoreCase(id)) {
                categoryViewHolder.ivCategoryIcon.setImageResource(R.drawable.ic_star_filled);
                categoryViewHolder.ivCategoryIcon.setColorFilter(ContextCompat.getColor(categoryViewHolder.itemView.getContext(), R.color.star_gold));
            } else if ("recent".equalsIgnoreCase(id)) {
                categoryViewHolder.ivCategoryIcon.setImageResource(R.drawable.ic_refresh);
                categoryViewHolder.ivCategoryIcon.setColorFilter(ContextCompat.getColor(categoryViewHolder.itemView.getContext(), R.color.cyan_accent));
            } else {
                categoryViewHolder.ivCategoryIcon.setImageResource(R.drawable.ic_folder);
                categoryViewHolder.ivCategoryIcon.setColorFilter(ContextCompat.getColor(categoryViewHolder.itemView.getContext(), R.color.brand_purple));
            }
            categoryViewHolder.ivCategoryLock.setVisibility(this.storageManager.isCategoryLocked(category.getName()) ? 0 : 8);
            categoryViewHolder.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.adapters.CategoryAdapter$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    CategoryAdapter.this.lambda$onBindViewHolder$0(categoryViewHolder, view);
                }
            });
            categoryViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.adapters.CategoryAdapter$$ExternalSyntheticLambda1
                @Override // android.view.View.OnLongClickListener
                public final boolean onLongClick(View view) {
                    boolean lambda$onBindViewHolder$1;
                    lambda$onBindViewHolder$1 = CategoryAdapter.this.lambda$onBindViewHolder$1(categoryViewHolder, view);
                    return lambda$onBindViewHolder$1;
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onBindViewHolder$0(CategoryViewHolder categoryViewHolder, View view) {
        int adapterPosition = categoryViewHolder.getAdapterPosition();
        int realPosition = getRealPosition(adapterPosition);
        if (this.listener == null || realPosition < 0 || realPosition >= this.categories.size()) {
            return;
        }
        this.listener.onCategoryClick(this.categories.get(realPosition), adapterPosition);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$onBindViewHolder$1(CategoryViewHolder categoryViewHolder, View view) {
        int adapterPosition = categoryViewHolder.getAdapterPosition();
        int realPosition = getRealPosition(adapterPosition);
        if (this.listener == null || realPosition < 0 || realPosition >= this.categories.size()) {
            return false;
        }
        this.listener.onCategoryLongClick(this.categories.get(realPosition), adapterPosition);
        return true;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        int i;
        int size = this.categories.size();
        if (size == 0) {
            return 0;
        }
        return (!this.isNativeRecycle || (i = this.nativePosition) <= 0) ? size : size + (size / i);
    }

    @Override // android.widget.Filterable
    public Filter getFilter() {
        return new Filter() { // from class: com.iptv.online.player.live.tv.smart.ui.adapters.CategoryAdapter.1
            @Override // android.widget.Filter
            protected Filter.FilterResults performFiltering(CharSequence charSequence) {
                ArrayList arrayList = new ArrayList();
                if (charSequence == null || charSequence.toString().trim().isEmpty()) {
                    arrayList.addAll(CategoryAdapter.this.categoriesFull);
                } else {
                    String trim = charSequence.toString().toLowerCase().trim();
                    for (Category category : CategoryAdapter.this.categoriesFull) {
                        if (category.getName().toLowerCase().contains(trim)) {
                            arrayList.add(category);
                        }
                    }
                }
                Filter.FilterResults filterResults = new Filter.FilterResults();
                filterResults.values = arrayList;
                filterResults.count = arrayList.size();
                return filterResults;
            }

            @Override // android.widget.Filter
            protected void publishResults(CharSequence charSequence, Filter.FilterResults filterResults) {
                CategoryAdapter.this.categories.clear();
                if (filterResults.values != null) {
                    CategoryAdapter.this.categories.addAll((List) filterResults.values);
                }
                CategoryAdapter.this.notifyDataSetChanged();
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
    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        CardView cardCategory;
        ImageView ivCategoryIcon;
        ImageView ivCategoryLock;
        TextView tvCategoryCount;
        TextView tvCategoryName;

        public CategoryViewHolder(View view) {
            super(view);
            this.cardCategory = (CardView) view.findViewById(R.id.cardCategory);
            this.ivCategoryIcon = (ImageView) view.findViewById(R.id.ivCategoryIcon);
            this.ivCategoryLock = (ImageView) view.findViewById(R.id.ivCategoryLock);
            this.tvCategoryName = (TextView) view.findViewById(R.id.tvCategoryName);
            this.tvCategoryCount = (TextView) view.findViewById(R.id.tvCategoryCount);
        }
    }
}

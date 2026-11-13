package com.iptv.online.player.live.tv.smart.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.iptv.online.player.live.tv.smart.R;
import com.iptv.online.player.live.tv.smart.ui.dialogs.CountryFilterItem;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes5.dex */
public class CountryFilterAdapter extends RecyclerView.Adapter<CountryFilterAdapter.ViewHolder> implements Filterable {
    private final List<CountryFilterItem> itemsDisplayed;
    private final List<CountryFilterItem> itemsFull;
    private final OnCountrySelectedListener listener;

    /* loaded from: classes5.dex */
    public interface OnCountrySelectedListener {
        void onCountrySelected(CountryFilterItem countryFilterItem);
    }

    public CountryFilterAdapter(List<CountryFilterItem> list, OnCountrySelectedListener onCountrySelectedListener) {
        this.itemsFull = new ArrayList(list);
        this.itemsDisplayed = new ArrayList(list);
        this.listener = onCountrySelectedListener;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_country_filter, viewGroup, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        final CountryFilterItem countryFilterItem = this.itemsDisplayed.get(i);
        Context context = viewHolder.itemView.getContext();
        viewHolder.tvFlag.setText(countryFilterItem.getFlagEmoji());
        viewHolder.tvName.setText(countryFilterItem.getCountryName());
        viewHolder.tvCount.setText(String.valueOf(countryFilterItem.getChannelCount()));
        if ("ALL".equalsIgnoreCase(countryFilterItem.getCountryCode())) {
            viewHolder.tvSubtitle.setText(context.getString(R.string.all_live_channels));
        } else if ("OTHER".equalsIgnoreCase(countryFilterItem.getCountryCode())) {
            viewHolder.tvSubtitle.setText(context.getString(R.string.international_other));
        } else {
            viewHolder.tvSubtitle.setText(context.getString(R.string.region_prefix, countryFilterItem.getCountryCode()));
        }
        if (countryFilterItem.isUserCountry()) {
            viewHolder.tvUserCountryTag.setVisibility(View.VISIBLE);
            viewHolder.tvUserCountryTag.setText(context.getString(R.string.your_country));
        } else {
            viewHolder.tvUserCountryTag.setVisibility(View.GONE);
        }
        if (countryFilterItem.isSelected()) {
            viewHolder.rootView.setBackgroundResource(R.drawable.bg_country_item_selected);
            viewHolder.radioIndicator.setBackgroundResource(R.drawable.radio_selected);
        } else {
            viewHolder.rootView.setBackgroundResource(R.drawable.bg_country_item_unselected);
            viewHolder.radioIndicator.setBackgroundResource(R.drawable.radio_unselected);
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.adapters.CountryFilterAdapter$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                CountryFilterAdapter.this.lambda$onBindViewHolder$0(countryFilterItem, view);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onBindViewHolder$0(CountryFilterItem countryFilterItem, View view) {
        OnCountrySelectedListener onCountrySelectedListener = this.listener;
        if (onCountrySelectedListener != null) {
            onCountrySelectedListener.onCountrySelected(countryFilterItem);
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.itemsDisplayed.size();
    }

    @Override // android.widget.Filterable
    public Filter getFilter() {
        return new Filter() { // from class: com.iptv.online.player.live.tv.smart.ui.adapters.CountryFilterAdapter.1
            @Override // android.widget.Filter
            protected Filter.FilterResults performFiltering(CharSequence charSequence) {
                ArrayList arrayList = new ArrayList();
                if (charSequence == null || charSequence.toString().trim().isEmpty()) {
                    arrayList.addAll(CountryFilterAdapter.this.itemsFull);
                } else {
                    String lowerCase = charSequence.toString().trim().toLowerCase();
                    for (CountryFilterItem countryFilterItem : CountryFilterAdapter.this.itemsFull) {
                        if (countryFilterItem.getCountryName().toLowerCase().contains(lowerCase) || countryFilterItem.getCountryCode().toLowerCase().contains(lowerCase)) {
                            arrayList.add(countryFilterItem);
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
                CountryFilterAdapter.this.itemsDisplayed.clear();
                if (filterResults.values != null) {
                    CountryFilterAdapter.this.itemsDisplayed.addAll((List) filterResults.values);
                }
                CountryFilterAdapter.this.notifyDataSetChanged();
            }
        };
    }

    /* loaded from: classes5.dex */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        final View radioIndicator;
        final View rootView;
        final TextView tvCount;
        final TextView tvFlag;
        final TextView tvName;
        final TextView tvSubtitle;
        final TextView tvUserCountryTag;

        public ViewHolder(View view) {
            super(view);
            this.rootView = view.findViewById(R.id.countryFilterItemRoot);
            this.radioIndicator = view.findViewById(R.id.radioIndicator);
            this.tvFlag = (TextView) view.findViewById(R.id.tvCountryFlag);
            this.tvName = (TextView) view.findViewById(R.id.tvCountryName);
            this.tvSubtitle = (TextView) view.findViewById(R.id.tvCountrySubtitle);
            this.tvUserCountryTag = (TextView) view.findViewById(R.id.tvUserCountryTag);
            this.tvCount = (TextView) view.findViewById(R.id.tvChannelCount);
        }
    }
}

package com.iptv.online.player.live.tv.smart.ui.vod;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.iptv.online.player.live.tv.smart.R;
import com.iptv.online.player.live.tv.smart.data.model.VodCategory;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes5.dex */
public class VodCategoryChipAdapter extends RecyclerView.Adapter<VodCategoryChipAdapter.ViewHolder> {
    private final OnCategorySelectListener listener;
    private List<VodCategory> categories = new ArrayList();
    private int selectedPosition = 0;

    /* loaded from: classes5.dex */
    public interface OnCategorySelectListener {
        void onCategorySelected(VodCategory vodCategory, int i);
    }

    public VodCategoryChipAdapter(OnCategorySelectListener onCategorySelectListener) {
        this.listener = onCategorySelectListener;
    }

    public void setCategories(List<VodCategory> list) {
        this.categories = list != null ? new ArrayList(list) : new ArrayList();
        this.selectedPosition = 0;
        notifyDataSetChanged();
    }

    public VodCategory getSelectedCategory() {
        int i = this.selectedPosition;
        if (i < 0 || i >= this.categories.size()) {
            return null;
        }
        return this.categories.get(this.selectedPosition);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_vod_category_chip, viewGroup, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        viewHolder.tvCategoryChip.setText(this.categories.get(i).getCategoryName());
        viewHolder.tvCategoryChip.setSelected(i == this.selectedPosition);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.vod.VodCategoryChipAdapter$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                VodCategoryChipAdapter.this.lambda$onBindViewHolder$0(viewHolder, view);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onBindViewHolder$0(ViewHolder viewHolder, View view) {
        int i;
        int i2 = this.selectedPosition;
        this.selectedPosition = viewHolder.getAdapterPosition();
        notifyItemChanged(i2);
        notifyItemChanged(this.selectedPosition);
        if (this.listener == null || (i = this.selectedPosition) < 0 || i >= this.categories.size()) {
            return;
        }
        this.listener.onCategorySelected(this.categories.get(this.selectedPosition), this.selectedPosition);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.categories.size();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes5.dex */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategoryChip;

        ViewHolder(View view) {
            super(view);
            this.tvCategoryChip = (TextView) view.findViewById(R.id.tvCategoryChip);
        }
    }
}

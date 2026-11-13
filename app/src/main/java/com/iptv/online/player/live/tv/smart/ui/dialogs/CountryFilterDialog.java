package com.iptv.online.player.live.tv.smart.ui.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.iptv.online.player.live.tv.smart.R;
import com.iptv.online.player.live.tv.smart.ui.adapters.CountryFilterAdapter;
import com.iptv.online.player.live.tv.smart.ui.dialogs.CountryFilterDialog;
import java.util.List;

@SuppressWarnings("all")
/* loaded from: classes5.dex */
public class CountryFilterDialog extends BottomSheetDialog {
    private CountryFilterAdapter adapter;
    private final List<CountryFilterItem> countryList;
    private final OnCountryFilterListener listener;

    /* loaded from: classes5.dex */
    public interface OnCountryFilterListener {
        void onCountrySelected(CountryFilterItem countryFilterItem);
    }

    public CountryFilterDialog(Context context, List<CountryFilterItem> list, OnCountryFilterListener onCountryFilterListener) {
        super(context, R.style.CustomBottomSheetDialogTheme);
        this.countryList = list;
        this.listener = onCountryFilterListener;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.material.bottomsheet.BottomSheetDialog, androidx.appcompat.app.AppCompatDialog, androidx.activity.ComponentDialog, android.app.Dialog
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.dialog_country_filter);
        setOnShowListener(new DialogInterface.OnShowListener() { // from class: com.iptv.online.player.live.tv.smart.ui.dialogs.CountryFilterDialog$$ExternalSyntheticLambda0
            @Override // android.content.DialogInterface.OnShowListener
            public final void onShow(DialogInterface dialogInterface) {
                CountryFilterDialog.this.lambda$onCreate$0(dialogInterface);
            }
        });
        initViews();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$0(DialogInterface dialogInterface) {
        FrameLayout frameLayout = (FrameLayout) findViewById(com.google.android.material.R.id.design_bottom_sheet);
        if (frameLayout != null) {
            frameLayout.setBackgroundResource(17170445);
            BottomSheetBehavior from = BottomSheetBehavior.from(frameLayout);
            from.setState(3);
            from.setSkipCollapsed(true);
        }
    }

    private void initViews() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rvCountries);
        final EditText editText = (EditText) findViewById(R.id.etCountrySearch);
        ImageButton imageButton = (ImageButton) findViewById(R.id.btnClearCountrySearch);
        ImageButton imageButton2 = (ImageButton) findViewById(R.id.btnDialogClose);
        View findViewById = findViewById(R.id.layoutEmptyCountries);
        TextView textView = (TextView) findViewById(R.id.tvCountrySectionTitle);
        if (imageButton2 != null) {
            imageButton2.setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.dialogs.CountryFilterDialog$$ExternalSyntheticLambda1
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    CountryFilterDialog.this.lambda$initViews$1(view);
                }
            });
        }
        if (recyclerView != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            CountryFilterAdapter countryFilterAdapter = new CountryFilterAdapter(this.countryList, new CountryFilterAdapter.OnCountrySelectedListener() { // from class: com.iptv.online.player.live.tv.smart.ui.dialogs.CountryFilterDialog$$ExternalSyntheticLambda2
                @Override // com.iptv.online.player.live.tv.smart.ui.adapters.CountryFilterAdapter.OnCountrySelectedListener
                public final void onCountrySelected(CountryFilterItem countryFilterItem) {
                    CountryFilterDialog.this.lambda$initViews$2(countryFilterItem);
                }
            });
            this.adapter = countryFilterAdapter;
            recyclerView.setAdapter(countryFilterAdapter);
        }
        if (editText != null) {
            editText.addTextChangedListener(new AnonymousClass1(imageButton, findViewById, recyclerView, textView));
        }
        if (imageButton == null || editText == null) {
            return;
        }
        imageButton.setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.dialogs.CountryFilterDialog$$ExternalSyntheticLambda3
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                editText.setText("");
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initViews$1(View view) {
        dismiss();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initViews$2(CountryFilterItem countryFilterItem) {
        OnCountryFilterListener onCountryFilterListener = this.listener;
        if (onCountryFilterListener != null) {
            onCountryFilterListener.onCountrySelected(countryFilterItem);
        }
        dismiss();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.iptv.online.player.live.tv.smart.ui.dialogs.CountryFilterDialog$1  reason: invalid class name */
    /* loaded from: classes5.dex */
    public class AnonymousClass1 implements TextWatcher {
        final /* synthetic */ ImageButton val$btnClearCountrySearch;
        final /* synthetic */ View val$layoutEmptyCountries;
        final /* synthetic */ RecyclerView val$rvCountries;
        final /* synthetic */ TextView val$tvCountrySectionTitle;

        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
        }

        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        AnonymousClass1(ImageButton imageButton, View view, RecyclerView recyclerView, TextView textView) {
            this.val$btnClearCountrySearch = imageButton;
            this.val$layoutEmptyCountries = view;
            this.val$rvCountries = recyclerView;
            this.val$tvCountrySectionTitle = textView;
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(final CharSequence charSequence, int i, int i2, int i3) {
            ImageButton imageButton = this.val$btnClearCountrySearch;
            if (imageButton != null) {
                imageButton.setVisibility((charSequence == null || charSequence.length() <= 0) ? 8 : 0);
            }
            if (CountryFilterDialog.this.adapter != null) {
                Filter filter = CountryFilterDialog.this.adapter.getFilter();
                final View view = this.val$layoutEmptyCountries;
                final RecyclerView recyclerView = this.val$rvCountries;
                final TextView textView = this.val$tvCountrySectionTitle;
                filter.filter(charSequence, new Filter.FilterListener() { // from class: com.iptv.online.player.live.tv.smart.ui.dialogs.CountryFilterDialog$1$$ExternalSyntheticLambda0
                    @Override // android.widget.Filter.FilterListener
                    public final void onFilterComplete(int i4) {
                        CountryFilterDialog.AnonymousClass1.this.lambda$onTextChanged$0(view, recyclerView, textView, charSequence, i4);
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onTextChanged$0(View view, RecyclerView recyclerView, TextView textView, CharSequence charSequence, int i) {
            boolean z = i == 0;
            if (view != null) {
                view.setVisibility(z ? 0 : 8);
            }
            if (recyclerView != null) {
                recyclerView.setVisibility(z ? 8 : 0);
            }
            if (textView != null) {
                if (charSequence != null && charSequence.length() > 0) {
                    textView.setText(CountryFilterDialog.this.getContext().getString(R.string.countries_matched, Integer.valueOf(i)));
                } else {
                    textView.setText(CountryFilterDialog.this.getContext().getString(R.string.available_countries));
                }
            }
        }
    }
}

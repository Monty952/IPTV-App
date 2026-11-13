package com.iptv.online.player.live.tv.smart.ui.main;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.content.ContextCompat;
//import androidx.media3.extractor.text.ttml.TtmlNode;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
//import com.adsUtils.adsloader.bannerads.BannerLoader;
//import com.adsUtils.adsloader.bannerads.BannerType;
//import com.adsUtils.analytics.AnalyticsTracker;
//import com.adsUtils.custom.CustomActivity;
import com.iptv.online.player.live.tv.smart.AdsPackage.CustomActivity;
import com.iptv.online.player.live.tv.smart.R;
import com.iptv.online.player.live.tv.smart.SelectCountryActivity;
import com.iptv.online.player.live.tv.smart.data.local.PlaylistStorageManager;
import com.iptv.online.player.live.tv.smart.data.model.PlaylistItem;
import com.iptv.online.player.live.tv.smart.data.model.PlaylistSource;
import com.iptv.online.player.live.tv.smart.ui.adapters.ChannelAdapter;
import com.iptv.online.player.live.tv.smart.ui.dialogs.CountryFilterDialog;
import com.iptv.online.player.live.tv.smart.ui.dialogs.CountryFilterItem;
import com.iptv.online.player.live.tv.smart.ui.main.ChannelActivity;
import com.iptv.online.player.live.tv.smart.ui.player.PlayerActivity;
import com.iptv.online.player.live.tv.smart.utils.CountryUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
@SuppressWarnings("all")


/* loaded from: classes5.dex */
public class ChannelActivity extends com.iptv.online.player.live.tv.smart.AdsPackage.CustomActivity implements ChannelAdapter.OnChannelClickListener {
    public static final String EXTRA_CATEGORY_ID = "extra_category_id";
    public static final String EXTRA_CATEGORY_NAME = "extra_category_name";
    private static List<PlaylistItem> sCategoryChannels;
    private ImageButton btnCountryFilter;
    private ChannelAdapter channelAdapter;
    private ProgressBar channelProgressBar;
    private EditText etSearchQuery;
    private LinearLayout layoutActiveFilter;
    private LinearLayout layoutEmptyState;
    private LinearLayout layoutSearch;
    private RecyclerView rvChannels;
    private PlaylistStorageManager storageManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView tvActiveCountryFlag;
    private TextView tvActiveCountryName;
    private TextView tvCategorySubtitle;
    private TextView tvCategoryTitle;
    private String categoryId = "all";
    private String categoryName = "All Channels";
    private List<PlaylistItem> channelList = new ArrayList();
    private String selectedCountryCode = null;
    private String selectedCountryName = null;

    public static void setCategoryChannels(List<PlaylistItem> list) {
        sCategoryChannels = list != null ? new ArrayList(list) : null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.adsUtils.custom.CustomActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_channel);
        View findViewById = findViewById(R.id.llAdsView);
        if (findViewById != null && (findViewById instanceof ViewGroup)) {
//            BannerLoader.showBannerAds(this, (ViewGroup) findViewById, BannerType.FIXED_BANNER);
        }
        this.storageManager = new PlaylistStorageManager(this);
        parseIntentData();
        initViews();
        setupRecyclerView();
        setupListeners();
        loadChannels();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.adsUtils.custom.CustomActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        ChannelAdapter channelAdapter = this.channelAdapter;
        if (channelAdapter != null) {
            channelAdapter.updateAdConfig();
            this.channelAdapter.notifyDataSetChanged();
        }
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.activity.ComponentActivity, android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        setupLayoutManager();
    }

    private void parseIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            String stringExtra = intent.getStringExtra(EXTRA_CATEGORY_NAME);
            String stringExtra2 = intent.getStringExtra(EXTRA_CATEGORY_ID);
            if (stringExtra != null) {
                this.categoryName = stringExtra;
            }
            if (stringExtra2 != null) {
                this.categoryId = stringExtra2;
            }
        }
        List<PlaylistItem> list = sCategoryChannels;
        if (list != null && !list.isEmpty()) {
            this.channelList = new ArrayList(sCategoryChannels);
        } else if ("favorites".equalsIgnoreCase(this.categoryId)) {
            this.channelList = getFavoriteChannels();
        } else if ("recent".equalsIgnoreCase(this.categoryId)) {
            this.channelList = this.storageManager.getRecentChannels();
        } else {
            boolean equalsIgnoreCase = "all".equalsIgnoreCase(this.categoryId);
            PlaylistStorageManager playlistStorageManager = this.storageManager;
            if (equalsIgnoreCase) {
                this.channelList = playlistStorageManager.getCachedChannels();
                return;
            }
            List<PlaylistItem> cachedChannels = playlistStorageManager.getCachedChannels();
            this.channelList = new ArrayList();
            for (PlaylistItem playlistItem : cachedChannels) {
                if (this.categoryName.equalsIgnoreCase(playlistItem.getGroupTitle())) {
                    this.channelList.add(playlistItem);
                }
            }
        }
    }

    private void initViews() {
        this.tvCategoryTitle = (TextView) findViewById(R.id.tvCategoryTitle);
        this.tvCategorySubtitle = (TextView) findViewById(R.id.tvCategorySubtitle);
        this.rvChannels = (RecyclerView) findViewById(R.id.rvChannels);
        this.swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        this.layoutEmptyState = (LinearLayout) findViewById(R.id.layoutEmptyState);
        this.layoutSearch = (LinearLayout) findViewById(R.id.layoutSearch);
        this.etSearchQuery = (EditText) findViewById(R.id.etSearchQuery);
        this.channelProgressBar = (ProgressBar) findViewById(R.id.channelProgressBar);
        this.layoutActiveFilter = (LinearLayout) findViewById(R.id.layoutActiveFilter);
        this.tvActiveCountryFlag = (TextView) findViewById(R.id.tvActiveCountryFlag);
        this.tvActiveCountryName = (TextView) findViewById(R.id.tvActiveCountryName);
        this.btnCountryFilter = (ImageButton) findViewById(R.id.btnCountryFilter);
        this.tvCategoryTitle.setText(this.categoryName);
        this.tvCategorySubtitle.setText(this.channelList.size() + " Channels • " + this.storageManager.getActiveSource().getName());
        this.swipeRefreshLayout.setColorSchemeResources(R.color.purple_accent, R.color.cyan_accent);
    }

    private void setupRecyclerView() {
        setupLayoutManager();
        ChannelAdapter channelAdapter = new ChannelAdapter(this, this.storageManager, this);
        this.channelAdapter = channelAdapter;
        this.rvChannels.setAdapter(channelAdapter);
    }

    private void setupLayoutManager() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() { // from class: com.iptv.online.player.live.tv.smart.ui.main.ChannelActivity.1
            @Override // androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
            public int getSpanSize(int i) {
                return (ChannelActivity.this.channelAdapter == null || !ChannelActivity.this.channelAdapter.isAdPosition(i)) ? 1 : 2;
            }
        });
        this.rvChannels.setLayoutManager(gridLayoutManager);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupListeners$0(View view) {
        setOnBackPressedClick();
    }

    private void setupListeners() {
        findViewById(R.id.btnChannelBack).setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.main.ChannelActivity$$ExternalSyntheticLambda4
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ChannelActivity.this.lambda$setupListeners$0(view);
            }
        });
        this.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() { // from class: com.iptv.online.player.live.tv.smart.ui.main.ChannelActivity$$ExternalSyntheticLambda5
            @Override // androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
            public final void onRefresh() {
                ChannelActivity.this.lambda$setupListeners$1();
            }
        });
        findViewById(R.id.btnSearchToggle).setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.main.ChannelActivity$$ExternalSyntheticLambda6
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ChannelActivity.this.lambda$setupListeners$2(view);
            }
        });
        findViewById(R.id.btnClearSearch).setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.main.ChannelActivity$$ExternalSyntheticLambda7
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ChannelActivity.this.lambda$setupListeners$3(view);
            }
        });
        ImageButton imageButton = this.btnCountryFilter;
        if (imageButton != null) {
            imageButton.setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.main.ChannelActivity$$ExternalSyntheticLambda8
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    ChannelActivity.this.lambda$setupListeners$4(view);
                }
            });
        }
        View findViewById = findViewById(R.id.btnClearCountryFilter);
        if (findViewById != null) {
            findViewById.setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.main.ChannelActivity$$ExternalSyntheticLambda9
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    ChannelActivity.this.lambda$setupListeners$5(view);
                }
            });
        }
        View findViewById2 = findViewById(R.id.chipCountryFilter);
        if (findViewById2 != null) {
            findViewById2.setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.main.ChannelActivity$$ExternalSyntheticLambda10
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    ChannelActivity.this.lambda$setupListeners$6(view);
                }
            });
        }
        findViewById(R.id.btnFavoriteChannels).setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.main.ChannelActivity$$ExternalSyntheticLambda11
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ChannelActivity.this.lambda$setupListeners$7(view);
            }
        });
        this.etSearchQuery.addTextChangedListener(new AnonymousClass2());
        this.etSearchQuery.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: com.iptv.online.player.live.tv.smart.ui.main.ChannelActivity$$ExternalSyntheticLambda1
            @Override // android.widget.TextView.OnEditorActionListener
            public final boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean lambda$setupListeners$9;
                lambda$setupListeners$9 = ChannelActivity.this.lambda$setupListeners$9(textView, i, keyEvent);
                return lambda$setupListeners$9;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupListeners$1() {
        this.swipeRefreshLayout.setRefreshing(false);
        loadChannels();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupListeners$2(View view) {
        int visibility = this.layoutSearch.getVisibility();
        LinearLayout linearLayout = this.layoutSearch;
        if (visibility == 0) {
            linearLayout.setVisibility(View.GONE);
            this.etSearchQuery.setText("");
            return;
        }
        linearLayout.setVisibility(View.VISIBLE);
        this.etSearchQuery.requestFocus();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupListeners$3(View view) {
        this.etSearchQuery.setText("");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupListeners$4(View view) {
        showCountryFilterDialog();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupListeners$5(View view) {
        clearCountryFilter();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupListeners$6(View view) {
        showCountryFilterDialog();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupListeners$7(View view) {
        startActivity(new Intent(this, FavoriteActivity.class));
//        startAdsActivity(new Intent(this, FavoriteActivity.class));
        new Intent(this, FavoriteActivity.class);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.iptv.online.player.live.tv.smart.ui.main.ChannelActivity$2  reason: invalid class name */
    /* loaded from: classes5.dex */
    public class AnonymousClass2 implements TextWatcher {
        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
        }

        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        AnonymousClass2() {
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            if (ChannelActivity.this.channelAdapter != null) {
                ChannelActivity.this.channelAdapter.getFilter().filter(charSequence, new Filter.FilterListener() { // from class: com.iptv.online.player.live.tv.smart.ui.main.ChannelActivity$2$$ExternalSyntheticLambda0
                    @Override // android.widget.Filter.FilterListener
                    public final void onFilterComplete(int i4) {
                        ChannelActivity.AnonymousClass2.this.lambda$onTextChanged$0(i4);
                    }
                });
            }
            if (charSequence == null || charSequence.length() < 2) {
                return;
            }
//            AnalyticsTracker.logSearch("ChannelActivity", charSequence.toString());
            Log.d(TAG, "ChannelActivity: " + charSequence.toString());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onTextChanged$0(int i) {
            ChannelActivity.this.updateSubtitle();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$setupListeners$9(TextView textView, int i, KeyEvent keyEvent) {
        if (i == 3) {
            ChannelAdapter channelAdapter = this.channelAdapter;
            if (channelAdapter != null) {
                channelAdapter.getFilter().filter(this.etSearchQuery.getText().toString(), new Filter.FilterListener() { // from class: com.iptv.online.player.live.tv.smart.ui.main.ChannelActivity$$ExternalSyntheticLambda2
                    @Override // android.widget.Filter.FilterListener
                    public final void onFilterComplete(int i2) {
                        ChannelActivity.this.lambda$setupListeners$8(i2);
                    }
                });
                return true;
            }
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupListeners$8(int i) {
        updateSubtitle();
    }

    private void showCountryFilterDialog() {
        String str;
//        showTabScrollInterstitialAds();
        List<PlaylistItem> list = this.channelList;
        if (list == null || list.isEmpty()) {
            Toast.makeText(this, getString(R.string.no_channels_to_filter), 0).show();
            return;
        }
        HashMap hashMap = new HashMap();
        for (PlaylistItem playlistItem : this.channelList) {
            String countryCode = playlistItem.getCountryCode();
            if (countryCode == null || countryCode.trim().isEmpty()) {
                str = "OTHER";
            } else {
                str = countryCode.toUpperCase();
            }
            hashMap.put(str, Integer.valueOf(((Integer) hashMap.getOrDefault(str, 0)).intValue() + 1));
        }
        ArrayList arrayList = new ArrayList();
        String str2 = this.selectedCountryCode;
        arrayList.add(new CountryFilterItem("ALL", getString(R.string.all_countries), "🌐", this.channelList.size(), str2 == null || "ALL".equalsIgnoreCase(str2)));
        ArrayList arrayList2 = new ArrayList();
        String savedCountry = SelectCountryActivity.getSavedCountry(this);
        CountryFilterItem countryFilterItem = null;
        // New Code
        for (Object obj : hashMap.entrySet()) {
            Map.Entry<?, ?> entry = (Map.Entry<?, ?>) obj;
            String str3 = (String) entry.getKey();
            CountryFilterItem countryFilterItem2 = new CountryFilterItem(
                    str3,
                    CountryUtils.getCountryNameFromCode(str3),
                    CountryUtils.getCountryFlag(str3),
                    ((Integer) entry.getValue()).intValue(),
                    str3.equalsIgnoreCase(this.selectedCountryCode)
            );
            if (savedCountry != null && savedCountry.equalsIgnoreCase(str3)) {
                countryFilterItem2.setUserCountry(true);
                countryFilterItem = countryFilterItem2;
            } else {
                arrayList2.add(countryFilterItem2);
            }
        }
        // Old Code
       /* for (Map.Entry entry : hashMap.entrySet()) {
            String str3 = (String) entry.getKey();
            CountryFilterItem countryFilterItem2 = new CountryFilterItem(str3, CountryUtils.getCountryNameFromCode(str3), CountryUtils.getCountryFlag(str3), ((Integer) entry.getValue()).intValue(), str3.equalsIgnoreCase(this.selectedCountryCode));
            if (savedCountry != null && savedCountry.equalsIgnoreCase(str3)) {
                countryFilterItem2.setUserCountry(true);
                countryFilterItem = countryFilterItem2;
            } else {
                arrayList2.add(countryFilterItem2);
            }
        }*/
        Collections.sort(arrayList2, new Comparator() { // from class: com.iptv.online.player.live.tv.smart.ui.main.ChannelActivity$$ExternalSyntheticLambda0
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                int compareToIgnoreCase;
                compareToIgnoreCase = ((CountryFilterItem) obj).getCountryName().compareToIgnoreCase(((CountryFilterItem) obj2).getCountryName());
                return compareToIgnoreCase;
            }
        });
        if (countryFilterItem != null) {
            arrayList.add(countryFilterItem);
        }
        arrayList.addAll(arrayList2);
        new CountryFilterDialog(this, arrayList, new CountryFilterDialog.OnCountryFilterListener() { // from class: com.iptv.online.player.live.tv.smart.ui.main.ChannelActivity$$ExternalSyntheticLambda3
            @Override // com.iptv.online.player.live.tv.smart.ui.dialogs.CountryFilterDialog.OnCountryFilterListener
            public final void onCountrySelected(CountryFilterItem countryFilterItem3) {
                ChannelActivity.this.lambda$showCountryFilterDialog$11(countryFilterItem3);
            }
        }).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showCountryFilterDialog$11(CountryFilterItem countryFilterItem) {
        if ("ALL".equalsIgnoreCase(countryFilterItem.getCountryCode())) {
            clearCountryFilter();
        } else {
            applyCountryFilter(countryFilterItem.getCountryCode(), countryFilterItem.getCountryName(), countryFilterItem.getFlagEmoji());
        }
    }

    private void applyCountryFilter(String str, String str2, String str3) {
        this.selectedCountryCode = str;
        this.selectedCountryName = str2;
//        AnalyticsTracker.logAction("ChannelActivity", "apply_country_filter", str2 != null ? str2 : str);
        Log.d("ChannelActivity", "apply_country_filter: " +  str2 != null ? str2 : str);
        ChannelAdapter channelAdapter = this.channelAdapter;
        if (channelAdapter != null) {
            channelAdapter.setCountryFilter(str);
        }
        LinearLayout linearLayout = this.layoutActiveFilter;
        if (linearLayout != null) {
            linearLayout.setVisibility(View.VISIBLE);
        }
        TextView textView = this.tvActiveCountryFlag;
        if (textView != null) {
            textView.setText(str3);
        }
        TextView textView2 = this.tvActiveCountryName;
        if (textView2 != null) {
            textView2.setText(str2);
        }
        ImageButton imageButton = this.btnCountryFilter;
        if (imageButton != null) {
            imageButton.setColorFilter(ContextCompat.getColor(this, R.color.brand_blue));
        }
        updateSubtitle();
    }

    private void clearCountryFilter() {
        this.selectedCountryCode = null;
        this.selectedCountryName = null;
//        AnalyticsTracker.logAction("ChannelActivity", "clear_country_filter", null);
        ChannelAdapter channelAdapter = this.channelAdapter;
        if (channelAdapter != null) {
            channelAdapter.setCountryFilter(null);
        }
        ImageButton imageButton = this.btnCountryFilter;
        if (imageButton != null) {
            imageButton.clearColorFilter();
        }
        LinearLayout linearLayout = this.layoutActiveFilter;
        if (linearLayout != null) {
            linearLayout.setVisibility(View.GONE);
        }
        updateSubtitle();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateSubtitle() {
        ChannelAdapter channelAdapter = this.channelAdapter;
        int itemCount = channelAdapter != null ? channelAdapter.getItemCount() : this.channelList.size();
        PlaylistSource activeSource = this.storageManager.getActiveSource();
        String str = this.selectedCountryName;
        TextView textView = this.tvCategorySubtitle;
        if (str != null) {
            textView.setText(getString(R.string.format_channels_with_country_source, new Object[]{Integer.valueOf(itemCount), this.selectedCountryName, activeSource.getName()}));
        } else {
            textView.setText(getString(R.string.format_channels_with_source, new Object[]{Integer.valueOf(itemCount), activeSource.getName()}));
        }
    }

    private void loadChannels() {
        if ("favorites".equalsIgnoreCase(this.categoryId)) {
            this.channelList = getFavoriteChannels();
        } else if ("recent".equalsIgnoreCase(this.categoryId)) {
            this.channelList = this.storageManager.getRecentChannels();
        }
        this.channelAdapter.setChannels(this.channelList);
        updateSubtitle();
        boolean isEmpty = this.channelList.isEmpty();
        LinearLayout linearLayout = this.layoutEmptyState;
        if (isEmpty) {
            linearLayout.setVisibility(View.VISIBLE);
            this.rvChannels.setVisibility(View.GONE);
            return;
        }
        linearLayout.setVisibility(View.GONE);
        this.rvChannels.setVisibility(View.VISIBLE);
    }

    private List<PlaylistItem> getFavoriteChannels() {
        ArrayList arrayList = new ArrayList();
        Set<String> favoriteIds = this.storageManager.getFavoriteIds();
        for (PlaylistItem playlistItem : this.storageManager.getCachedChannels()) {
            if (favoriteIds.contains(playlistItem.getId())) {
                arrayList.add(playlistItem);
            }
        }
        return arrayList;
    }

    @Override // com.iptv.online.player.live.tv.smart.ui.adapters.ChannelAdapter.OnChannelClickListener
    public void onChannelClick(PlaylistItem playlistItem, int i) {
        if (playlistItem != null) {
//            AnalyticsTracker.logAction("ChannelActivity", "click_channel", playlistItem.getName());
            Log.d("ChannelActivity", "click_channel: " + playlistItem.getName().toString());
        }
        this.storageManager.addRecentChannel(playlistItem);
        List<PlaylistItem> currentChannels = this.channelAdapter.getCurrentChannels();
        int indexOf = currentChannels != null ? currentChannels.indexOf(playlistItem) : -1;
        if (indexOf != -1) {
            i = indexOf;
        }
        PlayerActivity.setPlaybackList(currentChannels);
        Intent intent = new Intent(this, PlayerActivity.class);
        intent.putExtra(PlayerActivity.EXTRA_CHANNEL, playlistItem);
        intent.putExtra(PlayerActivity.EXTRA_CHANNEL_INDEX, i);
//        startAdsActivity(intent);
        startActivity(intent);
    }

    @Override // com.iptv.online.player.live.tv.smart.ui.adapters.ChannelAdapter.OnChannelClickListener
    public void onFavoriteToggle(PlaylistItem playlistItem, int i) {
        if (playlistItem != null) {
//            AnalyticsTracker.logFavorite("ChannelActivity", playlistItem.getName(), this.storageManager.isFavorite(playlistItem.getId()));
            Log.d("ChannelActivity", "onFavoriteToggle: " + playlistItem.getName() + this.storageManager.isFavorite(playlistItem.getId()));
        }
        if ("favorites".equalsIgnoreCase(this.categoryId)) {
            loadChannels();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.adsUtils.custom.CustomActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        sCategoryChannels = null;
    }
}

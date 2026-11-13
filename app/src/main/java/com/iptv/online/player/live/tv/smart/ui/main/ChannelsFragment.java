package com.iptv.online.player.live.tv.smart.ui.main;

// NEW: ChannelsFragment replaces MainActivity when switching to the Channels tab
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.iptv.online.player.live.tv.smart.R;
import com.iptv.online.player.live.tv.smart.data.local.PlaylistStorageManager;
import com.iptv.online.player.live.tv.smart.data.model.Category;
import com.iptv.online.player.live.tv.smart.data.model.PlaylistItem;
import com.iptv.online.player.live.tv.smart.data.model.PlaylistSource;
import com.iptv.online.player.live.tv.smart.data.network.ApiClient;
import com.iptv.online.player.live.tv.smart.data.network.XtreamCodesApi;
import com.iptv.online.player.live.tv.smart.data.parser.M3UParser;
import com.iptv.online.player.live.tv.smart.ui.adapters.CategoryAdapter;
import com.iptv.online.player.live.tv.smart.ui.cast.CastToTvActivity;
import com.iptv.online.player.live.tv.smart.ui.dialogs.AddPlaylistDialog;
import com.iptv.online.player.live.tv.smart.ui.dialogs.PinLockDialog;
import com.iptv.online.player.live.tv.smart.ui.help.HowToUseActivity;
import com.iptv.online.player.live.tv.smart.ui.settings.HomeSettingActivity;
import com.iptv.online.player.live.tv.smart.ui.vod.VodMainActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

// NEW: Fragment that manages the categories and channels list
public class ChannelsFragment extends Fragment implements CategoryAdapter.OnCategoryClickListener {

    // NEW: Member variables for UI components and data
    private CategoryAdapter categoryAdapter;
    private EditText etSearchQuery;
    private LinearLayout layoutEmptyState;
    private LinearLayout layoutSearch;
    private ProgressBar mainProgressBar;
    private RecyclerView rvCategories;
    private PlaylistStorageManager storageManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView tvActivePlaylistName;

    private List<PlaylistItem> allChannels = new ArrayList<>();
    private List<Category> allCategories = new ArrayList<>();
    private final Set<String> unlockedCategories = new HashSet<>();
    private String currentActiveSourceId = null;

    // NEW: Create and return the Fragment's view hierarchy
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_channels, container, false);
    }

    // NEW: Initialize views, adapter, listeners, and load data once view is created
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        storageManager = new PlaylistStorageManager(requireContext());
        currentActiveSourceId = storageManager.getActiveSourceId();

        initViews(view);
        setupRecyclerView();
        setupListeners(view);
        loadPlaylist(false);
    }

    // NEW: OnResume refreshes categories if the active playlist changed
    @Override
    public void onResume() {
        super.onResume();
        if (storageManager == null) return;
        String activeSourceId = storageManager.getActiveSourceId();
        if (currentActiveSourceId == null || !currentActiveSourceId.equals(activeSourceId)) {
            currentActiveSourceId = activeSourceId;
            loadPlaylist(false);
            return;
        }
        refreshSpecialCategories();
        if (categoryAdapter != null) {
            categoryAdapter.updateAdConfig();
            categoryAdapter.notifyDataSetChanged();
        }
    }

    // NEW: Handle screen orientation changes cleanly
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setupLayoutManager();
    }

    // NEW: Bind UI views from the layout
    private void initViews(View view) {
        tvActivePlaylistName = view.findViewById(R.id.tvActivePlaylistName);
        rvCategories = view.findViewById(R.id.rvCategories);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        layoutEmptyState = view.findViewById(R.id.layoutEmptyState);
        layoutSearch = view.findViewById(R.id.layoutSearch);
        etSearchQuery = view.findViewById(R.id.etSearchQuery);
        mainProgressBar = view.findViewById(R.id.mainProgressBar);
        swipeRefreshLayout.setColorSchemeResources(R.color.purple_accent, R.color.cyan_accent);
    }

    // NEW: Set up the category list RecyclerView and Adapter
    private void setupRecyclerView() {
        setupLayoutManager();
        categoryAdapter = new CategoryAdapter(requireActivity(), storageManager, this);
        rvCategories.setAdapter(categoryAdapter);
    }

    // NEW: Configure Grid layout depending on screen orientation
    private void setupLayoutManager() {
        if (!isAdded()) return;
        int orientation = getResources().getConfiguration().orientation;
        final int spanCount = (getResources().getConfiguration().screenWidthDp < 600 && orientation != Configuration.ORIENTATION_LANDSCAPE) ? 1 : 2;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), spanCount);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (categoryAdapter == null || !categoryAdapter.isAdPosition(position)) {
                    return 1;
                }
                return spanCount;
            }
        });
        rvCategories.setLayoutManager(gridLayoutManager);
    }

    // CHANGED: Set up button click and search listeners (removed/commented btnOpenVod, btnSearchToggle, btnAddPlaylist)
    private void setupListeners(View view) {
        swipeRefreshLayout.setOnRefreshListener(() -> loadPlaylist(true));

        // CHANGED: Commented out btnOpenVod, btnSearchToggle, and btnAddPlaylist functionality per request
        /*
        View btnOpenVod = view.findViewById(R.id.btnOpenVod);
        if (btnOpenVod != null) {
            btnOpenVod.setOnClickListener(v -> startActivity(new Intent(requireContext(), VodMainActivity.class)));
        }

        View btnSearchToggle = view.findViewById(R.id.btnSearchToggle);
        if (btnSearchToggle != null) {
            btnSearchToggle.setOnClickListener(v -> {
                if (layoutSearch.getVisibility() == View.VISIBLE) {
                    layoutSearch.setVisibility(View.GONE);
                    etSearchQuery.setText("");
                } else {
                    layoutSearch.setVisibility(View.VISIBLE);
                    etSearchQuery.requestFocus();
                }
            });
        }

        View btnAddPlaylist = view.findViewById(R.id.btnAddPlaylist);
        if (btnAddPlaylist != null) {
            btnAddPlaylist.setOnClickListener(v -> {
                new AddPlaylistDialog(requireActivity(), storageManager, playlistSource -> {
                    tvActivePlaylistName.setText(playlistSource.getName());
                    loadPlaylist(true);
                }).show();
            });
        }
        */

        // NEW: Wire up activity_home appbar buttons in fragment (Cast, Help, Settings)
        View btnCast = view.findViewById(R.id.btnCast);
        if (btnCast != null) {
            btnCast.setOnClickListener(v -> startActivity(new Intent(requireContext(), CastToTvActivity.class)));
        }

        View btnHelp = view.findViewById(R.id.btnHelp);
        if (btnHelp != null) {
            btnHelp.setOnClickListener(v -> startActivity(new Intent(requireContext(), HowToUseActivity.class)));
        }

        View btnHomeSettings = view.findViewById(R.id.btnHomeSettings);
        if (btnHomeSettings != null) {
            btnHomeSettings.setOnClickListener(v -> {
                HomeSettingActivity.setAvailableCategories(allCategories);
                startActivity(new Intent(requireContext(), HomeSettingActivity.class));
            });
        }

        view.findViewById(R.id.btnClearSearch).setOnClickListener(v -> etSearchQuery.setText(""));

        etSearchQuery.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (categoryAdapter != null) {
                    categoryAdapter.getFilter().filter(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        etSearchQuery.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == 3) {
                String query = etSearchQuery.getText().toString();
                if (categoryAdapter != null) {
                    categoryAdapter.getFilter().filter(query);
                }
                return true;
            }
            return false;
        });

        View btnRefresh = view.findViewById(R.id.btnRefresh);
        if (btnRefresh != null) {
            btnRefresh.setOnClickListener(v -> loadPlaylist(true));
        }

        view.findViewById(R.id.btnEmptyAdd).setOnClickListener(v -> {
            new AddPlaylistDialog(requireActivity(), storageManager, playlistSource -> {
                tvActivePlaylistName.setText(playlistSource.getName());
                loadPlaylist(true);
            }).show();
        });
    }

    // NEW: Load the channels of the active playlist
    public void loadPlaylist(boolean forceRefresh) {
        if (!isAdded()) return;
        PlaylistSource activeSource = storageManager.getActiveSource();
        if (activeSource == null) {
            checkEmptyState();
            return;
        }

        tvActivePlaylistName.setText(activeSource.getName());

        if (!forceRefresh) {
            List<PlaylistItem> cachedChannels = storageManager.getCachedChannels(activeSource.getId());
            if (!cachedChannels.isEmpty()) {
                mainProgressBar.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
                populateCategories(cachedChannels);
                return;
            }
        }

        mainProgressBar.setVisibility(View.VISIBLE);
        layoutEmptyState.setVisibility(View.GONE);

        if (activeSource.getType() == PlaylistSource.SourceType.SINGLE_STREAM) {
            loadSingleStreamPlaylist(activeSource);
        } else if (activeSource.getType() == PlaylistSource.SourceType.LOCAL_FILE) {
            loadLocalFilePlaylist(activeSource);
        } else if (activeSource.getType() == PlaylistSource.SourceType.XTREAM_CODES) {
            loadXtreamPlaylist(activeSource);
        } else {
            loadM3UPlaylist(activeSource);
        }
    }

    // NEW: Handle single stream playlist type
    private void loadSingleStreamPlaylist(PlaylistSource playlistSource) {
        ArrayList<PlaylistItem> list = new ArrayList<>();
        list.add(new PlaylistItem("stream_" + playlistSource.getId(), playlistSource.getName(), playlistSource.getUrl(), null, "Live Stream"));
        playlistSource.setChannelCount(1);
        storageManager.addOrUpdateSource(playlistSource);
        storageManager.saveCachedChannels(playlistSource.getId(), list);
        mainProgressBar.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(false);
        populateCategories(list);
    }

    // NEW: Handle local file playlist type
    private void loadLocalFilePlaylist(final PlaylistSource playlistSource) {
        new Thread(() -> {
            try {
                String url = playlistSource.getUrl();
                String localFilePath = playlistSource.getLocalFilePath();
                java.io.InputStream inputStream = null;

                if (url != null && (url.startsWith("content://") || url.startsWith("file://"))) {
                    inputStream = requireActivity().getContentResolver().openInputStream(android.net.Uri.parse(url));
                } else if (localFilePath != null && !localFilePath.isEmpty()) {
                    inputStream = requireActivity().getContentResolver().openInputStream(android.net.Uri.parse(localFilePath));
                }

                if (inputStream == null) {
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            mainProgressBar.setVisibility(View.GONE);
                            swipeRefreshLayout.setRefreshing(false);
                            checkEmptyState();
                        });
                    }
                    return;
                }

                final M3UParser.ParseResult parseResult;
                try (java.io.InputStream stream = inputStream) {
                    parseResult = M3UParser.parse(stream);
                }

                if (parseResult.channels.isEmpty() && url != null && !url.isEmpty()) {
                    PlaylistItem item = new PlaylistItem("ch_1_" + Math.abs(url.hashCode()), playlistSource.getName(), url, null, "Live Stream");
                    parseResult.channels.add(item);
                }

                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        mainProgressBar.setVisibility(View.GONE);
                        swipeRefreshLayout.setRefreshing(false);
                        storageManager.saveCachedChannels(playlistSource.getId(), parseResult.channels);
                        playlistSource.setChannelCount(parseResult.channels.size());
                        storageManager.addOrUpdateSource(playlistSource);
                        populateCategories(parseResult.channels);
                    });
                }
            } catch (final Exception e) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        mainProgressBar.setVisibility(View.GONE);
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(requireContext(), getString(R.string.err_load_local_file, e.getMessage()), Toast.LENGTH_SHORT).show();
                        checkEmptyState();
                    });
                }
            }
        }).start();
    }

    // NEW: Handle Xtream Codes playlist type
    private void loadXtreamPlaylist(final PlaylistSource playlistSource) {
        XtreamCodesApi.authenticateAndLoad(playlistSource, new XtreamCodesApi.ApiCallback<M3UParser.ParseResult>() {
            @Override
            public void onSuccess(M3UParser.ParseResult parseResult) {
                if (!isAdded()) return;
                mainProgressBar.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
                storageManager.saveCachedChannels(playlistSource.getId(), parseResult.channels);
                playlistSource.setChannelCount(parseResult.channels.size());
                storageManager.addOrUpdateSource(playlistSource);
                populateCategories(parseResult.channels);
            }

            @Override
            public void onError(Exception exc) {
                if (!isAdded()) return;
                mainProgressBar.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(requireContext(), getString(R.string.err_connect_xtream, exc.getMessage()), Toast.LENGTH_LONG).show();
                checkEmptyState();
            }
        });
    }

    // NEW: Handle remote M3U URL playlist type
    private void loadM3UPlaylist(final PlaylistSource playlistSource) {
        String url = playlistSource.getUrl() != null ? playlistSource.getUrl().trim() : "";
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "https://" + url;
        }

        ApiClient.getClient().newCall(new Request.Builder().url(url).header("User-Agent", ApiClient.DEFAULT_USER_AGENT).build())
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull final IOException e) {
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(() -> {
                                mainProgressBar.setVisibility(View.GONE);
                                swipeRefreshLayout.setRefreshing(false);
                                Toast.makeText(requireContext(), getString(R.string.err_download_playlist, e.getMessage()), Toast.LENGTH_LONG).show();
                                checkEmptyState();
                            });
                        }
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull final Response response) {
                        try {
                            if (response.isSuccessful() && response.body() != null) {
                                final M3UParser.ParseResult parse = M3UParser.parse(response.body().byteStream());
                                if (parse.channels.isEmpty() && !playlistSource.getUrl().isEmpty()) {
                                    parse.channels.add(new PlaylistItem("ch_1_" + Math.abs(playlistSource.getUrl().hashCode()), playlistSource.getName(), playlistSource.getUrl(), null, "Live Stream"));
                                }
                                if (getActivity() != null) {
                                    getActivity().runOnUiThread(() -> {
                                        mainProgressBar.setVisibility(View.GONE);
                                        swipeRefreshLayout.setRefreshing(false);
                                        storageManager.saveCachedChannels(playlistSource.getId(), parse.channels);
                                        playlistSource.setChannelCount(parse.channels.size());
                                        storageManager.addOrUpdateSource(playlistSource);
                                        populateCategories(parse.channels);
                                    });
                                }
                                return;
                            }
                            if (getActivity() != null) {
                                getActivity().runOnUiThread(() -> {
                                    mainProgressBar.setVisibility(View.GONE);
                                    swipeRefreshLayout.setRefreshing(false);
                                    Toast.makeText(requireContext(), getString(R.string.err_server_code, response.code()), Toast.LENGTH_SHORT).show();
                                    checkEmptyState();
                                });
                            }
                        } catch (Exception e) {
                            if (getActivity() != null) {
                                getActivity().runOnUiThread(() -> {
                                    mainProgressBar.setVisibility(View.GONE);
                                    swipeRefreshLayout.setRefreshing(false);
                                    Toast.makeText(requireContext(), getString(R.string.err_parse_m3u, e.getMessage()), Toast.LENGTH_SHORT).show();
                                    checkEmptyState();
                                });
                            }
                        }
                    }
                });
    }

    // NEW: Organize parsed channels into categories (All, Favorites, Recent, and grouped titles)
    private void populateCategories(List<PlaylistItem> list) {
        allChannels = new ArrayList<>(list);
        HashMap<String, List<PlaylistItem>> categoryMap = new HashMap<>();

        for (PlaylistItem channel : list) {
            String groupTitle = channel.getGroupTitle();
            if (!categoryMap.containsKey(groupTitle)) {
                categoryMap.put(groupTitle, new ArrayList<>());
            }
            categoryMap.get(groupTitle).add(channel);
        }

        allCategories.clear();

        Category allCategory = new Category("all", getString(R.string.all_channels));
        allCategory.setChannels(allChannels);
        allCategories.add(allCategory);

        List<PlaylistItem> favoriteChannels = getFavoriteChannels();
        Category favoritesCategory = new Category("favorites", "⭐ " + getString(R.string.favorites));
        favoritesCategory.setChannels(favoriteChannels);
        allCategories.add(favoritesCategory);

        List<PlaylistItem> recentChannels = storageManager.getRecentChannels();
        Category recentCategory = new Category("recent", "🕒 " + getString(R.string.recent));
        recentCategory.setChannels(recentChannels);
        allCategories.add(recentCategory);

        ArrayList<String> groupNames = new ArrayList<>(categoryMap.keySet());
        Collections.sort(groupNames, String.CASE_INSENSITIVE_ORDER);

        for (String group : groupNames) {
            Category category = new Category(group, group);
            category.setChannels(categoryMap.get(group));
            allCategories.add(category);
        }

        if (categoryAdapter != null) {
            categoryAdapter.setCategories(allCategories);
        }
        checkEmptyState();
    }

    // NEW: Refresh channels under dynamic categories like favorites and recent
    private void refreshSpecialCategories() {
        for (Category category : allCategories) {
            if ("favorites".equalsIgnoreCase(category.getId())) {
                category.setChannels(getFavoriteChannels());
            } else if ("recent".equalsIgnoreCase(category.getId())) {
                category.setChannels(storageManager.getRecentChannels());
            }
        }
        if (categoryAdapter != null) {
            categoryAdapter.notifyDataSetChanged();
        }
    }

    // NEW: Get channels marked as favorites
    private List<PlaylistItem> getFavoriteChannels() {
        ArrayList<PlaylistItem> favorites = new ArrayList<>();
        Set<String> favoriteIds = storageManager.getFavoriteIds();
        for (PlaylistItem channel : allChannels) {
            if (favoriteIds.contains(channel.getId())) {
                favorites.add(channel);
            }
        }
        return favorites;
    }

    // NEW: Toggle empty state illustration if there are no categories
    private void checkEmptyState() {
        int count = categoryAdapter != null ? categoryAdapter.getItemCount() : 0;
        if (count == 0) {
            layoutEmptyState.setVisibility(View.VISIBLE);
            rvCategories.setVisibility(View.GONE);
        } else {
            layoutEmptyState.setVisibility(View.GONE);
            rvCategories.setVisibility(View.VISIBLE);
        }
    }

    // NEW: OnCategoryClickListener implementation - verifies PIN if category is locked
    @Override
    public void onCategoryClick(final Category category, int position) {
        if (category == null) return;
        if (storageManager.isCategoryLocked(category.getName()) && !unlockedCategories.contains(category.getName())) {
            new PinLockDialog(requireActivity(), storageManager, PinLockDialog.Mode.UNLOCK, () -> {
                unlockedCategories.add(category.getName());
                openCategoryChannels(category);
            }).show();
        } else {
            openCategoryChannels(category);
        }
    }

    // NEW: OnCategoryClickListener implementation - lock / unlock categories on long click
    @Override
    public void onCategoryLongClick(final Category category, final int position) {
        if (category == null) return;
        String id = category.getId();
        if ("favorites".equalsIgnoreCase(id) || "recent".equalsIgnoreCase(id)) return;

        if (!storageManager.isParentalControlEnabled()) {
            Toast.makeText(requireContext(), R.string.err_set_pin_first, Toast.LENGTH_SHORT).show();
            new PinLockDialog(requireActivity(), storageManager, PinLockDialog.Mode.SET_NEW_PIN, () -> {
                storageManager.setCategoryLocked(category.getName(), true);
                if (categoryAdapter != null) {
                    categoryAdapter.notifyItemChanged(position);
                }
                Toast.makeText(requireContext(), category.getName() + " locked!", Toast.LENGTH_SHORT).show();
            }).show();
        } else if (storageManager.isCategoryLocked(category.getName())) {
            new PinLockDialog(requireActivity(), storageManager, PinLockDialog.Mode.UNLOCK, () -> {
                storageManager.setCategoryLocked(category.getName(), false);
                unlockedCategories.remove(category.getName());
                if (categoryAdapter != null) {
                    categoryAdapter.notifyItemChanged(position);
                }
                Toast.makeText(requireContext(), category.getName() + " unlocked", Toast.LENGTH_SHORT).show();
            }).show();
        } else {
            storageManager.setCategoryLocked(category.getName(), true);
            unlockedCategories.remove(category.getName());
            if (categoryAdapter != null) {
                categoryAdapter.notifyItemChanged(position);
            }
            Toast.makeText(requireContext(), category.getName() + " locked!", Toast.LENGTH_SHORT).show();
        }
    }

    // NEW: Launch ChannelActivity to view channels belonging to the clicked category
    private void openCategoryChannels(Category category) {
        List<PlaylistItem> channels;
        if ("favorites".equalsIgnoreCase(category.getId())) {
            channels = getFavoriteChannels();
        } else if ("recent".equalsIgnoreCase(category.getId())) {
            channels = storageManager.getRecentChannels();
        } else if ("all".equalsIgnoreCase(category.getId())) {
            channels = allChannels;
        } else {
            channels = category.getChannels();
        }
        ChannelActivity.setCategoryChannels(channels);
        Intent intent = new Intent(requireContext(), ChannelActivity.class);
        intent.putExtra(ChannelActivity.EXTRA_CATEGORY_ID, category.getId());
        intent.putExtra(ChannelActivity.EXTRA_CATEGORY_NAME, category.getName());
        startActivity(intent);
    }
}

package com.iptv.online.player.live.tv.smart.ui.settings;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.media3.common.MimeTypes;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.iptv.online.player.live.tv.smart.AdsPackage.CustomActivity;
import com.iptv.online.player.live.tv.smart.LanguageActivity;
import com.iptv.online.player.live.tv.smart.R;
import com.iptv.online.player.live.tv.smart.data.local.PlaylistStorageManager;
import com.iptv.online.player.live.tv.smart.data.model.Category;
import com.iptv.online.player.live.tv.smart.data.model.PlaylistSource;
import com.iptv.online.player.live.tv.smart.ui.adapters.PlaylistManageAdapter;
import com.iptv.online.player.live.tv.smart.ui.dialogs.AddPlaylistDialog;
import com.iptv.online.player.live.tv.smart.ui.dialogs.DeletePlaylistDialog;
import com.iptv.online.player.live.tv.smart.ui.dialogs.PinLockDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all")
public class HomeSettingActivity extends com.iptv.online.player.live.tv.smart.AdsPackage.CustomActivity {
    private PlaylistStorageManager storageManager;
    private TextView tvCurrentLanguageSubtitle;
    private TextView tvCurrentThemeSubtitle;

    // NEW: Fields for Playlist Management and Security & Parental Control
    private static List<Category> sAvailableCategories = new ArrayList<>();
    private PlaylistManageAdapter playlistAdapter;
    private RecyclerView rvPlaylists;
    private TextView tvPinStatus;
    private MaterialButton btnConfigurePin;
    private MaterialButton btnManageLockedCategories;
    private MaterialButton btnDisablePin;

    // NEW: Provide categories to be locked/unlocked via Parental Control
    public static void setAvailableCategories(List<Category> list) {
        sAvailableCategories = list != null ? new ArrayList<>(list) : new ArrayList<>();
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_home_setting);
        this.storageManager = new PlaylistStorageManager(this);
        initViews();
        updateSubtitles();
        setupListeners();
        loadPlaylistAndPinData(); // NEW: Initial load for playlist and parental control settings
    }

    @Override
    public void onResume() {
        super.onResume();
        updateSubtitles();
        loadPlaylistAndPinData(); // NEW: Refresh playlist list and PIN status when returning
    }

    private void initViews() {
        this.tvCurrentThemeSubtitle = (TextView) findViewById(R.id.tvCurrentThemeSubtitle);
        this.tvCurrentLanguageSubtitle = (TextView) findViewById(R.id.tvCurrentLanguageSubtitle);
        findViewById(R.id.btnSettingBack).setOnClickListener(v -> setOnBackPressedClick());

        // NEW: Initialize Playlist Management views
        this.rvPlaylists = (RecyclerView) findViewById(R.id.rvPlaylistsList);
        this.rvPlaylists.setLayoutManager(new LinearLayoutManager(this));
        this.playlistAdapter = new PlaylistManageAdapter(new PlaylistManageAdapter.OnPlaylistActionCallback() {
            @Override
            public void onSelectPlaylist(PlaylistSource playlistSource) {
                HomeSettingActivity.this.storageManager.setActiveSourceId(playlistSource.getId());
                HomeSettingActivity.this.playlistAdapter.setSources(HomeSettingActivity.this.storageManager.getPlaylistSources(), playlistSource.getId());
                Toast.makeText(HomeSettingActivity.this, getString(R.string.msg_switched_to, playlistSource.getName()), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDeletePlaylist(final PlaylistSource playlistSource) {
                new DeletePlaylistDialog(HomeSettingActivity.this, playlistSource, () -> {
                    HomeSettingActivity.this.storageManager.deleteSource(playlistSource.getId());
                    HomeSettingActivity.this.loadPlaylistAndPinData();
                }).show();
            }
        });
        this.rvPlaylists.setAdapter(this.playlistAdapter);

        // NEW: Add Playlist button in settings
        findViewById(R.id.btnAddPlaylistSetting).setOnClickListener(v -> {
            new AddPlaylistDialog(this, this.storageManager, playlistSource -> {
                this.storageManager.setActiveSourceId(playlistSource.getId());
                loadPlaylistAndPinData();
            }).show();
        });

        // NEW: Initialize Parental Control views
        this.tvPinStatus = (TextView) findViewById(R.id.tvPinStatus);
        this.btnConfigurePin = (MaterialButton) findViewById(R.id.btnConfigurePin);
        this.btnManageLockedCategories = (MaterialButton) findViewById(R.id.btnManageLockedCategories);
        this.btnDisablePin = (MaterialButton) findViewById(R.id.btnDisablePin);

        this.btnConfigurePin.setOnClickListener(v -> {
            new PinLockDialog(this, this.storageManager, PinLockDialog.Mode.SET_NEW_PIN, () -> {
                updatePinStatus();
            }).show();
        });

        this.btnManageLockedCategories.setOnClickListener(v -> {
            if (!this.storageManager.isParentalControlEnabled()) {
                Toast.makeText(this, R.string.err_set_pin_first_excl, Toast.LENGTH_SHORT).show();
                new PinLockDialog(this, this.storageManager, PinLockDialog.Mode.SET_NEW_PIN, () -> {
                    updatePinStatus();
                    showCategoryLockDialog();
                }).show();
                return;
            }
            showCategoryLockDialog();
        });

        this.btnDisablePin.setOnClickListener(v -> {
            new PinLockDialog(this, this.storageManager, PinLockDialog.Mode.UNLOCK, () -> {
                this.storageManager.removeParentalPin();
                Toast.makeText(this, R.string.msg_parental_disabled, Toast.LENGTH_SHORT).show();
                updatePinStatus();
            }).show();
        });
    }

    // NEW: Refresh playlist list and PIN status
    private void loadPlaylistAndPinData() {
        if (this.playlistAdapter != null && this.storageManager != null) {
            this.playlistAdapter.setSources(this.storageManager.getPlaylistSources(), this.storageManager.getActiveSourceId());
        }
        updatePinStatus();
    }

    // NEW: Update Parental Control status text and button labels
    private void updatePinStatus() {
        if (this.storageManager == null || this.tvPinStatus == null) return;
        boolean isParentalControlEnabled = this.storageManager.isParentalControlEnabled();
        if (isParentalControlEnabled) {
            this.tvPinStatus.setText(R.string.status_parental_enabled);
            this.btnConfigurePin.setText(R.string.change_parental_pin);
            this.btnDisablePin.setVisibility(View.VISIBLE);
        } else {
            this.tvPinStatus.setText(R.string.status_parental_disabled);
            this.btnConfigurePin.setText(R.string.set_pin);
            this.btnDisablePin.setVisibility(View.GONE);
        }
    }

    // NEW: Multi-choice dialog to lock or unlock individual categories
    private void showCategoryLockDialog() {
        if (sAvailableCategories.isEmpty()) {
            Toast.makeText(this, R.string.msg_no_categories_playlist, Toast.LENGTH_SHORT).show();
            return;
        }
        ArrayList<String> categoryNames = new ArrayList<>();
        for (Category category : sAvailableCategories) {
            String id = category.getId();
            if (!"favorites".equalsIgnoreCase(id) && !"recent".equalsIgnoreCase(id)) {
                categoryNames.add(category.getName());
            }
        }
        if (categoryNames.isEmpty()) {
            Toast.makeText(this, R.string.msg_no_categories_available, Toast.LENGTH_SHORT).show();
            return;
        }
        final String[] strArr = categoryNames.toArray(new String[0]);
        boolean[] zArr = new boolean[strArr.length];
        for (int i = 0; i < strArr.length; i++) {
            zArr[i] = this.storageManager.isCategoryLocked(strArr[i]);
        }
        final AlertDialog dialog = new AlertDialog.Builder(this, R.style.CustomDialogTheme)
                .setTitle(R.string.dialog_title_select_categories_lock)
                .setMultiChoiceItems(strArr, zArr, (dialogInterface, which, isChecked) -> {
                    this.storageManager.setCategoryLocked(strArr[which], isChecked);
                })
                .setPositiveButton(R.string.save, (dialogInterface, which) -> dialogInterface.dismiss())
                .setNegativeButton(R.string.cancel, null)
                .create();

        dialog.setOnShowListener(dialogInterface -> {
            Button button = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
            if (button != null) {
                button.setTextColor(ContextCompat.getColor(this, R.color.text_secondary));
            }
        });
        dialog.show();
    }

    private void updateSubtitles() {
        int themeMode = this.storageManager.getThemeMode();
        TextView textView = this.tvCurrentThemeSubtitle;
        if (themeMode == 2) {
            textView.setText(R.string.theme_dark);
        } else {
            textView.setText(R.string.theme_light_default);
        }
        this.tvCurrentLanguageSubtitle.setText(getLanguageDisplayName(this.storageManager.getAppLanguage()));
    }

    private String getLanguageDisplayName(String str) {
        return "gu".equalsIgnoreCase(str) ? "ગુજરાતી" : "hi".equalsIgnoreCase(str) ? "हिंदी" : "de".equalsIgnoreCase(str) ? "Deutsch" : "fr".equalsIgnoreCase(str) ? "Français" : "it".equalsIgnoreCase(str) ? "Italiano" : "pt".equalsIgnoreCase(str) ? "Português" : "tr".equalsIgnoreCase(str) ? "Türkçe" : "English";
    }

    private void setupListeners() {
        findViewById(R.id.settingRowTheme).setOnClickListener(v -> showThemeModeDialog());
        findViewById(R.id.settingRowLanguage).setOnClickListener(v -> {
            Intent intent = new Intent(this, LanguageActivity.class);
            intent.putExtra("EXTRA_FROM_SETTINGS", true);
            startActivity(intent);
        });
        findViewById(R.id.settingRowRate).setOnClickListener(v -> openPlayStoreForRating());
        findViewById(R.id.settingRowShare).setOnClickListener(v -> shareApp());
        findViewById(R.id.settingRowPrivacy).setOnClickListener(v -> showPrivacyPolicyDialog());
    }

    private void showThemeModeDialog() {
        final AlertDialog create = new AlertDialog.Builder(this, R.style.CustomDialogTheme)
                .setTitle(R.string.select_theme_mode)
                .setSingleChoiceItems(new String[]{getString(R.string.theme_option_light), getString(R.string.theme_option_dark)}, this.storageManager.getThemeMode() == 2 ? 1 : 0, (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    int i2 = i == 1 ? 2 : 1;
                    this.storageManager.setThemeMode(i2);
                    updateSubtitles();
                    recreate();
                })
                .setNegativeButton(R.string.cancel, null)
                .create();

        create.setOnShowListener(dialogInterface -> {
            Button button = create.getButton(DialogInterface.BUTTON_NEGATIVE);
            if (button != null) {
                button.setTextColor(ContextCompat.getColor(this, R.color.text_secondary));
            }
        });
        create.show();
    }

    private void openPlayStoreForRating() {
        String packageName = getPackageName();
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));
        } catch (ActivityNotFoundException unused) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)));
        }
    }

    private void shareApp() {
        String str = "Check out IPTV Player Pro! Stream Live TV, M3U playlists, and Xtream Codes with super fast HD playback.\n\nDownload now: https://play.google.com/store/apps/details?id=" + getPackageName();
        try {
            File file = new File(getCacheDir(), "images");
            if (!file.exists()) {
                file.mkdirs();
            }
            File file2 = new File(file, "banner.png");
            Bitmap decodeResource = BitmapFactory.decodeResource(getResources(), R.drawable.banner);
            if (decodeResource != null) {
                FileOutputStream fileOutputStream = new FileOutputStream(file2);
                decodeResource.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                fileOutputStream.close();
                Uri uriForFile = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", file2);
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType(MimeTypes.IMAGE_PNG);
                intent.putExtra(Intent.EXTRA_SUBJECT, "IPTV Player Pro");
                intent.putExtra(Intent.EXTRA_TEXT, str);
                intent.putExtra(Intent.EXTRA_STREAM, uriForFile);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(Intent.createChooser(intent, "Share IPTV Player Pro via"));
                return;
            }
        } catch (Exception unused) {
        }
        Intent intent2 = new Intent(Intent.ACTION_SEND);
        intent2.setType("text/plain");
        intent2.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
        intent2.putExtra(Intent.EXTRA_TEXT, str);
        startActivity(Intent.createChooser(intent2, getString(R.string.share_chooser_title)));
    }

    private void showPrivacyPolicyDialog() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW));
        } catch (ActivityNotFoundException unused) {
            startActivity(new Intent(Intent.ACTION_VIEW));
        }
    }
}

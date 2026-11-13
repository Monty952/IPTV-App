package com.iptv.online.player.live.tv.smart.ui.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
//import com.adsUtils.adsloader.nativeads.NativeLoader;
//import com.adsUtils.adsloader.nativeads.NativeType;
//import com.adsUtils.analytics.AnalyticsTracker;
//import com.adsUtils.custom.CustomActivity;
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
import com.iptv.online.player.live.tv.smart.ui.main.SettingMainActivity;
import java.util.ArrayList;
import java.util.List;
@SuppressWarnings("all")

/* loaded from: classes5.dex */
public class SettingMainActivity extends com.iptv.online.player.live.tv.smart.AdsPackage.CustomActivity {
    private static List<Category> sAvailableCategories = new ArrayList();
    private PlaylistManageAdapter adapter;
    private MaterialButton btnConfigurePin;
    private MaterialButton btnDisablePin;
    private MaterialButton btnManageLockedCategories;
    private RecyclerView rvPlaylists;
    private PlaylistStorageManager storageManager;
    private TextView tvCurrentLanguageSubtitle;
    private TextView tvCurrentThemeSubtitle;
    private TextView tvPinStatus;

    public static void setAvailableCategories(List<Category> list) {
        sAvailableCategories = list != null ? new ArrayList(list) : new ArrayList();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.adsUtils.custom.CustomActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_setting_main);
//        NativeLoader.showNativeAds(this, (LinearLayout) findViewById(R.id.llAdsView), NativeType.BIG_NATIVE);
        this.storageManager = new PlaylistStorageManager(this);
        initViews();
        loadData();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.adsUtils.custom.CustomActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        loadData();
    }

    private void initViews() {
        findViewById(R.id.btnSettingBack).setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.main.SettingMainActivity$$ExternalSyntheticLambda15
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                SettingMainActivity.this.lambda$initViews$0(view);
            }
        });
        this.rvPlaylists = (RecyclerView) findViewById(R.id.rvPlaylistsList);
        this.tvPinStatus = (TextView) findViewById(R.id.tvPinStatus);
        this.btnConfigurePin = (MaterialButton) findViewById(R.id.btnConfigurePin);
        this.btnManageLockedCategories = (MaterialButton) findViewById(R.id.btnManageLockedCategories);
        this.btnDisablePin = (MaterialButton) findViewById(R.id.btnDisablePin);
        this.tvCurrentThemeSubtitle = (TextView) findViewById(R.id.tvCurrentThemeSubtitle);
        this.tvCurrentLanguageSubtitle = (TextView) findViewById(R.id.tvCurrentLanguageSubtitle);
        this.rvPlaylists.setLayoutManager(new LinearLayoutManager(this));
        PlaylistManageAdapter playlistManageAdapter = new PlaylistManageAdapter(new AnonymousClass1());
        this.adapter = playlistManageAdapter;
        this.rvPlaylists.setAdapter(playlistManageAdapter);
        findViewById(R.id.btnAddPlaylistSetting).setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.main.SettingMainActivity$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                SettingMainActivity.this.lambda$initViews$2(view);
            }
        });
        this.btnConfigurePin.setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.main.SettingMainActivity$$ExternalSyntheticLambda2
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                SettingMainActivity.this.lambda$initViews$4(view);
            }
        });
        this.btnManageLockedCategories.setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.main.SettingMainActivity$$ExternalSyntheticLambda3
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                SettingMainActivity.this.lambda$initViews$6(view);
            }
        });
        this.btnDisablePin.setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.main.SettingMainActivity$$ExternalSyntheticLambda4
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                SettingMainActivity.this.lambda$initViews$8(view);
            }
        });
        findViewById(R.id.settingRowTheme).setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.main.SettingMainActivity$$ExternalSyntheticLambda5
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                SettingMainActivity.this.lambda$initViews$9(view);
            }
        });
        findViewById(R.id.settingRowLanguage).setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.main.SettingMainActivity$$ExternalSyntheticLambda6
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                SettingMainActivity.this.lambda$initViews$10(view);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initViews$0(View view) {
        setOnBackPressedClick();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.iptv.online.player.live.tv.smart.ui.main.SettingMainActivity$1  reason: invalid class name */
    /* loaded from: classes5.dex */
    public class AnonymousClass1 implements PlaylistManageAdapter.OnPlaylistActionCallback {
        AnonymousClass1() {
        }

        @Override // com.iptv.online.player.live.tv.smart.ui.adapters.PlaylistManageAdapter.OnPlaylistActionCallback
        public void onSelectPlaylist(PlaylistSource playlistSource) {
//            AnalyticsTracker.logPlaylistAction("SettingMainActivity", "select_playlist", playlistSource.getName());
            SettingMainActivity.this.storageManager.setActiveSourceId(playlistSource.getId());
            SettingMainActivity.this.adapter.setSources(SettingMainActivity.this.storageManager.getPlaylistSources(), playlistSource.getId());
            SettingMainActivity settingMainActivity = SettingMainActivity.this;
            Toast.makeText(settingMainActivity, settingMainActivity.getString(R.string.msg_switched_to, new Object[]{playlistSource.getName()}), 0).show();
        }

        @Override // com.iptv.online.player.live.tv.smart.ui.adapters.PlaylistManageAdapter.OnPlaylistActionCallback
        public void onDeletePlaylist(final PlaylistSource playlistSource) {
//            AnalyticsTracker.logPlaylistAction("SettingMainActivity", "delete_playlist", playlistSource.getName());
            new DeletePlaylistDialog(SettingMainActivity.this, playlistSource, new DeletePlaylistDialog.OnDeleteConfirmListener() { // from class: com.iptv.online.player.live.tv.smart.ui.main.SettingMainActivity$1$$ExternalSyntheticLambda0
                @Override // com.iptv.online.player.live.tv.smart.ui.dialogs.DeletePlaylistDialog.OnDeleteConfirmListener
                public final void onDeleteConfirmed() {
                    SettingMainActivity.AnonymousClass1.this.lambda$onDeletePlaylist$0(playlistSource);
                }
            }).show();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onDeletePlaylist$0(PlaylistSource playlistSource) {
            SettingMainActivity.this.storageManager.deleteSource(playlistSource.getId());
            SettingMainActivity.this.loadData();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initViews$2(View view) {
//        AnalyticsTracker.logAction("SettingMainActivity", "click_add_playlist", null);
        new AddPlaylistDialog(this, this.storageManager, new AddPlaylistDialog.OnPlaylistAddedListener() { // from class: com.iptv.online.player.live.tv.smart.ui.main.SettingMainActivity$$ExternalSyntheticLambda8
            @Override // com.iptv.online.player.live.tv.smart.ui.dialogs.AddPlaylistDialog.OnPlaylistAddedListener
            public final void onPlaylistAdded(PlaylistSource playlistSource) {
                SettingMainActivity.this.lambda$initViews$1(playlistSource);
            }
        }).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initViews$1(PlaylistSource playlistSource) {
        this.storageManager.setActiveSourceId(playlistSource.getId());
        loadData();
//        showTabScrollInterstitialAds();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initViews$4(View view) {
//        AnalyticsTracker.logAction("SettingMainActivity", "configure_pin", null);
        new PinLockDialog(this, this.storageManager, PinLockDialog.Mode.SET_NEW_PIN, new PinLockDialog.OnPinVerifiedListener() { // from class: com.iptv.online.player.live.tv.smart.ui.main.SettingMainActivity$$ExternalSyntheticLambda0
            @Override // com.iptv.online.player.live.tv.smart.ui.dialogs.PinLockDialog.OnPinVerifiedListener
            public final void onPinVerified() {
                SettingMainActivity.this.lambda$initViews$3();
            }
        }).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initViews$3() {
        updatePinStatus();
//        showTabScrollInterstitialAds();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initViews$6(View view) {
//        AnalyticsTracker.logAction("SettingMainActivity", "manage_locked_categories", null);
        if (!this.storageManager.isParentalControlEnabled()) {
            Toast.makeText(this, R.string.err_set_pin_first_excl, 0).show();
            new PinLockDialog(this, this.storageManager, PinLockDialog.Mode.SET_NEW_PIN, new PinLockDialog.OnPinVerifiedListener() { // from class: com.iptv.online.player.live.tv.smart.ui.main.SettingMainActivity$$ExternalSyntheticLambda14
                @Override // com.iptv.online.player.live.tv.smart.ui.dialogs.PinLockDialog.OnPinVerifiedListener
                public final void onPinVerified() {
                    SettingMainActivity.this.lambda$initViews$5();
                }
            }).show();
            return;
        }
        showCategoryLockDialog();
//        showTabScrollInterstitialAds();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initViews$5() {
        updatePinStatus();
        showCategoryLockDialog();
//        showTabScrollInterstitialAds();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initViews$8(View view) {
//        AnalyticsTracker.logAction("SettingMainActivity", "disable_pin", null);
        new PinLockDialog(this, this.storageManager, PinLockDialog.Mode.UNLOCK, new PinLockDialog.OnPinVerifiedListener() { // from class: com.iptv.online.player.live.tv.smart.ui.main.SettingMainActivity$$ExternalSyntheticLambda7
            @Override // com.iptv.online.player.live.tv.smart.ui.dialogs.PinLockDialog.OnPinVerifiedListener
            public final void onPinVerified() {
                SettingMainActivity.this.lambda$initViews$7();
            }
        }).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initViews$7() {
        this.storageManager.removeParentalPin();
        Toast.makeText(this, R.string.msg_parental_disabled, 0).show();
        updatePinStatus();
//        showTabScrollInterstitialAds();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initViews$9(View view) {
        showThemeModeDialog();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initViews$10(View view) {
        Intent intent = new Intent(this, LanguageActivity.class);
        intent.putExtra("EXTRA_FROM_SETTINGS", true);
//        startAdsActivity(intent);
        startActivity(intent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadData() {
        this.adapter.setSources(this.storageManager.getPlaylistSources(), this.storageManager.getActiveSourceId());
        updatePinStatus();
        updateThemeAndLanguageSubtitles();
    }

    private void updatePinStatus() {
        boolean isParentalControlEnabled = this.storageManager.isParentalControlEnabled();
        TextView textView = this.tvPinStatus;
        if (isParentalControlEnabled) {
            textView.setText(R.string.status_parental_enabled);
            this.btnConfigurePin.setText(R.string.change_parental_pin);
            this.btnDisablePin.setVisibility(View.VISIBLE);
            return;
        }
        textView.setText(R.string.status_parental_disabled);
        this.btnConfigurePin.setText(R.string.set_pin);
        this.btnDisablePin.setVisibility(View.GONE);
    }

    private void updateThemeAndLanguageSubtitles() {
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

    private void showCategoryLockDialog() {
        if (sAvailableCategories.isEmpty()) {
            Toast.makeText(this, R.string.msg_no_categories_playlist, 0).show();
            return;
        }
        ArrayList arrayList = new ArrayList();
        for (Category category : sAvailableCategories) {
            String id = category.getId();
            if (!"favorites".equalsIgnoreCase(id) && !"recent".equalsIgnoreCase(id)) {
                arrayList.add(category.getName());
            }
        }
        if (arrayList.isEmpty()) {
            Toast.makeText(this, R.string.msg_no_categories_available, 0).show();
            return;
        }
        final String[] strArr = (String[]) arrayList.toArray(new String[0]);
        boolean[] zArr = new boolean[strArr.length];
        for (int i = 0; i < strArr.length; i++) {
            zArr[i] = this.storageManager.isCategoryLocked(strArr[i]);
        }
        final AlertDialog create = new AlertDialog.Builder(this, R.style.CustomDialogTheme).setTitle(R.string.dialog_title_select_categories_lock).setMultiChoiceItems(strArr, zArr, new DialogInterface.OnMultiChoiceClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.main.SettingMainActivity$$ExternalSyntheticLambda11
            @Override // android.content.DialogInterface.OnMultiChoiceClickListener
            public final void onClick(DialogInterface dialogInterface, int i2, boolean z) {
                SettingMainActivity.this.lambda$showCategoryLockDialog$11(strArr, dialogInterface, i2, z);
            }
        }).setPositiveButton(R.string.save, new DialogInterface.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.main.SettingMainActivity$$ExternalSyntheticLambda12
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i2) {
                SettingMainActivity.this.lambda$showCategoryLockDialog$12(dialogInterface, i2);
            }
        }).setNegativeButton(R.string.cancel, (DialogInterface.OnClickListener) null).create();
        create.setOnShowListener(new DialogInterface.OnShowListener() { // from class: com.iptv.online.player.live.tv.smart.ui.main.SettingMainActivity$$ExternalSyntheticLambda13
            @Override // android.content.DialogInterface.OnShowListener
            public final void onShow(DialogInterface dialogInterface) {
                SettingMainActivity.this.lambda$showCategoryLockDialog$13(create, dialogInterface);
            }
        });
        create.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showCategoryLockDialog$11(String[] strArr, DialogInterface dialogInterface, int i, boolean z) {
        this.storageManager.setCategoryLocked(strArr[i], z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showCategoryLockDialog$12(DialogInterface dialogInterface, int i) {
//        AnalyticsTracker.logAction("SettingMainActivity", "save_locked_categories", null);
        Toast.makeText(this, R.string.msg_locked_categories_updated, 0).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showCategoryLockDialog$13(AlertDialog alertDialog, DialogInterface dialogInterface) {
        Button button = alertDialog.getButton(-1);
        Button button2 = alertDialog.getButton(-2);
        if (button != null) {
            button.setTextColor(ContextCompat.getColor(this, R.color.brand_blue));
        }
        if (button2 != null) {
            button2.setTextColor(ContextCompat.getColor(this, R.color.text_secondary));
        }
    }

    private void showThemeModeDialog() {
        final AlertDialog create = new AlertDialog.Builder(this, R.style.CustomDialogTheme).setTitle(R.string.select_theme_mode).setSingleChoiceItems(new String[]{getString(R.string.theme_option_light), getString(R.string.theme_option_dark)}, this.storageManager.getThemeMode() == 2 ? 1 : 0, new DialogInterface.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.main.SettingMainActivity$$ExternalSyntheticLambda9
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                SettingMainActivity.this.lambda$showThemeModeDialog$14(dialogInterface, i);
            }
        }).setNegativeButton(R.string.cancel, (DialogInterface.OnClickListener) null).create();
        create.setOnShowListener(new DialogInterface.OnShowListener() { // from class: com.iptv.online.player.live.tv.smart.ui.main.SettingMainActivity$$ExternalSyntheticLambda10
            @Override // android.content.DialogInterface.OnShowListener
            public final void onShow(DialogInterface dialogInterface) {
                SettingMainActivity.this.lambda$showThemeModeDialog$15(create, dialogInterface);
            }
        });
        create.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showThemeModeDialog$14(DialogInterface dialogInterface, int i) {
        dialogInterface.dismiss();
        int i2 = i == 1 ? 2 : 1;
        this.storageManager.setThemeMode(i2);
//        AnalyticsTracker.logSettingsChange("SettingMainActivity", "theme", i2 == 2 ? "dark" : "light");
        updateThemeAndLanguageSubtitles();
        recreate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showThemeModeDialog$15(AlertDialog alertDialog, DialogInterface dialogInterface) {
        Button button = alertDialog.getButton(-2);
        if (button != null) {
            button.setTextColor(ContextCompat.getColor(this, R.color.text_secondary));
        }
    }
}

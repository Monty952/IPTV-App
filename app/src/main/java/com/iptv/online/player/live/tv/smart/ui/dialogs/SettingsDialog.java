package com.iptv.online.player.live.tv.smart.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import com.iptv.online.player.live.tv.smart.R;
import com.iptv.online.player.live.tv.smart.data.local.PlaylistStorageManager;
import com.iptv.online.player.live.tv.smart.data.model.Category;
import com.iptv.online.player.live.tv.smart.data.model.PlaylistSource;
import com.iptv.online.player.live.tv.smart.ui.adapters.PlaylistManageAdapter;
import com.iptv.online.player.live.tv.smart.ui.dialogs.DeletePlaylistDialog;
import com.iptv.online.player.live.tv.smart.ui.dialogs.PinLockDialog;
import com.iptv.online.player.live.tv.smart.ui.dialogs.SettingsDialog;
import java.util.ArrayList;
import java.util.List;
@SuppressWarnings("all")

/* loaded from: classes5.dex */
public class SettingsDialog extends Dialog {
    private PlaylistManageAdapter adapter;
    private MaterialButton btnConfigurePin;
    private MaterialButton btnDisablePin;
    private MaterialButton btnDone;
    private MaterialButton btnManageLockedCategories;
    private final List<Category> categories;
    private final OnSettingsChangeListener listener;
    private RecyclerView rvPlaylists;
    private final PlaylistStorageManager storageManager;
    private TextView tvPinStatus;

    /* loaded from: classes5.dex */
    public interface OnSettingsChangeListener {
        default void onCategoriesLockChanged() {
        }

        void onPlaylistDeleted();

        void onPlaylistSwitched(PlaylistSource playlistSource);
    }

    public SettingsDialog(Context context, PlaylistStorageManager playlistStorageManager, List<Category> list, OnSettingsChangeListener onSettingsChangeListener) {
        super(context, R.style.CustomDialogTheme);
        this.storageManager = playlistStorageManager;
        this.categories = list == null ? new ArrayList<>() : list;
        this.listener = onSettingsChangeListener;
    }

    public SettingsDialog(Context context, PlaylistStorageManager playlistStorageManager, OnSettingsChangeListener onSettingsChangeListener) {
        this(context, playlistStorageManager, new ArrayList(), onSettingsChangeListener);
    }

    @Override // android.app.Dialog
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        setContentView(R.layout.dialog_settings);
        if (getWindow() != null) {
            getWindow().setLayout(-1, -2);
        }
        initViews();
        loadData();
    }

    private void initViews() {
        this.rvPlaylists = (RecyclerView) findViewById(R.id.rvPlaylistsList);
        this.tvPinStatus = (TextView) findViewById(R.id.tvPinStatus);
        this.btnConfigurePin = (MaterialButton) findViewById(R.id.btnConfigurePin);
        this.btnManageLockedCategories = (MaterialButton) findViewById(R.id.btnManageLockedCategories);
        this.btnDisablePin = (MaterialButton) findViewById(R.id.btnDisablePin);
        this.btnDone = (MaterialButton) findViewById(R.id.btnCloseSettings);
        this.rvPlaylists.setLayoutManager(new LinearLayoutManager(getContext()));
        PlaylistManageAdapter playlistManageAdapter = new PlaylistManageAdapter(new AnonymousClass1());
        this.adapter = playlistManageAdapter;
        this.rvPlaylists.setAdapter(playlistManageAdapter);
        this.btnConfigurePin.setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.dialogs.SettingsDialog$$ExternalSyntheticLambda5
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                SettingsDialog.this.lambda$initViews$1(view);
            }
        });
        this.btnManageLockedCategories.setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.dialogs.SettingsDialog$$ExternalSyntheticLambda6
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                SettingsDialog.this.lambda$initViews$3(view);
            }
        });
        this.btnDisablePin.setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.dialogs.SettingsDialog$$ExternalSyntheticLambda7
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                SettingsDialog.this.lambda$initViews$5(view);
            }
        });
        this.btnDone.setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.dialogs.SettingsDialog$$ExternalSyntheticLambda8
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                SettingsDialog.this.lambda$initViews$6(view);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.iptv.online.player.live.tv.smart.ui.dialogs.SettingsDialog$1  reason: invalid class name */
    /* loaded from: classes5.dex */
    public class AnonymousClass1 implements PlaylistManageAdapter.OnPlaylistActionCallback {
        AnonymousClass1() {
        }

        @Override // com.iptv.online.player.live.tv.smart.ui.adapters.PlaylistManageAdapter.OnPlaylistActionCallback
        public void onSelectPlaylist(PlaylistSource playlistSource) {
            SettingsDialog.this.storageManager.setActiveSourceId(playlistSource.getId());
            SettingsDialog.this.adapter.setSources(SettingsDialog.this.storageManager.getPlaylistSources(), playlistSource.getId());
            if (SettingsDialog.this.listener != null) {
                SettingsDialog.this.listener.onPlaylistSwitched(playlistSource);
            }
            SettingsDialog.this.dismiss();
        }

        @Override // com.iptv.online.player.live.tv.smart.ui.adapters.PlaylistManageAdapter.OnPlaylistActionCallback
        public void onDeletePlaylist(final PlaylistSource playlistSource) {
            new DeletePlaylistDialog(SettingsDialog.this.getContext(), playlistSource, new DeletePlaylistDialog.OnDeleteConfirmListener() { // from class: com.iptv.online.player.live.tv.smart.ui.dialogs.SettingsDialog$1$$ExternalSyntheticLambda0
                @Override // com.iptv.online.player.live.tv.smart.ui.dialogs.DeletePlaylistDialog.OnDeleteConfirmListener
                public final void onDeleteConfirmed() {
                    SettingsDialog.AnonymousClass1.this.lambda$onDeletePlaylist$0(playlistSource);
                }
            }).show();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onDeletePlaylist$0(PlaylistSource playlistSource) {
            SettingsDialog.this.storageManager.deleteSource(playlistSource.getId());
            SettingsDialog.this.loadData();
            if (SettingsDialog.this.listener != null) {
                SettingsDialog.this.listener.onPlaylistDeleted();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initViews$1(View view) {
        new PinLockDialog(getContext(), this.storageManager, PinLockDialog.Mode.SET_NEW_PIN, new PinLockDialog.OnPinVerifiedListener() { // from class: com.iptv.online.player.live.tv.smart.ui.dialogs.SettingsDialog$$ExternalSyntheticLambda0
            @Override // com.iptv.online.player.live.tv.smart.ui.dialogs.PinLockDialog.OnPinVerifiedListener
            public final void onPinVerified() {
                SettingsDialog.this.lambda$initViews$0();
            }
        }).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initViews$0() {
        updatePinStatus();
        OnSettingsChangeListener onSettingsChangeListener = this.listener;
        if (onSettingsChangeListener != null) {
            onSettingsChangeListener.onCategoriesLockChanged();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initViews$3(View view) {
        if (!this.storageManager.isParentalControlEnabled()) {
            Toast.makeText(getContext(), R.string.err_set_pin_first_excl, 0).show();
            new PinLockDialog(getContext(), this.storageManager, PinLockDialog.Mode.SET_NEW_PIN, new PinLockDialog.OnPinVerifiedListener() { // from class: com.iptv.online.player.live.tv.smart.ui.dialogs.SettingsDialog$$ExternalSyntheticLambda4
                @Override // com.iptv.online.player.live.tv.smart.ui.dialogs.PinLockDialog.OnPinVerifiedListener
                public final void onPinVerified() {
                    SettingsDialog.this.lambda$initViews$2();
                }
            }).show();
            return;
        }
        showCategoryLockDialog();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initViews$2() {
        updatePinStatus();
        showCategoryLockDialog();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initViews$5(View view) {
        new PinLockDialog(getContext(), this.storageManager, PinLockDialog.Mode.UNLOCK, new PinLockDialog.OnPinVerifiedListener() { // from class: com.iptv.online.player.live.tv.smart.ui.dialogs.SettingsDialog$$ExternalSyntheticLambda9
            @Override // com.iptv.online.player.live.tv.smart.ui.dialogs.PinLockDialog.OnPinVerifiedListener
            public final void onPinVerified() {
                SettingsDialog.this.lambda$initViews$4();
            }
        }).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initViews$4() {
        this.storageManager.removeParentalPin();
        Toast.makeText(getContext(), R.string.msg_parental_disabled, 0).show();
        updatePinStatus();
        OnSettingsChangeListener onSettingsChangeListener = this.listener;
        if (onSettingsChangeListener != null) {
            onSettingsChangeListener.onCategoriesLockChanged();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initViews$6(View view) {
        dismiss();
    }

    private void showCategoryLockDialog() {
        if (this.categories.isEmpty()) {
            Toast.makeText(getContext(), R.string.msg_no_categories_to_lock, 0).show();
            return;
        }
        ArrayList arrayList = new ArrayList();
        for (Category category : this.categories) {
            String id = category.getId();
            if (!"favorites".equalsIgnoreCase(id) && !"recent".equalsIgnoreCase(id)) {
                arrayList.add(category.getName());
            }
        }
        if (arrayList.isEmpty()) {
            Toast.makeText(getContext(), R.string.msg_no_categories_available, 0).show();
            return;
        }
        final String[] strArr = (String[]) arrayList.toArray(new String[0]);
        boolean[] zArr = new boolean[strArr.length];
        for (int i = 0; i < strArr.length; i++) {
            zArr[i] = this.storageManager.isCategoryLocked(strArr[i]);
        }
        final AlertDialog create = new AlertDialog.Builder(getContext(), R.style.CustomDialogTheme).setTitle(R.string.dialog_title_select_categories_lock).setMultiChoiceItems(strArr, zArr, new DialogInterface.OnMultiChoiceClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.dialogs.SettingsDialog$$ExternalSyntheticLambda1
            @Override // android.content.DialogInterface.OnMultiChoiceClickListener
            public final void onClick(DialogInterface dialogInterface, int i2, boolean z) {
                SettingsDialog.this.lambda$showCategoryLockDialog$7(strArr, dialogInterface, i2, z);
            }
        }).setPositiveButton(R.string.save, new DialogInterface.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.dialogs.SettingsDialog$$ExternalSyntheticLambda2
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i2) {
                SettingsDialog.this.lambda$showCategoryLockDialog$8(dialogInterface, i2);
            }
        }).setNegativeButton(R.string.cancel, (DialogInterface.OnClickListener) null).create();
        create.setOnShowListener(new DialogInterface.OnShowListener() { // from class: com.iptv.online.player.live.tv.smart.ui.dialogs.SettingsDialog$$ExternalSyntheticLambda3
            @Override // android.content.DialogInterface.OnShowListener
            public final void onShow(DialogInterface dialogInterface) {
                SettingsDialog.this.lambda$showCategoryLockDialog$9(create, dialogInterface);
            }
        });
        create.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showCategoryLockDialog$7(String[] strArr, DialogInterface dialogInterface, int i, boolean z) {
        this.storageManager.setCategoryLocked(strArr[i], z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showCategoryLockDialog$8(DialogInterface dialogInterface, int i) {
        Toast.makeText(getContext(), R.string.msg_locked_categories_updated, 0).show();
        OnSettingsChangeListener onSettingsChangeListener = this.listener;
        if (onSettingsChangeListener != null) {
            onSettingsChangeListener.onCategoriesLockChanged();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showCategoryLockDialog$9(AlertDialog alertDialog, DialogInterface dialogInterface) {
        Button button = alertDialog.getButton(-1);
        Button button2 = alertDialog.getButton(-2);
        if (button != null) {
            button.setTextColor(ContextCompat.getColor(getContext(), R.color.brand_blue));
        }
        if (button2 != null) {
            button2.setTextColor(ContextCompat.getColor(getContext(), R.color.text_secondary));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadData() {
        this.adapter.setSources(this.storageManager.getPlaylistSources(), this.storageManager.getActiveSourceId());
        updatePinStatus();
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
}

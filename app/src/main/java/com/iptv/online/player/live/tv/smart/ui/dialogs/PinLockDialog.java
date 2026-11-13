package com.iptv.online.player.live.tv.smart.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.button.MaterialButton;
import com.iptv.online.player.live.tv.smart.R;
import com.iptv.online.player.live.tv.smart.data.local.PlaylistStorageManager;
@SuppressWarnings("all")

/* loaded from: classes5.dex */
public class PinLockDialog extends Dialog {
    private MaterialButton btnCancel;
    private MaterialButton btnConfirm;
    private EditText etPin;
    private final OnPinVerifiedListener listener;
    private final Mode mode;
    private final PlaylistStorageManager storageManager;
    private TextView tvSubtitle;
    private TextView tvTitle;

    /* loaded from: classes5.dex */
    public enum Mode {
        UNLOCK,
        SET_NEW_PIN
    }

    /* loaded from: classes5.dex */
    public interface OnPinVerifiedListener {
        void onPinVerified();
    }

    public PinLockDialog(Context context, PlaylistStorageManager playlistStorageManager, Mode mode, OnPinVerifiedListener onPinVerifiedListener) {
        super(context, R.style.CustomDialogTheme);
        this.storageManager = playlistStorageManager;
        this.mode = mode;
        this.listener = onPinVerifiedListener;
    }

    @Override // android.app.Dialog
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        setContentView(R.layout.dialog_pin_lock);
        if (getWindow() != null) {
            getWindow().setLayout(-1, -2);
        }
        this.tvTitle = (TextView) findViewById(R.id.tvPinTitle);
        this.tvSubtitle = (TextView) findViewById(R.id.tvPinSubtitle);
        this.etPin = (EditText) findViewById(R.id.etPinCode);
        this.btnCancel = (MaterialButton) findViewById(R.id.btnCancelPin);
        this.btnConfirm = (MaterialButton) findViewById(R.id.btnConfirmPin);
        Mode mode = this.mode;
        Mode mode2 = Mode.SET_NEW_PIN;
        TextView textView = this.tvTitle;
        if (mode == mode2) {
            textView.setText(R.string.set_pin);
            this.tvSubtitle.setText(R.string.create_parental_pin);
            this.btnConfirm.setText(R.string.save_pin);
        } else {
            textView.setText(R.string.enter_pin);
            this.tvSubtitle.setText(R.string.parental_lock_category_msg);
            this.btnConfirm.setText(R.string.btn_unlock);
        }
        this.btnCancel.setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.dialogs.PinLockDialog$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                PinLockDialog.this.lambda$onCreate$0(view);
            }
        });
        this.btnConfirm.setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.dialogs.PinLockDialog$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                PinLockDialog.this.lambda$onCreate$1(view);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$0(View view) {
        dismiss();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$1(View view) {
        String trim = this.etPin.getText().toString().trim();
        if (trim.length() < 4) {
            Toast.makeText(getContext(), R.string.err_enter_4digit_pin, 0).show();
            return;
        }
        Mode mode = this.mode;
        Mode mode2 = Mode.SET_NEW_PIN;
        PlaylistStorageManager playlistStorageManager = this.storageManager;
        if (mode == mode2) {
            playlistStorageManager.setParentalPin(trim);
            Toast.makeText(getContext(), R.string.pin_set_success, 0).show();
            OnPinVerifiedListener onPinVerifiedListener = this.listener;
            if (onPinVerifiedListener != null) {
                onPinVerifiedListener.onPinVerified();
            }
            dismiss();
            return;
        }
        String parentalPin = playlistStorageManager.getParentalPin();
        if (parentalPin == null || parentalPin.equals(trim)) {
            OnPinVerifiedListener onPinVerifiedListener2 = this.listener;
            if (onPinVerifiedListener2 != null) {
                onPinVerifiedListener2.onPinVerified();
            }
            dismiss();
            return;
        }
        Toast.makeText(getContext(), R.string.incorrect_pin, 0).show();
        this.etPin.setText("");
    }
}

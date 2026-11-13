package com.iptv.online.player.live.tv.smart.AdsPackage.dialog;

/*import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
//import com.adUtils.R;
import com.adsUtils.dialog.CustomDialog;
import com.iptv.online.player.live.tv.smart.R;
import java.util.Objects;*/

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
//import com.adUtils.R;
//import com.adsUtils.dialog.CustomDialog;
import com.iptv.online.player.live.tv.smart.R;

import java.util.Objects;



@SuppressWarnings("all")

/* loaded from: classes3.dex */
public class CustomDialog {
    Activity activity;
    AppCompatButton btnOkay;
    Dialog dialog;
    AppCompatTextView tvMessage;
    AppCompatTextView tvTitle;
    String dialogTitle = "My APP";
    String dialogMessage = "Hello! Is this your app?";
    String dialogButtonText = "Okay";

    /* loaded from: classes3.dex */
    public interface OnDialogButtonClickListener {
        void onClick(View view);
    }

    public CustomDialog(Activity activity, final OnDialogButtonClickListener onDialogButtonClickListener) {
        this.activity = activity;
        int dimensionPixelSize = activity.getResources().getDimensionPixelSize(R.dimen.dimen_250sdp);
        Dialog dialog = new Dialog(activity, R.style.DialogCustomTheme);
        this.dialog = dialog;
        dialog.requestWindowFeature(1);
        this.dialog.setContentView(R.layout.dialog_custom);
        ((Window) Objects.requireNonNull(this.dialog.getWindow())).setLayout(dimensionPixelSize, -2);
        this.dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        ((Window) Objects.requireNonNull(this.dialog.getWindow())).setGravity(17);
        this.dialog.setCanceledOnTouchOutside(false);
        this.dialog.setCancelable(false);
        this.tvTitle = (AppCompatTextView) this.dialog.findViewById(R.id.tvTitle);
        this.tvMessage = (AppCompatTextView) this.dialog.findViewById(R.id.tvMessage);
        AppCompatButton appCompatButton = (AppCompatButton) this.dialog.findViewById(R.id.btnOkay);
        this.btnOkay = appCompatButton;
        // New Code
        appCompatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Call the listener passed into the constructor.
                CustomDialog.lambda$new$0(onDialogButtonClickListener, view);
            }
        });

        // Old Code
       /* appCompatButton.setOnClickListener(new View.OnClickListener() { // from class: com.adsUtils.dialog.CustomDialog$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                CustomDialog.lambda$new$0(CustomDialog.OnDialogButtonClickListener.this, view);
//                CustomDialog.lambda$new$0(OnDialogButtonClickListener.this, view);
            }
        });*/
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$new$0(OnDialogButtonClickListener onDialogButtonClickListener, View view) {
        if (onDialogButtonClickListener != null) {
            onDialogButtonClickListener.onClick(view);
        }
    }

    public void show() {
        this.tvTitle.setText(this.dialogTitle);
        this.tvMessage.setText(this.dialogMessage);
        this.btnOkay.setText(this.dialogButtonText);
        Activity activity = this.activity;
        if (activity == null || activity.isFinishing() || this.activity.isDestroyed()) {
            return;
        }
        this.dialog.show();
    }

    public void dismiss() {
        if (this.dialog.isShowing()) {
            this.dialog.dismiss();
        }
    }

    public void setDialogTitle(String str) {
        this.dialogTitle = str;
    }

    public void setDialogMessage(String str) {
        this.dialogMessage = str;
    }

    public void setDialogButtonText(String str) {
        this.dialogButtonText = str;
    }
}

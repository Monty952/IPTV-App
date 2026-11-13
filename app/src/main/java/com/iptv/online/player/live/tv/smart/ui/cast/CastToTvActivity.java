package com.iptv.online.player.live.tv.smart.ui.cast;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.TransportInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
//import com.adsUtils.adsloader.nativeads.NativeLoader;
//import com.adsUtils.adsloader.nativeads.NativeType;
//import com.adsUtils.custom.CustomActivity;
import com.iptv.online.player.live.tv.smart.AdsPackage.CustomActivity;
import com.iptv.online.player.live.tv.smart.R;
import com.iptv.online.player.live.tv.smart.ui.help.HowToUseActivity;
//import com.onesignal.location.internal.common.LocationConstants;

@SuppressWarnings("all")

/* loaded from: classes5.dex */
public class CastToTvActivity extends com.iptv.online.player.live.tv.smart.AdsPackage.CustomActivity {
    private final ActivityResultLauncher<String> requestLocationPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback() { // from class: com.iptv.online.player.live.tv.smart.ui.cast.CastToTvActivity$$ExternalSyntheticLambda0
        @Override // androidx.activity.result.ActivityResultCallback
        public final void onActivityResult(Object obj) {
            CastToTvActivity.this.lambda$new$0((Boolean) obj);
        }
    });
    private TextView tvWifiSsid;
    private TextView tvWifiStatus;
    private BroadcastReceiver wifiReceiver;

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(Boolean bool) {
        updateWifiInfo();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.adsUtils.custom.CustomActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_cast_to_tv);
//        NativeLoader.showNativeAds(this, (LinearLayout) findViewById(R.id.llAdsView), NativeType.SMALL_NATIVE);
        View findViewById = findViewById(R.id.topBar);
        ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);
        if (findViewById != null) {
            ViewCompat.setOnApplyWindowInsetsListener(findViewById, new OnApplyWindowInsetsListener() { // from class: com.iptv.online.player.live.tv.smart.ui.cast.CastToTvActivity$$ExternalSyntheticLambda1
                @Override // androidx.core.view.OnApplyWindowInsetsListener
                public final WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat) {
                     view.setPadding(view.getPaddingLeft(), windowInsetsCompat.getInsets(WindowInsetsCompat.Type.systemBars()).top + 12, view.getPaddingRight(), view.getPaddingBottom());
                    return windowInsetsCompat;
                }
            });
        }
        if (scrollView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(scrollView, new OnApplyWindowInsetsListener() { // from class: com.iptv.online.player.live.tv.smart.ui.cast.CastToTvActivity$$ExternalSyntheticLambda2
                @Override // androidx.core.view.OnApplyWindowInsetsListener
                public final WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat) {
                     view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), windowInsetsCompat.getInsets(WindowInsetsCompat.Type.systemBars()).bottom + 20);
                    return windowInsetsCompat;
                }
            });
        }
        this.tvWifiSsid = (TextView) findViewById(R.id.tvWifiSsid);
        this.tvWifiStatus = (TextView) findViewById(R.id.tvWifiStatus);
        findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.cast.CastToTvActivity$$ExternalSyntheticLambda3
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                CastToTvActivity.this.lambda$onCreate$3(view);
            }
        });
        findViewById(R.id.btnInfo).setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.cast.CastToTvActivity$$ExternalSyntheticLambda4
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                CastToTvActivity.this.lambda$onCreate$4(view);
            }
        });
        findViewById(R.id.btnConnect).setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.cast.CastToTvActivity$$ExternalSyntheticLambda5
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                CastToTvActivity.this.lambda$onCreate$5(view);
            }
        });
        findViewById(R.id.cardWifiNetwork).setOnClickListener(new View.OnClickListener() { // from class: com.iptv.online.player.live.tv.smart.ui.cast.CastToTvActivity$$ExternalSyntheticLambda6
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                CastToTvActivity.this.lambda$onCreate$6(view);
            }
        });
        checkAndRequestLocationPermission();
        updateWifiInfo();
        this.wifiReceiver = new BroadcastReceiver() { // from class: com.iptv.online.player.live.tv.smart.ui.cast.CastToTvActivity.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                CastToTvActivity.this.updateWifiInfo();
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$3(View view) {
        setOnBackPressedClick();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$4(View view) {
        Intent intent = new Intent(this, HowToUseActivity.class);
        intent.putExtra(HowToUseActivity.EXTRA_TAB, "CAST");
        startActivity(intent);
        overridePendingTransition(17432576, 17432577);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$5(View view) {
        openWirelessDisplaySettings();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$6(View view) {
        openWifiSettings();
    }

    private void checkAndRequestLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED) {

            this.requestLocationPermissionLauncher.launch(
                    Manifest.permission.ACCESS_FINE_LOCATION
            );
        }
    }
/*    private void checkAndRequestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, LocationConstants.ANDROID_FINE_LOCATION_PERMISSION_STRING) != 0) {
            this.requestLocationPermissionLauncher.launch(LocationConstants.ANDROID_FINE_LOCATION_PERMISSION_STRING);
        }
    }*/

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.adsUtils.custom.CustomActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        updateWifiInfo();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.wifi.STATE_CHANGE");
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        intentFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        registerReceiver(this.wifiReceiver, intentFilter);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.adsUtils.custom.CustomActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onPause() {
        super.onPause();
        try {
            unregisterReceiver(this.wifiReceiver);
        } catch (Exception unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateWifiInfo() {
        boolean z;
        String extraInfo;
        WifiInfo connectionInfo;
        Network activeNetwork;
        NetworkCapabilities networkCapabilities;
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService("connectivity");
            if (connectivityManager == null) {
                setWifiDisconnectedUI();
                return;
            }
            boolean z2 = true;
            String r4 = "";
            if (Build.VERSION.SDK_INT < 29 || (activeNetwork = connectivityManager.getActiveNetwork()) == null || (networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)) == null || !networkCapabilities.hasTransport(1)) {
                z = false;
            } else {
                TransportInfo transportInfo = networkCapabilities.getTransportInfo();
                r4 = transportInfo instanceof WifiInfo ? sanitizeSsid(((WifiInfo) transportInfo).getSSID()) : null;
                z = true;
            }
            WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService("wifi");
            if (wifiManager != null && wifiManager.isWifiEnabled() && (connectionInfo = wifiManager.getConnectionInfo()) != null && connectionInfo.getNetworkId() != -1) {
                if (r4 == null || r4.isEmpty() || r4.equalsIgnoreCase("<unknown ssid>")) {
                    r4 = sanitizeSsid(connectionInfo.getSSID());
                }
                z = true;
            }
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo == null || !activeNetworkInfo.isConnected() || activeNetworkInfo.getType() != 1) {
                z2 = z;
            } else if ((r4 == null || r4.isEmpty() || r4.equalsIgnoreCase("<unknown ssid>")) && (extraInfo = activeNetworkInfo.getExtraInfo()) != null && !extraInfo.isEmpty()) {
                r4 = sanitizeSsid(extraInfo);
            }
            if (z2) {
                if (r4 != null && !r4.isEmpty() && !r4.equalsIgnoreCase("<unknown ssid>")) {
                    this.tvWifiSsid.setText(r4);
                } else {
                    this.tvWifiSsid.setText(R.string.cast_wifi_connected_fallback);
                }
                this.tvWifiStatus.setText(R.string.status_connected);
                this.tvWifiStatus.setTextColor(Color.parseColor("#10B981"));
                return;
            }
            setWifiDisconnectedUI();
        } catch (Exception unused) {
            setWifiDisconnectedUI();
        }
    }

    private void setWifiDisconnectedUI() {
        TextView textView = this.tvWifiSsid;
        if (textView != null) {
            textView.setText(R.string.cast_wifi_not_connected);
        }
        TextView textView2 = this.tvWifiStatus;
        if (textView2 != null) {
            textView2.setText(R.string.cast_wifi_tap_to_connect);
            this.tvWifiStatus.setTextColor(Color.parseColor("#EF4444"));
        }
    }

    private String sanitizeSsid(String str) {
        if (str == null) {
            return null;
        }
        String trim = str.trim();
        if (trim.startsWith("\"") && trim.endsWith("\"") && trim.length() >= 2) {
            trim = trim.substring(1, trim.length() - 1);
        }
        if (trim.equalsIgnoreCase("<unknown ssid>") || trim.equalsIgnoreCase("0x") || trim.isEmpty()) {
            return null;
        }
        return trim;
    }

    private void openWifiSettings() {
        try {
            try {
                Intent intent = new Intent("android.settings.WIFI_SETTINGS");
                intent.addFlags(268435456);
                startActivity(intent);
//                startAdsActivity(intent);
            } catch (Exception unused) {
                Toast.makeText(this, R.string.msg_unable_open_wifi, 0).show();
            }
        } catch (Exception unused2) {
            new Intent("android.settings.SETTINGS");
            startActivity(new Intent("android.settings.SETTINGS"));
//            startAdsActivity(new Intent("android.settings.SETTINGS"));
        }
    }

    private void openWirelessDisplaySettings() {
        Intent[] intentArr = {new Intent("android.settings.CAST_SETTINGS"), new Intent("com.samsung.wfd.LAUNCH_WFD_PICKER_DLG"), new Intent("android.settings.WIFI_DISPLAY_SETTINGS"), new Intent("android.settings.CAST_SETTINGS"), new Intent("android.settings.DISPLAY_SETTINGS")};
        for (int i = 0; i < 5; i++) {
            Intent intent = intentArr[i];
            try {
                intent.addFlags(268435456);
                startActivity(intent);
//                startAdsActivity(intent);
                return;
            } catch (Exception unused) {
            }
        }
        Toast.makeText(this, R.string.msg_opening_display_settings, 0).show();
    }
}

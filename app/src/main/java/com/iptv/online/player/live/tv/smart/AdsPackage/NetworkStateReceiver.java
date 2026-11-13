package com.iptv.online.player.live.tv.smart.AdsPackage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import java.util.ArrayList;
import java.util.List;
@SuppressWarnings("all")

/* loaded from: classes3.dex */
public class NetworkStateReceiver extends BroadcastReceiver {
    protected List<NetworkStateReceiverListener> listeners = new ArrayList();
    protected Boolean connected = null;

    /* loaded from: classes3.dex */
    public interface NetworkStateReceiverListener {
        void isNetworkOff();

        void isNetworkOn();
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        if (intent == null || intent.getExtras() == null) {
            return;
        }
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.getState() == NetworkInfo.State.CONNECTED) {
            this.connected = true;
        } else if (intent.getBooleanExtra("noConnectivity", Boolean.FALSE.booleanValue())) {
            this.connected = false;
        }
        notifyStateToAll();
    }

    private void notifyStateToAll() {
        for (NetworkStateReceiverListener networkStateReceiverListener : this.listeners) {
            notifyState(networkStateReceiverListener);
        }
    }

    private void notifyState(NetworkStateReceiverListener networkStateReceiverListener) {
        Boolean bool = this.connected;
        if (bool == null || networkStateReceiverListener == null) {
            return;
        }
        if (bool.booleanValue()) {
            networkStateReceiverListener.isNetworkOn();
        } else {
            networkStateReceiverListener.isNetworkOff();
        }
    }

    public void addListener(NetworkStateReceiverListener networkStateReceiverListener) {
        this.listeners.add(networkStateReceiverListener);
        notifyState(networkStateReceiverListener);
    }

    public void removeListener(NetworkStateReceiverListener networkStateReceiverListener) {
        this.listeners.remove(networkStateReceiverListener);
    }
}

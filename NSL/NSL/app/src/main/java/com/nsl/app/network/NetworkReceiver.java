package com.nsl.app.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by admin on 4/19/2017.
 */

public class NetworkReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (null != intent) {
            NetworkInfo.State wifiState = null;
            NetworkInfo.State mobileState = null;
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            wifiState = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
            mobileState = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
            if (wifiState != null && mobileState != null
                    && NetworkInfo.State.CONNECTED != wifiState
                    && NetworkInfo.State.CONNECTED == mobileState) {
                // phone network connect success
                gNetwork();
            } else if (wifiState != null && mobileState != null
                    && NetworkInfo.State.CONNECTED != wifiState
                    && NetworkInfo.State.CONNECTED != mobileState) {
                // no network
                noNetwork();
            } else if (wifiState != null && NetworkInfo.State.CONNECTED == wifiState) {
                // wift connect success
                WIFINetwork();
            }
        }
    }

    private void WIFINetwork() {
        Log.d("WIFINetwork","WIFINetwork ");
    }

    private void noNetwork() {
        Log.d("WIFINetwork","noNetwork ");

    }

    private void gNetwork() {
        Log.d("WIFINetwork","mobileNetwork ");

    }
}

package com.tommyku.umbrellamovementsms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

public class NetworkConnectionReceiver extends BroadcastReceiver {
    public NetworkConnectionReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // http://stackoverflow.com/questions/15698790/broadcast-receiver-for-checking-internet-connection-in-android-app

        final ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        final android.net.NetworkInfo wifi = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        final android.net.NetworkInfo mobile = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (wifi.isAvailable() || mobile.isAvailable()) {
            // grab a new list of SMS numbers from the google calendar
        }
    }
}

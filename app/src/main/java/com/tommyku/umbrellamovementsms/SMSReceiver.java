package com.tommyku.umbrellamovementsms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class SMSReceiver extends BroadcastReceiver {
    public SMSReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "已成功收到", Toast.LENGTH_SHORT).show();
    }
}

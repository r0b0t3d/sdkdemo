package com.northroom.bhs.nodescanner.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.northroom.bhs.nodescanner.manager.WifiManager;
import com.northroom.bhs.nodescanner.services.WbService;

public class BootBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, WbService.class);
        context.startService(i);

        WifiManager.getInstance(context).enableWifi();
    }
}

package com.wosmart.sdkdemo.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.wosmart.sdkdemo.services.WbService;

public class BootBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, WbService.class);
        context.startService(i);
    }
}

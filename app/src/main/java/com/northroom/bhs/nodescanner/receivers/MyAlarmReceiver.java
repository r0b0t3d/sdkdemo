package com.northroom.bhs.nodescanner.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.northroom.bhs.nodescanner.services.WbService;

public class MyAlarmReceiver extends BroadcastReceiver  {
    public static final int REQUEST_CODE = 12345;
    public static final String ACTION = "com.northroom.bhs.nodescanner.alarm";

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, WbService.class);
        context.startService(i);
    }
}

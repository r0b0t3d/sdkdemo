package com.northroom.bhs.nodescanner.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.northroom.bhs.nodescanner.manager.WbManager;

public class WbService extends Service {
    private static final String TAG = "WbService";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "Service started");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        WbManager.getInstance(this).start();
        return START_STICKY;
    }
}

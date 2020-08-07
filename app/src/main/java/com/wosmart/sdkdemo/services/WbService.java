package com.wosmart.sdkdemo.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;

import com.wosmart.sdkdemo.manager.WbManager;

public class WbService extends IntentService {
    public WbService() {
        super("WbService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.e("WbService", "Service running");
        WbManager.getInstance(getApplicationContext()).start();
    }
}

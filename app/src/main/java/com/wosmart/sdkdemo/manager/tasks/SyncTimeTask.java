package com.wosmart.sdkdemo.manager.tasks;

import android.util.Log;

import com.wosmart.ukprotocollibary.WristbandManager;
import com.wosmart.ukprotocollibary.WristbandManagerCallback;

public class SyncTimeTask extends CommonTask {
    private static final String TAG = "SyncTimeTask";

    public SyncTimeTask(WristbandManager wristbandManager, Callback callback) {
        super(wristbandManager, callback);
    }

    @Override
    WristbandManagerCallback initWristbandManagerCallback() {
        return new WristbandManagerCallback() {};
    }

    @Override
    public void run() {
        super.run();
        if (wristbandManager.setTimeSync()) {
            Log.e(TAG, "syncTime SUCCESS");
        } else {
            Log.e(TAG, "syncTime FAILED");
        }

        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onSuccess();
    }
}

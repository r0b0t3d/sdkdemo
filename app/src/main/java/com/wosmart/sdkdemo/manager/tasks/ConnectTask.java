package com.wosmart.sdkdemo.manager.tasks;

import android.util.Log;

import com.wosmart.ukprotocollibary.WristbandManager;
import com.wosmart.ukprotocollibary.WristbandManagerCallback;

public class ConnectTask extends CommonTask {
    private static final String TAG = "ConnectTask";
    private String mac;

    public ConnectTask(WristbandManager wristbandManager, Callback c, String mac) {
        super(wristbandManager, c);
        this.mac = mac;

        wristbandManager.registerCallback(new WristbandManagerCallback() {
            @Override
            public void onConnectionStateChange(boolean status) {
                super.onConnectionStateChange(status);
                Log.e(TAG, "onConnectionStateChange " + status);
                if (status) {
                    callback.onSuccess();
                } else {
                    callback.onFailed();
                }
            }

            @Override
            public void onError(int error) {
                super.onError(error);
                callback.onFailed();
            }
        });
    }

    @Override
    public void run() {
        super.run();
        wristbandManager.connect(this.mac);
    }
}

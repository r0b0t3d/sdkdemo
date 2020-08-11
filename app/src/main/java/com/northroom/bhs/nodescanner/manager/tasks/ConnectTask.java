package com.northroom.bhs.nodescanner.manager.tasks;

import android.util.Log;

import com.wosmart.ukprotocollibary.WristbandManager;
import com.wosmart.ukprotocollibary.WristbandManagerCallback;

import java.util.concurrent.atomic.AtomicBoolean;

public class ConnectTask extends CommonTask {
    private static final String TAG = "ConnectTask";
    private String mac;
    private AtomicBoolean isConnected;

    public ConnectTask(WristbandManager manager, Callback c, String mac) {
        super(manager, c);
        this.mac = mac;
        isConnected = new AtomicBoolean(false);
    }

    @Override
    WristbandManagerCallback initWristbandManagerCallback() {
        return new WristbandManagerCallback() {
            @Override
            public void onConnectionStateChange(boolean status) {
                super.onConnectionStateChange(status);
                Log.e(TAG, "onConnectionStateChange " + status);
                if (status) {
                    isConnected.set(true);
                    onSuccess();
                } else {
                    onFailed();
                }
            }

            @Override
            public void onError(int error) {
                super.onError(error);
                onFailed();
            }
        };
    }

    @Override
    public void run() {
        super.run();
        wristbandManager.connect(this.mac);
        try {
            // Wait for 10 seconds to connect
            sleep(10000);

            if (!isConnected.get()) {
                onFailed();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

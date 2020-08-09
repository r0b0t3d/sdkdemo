package com.northroom.bhs.nodescanner.manager.tasks;

import android.util.Log;

import com.wosmart.ukprotocollibary.WristbandManager;
import com.wosmart.ukprotocollibary.WristbandManagerCallback;
import com.wosmart.ukprotocollibary.applicationlayer.ApplicationLayerDeviceInfoPacket;
import com.wosmart.ukprotocollibary.applicationlayer.ApplicationLayerFunctionPacket;

public class DeviceInfoTask extends CommonTask {
    private static final String TAG = "DeviceInfoTask";

    public DeviceInfoTask(WristbandManager wristbandManager, Callback callback) {
        super(wristbandManager, callback);
    }

    @Override
    WristbandManagerCallback initWristbandManagerCallback() {
        return new WristbandManagerCallback() {

            @Override
            public void onDeviceInfo(ApplicationLayerDeviceInfoPacket packet) {
                super.onDeviceInfo(packet);
                Log.e(TAG, "device info = " + packet.toString());
            }

            @Override
            public void onDeviceFunction(ApplicationLayerFunctionPacket packet) {
                super.onDeviceFunction(packet);
                Log.e(TAG, "function info = " + packet.toString());
            }
        };
    }

    @Override
    public void run() {
        super.run();
        if (wristbandManager.requestDeviceInfo()) {
            Log.e(TAG, "readVersion SUCCESS");
        } else {
            Log.e(TAG, "readVersion FAIL");
        }

        if (wristbandManager.sendFunctionReq()) {
            Log.e(TAG, "readFunction SUCCESS");
        } else {
            Log.e(TAG, "readFunction FAIL");
        }

        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onSuccess();
    }
}

package com.northroom.bhs.nodescanner.manager.tasks;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanRecord;
import android.util.Log;

import com.wosmart.ukprotocollibary.WristbandManager;
import com.wosmart.ukprotocollibary.WristbandManagerCallback;
import com.wosmart.ukprotocollibary.WristbandScanCallback;

import java.util.HashMap;
import java.util.Map;

public class ScanTask extends CommonTask {
    private static final String TAG = "ScanTask";
    private ScanTaskListener listener;
    private Map<String, Boolean> devicesMap;
    private final Object lock = new Object();

    public ScanTask(WristbandManager wristbandManager, ScanTaskListener listener) {
        super(wristbandManager, new Callback() {
            @Override
            public void onSuccess(Object... args) {

            }

            @Override
            public void onFailed() {

            }
        });
        this.listener = listener;
        devicesMap = new HashMap<>();
    }

    @Override
    public void run() {
        super.run();
        wristbandManager.startScan(new WristbandScanCallback() {
            @Override
            public void onWristbandDeviceFind(BluetoothDevice device, int rssi, byte[] scanRecord) {
                super.onWristbandDeviceFind(device, rssi, scanRecord);
                Log.e(TAG, "Device found " + device.getAddress());
                handleDeviceFound(device);
            }

            @Override
            public void onWristbandDeviceFind(BluetoothDevice device, int rssi, ScanRecord scanRecord) {
                super.onWristbandDeviceFind(device, rssi, scanRecord);
            }

            @Override
            public void onLeScanEnable(boolean enable) {
                super.onLeScanEnable(enable);
                if (!enable) {
                }
            }

            @Override
            public void onWristbandLoginStateChange(boolean connected) {
                super.onWristbandLoginStateChange(connected);
            }
        });

        try {
            sleep(30000);
            listener.onStopScan();
            onSuccess();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void handleDeviceFound(BluetoothDevice device) {
        synchronized (lock) {
            if (devicesMap.get(device.getAddress()) == null) {
                devicesMap.put(device.getAddress(), true);
                listener.onDeviceFound(device);
            }
        }
    }

    @Override
    WristbandManagerCallback initWristbandManagerCallback() {
        return new WristbandManagerCallback() {};
    }

    public interface ScanTaskListener {
        void onDeviceFound(BluetoothDevice device);
        void onStopScan();
    }
}

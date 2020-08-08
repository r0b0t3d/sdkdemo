package com.wosmart.sdkdemo.manager;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanRecord;
import android.content.Context;
import android.util.Log;

import com.wosmart.ukprotocollibary.WristbandManager;
import com.wosmart.ukprotocollibary.WristbandScanCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class WbManager {
    public static final String TAG = "WbManager";
    private Context context;
    private DeviceManager deviceManager;
    private AtomicBoolean isScanning = new AtomicBoolean(false);
    private Map<String, BluetoothDevice> devicesMap;

    private static WbManager instance;

    public static WbManager getInstance(Context context) {
        if (instance == null) {
            instance = new WbManager(context);
        }
        return instance;
    }

    private WbManager(Context context) {
        this.context = context.getApplicationContext();
        this.deviceManager = new DeviceManager(context);
    }

    public void start() {
        startScan();
    }

    // Scan process
    private void startScan() {
        if (isScanning.get()) {
            return;
        }
        isScanning.set(true);
        devicesMap = new HashMap<>();
        Log.e(TAG, "start scan");
        WristbandManager.getInstance(context).startScan(new WristbandScanCallback() {
            @Override
            public void onWristbandDeviceFind(BluetoothDevice device, int rssi, byte[] scanRecord) {
                super.onWristbandDeviceFind(device, rssi, scanRecord);
                Log.e(TAG, "Device found " + device.getAddress());
                if (!deviceManager.isProcessing()) {
                    processDevice(device.getAddress());
                } else {
                    devicesMap.put(device.getAddress(), device);
                }
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
    }

    private void stopScan() {
        isScanning.set(false);
        WristbandManager.getInstance(context).stopScan();
    }

    private void processDevice(String mac) {
        deviceManager.process(mac, new DeviceManager.DeviceManagerListener() {
            @Override
            public void onFinish() {
                if (isScanning.get()) {
                    stopScan();
                }
                List<BluetoothDevice> devices = new ArrayList<>(devicesMap.values());
                if (devices.size() > 0) {
                    BluetoothDevice device = devices.remove(0);
                    devicesMap.remove(device.getAddress());
                    processDevice(device.getAddress());
                }
            }
        });
    }
}

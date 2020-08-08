package com.wosmart.sdkdemo.manager;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanRecord;
import android.content.Context;
import android.provider.Settings;
import android.util.Log;

import com.wosmart.sdkdemo.manager.tasks.CommonTask;
import com.wosmart.sdkdemo.manager.tasks.ConnectTask;
import com.wosmart.sdkdemo.manager.tasks.DeviceInfoTask;
import com.wosmart.sdkdemo.manager.tasks.LoginTask;
import com.wosmart.sdkdemo.manager.tasks.MeasureTask;
import com.wosmart.sdkdemo.manager.tasks.SyncTimeTask;
import com.wosmart.sdkdemo.models.ZoneData;
import com.wosmart.ukprotocollibary.WristbandManager;
import com.wosmart.ukprotocollibary.WristbandScanCallback;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class WbManager {
    public static final String TAG = "WbManager";
    private Context context;

    private static WbManager instance;

    public static WbManager getInstance(Context context) {
        if (instance == null) {
            instance = new WbManager(context);
        }
        return instance;
    }

    private WbManager(Context context) {
        this.context = context.getApplicationContext();
    }

    public void start() {
        startScan();
    }

    // Scan process
    private void startScan() {
        Log.e(TAG, "start scan");
        WristbandManager.getInstance(context).startScan(new WristbandScanCallback() {
            @Override
            public void onWristbandDeviceFind(BluetoothDevice device, int rssi, byte[] scanRecord) {
                super.onWristbandDeviceFind(device, rssi, scanRecord);
                Log.e(TAG, "Device found " + device.getAddress());
                stopScan();
                connect(device.getAddress(), device.getName());
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
        WristbandManager.getInstance(context).stopScan();
    }

    private void connect(final String mac, final String name) {
        Log.e(TAG, "Connect " + mac + ", " + name);
        ConnectTask connectTask = new ConnectTask(WristbandManager.getInstance(context), new CommonTask.Callback() {
            @Override
            public void onSuccess(Object... args) {
                login();
            }

            @Override
            public void onFailed() {
                disconnect();
            }
        }, mac);
        connectTask.start();
    }

    private void disconnect() {
        WristbandManager.getInstance(context).close();
    }

    public void login() {
        Log.e(TAG, "Start login");
        final LoginTask loginTask = new LoginTask(WristbandManager.getInstance(context), new CommonTask.Callback() {
            @Override
            public void onSuccess(Object... args) {
                Log.e(TAG, "Login SUCCESS");
                readDeviceInformation();
            }

            @Override
            public void onFailed() {
                Log.e(TAG, "Login FAILED");
            }
        });
        loginTask.start();
    }

    public void readDeviceInformation() {
        DeviceInfoTask task = new DeviceInfoTask(WristbandManager.getInstance(context), new CommonTask.Callback() {
            @Override
            public void onSuccess(Object... args) {
                syncTime();
            }

            @Override
            public void onFailed() {

            }
        });
        task.start();
    }

    private void syncTime() {
        SyncTimeTask task = new SyncTimeTask(WristbandManager.getInstance(context), new CommonTask.Callback() {
            @Override
            public void onSuccess(Object... args) {
                startMeasure();
            }

            @Override
            public void onFailed() {

            }
        });
        task.start();
    }

    private void startMeasure() {
        MeasureTask task = new MeasureTask(WristbandManager.getInstance(context), new CommonTask.Callback() {
            @Override
            public void onSuccess(Object... args) {
                // Finish
            }

            @Override
            public void onFailed() {

            }
        });
        task.start();
    }
}

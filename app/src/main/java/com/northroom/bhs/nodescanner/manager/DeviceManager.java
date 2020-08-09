package com.northroom.bhs.nodescanner.manager;

import android.content.Context;
import android.util.Log;

import com.northroom.bhs.nodescanner.manager.tasks.CommonTask;
import com.northroom.bhs.nodescanner.manager.tasks.ConnectTask;
import com.northroom.bhs.nodescanner.manager.tasks.DeviceInfoTask;
import com.northroom.bhs.nodescanner.manager.tasks.LoginTask;
import com.northroom.bhs.nodescanner.manager.tasks.MeasureTask;
import com.northroom.bhs.nodescanner.manager.tasks.SyncTimeTask;
import com.wosmart.ukprotocollibary.WristbandManager;

import java.util.concurrent.atomic.AtomicBoolean;

public class DeviceManager {
    private static final String TAG = "DeviceManager";
    private final Context context;
    private DeviceManagerListener listener;
    private AtomicBoolean isProcessing = new AtomicBoolean(false);
    private String mac;

    public DeviceManager(Context context) {
        this.context = context;
    }

    public boolean isProcessing() {
        return isProcessing.get();
    }

    public void process(String mac, DeviceManagerListener listener) {
        this.listener = listener;
        this.mac = mac;
        connect(mac);
    }

    private void connect(final String mac) {
        isProcessing.set(true);
        Log.e(TAG, "Connect " + mac);
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
                disconnect();
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
                disconnect();
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
                disconnect();
            }
        });
        task.start();
    }

    private void startMeasure() {
        MeasureTask task = new MeasureTask(context, WristbandManager.getInstance(context), new CommonTask.Callback() {
            @Override
            public void onSuccess(Object... args) {
                // Finish
                disconnect();
            }

            @Override
            public void onFailed() {
                disconnect();
            }
        }, this.mac);
        task.start();
    }

    private void disconnect() {
        isProcessing.set(false);
        WristbandManager.getInstance(context).close();
        listener.onFinish();
    }

    public interface DeviceManagerListener {
        void onFinish();
    }
}

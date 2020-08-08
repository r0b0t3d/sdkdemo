package com.wosmart.sdkdemo.manager;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanRecord;
import android.content.Context;
import android.util.Log;

import com.wosmart.sdkdemo.manager.tasks.CommonTask;
import com.wosmart.sdkdemo.manager.tasks.ConnectTask;
import com.wosmart.sdkdemo.manager.tasks.DeviceInfoTask;
import com.wosmart.sdkdemo.manager.tasks.LoginTask;
import com.wosmart.sdkdemo.manager.tasks.MeasureTask;
import com.wosmart.sdkdemo.manager.tasks.SyncTimeTask;
import com.wosmart.ukprotocollibary.WristbandManager;
import com.wosmart.ukprotocollibary.WristbandManagerCallback;
import com.wosmart.ukprotocollibary.WristbandScanCallback;
import com.wosmart.ukprotocollibary.applicationlayer.ApplicationLayerDeviceInfoPacket;
import com.wosmart.ukprotocollibary.applicationlayer.ApplicationLayerFunctionPacket;
import com.wosmart.ukprotocollibary.applicationlayer.ApplicationLayerHrpItemPacket;
import com.wosmart.ukprotocollibary.applicationlayer.ApplicationLayerHrpPacket;
import com.wosmart.ukprotocollibary.applicationlayer.ApplicationLayerTemperatureControlPacket;

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
                if (!enable) {}
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

            }

            @Override
            public void onFailed() {

            }
        });
        task.start();
//        WristbandManager.getInstance(context).registerCallback(new WristbandManagerCallback() {
//            @Override
//            public void onHrpDataReceiveIndication(ApplicationLayerHrpPacket packet) {
//                super.onHrpDataReceiveIndication(packet);
//                for (ApplicationLayerHrpItemPacket item : packet.getHrpItems()) {
//                    Log.e(TAG, "hr value :" + item.getValue());
//                }
//            }
//
//            @Override
//            public void onDeviceCancelSingleHrpRead() {
//                super.onDeviceCancelSingleHrpRead();
//                Log.e(TAG, "stop measure hr ");
//            }
//        });
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                if (WristbandManager.getInstance(context).readHrpValue()) {
//                    Log.e(TAG, "startMeasure SUCCESS");
//                    startMeasureTemp();
//                } else {
//                    Log.e(TAG, "startMeasure FAIL");
//                }
//            }
//        });
//        thread.start();
    }

    private void stopMeasure() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (WristbandManager.getInstance(context).stopReadHrpValue()) {
                    Log.e(TAG, "stopMeasure SUCCESS");
                } else {
                    Log.e(TAG, "stopMeasure FAIL");
                }
            }
        });
        thread.start();
    }

    private void setTempSetting() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                ApplicationLayerTemperatureControlPacket packet = new ApplicationLayerTemperatureControlPacket();
                packet.setShow(true);
                if (WristbandManager.getInstance(context).setTemperatureControl(packet)) {
                    Log.e(TAG, "setTempSetting SUCCESS");
                } else {
                    Log.e(TAG, "setTempSetting FAIL");
                }
            }
        });
        thread.start();
    }

    private void startMeasureTemp() {
        WristbandManager.getInstance(context).registerCallback(new WristbandManagerCallback() {
            @Override
            public void onTemperatureData(ApplicationLayerHrpPacket packet) {
                super.onTemperatureData(packet);
                for (ApplicationLayerHrpItemPacket item : packet.getHrpItems()) {
                    Log.e(TAG, "temp origin value :" + item.getTempOriginValue() + " temperature adjust value : " + item.getTemperature() + " is wear :" + item.isWearStatus() + " is adjust : " + item.isAdjustStatus() + "is animation :" + item.isAnimationStatus());
                }
            }

            @Override
            public void onTemperatureMeasureSetting(ApplicationLayerTemperatureControlPacket packet) {
                super.onTemperatureMeasureSetting(packet);
                Log.e(TAG, "temp setting : show = " + packet.isShow() + " adjust = " + packet.isAdjust() + " celsius unit = " + packet.isCelsiusUnit());
            }

            @Override
            public void onTemperatureMeasureStatus(int status) {
                super.onTemperatureMeasureStatus(status);
                Log.e(TAG, "temp status :" + status);
            }
        });
        setTempSetting();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (WristbandManager.getInstance(context).setTemperatureStatus(true)) {
                    Log.e(TAG, "startMeasureTemp SUCCESS");
                } else {
                    Log.e(TAG, "startMeasureTemp FAIL");
                }
            }
        });
        thread.start();
    }

    private void stopMeasureTemp() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (WristbandManager.getInstance(context).setTemperatureStatus(false)) {
                    Log.e(TAG, "stopMeasureTemp SUCCESS");
                } else {
                    Log.e(TAG, "stopMeasureTemp FAIL");
                }
            }
        });
        thread.start();
    }
}

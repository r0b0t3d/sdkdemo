package com.wosmart.sdkdemo.activity;

import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanRecord;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import com.wosmart.sdkdemo.R;
import com.wosmart.sdkdemo.manager.WbManager;
import com.wosmart.ukprotocollibary.WristbandManager;
import com.wosmart.ukprotocollibary.WristbandManagerCallback;
import com.wosmart.ukprotocollibary.WristbandScanCallback;
import com.wosmart.ukprotocollibary.applicationlayer.ApplicationLayerDeviceInfoPacket;
import com.wosmart.ukprotocollibary.applicationlayer.ApplicationLayerFunctionPacket;
import com.wosmart.ukprotocollibary.applicationlayer.ApplicationLayerHrpItemPacket;
import com.wosmart.ukprotocollibary.applicationlayer.ApplicationLayerHrpPacket;
import com.wosmart.ukprotocollibary.applicationlayer.ApplicationLayerTemperatureControlPacket;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();

//        startScan();
        WbManager.getInstance(this).start();
    }

    private void initData() {
        checkStoragePermission();
        checkLocationPermission();
    }

    private void checkLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getApplication().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 0x01);
            }
        }
    }

    private void checkStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                //未获得授权
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0x02);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0x02) {
            if (grantResults.length > 0) {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                }
            } else {
            }
        }
    }

    // Scan process
    private void startScan() {
        Log.e(TAG, "start scan");
        WristbandManager.getInstance(MainActivity.this).startScan(new WristbandScanCallback() {
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
        WristbandManager.getInstance(MainActivity.this).stopScan();
    }

    private void connect(final String mac, final String name) {

        WristbandManager.getInstance(MainActivity.this).registerCallback(new WristbandManagerCallback() {
            @Override
            public void onConnectionStateChange(boolean status) {
                super.onConnectionStateChange(status);
                if (status) {
                    login();
                } else {
                    disconnect();
                }
            }

            @Override
            public void onError(int error) {
                super.onError(error);
            }
        });

        WristbandManager.getInstance(MainActivity.this).connect(mac);

    }

    private void disconnect() {
        WristbandManager.getInstance(MainActivity.this).close();
    }

    public void login() {
        WristbandManager.getInstance(MainActivity.this).registerCallback(new WristbandManagerCallback() {
            @Override
            public void onLoginStateChange(int state) {
                super.onLoginStateChange(state);
                if (state == WristbandManager.STATE_WRIST_LOGIN) {
                    Log.e(TAG, "Login success");

                    readDeviceInformation();
                }
            }
        });
        WristbandManager.getInstance(MainActivity.this).startLoginProcess("1234567890");
    }



    public void readDeviceInformation() {
        WristbandManager.getInstance(MainActivity.this).registerCallback(new WristbandManagerCallback() {

            @Override
            public void onDeviceInfo(ApplicationLayerDeviceInfoPacket packet) {
                super.onDeviceInfo(packet);
                Log.e(TAG, "device info = " + packet.toString());
            }

            @Override
            public void onDeviceFunction(ApplicationLayerFunctionPacket packet) {
                super.onDeviceFunction(packet);
                Log.e(TAG, "function info = " + packet.toString());
                syncTime();
            }
        });

        readVersion();
    }

    private void readVersion() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (WristbandManager.getInstance(MainActivity.this).requestDeviceInfo()) {
                    Log.e(TAG, "readVersion SUCCESS");
                    readFunction();
                } else {
                    Log.e(TAG, "readVersion FAIL");
                }
            }
        });
        thread.start();
    }

    private void readFunction() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (WristbandManager.getInstance(MainActivity.this).sendFunctionReq()) {
                    Log.e(TAG, "readFunction SUCCESS");
                } else {
                    Log.e(TAG, "readFunction FAIL");
                }
            }
        });
        thread.start();
    }

    private void syncTime() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (WristbandManager.getInstance(MainActivity.this).setTimeSync()) {
                    Log.e(TAG, "syncTime SUCCESS");
                    startMeasure();
                } else {
                    Log.e(TAG, "syncTime FAILED");
                }
            }
        });
        thread.start();
    }

    private void startMeasure() {
        WristbandManager.getInstance(MainActivity.this).registerCallback(new WristbandManagerCallback() {
            @Override
            public void onHrpDataReceiveIndication(ApplicationLayerHrpPacket packet) {
                super.onHrpDataReceiveIndication(packet);
                for (ApplicationLayerHrpItemPacket item : packet.getHrpItems()) {
                    Log.e(TAG, "hr value :" + item.getValue());
                }
            }

            @Override
            public void onDeviceCancelSingleHrpRead() {
                super.onDeviceCancelSingleHrpRead();
                Log.e(TAG, "stop measure hr ");
            }
        });
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (WristbandManager.getInstance(MainActivity.this).readHrpValue()) {
                    Log.e(TAG, "startMeasure SUCCESS");
                    startMeasureTemp();
                } else {
                    Log.e(TAG, "startMeasure FAIL");
                }
            }
        });
        thread.start();
    }

    private void stopMeasure() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (WristbandManager.getInstance(MainActivity.this).stopReadHrpValue()) {
                    Log.e(TAG, "stopMeasure SUCCESS");
                } else {
                    Log.e(TAG, "stopMeasure FAIL");
                }
            }
        });
        thread.start();
    }

    private void startMeasureTemp() {
        WristbandManager.getInstance(MainActivity.this).registerCallback(new WristbandManagerCallback() {
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
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (WristbandManager.getInstance(MainActivity.this).setTemperatureStatus(true)) {
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
                if (WristbandManager.getInstance(MainActivity.this).setTemperatureStatus(false)) {
                    Log.e(TAG, "stopMeasureTemp SUCCESS");
                } else {
                    Log.e(TAG, "stopMeasureTemp FAIL");
                }
            }
        });
        thread.start();
    }
}

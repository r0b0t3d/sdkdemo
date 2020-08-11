package com.northroom.bhs.nodescanner.manager;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.util.Log;

import androidx.annotation.NonNull;

import com.northroom.bhs.nodescanner.Configs;
import com.thanosfisherman.wifiutils.WifiUtils;
import com.thanosfisherman.wifiutils.wifiConnect.ConnectionErrorCode;
import com.thanosfisherman.wifiutils.wifiConnect.ConnectionSuccessListener;
import com.thanosfisherman.wifiutils.wifiState.WifiStateListener;

public class WifiManager {
    private static final String TAG = "WifiManager";
    private static WifiManager instance;
    private Context context;

    public static WifiManager getInstance(Context context) {
        if (instance == null) {
            instance = new WifiManager(context);
        }
        return instance;
    }

    private WifiManager(Context context) {
        this.context = context.getApplicationContext();
    }

    public void enableWifi() {
        WifiUtils.enableLog(true);
        WifiUtils.withContext(context).enableWifi(new WifiStateListener() {
            @Override
            public void isSuccess(boolean isSuccess) {
                if (isSuccess) {
                    Log.e(TAG, "WIFI ENABLED");
                    scanWifis();
                }  else {
                    Log.e(TAG, "COULDN'T ENABLED WIFI");
                }
            }
        });
    }

    private void scanWifis() {
        Log.e(TAG, "Scan wifi");
        WifiUtils.withContext(context).scanWifi(scanResults -> {
            if (scanResults.isEmpty()) {
                Log.i(TAG, "SCAN RESULTS IT'S EMPTY");
                return;
            }
            Log.i(TAG, "GOT SCAN RESULTS " + scanResults);
            for (ScanResult result : scanResults) {
                if (result.SSID.equals(Configs.WIFI_SSID)) {
                    connectWifi(Configs.WIFI_SSID, Configs.WIFI_PASSWORD);
                    return;
                }
            }
            Log.e(TAG, "Could not found wifi " + Configs.WIFI_SSID);
        }).start();
    }

    public void connectWifi(String ssid, String password) {
        Log.e(TAG, "Connect wifi " + ssid);
        WifiUtils.withContext(context)
                .connectWith(ssid, password)
                .setTimeout(5 * 60 * 60 * 1000)
                .onConnectionResult(new ConnectionSuccessListener() {
                    @Override
                    public void success() {
                        Log.e(TAG, "CONNECT WIFI SUCCESS");
                    }

                    @Override
                    public void failed(@NonNull ConnectionErrorCode errorCode) {
                        Log.e(TAG, "CONNECT WIFI FAILED " + errorCode.toString());
                    }
                });
    }
}

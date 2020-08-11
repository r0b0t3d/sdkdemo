package com.northroom.bhs.nodescanner.manager;

import android.content.Context;
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
                    connectWifi(Configs.WIFI_SSID, Configs.WIFI_PASSWORD);
                }  else {
                    Log.e(TAG, "COULDN'T ENABLED WIFI");
                }
            }
        });
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

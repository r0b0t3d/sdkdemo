package com.wosmart.sdkdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.wosmart.sdkdemo.Common.BaseActivity;
import com.wosmart.ukprotocollibary.WristbandManager;
import com.wosmart.ukprotocollibary.WristbandManagerCallback;
import com.wosmart.ukprotocollibary.applicationlayer.ApplicationLayerDeviceInfoPacket;
import com.wosmart.ukprotocollibary.applicationlayer.ApplicationLayerFunctionPacket;

public class DeviceInfoActivity extends BaseActivity implements View.OnClickListener {
    private String tag = "DeviceInfoActivity";
    private Toolbar toolbar;
    private Button btn_read_device;
    private Button btn_read_function;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_info);
        initView();
        initData();
        addListener();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        btn_read_device = findViewById(R.id.btn_read_device);
        btn_read_function = findViewById(R.id.btn_read_function);
    }

    private void initData() {
        WristbandManager.getInstance(this).registerCallback(new WristbandManagerCallback() {

            @Override
            public void onDeviceInfo(ApplicationLayerDeviceInfoPacket packet) {
                super.onDeviceInfo(packet);
                Log.i(tag, "device info = " + packet.toString());
            }

            @Override
            public void onDeviceFunction(ApplicationLayerFunctionPacket packet) {
                super.onDeviceFunction(packet);
                Log.i(tag, "function info = " + packet.toString());
            }
        });
    }

    private void addListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_read_device.setOnClickListener(this);
        btn_read_function.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_read_device:
                readVersion();
                break;
            case R.id.btn_read_function:
                readFunction();
                break;
        }
    }

    private void readVersion() {
        if (WristbandManager.getInstance(this).requestDeviceInfo()) {
            showToast(getString(R.string.app_success));
        } else {
            showToast(getString(R.string.app_fail));
        }
    }

    private void readFunction() {
        if (WristbandManager.getInstance(this).sendFunctionReq()) {
            showToast(getString(R.string.app_success));
        } else {
            showToast(getString(R.string.app_success));
        }
    }
}

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
import com.wosmart.ukprotocollibary.model.data.DeviceFunction;
import com.wosmart.ukprotocollibary.model.data.ReminderFunction;
import com.wosmart.ukprotocollibary.model.data.SportFunction;

public class DeviceInfoActivity extends BaseActivity implements View.OnClickListener {
    private String tag = "DeviceInfoActivity";
    private Toolbar toolbar;
    private Button btn_read_version;
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
        btn_read_version = findViewById(R.id.btn_read_version);
        btn_read_function = findViewById(R.id.btn_read_function);
    }

    private void initData() {
        WristbandManager.getInstance(this).registerCallback(new WristbandManagerCallback() {
            @Override
            public void onVersionRead(int appVersion, int patchVersion) {
                super.onVersionRead(appVersion, patchVersion);
                showToast("appVersion : " + appVersion + "patchVersion : " + patchVersion);
            }

            @Override
            public void onDeviceFunction(DeviceFunction deviceFunction) {
                super.onDeviceFunction(deviceFunction);
                Log.i(tag, "deviceFunction = " + deviceFunction.toString());
                showToast("deviceFunction : " + deviceFunction.toString());
            }

            @Override
            public void onReminderFunction(ReminderFunction reminderFunction) {
                super.onReminderFunction(reminderFunction);
                Log.i(tag, "reminderFunction = " + reminderFunction.toString());
                showToast("reminderFunction : " + reminderFunction.toString());
            }

            @Override
            public void onSportFunction(SportFunction sportFunction) {
                super.onSportFunction(sportFunction);
                Log.i(tag, "sportFunction = " + sportFunction.toString());
                showToast("sportFunction : " + sportFunction.toString());
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
        btn_read_version.setOnClickListener(this);
        btn_read_function.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_read_version:
                readVersion();
                break;
            case R.id.btn_read_function:
                readFunction();
                break;
        }
    }

    private void readVersion() {
        if (WristbandManager.getInstance(this).readDfuVersion()) {
            showToast("读取版本成功");
        } else {
            showToast("读取版本失败");
        }
    }

    private void readFunction() {
        if (WristbandManager.getInstance(this).sendFunctionReq()) {
            showToast("读取功能成功");
        } else {
            showToast("读取功能失败");
        }
    }
}

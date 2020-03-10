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

public class BatteryActivity extends BaseActivity implements View.OnClickListener {

    private String tag = "BatteryActivity";

    private Toolbar toolbar;

    private Button btn_query_battery;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battery);
        initView();
        initData();
        addListener();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        btn_query_battery = findViewById(R.id.btn_query_battery);
    }

    private void initData() {
        WristbandManager.getInstance(this).registerCallback(new WristbandManagerCallback() {
            @Override
            public void onBatteryRead(int value) {
                super.onBatteryRead(value);
                Log.i(tag, "battery : " + value);
            }

            @Override
            public void onBatteryChange(int value) {
                super.onBatteryChange(value);
                Log.i(tag, "battery : " + value);
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
        btn_query_battery.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_query_battery:
                queryBattery();
                break;
        }
    }

    private void queryBattery() {
        if (WristbandManager.getInstance(this).readBatteryLevel()) {
            showToast(getString(R.string.app_success));
        } else {
            showToast(getString(R.string.app_fail));
        }
    }
}

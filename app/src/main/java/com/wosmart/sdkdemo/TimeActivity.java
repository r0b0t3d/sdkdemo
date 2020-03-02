package com.wosmart.sdkdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import com.wosmart.sdkdemo.Common.BaseActivity;
import com.wosmart.ukprotocollibary.WristbandManager;

public class TimeActivity extends BaseActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private Button btn_sync_time;
    private RadioGroup rg_hour_system;
    private Button btn_sync_hour;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);
        initView();
        initData();
        addListener();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        btn_sync_time = findViewById(R.id.btn_sync_time);
        rg_hour_system = findViewById(R.id.rg_hour_system);
        btn_sync_hour = findViewById(R.id.btn_sync_hour);
    }

    private void initData() {

    }

    private void addListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_sync_time.setOnClickListener(this);
        btn_sync_hour.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sync_time:
                syncTime();
                break;
            case R.id.btn_sync_hour:
                boolean flag = rg_hour_system.getCheckedRadioButtonId() == R.id.rb_24;
                setHourUnit(flag);
                break;
        }
    }

    private void syncTime() {
        if (WristbandManager.getInstance(this).setTimeSync()) {
            showToast("同步时间成功");
        } else {
            showToast("同步时间失败");
        }
    }

    private void setHourUnit(boolean is24Model) {
        if (WristbandManager.getInstance(this).setHourSystem(is24Model)) {
            showToast("设置成功");
        } else {
            showToast("设置失败");
        }
    }
}

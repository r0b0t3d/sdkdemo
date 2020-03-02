package com.wosmart.sdkdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.wosmart.bandlibrary.bluetooth.connect.response.BleWriteResponse;
import com.wosmart.bandlibrary.protocol.WoBtOperationManager;
import com.wosmart.bandlibrary.protocol.listener.SendShortListener;
import com.wosmart.sdkdemo.Common.BaseActivity;

public class MotorActivity extends BaseActivity implements View.OnClickListener {
    private Toolbar toolbar;

    private Button btn_shock;

    private Button btn_un_shock;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motor);
        initView();
        initData();
        addListener();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        btn_shock = findViewById(R.id.btn_shock);
        btn_un_shock = findViewById(R.id.btn_un_shock);
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
        btn_shock.setOnClickListener(this);
        btn_un_shock.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_shock:
                controlMotor(true);
                break;
            case R.id.btn_un_shock:
                controlMotor(false);
                break;
        }
    }

    private void controlMotor(boolean flag) {
        WoBtOperationManager.getInstance(this).controlMotor(flag, new BleWriteResponse() {
            @Override
            public void onResponse(int code) {

            }
        }, new SendShortListener() {
            @Override
            public void onSendFail() {
                showToast("发送失败");
            }

            @Override
            public void onSendSuccess() {
                showToast("发送成功");
            }
        });
    }
}

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

public class ResetActivity extends BaseActivity implements View.OnClickListener {

    private Toolbar toolbar;

    private Button btn_reset;

    private Button btn_shut_down;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);
        initView();
        initData();
        addListener();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        btn_reset = findViewById(R.id.btn_reset);
        btn_shut_down = findViewById(R.id.btn_shut_down);
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
        btn_reset.setOnClickListener(this);
        btn_shut_down.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_reset:
                reset(0);
                break;
            case R.id.btn_shut_down:
                reset(1);
                break;
        }
    }

    private void reset(int type) {
        WoBtOperationManager.getInstance(this).sendReset(type, new BleWriteResponse() {
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

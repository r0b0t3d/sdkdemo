package com.wosmart.sdkdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import com.wosmart.bandlibrary.bluetooth.connect.response.BleWriteResponse;
import com.wosmart.bandlibrary.protocol.WoBtOperationManager;
import com.wosmart.bandlibrary.protocol.listener.SendShortListener;
import com.wosmart.bandlibrary.protocol.listener.StepWatchListener;
import com.wosmart.sdkdemo.Common.BaseActivity;

public class StepWatchActivity extends BaseActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private Button btn_read;
    private RadioGroup rg_status;
    private Button btn_set;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_watch);
        initView();
        initData();
        addListener();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        btn_read = findViewById(R.id.btn_read);
        rg_status = findViewById(R.id.rg_status);
        btn_set = findViewById(R.id.btn_set);
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
        btn_read.setOnClickListener(this);
        btn_set.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_read:
                readStepWatch();
                break;
            case R.id.btn_set:
                boolean flag = false;
                if (rg_status.getCheckedRadioButtonId() == R.id.rb_open) {
                    flag = true;
                }
                setStepWatch(flag);
                break;
        }
    }

    private void readStepWatch() {
        WoBtOperationManager.getInstance(this).readStepWatch(new BleWriteResponse() {
            @Override
            public void onResponse(int code) {

            }
        }, new StepWatchListener() {
            @Override
            public void onFail() {
                showToast("读取失败");
            }

            @Override
            public void onSuccess(boolean isOpen) {
                showToast("读取成功 " + isOpen);
            }
        });
    }

    private void setStepWatch(boolean flag) {
        WoBtOperationManager.getInstance(this).setStepWatch(flag, new BleWriteResponse() {
            @Override
            public void onResponse(int code) {

            }
        }, new SendShortListener() {
            @Override
            public void onSendFail() {
                showToast("设置失败");
            }

            @Override
            public void onSendSuccess() {
                showToast("设置成功");
            }
        });
    }
}

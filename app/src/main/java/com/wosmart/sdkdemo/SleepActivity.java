package com.wosmart.sdkdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.wosmart.bandlibrary.bluetooth.connect.response.BleWriteResponse;
import com.wosmart.bandlibrary.protocol.WoBtOperationManager;
import com.wosmart.bandlibrary.protocol.listener.HistorySleepListener;
import com.wosmart.bandlibrary.protocol.listener.SendShortListener;
import com.wosmart.bandlibrary.protocol.model.data.SleepData;
import com.wosmart.sdkdemo.Common.BaseActivity;

public class SleepActivity extends BaseActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private Button btn_create;
    private EditText et_read_day;
    private Button btn_read_history;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep);
        initView();
        initData();
        addListener();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        btn_create = findViewById(R.id.btn_create);
        et_read_day = findViewById(R.id.et_read_day);
        btn_read_history = findViewById(R.id.btn_read_history);
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
        btn_create.setOnClickListener(this);
        btn_read_history.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_create:
                createHistory();
                break;
            case R.id.btn_read_history:
                String dayStr = et_read_day.getText().toString();
                if (null != dayStr && !dayStr.isEmpty()) {
                    int day = Integer.parseInt(dayStr);
                    readHistory(day);
                } else {
                    showToast("请输入读取天数");
                }
                break;
        }
    }

    private void createHistory() {
        WoBtOperationManager.getInstance(this).createHistorySleep(new BleWriteResponse() {
            @Override
            public void onResponse(int code) {

            }
        }, new SendShortListener() {
            @Override
            public void onSendFail() {
                showToast("创建失败");
            }

            @Override
            public void onSendSuccess() {
                showToast("创建成功");
            }
        });
    }

    private void readHistory(int day) {
        WoBtOperationManager.getInstance(this).readHistorySleep(day, new BleWriteResponse() {
            @Override
            public void onResponse(int code) {

            }
        }, new HistorySleepListener() {
            @Override
            public void onSleep(SleepData sleepData) {
                showToast("读取历史睡眠 " + sleepData.toString());
            }

            @Override
            public void onReadCompleted() {
                showToast("读取历史睡眠完成");
            }

            @Override
            public void onReadFail() {
                showToast("读取历史睡眠失败");
            }
        });
    }
}

package com.wosmart.sdkdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.wosmart.bandlibrary.bluetooth.connect.response.BleWriteResponse;
import com.wosmart.bandlibrary.protocol.WoBtOperationManager;
import com.wosmart.bandlibrary.protocol.listener.ReadHomeUiListener;
import com.wosmart.bandlibrary.protocol.listener.SendShortListener;
import com.wosmart.bandlibrary.protocol.model.data.HomeUiData;
import com.wosmart.sdkdemo.Common.BaseActivity;

public class HomeUiActivity extends BaseActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private EditText et_ui;
    private Button btn_read;
    private Button btn_set;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_ui);
        initView();
        initData();
        addListener();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        et_ui = findViewById(R.id.et_ui);
        btn_read = findViewById(R.id.btn_read);
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
                readHomeUI();
                break;
            case R.id.btn_set:
                String indexStr = et_ui.getText().toString();
                if (null != indexStr && !indexStr.isEmpty()) {
                    int index = Integer.parseInt(indexStr);
                    setUI(index);
                } else {
                    showToast("请输入Ui序号");
                }
                break;
        }
    }

    private void readHomeUI() {
        WoBtOperationManager.getInstance(this).readHomeUI(new BleWriteResponse() {
            @Override
            public void onResponse(int code) {

            }
        }, new ReadHomeUiListener() {
            @Override
            public void onReadFail() {
                showToast("读取失败");
            }

            @Override
            public void onReadSuccess(HomeUiData homeUiData) {
                showToast("读取成功 " + homeUiData.toString());
            }
        });
    }

    private void setUI(int index) {
        WoBtOperationManager.getInstance(this).setHomeUI(index, new BleWriteResponse() {
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

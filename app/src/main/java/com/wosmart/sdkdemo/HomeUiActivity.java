package com.wosmart.sdkdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.wosmart.sdkdemo.Common.BaseActivity;
import com.wosmart.ukprotocollibary.WristbandManager;
import com.wosmart.ukprotocollibary.WristbandManagerCallback;
import com.wosmart.ukprotocollibary.applicationlayer.ApplicationLayerScreenStylePacket;

public class HomeUiActivity extends BaseActivity implements View.OnClickListener {
    private String tag = "HomeUiActivity";
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
        WristbandManager.getInstance(this).registerCallback(new WristbandManagerCallback() {
            @Override
            public void onHomePager(ApplicationLayerScreenStylePacket packet) {
                super.onHomePager(packet);
                Log.i(tag, "home ui = " + packet.toString());
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
                    showToast(getString(R.string.app_home_ui_hint));
                }
                break;
        }
    }

    private void readHomeUI() {
        if (WristbandManager.getInstance(this).requestHomePager()) {
            showToast(getString(R.string.app_success));
        } else {
            showToast(getString(R.string.app_fail));
        }
    }

    private void setUI(int index) {
        if (WristbandManager.getInstance(this).settingHomePager(index)) {
            showToast(getString(R.string.app_success));
        } else {
            showToast(getString(R.string.app_fail));
        }
    }
}

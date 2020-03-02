package com.wosmart.sdkdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.wosmart.sdkdemo.Common.BaseActivity;
import com.wosmart.ukprotocollibary.WristbandManager;

public class FindBandActivity extends BaseActivity implements View.OnClickListener {

    private Toolbar toolbar;

    private Button btn_find_band;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_band);
        initView();
        initData();
        addListener();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        btn_find_band = findViewById(R.id.btn_find_band);
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
        btn_find_band.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_find_band:
                findBand();
                break;
        }
    }

    private void findBand() {
        if (WristbandManager.getInstance(this).enableImmediateAlert(true)) {
            showToast("查找手环成功");
        } else {
            showToast("查找手环失败");
        }
    }
}

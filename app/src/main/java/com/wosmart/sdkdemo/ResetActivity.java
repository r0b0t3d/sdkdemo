package com.wosmart.sdkdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.wosmart.sdkdemo.Common.BaseActivity;
import com.wosmart.ukprotocollibary.WristbandManager;

public class ResetActivity extends BaseActivity implements View.OnClickListener {

    private Toolbar toolbar;

    private Button btn_close;

    private Button btn_remove_bind;

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
        btn_close = findViewById(R.id.btn_close);
        btn_remove_bind = findViewById(R.id.btn_remove_bind);
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
        btn_close.setOnClickListener(this);
        btn_remove_bind.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_close:
                close();
                break;
            case R.id.btn_remove_bind:
                removeBind();
                break;
        }
    }

    private void close() {
        WristbandManager.getInstance(this).close();
    }

    private void removeBind() {
        if (WristbandManager.getInstance(this).sendRemoveBondCommand()) {
            WristbandManager.getInstance(this).close();
            showToast(getString(R.string.app_success));
        } else {
            showToast(getString(R.string.app_fail));
        }
    }
}

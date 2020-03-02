package com.wosmart.sdkdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.wosmart.bandlibrary.protocol.WoBtOperationManager;
import com.wosmart.bandlibrary.protocol.listener.FindPhoneListener;
import com.wosmart.sdkdemo.Common.BaseActivity;

public class FindPhoneActivity extends BaseActivity implements View.OnClickListener {

    private Toolbar toolbar;

    private Button btn_register;

    private Button btn_unregister;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_phone);
        initView();
        initData();
        addListener();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        btn_register = findViewById(R.id.btn_register);
        btn_unregister = findViewById(R.id.btn_unregister);
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
        btn_register.setOnClickListener(this);
        btn_unregister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register:
                registerFindPhone();
                break;
            case R.id.btn_unregister:
                unRegisterFindPhone();
                break;
        }
    }

    private void registerFindPhone() {
        WoBtOperationManager.getInstance(this).registerFindPhoneListener(new FindPhoneListener() {
            @Override
            public void onFindPhone() {
                showToast("查找手机啦");
            }
        });
    }

    private void unRegisterFindPhone() {
        WoBtOperationManager.getInstance(this).unRegisterFindPhoneListener();
    }
}

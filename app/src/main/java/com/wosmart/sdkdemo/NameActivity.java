package com.wosmart.sdkdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.wosmart.sdkdemo.Common.BaseActivity;
import com.wosmart.ukprotocollibary.WristbandManager;

public class NameActivity extends BaseActivity implements View.OnClickListener {

    private Toolbar toolbar;

    private EditText et_name;

    private Button btn_modify_name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);
        initView();
        initData();
        addListener();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        et_name = findViewById(R.id.et_name);
        btn_modify_name = findViewById(R.id.btn_modify_name);
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
        btn_modify_name.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_modify_name:
                String nameStr = et_name.getText().toString();
                if (null != nameStr && !nameStr.isEmpty()) {
                    modifyName(nameStr);
                } else {
                    showToast("请输入要修改的名称");
                }
                break;
        }
    }

    private void modifyName(String name) {
        if (WristbandManager.getInstance(this).setDeviceName(name)) {
            showToast("修改成功");
        } else {
            showToast("修改失败");
        }
    }
}

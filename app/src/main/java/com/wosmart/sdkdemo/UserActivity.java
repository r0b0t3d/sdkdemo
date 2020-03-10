package com.wosmart.sdkdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.wosmart.sdkdemo.Common.BaseActivity;
import com.wosmart.ukprotocollibary.WristbandManager;
import com.wosmart.ukprotocollibary.WristbandManagerCallback;
import com.wosmart.ukprotocollibary.applicationlayer.ApplicationLayerUserPacket;

public class UserActivity extends BaseActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private EditText et_age;
    private RadioGroup rg_gender;
    private EditText et_height;
    private EditText et_weight;
    private Button btn_sync_user_info;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        initView();
        initData();
        addListener();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        et_age = findViewById(R.id.et_age);
        rg_gender = findViewById(R.id.rg_gender);
        et_height = findViewById(R.id.et_height);
        et_weight = findViewById(R.id.et_weight);
        btn_sync_user_info = findViewById(R.id.btn_sync_user_info);
    }

    private void initData() {
        WristbandManager.getInstance(this).registerCallback(new WristbandManagerCallback() {
            @Override
            public void onError(int error) {
                super.onError(error);
                showToast(getString(R.string.app_error));
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
        btn_sync_user_info.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sync_user_info:
                String ageStr = et_age.getText().toString();
                String heightStr = et_height.getText().toString();
                String weightStr = et_weight.getText().toString();
                int age;
                int height;
                float weight;
                if (null != ageStr && !ageStr.isEmpty()) {
                    age = Integer.parseInt(ageStr);
                } else {
                    showToast(getString(R.string.app_user_hint_age));
                    return;
                }
                if (null != heightStr && !heightStr.isEmpty()) {
                    height = Integer.parseInt(heightStr);
                } else {
                    showToast(getString(R.string.app_user_hint_height));
                    return;
                }
                if (null != weightStr && !weightStr.isEmpty()) {
                    weight = Float.parseFloat(weightStr);
                } else {
                    showToast(getString(R.string.app_user_hint_weight));
                    return;
                }
                int checkGender = rg_gender.getCheckedRadioButtonId();
                ApplicationLayerUserPacket info = new ApplicationLayerUserPacket(checkGender == R.id.rb_male, age, height, weight);
                syncUserInfo(info);
                break;
        }
    }

    private void syncUserInfo(ApplicationLayerUserPacket info) {
        if (WristbandManager.getInstance(this).setUserProfile(info)) {
            showToast(getString(R.string.app_success));
        } else {
            showToast(getString(R.string.app_fail));
        }
    }
}

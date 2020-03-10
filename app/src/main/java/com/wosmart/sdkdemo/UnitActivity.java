package com.wosmart.sdkdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import com.wosmart.sdkdemo.Common.BaseActivity;
import com.wosmart.ukprotocollibary.WristbandManager;

public class UnitActivity extends BaseActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private RadioGroup rg_unit;
    private Button btn_set;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit);
        initView();
        initData();
        addListener();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        rg_unit = findViewById(R.id.rg_unit);
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
        btn_set.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_set:
                boolean isMetric;
                if (rg_unit.getCheckedRadioButtonId() == R.id.rb_metric) {
                    isMetric = true;
                } else {
                    isMetric = false;
                }
                setUnit(isMetric);
                break;
        }
    }

    private void setUnit(boolean isMetric) {
        if (WristbandManager.getInstance(this).settingUnitSystem(isMetric)) {
            showToast(getString(R.string.app_success));
        } else {
            showToast(getString(R.string.app_fail));
        }
    }
}

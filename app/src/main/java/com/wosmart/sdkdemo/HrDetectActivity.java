package com.wosmart.sdkdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.wosmart.bandlibrary.protocol.model.data.HrDetect;
import com.wosmart.sdkdemo.Common.BaseActivity;
import com.wosmart.ukprotocollibary.WristbandManager;
import com.wosmart.ukprotocollibary.WristbandManagerCallback;

public class HrDetectActivity extends BaseActivity implements View.OnClickListener {

    private Toolbar toolbar;

    private Button btn_read;

    private RadioGroup rg_detect_status;

    private EditText et_interval;

    private Button btn_set;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_detect);
        initView();
        initData();
        addListener();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        btn_read = findViewById(R.id.btn_read);
        rg_detect_status = findViewById(R.id.rg_detect_status);
        et_interval = findViewById(R.id.et_interval);
        btn_set = findViewById(R.id.btn_set);
    }

    private void initData() {
        WristbandManager.getInstance(this).registerCallback(new WristbandManagerCallback() {
            @Override
            public void onHrpContinueParamRsp(boolean enable, int interval) {
                super.onHrpContinueParamRsp(enable, interval);
                showToast("enable : " + enable + "interval : " + interval);
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
                readHrDetect();
                break;
            case R.id.btn_set:
                boolean flag = rg_detect_status.getCheckedRadioButtonId() == R.id.rb_detect_open;
                String intervalStr = et_interval.getText().toString();
                if (null != intervalStr && !intervalStr.isEmpty()) {
                    int interval = Integer.parseInt(intervalStr);
                    HrDetect hrDetect = new HrDetect();
                    hrDetect.setOpen(flag);
                    hrDetect.setInterval(interval);
                    setHrDetect(hrDetect);
                } else {
                    showToast("请输入检测间隔");
                }
                break;
        }
    }

    private void readHrDetect() {
        if (WristbandManager.getInstance(this).sendContinueHrpParamRequest()) {
            showToast("读取成功");
        } else {
            showToast("读取失败");
        }
    }

    private void setHrDetect(HrDetect hrDetect) {
        if (WristbandManager.getInstance(this).setContinueHrp(hrDetect.isOpen(), hrDetect.getInterval())) {
            showToast("设置成功");
        } else {
            showToast("设置失败");
        }
    }
}

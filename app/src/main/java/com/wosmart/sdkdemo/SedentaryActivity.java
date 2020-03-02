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
import com.wosmart.ukprotocollibary.applicationlayer.ApplicationLayerSitPacket;

public class SedentaryActivity extends BaseActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private Button btn_read;
    private RadioGroup rg_status;
    private EditText et_max;
    private EditText et_interval;
    private EditText et_start_minute;
    private EditText et_end_minute;
    private Button btn_set_sedentary;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sedentary);
        initView();
        initData();
        addListener();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        btn_read = findViewById(R.id.btn_read);
        rg_status = findViewById(R.id.rg_status);
        et_max = findViewById(R.id.et_max);
        et_interval = findViewById(R.id.et_interval);
        et_start_minute = findViewById(R.id.et_start_minute);
        et_end_minute = findViewById(R.id.et_end_minute);
        btn_set_sedentary = findViewById(R.id.btn_set_sedentary);
    }

    private void initData() {
        WristbandManager.getInstance(this).registerCallback(new WristbandManagerCallback() {
            @Override
            public void onLongSitSettingReceive(boolean openFlag) {
                super.onLongSitSettingReceive(openFlag);
                showToast("open flag :" + openFlag);
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
        btn_set_sedentary.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_read:
                readSedentary();
                break;
            case R.id.btn_set_sedentary:
                String maxStr = et_max.getText().toString();
                String startMinuteStr = et_start_minute.getText().toString();
                String endMinuteStr = et_end_minute.getText().toString();
                String intervalStr = et_interval.getText().toString();
                if (null != maxStr) {
                    if (null != startMinuteStr) {
                        if (null != endMinuteStr) {
                            if (null != intervalStr) {
                                int max = Integer.parseInt(maxStr);
                                int interval = Integer.parseInt(intervalStr);
                                int startMinute = Integer.parseInt(startMinuteStr);
                                int endMinute = Integer.parseInt(endMinuteStr);
                                boolean openFlag = false;
                                if (rg_status.getCheckedRadioButtonId() == R.id.rb_open) {
                                    openFlag = true;
                                }
                                ApplicationLayerSitPacket sedentary = new ApplicationLayerSitPacket();
                                sedentary.setmEnable(openFlag ? ApplicationLayerSitPacket.LONG_SIT_CONTROL_ENABLE : ApplicationLayerSitPacket.LONG_SIT_CONTROL_DISABLE);
                                sedentary.setmThreshold(max);
                                sedentary.setmNotifyTime(interval);
                                sedentary.setmStartNotifyTime(startMinute);
                                sedentary.setmStopNotifyTime(endMinute);
                                sedentary.setmDayFlags(ApplicationLayerSitPacket.REPETITION_ALL);

                                setSedentary(sedentary);
                            } else {
                                showToast("请输入间隔时间");
                            }
                        } else {
                            showToast("请输入结束分钟");
                        }
                    } else {
                        showToast("请输入开始分钟");
                    }
                } else {
                    showToast("请输入阀值");
                }
                break;
        }
    }

    private void readSedentary() {
        if (WristbandManager.getInstance(this).sendLongSitRequest()) {
            showToast("读取成功");
        } else {
            showToast("读取失败");
        }
    }

    private void setSedentary(ApplicationLayerSitPacket packet) {
        if (WristbandManager.getInstance(this).setLongSit(packet)) {
            showToast("设置成功");
        } else {
            showToast("设置失败");
        }
    }
}

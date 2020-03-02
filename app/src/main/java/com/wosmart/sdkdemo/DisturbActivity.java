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
import com.wosmart.ukprotocollibary.applicationlayer.ApplicationLayerDisturbPacket;

public class DisturbActivity extends BaseActivity implements View.OnClickListener {

    private Toolbar toolbar;

    private Button btn_read;

    private RadioGroup rg_status;

    private EditText et_start_hour;

    private EditText et_start_minute;

    private EditText et_end_hour;

    private EditText et_end_minute;

    private Button btn_set;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disturb);
        initView();
        initData();
        addListener();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        rg_status = findViewById(R.id.rg_status);
        et_start_hour = findViewById(R.id.et_start_hour);
        et_start_minute = findViewById(R.id.et_start_minute);
        et_end_hour = findViewById(R.id.et_end_hour);
        et_end_minute = findViewById(R.id.et_end_minute);
        btn_read = findViewById(R.id.btn_read);
        btn_set = findViewById(R.id.btn_set);
    }

    private void initData() {
        WristbandManager.getInstance(this).registerCallback(new WristbandManagerCallback() {
            @Override
            public void onDisturb(boolean isOpen) {
                super.onDisturb(isOpen);
                showToast("isOpen : " + isOpen);
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
                readDisturb();
                break;
            case R.id.btn_set:
                int checkID = rg_status.getCheckedRadioButtonId();
                boolean flag;
                if (checkID == R.id.rb_open) {
                    flag = true;
                } else {
                    flag = false;
                }
                String startHourStr = et_start_hour.getText().toString();
                String startMinuteStr = et_start_minute.getText().toString();
                String endHourStr = et_end_hour.getText().toString();
                String endMinuteStr = et_end_minute.getText().toString();
                if (null != startHourStr && !startHourStr.isEmpty()) {
                    int startHour = Integer.parseInt(startHourStr);
                    if (null != startMinuteStr && !startMinuteStr.isEmpty()) {
                        int startMinute = Integer.parseInt(startMinuteStr);
                        if (null != endHourStr && !endHourStr.isEmpty()) {
                            int endHour = Integer.parseInt(endHourStr);
                            if (null != endMinuteStr && !endMinuteStr.isEmpty()) {
                                int endMinute = Integer.parseInt(endMinuteStr);
                                ApplicationLayerDisturbPacket disturbData = new ApplicationLayerDisturbPacket();
                                disturbData.setOpen(flag);
                                disturbData.setStartHour(startHour);
                                disturbData.setStartMinute(startMinute);
                                disturbData.setEndHour(endHour);
                                disturbData.setEndMinute(endMinute);
                                setDisturb(disturbData);
                            } else {
                                showToast("请输入结束分钟");
                            }
                        } else {
                            showToast("请输入结束小时");
                        }
                    } else {
                        showToast("请输入开始分钟");
                    }
                } else {
                    showToast("请输入开始小时");
                }

                break;
        }
    }

    private void readDisturb() {
        if (WristbandManager.getInstance(this).sendDisturbSettingReq()) {
            showToast("读取成功");
        } else {
            showToast("读取失败");
        }
    }

    private void setDisturb(ApplicationLayerDisturbPacket disturbPacket) {
        if (WristbandManager.getInstance(this).settingDisturb(disturbPacket)) {
            showToast("设置成功");
        } else {
            showToast("设置失败");
        }
    }


}

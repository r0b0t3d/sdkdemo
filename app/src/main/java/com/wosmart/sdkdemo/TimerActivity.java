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
import com.wosmart.ukprotocollibary.applicationlayer.ApplicationLayerTimerPacket;
import com.wosmart.ukprotocollibary.model.enums.TimerOpt;

public class TimerActivity extends BaseActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private Button btn_read;
    private EditText et_duration;
    private Button btn_set;
    private Button btn_start;
    private Button btn_stop;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        initView();
        initData();
        addListener();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        btn_read = findViewById(R.id.btn_read);
        et_duration = findViewById(R.id.et_duration);
        btn_set = findViewById(R.id.btn_set);
        btn_start = findViewById(R.id.btn_start);
        btn_stop = findViewById(R.id.btn_stop);
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
        btn_read.setOnClickListener(this);
        btn_set.setOnClickListener(this);
        btn_start.setOnClickListener(this);
        btn_stop.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_read:
                readTimer();
                break;
            case R.id.btn_set:
                String durationStr = et_duration.getText().toString();
                if (null != durationStr && !durationStr.isEmpty()) {
                    int duration = Integer.parseInt(durationStr);
                    ApplicationLayerTimerPacket timerInfo = new ApplicationLayerTimerPacket();
                    timerInfo.setOpt(TimerOpt.SETTING);
                    timerInfo.setSeconds(duration);
                    timerInfo.setShow(false);
                    setTimer(timerInfo);
                } else {
                    showToast(getString(R.string.app_timer_hint_duration));
                }
                break;
            case R.id.btn_start:
                ApplicationLayerTimerPacket timerInfo2 = new ApplicationLayerTimerPacket();
                timerInfo2.setOpt(TimerOpt.BEGIN);
                timerInfo2.setSeconds(300);
                timerInfo2.setShow(false);
                setTimer(timerInfo2);
                break;
            case R.id.btn_stop:
                ApplicationLayerTimerPacket timerInfo3 = new ApplicationLayerTimerPacket();
                timerInfo3.setOpt(TimerOpt.END);
                timerInfo3.setSeconds(300);
                timerInfo3.setShow(false);
                setTimer(timerInfo3);
                break;
        }
    }

    private void readTimer() {
        if (WristbandManager.getInstance(this).readTimerInfo()) {
            showToast(getString(R.string.app_success));
        } else {
            showToast(getString(R.string.app_fail));
        }
    }

    private void setTimer(ApplicationLayerTimerPacket timerInfo) {
        if (WristbandManager.getInstance(this).setTimerInfo(timerInfo)) {
            showToast(getString(R.string.app_success));
        } else {
            showToast(getString(R.string.app_fail));
        }
    }
}

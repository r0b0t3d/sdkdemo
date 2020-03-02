package com.wosmart.sdkdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.wosmart.bandlibrary.bluetooth.connect.response.BleWriteResponse;
import com.wosmart.bandlibrary.protocol.WoBtOperationManager;
import com.wosmart.bandlibrary.protocol.listener.SendShortListener;
import com.wosmart.bandlibrary.protocol.listener.TimerListener;
import com.wosmart.bandlibrary.protocol.model.data.TimerInfo;
import com.wosmart.sdkdemo.Common.BaseActivity;

public class TimerActivity extends BaseActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private Button btn_read;
    private RadioGroup rg_status;
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
        rg_status = findViewById(R.id.rg_status);
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
                boolean flag = false;
                if (rg_status.getCheckedRadioButtonId() == R.id.rb_open) {
                    flag = true;
                }
                String durationStr = et_duration.getText().toString();
                if (null != durationStr && !durationStr.isEmpty()) {
                    int duration = Integer.parseInt(durationStr);
                    TimerInfo timerInfo = new TimerInfo();
                    timerInfo.setOpen(flag);
                    timerInfo.setCounting(false);
                    timerInfo.setDuration(duration);
                    setTimer(timerInfo);
                } else {
                    showToast("请输入常用时长");
                }
                break;
            case R.id.btn_start:
                TimerInfo timerInfo2 = new TimerInfo();
                timerInfo2.setOpen(true);
                timerInfo2.setCounting(true);
                timerInfo2.setDuration(30);
                setTimer(timerInfo2);
                break;
            case R.id.btn_stop:
                TimerInfo timerInfo3 = new TimerInfo();
                timerInfo3.setOpen(true);
                timerInfo3.setCounting(false);
                timerInfo3.setDuration(30);
                setTimer(timerInfo3);
                break;
        }
    }

    private void readTimer() {
        WoBtOperationManager.getInstance(this).readTimer(new BleWriteResponse() {
            @Override
            public void onResponse(int code) {

            }
        }, new TimerListener() {
            @Override
            public void onFail() {
                showToast("读取失败");
            }

            @Override
            public void onSuccess(TimerInfo timerInfo) {
                showToast("读取成功 " + timerInfo.toString());
            }
        });
    }

    private void setTimer(TimerInfo timerInfo) {
        WoBtOperationManager.getInstance(this).setTimer(timerInfo, new BleWriteResponse() {
            @Override
            public void onResponse(int code) {

            }
        }, new SendShortListener() {
            @Override
            public void onSendFail() {
                showToast("设置失败");
            }

            @Override
            public void onSendSuccess() {
                showToast("设置成功");
            }
        });
    }
}

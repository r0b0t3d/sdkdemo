package com.wosmart.sdkdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.wosmart.bandlibrary.bluetooth.connect.response.BleWriteResponse;
import com.wosmart.bandlibrary.protocol.WoBtOperationManager;
import com.wosmart.bandlibrary.protocol.listener.HRReminderListener;
import com.wosmart.bandlibrary.protocol.listener.SendShortListener;
import com.wosmart.bandlibrary.protocol.model.data.HrReminder;
import com.wosmart.sdkdemo.Common.BaseActivity;

public class HrReminderActivity extends BaseActivity implements View.OnClickListener {

    private android.support.v7.widget.Toolbar toolbar;
    private Button btn_read;
    private RadioGroup rg_status;
    private EditText et_max;
    private Button btn_set;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hr_reminder);
        initView();
        initData();
        addListener();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        btn_read = findViewById(R.id.btn_read);
        rg_status = findViewById(R.id.rg_status);
        et_max = findViewById(R.id.et_max);
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
        btn_read.setOnClickListener(this);
        btn_set.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_read:
                readHrReminder();
                break;
            case R.id.btn_set:
                boolean flag = rg_status.getCheckedRadioButtonId() == R.id.rb_open;
                String maxStr = et_max.getText().toString();
                if (null != maxStr && !maxStr.isEmpty()) {
                    int maxRate = Integer.parseInt(maxStr);
                    HrReminder hrReminder = new HrReminder();
                    hrReminder.setOpen(flag);
                    hrReminder.setMaxRate(maxRate);
                    setHrReminder(hrReminder);
                } else {
                    showToast("请输入最大心率");
                }
                break;
        }
    }

    private void readHrReminder() {
        WoBtOperationManager.getInstance(this).readHrReminder(new BleWriteResponse() {
            @Override
            public void onResponse(int code) {

            }
        }, new HRReminderListener() {
            @Override
            public void onFail() {
                showToast("读取失败");
            }

            @Override
            public void onSuccess(HrReminder hrReminder) {
                showToast("读取成功" + hrReminder.toString());
            }
        });
    }

    private void setHrReminder(HrReminder hrReminder) {
        WoBtOperationManager.getInstance(this).setHrReminder(hrReminder, new BleWriteResponse() {
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

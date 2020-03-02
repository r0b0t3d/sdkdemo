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

public class LightControlActivity extends BaseActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private Button btn_read_turn_wrist;
    private RadioGroup rg_turn_wrist;
    private Button btn_set_turn_wrist;
    private Button btn_read;
    private EditText et_light_time;
    private Button btn_set;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light_control);
        initView();
        initData();
        addListener();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        btn_read_turn_wrist = findViewById(R.id.btn_read_turn_wrist);
        rg_turn_wrist = findViewById(R.id.rg_turn_wrist);
        btn_set_turn_wrist = findViewById(R.id.btn_set_turn_wrist);
        btn_read = findViewById(R.id.btn_read);
        et_light_time = findViewById(R.id.et_light_time);
        btn_set = findViewById(R.id.btn_set);
    }

    private void initData() {
        WristbandManager.getInstance(this).registerCallback(new WristbandManagerCallback() {
            @Override
            public void onScreenLightDuration(int duration) {
                super.onScreenLightDuration(duration);
                showToast("duration : " + duration);
            }

            @Override
            public void onTurnOverWristSettingReceive(boolean mode) {
                super.onTurnOverWristSettingReceive(mode);
                showToast("turn wrist status : " + mode);
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
        btn_read_turn_wrist.setOnClickListener(this);
        btn_set_turn_wrist.setOnClickListener(this);
        btn_read.setOnClickListener(this);
        btn_set.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_read_turn_wrist:
                readTurnWrist();
                break;
            case R.id.btn_set_turn_wrist:
                boolean flag = rg_turn_wrist.getCheckedRadioButtonId() == R.id.rb_open;
                setTurnWrist(flag);
                break;
            case R.id.btn_read:
                readScreenLight();
                break;
            case R.id.btn_set:
                String durationStr = et_light_time.getText().toString();
                if (null != durationStr && !durationStr.isEmpty()) {
                    int duration = Integer.parseInt(durationStr);
                    setScreenLight(duration);
                } else {
                    showToast("请输入亮屏时长");
                }
                break;
        }
    }

    private void readTurnWrist() {
        if (WristbandManager.getInstance(this).sendTurnOverWristRequest()) {
            showToast("读取成功");
        } else {
            showToast("读取失败");
        }
    }

    private void setTurnWrist(boolean openFlag) {
        if (WristbandManager.getInstance(this).setTurnOverWrist(openFlag)) {
            showToast("设置成功");
        } else {
            showToast("设置失败");
        }
    }

    private void readScreenLight() {
        if (WristbandManager.getInstance(this).sendScreenLightDurationReq()) {
            showToast("读取成功");
        } else {
            showToast("读取失败");
        }
    }


    private void setScreenLight(int duration) {
        if (WristbandManager.getInstance(this).settingScreenLightDuration(duration)) {
            showToast("设置成功");
        } else {
            showToast("设置失败");
        }
    }

}

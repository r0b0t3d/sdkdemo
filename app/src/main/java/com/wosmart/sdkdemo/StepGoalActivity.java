package com.wosmart.sdkdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.wosmart.sdkdemo.Common.BaseActivity;
import com.wosmart.ukprotocollibary.WristbandManager;

public class StepGoalActivity extends BaseActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private EditText et_step_goal;
    private Button btn_set;
    private EditText et_sleep_goal;
    private Button btn_set_sleep;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_goal);
        initView();
        initData();
        addListener();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        et_step_goal = findViewById(R.id.et_step_goal);
        btn_set = findViewById(R.id.btn_set);
        et_sleep_goal = findViewById(R.id.et_sleep_goal);
        btn_set_sleep = findViewById(R.id.btn_set_sleep);
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
        btn_set_sleep.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_set:
                String goalStr = et_step_goal.getText().toString();
                if (null != goalStr) {
                    int goal = Integer.parseInt(goalStr);
                    setStepGoal(goal);
                }
                break;
            case R.id.btn_set_sleep:
                String goalStr2 = et_sleep_goal.getText().toString();
                if (null != goalStr2) {
                    int goal2 = Integer.parseInt(goalStr2);
                    setSleepGoal(goal2);
                }
                break;
        }
    }

    private void setStepGoal(int goal) {
        if (WristbandManager.getInstance(this).setTargetStep(goal)) {
            showToast("设置成功");
        } else {
            showToast("设置失败");
        }
    }

    private void setSleepGoal(int goal) {
        if (WristbandManager.getInstance(this).setTargetSleep(goal)) {
            showToast("设置成功");
        } else {
            showToast("设置失败");
        }
    }
}

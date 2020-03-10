package com.wosmart.sdkdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.wosmart.sdkdemo.Common.BaseActivity;
import com.wosmart.ukprotocollibary.WristbandManager;
import com.wosmart.ukprotocollibary.WristbandManagerCallback;
import com.wosmart.ukprotocollibary.model.db.GlobalGreenDAO;
import com.wosmart.ukprotocollibary.model.sleep.SleepData;
import com.wosmart.ukprotocollibary.model.sport.SportData;

import java.util.Calendar;
import java.util.List;

public class StepActivity extends BaseActivity implements View.OnClickListener {
    private String tag = "StepActivity";

    private Toolbar toolbar;

    private Button btn_sync_current;

    private Button btn_read_history;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);
        initView();
        initData();
        addListener();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        btn_sync_current = findViewById(R.id.btn_sync_current);
        btn_read_history = findViewById(R.id.btn_read_history);
    }

    private void initData() {
        WristbandManager.getInstance(this).registerCallback(new WristbandManagerCallback() {

        });
    }

    private void addListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_sync_current.setOnClickListener(this);
        btn_read_history.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sync_current:
                syncCurrent();
                break;
            case R.id.btn_read_history:
                readStepLocal();
                break;
        }
    }

    private void syncCurrent() {
        if (WristbandManager.getInstance(this).sendSyncTodayStepCommand()) {
            showToast(getString(R.string.app_success));
        } else {
            showToast(getString(R.string.app_fail));
        }
    }

    private void readStepLocal() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        List<SportData> steps = GlobalGreenDAO.getInstance().loadSportDataByDate(year, month, day);

        if (null != steps) {
            for (SportData item : steps) {
                Log.i(tag, "item = " + item.toString());
            }
        }
    }
}

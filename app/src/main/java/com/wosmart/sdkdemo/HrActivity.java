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
import com.wosmart.ukprotocollibary.applicationlayer.ApplicationLayerHrpItemPacket;
import com.wosmart.ukprotocollibary.applicationlayer.ApplicationLayerHrpPacket;
import com.wosmart.ukprotocollibary.model.db.GlobalGreenDAO;
import com.wosmart.ukprotocollibary.model.hrp.HrpData;
import com.wosmart.ukprotocollibary.model.sleep.SleepData;

import java.util.Calendar;
import java.util.List;

public class HrActivity extends BaseActivity implements View.OnClickListener {

    private String tag = "HrActivity";

    private Toolbar toolbar;

    private Button btn_start_measure;

    private Button btn_stop_measure;

    private Button btn_read_history;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);
        initView();
        initData();
        addListener();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        btn_start_measure = findViewById(R.id.btn_start_measure);
        btn_stop_measure = findViewById(R.id.btn_stop_measure);
        btn_read_history = findViewById(R.id.btn_read_history);
    }

    private void initData() {
        WristbandManager.getInstance(this).registerCallback(new WristbandManagerCallback() {
            @Override
            public void onHrpDataReceiveIndication(ApplicationLayerHrpPacket packet) {
                super.onHrpDataReceiveIndication(packet);
                for (ApplicationLayerHrpItemPacket item : packet.getHrpItems()) {
                    Log.i(tag, "hr value :" + item.getValue());
                }
            }

            @Override
            public void onDeviceCancelSingleHrpRead() {
                super.onDeviceCancelSingleHrpRead();
                Log.i(tag, "stop measure hr ");
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
        btn_start_measure.setOnClickListener(this);
        btn_stop_measure.setOnClickListener(this);
        btn_read_history.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start_measure:
                startMeasure();
                break;
            case R.id.btn_stop_measure:
                stopMeasure();
                break;
            case R.id.btn_read_history:
                loadLocal();
                break;
        }
    }

    private void startMeasure() {
        if (WristbandManager.getInstance(this).readHrpValue()) {
            showToast(getString(R.string.app_success));
        } else {
            showToast(getString(R.string.app_fail));
        }
    }

    private void stopMeasure() {
        if (WristbandManager.getInstance(this).stopReadHrpValue()) {
            showToast(getString(R.string.app_success));
        } else {
            showToast(getString(R.string.app_fail));
        }
    }

    private void loadLocal() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        List<HrpData> hrs = GlobalGreenDAO.getInstance().loadHrpDataByDate(year, month, day);

        if (null != hrs) {
            for (HrpData item : hrs) {
                Log.i(tag, "item = " + item.toString());
            }
        }
    }
}

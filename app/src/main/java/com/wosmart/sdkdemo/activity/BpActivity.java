package com.wosmart.sdkdemo.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.wosmart.sdkdemo.common.BaseActivity;
import com.wosmart.sdkdemo.R;
import com.wosmart.ukprotocollibary.WristbandManager;
import com.wosmart.ukprotocollibary.WristbandManagerCallback;
import com.wosmart.ukprotocollibary.applicationlayer.ApplicationLayerBpItemPacket;
import com.wosmart.ukprotocollibary.applicationlayer.ApplicationLayerBpPacket;

public class BpActivity extends BaseActivity implements View.OnClickListener {

    private String tag = "BpActivity";

    private Toolbar toolbar;

    private Button btn_start_measure;

    private Button btn_stop_measure;

    private Handler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bp);
        initView();
        initData();
        addListener();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        btn_start_measure = findViewById(R.id.btn_start_measure);
        btn_stop_measure = findViewById(R.id.btn_stop_measure);
    }

    private void initData() {
        handler = new MyHandler();
        WristbandManager.getInstance(this).registerCallback(new WristbandManagerCallback() {
            @Override
            public void onBpDataReceiveIndication(ApplicationLayerBpPacket packet) {
                super.onBpDataReceiveIndication(packet);
                for (ApplicationLayerBpItemPacket item : packet.getBpItems()) {
                    Log.i(tag, "bp high :" + item.getmHighValue() + " low : " + item.getmLowValue());
                }
            }

            @Override
            public void onDeviceCancelSingleBpRead() {
                super.onDeviceCancelSingleBpRead();
                Log.i(tag, "stop measure bp ");
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
        }
    }

    private void startMeasure() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (WristbandManager.getInstance(BpActivity.this).readBpValue()) {
                    handler.sendEmptyMessage(0x01);
                } else {
                    handler.sendEmptyMessage(0x02);
                }
            }
        });
        thread.start();
    }

    private void stopMeasure() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (WristbandManager.getInstance(BpActivity.this).stopReadBpValue()) {
                    handler.sendEmptyMessage(0x01);
                } else {
                    handler.sendEmptyMessage(0x02);
                }
            }
        });
        thread.start();
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0x01:
                    showToast(getString(R.string.app_success));
                    break;
                case 0x02:
                    showToast(getString(R.string.app_fail));
                    break;
            }
        }
    }
}

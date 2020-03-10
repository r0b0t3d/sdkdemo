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
import com.wosmart.ukprotocollibary.applicationlayer.ApplicationLayerRateItemPacket;
import com.wosmart.ukprotocollibary.applicationlayer.ApplicationLayerRateListPacket;
import com.wosmart.ukprotocollibary.applicationlayer.ApplicationLayerSleepItemPacket;
import com.wosmart.ukprotocollibary.applicationlayer.ApplicationLayerSleepPacket;
import com.wosmart.ukprotocollibary.applicationlayer.ApplicationLayerSportItemPacket;
import com.wosmart.ukprotocollibary.applicationlayer.ApplicationLayerSportPacket;
import com.wosmart.ukprotocollibary.applicationlayer.ApplicationLayerStepItemPacket;
import com.wosmart.ukprotocollibary.applicationlayer.ApplicationLayerStepPacket;
import com.wosmart.ukprotocollibary.applicationlayer.ApplicationLayerTodaySumSportPacket;

public class SyncDataActivity extends BaseActivity implements View.OnClickListener {

    private String tag = "SyncDataActivity";

    private Toolbar toolbar;

    private Button btn_sync_data;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_data);
        initView();
        initData();
        addListener();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        btn_sync_data = findViewById(R.id.btn_sync_data);
    }

    private void initData() {
        WristbandManager.getInstance(this).registerCallback(new WristbandManagerCallback() {

            @Override
            public void onSyncDataBegin() {
                super.onSyncDataBegin();
                Log.i(tag, "sync begin");
            }

            @Override
            public void onStepDataReceiveIndication(ApplicationLayerStepPacket packet) {
                super.onStepDataReceiveIndication(packet);
                for (ApplicationLayerStepItemPacket item : packet.getStepsItems()) {
                    Log.i(tag, item.toString());
                }
                Log.i(tag, "size = " + packet.getStepsItems().size());
            }

            @Override
            public void onSleepDataReceiveIndication(ApplicationLayerSleepPacket packet) {
                super.onSleepDataReceiveIndication(packet);
                for (ApplicationLayerSleepItemPacket item : packet.getSleepItems()) {
                    Log.i(tag, item.toString());
                }
                Log.i(tag, "size = " + packet.getSleepItems().size());
            }

            @Override
            public void onHrpDataReceiveIndication(ApplicationLayerHrpPacket packet) {
                super.onHrpDataReceiveIndication(packet);
                for (ApplicationLayerHrpItemPacket item : packet.getHrpItems()) {
                    Log.i(tag, item.toString());
                }
                Log.i(tag, "size = " + packet.getHrpItems().size());
            }

            @Override
            public void onRateList(ApplicationLayerRateListPacket packet) {
                super.onRateList(packet);
                for (ApplicationLayerRateItemPacket item : packet.getRateList()) {
                    Log.i(tag, item.toString());
                }
                Log.i(tag, "size = " + packet.getRateList().size());
            }

            @Override
            public void onSportDataReceiveIndication(ApplicationLayerSportPacket packet) {
                super.onSportDataReceiveIndication(packet);
                for (ApplicationLayerSportItemPacket item : packet.getSportItems()) {
                    Log.i(tag, item.toString());
                }
                Log.i(tag, "size = " + packet.getSportItems().size());
            }

            @Override
            public void onSyncDataEnd(ApplicationLayerTodaySumSportPacket packet) {
                super.onSyncDataEnd(packet);
                Log.i(tag, "sync end");
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
        btn_sync_data.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sync_data:
                syncData();
                break;
        }
    }

    private void syncData() {
        if (WristbandManager.getInstance(this).sendDataRequest()) {
            showToast(getString(R.string.app_success));
        } else {
            showToast(getString(R.string.app_fail));
        }
    }
}

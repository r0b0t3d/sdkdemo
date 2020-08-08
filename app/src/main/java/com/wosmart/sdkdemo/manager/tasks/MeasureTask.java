package com.wosmart.sdkdemo.manager.tasks;

import android.util.Log;

import com.wosmart.ukprotocollibary.WristbandManager;
import com.wosmart.ukprotocollibary.WristbandManagerCallback;
import com.wosmart.ukprotocollibary.applicationlayer.ApplicationLayerHrpItemPacket;
import com.wosmart.ukprotocollibary.applicationlayer.ApplicationLayerHrpPacket;
import com.wosmart.ukprotocollibary.applicationlayer.ApplicationLayerTemperatureControlPacket;

public class MeasureTask extends CommonTask {
    private static final String TAG = "MeasureTask";

    public MeasureTask(WristbandManager wristbandManager, Callback callback) {
        super(wristbandManager, callback);
    }

    @Override
    WristbandManagerCallback initWristbandManagerCallback() {
        return new WristbandManagerCallback() {
            @Override
            public void onHrpDataReceiveIndication(ApplicationLayerHrpPacket packet) {
                super.onHrpDataReceiveIndication(packet);
                for (ApplicationLayerHrpItemPacket item : packet.getHrpItems()) {
                    Log.e(TAG, "hr value :" + item.getValue());
                }
            }

            @Override
            public void onDeviceCancelSingleHrpRead() {
                super.onDeviceCancelSingleHrpRead();
                Log.e(TAG, "stop measure hr ");
            }

            @Override
            public void onTemperatureData(ApplicationLayerHrpPacket packet) {
                super.onTemperatureData(packet);
                for (ApplicationLayerHrpItemPacket item : packet.getHrpItems()) {
                    Log.e(TAG, "temp origin value :" + item.getTempOriginValue() + " temperature adjust value : " + item.getTemperature() + " is wear :" + item.isWearStatus() + " is adjust : " + item.isAdjustStatus() + "is animation :" + item.isAnimationStatus());
                }
            }

            @Override
            public void onTemperatureMeasureSetting(ApplicationLayerTemperatureControlPacket packet) {
                super.onTemperatureMeasureSetting(packet);
                Log.e(TAG, "temp setting : show = " + packet.isShow() + " adjust = " + packet.isAdjust() + " celsius unit = " + packet.isCelsiusUnit());
            }

            @Override
            public void onTemperatureMeasureStatus(int status) {
                super.onTemperatureMeasureStatus(status);
                Log.e(TAG, "temp status :" + status);
            }
        };
    }

    @Override
    public void run() {
        super.run();

        ApplicationLayerTemperatureControlPacket packet = new ApplicationLayerTemperatureControlPacket();
        packet.setShow(true);
        if (wristbandManager.setTemperatureControl(packet)) {
            Log.e(TAG, "setTempSetting SUCCESS");
        } else {
            Log.e(TAG, "setTempSetting FAIL");
        }

        if (wristbandManager.readHrpValue()) {
            Log.e(TAG, "startMeasure SUCCESS");
        } else {
            Log.e(TAG, "startMeasure FAIL");
        }

        if (wristbandManager.setTemperatureStatus(true)) {
            Log.e(TAG, "startMeasureTemp SUCCESS");
        } else {
            Log.e(TAG, "startMeasureTemp FAIL");
        }
    }
}

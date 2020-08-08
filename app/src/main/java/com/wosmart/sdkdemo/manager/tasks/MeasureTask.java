package com.wosmart.sdkdemo.manager.tasks;

import android.util.Log;

import com.wosmart.sdkdemo.models.ZoneReport;
import com.wosmart.ukprotocollibary.WristbandManager;
import com.wosmart.ukprotocollibary.WristbandManagerCallback;
import com.wosmart.ukprotocollibary.applicationlayer.ApplicationLayerHrpItemPacket;
import com.wosmart.ukprotocollibary.applicationlayer.ApplicationLayerHrpPacket;
import com.wosmart.ukprotocollibary.applicationlayer.ApplicationLayerTemperatureControlPacket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MeasureTask extends CommonTask {
    private static final String TAG = "MeasureTask";
    private final String mac;
    private List<Integer> hrValues;
    private float tempValue = 0f;
    private boolean isDataGathered = false;

    public MeasureTask(WristbandManager wristbandManager, Callback callback, String mac) {
        super(wristbandManager, callback);
        this.mac = mac;
        hrValues = new ArrayList<>();
    }

    private void onHrUpdate(int hr) {
        if (hr > 0) {
            hrValues.add(hr);
            if (tempValue > 0) {
                isDataGathered = true;
            }
        }
    }

    private void onTempUpdate(float temp) {
        if (temp > 0) {
            this.tempValue = temp;
            if (this.hrValues.size() > 0) {
                isDataGathered = true;
            }
        }
    }

    @Override
    WristbandManagerCallback initWristbandManagerCallback() {
        return new WristbandManagerCallback() {
            @Override
            public void onHrpDataReceiveIndication(ApplicationLayerHrpPacket packet) {
                super.onHrpDataReceiveIndication(packet);
                for (ApplicationLayerHrpItemPacket item : packet.getHrpItems()) {
                    Log.e(TAG, "hr value :" + item.getValue());
                    onHrUpdate(item.getValue());
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
                    onTempUpdate(item.getTempOriginValue());
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

        while (true) {
            try {
                sleep(1000);
                if (isDataGathered) {
                    stopMeasure();
                    ZoneReport data = new ZoneReport();
                    data.Heartrate_arr = hrValues;
                    data.Temperature = tempValue;
                    data.BandID = mac;
                    uploadData(data);
                    onSuccess();
                    break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    private void stopMeasure() {
        if (wristbandManager.stopReadHrpValue()) {
            Log.e(TAG, "stopMeasure SUCCESS");
        } else {
            Log.e(TAG, "stopMeasure FAIL");
        }

        if (wristbandManager.setTemperatureStatus(false)) {
            Log.e(TAG, "stopMeasureTemp SUCCESS");
        } else {
            Log.e(TAG, "stopMeasureTemp FAIL");
        }
    }

    private void uploadData(ZoneReport data) {
        try {
            URL url = new URL("http://41.79.79.221/zonereport");
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);
            String jsonBody = data.toJSON();
            try(OutputStream os = con.getOutputStream()) {
                byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            try(BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                Log.e(TAG, "Upload zone report " + response.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
    }
}

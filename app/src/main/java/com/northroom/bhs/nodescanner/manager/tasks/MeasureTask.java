package com.northroom.bhs.nodescanner.manager.tasks;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.northroom.bhs.nodescanner.models.ZoneReport;
import com.wosmart.ukprotocollibary.WristbandManager;
import com.wosmart.ukprotocollibary.WristbandManagerCallback;
import com.wosmart.ukprotocollibary.applicationlayer.ApplicationLayerHrpItemPacket;
import com.wosmart.ukprotocollibary.applicationlayer.ApplicationLayerHrpPacket;
import com.wosmart.ukprotocollibary.applicationlayer.ApplicationLayerTemperatureControlPacket;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MeasureTask extends CommonTask {
    private static final String TAG = "MeasureTask";
    private final String mac;
    private final Context context;
    private List<Integer> hrValues;
    private float tempValue = 0f;
    private boolean isDataGathered = false;
    private int waitingCount = 0;

    public MeasureTask(Context context, WristbandManager wristbandManager, Callback callback, String mac) {
        super(wristbandManager, callback);
        this.context = context;
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
        packet.setCelsiusUnit(true);
        packet.setAdjust(true);
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
                waitingCount += 1;
                Log.e(TAG, "Measure task is running " + waitingCount);
                if (waitingCount > 10 && !isDataGathered) {
                    onFailed();
                    break;
                }
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

    private void uploadData(final ZoneReport data) {
        String url = "http://41.79.79.221/zonereport";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, data.toJSON(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, "Response:  " + response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.getMessage());
            }
        });
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);
    }
}

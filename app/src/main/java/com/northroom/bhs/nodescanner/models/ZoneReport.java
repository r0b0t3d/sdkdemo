package com.northroom.bhs.nodescanner.models;

import com.northroom.bhs.nodescanner.Configs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ZoneReport {
    public String zoneName = "test";
    public String zoneID = Configs.ZONE_ID;
    public String bandID;
    public List<Integer> heartRateArr;
    public float temperature;
    public long timestamp;
    public float deviceBattery;

    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("zoneName", zoneName);
            jsonObject.put("zoneID", zoneID);
            jsonObject.put("BandID", bandID);
            JSONArray hrJsonArray = new JSONArray();
            for (Integer hr : heartRateArr) {
                hrJsonArray.put(hr);
            }
            jsonObject.put("Heartrate_arr", hrJsonArray);
            jsonObject.put("Temperature", temperature);
            jsonObject.put("timestamp", timestamp);
            jsonObject.put("deviceBattery", deviceBattery);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}

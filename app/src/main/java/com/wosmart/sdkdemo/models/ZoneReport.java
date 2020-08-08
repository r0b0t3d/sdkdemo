package com.wosmart.sdkdemo.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ZoneReport {
    public String zoneName = "test";
    public String zoneID = "test";
    public String BandID;
    public List<Integer> Heartrate_arr;
    public float Temperature;

    public String toJSON() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("zoneName", zoneName);
            jsonObject.put("zoneID", zoneID);
            jsonObject.put("BandID", BandID);
            jsonObject.put("Heartrate_arr", Heartrate_arr);
            jsonObject.put("Temperature", Temperature);
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }
}

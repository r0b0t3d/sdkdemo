package com.northroom.bhs.nodescanner.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ZoneReport {
    public String zoneName = "test";
    public String zoneID = "test";
    public String BandID;
    public List<Integer> Heartrate_arr;
    public float Temperature;

    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("zoneName", zoneName);
            jsonObject.put("zoneID", zoneID);
            jsonObject.put("BandID", BandID);
            JSONArray hrJsonArray = new JSONArray();
            for (Integer hr : Heartrate_arr) {
                hrJsonArray.put(hr);
            }
            jsonObject.put("Heartrate_arr", hrJsonArray);
            jsonObject.put("Temperature", Temperature);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}

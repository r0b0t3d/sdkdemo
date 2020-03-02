package com.wosmart.ukprotocollibary.model.sleep;

public class SleepSubData {
    private int deepSleepTime;//by minutes
    private int lightSleepTime;//by minutes
    private int awakeTimes;//by minutes

    public SleepSubData() {
        deepSleepTime = 0;
        lightSleepTime = 0;
        awakeTimes = 0;
    }

    public void add(SleepSubData ssd) {
        deepSleepTime += ssd.getDeepSleepTime();
        lightSleepTime += ssd.getLightSleepTime();
        awakeTimes += ssd.getAwakeTimes();
    }

    public int getDeepSleepTime() {
        return deepSleepTime;
    }


    public void setDeepSleepTime(int deepSleepTime) {
        this.deepSleepTime = deepSleepTime;
    }


    public int getLightSleepTime() {
        return lightSleepTime;
    }


    public void setLightSleepTime(int lightSleepTime) {
        this.lightSleepTime = lightSleepTime;
    }


    public int getAwakeTimes() {
        return awakeTimes;
    }


    public void setAwakeTimes(int awakeTimes) {
        this.awakeTimes = awakeTimes;
    }

    public int getTotalSleepTime() {
        return lightSleepTime + deepSleepTime;
    }

    public String toString() {
        return "deepSleepTime: " + deepSleepTime
                + ", lightSleepTime: " + lightSleepTime
                + ", awakeTimes: " + awakeTimes;
    }
}
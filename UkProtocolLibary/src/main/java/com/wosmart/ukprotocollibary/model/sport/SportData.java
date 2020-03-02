package com.wosmart.ukprotocollibary.model.sport;


public class SportData {

    private Long id;
    private int year;
    private int month;
    private int day;
    private int offset;
    private int mode;
    private int stepCount;
    private int activeTime;
    private int calory;
    private int distance;
    private java.util.Date date;

    public SportData() {
    }

    public SportData(Long id) {
        this.id = id;
    }

    public SportData(Long id, int year, int month, int day, int offset, int mode, int stepCount, int activeTime, int calory, int distance, java.util.Date date) {
        this.id = id;
        this.year = year;
        this.month = month;
        this.day = day;
        this.offset = offset;
        this.mode = mode;
        this.stepCount = stepCount;
        this.activeTime = activeTime;
        this.calory = calory;
        this.distance = distance;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getStepCount() {
        return stepCount;
    }

    public void setStepCount(int stepCount) {
        this.stepCount = stepCount;
    }

    public int getActiveTime() {
        return activeTime;
    }

    public void setActiveTime(int activeTime) {
        this.activeTime = activeTime;
    }

    public int getCalory() {
        return calory;
    }

    public void setCalory(int calory) {
        this.calory = calory;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public java.util.Date getDate() {
        return date;
    }

    public void setDate(java.util.Date date) {
        this.date = date;
    }

}

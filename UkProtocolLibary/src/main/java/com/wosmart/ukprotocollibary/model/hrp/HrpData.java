package com.wosmart.ukprotocollibary.model.hrp;

public class HrpData {

    private Long id;
    private int year;
    private int month;
    private int day;
    private int minutes;
    private int value;
    private long dateStampByRealSample;
    private java.util.Date date;

    public HrpData() {
    }

    public HrpData(Long id) {
        this.id = id;
    }

    public HrpData(Long id, int year, int month, int day, int minutes, int value, long dateStampByRealSample, java.util.Date date) {
        this.id = id;
        this.year = year;
        this.month = month;
        this.day = day;
        this.minutes = minutes;
        this.value = value;
        this.dateStampByRealSample = dateStampByRealSample;
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

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public long getDateStampByRealSample() {
        return dateStampByRealSample;
    }

    public void setDateStampByRealSample(long dateStampByRealSample) {
        this.dateStampByRealSample = dateStampByRealSample;
    }

    public java.util.Date getDate() {
        return date;
    }

    public void setDate(java.util.Date date) {
        this.date = date;
    }

}

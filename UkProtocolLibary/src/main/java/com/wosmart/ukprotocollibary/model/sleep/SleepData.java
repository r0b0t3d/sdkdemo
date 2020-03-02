package com.wosmart.ukprotocollibary.model.sleep;

public class SleepData {

    private Long id;
    private int year;
    private int month;
    private int day;
    private int minutes;
    private int mode;
    private java.util.Date date;

    public SleepData() {
    }

    public SleepData(Long id) {
        this.id = id;
    }

    public SleepData(Long id, int year, int month, int day, int minutes, int mode, java.util.Date date) {
        this.id = id;
        this.year = year;
        this.month = month;
        this.day = day;
        this.minutes = minutes;
        this.mode = mode;
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

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public java.util.Date getDate() {
        return date;
    }

    public void setDate(java.util.Date date) {
        this.date = date;
    }

}

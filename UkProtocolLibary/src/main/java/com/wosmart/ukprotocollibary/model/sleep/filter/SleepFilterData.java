package com.wosmart.ukprotocollibary.model.sleep.filter;

import com.wosmart.ukprotocollibary.applicationlayer.ApplicationLayer;

import java.util.Comparator;
import java.util.Date;

public class SleepFilterData {
    private Long id;
    private int year;
    private int month;
    private int day;
    private int minutes;
    private int mode;
    private Date date;
    private int minutesAxes;

    public SleepFilterData(Long id, int year, int month, int day, int minutes, int mode, Date date) {
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getMinutesAxes() {
        return minutesAxes;
    }

    public void setMinutesAxes(int minutesAxes) {
        this.minutesAxes = minutesAxes;
    }

    @Override
    public String toString() {
//        SimpleDateFormat shortDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String modeString = "";
        switch (mode) {
            case ApplicationLayer.SLEEP_MODE_START_SLEEP:
                modeString = "Start Sleep(0x01)";
                break;
            case ApplicationLayer.SLEEP_MODE_START_DEEP_SLEEP:
                modeString = "Start Deep Sleep(0x02)";
                break;
            case ApplicationLayer.SLEEP_MODE_START_WAKE:
                modeString = "Start Wake(0x03)";
                break;
            default:
                modeString = "Error mode(" + String.valueOf(mode) + ")";
                break;
        }
        int startHour = minutes / 60;
        int startMinute = minutes % 60;
        return String.format("%d/%d/%d %02d:%02d(%d %.2f), %s, minutesAxes=%d",
                year, month, day, startHour % 24, startMinute,
                minutes, minutes / 60f, modeString, minutesAxes);

//        return super.toString();
    }

    /**
     * Sleep data list Increase Comparator class, sort by the minutes.
     */
    public static class IncreaseComparator implements Comparator {

        public int compare(Object arg0, Object arg1) {
            return compareSleep((SleepFilterData) arg0, (SleepFilterData) arg1);
        }

        public int compareSleep(SleepFilterData o1, SleepFilterData o2) {
            if (o1.getMinutes() > o2.getMinutes()) {
                return 1;
            } else if (o1.getMinutes() < o2.getMinutes()) {
                return -1;
            } else {
                return 0;
            }
        }
    }

}
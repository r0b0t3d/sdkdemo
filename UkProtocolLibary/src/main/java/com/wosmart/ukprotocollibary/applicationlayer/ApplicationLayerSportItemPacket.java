package com.wosmart.ukprotocollibary.applicationlayer;

import com.realsil.realteksdk.logger.ZLogger;

public class ApplicationLayerSportItemPacket {
    // Parameters
    private int minutes;            // 11bits
    private int seconds;                // 2bits
    private int sportModel;            // 12bits
    private int sportHour;        // 4bits
    private int sportMinute;            // 19bits
    private int sportSecond;            // 16bits
    private int steps;
    private int distance;
    private int calories;

    // Packet Length
    public final static int SPORT_ITEM_LENGTH = 20;

    public boolean parseData(byte[] data) {
        // check header length
        if (data.length < SPORT_ITEM_LENGTH) {
            return false;
        }
        minutes = (data[1] << 8) & 0xff + data[2] & 0xff;
        seconds = data[3] & 0xff;
        sportModel = data[4] & 0xff;
        sportHour = data[5] & 0xff;
        sportMinute = data[6] & 0xff;
        sportSecond = data[7] & 0xff;
        steps = (data[8] << 24) & 0xff + (data[9] << 16) & 0xff + (data[10] << 8) & 0xff + (data[11] & 0xff);
        distance = (data[12] << 24) & 0xff + (data[13] << 16) & 0xff + (data[14] << 8) & 0xff + (data[15] & 0xff);
        calories = (data[16] << 24) & 0xff + (data[17] << 16) & 0xff + (data[18] << 8) & 0xff + (data[19] & 0xff);
        ZLogger.i("minutes: " + minutes +
                ", seconds:" + seconds +
                ", sportModel:" + sportModel +
                ", sportHour:" + sportHour +
                ", sportMinute:" + sportMinute +
                ", sportSecond:" + sportSecond +
                ", steps:" + steps +
                ", sportSecond:" + sportSecond +
                ", sportSecond:" + sportSecond);
        return true;
    }

    public int getMinutes() {
        return minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    public int getSportModel() {
        return sportModel;
    }

    public int getSportHour() {
        return sportHour;
    }

    public int getSportMinute() {
        return sportMinute;
    }

    public int getSportSecond() {
        return sportSecond;
    }

    public int getSteps() {
        return steps;
    }

    public int getDistance() {
        return distance;
    }

    public int getCalories() {
        return calories;
    }
}

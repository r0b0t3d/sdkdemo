package com.wosmart.blehub.dfu;

import com.realsil.realteksdk.logger.ZLogger;

public class SpeedControl {
    // Debug
    private static final String TAG = "SpeedControl";
    private boolean D = true;

    // Speed Control
    private int scTotalSpeed;//5KByte/s
    private volatile long lastSpeedControlStartTime = -1;
    private int scPacketSize;
    private int mTimeDelta = -1;
    private boolean isSpeedControlOn = false;

    SpeedControl(int packetsize, int speed, boolean en) {
        isSpeedControlOn = en;
        scPacketSize = packetsize;
        scTotalSpeed = speed;
        mTimeDelta = (int) (((float) (scPacketSize * 1000)) / ((float) scTotalSpeed) * 1000);
        ZLogger.i(D, "time delta is: " + mTimeDelta);

    }

    public void SetSpeedControlMode(boolean en) {
        isSpeedControlOn = en;
    }

    public void SetTotalSpeed(int speed) {
        if (!isSpeedControlOn) {
            return;
        }
        if (speed != scTotalSpeed) {
            scTotalSpeed = speed;
            mTimeDelta = (int) (((float) (scPacketSize * 1000)) / ((float) scTotalSpeed) * 1000);
            ZLogger.i(D, "time delta is: " + mTimeDelta);
        } else {
            ZLogger.w(D, "speed didn't change");
        }
    }

    public void SetPacketSize(int size) {
        if (!isSpeedControlOn) {
            return;
        }
        if (size != scPacketSize) {
            scPacketSize = size;
            mTimeDelta = (int) (((float) (scPacketSize * 1000)) / ((float) scTotalSpeed) * 1000);
            ZLogger.i(D, "time delta is: " + mTimeDelta);
        } else {
            ZLogger.w(D, "packet size didn't change");
        }
    }

    public int GetTotalSpeed() {
        return scTotalSpeed;
    }

    public void StartSpeedControl() {
        if (!isSpeedControlOn) {
            return;
        }
        // speed control, clear the flag, the speed control thread will wait for a while
        lastSpeedControlStartTime = System.nanoTime();

        ZLogger.d(D, "start speed control");
    }

    public void WaitSpeedControl() {
        if (!isSpeedControlOn) {
            return;
        }
        if (lastSpeedControlStartTime == -1 || mTimeDelta == -1) {
            ZLogger.e(D, "stop speed control with error, must initial first");
            return;
        }
        // speed control, wait for sc timer fire
        // start the speed control timer, the delta accuracy is "us"
        while (((System.nanoTime() - lastSpeedControlStartTime) / 1000) < mTimeDelta) ;
        ZLogger.d(D, "stop speed control");

    }

}

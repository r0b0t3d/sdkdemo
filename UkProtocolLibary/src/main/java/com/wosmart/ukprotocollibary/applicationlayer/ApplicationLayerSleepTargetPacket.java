package com.wosmart.ukprotocollibary.applicationlayer;

public class ApplicationLayerSleepTargetPacket {
    // Parameters
    private int mSleepTarget;            // 2byte

    // Packet Length
    private final static int SLEEP_HEADER_LENGTH = 2;

    public ApplicationLayerSleepTargetPacket() {
    }

    public ApplicationLayerSleepTargetPacket(int mSleepTarget) {
        this.mSleepTarget = mSleepTarget;
    }

    public int getmSleepTarget() {
        return mSleepTarget;
    }

    public void setmSleepTarget(int mSleepTarget) {
        this.mSleepTarget = mSleepTarget;
    }

    public byte[] getPacket() {
        byte[] data = new byte[SLEEP_HEADER_LENGTH];
        data[0] = (byte) ((mSleepTarget >> 8) & 0xff);
        data[1] = (byte) (mSleepTarget & 0xff);
        return data;
    }
}

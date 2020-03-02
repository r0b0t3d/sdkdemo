package com.wosmart.ukprotocollibary.applicationlayer;

public class ApplicationLayerSitPacket {
    // Parameters
    private byte mEnable;            // 1byte
    private int mThreshold;            // 2bytes
    private int mNotifyTime;        // 1byte
    private int mStartNotifyTime;    // 1byte
    private int mStopNotifyTime;    // 1byte
    private byte mDayFlags;            // 1byte

    // Day Flags
    public final static byte LONG_SIT_CONTROL_ENABLE = 0x01;
    public final static byte LONG_SIT_CONTROL_DISABLE = 0x00;

    // Day Flags
    public final static byte REPETITION_NULL = 0x00;
    public final static byte REPETITION_MON = 0x01;
    public final static byte REPETITION_TUES = 0x02;
    public final static byte REPETITION_WED = 0x04;
    public final static byte REPETITION_THU = 0x08;
    public final static byte REPETITION_FRI = 0x10;
    public final static byte REPETITION_SAT = 0x20;
    public final static byte REPETITION_SUN = 0x40;
    public final static byte REPETITION_ALL = (byte) 0xFF;


    // Packet Length
    private final static int SIT_HEADER_LENGTH = 8;

    public ApplicationLayerSitPacket() {
    }

    public ApplicationLayerSitPacket(byte enable, int threshold, int notify, int start, int stop, byte dayflags) {
        mEnable = enable;
        mThreshold = threshold;
        mNotifyTime = notify;
        mStartNotifyTime = start;
        mStopNotifyTime = stop;
        mDayFlags = dayflags;
    }

    public byte getmEnable() {
        return mEnable;
    }

    public void setmEnable(byte mEnable) {
        this.mEnable = mEnable;
    }

    public int getmThreshold() {
        return mThreshold;
    }

    public void setmThreshold(int mThreshold) {
        this.mThreshold = mThreshold;
    }

    public int getmNotifyTime() {
        return mNotifyTime;
    }

    public void setmNotifyTime(int mNotifyTime) {
        this.mNotifyTime = mNotifyTime;
    }

    public int getmStartNotifyTime() {
        return mStartNotifyTime;
    }

    public void setmStartNotifyTime(int mStartNotifyTime) {
        this.mStartNotifyTime = mStartNotifyTime;
    }

    public int getmStopNotifyTime() {
        return mStopNotifyTime;
    }

    public void setmStopNotifyTime(int mStopNotifyTime) {
        this.mStopNotifyTime = mStopNotifyTime;
    }

    public byte getmDayFlags() {
        return mDayFlags;
    }

    public void setmDayFlags(byte mDayFlags) {
        this.mDayFlags = mDayFlags;
    }

    public byte[] getPacket() {
        byte[] data = new byte[SIT_HEADER_LENGTH];
        data[0] = 0x00;
        data[1] = mEnable;
        data[2] = (byte) ((mThreshold >> 8) & 0xff);
        data[3] = (byte) (mThreshold & 0xff);
        data[4] = (byte) (mNotifyTime & 0xff);
        data[5] = (byte) (mStartNotifyTime & 0xff);
        data[6] = (byte) (mStopNotifyTime & 0xff);
        data[7] = mDayFlags;
        return data;
    }

    public boolean parseData(byte[] data) {
        if (data.length >= SIT_HEADER_LENGTH) {
            mEnable = data[1];
            mThreshold = ((data[2] << 8) & 0xff) | (data[3] & 0xff);
            mNotifyTime = data[4] & 0xff;
            mStartNotifyTime = data[5] & 0xff;
            mStopNotifyTime = data[6] & 0xff;
            mDayFlags = data[7];
        }
        return true;
    }

}

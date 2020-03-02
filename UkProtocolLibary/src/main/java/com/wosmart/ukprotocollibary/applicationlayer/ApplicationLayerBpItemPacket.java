package com.wosmart.ukprotocollibary.applicationlayer;

public class ApplicationLayerBpItemPacket {

    // Parameters
    private int mMinutes;             // 16bits
    private int mSequenceNum;     //8 bits
    private int mValue;
    private int mLowValue;             // 8bits
    private int mHighValue;             // 8bits


    // Packet Length
    public final static int ITEM_LENGTH = 8;

    public boolean parseData(byte[] data) {
        // check header length
        if (data.length < ITEM_LENGTH) {
            return false;
        }
        mMinutes = ((data[2] << 8) | (data[3] & 0xff)) & 0xffff;// here must be care shift operation of negative
        mSequenceNum = (data[4] & 0xff);
        //mValue = ((data[2] << 8) | (data[3] & 0xff)) & 0xffff;// here must be care shift operation of negative
        mValue = (data[5] & 0xff);
        mLowValue = (data[6] & 0xff);
        mHighValue = (data[7] & 0xff);
        return true;
    }

    public int getMinutes() {
        return mMinutes;
    }

    public int getSequenceNum() {
        return mSequenceNum;
    }

    public int getValue() {
        return mValue;
    }

    public int getmLowValue() {
        return mLowValue;
    }

    public int getmHighValue() {
        return mHighValue;
    }
}

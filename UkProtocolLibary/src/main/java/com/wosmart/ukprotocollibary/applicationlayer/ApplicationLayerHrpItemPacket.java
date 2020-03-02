package com.wosmart.ukprotocollibary.applicationlayer;

public class ApplicationLayerHrpItemPacket {

	// Parameters
	private int mMinutes;			 // 16bits
	private int mSequenceNum;     //8 bits
	private int mValue;		     // 8bits
	
	// Packet Length
	public final static int ITEM_LENGTH = 4;
	
	public boolean parseData(byte[] data) {
		// check header length
		if(data.length < ITEM_LENGTH) {
			return false;
		}
		mMinutes = ((data[0] << 8) | (data[1] & 0xff)) & 0xffff;// here must be care shift operation of negative
		mSequenceNum = (data[2] & 0xff);
		//mValue = ((data[2] << 8) | (data[3] & 0xff)) & 0xffff;// here must be care shift operation of negative
		mValue = (data[3] & 0xff);
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
}

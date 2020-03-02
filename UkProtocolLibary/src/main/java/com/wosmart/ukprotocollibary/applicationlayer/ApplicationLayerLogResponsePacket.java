package com.wosmart.ukprotocollibary.applicationlayer;


public class ApplicationLayerLogResponsePacket {


	// Header
	private byte[] mData;
	
	public boolean parseData(byte[] data) {
		// check header length
		mData = new byte[data.length];

		System.arraycopy(data, 0, mData, 0, data.length);
		return true;
	}

	public byte[] getData() {
		return mData;
	}

}

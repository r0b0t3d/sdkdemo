package com.wosmart.ukprotocollibary.applicationlayer;

import java.util.ArrayList;


public class ApplicationLayerStepPacket {

	// Header
	private int mYear;			// 6bits
	private int mMonth;			// 4bits
	private int mDay;			// 5bits
	private int mItemCount;		// 8bits
	
	// Packet Length
	private final static int SPORT_HEADER_LENGTH = 4;

	// Sport Item
	ArrayList<ApplicationLayerStepItemPacket> mStepsItems = new ArrayList<ApplicationLayerStepItemPacket>();
	
	public boolean parseData(byte[] data) {
		// check header length
		if(data.length < SPORT_HEADER_LENGTH) {
			return false;
		}
		mYear = (data[0] & 0x7e) >> 1;// here must be care shift operation of negative
		mMonth = ((data[0] & 0x01) << 3)  | (data[1] >> 5) & 0x07;//here must be care shift operation of negative
		mDay = data[1] & 0x1f;//here must be care shift operation of negative
		mItemCount = data[3] & 0xff;
		// check the item length
		if((data.length - SPORT_HEADER_LENGTH) != mItemCount * ApplicationLayerStepItemPacket.SPORT_ITEM_LENGTH) {
			return false;
		}
		for(int i = 0; i < mItemCount; i ++) {
			ApplicationLayerStepItemPacket sportItem = new ApplicationLayerStepItemPacket();
			
			byte[] sportItemData = new byte[ApplicationLayerStepItemPacket.SPORT_ITEM_LENGTH];
			System.arraycopy(data, SPORT_HEADER_LENGTH + i * ApplicationLayerStepItemPacket.SPORT_ITEM_LENGTH,
					sportItemData, 0, ApplicationLayerStepItemPacket.SPORT_ITEM_LENGTH);
			sportItem.parseData(sportItemData);
			mStepsItems.add(sportItem);
		} 
		return true;
	}


	public ArrayList<ApplicationLayerStepItemPacket> getStepsItems() {
		return mStepsItems;
	}
	public int getYear() {
		return mYear;
	}

	public int getMonth() {
		return mMonth;
	}

	public int getDay() {
		return mDay;
	}

	public int getItemCount() {
		return mItemCount;
	}
}

package com.wosmart.ukprotocollibary.applicationlayer;

import java.util.ArrayList;


public class ApplicationLayerHrpPacket {


	// Header
	private int mYear;			// 6bits
	private int mMonth;			// 4bits
	private int mDay;			// 5bits
	private int mItemCount;		// 8bits
	
	// Packet Length
	private final static int HRP_HEADER_LENGTH = 4;

	// Sport Item
	ArrayList<ApplicationLayerHrpItemPacket> mHrpItems = new ArrayList<ApplicationLayerHrpItemPacket>();
	
	public boolean parseData(byte[] data) {
		// check header length
		if(data.length < HRP_HEADER_LENGTH) {
			return false;
		}
		mYear = (data[0] & 0x7e) >> 1;// here must be care shift operation of negative
		mMonth = ((data[0] & 0x01) << 3)  | (data[1] >> 5) & 0x07;//here must be care shift operation of negative
		mDay = data[1] & 0x1f;//here must be care shift operation of negative
		mItemCount = data[3] & 0xff;
		// check the item length
		if((data.length - HRP_HEADER_LENGTH) != mItemCount * ApplicationLayerHrpItemPacket.ITEM_LENGTH) {
			return false;
		}
		for(int i = 0; i < mItemCount; i ++) {
			ApplicationLayerHrpItemPacket hrpItem = new ApplicationLayerHrpItemPacket();
			
			byte[] hrpItemData = new byte[ApplicationLayerStepItemPacket.SPORT_ITEM_LENGTH];
			System.arraycopy(data, HRP_HEADER_LENGTH + i * ApplicationLayerHrpItemPacket.ITEM_LENGTH,
					hrpItemData, 0, ApplicationLayerHrpItemPacket.ITEM_LENGTH);
			hrpItem.parseData(hrpItemData);
			mHrpItems.add(hrpItem);
		} 
		return true;
	}

	public ArrayList<ApplicationLayerHrpItemPacket> getHrpItems() {
		return mHrpItems;
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

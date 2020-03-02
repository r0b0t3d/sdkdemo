package com.wosmart.ukprotocollibary;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanRecord;

public class WristbandScanCallback {

    public void onWristbandDeviceFind(BluetoothDevice device, int rssi, byte[] scanRecord) {
    }

    public void onWristbandDeviceFind(BluetoothDevice device, int rssi, ScanRecord scanRecord) {
    }

    public void onLeScanEnable(boolean enable) {
    }

    public void onWristbandLoginStateChange(boolean connected) {
    }

}

package com.wosmart.ukprotocollibary.corespec;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;

import com.realsil.realteksdk.GlobalGatt;
import com.realsil.realteksdk.logger.ZLogger;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class DfuService {
    // LOG
    private static final boolean D = true;
    private static final String TAG = "DfuService";

    // Support Service UUID and Characteristic UUID
    private final static UUID OTA_SERVICE_UUID = UUID.fromString("0000d0ff-3c17-d293-8e48-14fe2e4da212");
    private final static UUID OTA_CHARACTERISTIC_UUID = UUID.fromString("0000ffd1-0000-1000-8000-00805f9b34fb");
    private final static UUID OTA_READ_PATCH_CHARACTERISTIC_UUID = UUID.fromString("0000ffd3-0000-1000-8000-00805f9b34fb");
    private final static UUID OTA_READ_APP_CHARACTERISTIC_UUID = UUID.fromString("0000ffd4-0000-1000-8000-00805f9b34fb");

    public final static UUID CLIENT_CHARACTERISTIC_CONFIG = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");


    public static final UUID DFU_SERVICE_UUID = UUID.fromString("00006287-3c17-d293-8e48-14fe2e4da212");
    // Add for extend OTA upload.
    private final static UUID OTA_EXTEND_FLASH_CHARACTERISTIC_UUID = UUID.fromString("00006587-3c17-d293-8e48-14fe2e4da212");

    // Support Service object and Characteristic object
    private BluetoothGattService mService;
    private BluetoothGattCharacteristic mAppCharac;
    private BluetoothGattCharacteristic mPatchCharac;

    private BluetoothGattService mDfuService;
    private BluetoothGattCharacteristic mExtendCharac;

    // Current Battery value
    private int mAppValue = -1;
    private int mPatchValue = -1;

    private GlobalGatt mGlobalGatt;

    private OnServiceListener mCallback;

    private String mBluetoothAddress;

    public DfuService(String addr, OnServiceListener callback) {
        mCallback = callback;
        mBluetoothAddress = addr;

        mGlobalGatt = GlobalGatt.getInstance();
        initial();
    }

    public void close() {
        mGlobalGatt.unRegisterCallback(mBluetoothAddress, mGattCallback);
    }

    private void initial() {
        // register service discovery callback
        mGlobalGatt.registerCallback(mBluetoothAddress, mGattCallback);
    }

    public boolean setService(BluetoothGattService service) {
        if(service.getUuid().equals(OTA_SERVICE_UUID)) {
            mService = service;
            return true;
        }
        return false;
    }

    public List<BluetoothGattCharacteristic> getNotifyCharacteristic() {
        return null;
    }

    public boolean readInfo() {
        if(mAppCharac == null || mPatchCharac == null) {
            ZLogger.e(D, "read Version info error with null charac");
            return false;
        }
        ZLogger.d(D, "read Version info.");
        return readDeviceInfo(mAppCharac);
    }

    public String getServiceUUID() {
        return OTA_SERVICE_UUID.toString();
    }

    public String getServiceSimpleName() {
        return "Dfu";
    }

    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                mService = gatt.getService(OTA_SERVICE_UUID);
                if(mService == null) {
                    ZLogger.e(D, "OTA service not found");
                    return;
                }else {
                    mPatchCharac = mService.getCharacteristic(OTA_READ_PATCH_CHARACTERISTIC_UUID);
                    if(mPatchCharac == null) {
                        ZLogger.e(D, "OTA Patch characteristic not found");
                        return;
                    }else {
                        ZLogger.d(D, "OTA Patch characteristic is found, mPatchCharac: " + mPatchCharac.getUuid());
                    }
                    mAppCharac = mService.getCharacteristic(OTA_READ_APP_CHARACTERISTIC_UUID);
                    if(mAppCharac == null) {
                        ZLogger.e(D, "OTA App characteristic not found");
                        return;
                    }else {
                        ZLogger.d(D, "OTA App characteristic is found, mAppCharac: " + mAppCharac.getUuid());
                    }
                }

                mDfuService = gatt.getService(DFU_SERVICE_UUID);
                if(mDfuService == null) {
                    ZLogger.e(D, "Dfu Service not found");
                    return;
                }else {
                    ZLogger.d(D, "Dfu Service is found, mDfuService: " + mDfuService.getUuid());
                    mExtendCharac = mDfuService.getCharacteristic(OTA_EXTEND_FLASH_CHARACTERISTIC_UUID);
                    if(mExtendCharac == null) {
                        ZLogger.e(D, "Dfu extend characteristic not found");
                        return;
                    }else {
                        ZLogger.d(D, "Dfu extend characteristic is found, mExtendCharac: " + mExtendCharac.getUuid());
                    }
                }
                //isConnected = true;
            } else {
                ZLogger.e(D, "Discovery service error: " + status);
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            //if(D) Log.d(TAG, "onCharacteristicRead UUID is: " + characteristic.getUuid() + ", addr: " +mBluetoothAddress);
            //if(D) Log.d(TAG, "onCharacteristicRead data value:"+ Arrays.toString(characteristic.getValue()) + ", addr: " +mBluetoothAddress);
            byte[] data = characteristic.getValue();
            if (status == BluetoothGatt.GATT_SUCCESS){
                if(characteristic.getUuid().equals(OTA_READ_APP_CHARACTERISTIC_UUID)) {
                    ZLogger.d(D, "data = " + Arrays.toString(characteristic.getValue()));
                    byte[] appVersionValue = characteristic.getValue();
                    ByteBuffer wrapped = ByteBuffer.wrap(appVersionValue);
                    wrapped.order(ByteOrder.LITTLE_ENDIAN);
                    mAppValue = wrapped.getShort(0);

                    //mTargetVersionView.setText(String.valueOf(oldFwVersion));
                    ZLogger.d(D, "old firmware version: " + mAppValue + " .getValue=" + Arrays.toString(characteristic.getValue()));
                    if(mPatchCharac != null) {
                        readDeviceInfo(mPatchCharac);
                    }
                }else if(characteristic.getUuid().equals(OTA_READ_PATCH_CHARACTERISTIC_UUID)){
                    byte[] patchVersionValue = characteristic.getValue();
                    ByteBuffer wrapped = ByteBuffer.wrap(patchVersionValue);
                    wrapped.order(ByteOrder.LITTLE_ENDIAN);
                    mPatchValue = wrapped.getShort(0);
                    ZLogger.d(D, "old patch version: " + mPatchValue + " .getValue=" + Arrays.toString(characteristic.getValue()));
                    //here can add read other characteristic
                    mCallback.onVersionRead(mAppValue, mPatchValue);
                }
            }

        }
    };

    public boolean checkSupportedExtendFlash() {
        return mExtendCharac != null;
    }

    private boolean readDeviceInfo(BluetoothGattCharacteristic characteristic) {
        if(characteristic != null){
            ZLogger.d(D, characteristic.getUuid().toString());

            return mGlobalGatt.readCharacteristic(mBluetoothAddress, characteristic);
        } else {
            ZLogger.e(D, "Characteristic is null");
        }
        return false;
    }

    public int getAppValue() {
        return mAppValue;
    }
    public int getPatchValue() {
        return mPatchValue;
    }

    /**
     * Interface required to be implemented by activity
     */
    public interface OnServiceListener {
        /**
         * Fired when value come.
         *
         * @param appVersion      app Version value
         * @param patchVersion      patch Version value
         *
         */
        void onVersionRead(int appVersion, int patchVersion);
    }
}

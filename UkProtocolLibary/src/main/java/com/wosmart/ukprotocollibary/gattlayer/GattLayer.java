package com.wosmart.ukprotocollibary.gattlayer;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.os.Build;

import com.realsil.realteksdk.GlobalGatt;
import com.realsil.realteksdk.logger.ZLogger;
import com.realsil.realteksdk.utility.DataConverter;

import java.util.UUID;


public class GattLayer {
    // Log
    private final static String TAG = "GattLayer";
    private final static boolean D = true;

    // Gatt Layer Call
    private GattLayerCallback mCallback;

    // Bluetooth Manager
    private BluetoothGatt mBluetoothGatt;

    // MTU size
    private static int MTU_SIZE_EXPECT = 240;

    // Device info
    private String mBluetoothDeviceAddress;

    // Context
    private Context mContext;

    // Global Gatt
    private GlobalGatt mGlobalGatt;

    // UUID
    private final static UUID WRISTBAND_SERVICE_UUID = UUID.fromString("000001ff-3c17-d293-8e48-14fe2e4da212");      //ff01
    private final static UUID WRISTBAND_WRITE_CHARACTERISTIC_UUID = UUID.fromString("0000ff02-0000-1000-8000-00805f9b34fb");
    private final static UUID WRISTBAND_NOTIFY_CHARACTERISTIC_UUID = UUID.fromString("0000ff03-0000-1000-8000-00805f9b34fb");
    private final static UUID WRISTBAND_NAME_CHARACTERISTIC_UUID = UUID.fromString("0000ff04-0000-1000-8000-00805f9b34fb");
    /**
     * Client configuration descriptor that will allow us to enable notifications and indications
     */
    private static final UUID CLIENT_CHARACTERISTIC_CONFIG_DESCRIPTOR_UUID = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");

    // Characteristic
    private BluetoothGattCharacteristic mWriteCharacteristic;
    private BluetoothGattCharacteristic mNameCharacteristic;
    private BluetoothGattCharacteristic mNotifyCharacteristic;

    public GattLayer(Context context, GattLayerCallback callback) {
        ZLogger.d(D, "initial.");
        mContext = context;
        // register callback
        mCallback = callback;
        // Global Gatt
        mGlobalGatt = GlobalGatt.getInstance();
    }

    /**
     * Set the name
     *
     * @param name the name
     */
    public void setDeviceName(String name) {
        ZLogger.d(D, "name: " + name);
        if (mNameCharacteristic == null) {
            return;
        }
        // Send the data
        mNameCharacteristic.setValue(name);
        mGlobalGatt.writeCharacteristicSync(mBluetoothDeviceAddress, mNameCharacteristic);
    }

    /**
     * Get the name
     */
    public void getDeviceName() {
        ZLogger.d(D, "getDeviceName");
        if (mNameCharacteristic == null) {
            return;
        }
        // Send the data
        mGlobalGatt.readCharacteristic(mBluetoothDeviceAddress, mNameCharacteristic);
    }

    /**
     * Send data
     *
     * @param data the data need to send
     */
    public boolean sendData(byte[] data) {
        if (mWriteCharacteristic == null) {
            ZLogger.w(D, "WRISTBAND_WRITE_CHARACTERISTIC_UUID not supported");
            return false;
        }
        if (!mGlobalGatt.isConnected(mBluetoothDeviceAddress)) {
            ZLogger.w(D, "disconnected, addr=" + mBluetoothDeviceAddress);
            return false;
        }
        ZLogger.d(D, "-->> " + DataConverter.bytes2Hex(data));

        // Send the data
        mWriteCharacteristic.setValue(data);
        return mGlobalGatt.writeCharacteristic(mBluetoothDeviceAddress, mWriteCharacteristic);
//		return true;
    }

    /**
     * Connects to the GATT server hosted on the Bluetooth LE device.
     *
     * @param address The device address of the destination device.
     * @return Return true if the connection is initiated successfully. The connection result
     * is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */
    public boolean connect(final String address) {
        ZLogger.d(D, "address: " + address);
        mBluetoothDeviceAddress = address;
        return mGlobalGatt.connect(address, mGattCallback);
    }

    /**
     * When the le services manager close, it must disconnect and close the gatt.
     */
    public void close() {
        ZLogger.d(D, "close()");
        try {
            mGlobalGatt.close(mBluetoothDeviceAddress);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Disconnects an existing connection or cancel a pending connection. The disconnection result
     * is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */
    public void disconnectGatt() {
        ZLogger.d(D, "disconnect()");
        mGlobalGatt.disconnectGatt(mBluetoothDeviceAddress);
    }


    // Implements callback methods for GATT events that the app cares about.  For example,
    // connection change and services discovered.
    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
            ZLogger.d(D, "mtu=" + mtu + ", status=" + status);
            // change the mtu real payloaf size
            if (status == BluetoothGatt.GATT_SUCCESS) {
                mCallback.onDataLengthChanged(mtu - 3);
            }

            // Attempts to discover services after successful connection.
            boolean sta = mBluetoothGatt.discoverServices();
            ZLogger.i(D, "Attempting to start service discovery: " + sta);
        }

        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    mBluetoothGatt = gatt;
                    ZLogger.i(D, "Connected to GATT server.");

                    // only android 5.0 add the requestMTU feature
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {

                        // Attempts to discover services after successful connection.
                        boolean sta = mBluetoothGatt.discoverServices();
                        ZLogger.i(D, "Attempting to start service discovery: " +
                                sta);
                    } else {
                        ZLogger.i(D, "Attempting to request mtu size, expect mtu size is: " + String.valueOf(MTU_SIZE_EXPECT));
                        mBluetoothGatt.requestMtu(MTU_SIZE_EXPECT);
                    }


                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    ZLogger.i(D, "Disconnected from GATT server.");
                    // try to close gatt
                    close();
                    // tell up stack the current connect state
                    mCallback.onConnectionStateChange(true, false);
                }
            } else {
                ZLogger.e(D, "error: status " + status + " newState: " + newState);
                // try to close gatt
                close();
                // tell up stack the current connect state
                mCallback.onConnectionStateChange(false, false);
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            ZLogger.d(D, "status=" + status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                // set the characteristic
                // initial the service and characteristic
                BluetoothGattService service = gatt.getService(WRISTBAND_SERVICE_UUID);
                if (service == null) {
                    ZLogger.w(D, "WRISTBAND_SERVICE_UUID not supported");

                    // try to disconnect gatt
                    disconnectGatt();
                    return;
                }
                mWriteCharacteristic = service.getCharacteristic(WRISTBAND_WRITE_CHARACTERISTIC_UUID);
                if (mWriteCharacteristic == null) {
                    ZLogger.w(D, "WRISTBAND_WRITE_CHARACTERISTIC_UUID not supported");

                    // try to disconnect gatt
                    disconnectGatt();
                    return;
                }
                mNameCharacteristic = service.getCharacteristic(WRISTBAND_NAME_CHARACTERISTIC_UUID);
                if (mNameCharacteristic == null) {
                    ZLogger.w(D, "WRISTBAND_NAME_CHARACTERISTIC_UUID not supported");

                    // try to disconnect gatt
                    disconnectGatt();
                    return;
                }
                mNotifyCharacteristic = service.getCharacteristic(WRISTBAND_NOTIFY_CHARACTERISTIC_UUID);
                if (mNotifyCharacteristic == null) {
                    ZLogger.w(D, "WRISTBAND_NOTIFY_CHARACTERISTIC_UUID not supported");

                    // try to disconnect gatt
                    disconnectGatt();
                    return;
                }
                // enable notification
                mGlobalGatt.setCharacteristicNotification(mBluetoothDeviceAddress, mNotifyCharacteristic, true);
                // tell up stack the current connect state
                //mCallback.onConnectionStateChange(true, true);
            } else {
                // try to disconnect gatt
                disconnectGatt();
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            byte[] data = characteristic.getValue();

            if (WRISTBAND_NOTIFY_CHARACTERISTIC_UUID.equals(characteristic.getUuid())) {
                ZLogger.d(D, "<<-- olength: " + characteristic.getValue().length
                        + ", data: " + DataConverter.bytes2Hex(data));
                // tell up stack a data receive
                mCallback.onDataReceive(data);
            }

        }

        @Override
        public void onCharacteristicWrite(final BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic, final int status) {
            if (WRISTBAND_WRITE_CHARACTERISTIC_UUID.equals(characteristic.getUuid())) {
                mCallback.onDataSend(status == BluetoothGatt.GATT_SUCCESS);
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            ZLogger.d(D, "<<<--- status: " + status + " value: " + DataConverter.bytes2Hex(characteristic.getValue()));
            if (WRISTBAND_NAME_CHARACTERISTIC_UUID.equals(characteristic.getUuid())) {
                String name = characteristic.getStringValue(0);
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    // tell up stack data send right
                    mCallback.onNameReceive(name);
                }
            }
        }

        @Override
        public void onDescriptorWrite(final BluetoothGatt gatt, final BluetoothGattDescriptor descriptor, final int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                if (CLIENT_CHARACTERISTIC_CONFIG_DESCRIPTOR_UUID.equals(descriptor.getUuid())) {
                    if (descriptor.getCharacteristic().getUuid().equals(mNotifyCharacteristic.getUuid())) {
                        boolean enabled = descriptor.getValue()[0] == 1;
                        if (enabled) {
                            ZLogger.d(D, "Notification enabled");
                            // tell up stack the current connect state
                            mCallback.onConnectionStateChange(true, true);
                        } else {
                            ZLogger.e(D, "Notification  not enabled!!!");
                            disconnectGatt();
                        }
                    }
                }
            } else {
                ZLogger.e(D, "Descriptor write error: " + status);
                disconnectGatt();
            }
        }
    };
}

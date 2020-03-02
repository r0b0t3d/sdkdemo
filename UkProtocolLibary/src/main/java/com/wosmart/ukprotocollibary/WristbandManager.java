package com.wosmart.ukprotocollibary;

/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelUuid;
import android.provider.Settings;
import android.text.TextUtils;

import com.realsil.realteksdk.RealtekSDK;
import com.realsil.realteksdk.bluetooth.SpecScanRecord;
import com.realsil.realteksdk.corespec.BatteryService;
import com.realsil.realteksdk.corespec.HrpService;
import com.realsil.realteksdk.corespec.ImmediateAlertService;
import com.realsil.realteksdk.corespec.LinkLossService;
import com.realsil.realteksdk.logger.ZLogger;
import com.realsil.realteksdk.utility.PermissionUtil;
import com.wosmart.ukprotocollibary.applicationlayer.ApplicationLayer;
import com.wosmart.ukprotocollibary.applicationlayer.ApplicationLayerAlarmPacket;
import com.wosmart.ukprotocollibary.applicationlayer.ApplicationLayerAlarmsPacket;
import com.wosmart.ukprotocollibary.applicationlayer.ApplicationLayerBpPacket;
import com.wosmart.ukprotocollibary.applicationlayer.ApplicationLayerCallback;
import com.wosmart.ukprotocollibary.applicationlayer.ApplicationLayerDisturbPacket;
import com.wosmart.ukprotocollibary.applicationlayer.ApplicationLayerFacSensorPacket;
import com.wosmart.ukprotocollibary.applicationlayer.ApplicationLayerHrpPacket;
import com.wosmart.ukprotocollibary.applicationlayer.ApplicationLayerLogResponsePacket;
import com.wosmart.ukprotocollibary.applicationlayer.ApplicationLayerRecentlySportPacket;
import com.wosmart.ukprotocollibary.applicationlayer.ApplicationLayerSitPacket;
import com.wosmart.ukprotocollibary.applicationlayer.ApplicationLayerSleepPacket;
import com.wosmart.ukprotocollibary.applicationlayer.ApplicationLayerSportPacket;
import com.wosmart.ukprotocollibary.applicationlayer.ApplicationLayerStepPacket;
import com.wosmart.ukprotocollibary.applicationlayer.ApplicationLayerTodaySportPacket;
import com.wosmart.ukprotocollibary.applicationlayer.ApplicationLayerTodaySumSportPacket;
import com.wosmart.ukprotocollibary.applicationlayer.ApplicationLayerUserPacket;
import com.wosmart.ukprotocollibary.corespec.DfuService;
import com.wosmart.ukprotocollibary.model.data.DeviceFunction;
import com.wosmart.ukprotocollibary.model.data.ReminderFunction;
import com.wosmart.ukprotocollibary.model.data.SportFunction;
import com.wosmart.ukprotocollibary.model.enums.DeviceLanguage;
import com.wosmart.ukprotocollibary.model.enums.NotifyType;
import com.wosmart.ukprotocollibary.util.SPWristbandConfigInfo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

import static com.wosmart.ukprotocollibary.applicationlayer.ApplicationLayer.DEBUG_LOG_TYPE_MAX_CNT;
import static com.wosmart.ukprotocollibary.applicationlayer.ApplicationLayer.LOGIN_RSP_SUCCESS;

public class WristbandManager
        implements BatteryService.OnServiceListener, LinkLossService.OnServiceListener, DfuService.OnServiceListener, HrpService.OnServiceListener {
    // Log
    private final static String TAG = "WristbandManager";

    private final static boolean D = true;

    public static final String ACTION_SYNC_DATA_OK = "ACTION_SYNC_DATA_OK";

    private static final String ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners";

    // Message
    public static final int MSG_STATE_CONNECTED = 0;
    public static final int MSG_STATE_DISCONNECTED = 1;
    public static final int MSG_WRIST_STATE_CHANGED = 2;
    public static final int MSG_RECEIVE_STEP_INFO = 3;//characteristic read
    public static final int MSG_RECEIVE_SLEEP_INFO = 4;
    public static final int MSG_RECEIVE_HISTORY_SYNC_BEGIN = 5;
    public static final int MSG_RECEIVE_HISTORY_SYNC_END = 6;
    public static final int MSG_RECEIVE_ALARMS_INFO = 7;
    public static final int MSG_RECEIVE_NOTIFY_MODE_SETTING = 8;
    public static final int MSG_RECEIVE_LONG_SIT_SETTING = 9;
    public static final int MSG_RECEIVE_FAC_SENSOR_INFO = 10;
    public static final int MSG_RECEIVE_DFU_VERSION_INFO = 11;
    public static final int MSG_RECEIVE_DEVICE_NAME_INFO = 12;
    public static final int MSG_RECEIVE_BATTERY_INFO = 13;
    public static final int MSG_RECEIVE_BATTERY_CHANGE_INFO = 14;
    public static final int MSG_RECEIVE_HRP_INFO = 15;
    public static final int MSG_RECEIVE_TAKE_PHOTO_RSP = 16;
    public static final int MSG_RECEIVE_TURN_OVER_WRIST_SETTING = 17;
    public static final int MSG_RECEIVE_HRP_DEVICE_CANCEL_SINGLE_READ = 18;
    public static final int MSG_RECEIVE_HRP_CONTINUE_PARAM_RSP = 19;
    public static final int MSG_RECEIVER_SPORT_FUNCTION = 20;
    public static final int MSG_RECEIVER_REMINDER_FUNCTION = 21;
    public static final int MSG_RECEIVER_DEVICE_FUNCTION = 22;
    public static final int MSG_RECEIVER_HOUR_SYSTEM = 23;
    public static final int MSG_RECEIVER_UNIT_SYSTEM = 24;
    public static final int MSG_RECEIVER_DISTURB = 25;
    public static final int MSG_RECEIVER_SCREEN_LIGHT_DURATION = 26;
    public static final int MSG_RECEIVER_LANGUAGE = 27;
    public static final int MSG_RECEIVER_END_CALL = 28;
    public static final int MSG_RECEIVER_BP_INFO = 29;
    public static final int MSG_RECEIVE_BP_DEVICE_CANCEL_SINGLE_READ = 30;
    public static final int MSG_RECEIVE_SPORT_INFO = 31;
    public static final int MSG_RECEIVE_CLASSIC_ADDRESS = 32;
    public static final int MSG_RECEIVE_CLASSIC_STATUS = 33;

    public static final int MSG_RECEIVE_LOG_START = 50;
    public static final int MSG_RECEIVE_LOG_END = 51;
    public static final int MSG_RECEIVE_LOG_RSP = 52;


    public static final int MSG_ERROR = 100;

    // Wristband state manager
    public int mWristState;
    public static final int STATE_WRIST_INITIAL = 0;
    public static final int STATE_WRIST_LOGING = 1;
    public static final int STATE_WRIST_BONDING = 2;
    public static final int STATE_WRIST_LOGIN = 3;
    public static final int STATE_WRIST_SYNC_DATA = 4;
    public static final int STATE_WRIST_SYNC_HISTORY_DATA = 5;
    public static final int STATE_WRIST_SYNC_LOG_DATA = 6;
    public static final int STATE_WRIST_ENTER_TEST_MODE = 7;

    private boolean mErrorStatus;
    public static final int ERROR_CODE_NO_LOGIN_RESPONSE_COME = 1;
    public static final int ERROR_CODE_BOND_ERROR = 2;
    public static final int ERROR_CODE_COMMAND_SEND_ERROR = 3;

    // Token Key
    private final byte[] TEST_TOKEN_KEY = {(byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x01,
            (byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x01,
            (byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x01,
            (byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x01,
            (byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x01,
            (byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x01};

    // Use to manager request and response transaction

    private static Context mContext;

    private static WristbandManager mInstance;

    private final Object mRequestResponseLock = new Object();

    private final int MAX_REQUEST_RESPONSE_TRANSACTION_WAIT_TIME = 30000;

    private volatile boolean isResponseCome;

    private volatile boolean isNeedWaitForResponse;

    // Use to manager command send transaction
    private volatile boolean isInSendCommand;

    private volatile boolean isCommandSend;

    private volatile boolean isCommandSendOk;

    private final Object mCommandSendLock = new Object();

    private final int MAX_COMMAND_SEND_WAIT_TIME = 15000;

    /**
     * scan
     */
    private BluetoothManager mBluetoothManager;

    private BluetoothAdapter mBluetoothAdapter;

    private BluetoothAdapter.LeScanCallback mLeScanCallback;

    private ArrayList<WristbandScanCallback> scanCallbacks;

    private final static UUID WRISTBAND_SERVICE_UUID = UUID.fromString("000001ff-3c17-d293-8e48-14fe2e4da212");

    private boolean mScanning;

    private Handler scanHandler;

    private boolean isInSyncDataToService = false;

    private String mDeviceName;

    private String mDeviceAddress;

    // Application Layer Object
    private ApplicationLayer mApplicationLayer;

    private ApplicationLayerCallback mApplicationCallback;

    // Extend Service
    private BatteryService mBatteryService;

    private ImmediateAlertService mImmediateAlertService;

    private LinkLossService mLinkLossService;

    private DfuService mDfuService;

    private boolean isConnected = false;

    // object
    private ArrayList<WristbandManagerCallback> mCallbacks;

    private volatile int mAppVersion = -1;

    private volatile int mPatchVersion = -1;

    public WristbandManager(Context context) {
        mContext = context;

        RealtekSDK.init(context);

        // Initial ScanBack list
        scanCallbacks = new ArrayList<>();

        //initial bluetooth manager and callback
        initialScanCallBack();

        mBluetoothManager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);

        mBluetoothAdapter = mBluetoothManager.getAdapter();

        scanHandler = new Handler();

        isConnected = false;

        // initial Wristband Application Layer and register the callback
        initialApplicationCallback();

        mApplicationLayer = new ApplicationLayer(context, mApplicationCallback);

        // Initial Callback list
        mCallbacks = new ArrayList<>();

        // Initial State
        mWristState = STATE_WRIST_INITIAL;
    }

    public static synchronized WristbandManager getInstance(Context context) {
        if (null == mInstance) {
            synchronized (WristbandManager.class) {
                if (null == mInstance) {
                    mInstance = new WristbandManager(context);
                }
            }
        }
        return mInstance;
    }

    private void initialScanCallBack() {
        if (null == mLeScanCallback) {
            mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

                @Override
                public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {
                    if (!mScanning) {
                        ZLogger.w(D, "is stop le scan, return");
                        return;
                    }
                    SpecScanRecord record = SpecScanRecord.parseFromBytes(scanRecord);
                    ZLogger.d(D, record.toString());

                    if ((record.getServiceUuids() == null) || (!record.getServiceUuids().contains(new ParcelUuid(WRISTBAND_SERVICE_UUID)))) {
                        return;
                    }

                    for (WristbandScanCallback callback : scanCallbacks) {
                        callback.onWristbandDeviceFind(device, rssi, scanRecord);
                    }
                }
            };
        }
    }

    public void close() {
        ZLogger.d(D, "close()");
        // be careful here!!!!
        isConnected = false;

        mCallbacks.clear();
        mApplicationLayer.close();
        // close all wait lock
        synchronized (mRequestResponseLock) {
            isResponseCome = false;
            isNeedWaitForResponse = false;
            mRequestResponseLock.notifyAll();
        }

        synchronized (mCommandSendLock) {
            isCommandSend = false;
            isCommandSendOk = false;
            isInSendCommand = false;
            mCommandSendLock.notifyAll();
        }
        updateWristState(STATE_WRIST_INITIAL);
    }

    private void registerScanCallback(WristbandScanCallback callback) {
        synchronized (scanCallbacks) {
            if (!scanCallbacks.contains(callback)) {
                scanCallbacks.add(callback);
            }
        }
    }

    public void startScan(WristbandScanCallback callback) {
        registerScanCallback(callback);
        scanLeDevice(true);
        scanHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                scanLeDevice(false);
            }
        }, 6000);
    }

    public void stopScan() {
        scanLeDevice(false);
    }

    private void scanLeDevice(boolean enable) {
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            ZLogger.w(D, "please ensure bluetooth is enabled.");
            for (WristbandScanCallback callback : scanCallbacks) {
                callback.onLeScanEnable(false);
            }
            return;
        }

        if (enable) {
            if (!PermissionUtil.checkSelfPermissions(mContext, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION})) {
                ZLogger.w("Need ACCESS_COARSE_LOCATION or ACCESS_FINE_LOCATION permission to get scan results");
                for (WristbandScanCallback callback : scanCallbacks) {
                    callback.onLeScanEnable(false);
                }
                return;
            }
            // avoid repetition operator
            if (mScanning == true) {
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
            }
            UUID[] serviceUUIDs = {WRISTBAND_SERVICE_UUID};
            mBluetoothAdapter.startLeScan(serviceUUIDs, mLeScanCallback);
        } else {
            if (mScanning == true) {
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
            }
        }
        // update le scan status
        mScanning = enable;
        for (WristbandScanCallback callback : scanCallbacks) {
            callback.onLeScanEnable(mScanning);
        }
    }

    public boolean isConnect() {
        ZLogger.d(D, "isConnected: " + isConnected);
        return isConnected;
    }

    public String getBluetoothAddress() {
        return mDeviceAddress;
    }

    public void registerCallback(WristbandManagerCallback callback) {
        synchronized (mCallbacks) {
            if (!mCallbacks.contains(callback)) {
                mCallbacks.add(callback);
            }
        }
    }

    public boolean isCallbackRegistered(WristbandManagerCallback callback) {
        boolean isCon;
        synchronized (mCallbacks) {
            isCon = mCallbacks.contains(callback);
        }
        return isCon;
    }

    public void unRegisterCallback(WristbandManagerCallback callback) {
        synchronized (mCallbacks) {
            if (mCallbacks.contains(callback)) {
                mCallbacks.remove(callback);
            }
        }
    }

    /**
     * Connect to the wristband.
     */
    public void connect(String address, WristbandManagerCallback callback) {
        ZLogger.d(D, "Connect to: " + address);
        // register callback
        registerCallback(callback);

        // connect to the device
        mDeviceAddress = address;
        mApplicationLayer.connect(address);

        // Add first initial flag
        SPWristbandConfigInfo.setFirstInitialFlag(mContext, true);

        // close all.
        if (mBatteryService != null) {
            mBatteryService.close();
            mImmediateAlertService.close();
            mLinkLossService.close();
            mDfuService.close();
        }
        // Extend service
        mBatteryService = new BatteryService(mDeviceAddress, this);
        mImmediateAlertService = new ImmediateAlertService(mDeviceAddress);
        mLinkLossService = new LinkLossService(mDeviceAddress, this);
        mDfuService = new DfuService(mDeviceAddress, this);
    }

    /**
     * start login
     *
     * @param id user id
     */
    public void startLoginProcess(final String id) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // update state
                updateWristState(STATE_WRIST_LOGING);
				/*
				if(ConstantParam.isInDebugMode()) {
					if(D) Log.i(TAG, "Need to send log enable command.");
					SendLogEnableCommand();
				}*/

                // Enable battery notification
                enableBatteryNotification(true);

                // Request to login
                if (!requestLogin(id)) {
                    if (!mErrorStatus) {
                        // update state
                        updateWristState(STATE_WRIST_BONDING);
                        // Request to bond
                        if (requestBond(id)) {
                            // do all the setting work.
                            setDataSync(false);
                            if (requestSetNeedInfo()) {
                                // update state
                                updateWristState(STATE_WRIST_LOGIN);
                            }
                        } else {

                            ZLogger.e(D, "Something error in bond. isResponseCome: " + isResponseCome);
                            // some thing error
                            if (isResponseCome) {
                                sendErrorMessage(ERROR_CODE_BOND_ERROR);
                            } else {
                                sendErrorMessage(ERROR_CODE_NO_LOGIN_RESPONSE_COME);
                            }
                            return;
                        }
                    } else {
                        // some thing error
                        sendErrorMessage(ERROR_CODE_NO_LOGIN_RESPONSE_COME);
                        ZLogger.e(D, "long time no login response, do disconnect");
                        return;
                    }
                } else {
                    //if(syncNotifySetting()) {
                    if (mLoginResponseStatus == LOGIN_RSP_SUCCESS) {
                        // time sync
                        if (setTimeSync()) {
                            if (setPhoneOS()) {
                                if (setClocksSyncRequest()) {
                                    if (sendNotifyModeRequest()) {
                                        if (sendLongSitRequest()) {
                                            if (sendTurnOverWristRequest()) {
                                                if (sendContinueHrpParamRequest()) {
                                                    // update state
                                                    updateWristState(STATE_WRIST_LOGIN);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    //}
                }
            }
        }).start();

    }

    private boolean syncNotifySetting() {
        // initial error status
        mErrorStatus = false;
        isResponseCome = false;
        if (sendNotifyModeRequest()) {
            // wait for a while the remote response
            synchronized (mRequestResponseLock) {
                if (!isResponseCome) {
                    try {
                        // wait a while
                        ZLogger.d(D, "wait the notify setting response come, wait for: " + MAX_REQUEST_RESPONSE_TRANSACTION_WAIT_TIME + "ms");
                        mRequestResponseLock.wait(MAX_REQUEST_RESPONSE_TRANSACTION_WAIT_TIME);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }

            if (!isResponseCome) {
                ZLogger.e(D, "wait the notify setting response come failed");

                mErrorStatus = true;
                return false;
            }

            // initial error status
            mErrorStatus = false;
            isResponseCome = false;
            if (sendLongSitRequest()) {
                // wait for a while the remote response
                synchronized (mRequestResponseLock) {
                    if (!isResponseCome) {
                        try {
                            // wait a while
                            ZLogger.d(D, "wait the long sit setting response come, wait for: " + MAX_REQUEST_RESPONSE_TRANSACTION_WAIT_TIME + "ms");
                            mRequestResponseLock.wait(MAX_REQUEST_RESPONSE_TRANSACTION_WAIT_TIME);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
                if (!isResponseCome) {
                    ZLogger.e(D, "wait the long sit setting response come failed");
                    mErrorStatus = true;
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    public boolean requestSendLongSitRequestSync() {
        // initial error status
        mErrorStatus = false;
        isResponseCome = false;
        if (sendLongSitRequest()) {
            // wait for a while the remote response
            synchronized (mRequestResponseLock) {
                if (!isResponseCome) {
                    try {
                        // wait a while
                        ZLogger.d(D, "wait the long sit setting response come, wait for: " + MAX_REQUEST_RESPONSE_TRANSACTION_WAIT_TIME + "ms");
                        mRequestResponseLock.wait(MAX_REQUEST_RESPONSE_TRANSACTION_WAIT_TIME);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            if (!isResponseCome) {
                ZLogger.e(D, "wait the long sit setting response come failed");
                mErrorStatus = true;
                return false;
            }
            return true;
        }
        return false;
    }

    private boolean requestSetNeedInfo() {
        boolean enableCall = SPWristbandConfigInfo.getNotifyCallFlag(mContext);
        boolean enableMes = SPWristbandConfigInfo.getNotifyMessageFlag(mContext);
        boolean enableQQ = SPWristbandConfigInfo.getNotifyQQFlag(mContext);
        boolean enableWechat = SPWristbandConfigInfo.getNotifyWechatFlag(mContext);

        //ApplicationLayerSitPacket sit = new ApplicationLayerSitPacket((byte) 0x01, 6123, 57, 16, 17, (byte) (ApplicationLayer.REPETITION_SUN | ApplicationLayer.REPETITION_FRI));
        // set time sync
        if (setUserProfile()) {
            if (setTargetStep()) {
                if (setTimeSync()) {
                    if (setPhoneOS()) {
                        if (setClocksSyncRequest()) {
                            if (sendNotifyModeRequest()) {
                                if (sendLongSitRequest()) {
                                    if (sendTurnOverWristRequest()) {
//                                        if (sendSyncTodayStepCommand()) {
                                        if (sendSyncTodayNearlyOffsetStepCommand()) {
                                            if (sendContinueHrpParamRequest()) {
                                                //if (SetNotifyMode(enableCall ? ApplicationLayer.CALL_NOTIFY_MODE_ON : ApplicationLayer.CALL_NOTIFY_MODE_OFF)) {
                                                //if (SetNotifyMode(enableMes ? ApplicationLayer.CALL_NOTIFY_MODE_ENABLE_MESSAGE : ApplicationLayer.CALL_NOTIFY_MODE_DISABLE_MESSAGE)) {
                                                //if (SetNotifyMode(enableQQ ? ApplicationLayer.CALL_NOTIFY_MODE_ENABLE_QQ : ApplicationLayer.CALL_NOTIFY_MODE_DISABLE_QQ)) {
                                                //if (SetNotifyMode(enableWechat ? ApplicationLayer.CALL_NOTIFY_MODE_ENABLE_WECHAT : ApplicationLayer.CALL_NOTIFY_MODE_DISABLE_WECHAT)) {
                                                ZLogger.e(D, "all set is ok!");
                                                return true;
                                                //}
                                                //}
                                                //}
                                                //}
                                            }
//                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Set the name
     *
     * @param name the name
     */
    public boolean setDeviceName(String name) {
        ZLogger.d(D, "name: " + name);
        initialCommandSend();

        mApplicationLayer.setDeviceName(name);

        isInSendCommand = false;

        SPWristbandConfigInfo.setInfoKeyValue(mContext, getBluetoothAddress(), name);

        return waitCommandSend();
    }

    /**
     * Get the name
     */
    public boolean getDeviceName() {
        ZLogger.d(D, "getDeviceName");
        initialCommandSend();

        mApplicationLayer.getDeviceName();

        return waitCommandSend();
    }

    /**
     * Use to sync the notify mode
     *
     * @return the operate result
     */
    public boolean setNotifyMode(NotifyType type, boolean flag) {
        byte mode;
        switch (type) {
            case CALL:
                if (flag) {
                    mode = ApplicationLayer.CALL_NOTIFY_MODE_ON;
                } else {
                    mode = ApplicationLayer.CALL_NOTIFY_MODE_OFF;
                }
                break;
            case QQ:
                if (flag) {
                    mode = ApplicationLayer.CALL_NOTIFY_MODE_ENABLE_QQ;
                } else {
                    mode = ApplicationLayer.CALL_NOTIFY_MODE_DISABLE_QQ;
                }
                break;
            case WECHAT:
                if (flag) {
                    mode = ApplicationLayer.CALL_NOTIFY_MODE_ENABLE_WECHAT;
                } else {
                    mode = ApplicationLayer.CALL_NOTIFY_MODE_DISABLE_WECHAT;
                }
                break;
            case SMS:
                if (flag) {
                    mode = ApplicationLayer.CALL_NOTIFY_MODE_ENABLE_MESSAGE;
                } else {
                    mode = ApplicationLayer.CALL_NOTIFY_MODE_DISABLE_MESSAGE;
                }
                break;
            case LINE:
                if (flag) {
                    mode = ApplicationLayer.CALL_NOTIFY_MODE_ENABLE_LINE;
                } else {
                    mode = ApplicationLayer.CALL_NOTIFY_MODE_DISABLE_LINE;
                }
                break;
            case TWITTER:
                if (flag) {
                    mode = ApplicationLayer.CALL_NOTIFY_MODE_ENABLE_TWITTER;
                } else {
                    mode = ApplicationLayer.CALL_NOTIFY_MODE_DISABLE_TWITTER;
                }
                break;
            case FACEBOOK:
                if (flag) {
                    mode = ApplicationLayer.CALL_NOTIFY_MODE_ENABLE_FACEBOOK;
                } else {
                    mode = ApplicationLayer.CALL_NOTIFY_MODE_DISABLE_FACEBOOK;
                }
                break;
            case MESSENGER:
                if (flag) {
                    mode = ApplicationLayer.CALL_NOTIFY_MODE_ENABLE_MESSENGER;
                } else {
                    mode = ApplicationLayer.CALL_NOTIFY_MODE_DISABLE_MESSENGER;
                }
                break;
            case WHATSAPP:
                if (flag) {
                    mode = ApplicationLayer.CALL_NOTIFY_MODE_ENABLE_WHATSAPP;
                } else {
                    mode = ApplicationLayer.CALL_NOTIFY_MODE_DISABLE_WHATSAPP;
                }
                break;
            case LINKEDIN:
                if (flag) {
                    mode = ApplicationLayer.CALL_NOTIFY_MODE_ENABLE_LINKEDIN;
                } else {
                    mode = ApplicationLayer.CALL_NOTIFY_MODE_DISABLE_LINKEDIN;
                }
                break;
            case INSTAGRAM:
                if (flag) {
                    mode = ApplicationLayer.CALL_NOTIFY_MODE_ENABLE_INSTAGRAM;
                } else {
                    mode = ApplicationLayer.CALL_NOTIFY_MODE_DISABLE_INSTAGRAM;
                }
                break;
            case SKYPE:
                if (flag) {
                    mode = ApplicationLayer.CALL_NOTIFY_MODE_ENABLE_SKYPE;
                } else {
                    mode = ApplicationLayer.CALL_NOTIFY_MODE_DISABLE_SKYPE;
                }
                break;
            case VIBER:
                if (flag) {
                    mode = ApplicationLayer.CALL_NOTIFY_MODE_ENABLE_VIBER;
                } else {
                    mode = ApplicationLayer.CALL_NOTIFY_MODE_DISABLE_VIBER;
                }
                break;
            case KAKAOTALK:
                if (flag) {
                    mode = ApplicationLayer.CALL_NOTIFY_MODE_ENABLE_KAKAOTALK;
                } else {
                    mode = ApplicationLayer.CALL_NOTIFY_MODE_DISABLE_KAKAOTALK;
                }
                break;
            case VKONTAKTE:
                if (flag) {
                    mode = ApplicationLayer.CALL_NOTIFY_MODE_ENABLE_VKONTAKTE;
                } else {
                    mode = ApplicationLayer.CALL_NOTIFY_MODE_DISABLE_VKONTAKE;
                }
                break;
            default:
                if (flag) {
                    mode = ApplicationLayer.CALL_NOTIFY_MODE_ENABLE_QQ;
                } else {
                    mode = ApplicationLayer.CALL_NOTIFY_MODE_DISABLE_QQ;
                }
                break;
        }
        ZLogger.d(D, "mode: " + mode);
        initialCommandSend();

        // Try to set notify mode
        mApplicationLayer.SettingCmdCallNotifySetting(mode);

        return waitCommandSend();
    }

    /**
     * Use to request current notify mode
     *
     * @return the operate result
     */
    public boolean sendNotifyModeRequest() {
        ZLogger.d(D, "SendNotifyModeRequest");
        initialCommandSend();
        // Need add this
        isNeedWaitForResponse = true;
        // Try to request notify mode
        mApplicationLayer.SettingCmdRequestNotifySwitchSetting();

        return waitCommandSend();
    }

    /**
     * Use to start/stop data sync.
     *
     * @param enable start or stop data sync.
     * @return the operate result
     */
    public boolean setDataSync(boolean enable) {
        ZLogger.d(D, "SetDataSync(): " + enable);
		/*
        if(mWristState != STATE_WRIST_LOGIN && mWristState != STATE_WRIST_SYNC_DATA) {
            if(D) Log.e(TAG, "SetDataSync failed, with error state: " + mWristState);
            return false;
        }
        */

        initialCommandSend();

        // Try to start/stop data sync
        if (enable) {
            mApplicationLayer.SportDataCmdSyncSetting(ApplicationLayer.SPORT_DATA_SYNC_MODE_ENABLE);
            //if(!waitCommandSend()) {
            //	return false;
            //}
            //initialCommandSend();
            //mApplicationLayer.SportDataCmdRequestData();

            //UpdateWristState(STATE_WRIST_SYNC_DATA);
        } else {
            mApplicationLayer.SportDataCmdSyncSetting(ApplicationLayer.SPORT_DATA_SYNC_MODE_DISABLE);

            //UpdateWristState(STATE_WRIST_LOGIN);
        }

        return waitCommandSend();
    }

    /**
     * Use to send data request.
     *
     * @return the operate result
     */
    public boolean sendDataRequest() {
        ZLogger.d(D, "SendDataRequest()");
        if (mWristState != STATE_WRIST_LOGIN && mWristState != STATE_WRIST_SYNC_DATA) {
            ZLogger.e(D, "with error state: " + mWristState);
            return false;
        }

        initialCommandSend();

        isNeedWaitForResponse = true;
        mApplicationLayer.SportDataCmdRequestData();

        return waitCommandSend();
    }

    /**
     * Use to enable/disable the long sit set
     *
     * @return the operate result
     */
    public boolean setLongSit(boolean enable) {
        ZLogger.d(D, "enable: " + enable);
        initialCommandSend();
        ApplicationLayerSitPacket sit;
        if (enable) {
            sit = new ApplicationLayerSitPacket(ApplicationLayer.LONG_SIT_CONTROL_ENABLE
                    , 0, SPWristbandConfigInfo.getLongSitAlarmTime(mContext), 0, 0, (byte) 0);
        } else {
            sit = new ApplicationLayerSitPacket(ApplicationLayer.LONG_SIT_CONTROL_DISABLE
                    , 0, 0, 0, 0, (byte) 0);
        }
        // Try to set long sit
        mApplicationLayer.SettingCmdLongSitSetting(sit);

        return waitCommandSend();
    }

    /**
     * Use to enable/disable the continue hrp set
     *
     * @return the operate result
     */
    public boolean setContinueHrp(boolean enable, int interval) {
        ZLogger.d(D, "enable: " + enable);
        initialCommandSend();

        // Try to set long sit
        mApplicationLayer.SportDataCmdHrpContinueSet(enable, interval);

        return waitCommandSend();
    }


    public boolean sendContinueHrpParamRequest() {
        ZLogger.d(D, "SendContinueHrpParamRequest");
        initialCommandSend();

        // Try to set long sit
        mApplicationLayer.SportDataCmdHrpContinueParamRequest();

        return waitCommandSend();
    }

    /**
     * Use to enable/disable the long sit set
     *
     * @return the operate result
     */
    public boolean setTurnOverWrist(boolean enable) {
        ZLogger.d(D, "enable: " + enable);
        initialCommandSend();
        // Try to set long sit
        mApplicationLayer.SettingCmdTurnOverWristSetting(enable);

        return waitCommandSend();
    }

    /**
     * Use to sync the long sit set
     *
     * @return the operate result
     */
    public boolean setLongSit(ApplicationLayerSitPacket sit) {
        ZLogger.d(D, "SetLongSit()");
        initialCommandSend();

        // Try to set long sit
        mApplicationLayer.SettingCmdLongSitSetting(sit);

        return waitCommandSend();
    }


    /**
     * Use to request current long sit set
     *
     * @return the operate result
     */
    public boolean sendLongSitRequest() {
        ZLogger.d(D, "SendLongSitRequest()");
        initialCommandSend();
        // Need add this
        isNeedWaitForResponse = true;

        // Try to set long sit
        mApplicationLayer.SettingCmdRequestLongSitSetting();

        return waitCommandSend();
    }

    /**
     * Use to request current long sit set
     *
     * @return the operate result
     */
    public boolean sendTurnOverWristRequest() {
        ZLogger.d(D, "SendTurnOverWristRequest()");
        initialCommandSend();
        // Need add this
        isNeedWaitForResponse = true;

        // Try to set long sit
        mApplicationLayer.SettingCmdRequestTurnOverWristSetting();

        return waitCommandSend();
    }

    /**
     * Use to sync the user profile, use local info
     *
     * @return the operate result
     */
    public boolean setUserProfile() {
        ZLogger.d(D, "SetUserProfile()");
        initialCommandSend();
        boolean sex = SPWristbandConfigInfo.getGendar(mContext);
        int age = SPWristbandConfigInfo.getAge(mContext);
        int height = SPWristbandConfigInfo.getHeight(mContext);
        int weight = SPWristbandConfigInfo.getWeight(mContext);

        ApplicationLayerUserPacket user = new ApplicationLayerUserPacket(sex, age, height, weight);
        // Try to set user profile
        mApplicationLayer.SettingCmdUserSetting(user);

        return waitCommandSend();
    }

    /**
     * Use to sync the user profile
     *
     * @return the operate result
     */
    public boolean setUserProfile(ApplicationLayerUserPacket user) {
        ZLogger.d(D, "SetUserProfile()");
        initialCommandSend();

        // Try to set user profile
        mApplicationLayer.SettingCmdUserSetting(user);

        return waitCommandSend();
    }

    /**
     * Use to sync the target step, user local info
     *
     * @return the operate result
     */
    public boolean setTargetStep() {
        initialCommandSend();

        int step = SPWristbandConfigInfo.getTotalStep(mContext);
        ZLogger.d(D, "step: " + step);

        // Try to set step
        mApplicationLayer.SettingCmdStepTargetSetting(step);

        return waitCommandSend();
    }

    /**
     * Use to sync the target step
     *
     * @return the operate result
     */
    public boolean setTargetStep(long step) {
        ZLogger.d(D, "step: " + step);
        initialCommandSend();

        // Try to set step
        mApplicationLayer.SettingCmdStepTargetSetting(step);

        return waitCommandSend();
    }


    /**
     * use to sync the target sleep
     *
     * @param sleepMinute minute of all sleep
     * @return
     */
    public boolean setTargetSleep(int sleepMinute) {
        ZLogger.d(D, "sleepMinute: " + sleepMinute);
        initialCommandSend();

        // Try to set step
        mApplicationLayer.SettingCmdSleepTargetSetting(sleepMinute);

        return waitCommandSend();
    }

    /**
     * Use to set the phone os
     *
     * @return the operate result
     */
    public boolean setPhoneOS() {
        ZLogger.d(D, "SetPhoneOS");
        initialCommandSend();

        // Try to set step
        mApplicationLayer.SettingCmdPhoneOSSetting(ApplicationLayer.PHONE_OS_ANDROID);

        return waitCommandSend();
    }

    /**
     * Use to set the clocks
     *
     * @return the operate result
     */
    public boolean setClocks(ApplicationLayerAlarmsPacket alarms) {
        ZLogger.d(D, "SetClocks()");
        initialCommandSend();

        // Try to set alarms
        mApplicationLayer.SettingCmdAlarmsSetting(alarms);

        return waitCommandSend();
    }


    /**
     * Use to set the clock
     *
     * @return the operate result
     */
    public boolean setClock(ApplicationLayerAlarmPacket alarm) {
        ZLogger.d(D, "SetClocks()");
        initialCommandSend();
        ApplicationLayerAlarmsPacket alarms = new ApplicationLayerAlarmsPacket();
        alarms.add(alarm);
        // Try to set alarms
        mApplicationLayer.SettingCmdAlarmsSetting(alarms);

        return waitCommandSend();
    }

    /**
     * Use to sync the clock
     *
     * @return the operate result
     */
    public boolean setClocksSyncRequest() {
        ZLogger.d(D, "SetClocksSyncRequest()");
        initialCommandSend();

        isNeedWaitForResponse = true;

        mApplicationLayer.SettingCmdRequestAlarmList();

        return waitCommandSend();
    }

    /**
     * Use to sync the time
     *
     * @return the operate result
     */
    public boolean setTimeSync() {
        ZLogger.d(D, "SetTimeSync()");
        initialCommandSend();

        // Try to set time
        //in nexus9, Calendar's month is not right
        Calendar c1 = Calendar.getInstance();
        ZLogger.d(D, c1.toString());
        mApplicationLayer.SettingCmdTimeSetting(c1.get(Calendar.YEAR),
                c1.get(Calendar.MONTH) + 1, // here need add 1, because it origin range is 0 - 11;
                c1.get(Calendar.DATE),
                c1.get(Calendar.HOUR_OF_DAY),
                c1.get(Calendar.MINUTE),
                c1.get(Calendar.SECOND));
        return waitCommandSend();
    }

    /**
     * Use to send the call notify info
     *
     * @return the operate result
     */
    public boolean sendCallNotifyInfo() {
        ZLogger.d(D, "SendCallNotifyInfo");
        initialCommandSend();

        mApplicationLayer.NotifyCmdCallNotifyInfoSetting();

        return waitCommandSend();
    }

    public boolean sendCallNotifyInfo(String show) {
        ZLogger.d(D, "SendCallNotifyInfo");
        initialCommandSend();

        mApplicationLayer.NotifyCmdCallNotifyInfoSetting(show);

        return waitCommandSend();
    }

    /**
     * Use to send the call accept notify info
     *
     * @return the operate result
     */
    public boolean sendCallAcceptNotifyInfo() {
        ZLogger.d(D, "SendCallAcceptNotifyInfo");
        initialCommandSend();

        mApplicationLayer.NotifyCmdCallAcceptNotifyInfoSetting();

        return waitCommandSend();
    }

    /**
     * Use to send the call reject notify info
     *
     * @return the operate result
     */
    public boolean sendCallRejectNotifyInfo() {
        ZLogger.d(D, "SendCallRejectNotifyInfo");
        initialCommandSend();

        mApplicationLayer.NotifyCmdCallRejectNotifyInfoSetting();

        return waitCommandSend();
    }

    /**
     * Use to send the other notify info
     *
     * @return the operate result
     */
    public boolean sendOtherNotifyInfo(NotifyType type) {
//        ZLogger.d(D, "info: " + info);
//        initialCommandSend();
//
//        mApplicationLayer.NotifyCmdOtherNotifyInfoSetting(info);
//
//        return waitCommandSend();
        return sendOtherNotifyInfo(type, "");
    }

    /**
     * use to send other notify info
     *
     * @param type
     * @param show
     * @return
     */
    public boolean sendOtherNotifyInfo(NotifyType type, String show) {

        byte info;
        switch (type) {
            case QQ:
                info = ApplicationLayer.OTHER_NOTIFY_INFO_QQ;
                break;
            case SMS:
                info = ApplicationLayer.OTHER_NOTIFY_INFO_SMS;
                break;
            case LINE:
                info = ApplicationLayer.OTHER_NOTIFY_INFO_LINE;
                break;
            case SKYPE:
                info = ApplicationLayer.OTHER_NOTIFY_INFO_SKYPE;
                break;
            case VIBER:
                info = ApplicationLayer.OTHER_NOTIFY_INFO_VIBER;
                break;
            case WECHAT:
                info = ApplicationLayer.OTHER_NOTIFY_INFO_WECHAT;
                break;
            case TWITTER:
                info = ApplicationLayer.OTHER_NOTIFY_INFO_TWITTER;
                break;
            case FACEBOOK:
                info = ApplicationLayer.OTHER_NOTIFY_INFO_FACEBOOK;
                break;
            case LINKEDIN:
                info = ApplicationLayer.OTHER_NOTIFY_INFO_LINKEDIN;
                break;
            case WHATSAPP:
                info = ApplicationLayer.OTHER_NOTIFY_INFO_WHATSAPP;
                break;
            case INSTAGRAM:
                info = ApplicationLayer.OTHER_NOTIFY_INFO_INSTAGRAM;
                break;
            case KAKAOTALK:
                info = ApplicationLayer.OTHER_NOTIFY_INFO_KAKAOTALK;
                break;
            case MESSENGER:
                info = ApplicationLayer.OTHER_NOTIFY_INFO_MESSENGER;
                break;
            case VKONTAKTE:
                info = ApplicationLayer.OTHER_NOTIFY_INFO_VKONTAKTE;
                break;
            default:
                info = ApplicationLayer.OTHER_NOTIFY_INFO_SMS;
                break;
        }
        ZLogger.d(D, "info: " + info + "; showData: " + show);
        initialCommandSend();

        mApplicationLayer.NotifyCmdOtherNotifyInfoSetting(info, show);

        return waitCommandSend();
    }

    /**
     * usee to send request device support function
     *
     * @return
     */
    public boolean sendFunctionReq() {
        ZLogger.d(D, "sendFunctionReq");
        initialCommandSend();

        mApplicationLayer.SettingCmdRequestDeviceFunctionSetting();

        return waitCommandSend();
    }

    /**
     * Use to send enable fac test mode
     *
     * @return the operate result
     */
    public boolean sendEnableFacTest() {
        ZLogger.d(D, "SendEnableFacTest");
        if (mWristState != STATE_WRIST_INITIAL) {
            return false;
        }
        initialCommandSend();

        mApplicationLayer.FACCmdEnterTestMode(null);

        return waitCommandSend();
    }

    /**
     * use to setting device hour system
     *
     * @param is24Model
     * @return
     */
    public boolean setHourSystem(boolean is24Model) {
        ZLogger.d(D, "setHourSystem");
        initialCommandSend();

        mApplicationLayer.SettingCmdSettingHourSystem(is24Model ? ApplicationLayer.HOUR_24_MODEL : ApplicationLayer.HOUR_12_MODEL);

        return waitCommandSend();
    }

    /**
     * use to request hour system
     *
     * @return
     */
    public boolean sendHourSystemReq() {
        ZLogger.d(D, "sendHourSystemReq");
        initialCommandSend();

        mApplicationLayer.SettingCmdRequestHourSystemSetting();

        return waitCommandSend();
    }

    /**
     * use to set device unit system
     *
     * @param isMetric
     * @return
     */
    public boolean settingUnitSystem(boolean isMetric) {
        ZLogger.d(D, "settingUnitSystem isMetric = :" + isMetric);
        initialCommandSend();

        mApplicationLayer.SettingCmdSettingUnit(isMetric ? ApplicationLayer.UNIT_METRIC_SYSTEM : ApplicationLayer.UNIT_ENGLISH_SYSTEM);

        return waitCommandSend();
    }

    /**
     * use to send unit system request
     *
     * @return
     */
    public boolean sendUnitSystemReq() {
        ZLogger.d(D, "sendUnitSystemReq ");
        initialCommandSend();

        mApplicationLayer.SettingCmdRequestUnit();

        return waitCommandSend();
    }

    /**
     * use to setting disturb model
     *
     * @param packet
     * @return
     */
    public boolean settingDisturb(ApplicationLayerDisturbPacket packet) {
        ZLogger.d(D, "settingDisturb " + packet.toString());
        initialCommandSend();

        mApplicationLayer.SettingCmdSettingDisturbSetting(packet);

        return waitCommandSend();
    }

    /**
     * use to send disturb setting request
     *
     * @return
     */
    public boolean sendDisturbSettingReq() {
        ZLogger.d(D, "sendDisturbSettingReq ");
        initialCommandSend();

        mApplicationLayer.SettingCmdRequestDisturbSetting();

        return waitCommandSend();
    }

    /**
     * use to setting screen light duration
     *
     * @param duration
     * @return
     */
    public boolean settingScreenLightDuration(int duration) {
        ZLogger.d(D, "settingScreenLightDuration duration= " + duration);
        initialCommandSend();

        mApplicationLayer.SettingCmdSettingScreenLightDuration(duration);

        return waitCommandSend();
    }

    /**
     * use to send screen light duration req
     *
     * @return
     */
    public boolean sendScreenLightDurationReq() {
        ZLogger.d(D, "sendScreenLightDurationReq ");
        initialCommandSend();

        mApplicationLayer.SettingCmdRequestScreenLightDurationSetting();

        return waitCommandSend();
    }

    /**
     * use to setting device language
     *
     * @param language
     * @return
     */
    public boolean settingLanguage(DeviceLanguage language) {
        ZLogger.d(D, "settingLanguage " + language);
        initialCommandSend();

        mApplicationLayer.SettingCmdSettingLanguage(language);

        return waitCommandSend();
    }

    /**
     * use to send device language request
     *
     * @return
     */
    public boolean sendDeviceLanguageReq() {
        ZLogger.d(D, "sendDeviceLanguageReq ");
        initialCommandSend();

        mApplicationLayer.SettingCmdRequestLanguage();

        return waitCommandSend();
    }


    /**
     * Use to send disable fac test mode
     *
     * @return the operate result
     */
    public boolean sendDisableFacTest() {
        ZLogger.d(D, "SendDisableFacTest");
        if (mWristState != STATE_WRIST_ENTER_TEST_MODE) {
            return false;
        }
        initialCommandSend();

        mApplicationLayer.FACCmdExitTestMode(null);

        return waitCommandSend();
    }

    /**
     * Use to send enable led
     *
     * @param led the led want to enable
     * @return the operate result
     */
    public boolean sendEnableFacLed(byte led) {
        ZLogger.d(D, "SendEnableFacLed");
        initialCommandSend();

        mApplicationLayer.FACCmdEnableLed(led);

        return waitCommandSend();
    }

    /**
     * Use to send enable vibrate
     *
     * @return the operate result
     */
    public boolean sendEnableFacVibrate() {
        ZLogger.d(D, "SendEnableFacVibrate");
        initialCommandSend();

        mApplicationLayer.FACCmdEnableVibrate();

        return waitCommandSend();
    }

    /**
     * Use to send request sensor data
     *
     * @return the operate result
     */
    public boolean sendEnableFacSensorDataRequest() {
        ZLogger.d(D, "SendEnableFacSensorData");
        initialCommandSend();

        mApplicationLayer.FACCmdRequestSensorData();

        return waitCommandSend();
    }

    /**
     * Use to send open log command
     *
     * @return the operate result
     */
    public boolean sendLogEnableCommand(byte[] keyArray) {
        ZLogger.d(D, "SendLogEnableCommand");
        initialCommandSend();

        mApplicationLayer.LogCmdOpenLog(keyArray);

        return waitCommandSend();
    }

    public boolean sendLogEnableCommand() {
        int cnt = 0;
        byte[] temp = new byte[DEBUG_LOG_TYPE_MAX_CNT];
        if (SPWristbandConfigInfo.getDebugLogTypeModuleApp(mContext)) {
            temp[cnt] = ApplicationLayer.DEBUG_LOG_TYPE_MODULE_APP;
            cnt++;
        }
        if (SPWristbandConfigInfo.getDebugLogTypeModuleUpperStack(mContext)) {
            temp[cnt] = ApplicationLayer.DEBUG_LOG_TYPE_MODULE_UPSTACK;
            cnt++;
        }
        if (SPWristbandConfigInfo.getDebugLogTypeModuleLowerStack(mContext)) {
            temp[cnt] = ApplicationLayer.DEBUG_LOG_TYPE_MODULE_LOWERSTACK;
            cnt++;
        }
        if (SPWristbandConfigInfo.getDebugLogTypeSleep(mContext)) {
            temp[cnt] = ApplicationLayer.DEBUG_LOG_TYPE_SLEEP_DATA;
            cnt++;
        }
        if (SPWristbandConfigInfo.getDebugLogTypeSport(mContext)) {
            temp[cnt] = ApplicationLayer.DEBUG_LOG_TYPE_SPORT_DATA;
            cnt++;
        }
        if (SPWristbandConfigInfo.getDebugLogTypeConfig(mContext)) {
            temp[cnt] = ApplicationLayer.DEBUG_LOG_TYPE_CONFIG_DATA;
            cnt++;
        }
        if (cnt == 0) {
            ZLogger.i(D, "No need to enable log.");
            return true;
        }
        byte[] keyArray = new byte[cnt];
        System.arraycopy(temp, 0, keyArray, 0, cnt);

        return sendLogEnableCommand(keyArray);
    }

    /**
     * Use to send open log command
     *
     * @return the operate result
     */
    public boolean sendLogCloseCommand() {
        ZLogger.d(D, "SendLogCloseCommand");
        initialCommandSend();

        mApplicationLayer.LogCmdCloseLog();

        return waitCommandSend();
    }

    /**
     * Use to send request log command
     *
     * @return the operate result
     */
    public boolean sendLogRequestCommand(byte key) {
        ZLogger.d(D, "SendLogRequestCommand");
        initialCommandSend();

        mApplicationLayer.LogCmdRequestLog(key);

        return waitCommandSend();
    }

    /**
     * Use to send sync today step command
     *
     * @return the operate result
     */
    public boolean sendSyncTodayNearlyOffsetStepCommand() {
        ZLogger.d(D, "SendSyncTodayNearlyOffsetStepCommand");
        initialCommandSend();
        Calendar c1 = Calendar.getInstance();
//        List<SportData> sports = mGlobalGreenDAO.loadSportDataByDate(c1.get(Calendar.YEAR),
//                c1.get(Calendar.MONTH) + 1,// here need add 1, because it origin range is 0 - 11;
//                c1.get(Calendar.DATE));

//        SportData subData = WristbandCalculator.getNearlyOffsetStepData(sports);

        ApplicationLayerRecentlySportPacket packet;
//        if (subData != null) {
//            packet = new ApplicationLayerRecentlySportPacket((byte) (subData.getMode() & 0xff)
//                    , subData.getActiveTime()
//                    , subData.getCalory()
//                    , subData.getStepCount()
//                    , subData.getDistance());
//        } else {
        packet = new ApplicationLayerRecentlySportPacket((byte) 0x00
                , 0
                , 0
                , 0
                , 0);
//        }
        // Try to sync total step data
        mApplicationLayer.SportDataCmdSyncRecently(packet);

        return waitCommandSend();
    }

    /**
     * Use to send remove bond command
     *
     * @return the operate result
     */
    public boolean sendRemoveBondCommand() {
        ZLogger.d(D, "SendRemoveBondCommand");
        initialCommandSend();

        mApplicationLayer.BondCmdRequestRemoveBond();

        // when send remove bond command, we just think link is lost.
        isConnected = false;
        return waitCommandSend();
    }

    /**
     * Use to send camera control command
     *
     * @return the operate result
     */
    public boolean sendCameraControlCommand(boolean enable) {
        ZLogger.d(D, "enable: " + enable);
        initialCommandSend();

        mApplicationLayer.ControlCmdCameraControl(
                enable ? ApplicationLayer.CAMERA_CONTROL_APP_IN_FORE : ApplicationLayer.CAMERA_CONTROL_APP_IN_BACK);

        return waitCommandSend();
    }

    /**
     * Use to send sync today step command
     *
     * @return the operate result
     */
    public boolean sendSyncTodayStepCommand() {
        ZLogger.d(D, "SendSyncTodayStepCommand");
        initialCommandSend();
        Calendar c1 = Calendar.getInstance();
//        List<SportData> sports = mGlobalGreenDAO.loadSportDataByDate(c1.get(Calendar.YEAR),
//                c1.get(Calendar.MONTH) + 1,// here need add 1, because it origin range is 0 - 11;
//                c1.get(Calendar.DATE));

//        SportSubData subData = WristbandCalculator.sumOfSportDataByDate(c1.get(Calendar.YEAR),
//                c1.get(Calendar.MONTH) + 1,// here need add 1, because it origin range is 0 - 11;
//                c1.get(Calendar.DATE),
//                sports);
        ApplicationLayerTodaySportPacket packet;
//        if (subData != null) {
//            packet = new ApplicationLayerTodaySportPacket((long) subData.getStepCount()
//                    , (long) subData.getDistance()
//                    , (long) subData.getCalory());
//        } else {
        packet = new ApplicationLayerTodaySportPacket(0
                , 0
                , 0);
//        }
        // Try to sync total step data
        mApplicationLayer.SportDataCmdSyncToday(packet);

        return waitCommandSend();
    }

    public boolean isInSendCommand() {
        return this.isInSendCommand;
    }

    private boolean initialCommandSend() {
        ZLogger.d(D, "initialCommandSend()");
        // Here we need do more thing for queue send command, current version didn't fix it.

        while (isInSendCommand || isNeedWaitForResponse) {
            if (!isConnect()) {
                return false;
            }
            ZLogger.d(D, "Wait for last command send ok. isInSendCommand: " + isInSendCommand + ", isNeedWaitForResponse: " + isNeedWaitForResponse);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        synchronized (mCommandSendLock) {
            // initial status
            mErrorStatus = false;
            isCommandSend = false;
            isCommandSendOk = false;

            isInSendCommand = true;
        }

        return true;
    }

    private boolean waitCommandSend() {
        ZLogger.d(D, "waitCommandSend()");
        boolean commendSendReady = false;
        synchronized (mCommandSendLock) {
            if (!isCommandSend) {
                try {
                    // wait a while
                    ZLogger.d(D, "wait the time set callback, wait for: " + MAX_COMMAND_SEND_WAIT_TIME + "ms");
                    mCommandSendLock.wait(MAX_COMMAND_SEND_WAIT_TIME);

                    ZLogger.d(D, "isCommandSendOk=" + isCommandSendOk + ", isInSendCommand=" + isInSendCommand);
                    isInSendCommand = false;

                    commendSendReady = isCommandSendOk;
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return commendSendReady;
    }


    // Login response
    private boolean mLoginResponse;

    private int mLoginResponseStatus;

    /**
     * Request Login
     *
     * @return the login result, fail or success
     * @id the user id
     */
    public boolean requestLogin(String id) {
        ZLogger.d(D, "id: " + id);
        // initial error status
        mErrorStatus = false;
        isResponseCome = false;
        mLoginResponse = false;
        mLoginResponseStatus = LOGIN_RSP_SUCCESS;


        // Try to login
        mApplicationLayer.BondCmdRequestLogin(id);// it will wait the onBondCmdRequestLogin callback invoke.

        synchronized (mRequestResponseLock) {
            if (!isResponseCome) {
                try {
                    // wait a while
                    ZLogger.d(D, "wait the login response come, wait for: " + MAX_REQUEST_RESPONSE_TRANSACTION_WAIT_TIME + "ms");
                    mRequestResponseLock.wait(MAX_REQUEST_RESPONSE_TRANSACTION_WAIT_TIME);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        if (!isResponseCome) {
            mErrorStatus = true;
            return false;
        }

        if (mLoginResponse) {
            setDataSync(false);
        }

        if (isResponseCome && (mLoginResponseStatus == ApplicationLayer.LOGIN_LOSS_LOGIN_INFO)) {
            //
            ZLogger.w(D, "Be-careful, may be last connection loss sync info, do again.");
            requestSetNeedInfo();
            // update state
            updateWristState(STATE_WRIST_LOGIN);
        }
        return mLoginResponse;
    }

    // Login response
    private boolean mBondResponse;

    /**
     * Request Bond
     *
     * @return the bond result, fail or success
     * @id the user id
     */
    public boolean requestBond(String id) {
        ZLogger.d(D, "id: " + id);
        // initial error status
        mErrorStatus = false;
        isResponseCome = false;
        mBondResponse = false;

        // Try to login
        mApplicationLayer.BondCmdRequestBond(id);// it will wait the onBondCmdRequestLogin callback invoke.

        synchronized (mRequestResponseLock) {
            if (!isResponseCome) {
                try {
                    // wait a while
                    ZLogger.d(D, "wait the bond response come, wait for: " + MAX_REQUEST_RESPONSE_TRANSACTION_WAIT_TIME + "ms");
                    mRequestResponseLock.wait(MAX_REQUEST_RESPONSE_TRANSACTION_WAIT_TIME);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        if (!isResponseCome) {
            mErrorStatus = true;
            return false;
        }
        return mBondResponse;
    }

    public boolean isReady() {
        ZLogger.d(D, "mWristState: " + mWristState);
        return mWristState == STATE_WRIST_SYNC_DATA;
    }

    /**
     * Use to set the remote Immediate Alert Level
     *
     * @param enable enable/disable Immediate Alert
     */
    public boolean enableImmediateAlert(boolean enable) {
        initialCommandSend();
        boolean result = mImmediateAlertService.setAlertLevelEnabled(mDeviceAddress, enable);
        isInSendCommand = false;
        return result;
    }

    /**
     * Use to set the remote Link Loss Alert Level
     *
     * @param enable enable/disable Link Loss Alert
     */
    public boolean enableLinkLossAlert(boolean enable) {
        initialCommandSend();
        boolean result = mLinkLossService.setAlertEnabled(enable);
        isInSendCommand = false;
        return result;
    }

    /**
     * Use to read the remote Battery Level
     */
    public boolean readBatteryLevel() {
        ZLogger.d(D, "readBatteryLevel");
        //isInSendCommand = true;
        initialCommandSend();

        boolean result = mBatteryService.readBatteryLevel();

        isInSendCommand = false;
        return result;
    }

    /**
     * Use to read the remote Hrp Level
     */
    public boolean readHrpValue() {
        ZLogger.d(D, "readHrpValue");
        initialCommandSend();

        mApplicationLayer.SportDataCmdHrpSingleRequest(true);

        return waitCommandSend();
    }

    /**
     * Use to read the classic bt address
     */
    public boolean requestClassicAddress() {
        ZLogger.d(D, "requestClassicAddress");
        initialCommandSend();

        mApplicationLayer.RequestClassicAddress();

        return waitCommandSend();
    }

    /**
     * Use to read the classic bt status
     */
    public boolean requestClassicStatus() {
        ZLogger.d(D, "requestClassicStatus");
        initialCommandSend();

        mApplicationLayer.RequestClassicStatus();

        return waitCommandSend();
    }

    /**
     * Use to read the remote Hrp Level
     */
    public boolean readBpValue() {
        ZLogger.d(D, "readBpValue");
        initialCommandSend();

        mApplicationLayer.SportDataCmdBpSingleRequest(true);

        return waitCommandSend();
    }

    /**
     * Use to read the remote Hrp Level
     */
    public boolean stopReadHrpValue() {
        ZLogger.d(D, "stopReadHrpValue");
        //isInSendCommand = true;
        initialCommandSend();

        mApplicationLayer.SportDataCmdHrpSingleRequest(false);

        return waitCommandSend();
    }

    /**
     * Use to read the remote bp level
     */
    public boolean stopReadBpValue() {
        ZLogger.d(D, "stopReadBpValue");
        //isInSendCommand = true;
        initialCommandSend();

        mApplicationLayer.SportDataCmdBpSingleRequest(false);

        return waitCommandSend();
    }


    /**
     * Use to read the remote Link loss Level
     */
    public boolean readLinkLossLevel() {
        ZLogger.d(D, "readLinkLossLevel");
        initialCommandSend();

        if (!mLinkLossService.readAlertLevel()) {
            ZLogger.w(D, "readLinkLossLevel, failed");
            isInSendCommand = false;

            return false;
        }

        return waitCommandSend();
    }

    /**
     * Use to read the remote version info
     */
    public boolean readDfuVersion() {
        ZLogger.d(D, "readDfuVersion");
        initialCommandSend();

        if (!mDfuService.readInfo()) {
            ZLogger.e(D, "readDfuVersion, failed");
            isInSendCommand = false;
            return false;
        }

        return waitCommandSend();
    }

    public int[] readSavedDfuVersion() {
        if ((mAppVersion != -1) && (mPatchVersion != -1)) {
            int[] dfuVersin = new int[2];
            dfuVersin[0] = mAppVersion;
            dfuVersin[1] = mPatchVersion;
            return dfuVersin;
        } else {
            return null;
        }
    }

    public void setDfuVersion(int appVersion, int patchVersion) {
        mAppVersion = appVersion;
        mPatchVersion = patchVersion;
    }

    /**
     * Use to get the remote Battery Level
     */
    public int getBatteryLevel() {
        if (mBatteryService != null) {
            return mBatteryService.getBatteryValue();
        } else {
            return -1;
        }
    }

    /**
     * Use to check support extend flash
     */
    public boolean checkSupportedExtendFlash() {
        if (mDfuService != null) {
            return mDfuService.checkSupportedExtendFlash();
        } else {
            return false;
        }
    }


    /**
     * Use to enable battery power notification
     */
    public boolean enableBatteryNotification(boolean enable) {
        ZLogger.e(D, "Attempt to enableBatteryNotification: " + enable);

        initialCommandSend();
        // enable notification
        boolean result = mBatteryService.setNotificationEnabled(enable);
        isInSendCommand = false;
        return result;
    }

    // The Handler that gets information back from test thread
    //private class MyHandler extends Handler {
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_STATE_CONNECTED:
                    isConnected = true;
                    synchronized (mCallbacks) {
                        for (WristbandManagerCallback callback : mCallbacks) {
                            callback.onConnectionStateChange(true);
                        }
                    }
                    break;
                case MSG_STATE_DISCONNECTED:
                    isConnected = false;
                    synchronized (mCallbacks) {
                        // do something
                        for (WristbandManagerCallback callback : mCallbacks) {
                            callback.onConnectionStateChange(false);
                        }
                    }
                    close();
                    break;
                case MSG_ERROR:
                    synchronized (mCallbacks) {
                        // do something
                        for (WristbandManagerCallback callback : mCallbacks) {
                            callback.onError(msg.arg1);
                        }
                    }
                    break;
                case MSG_WRIST_STATE_CHANGED:
                    ZLogger.d(D, "MSG_WRIST_STATE_CHANGED, current state: " + msg.arg1);
                    synchronized (mCallbacks) {
                        for (WristbandManagerCallback callback : mCallbacks) {
                            callback.onLoginStateChange(msg.arg1);
                        }
                    }
                    break;
                case MSG_RECEIVE_STEP_INFO:
                    ApplicationLayerStepPacket step = (ApplicationLayerStepPacket) msg.obj;
                    synchronized (mCallbacks) {
                        for (WristbandManagerCallback callback : mCallbacks) {
                            callback.onStepDataReceiveIndication(step);
                        }
                    }
                    break;
                case MSG_RECEIVE_SPORT_INFO:
                    ApplicationLayerSportPacket sport = (ApplicationLayerSportPacket) msg.obj;
                    synchronized (mCallbacks) {
                        for (WristbandManagerCallback callback : mCallbacks) {
                            callback.onSportDataReceiveIndication(sport);
                        }
                    }
                    break;
                case MSG_RECEIVE_SLEEP_INFO:
                    ApplicationLayerSleepPacket sleep = (ApplicationLayerSleepPacket) msg.obj;
                    synchronized (mCallbacks) {
                        for (WristbandManagerCallback callback : mCallbacks) {
                            callback.onSleepDataReceiveIndication(sleep);
                        }
                    }
                    break;
                case MSG_RECEIVE_NOTIFY_MODE_SETTING:
                    ReminderFunction reminderFunction = (ReminderFunction) msg.obj;
//                    ZLogger.w(D, "Current notify setting is: " + mode);
//                    SPWristbandConfigInfo.setNotifyCallFlag(mContext, (mode & ApplicationLayer.NOTIFY_SWITCH_SETTING_CALL) != 0);
//                    SPWristbandConfigInfo.setNotifyMessageFlag(mContext, (mode & ApplicationLayer.NOTIFY_SWITCH_SETTING_MESSAGE) != 0);
//
//                    if (isNotifyManageEnabled()) {
//                        SPWristbandConfigInfo.setNotifyQQFlag(mContext, (mode & ApplicationLayer.NOTIFY_SWITCH_SETTING_QQ) != 0);
//                        SPWristbandConfigInfo.setNotifyWechatFlag(mContext, (mode & ApplicationLayer.NOTIFY_SWITCH_SETTING_WECHAT) != 0);
//                    } else {
//                        ZLogger.w(D, "Notify not enable, should not enable these setting.");
//                        SPWristbandConfigInfo.setNotifyQQFlag(mContext, false);
//                        SPWristbandConfigInfo.setNotifyWechatFlag(mContext, false);
//                    }
                    synchronized (mRequestResponseLock) {
                        isResponseCome = true;
                        isNeedWaitForResponse = false;
                        mRequestResponseLock.notifyAll();
                    }
                    synchronized (mCallbacks) {
                        for (WristbandManagerCallback callback : mCallbacks) {
                            callback.onNotifyModeSettingReceive(reminderFunction);
                        }
                    }
                    break;
                case MSG_RECEIVE_LONG_SIT_SETTING:
                    boolean isOpen = (boolean) msg.obj;
                    ZLogger.w(D, "Current long sit setting is: " + isOpen);
//                    SPWristbandConfigInfo.setControlSwitchLongSit(mContext, longSitMode == ApplicationLayer.LONG_SIT_CONTROL_ENABLE);
                    synchronized (mRequestResponseLock) {
                        isResponseCome = true;
                        isNeedWaitForResponse = false;
                        mRequestResponseLock.notifyAll();
                    }
                    synchronized (mCallbacks) {
                        for (WristbandManagerCallback callback : mCallbacks) {
                            callback.onLongSitSettingReceive(isOpen);
                        }
                    }
                    break;
                case MSG_RECEIVE_TURN_OVER_WRIST_SETTING:
                    boolean enable = (boolean) msg.obj;
                    ZLogger.w(D, "Current turn over wrist setting is: " + enable);
                    SPWristbandConfigInfo.setControlSwitchTurnOverWrist(mContext, enable);
                    synchronized (mRequestResponseLock) {
                        isResponseCome = true;
                        isNeedWaitForResponse = false;
                        mRequestResponseLock.notifyAll();
                    }
                    synchronized (mCallbacks) {
                        for (WristbandManagerCallback callback : mCallbacks) {
                            callback.onTurnOverWristSettingReceive(enable);
                        }
                    }
                    break;
                case MSG_RECEIVE_ALARMS_INFO:
                    SPWristbandConfigInfo.setAlarmControlOne(mContext, false);
                    SPWristbandConfigInfo.setAlarmControlTwo(mContext, false);
                    SPWristbandConfigInfo.setAlarmControlThree(mContext, false);

                    ApplicationLayerAlarmsPacket alarmPacket = (ApplicationLayerAlarmsPacket) msg.obj;
                    for (final ApplicationLayerAlarmPacket p : alarmPacket.getAlarms()) {
                        byte dayFlag = p.getDayFlags();
                        String hour = String.valueOf(p.getHour()).length() == 1
                                ? "0" + String.valueOf(p.getHour())
                                : String.valueOf(p.getHour());
                        String minute = String.valueOf(p.getMinute()).length() == 1
                                ? "0" + String.valueOf(p.getMinute())
                                : String.valueOf(p.getMinute());
                        String timeString = hour + ":" + minute;
                        // Set check
                        if (p.getId() == 0) {
                            SPWristbandConfigInfo.setAlarmControlOne(mContext, true);
                            SPWristbandConfigInfo.setAlarmTimeOne(mContext, timeString);
                            SPWristbandConfigInfo.setAlarmFlagOne(mContext, dayFlag);
                        } else if (p.getId() == 1) {
                            SPWristbandConfigInfo.setAlarmControlTwo(mContext, true);
                            SPWristbandConfigInfo.setAlarmTimeTwo(mContext, timeString);
                            SPWristbandConfigInfo.setAlarmFlagTwo(mContext, dayFlag);
                        } else {
                            SPWristbandConfigInfo.setAlarmControlThree(mContext, true);
                            SPWristbandConfigInfo.setAlarmTimeThree(mContext, timeString);
                            SPWristbandConfigInfo.setAlarmFlagThree(mContext, dayFlag);
                        }
                        //mCallback.onSleepDataReceive(sleepData);
                    }
                    synchronized (mRequestResponseLock) {
                        isResponseCome = true;
                        isNeedWaitForResponse = false;
                        mRequestResponseLock.notifyAll();
                    }
                    synchronized (mCallbacks) {
                        for (WristbandManagerCallback callback : mCallbacks) {
                            callback.onAlarmsDataReceive(alarmPacket);
                        }
                    }
                    break;
                case MSG_RECEIVE_TAKE_PHOTO_RSP:
                    ZLogger.d(D, "MSG_RECEIVE_TAKE_PHOTO_RSP");
                    synchronized (mCallbacks) {
                        // do something
                        for (WristbandManagerCallback callback : mCallbacks) {
                            callback.onTakePhotoRsp();
                        }
                    }
                    break;
                case MSG_RECEIVE_FAC_SENSOR_INFO:
                    ZLogger.d(D, "MSG_RECEIVE_FAC_SENSOR_INFO");
                    ApplicationLayerFacSensorPacket sensorPacket = (ApplicationLayerFacSensorPacket) msg.obj;
                    ZLogger.d(D, "Receive Fac Sensor info, X: " + sensorPacket.getX() +
                            ", Y: " + sensorPacket.getY() +
                            ", Z: " + sensorPacket.getZ());
                    // show state
                    synchronized (mCallbacks) {
                        // do something
                        for (WristbandManagerCallback callback : mCallbacks) {
                            callback.onFacSensorDataReceive(sensorPacket);
                        }
                    }
                    break;
                case MSG_RECEIVER_END_CALL:
                    synchronized (mCallbacks) {
                        // do something
                        for (WristbandManagerCallback callback : mCallbacks) {
                            callback.onEndCall();
                        }
                    }
                    break;
                case MSG_RECEIVE_LOG_START:
                    ZLogger.d(D, "MSG_RECEIVE_LOG_START");
                    long logLength = (long) msg.obj;

                    // show state
                    synchronized (mCallbacks) {
                        // do something
                        for (WristbandManagerCallback callback : mCallbacks) {
                            callback.onLogCmdStart(logLength);
                        }
                    }
                    break;
                case MSG_RECEIVE_LOG_END:
                    ZLogger.d(D, "MSG_RECEIVE_LOG_END");
                    // show state
                    synchronized (mCallbacks) {
                        // do something
                        for (WristbandManagerCallback callback : mCallbacks) {
                            callback.onLogCmdEnd();
                        }
                    }
                    break;
                case MSG_RECEIVE_LOG_RSP:
                    ZLogger.d(D, "MSG_RECEIVE_LOG_RSP");
                    ApplicationLayerLogResponsePacket logResponsePacket = (ApplicationLayerLogResponsePacket) msg.obj;
                    // show state
                    synchronized (mCallbacks) {
                        // do something
                        for (WristbandManagerCallback callback : mCallbacks) {
                            callback.onLogCmdRsp(logResponsePacket);
                        }
                    }
                    break;
                case MSG_RECEIVE_DFU_VERSION_INFO:
                    ZLogger.d(D, "MSG_RECEIVE_DFU_VERSION_INFO");
                    int appVersion = msg.arg1;
                    int patchVersion = msg.arg2;
                    ZLogger.d(D, "Receive dfu version info, appVersion: " + appVersion +
                            ", patchVersion: " + patchVersion);
                    // show state
                    synchronized (mCallbacks) {
                        // do something
                        for (WristbandManagerCallback callback : mCallbacks) {
                            callback.onVersionRead(appVersion, patchVersion);
                        }
                    }
                    break;
                case MSG_RECEIVE_DEVICE_NAME_INFO:
                    ZLogger.d(D, "MSG_RECEIVE_DEVICE_NAME_INFO");
                    String name = (String) msg.obj;
                    ZLogger.d(D, "Receive device name info, name: " + name);
                    // show state
                    synchronized (mCallbacks) {
                        // do something
                        for (WristbandManagerCallback callback : mCallbacks) {
                            callback.onNameRead(name);
                        }
                    }
                    break;
                case MSG_RECEIVE_BATTERY_INFO:
                    ZLogger.d(D, "MSG_RECEIVE_BATTERY_INFO");
                    int battery = msg.arg1;
                    synchronized (mCallbacks) {
                        for (WristbandManagerCallback callback : mCallbacks) {
                            callback.onBatteryRead(battery);
                        }
                    }
                    break;
                case MSG_RECEIVE_BATTERY_CHANGE_INFO:
                    ZLogger.d(D, "MSG_RECEIVE_BATTERY_CHANGE_INFO");
                    int batteryChange = msg.arg1;
                    synchronized (mCallbacks) {
                        for (WristbandManagerCallback callback : mCallbacks) {
                            callback.onBatteryChange(batteryChange);
                        }
                    }
                    break;
                case MSG_RECEIVE_HRP_INFO:
                    ZLogger.d(D, "MSG_RECEIVE_HRP_INFO");
                    ApplicationLayerHrpPacket hrpPacket = (ApplicationLayerHrpPacket) msg.obj;
                    synchronized (mCallbacks) {
                        for (WristbandManagerCallback callback : mCallbacks) {
                            callback.onHrpDataReceiveIndication(hrpPacket);
                        }
                    }
                    break;
                case MSG_RECEIVE_HRP_DEVICE_CANCEL_SINGLE_READ:
                    ZLogger.d(D, "MSG_RECEIVE_HRP_DEVICE_CANCEL_SINGLE_READ");

                    synchronized (mCallbacks) {
                        for (WristbandManagerCallback callback : mCallbacks) {
                            callback.onDeviceCancelSingleHrpRead();
                        }
                    }
                    break;
                case MSG_RECEIVE_HRP_CONTINUE_PARAM_RSP:
                    ZLogger.d(D, "MSG_RECEIVE_HRP_CONTINUE_PARAM_RSP");
                    synchronized (mCallbacks) {
                        for (WristbandManagerCallback callback : mCallbacks) {
                            callback.onDeviceCancelSingleHrpRead();
                        }
                    }
                    break;
                case MSG_RECEIVER_SPORT_FUNCTION:
                    SportFunction sportFunction = (SportFunction) msg.obj;
                    synchronized (mCallbacks) {
                        for (WristbandManagerCallback callback : mCallbacks) {
                            callback.onSportFunction(sportFunction);
                        }
                    }
                    break;
                case MSG_RECEIVER_REMINDER_FUNCTION:
                    ReminderFunction reminder = (ReminderFunction) msg.obj;
                    synchronized (mCallbacks) {
                        for (WristbandManagerCallback callback : mCallbacks) {
                            callback.onReminderFunction(reminder);
                        }
                    }
                    break;
                case MSG_RECEIVER_DEVICE_FUNCTION:
                    DeviceFunction deviceFunction = (DeviceFunction) msg.obj;
                    synchronized (mCallbacks) {
                        for (WristbandManagerCallback callback : mCallbacks) {
                            callback.onDeviceFunction(deviceFunction);
                        }
                    }
                    break;
                case MSG_RECEIVER_HOUR_SYSTEM:
                    synchronized (mCallbacks) {
                        for (WristbandManagerCallback callback : mCallbacks) {
                            callback.onHour((boolean) msg.obj);
                        }
                    }
                    break;
                case MSG_RECEIVER_UNIT_SYSTEM:
                    synchronized (mCallbacks) {
                        for (WristbandManagerCallback callback : mCallbacks) {
                            callback.onUnit((boolean) msg.obj);
                        }
                    }
                    break;
                case MSG_RECEIVER_DISTURB:
                    boolean openStatus = (boolean) msg.obj;
                    synchronized (mCallbacks) {
                        for (WristbandManagerCallback callback : mCallbacks) {
                            callback.onDisturb(openStatus);
                        }
                    }
                    break;
                case MSG_RECEIVER_SCREEN_LIGHT_DURATION:
                    synchronized (mCallbacks) {
                        for (WristbandManagerCallback callback : mCallbacks) {
                            callback.onScreenLightDuration((int) msg.obj);
                        }
                    }
                    break;
                case MSG_RECEIVER_LANGUAGE:
                    DeviceLanguage language = (DeviceLanguage) msg.obj;
                    synchronized (mCallbacks) {
                        for (WristbandManagerCallback callback : mCallbacks) {
                            callback.onLanguage(language);
                        }
                    }
                    break;
                case MSG_RECEIVER_BP_INFO:
                    ApplicationLayerBpPacket bpPacket = (ApplicationLayerBpPacket) msg.obj;
                    synchronized (mCallbacks) {
                        for (WristbandManagerCallback callback : mCallbacks) {
                            callback.onBpDataReceiveIndication(bpPacket);
                        }
                    }
                    break;
                case MSG_RECEIVE_BP_DEVICE_CANCEL_SINGLE_READ:
                    synchronized (mCallbacks) {
                        for (WristbandManagerCallback callback : mCallbacks) {
                            callback.onDeviceCancelSingleBpRead();
                        }
                    }
                    break;
                case MSG_RECEIVE_CLASSIC_ADDRESS:
                    ZLogger.d(D, "MSG_RECEIVE_CLASSIC_ADDRESS");
                    String address = (String) msg.obj;
                    synchronized (mCallbacks) {
                        for (WristbandManagerCallback callback : mCallbacks) {
                            callback.onClassAddress(address);
                        }
                    }
                    break;
                case MSG_RECEIVE_CLASSIC_STATUS:
                    ZLogger.d(D, "MSG_RECEIVE_CLASSIC_STATUS");
                    int status = (int) msg.obj;
                    synchronized (mCallbacks) {
                        for (WristbandManagerCallback callback : mCallbacks) {
                            callback.onClassicStatus(status);
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    };


    private void initialApplicationCallback() {
        mApplicationCallback = new ApplicationLayerCallback() {
            @Override
            public void onConnectionStateChange(final boolean status, final boolean newState) {
                ZLogger.d(D, "status: " + status + ", newState: " + newState);
                // if already connect to the remote device, we can do more things here.
                if (status && newState) {
                    sendMessage(MSG_STATE_CONNECTED, null, -1, -1);
                } else {
                    sendMessage(MSG_STATE_DISCONNECTED, null, -1, -1);
                }
            }

            @Override
            public void onSettingCmdRequestAlarmList(final ApplicationLayerAlarmsPacket alarms) {
                ZLogger.d(D, "ApplicationLayerAlarmsPacket");
                sendMessage(MSG_RECEIVE_ALARMS_INFO, alarms, -1, -1);
            }

            @Override
            public void onSettingCmdRequestNotifySwitch(ReminderFunction reminderFunction) {
                ZLogger.d(D, "onSettingCmdRequestNotifySwitch");
                sendMessage(MSG_RECEIVE_NOTIFY_MODE_SETTING, reminderFunction, -1, -1);
            }

            @Override
            public void onSettingCmdRequestLongSit(boolean isOpen) {
                ZLogger.d(D, "onSettingCmdRequestLongSit");
                sendMessage(MSG_RECEIVE_LONG_SIT_SETTING, isOpen, -1, -1);
            }

            @Override
            public void onTurnOverWristSettingReceive(final boolean mode) {
                ZLogger.d(D, "onTurnOverWristSettingReceive");
                sendMessage(MSG_RECEIVE_TURN_OVER_WRIST_SETTING, mode, -1, -1);
            }

            @Override
            public void onSportDataCmdSportData(ApplicationLayerSportPacket sport) {
                super.onSportDataCmdSportData(sport);
                ZLogger.d(D, "WristbandManager state is " + mWristState);
                ZLogger.d(D, "Receive a sport packet, Year: " + (sport.getYear() + 2000) +
                        ", Month: " + sport.getMonth() +
                        ", Day: " + sport.getDay() +
                        ", Item count: " + sport.getItemCount());
                if (mWristState != STATE_WRIST_SYNC_HISTORY_DATA) {
                    sendMessage(MSG_RECEIVE_SPORT_INFO, sport, -1, -1);
                }
            }

            @Override
            public void onSportDataCmdStepData(final ApplicationLayerStepPacket step) {
                ZLogger.d(D, "WristbandManager state is " + mWristState);
                ZLogger.d(D, "Receive a step packet, Year: " + (step.getYear() + 2000) +
                        ", Month: " + step.getMonth() +
                        ", Day: " + step.getDay() +
                        ", Item count: " + step.getItemCount());
                if (mWristState != STATE_WRIST_SYNC_HISTORY_DATA) {
                    sendMessage(MSG_RECEIVE_STEP_INFO, step, -1, -1);
                }
            }

            @Override
            public void onSportDataCmdSleepData(final ApplicationLayerSleepPacket sleep) {
                ZLogger.d(D, "onSportDataCmdSleepData");
                ZLogger.d(D, "Year: " + (sleep.getYear() + 2000) +
                        ", Month: " + sleep.getMonth() +
                        ", Day: " + sleep.getDay() +
                        ", Item count: " + sleep.getItemCount());
                sendMessage(MSG_RECEIVE_SLEEP_INFO, sleep, -1, -1);
            }

            @Override
            public void onSportDataCmdHistorySyncBegin() {
                ZLogger.d(D, "onSportDataCmdHistorySyncBegin");
                updateWristState(STATE_WRIST_SYNC_HISTORY_DATA);
            }

            @Override
            public void onSportDataCmdHistorySyncEnd(final ApplicationLayerTodaySumSportPacket packet) {
                ZLogger.d(D, "onSportDataCmdHistorySyncEnd");
                isNeedWaitForResponse = false;
                if (null != packet) {
                    ZLogger.d(D, "pakect.getOffset()" + packet.getOffset()
                            + ", pakect.getTotalStep()" + packet.getTotalStep()
                            + ", pakect.getTotalCalory()" + packet.getTotalCalory()
                            + ", pakect.getTotalDistance()" + packet.getTotalDistance());
                }
                updateWristState(STATE_WRIST_SYNC_DATA);
            }

            @Override
            public void onSportDataCmdHrpData(final ApplicationLayerHrpPacket hrp) {
                ZLogger.d(D, "onSportDataCmdHrpData");
                if (null != hrp) {
                    ZLogger.d(D, "Receive a hrp packet, Year: " + (hrp.getYear() + 2000) +
                            ", Month: " + hrp.getMonth() +
                            ", Day: " + hrp.getDay() +
                            ", Item count: " + hrp.getItemCount());
                }
                sendMessage(MSG_RECEIVE_HRP_INFO, hrp, -1, -1);
            }

            @Override
            public void onSportDataCmdDeviceCancelSingleHrpRead() {
                ZLogger.d(D, "onSportDataCmdDeviceCancelSingleHrpRead");
                sendMessage(MSG_RECEIVE_HRP_DEVICE_CANCEL_SINGLE_READ, null, -1, -1);
            }

            @Override
            public void onSportDataCmdHrpContinueParamRsp(boolean enable, int interval) {
                ZLogger.d(D, "enable: " + enable + ", interval: " + interval);
                SPWristbandConfigInfo.setContinueHrpControl(mContext, enable);
                sendMessage(MSG_RECEIVE_HRP_CONTINUE_PARAM_RSP, null, -1, -1);
            }

            @Override
            public void onTakePhotoRsp() {
                ZLogger.d(D, "onTakePhotoRsp");
                sendMessage(MSG_RECEIVE_TAKE_PHOTO_RSP, null, -1, -1);
            }

            @Override
            public void onFACCmdSensorData(final ApplicationLayerFacSensorPacket sensor) {
                ZLogger.d(D, "onFACCmdSensorData");
                sendMessage(MSG_RECEIVE_FAC_SENSOR_INFO, sensor, -1, -1);
            }

            @Override
            public void onBondCmdRequestBond(final byte status) {
                ZLogger.d(D, "status: " + status);
                // bond right
                if (status == ApplicationLayer.BOND_RSP_SUCCESS) {
                    mBondResponse = true;
                }
                // bond error
                else {
                    mBondResponse = false;
                }
                synchronized (mRequestResponseLock) {
                    isResponseCome = true;
                    isNeedWaitForResponse = false;
                    mRequestResponseLock.notifyAll();
                }
            }

            @Override
            public void onBondCmdRequestLogin(final byte status) {
                ZLogger.d(D, "status: " + status);
                // Login right
                if (status == LOGIN_RSP_SUCCESS
                        || status == ApplicationLayer.LOGIN_LOSS_LOGIN_INFO) {
                    mLoginResponseStatus = status;
                    mLoginResponse = true;
                }
                // Login error
                else {
                    mLoginResponse = false;
                }
                synchronized (mRequestResponseLock) {
                    isResponseCome = true;
                    isNeedWaitForResponse = false;
                    mRequestResponseLock.notifyAll();
                }
            }

            @Override
            public void onEndCallReceived() {
                ZLogger.d(D, "onEndCallReceived");
//                synchronized (mRejectCallLock) {
//                	isRejectCall = true;
//                }
//            endCall();

                sendMessage(MSG_RECEIVER_END_CALL, null, -1, -1);
            }

            @Override
            public void onLogCmdStart(final long logLength) {
                ZLogger.d(D, "onLogCmdStart");
                updateWristState(STATE_WRIST_SYNC_LOG_DATA);
                sendMessage(MSG_RECEIVE_LOG_START, logLength, -1, -1);
            }

            @Override
            public void onLogCmdEnd() {
                ZLogger.d(D, "onLogCmdEnd");
                updateWristState(STATE_WRIST_SYNC_DATA);
                sendMessage(MSG_RECEIVE_LOG_END, null, -1, -1);
            }

            @Override
            public void onLogCmdRsp(final ApplicationLayerLogResponsePacket packet) {
                ZLogger.d(D, "onLogCmdRsp");
                sendMessage(MSG_RECEIVE_LOG_RSP, packet, -1, -1);
            }

            @Override
            public void onCommandSend(final boolean status, byte command, byte key) {
                ZLogger.d(D, "status: " + status + ", command: " + command + ", key: " + key);
                // if command send not right(no ACK). we just close it, and think connection is wrong.
                // Or, we can try to reconnect, or do other things.
                if (!status) {
                    isCommandSendOk = false;
                    sendErrorMessage(ERROR_CODE_COMMAND_SEND_ERROR);
                    //mApplicationLayer.close(); // error
                } else {
                    isCommandSendOk = true;
                    if (command == ApplicationLayer.CMD_FACTORY_TEST) {
                        if (key == ApplicationLayer.KEY_FAC_TEST_ENTER_SPUER_KEY) {
                            updateWristState(STATE_WRIST_ENTER_TEST_MODE);
                        } else if (key == ApplicationLayer.KEY_FAC_TEST_LEAVE_SPUER_KEY) {
                            updateWristState(STATE_WRIST_INITIAL);
                        }
                    }
                }
                synchronized (mCommandSendLock) {
                    isCommandSend = true;
                    mCommandSendLock.notifyAll();
                }
            }


            @Override
            public void onNameReceive(final String data) {
                sendMessage(MSG_RECEIVE_DEVICE_NAME_INFO, data, -1, -1);
                synchronized (mCommandSendLock) {
                    isCommandSend = true;
                    mCommandSendLock.notifyAll();
                }
            }

            @Override
            public void onUpdateCmdRequestEnterOtaMode(byte status, byte errorcode) {
                super.onUpdateCmdRequestEnterOtaMode(status, errorcode);
            }

            @Override
            public void onSportDataCmdMoreData() {
                super.onSportDataCmdMoreData();
            }

            @Override
            public void onSportDataCmdSleepSetData(ApplicationLayerSleepPacket sleep) {
                super.onSportDataCmdSleepSetData(sleep);

            }

            @Override
            public void onSportFunction(SportFunction sportFunction) {
                super.onSportFunction(sportFunction);
                sendMessage(MSG_RECEIVER_SPORT_FUNCTION, sportFunction, -1, -1);
            }

            @Override
            public void onReminderFunction(ReminderFunction reminderFunction) {
                super.onReminderFunction(reminderFunction);
                sendMessage(MSG_RECEIVER_REMINDER_FUNCTION, reminderFunction, -1, -1);
            }

            @Override
            public void onDeviceFunction(DeviceFunction deviceFunction) {
                super.onDeviceFunction(deviceFunction);
                sendMessage(MSG_RECEIVER_DEVICE_FUNCTION, deviceFunction, -1, -1);
            }

            @Override
            public void onHour(boolean is24Model) {
                super.onHour(is24Model);
                sendMessage(MSG_RECEIVER_HOUR_SYSTEM, is24Model, -1, -1);
            }

            @Override
            public void onUnit(boolean isMetricSystem) {
                super.onUnit(isMetricSystem);
                sendMessage(MSG_RECEIVER_UNIT_SYSTEM, isMetricSystem, -1, -1);
            }

            @Override
            public void onDisturb(boolean isOpen) {
                super.onDisturb(isOpen);
                sendMessage(MSG_RECEIVER_DISTURB, isOpen, -1, -1);
            }

            @Override
            public void onScreenLightDuration(int duration) {
                super.onScreenLightDuration(duration);
                sendMessage(MSG_RECEIVER_SCREEN_LIGHT_DURATION, duration, -1, -1);
            }

            @Override
            public void onLanguage(DeviceLanguage language) {
                super.onLanguage(language);
                sendMessage(MSG_RECEIVER_LANGUAGE, language, -1, -1);
            }

            @Override
            public void onSportDataCmdBpData(ApplicationLayerBpPacket packet) {
                super.onSportDataCmdBpData(packet);
                sendMessage(MSG_RECEIVER_BP_INFO, packet, -1, -1);
            }

            @Override
            public void onSportDataCmdDeviceCancelSingleBpRead() {
                super.onSportDataCmdDeviceCancelSingleBpRead();
                sendMessage(MSG_RECEIVE_BP_DEVICE_CANCEL_SINGLE_READ, null, -1, -1);
            }

            @Override
            public void onClassAddress(String address) {
                super.onClassAddress(address);
                sendMessage(MSG_RECEIVE_CLASSIC_ADDRESS, address, -1, -1);
            }

            @Override
            public void onClassicStatus(int status) {
                super.onClassicStatus(status);
                sendMessage(MSG_RECEIVE_CLASSIC_STATUS, status, -1, -1);
            }
        };
    }

    private void sendErrorMessage(int error) {
        sendMessage(MSG_ERROR, null, error, -1);
    }


    private void updateWristState(int state) {
        // update the wrist state
        mWristState = state;
        sendMessage(MSG_WRIST_STATE_CHANGED, null, mWristState, -1);
    }

    /**
     * send message
     *
     * @param msgType Type message type
     * @param obj     object sent with the message set to null if not used
     * @param arg1    parameter sent with the message, set to -1 if not used
     * @param arg2    parameter sent with the message, set to -1 if not used
     **/
    private void sendMessage(int msgType, Object obj, int arg1, int arg2) {
        if (mHandler != null) {
            //	Message msg = new Message();
            Message msg = Message.obtain();
            msg.what = msgType;
            if (arg1 != -1) {
                msg.arg1 = arg1;
            }
            if (arg2 != -1) {
                msg.arg2 = arg2;
            }
            if (null != obj) {
                msg.obj = obj;
            }
            mHandler.sendMessage(msg);
        } else {
            ZLogger.e(D, "handler is null, can't send message");
        }
    }

    @Override
    public void onVersionRead(int appVersion, int patchVersion) {
        ZLogger.d(D, "appVersion: " + appVersion + ", patchVersion: " + patchVersion);


        setDfuVersion(appVersion, patchVersion);

        sendMessage(MSG_RECEIVE_DFU_VERSION_INFO, null, appVersion, patchVersion);
//        synchronized (mCallbacks) {
//            // do something
//            for (WristbandManagerCallback callback : mCallbacks) {
//                callback.onVersionRead(appVersion, patchVersion);
//            }
//        }

        synchronized (mCommandSendLock) {
            isCommandSendOk = true;
            isCommandSend = true;
            mCommandSendLock.notifyAll();
        }
    }

    @Override
    public void onBatteryLevelChanged(int i, boolean b) {
        ZLogger.d(D, "value: " + i);
        if (b) {
            sendMessage(MSG_RECEIVE_BATTERY_CHANGE_INFO, null, i, -1);
        } else {
            sendMessage(MSG_RECEIVE_BATTERY_INFO, null, i, -1);
        }
    }

    @Override
    public void onHrpValueReceive(int value) {
        ZLogger.d(D, "value: " + value);
        sendMessage(MSG_RECEIVE_HRP_INFO, null, value, -1);
    }

    @Override
    public void onLinkLossAlertLevelChanged(byte level) {
        ZLogger.d(D, "level: " + level);
        SPWristbandConfigInfo.setControlSwitchLost(mContext, mLinkLossService.isAlertEnabled());
        synchronized (mCommandSendLock) {
            isCommandSendOk = true;
            isCommandSend = true;
            mCommandSendLock.notifyAll();
        }
    }

    private boolean isNotifyManageEnabled() {
        ZLogger.d(D, "isNotifyManageEnabled");
        String pkgName = mContext.getPackageName();
        final String flat = Settings.Secure.getString(mContext.getContentResolver(),
                ENABLED_NOTIFICATION_LISTENERS);
        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (String name : names) {
                final ComponentName cn = ComponentName.unflattenFromString(name);
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}

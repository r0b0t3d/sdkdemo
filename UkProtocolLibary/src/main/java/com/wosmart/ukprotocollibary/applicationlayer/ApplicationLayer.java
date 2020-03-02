package com.wosmart.ukprotocollibary.applicationlayer;


import android.content.Context;
import android.text.TextUtils;

import com.realsil.realteksdk.logger.ZLogger;
import com.realsil.realteksdk.utility.DataConverter;
import com.wosmart.ukprotocollibary.model.data.DeviceFunction;
import com.wosmart.ukprotocollibary.model.data.ReminderFunction;
import com.wosmart.ukprotocollibary.model.data.SportFunction;
import com.wosmart.ukprotocollibary.model.enums.DeviceLanguage;
import com.wosmart.ukprotocollibary.transportlayer.TransportLayer;
import com.wosmart.ukprotocollibary.transportlayer.TransportLayerCallback;
import com.wosmart.ukprotocollibary.util.StringByteTrans;

import java.util.List;


public class ApplicationLayer {
    // Log
    private final static String TAG = "ApplicationLayer";
    private final static boolean D = true;

    // Support Command Id
    public final static byte CMD_IMAGE_UPDATE = 0x01;
    public final static byte CMD_SETTING = 0x02;
    public final static byte CMD_BOND_REG = 0x03;
    public final static byte CMD_NOTIFY = 0x04;
    public final static byte CMD_SPORTS_DATA = 0x05;
    public final static byte CMD_FACTORY_TEST = 0x06;
    public final static byte CMD_CTRL = 0x07;
    public final static byte CMD_DUMP_STACK = 0x08;
    public final static byte CMD_TEST_FLASH = 0x09;
    public final static byte CMD_LOG = 0x0a;

    /*CMD_UPDATE : key */
    public final static byte KEY_UPDATE_REQUEST = 0x01;
    public final static byte KEY_UPDATE_RESPONSE = 0x02;
    public final static byte KEY_UPDATE_RESPONSE_OK = 0x00;
    public final static byte KEY_UPDATE_RESPONSE_ERR = 0x01;
    public final static byte UPDATE_RESPONSE_ERRCODE = 0x01;       //low power

    /*CMD_SETTING : key */
    public final static byte KEY_SETTING_TIMER = 0x01;
    public final static byte KEY_SETTING_ALARM = 0x02;
    public final static byte KEY_SETTING_GET_ALARM_LIST_REQ = 0x03;
    public final static byte KEY_SETTING_GET_ALARM_LIST_RSP = 0x04;
    public final static byte KEY_SETTING_STEP_TARGET = 0x05;
    public final static byte KEY_SETTING_SLEEP_TARGET = 0x06;
    public final static byte KEY_SETTING_USER_PROFILE = 0x10;
    public final static byte KEY_SETTING_LOST_MODE = 0x20;
    public final static byte KEY_SETTING_SIT_TOOLONG_NOTIFY = 0x21;
    public final static byte KEY_SETTING_LEFT_RIGHT_HAND_NOTIFY = 0x22;
    public final static byte KEY_SETTING_PHONE_OS = 0x23;
    public final static byte KEY_SETTING_INCOMING_CALL_LIST = 0x24;
    public final static byte KEY_SETTING_INCOMING_ON_OFF = 0x25;
    public final static byte KEY_SETTING_SIT_TOOLONG_SWITCH_REQ = 0x26;
    public final static byte KEY_SETTING_SIT_TOOLONG_SWITCH_RSP = 0x27;
    public final static byte KEY_SETTING_NOTIFY_SWITCH_REQ = 0x28;
    public final static byte KEY_SETTING_NOTIFY_SWITCH_RSP = 0x29;
    public final static byte KEY_SETTING_CLASSIC_ADDRESS = 0x60;
    public final static byte KEY_SETTING_GET_CLASSIC_STATUS = 0x61;

    public final static byte KEY_SETTING_TURN_OVER_WRIST_SET = 0x2A;
    public final static byte KEY_SETTING_TURN_OVER_WRIST_REQ = 0x2B;
    public final static byte KEY_SETTING_TURN_OVER_WRIST_RSP = 0x2C;

    public final static byte KEY_DEVICE_FUNCTION_REQ = 0x36;
    public final static byte KEY_DEVICE_FUNCTION_RSP = 0x37;

    public final static byte KEY_HOUR_SYSTEM_SETTING = 0x41;
    public final static byte KEY_HOUR_SYSTEM_REQ = 0x42;
    public final static byte KEY_HOUR_SYSTEM_RSP = 0x43;

    public final static byte KEY_UNIT_SYSTEM_SETTING = 0x44;
    public final static byte KEY_UNIT_SYSTEM_REQ = 0x45;
    public final static byte KEY_UNIT_SYSTEM_RSP = 0x46;

    public final static byte KEY_DISTURB_SETTING = 0x47;
    public final static byte KEY_DISTURB_REQ = 0x48;
    public final static byte KEY_DISTURB_RSP = 0x49;

    public final static byte KEY_SCREEN_LIGHT_DURATION_SETTING = 0x4A;
    public final static byte KEY_SCREEN_LIGHT_DURATION_REQ = 0x4B;
    public final static byte KEY_SCREEN_LIGHT_DURATION_RSP = 0x4C;

    public final static byte KEY_LANGUAGE_SETTING = 0x4E;
    public final static byte KEY_LANGUAGE_REQ = 0x4F;
    public final static byte KEY_LANGUAGE_RSP = 0x50;

    /*CMD_BOND_REG : key */
    public final static byte KEY_BOND_REQ = 0x01;
    public final static byte KEY_BOND_RSP = 0x02;
    public final static byte KEY_LOGIN_REQ = 0x03;
    public final static byte KEY_LOGIN_RSP = 0x04;
    public final static byte KEY_UNBOND = 0x05;
    public final static byte KEY_SUP_BOND_KEY = 0x06;
    public final static byte KEY_SUP_BOND_KEY_RSP = 0x07;

    /* CMD_NOTIFY: key:*/
    public final static byte KEY_NOTIFY_IMCOMING_CALL = 0x01;
    public final static byte KEY_NOTIFY_IMCOMING_CALL_ACC = 0x02;
    public final static byte KEY_NOTIFY_IMCOMING_CALL_REJ = 0x03;
    public final static byte KEY_NOTIFY_INCOMING_OTHER_NOTIFY = 0x04;
    public final static byte KEY_NOTIFY_END_CALL = 0x05;
    public final static byte KEY_NOTIFY_CLASSIC_ADDRESS = 0x10;
    public final static byte KEY_NOTIFY_CLASSIC_STATUS = 0x11;

    /* CMD_SPORTS: key:*/
    public final static byte KEY_SPORTS_REQ = 0x01;
    public final static byte KEY_SPORTS_RUNNIG_RSP = 0x02;
    public final static byte KEY_SPORTS_SLEEP_RSP = 0x03;
    public final static byte KEY_SPORTS_RUNNIG_RSP_MORE = 0x04;
    public final static byte KEY_SPORTS_SLEEP_SET_RSP = 0x05;
    public final static byte KEY_SPORTS_DATA_SYNC = 0x06;
    public final static byte KEY_SPORTS_HIS_SYNC_BEG = 0x07;
    public final static byte KEY_SPORTS_HIS_SYNC_END = 0x08;
    public final static byte KEY_SPORTS_DATA_TODAY_SYNC = 0x09;
    public final static byte KEY_SPORTS_DATA_LAST_SYNC = 0x0a;
    public final static byte KEY_SPORTS_DATA_TODAY_ADJUST = 0x0b;// 需要确认的功能
    public final static byte KEY_SPORTS_DATA_TODAY_ADJUST_RETURN = 0x0c;// 需要确认的功能
    public final static byte KEY_SPORTS_HRP_SINGLE_REQ = 0x0d;// 单次读取心率请求
    public final static byte KEY_SPORTS_HRP_CONTINUE_SET = 0x0e;// 连续心率采集设定
    public final static byte KEY_SPORTS_HRP_DATA_RSP = 0x0f;// 心率数据返回
    public final static byte KEY_SPORTS_HRP_DEVICE_CANCEL_READ_HRP = 0x10;// 手环取消单次心率读取
    public final static byte KEY_SPORTS_HRP_CONTINUE_PARAMS_GET = 0x11;// 连续心率采集参数获取
    public final static byte KEY_SPORTS_HRP_CONTINUE_PARAMS_RSP = 0x12;// 连续心率采集参数返回
    public final static byte KEY_SPORTS_BP_DATA_RSP = 0x13;
    public final static byte KEY_SPORTS_BP_SINGLE_REQ = 0x14;
    public final static byte KEY_SPORTS_BP_CANCEL_READ = 0x15;
    public final static byte KEY_SPORTS_DATA_RSP = 0x16;


    /* CMD_FAC_TEST: key:*/
    public final static byte KEY_FAC_TEST_ECHO_REQ = 0x01;
    public final static byte KEY_FAC_TEST_ECHO_RSP = 0x02;
    public final static byte KEY_FAC_TEST_CHAR_REQ = 0x03;
    public final static byte KEY_FAC_TEST_CHAR_RSP = 0x04;
    public final static byte KEY_FAC_TEST_LED = 0x05;
    public final static byte KEY_FAC_TEST_MOTO = 0x06;
    public final static byte KEY_FAC_TEST_WRITE_SN = 0x07;
    public final static byte KEY_FAC_TEST_READ_SN = 0x08;
    public final static byte KEY_FAC_TEST_SN_RSP = 0x09;
    public final static byte KEY_FAC_TEST_WRITE_TEST_FLAG = 0x0a;
    public final static byte KEY_FAC_TEST_READ_TEST_FLAG = 0x0b;
    public final static byte KEY_FAC_TEST_FLAG_RSP = 0x0c;
    public final static byte KEY_FAC_TEST_SENSOR_DATA_REQ = 0x0d;
    public final static byte KEY_FAC_TEST_SENSOR_DATA_RSP = 0x0e;
    public final static byte KEY_FAC_TEST_ENTER_SPUER_KEY = 0x10;
    public final static byte KEY_FAC_TEST_LEAVE_SPUER_KEY = 0x11;
    public final static byte KEY_FAC_TEST_BUTTON_TEST = 0x21;
    public final static byte KEY_FAC_TEST_MOTO_OLD = 0x31;
    public final static byte KEY_FAC_TEST_LED_OLD = 0x32;

    /* CMD_CONTROL: key:*/
    public final static byte KEY_CTRL_PHOTO_RSP = 0x01;
    public final static byte KEY_CTRL_CLICK_RSP = 0x02;
    public final static byte KEY_CTRL_DOUBLE_CLICK_RSP = 0x03;
    public final static byte KEY_CTRL_APP_REQ = 0x11;

    /* CMD_DUMP: key:*/
    public final static byte KEY_DUMP_ASSERT_LOCATE_REQ = 0x01;
    public final static byte KEY_DUMP_ASSERT_LOCATE_RSP = 0x02;
    public final static byte KEY_DUMP_ASSERT_STACK_REQ = 0x03;
    public final static byte KEY_DUMP_ASSERT_STACK_RSP = 0x14;

    /* LOG: key:*/
    public final static byte KEY_LOG_FUNC_OPEN = 0x01;
    public final static byte KEY_LOG_FUNC_CLOSE = 0x02;
    public final static byte KEY_LOG_RSP = 0x03;
    public final static byte KEY_LOG_REQ = 0x04;
    public final static byte KEY_LOG_START = 0x05;
    public final static byte KEY_LOG_END = 0x06;

    /* Login response*/
    public final static byte LOGIN_RSP_SUCCESS = 0x00;
    public final static byte LOGIN_RSP_ERROR = 0x01;
    public final static byte LOGIN_LOSS_LOGIN_INFO = 0x02;

    /* Bond response*/
    public final static byte BOND_RSP_SUCCESS = 0x00;
    public final static byte BOND_RSP_ERROR = 0x01;

    /* Sport Sync Mode */
    public final static byte SPORT_DATA_SYNC_MODE_DISABLE = 0x00;
    public final static byte SPORT_DATA_SYNC_MODE_ENABLE = 0x01;

    /* err code*/
    public final static byte SUCCESS = 0x00;
    public final static byte BOND_FAIL_TIMEOUT = 0x01;
    public final static byte SUPER_KEY_FAIL = 0x02;
    public final static byte LOW_POWER = 0x03;

    // Day Flags
    public final static byte REPETITION_NULL = 0x00;
    public final static byte REPETITION_ALL = 0x7f;
    public final static byte REPETITION_MON = 0x01;
    public final static byte REPETITION_TUES = 0x02;
    public final static byte REPETITION_WED = 0x04;
    public final static byte REPETITION_THU = 0x08;
    public final static byte REPETITION_FRI = 0x10;
    public final static byte REPETITION_SAT = 0x20;
    public final static byte REPETITION_SUN = 0x40;

    // Sex Flags
    public final static boolean SEX_MAN = true;
    public final static boolean SEX_WOMAN = false;

    // Call notify flags
    public final static byte PHONE_OS_IOS = 0x01;
    public final static byte PHONE_OS_ANDROID = 0x02;

    // Call notify flags
    public final static byte CALL_NOTIFY_MODE_ON = 0x01;
    public final static byte CALL_NOTIFY_MODE_OFF = 0x02;
    public final static byte CALL_NOTIFY_MODE_ENABLE_QQ = 0x03;
    public final static byte CALL_NOTIFY_MODE_DISABLE_QQ = 0x04;
    public final static byte CALL_NOTIFY_MODE_ENABLE_WECHAT = 0x05;
    public final static byte CALL_NOTIFY_MODE_DISABLE_WECHAT = 0x06;
    public final static byte CALL_NOTIFY_MODE_ENABLE_MESSAGE = 0x07;
    public final static byte CALL_NOTIFY_MODE_DISABLE_MESSAGE = 0x08;
    public final static byte CALL_NOTIFY_MODE_ENABLE_LINE = 0x09;
    public final static byte CALL_NOTIFY_MODE_DISABLE_LINE = 0x0a;
    public final static byte CALL_NOTIFY_MODE_ENABLE_TWITTER = 0x0b;
    public final static byte CALL_NOTIFY_MODE_DISABLE_TWITTER = 0x0c;
    public final static byte CALL_NOTIFY_MODE_ENABLE_FACEBOOK = 0x0e;
    public final static byte CALL_NOTIFY_MODE_DISABLE_FACEBOOK = 0x0f;
    public final static byte CALL_NOTIFY_MODE_ENABLE_MESSENGER = 0x10;
    public final static byte CALL_NOTIFY_MODE_DISABLE_MESSENGER = 0x11;
    public final static byte CALL_NOTIFY_MODE_ENABLE_WHATSAPP = 0x12;
    public final static byte CALL_NOTIFY_MODE_DISABLE_WHATSAPP = 0x13;
    public final static byte CALL_NOTIFY_MODE_ENABLE_LINKEDIN = 0x14;
    public final static byte CALL_NOTIFY_MODE_DISABLE_LINKEDIN = 0x15;
    public final static byte CALL_NOTIFY_MODE_ENABLE_INSTAGRAM = 0x16;
    public final static byte CALL_NOTIFY_MODE_DISABLE_INSTAGRAM = 0x17;
    public final static byte CALL_NOTIFY_MODE_ENABLE_SKYPE = 0x18;
    public final static byte CALL_NOTIFY_MODE_DISABLE_SKYPE = 0x19;
    public final static byte CALL_NOTIFY_MODE_ENABLE_VIBER = 0x1A;
    public final static byte CALL_NOTIFY_MODE_DISABLE_VIBER = 0x1B;
    public final static byte CALL_NOTIFY_MODE_ENABLE_KAKAOTALK = 0x1C;
    public final static byte CALL_NOTIFY_MODE_DISABLE_KAKAOTALK = 0x1D;
    public final static byte CALL_NOTIFY_MODE_ENABLE_VKONTAKTE = 0x1E;
    public final static byte CALL_NOTIFY_MODE_DISABLE_VKONTAKE = 0x1F;

    // notify flags
    public final static byte OTHER_NOTIFY_INFO_QQ = 0x01;
    public final static byte OTHER_NOTIFY_INFO_WECHAT = 0x02;
    public final static byte OTHER_NOTIFY_INFO_SMS = 0x04;
    public final static byte OTHER_NOTIFY_INFO_LINE = 0x08;
    public final static byte OTHER_NOTIFY_INFO_TWITTER = 0x09;
    public final static byte OTHER_NOTIFY_INFO_FACEBOOK = 0x0A;
    public final static byte OTHER_NOTIFY_INFO_MESSENGER = 0x0B;
    public final static byte OTHER_NOTIFY_INFO_WHATSAPP = 0x0C;
    public final static byte OTHER_NOTIFY_INFO_LINKEDIN = 0x0D;
    public final static byte OTHER_NOTIFY_INFO_INSTAGRAM = 0x0E;
    public final static byte OTHER_NOTIFY_INFO_SKYPE = 0x0F;
    public final static byte OTHER_NOTIFY_INFO_VIBER = 0x10;
    public final static byte OTHER_NOTIFY_INFO_KAKAOTALK = 0x11;
    public final static byte OTHER_NOTIFY_INFO_VKONTAKTE = 0x12;

    public final static byte NOTIFY_SWITCH_SETTING_CALL = 0x01;
    public final static byte NOTIFY_SWITCH_SETTING_QQ = 0x02;
    public final static byte NOTIFY_SWITCH_SETTING_WECHAT = 0x04;
    public final static byte NOTIFY_SWITCH_SETTING_MESSAGE = 0x08;

    // Long sit Flags
    public final static byte LONG_SIT_CONTROL_ENABLE = 0x01;
    public final static byte LONG_SIT_CONTROL_DISABLE = 0x00;

    // FAC Led Flags
    public final static byte FAC_LED_CONTROL_ENABLE_ALL = (byte) 0xFF;
    public final static byte FAC_LED_CONTROL_ENABLE_0 = 0x00;
    public final static byte FAC_LED_CONTROL_ENABLE_1 = 0x01;
    public final static byte FAC_LED_CONTROL_ENABLE_2 = 0x02;

    // Sleep Mode
    public static final int SLEEP_MODE_START_SLEEP = 1;
    public static final int SLEEP_MODE_START_DEEP_SLEEP = 2;
    public static final int SLEEP_MODE_START_WAKE = 3;

    // Debug Log type
    public final static byte DEBUG_LOG_TYPE_MODULE_APP = (byte) 0x01;
    public final static byte DEBUG_LOG_TYPE_MODULE_LOWERSTACK = (byte) 0x02;
    public final static byte DEBUG_LOG_TYPE_MODULE_UPSTACK = (byte) 0x03;
    public final static byte DEBUG_LOG_TYPE_SLEEP_DATA = (byte) 0x11;
    public final static byte DEBUG_LOG_TYPE_SPORT_DATA = (byte) 0x21;
    public final static byte DEBUG_LOG_TYPE_CONFIG_DATA = (byte) 0x31;

    public final static int DEBUG_LOG_TYPE_MAX_CNT = 6;

    // Camera control
    public final static byte CAMERA_CONTROL_APP_IN_FORE = 0x00;
    public final static byte CAMERA_CONTROL_APP_IN_BACK = 0x01;


    // Turn over wrist control
    public final static byte TURN_OVER_WRIST_CONTROL_ENABLE = 0x01;
    public final static byte TURN_OVER_WRIST_CONTROL_DISABLE = 0x00;


    // Hrp single request
    public final static byte HRP_SINGLE_REQ_MODE_ENABLE = 0x01;
    public final static byte HRP_SINGLE_REQ_MODE_DISABLE = 0x00;

    // Hrp single request
    public final static byte BP_SINGLE_REQ_MODE_ENABLE = 0x01;
    public final static byte BP_SINGLE_REQ_MODE_DISABLE = 0x00;

    // Hrp continue request
    public final static byte HRP_CONTINUE_REQ_MODE_ENABLE = 0x01;
    public final static byte HRP_CONTINUE_REQ_MODE_DISABLE = 0x00;

    // hour model
    public final static byte HOUR_12_MODEL = 0x01;
    public final static byte HOUR_24_MODEL = 0x00;

    // unit model
    public final static byte UNIT_METRIC_SYSTEM = 0x00;
    public final static byte UNIT_ENGLISH_SYSTEM = 0x01;

    // disturb model
    public final static byte DISTURB_ENABLE = 0x01;
    public final static byte DISTURB_DISABLE = 0x00;

    // Transport Layer Object
    TransportLayer mTransportLayer;

    // Application Layer Call
    private ApplicationLayerCallback mCallback;

    public ApplicationLayer(Context context, ApplicationLayerCallback callback) {
        ZLogger.d(D, "initial");
        // register callback
        mCallback = callback;

        // initial the transport layer
        mTransportLayer = new TransportLayer(context, mTransportCallback);

    }

    /**
     * Connect to the remote device.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onConnectionStateChange} callback is invoked, reporting the result of the operation.
     *
     * @return the operation result
     */
    public boolean connect(String addr) {
        ZLogger.d(D, "connect with: " + addr);
        return mTransportLayer.connect(addr);
    }

    /**
     * Close, it will disconnect to the remote, and close gatt.
     */
    public void close() {
        ZLogger.d(D, "close()");
        mTransportLayer.close();
    }

    /**
     * Disconnect, it will disconnect to the remote.
     */
    public void disconnect() {
        mTransportLayer.disconnect();
    }

    /**
     * Set the name
     *
     * @param name the name
     */
    public void setDeviceName(String name) {
        ZLogger.d(D, "set name, name: " + name);
        mTransportLayer.setDeviceName(name);
    }

    /**
     * Get the name
     */
    public void getDeviceName() {
        ZLogger.d(D, "getDeviceName");
        mTransportLayer.getDeviceName();
    }

    /**
     * Request Remote enter OTA mode. Command: 0x01, Key: 0x01.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked, reporting the result of the operation.
     * then the remote will response some information, the {@link ApplicationLayerCallback#onUpdateCmdRequestEnterOtaMode}
     * callback will invoked, response some information to host
     *
     * @return the operation result
     */
    public boolean UpdateCmdRequestEnterOtaMode() {
        ZLogger.d(D, "UpdateCmdRequestEnterOtaMode");
        // generate key data
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_UPDATE_REQUEST, null);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_IMAGE_UPDATE, keyData);
        ZLogger.i(D, "appData: " + DataConverter.bytes2Hex(appPacketData));

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }

    /**
     * Set the remote time. Command: 0x02, Key: 0x01.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked,
     * reporting the result of the operation.
     *
     * @param year the current time of year.
     * @param mon  the current time of mon.
     * @param day  the current time of day.
     * @param hour the current time of hour.
     * @param min  the current time of min.
     * @param sec  the current time of sec.
     * @return the operation result
     */
    public boolean SettingCmdTimeSetting(int year, int mon, int day, int hour, int min, int sec) {
        ZLogger.d(D, "year: " + year
                + ", mon: " + mon
                + ", day: " + day
                + ", hour: " + hour
                + ", min: " + min
                + ", sec: " + sec);
        // generate key value data
        ApplicationLayerTimerPacket timePackt = new ApplicationLayerTimerPacket(year, mon, day, hour, min, sec);
        // generate key data
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_SETTING_TIMER, timePackt.getPacket());
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_SETTING, keyData);
        ZLogger.i(D, "appData: " + DataConverter.bytes2Hex(appPacketData));

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }

    /**
     * Set the remote time. Command: 0x02, Key: 0x02.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked,
     * reporting the result of the operation.
     *
     * @param Alarms the alarm list.
     * @return the operation result
     */
    public boolean SettingCmdAlarmsSetting(ApplicationLayerAlarmsPacket Alarms) {
        ZLogger.d(D, "SettingCmdAlarmSetting");
        // generate key value data
        byte[] keyValue = null;
        if (Alarms == null || (Alarms.size() == 0)) {
            //do nothing
        } else {
            if (Alarms.size() > 8) {
                return false;
            }
            keyValue = Alarms.getPacket();
        }
        // generate key data
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_SETTING_ALARM, keyValue);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_SETTING, keyData);
        ZLogger.i(D, "appData: " + DataConverter.bytes2Hex(appPacketData));

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }

    /**
     * Request Remote alarms list. Command: 0x01, Key: 0x03.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked, reporting the result of the operation.
     * then the remote will response some information, the {@link ApplicationLayerCallback#onSettingCmdRequestAlarmList}
     * callback will invoked, response some information to host
     *
     * @return the operation result
     */
    public boolean SettingCmdRequestAlarmList() {
        ZLogger.d(D, "SettingCmdRequestAlarmList");
        // generate key data
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_SETTING_GET_ALARM_LIST_REQ, null);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_SETTING, keyData);
        ZLogger.i(D, "appData: " + DataConverter.bytes2Hex(appPacketData));

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }

    /**
     * Set the remote user profile. Command: 0x02, Key: 0x10.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked,
     * reporting the result of the operation.
     *
     * @param user the user profile packet.
     * @return the operation result
     */
    public boolean SettingCmdUserSetting(ApplicationLayerUserPacket user) {
        ZLogger.d(D, "SettingCmdUserSetting");
        // generate key value data
        byte[] keyValue = user.getPacket();
        // generate key data
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_SETTING_USER_PROFILE, keyValue);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_SETTING, keyData);
        ZLogger.i(D, "appData: " + DataConverter.bytes2Hex(appPacketData));

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }

    /**
     * Set the remote lost mode. Command: 0x02, Key: 0x20.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked,
     * reporting the result of the operation.
     *
     * @param mode the lost mode.
     * @return the operation result
     */
    public boolean SettingCmdLostModeSetting(byte mode) {
        ZLogger.d(D, "mode: " + mode);
        // generate key value data
        byte[] keyValue = {mode};
        // generate key data
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_SETTING_LOST_MODE, keyValue);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_SETTING, keyData);
        ZLogger.i(D, "appData: " + DataConverter.bytes2Hex(appPacketData));

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }

    /**
     * Set the remote step target. Command: 0x02, Key: 0x05.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked,
     * reporting the result of the operation.
     *
     * @param step the step target.
     * @return the operation result
     */
    public boolean SettingCmdStepTargetSetting(long step) {
        ZLogger.d(D, "step: " + step);
        // generate key value data
        ApplicationLayerStepTargetPacket p = new ApplicationLayerStepTargetPacket(step);
        byte[] keyValue = p.getPacket();
        // generate key data
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_SETTING_STEP_TARGET, keyValue);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_SETTING, keyData);
        ZLogger.i(D, "appData: " + DataConverter.bytes2Hex(appPacketData));

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }

    /**
     * Set the remote step target. Command: 0x02, Key: 0x05.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked,
     * reporting the result of the operation.
     *
     * @param sleepMinute the sleep minute target.
     * @return the operation result
     */
    public boolean SettingCmdSleepTargetSetting(int sleepMinute) {
        ZLogger.d(D, "sleepMinute: " + sleepMinute);
        // generate key value data
        ApplicationLayerSleepTargetPacket p = new ApplicationLayerSleepTargetPacket(sleepMinute);
        byte[] keyValue = p.getPacket();
        // generate key data
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_SETTING_SLEEP_TARGET, keyValue);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_SETTING, keyData);
        ZLogger.i(D, "appData: " + DataConverter.bytes2Hex(appPacketData));

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }

    /**
     * Set the remote long sit notification. Command: 0x02, Key: 0x21.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked,
     * reporting the result of the operation.
     *
     * @param packet the sit packet.
     * @return the operation result
     */
    public boolean SettingCmdLongSitSetting(ApplicationLayerSitPacket packet) {
        ZLogger.d(D, "SettingCmdLongSitSetting");
        // generate key value data
        byte[] keyValue = packet.getPacket();
        // generate key data
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_SETTING_SIT_TOOLONG_NOTIFY, keyValue);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_SETTING, keyData);
        ZLogger.i(D, "appData: " + DataConverter.bytes2Hex(appPacketData));

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }

    /**
     * Set the remote left right hand. Command: 0x02, Key: 0x22.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked,
     * reporting the result of the operation.
     *
     * @param mode the left right hand mode.
     * @return the operation result
     */
    public boolean SettingCmdLeftRightSetting(byte mode) {
        ZLogger.d(D, "mode: " + mode);
        // generate key value data
        byte[] keyValue = {mode};
        // generate key data
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_SETTING_LEFT_RIGHT_HAND_NOTIFY, keyValue);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_SETTING, keyData);
        ZLogger.i(D, "appData: " + DataConverter.bytes2Hex(appPacketData));

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }

    /**
     * Set the phone OS. Command: 0x02, Key: 0x23.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked,
     * reporting the result of the operation.
     *
     * @param mode the phone os.
     * @return the operation result
     */
    public boolean SettingCmdPhoneOSSetting(byte mode) {
        ZLogger.d(D, "mode: " + mode);
        // generate key value data
        byte[] keyValue = {mode, 0x00};
        // generate key data
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_SETTING_PHONE_OS, keyValue);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_SETTING, keyData);
        ZLogger.i(D, "appData: " + DataConverter.bytes2Hex(appPacketData));

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }

    /**
     * Set the remote call notify mode. Command: 0x02, Key: 0x25.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked,
     * reporting the result of the operation.
     *
     * @param mode call notify mode.
     * @return the operation result
     */
    public boolean SettingCmdCallNotifySetting(byte mode) {
        ZLogger.d(D, "mode: " + mode);
        // generate key value data
        byte[] keyValue = {mode};
        // generate key data
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_SETTING_INCOMING_ON_OFF, keyValue);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_SETTING, keyData);
        ZLogger.i(D, "appData: " + DataConverter.bytes2Hex(appPacketData));

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }

    /**
     * Request the remote current long sit setting. Command: 0x02, Key: 0x26.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked, reporting the result of the operation.
     * then the remote will response some information, the {@link ApplicationLayerCallback#onSettingCmdRequestLongSit}
     * callback will invoked, response some information to host
     *
     * @return the operation result
     */
    public boolean SettingCmdRequestLongSitSetting() {
        ZLogger.d(D, "SettingCmdRequestLongSitSetting");
        // generate key data
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_SETTING_SIT_TOOLONG_SWITCH_REQ, null);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_SETTING, keyData);
        ZLogger.i(D, "appData: " + DataConverter.bytes2Hex(appPacketData));

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }

    /**
     * Request the remote support function. Command: 0x01, Key: 0x37.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked, reporting the result of the operation.
     * then the remote will response some information, the {@link ApplicationLayerCallback#onSportFunction(SportFunction)},the {@link ApplicationLayerCallback#onReminderFunction(ReminderFunction)} ,the {@link ApplicationLayerCallback#onDeviceFunction(DeviceFunction)}
     * callback will invoked, response some information to host
     *
     * @return the operation result
     */
    public boolean SettingCmdRequestDeviceFunctionSetting() {
        ZLogger.d(D, "SettingCmdRequestDeviceFunctionSetting");
        // generate key data

        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_DEVICE_FUNCTION_REQ, null);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_SETTING, keyData);
        ZLogger.i(D, "appData: " + DataConverter.bytes2Hex(appPacketData));

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }

    /**
     * setting the remote hour system command:0x01  key:0x41
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked, reporting the result of the operation.
     *
     * @param mode 0x01: 12 hour model  0x00: 24 hour model
     * @return the operation result
     */
    public boolean SettingCmdSettingHourSystem(byte mode) {
        ZLogger.d(D, "SettingCmdSettingHourSystem");
        // generate key value
        byte[] keyValue = {mode};
        // generate key data
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_HOUR_SYSTEM_SETTING, keyValue);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_SETTING, keyData);
        ZLogger.i(D, "appData: " + DataConverter.bytes2Hex(appPacketData));
        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }

    /**
     * request the remote hour system setting command:0x01  key:0x42
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked, reporting the result of the operation.
     *
     * @return the operation result
     */
    public boolean SettingCmdRequestHourSystemSetting() {
        ZLogger.d(D, "SettingCmdRequestHourSystemSetting");
        // generate key data
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_HOUR_SYSTEM_REQ, null);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_SETTING, keyData);
        ZLogger.i(D, "appData: " + DataConverter.bytes2Hex(appPacketData));
        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }


    public boolean SettingCmdSettingUnit(byte mode) {
        ZLogger.d(D, "SettingCmdSettingUnit");
        byte[] keyValue = {mode};
        // generate key data
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_UNIT_SYSTEM_SETTING, keyValue);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_SETTING, keyData);
        ZLogger.i(D, "appData: " + DataConverter.bytes2Hex(appPacketData));

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }

    public boolean SettingCmdSettingDisturbSetting(ApplicationLayerDisturbPacket packet) {
        ZLogger.d(D, "SettingCmdSettingDisturbSetting");
        byte[] keyValue = packet.getPacket();
        // generate key data
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_DISTURB_SETTING, keyValue);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_SETTING, keyData);
        ZLogger.i(D, "appData: " + DataConverter.bytes2Hex(appPacketData));

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }

    public boolean SettingCmdSettingScreenLightDuration(int duration) {
        ZLogger.d(D, "SettingCmdSettingScreenLightDuration");
        byte[] keyValue = {(byte) (duration & 0xFF)};
        // generate key data
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_SCREEN_LIGHT_DURATION_SETTING, keyValue);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_SETTING, keyData);
        ZLogger.i(D, "appData: " + DataConverter.bytes2Hex(appPacketData));

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }

    public boolean SettingCmdRequestScreenLightDurationSetting() {
        ZLogger.d(D, "SettingCmdRequestScreenLightDurationSetting");
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_SCREEN_LIGHT_DURATION_REQ, null);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_SETTING, keyData);
        ZLogger.i(D, "appData: " + DataConverter.bytes2Hex(appPacketData));

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }

    public boolean SettingCmdSettingLanguage(DeviceLanguage language) {
        ZLogger.d(D, "SettingCmdSettingLanguage");
        byte[] keyValue = new byte[]{1};
        switch (language) {
            case LANGUAGE_SAMPLE_CHINESE:
                keyValue[0] = (byte) 0x01;
                break;
            case LANGUAGE_TRADITIONAL_CHINESE:
                keyValue[0] = (byte) 0x02;
                break;
            case LANGUAGE_ENGLISH:
                keyValue[0] = (byte) 0x00;
                break;
            case LANGUAGE_SPANISH:
                keyValue[0] = (byte) 0x57;
                break;
            case LANGUAGE_FRENCH:
                keyValue[0] = (byte) 0x14;
                break;
            case LANGUAGE_GERMAN:
                keyValue[0] = (byte) 0x12;
                break;
            case LANGUAGE_ITALIAN:
                keyValue[0] = (byte) 0x60;
                break;
            case LANGUAGE_JAPANESE:
                keyValue[0] = (byte) 0x41;
                break;
            default:
                keyValue[0] = (byte) 0x00;
                break;
        }
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_LANGUAGE_SETTING, keyValue);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_SETTING, keyData);
        ZLogger.i(D, "appData: " + DataConverter.bytes2Hex(appPacketData));

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }

    public boolean SettingCmdRequestLanguage() {
        ZLogger.d(D, "SettingCmdRequestLanguage");
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_LANGUAGE_REQ, null);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_SETTING, keyData);
        ZLogger.i(D, "appData: " + DataConverter.bytes2Hex(appPacketData));

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }


    public boolean SettingCmdRequestDisturbSetting() {
        ZLogger.d(D, "SettingCmdRequestDisturbSetting");
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_DISTURB_REQ, null);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_SETTING, keyData);
        ZLogger.i(D, "appData: " + DataConverter.bytes2Hex(appPacketData));

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }

    public boolean SettingCmdRequestUnit() {
        ZLogger.d(D, "SettingCmdRequestUnit");
        // generate key data
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_UNIT_SYSTEM_REQ, null);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_SETTING, keyData);
        ZLogger.i(D, "appData: " + DataConverter.bytes2Hex(appPacketData));

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }


    /**
     * Request the remote current notify setting. Command: 0x02, Key: 0x28.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked, reporting the result of the operation.
     * then the remote will response some information, the {@link ApplicationLayerCallback#onSettingCmdRequestNotifySwitch}
     * callback will invoked, response some information to host
     *
     * @return the operation result
     */
    public boolean SettingCmdRequestNotifySwitchSetting() {
        ZLogger.d(D, "SettingCmdRequestNotifySwitchSetting");
        // generate key data
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_SETTING_NOTIFY_SWITCH_REQ, null);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_SETTING, keyData);
        ZLogger.i(D, "appData: " + DataConverter.bytes2Hex(appPacketData));

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }

    /**
     * Set the remote Turn Over Wrist. Command: 0x02, Key: 0x2A.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked,
     * reporting the result of the operation.
     *
     * @param enable ENABLE/DISABLE.
     * @return the operation result
     */
    public boolean SettingCmdTurnOverWristSetting(boolean enable) {
        ZLogger.d(D, "enable: " + enable);
        // generate key value data
        byte[] keyValue = {enable ? TURN_OVER_WRIST_CONTROL_ENABLE : TURN_OVER_WRIST_CONTROL_DISABLE};
        // generate key data
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_SETTING_TURN_OVER_WRIST_SET, keyValue);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_SETTING, keyData);
        ZLogger.i(D, "appData: " + DataConverter.bytes2Hex(appPacketData));

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }

    /**
     * Request the remote current Turn Over Wrist setting. Command: 0x02, Key: 0x2B.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked, reporting the result of the operation.
     * then the remote will response some information, the {@link ApplicationLayerCallback#onSettingCmdRequestLongSit}
     * callback will invoked, response some information to host
     *
     * @return the operation result
     */
    public boolean SettingCmdRequestTurnOverWristSetting() {
        ZLogger.d(D, "SettingCmdRequestTurnOverWristSetting");
        // generate key data
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_SETTING_TURN_OVER_WRIST_REQ, null);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_SETTING, keyData);
        ZLogger.i(D, "appData: " + DataConverter.bytes2Hex(appPacketData));

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }

    /**
     * Request bond to remote. Command: 0x03, Key: 0x01.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked, reporting the result of the operation.
     * then the remote will response some information, the {@link ApplicationLayerCallback#onBondCmdRequestBond}
     * callback will invoked, response some information to host
     *
     * @param userId the user id string.
     * @return the operation result
     */
    public boolean BondCmdRequestBond(String userId) {
        ZLogger.d(D, "userId=" + userId);
        // generate key value data
        // be careful java use the UTF-16 code, so we need change the ascii byte array
        byte[] asciiArray = StringByteTrans.Str2Bytes(userId);
        byte[] keyValue = new byte[32];
        if (asciiArray.length > 32) {
            System.arraycopy(asciiArray, 0, keyValue, 0, 32);
        } else {
            System.arraycopy(asciiArray, 0, keyValue, 0, asciiArray.length);
        }
        ZLogger.i(D, "keyValue=" + DataConverter.bytes2Hex(keyValue));
        // generate key data
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_BOND_REQ, keyValue);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_BOND_REG, keyData);
        ZLogger.i(D, "appData=" + DataConverter.bytes2Hex(appPacketData));

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }

    /**
     * Request login to remote. Command: 0x03, Key: 0x03.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked, reporting the result of the operation.
     * then the remote will response some information, the {@link ApplicationLayerCallback#onBondCmdRequestLogin}
     * callback will invoked, response some information to host
     *
     * @param userId the user id string.
     * @return the operation result
     */
    public boolean BondCmdRequestLogin(String userId) {
        ZLogger.d(D, "userid: " + userId);
        // generate key value data
        // be careful java use the UTF-16 code, so we need change the ascii byte array
        byte[] asciiArray = StringByteTrans.Str2Bytes(userId);
        byte[] keyValue = new byte[32];
        if (asciiArray.length > 32) {
            System.arraycopy(asciiArray, 0, keyValue, 0, 32);
        } else {
            System.arraycopy(asciiArray, 0, keyValue, 0, asciiArray.length);
        }
        ZLogger.i(D, "keyValue: " + DataConverter.bytes2Hex(keyValue));
        // generate key data
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_LOGIN_REQ, keyValue);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_BOND_REG, keyData);
        ZLogger.i(D, "appData: " + DataConverter.bytes2Hex(appPacketData));

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }

    /**
     * Request clear bond to remote. Command: 0x03, Key: 0x05.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked, reporting the result of the operation.
     *
     * @return the operation result
     */
    public boolean BondCmdRequestRemoveBond() {
        // generate key value data, reserve one byte
        byte[] keyValue = new byte[1];
        ZLogger.i(D, "keyValue: " + DataConverter.bytes2Hex(keyValue));
        // generate key data
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_UNBOND, keyValue);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_BOND_REG, keyData);
        ZLogger.i(D, "appData: " + DataConverter.bytes2Hex(appPacketData));

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }

    /**
     * Send notify info to the remote. Command: 0x04, Key: 0x01.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked,
     * reporting the result of the operation.
     *
     * @return the operation result
     */
    public boolean NotifyCmdCallNotifyInfoSetting() {
        ZLogger.d(D, "NotifyCmdCallNotifyInfoSetting");
        return NotifyCmdCallNotifyInfoSetting("");
        /*
        // generate key data
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_NOTIFY_IMCOMING_CALL, null);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_NOTIFY, keyData);
        ZLogger.i(D,"appData: " + DataConverter.bytes2Hex(appPacketData) + "\n, length: " + appPacketData.length);

        // sent the data
        return mTransportLayer.sendData(appPacketData);
        */
    }

    public boolean NotifyCmdCallNotifyInfoSetting(String showData) {
        ZLogger.d(D, "showData: " + showData);
        // generate key data
        byte[] keyData;
        if (TextUtils.isEmpty(showData)) {
            keyData = ApplicationLayerKeyPacket.preparePacket(KEY_NOTIFY_IMCOMING_CALL, null);
        } else {
            keyData = ApplicationLayerKeyPacket.preparePacket(KEY_NOTIFY_IMCOMING_CALL, showData.getBytes());
        }
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_NOTIFY, keyData);
        ZLogger.i(D, "appData: " + DataConverter.bytes2Hex(appPacketData));

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }

    /**
     * Send call accept notify info to the remote. Command: 0x04, Key: 0x02.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked,
     * reporting the result of the operation.
     *
     * @return the operation result
     */
    public boolean NotifyCmdCallAcceptNotifyInfoSetting() {
        ZLogger.d(D, "NotifyCmdCallAcceptNotifyInfoSetting");
        // generate key data
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_NOTIFY_IMCOMING_CALL_ACC, null);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_NOTIFY, keyData);
        ZLogger.i(D, "appData: " + DataConverter.bytes2Hex(appPacketData));

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }

    /**
     * Send call reject notify info to the remote. Command: 0x04, Key: 0x03.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked,
     * reporting the result of the operation.
     *
     * @return the operation result
     */
    public boolean NotifyCmdCallRejectNotifyInfoSetting() {
        ZLogger.d(D, "NotifyCmdCallRejectNotifyInfoSetting");
        // generate key data
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_NOTIFY_IMCOMING_CALL_REJ, null);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_NOTIFY, keyData);
        ZLogger.i(D, "appData: " + DataConverter.bytes2Hex(appPacketData));

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }

    /**
     * Send notify info to the remote. Command: 0x04, Key: 0x04.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked,
     * reporting the result of the operation.
     *
     * @param info call notify info.
     * @return the operation result
     */
    public boolean NotifyCmdOtherNotifyInfoSetting(byte info) {
        ZLogger.d(D, "NotifyCmdOtherNotifyInfoSetting, info: " + info);
        /*
        // generate key value data
        byte[] keyValue = {info};
        // generate key data
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_NOTIFY_INCOMING_OTHER_NOTIFY, keyValue);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_NOTIFY, keyData);
        ZLogger.i(D,"appData: " + DataConverter.bytes2Hex(appPacketData) + "\n, length: " + appPacketData.length);

        // sent the data
        return mTransportLayer.sendData(appPacketData);
        */
        return NotifyCmdOtherNotifyInfoSetting(info, "");
    }

    public boolean NotifyCmdOtherNotifyInfoSetting(byte info, String showData) {
        ZLogger.d(D, "info: " + info + "showData: " + showData);
        // generate key value data
        //byte[] keyValue = {info};
        byte[] showDataByte = null;
        byte[] keyValue = null;
        if (TextUtils.isEmpty(showData)) {
            keyValue = new byte[1];
            keyValue[0] = info;
        } else {
            showDataByte = showData.getBytes();    //UTF-8
            int len = showDataByte.length;
            keyValue = new byte[1 + len];
            keyValue[0] = info;
            System.arraycopy(showDataByte, 0, keyValue, 1, len);
        }

        // generate key data
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_NOTIFY_INCOMING_OTHER_NOTIFY, keyValue);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_NOTIFY, keyData);
        ZLogger.i(D, "appData: " + DataConverter.bytes2Hex(appPacketData));

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }


    /**
     * Request remote send sport data. Command: 0x05, Key: 0x01.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked, reporting the result of the operation.
     * then the remote will response some information, the {@link ApplicationLayerCallback#onSportDataCmdStepData}
     * and {@link ApplicationLayerCallback#onSportDataCmdSleepData}
     * callback will invoked, response some information to host
     *
     * @return the operation result
     */
    public boolean SportDataCmdRequestData() {
        ZLogger.d(D, "SportDataCmdRequestData");
        // generate key value data

        // generate key data
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_SPORTS_REQ, null);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_SPORTS_DATA, keyData);
        ZLogger.i(D, "appData: " + DataConverter.bytes2Hex(appPacketData));

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }

    /**
     * Set the remote sync mode. Command: 0x05, Key: 0x06.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked,
     * reporting the result of the operation.
     *
     * @param mode the left right hand mode.
     * @return the operation result
     */
    public boolean SportDataCmdSyncSetting(byte mode) {
        ZLogger.d(D, "mode: " + mode);
        // generate key value data
        byte[] keyValue = {mode};
        // generate key data
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_SPORTS_DATA_SYNC, keyValue);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_SPORTS_DATA, keyData);
        ZLogger.i(D, "appData: " + DataConverter.bytes2Hex(appPacketData));

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }

    /**
     * Set the remote sync today. Command: 0x05, Key: 0x09.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked,
     * reporting the result of the operation.
     *
     * @param packet the today sport packet.
     * @return the operation result
     */
    public boolean SportDataCmdSyncToday(ApplicationLayerTodaySportPacket packet) {
        ZLogger.d(D, "SportDataCmdSyncToday");
        // generate key value data
        byte[] keyValue = packet.getPacket();
        // generate key data
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_SPORTS_DATA_TODAY_SYNC, keyValue);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_SPORTS_DATA, keyData);
        ZLogger.i(D, "appData: " + DataConverter.bytes2Hex(appPacketData));

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }

    /**
     * Set the remote sync recently. Command: 0x05, Key: 0x0a.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked,
     * reporting the result of the operation.
     *
     * @param packet the recently sport packet.
     * @return the operation result
     */
    public boolean SportDataCmdSyncRecently(ApplicationLayerRecentlySportPacket packet) {
        ZLogger.d(D, "SportDataCmdSyncRecently");
        // generate key value data
        byte[] keyValue = packet.getPacket();
        // generate key data
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_SPORTS_DATA_LAST_SYNC, keyValue);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_SPORTS_DATA, keyData);
        ZLogger.i(D, "appData: " + DataConverter.bytes2Hex(appPacketData));

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }

    /**
     * hrp single request. Command: 0x05, Key: 0x0d.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked,
     * reporting the result of the operation.
     *
     * @param enable enable/disable hrp single request .
     * @return the operation result
     */
    public boolean SportDataCmdHrpSingleRequest(boolean enable) {
        ZLogger.d(D, "enable: " + enable);
        // generate key value data
        byte[] keyValue = new byte[]{enable ? HRP_SINGLE_REQ_MODE_ENABLE : HRP_SINGLE_REQ_MODE_DISABLE};
        // generate key data
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_SPORTS_HRP_SINGLE_REQ, keyValue);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_SPORTS_DATA, keyData);
        ZLogger.i(D, "appData: " + DataConverter.bytes2Hex(appPacketData));

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }

    /**
     * hrp single request. Command: 0x05, Key: 0x0d.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked,
     * reporting the result of the operation.
     *
     * @param enable enable/disable hrp single request .
     * @return the operation result
     */
    public boolean SportDataCmdBpSingleRequest(boolean enable) {
        ZLogger.d(D, "enable: " + enable);
        // generate key value data
        byte[] keyValue = new byte[]{enable ? BP_SINGLE_REQ_MODE_ENABLE : BP_SINGLE_REQ_MODE_DISABLE};
        // generate key data
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_SPORTS_BP_SINGLE_REQ, keyValue);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_SPORTS_DATA, keyData);
        ZLogger.i(D, "appData: " + DataConverter.bytes2Hex(appPacketData));

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }

    /**
     * hrp continue set. Command: 0x05, Key: 0x0d.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked,
     * reporting the result of the operation.
     *
     * @param enable   enable/disable hrp continue request .
     * @param interval Unit minutes, only enable valid.
     * @return the operation result
     */
    public boolean SportDataCmdHrpContinueSet(boolean enable, int interval) {
        ZLogger.d(D, "SportDataCmdHrpContinueRequest, enable: " + enable + ", interval: " + interval);
        // generate key value data
        byte[] keyValue = new byte[]{enable ? HRP_CONTINUE_REQ_MODE_ENABLE : HRP_CONTINUE_REQ_MODE_DISABLE
                , (byte) (interval & 0xff)};
        // generate key data
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_SPORTS_HRP_CONTINUE_SET, keyValue);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_SPORTS_DATA, keyData);
        ZLogger.i(D, "appData: " + DataConverter.bytes2Hex(appPacketData));

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }

    /**
     * hrp continue set. Command: 0x05, Key: 0x11.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked, reporting the result of the operation.
     * then the remote will response some information, the {@link ApplicationLayerCallback#onSportDataCmdHrpContinueParamRsp}
     * callback will invoked, response some information to host
     * reporting the result of the operation.
     *
     * @return the operation result
     */
    public boolean SportDataCmdHrpContinueParamRequest() {
        ZLogger.d(D, "SportDataCmdHrpContinueParamRequest");
        // generate key value data
        byte[] keyValue = null;
        // generate key data
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_SPORTS_HRP_CONTINUE_PARAMS_GET, keyValue);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_SPORTS_DATA, keyData);
        ZLogger.i(D, "appData: " + DataConverter.bytes2Hex(appPacketData));

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }


    /**
     * Fac Command use to enable led. Command: 0x06, Key: 0x05.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked,
     * reporting the result of the operation.
     *
     * @param led the led want to enable
     * @return the operation result
     */
    public boolean FACCmdEnableLed(byte led) {
        ZLogger.d(D, "FACCmdEnableLed");
        byte[] keyData;
        if (led != FAC_LED_CONTROL_ENABLE_ALL) {
            // generate key value data
            byte[] keyValue = {led};
            // generate key data
            keyData = ApplicationLayerKeyPacket.preparePacket(KEY_FAC_TEST_LED, keyValue);
        } else {
            keyData = ApplicationLayerKeyPacket.preparePacket(KEY_FAC_TEST_LED, null);
        }
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_FACTORY_TEST, keyData);
        ZLogger.i(D, "appData: " + DataConverter.bytes2Hex(appPacketData));

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }

    /**
     * Fac Command use to enable vibrate. Command: 0x06, Key: 0x06.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked,
     * reporting the result of the operation.
     *
     * @return the operation result
     */
    public boolean FACCmdEnableVibrate() {
        // generate key value data
        //byte[] keyValue = packet.getPacket();
        // generate key data
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_FAC_TEST_MOTO, null);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_FACTORY_TEST, keyData);
        ZLogger.i(D, "appData: " + DataConverter.bytes2Hex(appPacketData) + "\n, length: " + appPacketData.length);

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }

    /**
     * Fac Command use to request sensor data. Command: 0x06, Key: 0x0d.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked,
     * reporting the result of the operation.
     * then the remote will response some information, the {@link ApplicationLayerCallback#onFACCmdSensorData}
     * and {@link ApplicationLayerCallback#onSportDataCmdSleepData}
     * callback will invoked, response some information to host
     *
     * @return the operation result
     */
    public boolean FACCmdRequestSensorData() {
        // generate key value data
        //byte[] keyValue = packet.getPacket();
        // generate key data
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_FAC_TEST_SENSOR_DATA_REQ, null);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_FACTORY_TEST, keyData);
        ZLogger.i(D, "appData: " + DataConverter.bytes2Hex(appPacketData));

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }

    /**
     * Fac Command use to enter test mode. Command: 0x06, Key: 0x0e.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked,
     * reporting the result of the operation.
     *
     * @param key the 32 byte token key
     * @return the operation result
     */
    public boolean FACCmdEnterTestMode(byte[] key) {
        byte[] keyData;
        if (key != null) {
            if (key.length != 32) {
                ZLogger.d(D, "The length is not right.");
                return false;
            }
            byte[] keyValue = new byte[32];
            // generate key value data
            System.arraycopy(key, 0, keyValue, 0, 32);
            // generate key data
            keyData = ApplicationLayerKeyPacket.preparePacket(KEY_FAC_TEST_ENTER_SPUER_KEY, keyValue);
        } else {
            keyData = ApplicationLayerKeyPacket.preparePacket(KEY_FAC_TEST_ENTER_SPUER_KEY, null);
        }
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_FACTORY_TEST, keyData);
        ZLogger.i(D, "appData: " + DataConverter.bytes2Hex(appPacketData));

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }

    /**
     * Fac Command use to exit test mode. Command: 0x06, Key: 0x0e.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked,
     * reporting the result of the operation.
     *
     * @param key the 32 byte token key
     * @return the operation result
     */
    public boolean FACCmdExitTestMode(byte[] key) {
        byte[] keyData;
        if (key != null) {
            if (key.length != 32) {
                ZLogger.d(D, "The length is not right.");
                return false;
            }
            byte[] keyValue = new byte[32];
            // generate key value data
            System.arraycopy(key, 0, keyValue, 0, 32);
            // generate key data
            keyData = ApplicationLayerKeyPacket.preparePacket(KEY_FAC_TEST_LEAVE_SPUER_KEY, keyValue);
        } else {
            keyData = ApplicationLayerKeyPacket.preparePacket(KEY_FAC_TEST_LEAVE_SPUER_KEY, null);
        }
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_FACTORY_TEST, keyData);
        ZLogger.i(D, "appData: " + DataConverter.bytes2Hex(appPacketData));

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }

    /**
     * Send control cmd to the remote. Command: 0x07, Key: 0x11.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked,
     * reporting the result of the operation.
     *
     * @param cmd cmd.
     * @return the operation result
     */
    public boolean ControlCmdCameraControl(byte cmd) {
        ZLogger.d(D, "cmd: " + cmd);
        // generate key value data
        byte[] keyValue = {cmd};
        // generate key data
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_CTRL_APP_REQ, keyValue);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_CTRL, keyData);
        ZLogger.i(D, "appData: " + DataConverter.bytes2Hex(appPacketData));

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }

    /**
     * Log Command use to open log. Command: 0x0a, Key: 0x01.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked,
     * reporting the result of the operation.
     *
     * @param key the 32 byte token key
     * @return the operation result
     */
    public boolean LogCmdOpenLog(byte[] key) {
        ZLogger.d(D, "LogCmdOpenLog");
        byte[] keyData;
        if (key != null) {
            byte[] keyValue = new byte[key.length];
            // generate key value data
            System.arraycopy(key, 0, keyValue, 0, key.length);
            // generate key data
            keyData = ApplicationLayerKeyPacket.preparePacket(KEY_LOG_FUNC_OPEN, keyValue);
        } else {
            keyData = ApplicationLayerKeyPacket.preparePacket(KEY_LOG_FUNC_OPEN, null);
        }
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_LOG, keyData);
        ZLogger.i(D, "appData: " + DataConverter.bytes2Hex(appPacketData));

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }

    /**
     * Log Command use to close log. Command: 0x0a, Key: 0x02.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked,
     * reporting the result of the operation.
     *
     * @return the operation result
     */
    public boolean LogCmdCloseLog() {
        ZLogger.d(D, "LogCmdCloseLog");
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_LOG_FUNC_CLOSE, null);
        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_LOG, keyData);
        ZLogger.i(D, "appData: " + DataConverter.bytes2Hex(appPacketData));

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }

    /**
     * Log Command use to request log. Command: 0x0a, Key: 0x04.
     * <p>This is an asynchronous operation. Once the operation has been completed, the
     * {@link ApplicationLayerCallback#onCommandSend} callback is invoked,
     * reporting the result of the operation.
     *
     * @param key the 32 byte token key
     * @return the operation result
     */
    public boolean LogCmdRequestLog(byte key) {
        ZLogger.d(D, "LogCmdRequestLog");
        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_LOG_REQ, new byte[]{key});

        // generate application layer packet
        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_LOG, keyData);
        ZLogger.i(D, "appData: " + DataConverter.bytes2Hex(appPacketData));

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }

    /**
     * request classic address
     *
     * @return
     */
    public boolean RequestClassicAddress() {
        ZLogger.d(D, "RequestClassicAddress");

        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_SETTING_CLASSIC_ADDRESS, null);

        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_SETTING, keyData);
        ZLogger.i(D, "appData: " + DataConverter.bytes2Hex(appPacketData));

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }

    /**
     * request classic status
     *
     * @return
     */
    public boolean RequestClassicStatus() {
        ZLogger.d(D, "RequestClassicStatus");

        byte[] keyData = ApplicationLayerKeyPacket.preparePacket(KEY_SETTING_GET_CLASSIC_STATUS, null);

        byte[] appPacketData = ApplicationLayerPacket.preparePacket(CMD_SETTING, keyData);
        ZLogger.i(D, "appData: " + DataConverter.bytes2Hex(appPacketData));

        // sent the data
        return mTransportLayer.sendData(appPacketData);
    }


    TransportLayerCallback mTransportCallback = new TransportLayerCallback() {
        @Override
        public void onConnectionStateChange(final boolean status, final boolean newState) {
            ZLogger.d(D, "onConnectionStateChange, status: " + status + ", newState: " + newState);
            mCallback.onConnectionStateChange(status, newState);
        }

        @Override
        public void onDataSend(final boolean status, byte[] data) {
            ZLogger.d(D, "onDataSend, status: " + status);
            // decode the packet
            ApplicationLayerPacket appPacket = new ApplicationLayerPacket();
            appPacket.parseData(data);
            // dispatch the key value
            List<ApplicationLayerKeyPacket> keyPackets = appPacket.getKeyPacketArrays();
            for (ApplicationLayerKeyPacket keyPacket : keyPackets) {
                byte commandId = appPacket.getCommandId();
                byte keyId = keyPacket.getKey();
                ZLogger.d(D, "onDataSend, commandId: " + commandId + ", keyId: " + keyId);
                mCallback.onCommandSend(status, commandId, keyId);
            }

        }

        @Override
        public void onDataReceive(byte[] data) {
            ZLogger.d(D, "onDataReceive, data length: " + data.length);
            // decode the packet
            ApplicationLayerPacket appPacket = new ApplicationLayerPacket();
            appPacket.parseData(data);

            // dispatch the key value
            List<ApplicationLayerKeyPacket> keyPackets = appPacket.getKeyPacketArrays();
            for (ApplicationLayerKeyPacket keyPacket : keyPackets) {
                byte commandId = appPacket.getCommandId();
                byte keyId = keyPacket.getKey();
                byte[] keyData = keyPacket.getKeyData();
                ZLogger.d(D, "onDataReceive, commandId: " + commandId + ", keyId: " + keyId);
                // check the command id
                switch (commandId) {
                    case CMD_IMAGE_UPDATE:
                        // check the key id
                        switch (keyId) {
                            case KEY_UPDATE_RESPONSE:
                                mCallback.onUpdateCmdRequestEnterOtaMode(keyData[0], keyData[1]);
                                break;

                            default:
                                ZLogger.w(D, "onDataReceive, unknown key id: " + keyId);
                                break;
                        }
                        break;
                    case CMD_SETTING:
                        // check the key id
                        switch (keyId) {
                            case KEY_SETTING_GET_ALARM_LIST_RSP:
                                // parse the alarm list
                                ApplicationLayerAlarmsPacket alarms = new ApplicationLayerAlarmsPacket();
                                alarms.parseData(keyData);
                                mCallback.onSettingCmdRequestAlarmList(alarms);
                                break;
                            case KEY_SETTING_NOTIFY_SWITCH_RSP:
                                ReminderFunction reminderFunction = new ReminderFunction();
                                reminderFunction.parseData2(keyData);
                                mCallback.onSettingCmdRequestNotifySwitch(reminderFunction);
                                break;
                            case KEY_SETTING_SIT_TOOLONG_SWITCH_RSP:
                                mCallback.onSettingCmdRequestLongSit(keyData[0] == LONG_SIT_CONTROL_ENABLE);
                                break;
                            case KEY_SETTING_TURN_OVER_WRIST_RSP:
                                mCallback.onTurnOverWristSettingReceive(keyData[0] == TURN_OVER_WRIST_CONTROL_ENABLE);
                                break;
                            case KEY_DEVICE_FUNCTION_RSP:
                                //parse the function data
                                ApplicationLayerFunctionPacket functionPacket = new ApplicationLayerFunctionPacket();
                                functionPacket.parseData(keyData);
                                mCallback.onDeviceFunction(functionPacket.getDeviceFunction());
                                mCallback.onReminderFunction(functionPacket.getReminderFunction());
                                mCallback.onSportFunction(functionPacket.getSportFunction());
                                break;
                            case KEY_HOUR_SYSTEM_RSP:
                                mCallback.onHour(keyData[0] == HOUR_24_MODEL);
                                break;
                            case KEY_UNIT_SYSTEM_RSP:
                                mCallback.onUnit(keyData[0] == UNIT_METRIC_SYSTEM);
                                break;
                            case KEY_DISTURB_RSP:
                                mCallback.onDisturb(keyData[0] == DISTURB_ENABLE);
                                break;
                            case KEY_SCREEN_LIGHT_DURATION_RSP:
                                mCallback.onScreenLightDuration(keyData[0] & 0xFF);
                                break;
                            case KEY_LANGUAGE_RSP:
                                DeviceLanguage language;
                                int languageCode = keyData[0] & 0xFF;
                                switch (languageCode) {
                                    case 0:
                                        language = DeviceLanguage.LANGUAGE_ENGLISH;
                                        break;
                                    case 1:
                                        language = DeviceLanguage.LANGUAGE_SAMPLE_CHINESE;
                                        break;
                                    case 2:
                                        language = DeviceLanguage.LANGUAGE_TRADITIONAL_CHINESE;
                                        break;
                                    case 87:
                                        language = DeviceLanguage.LANGUAGE_SPANISH;
                                        break;
                                    case 20:
                                        language = DeviceLanguage.LANGUAGE_FRENCH;
                                        break;
                                    case 18:
                                        language = DeviceLanguage.LANGUAGE_GERMAN;
                                        break;
                                    case 96:
                                        language = DeviceLanguage.LANGUAGE_ITALIAN;
                                        break;
                                    case 65:
                                        language = DeviceLanguage.LANGUAGE_JAPANESE;
                                        break;
                                    default:
                                        language = DeviceLanguage.LANGUAGE_ENGLISH;
                                        break;
                                }
                                mCallback.onLanguage(language);
                                break;
                            default:
                                ZLogger.i(D, "onDataReceive, unknown key id: " + keyId);
                                break;
                        }
                        break;
                    case CMD_BOND_REG:
                        // check the key id
                        switch (keyId) {
                            case KEY_BOND_RSP:
                                mCallback.onBondCmdRequestBond(keyData[0]);
                                break;
                            case KEY_LOGIN_RSP:
                                mCallback.onBondCmdRequestLogin(keyData[0]);
                                break;
                            default:
                                ZLogger.i(D, "onDataReceive, unknown key id: " + keyId);
                                break;
                        }
                        break;
                    case CMD_NOTIFY:
                        switch (keyId) {
                            case KEY_NOTIFY_END_CALL:
                                mCallback.onEndCallReceived();
                                break;
                            case KEY_NOTIFY_CLASSIC_ADDRESS:
                                ZLogger.i(D, "cmd id = " + commandId + "key id = " + keyId);
                                ZLogger.i(D, keyData.length + "");
                                ZLogger.i(D, DataConverter.bytes2Hex(keyData));
                                String mac = StringByteTrans.byte2Mac(keyData);
                                mCallback.onClassAddress(mac);
                                break;
                            case KEY_NOTIFY_CLASSIC_STATUS:
                                int status = keyData[0] & 0xFF;
                                mCallback.onClassicStatus(status);
                                break;
                            default:
                                ZLogger.i(D, "onDataReceive, unknown key id: " + keyId);
                                break;
                        }
                        break;
                    case CMD_SPORTS_DATA:
                        // check the key id
                        switch (keyId) {
                            case KEY_SPORTS_RUNNIG_RSP:
                                // parse the alarm list
                                ApplicationLayerStepPacket step = new ApplicationLayerStepPacket();
                                step.parseData(keyData);
                                mCallback.onSportDataCmdStepData(step);
                                break;
                            case KEY_SPORTS_SLEEP_RSP:
                                // parse the alarm list
                                ApplicationLayerSleepPacket sleep = new ApplicationLayerSleepPacket();
                                sleep.parseData(keyData);
                                mCallback.onSportDataCmdSleepData(sleep);
                                break;
                            case KEY_SPORTS_RUNNIG_RSP_MORE:
                                mCallback.onSportDataCmdMoreData();
                                break;
                            case KEY_SPORTS_SLEEP_SET_RSP:
                                // parse the alarm list
                                ApplicationLayerSleepPacket sleepSet = new ApplicationLayerSleepPacket();
                                sleepSet.parseData(keyData);
                                mCallback.onSportDataCmdSleepSetData(sleepSet);
                                break;
                            case KEY_SPORTS_HIS_SYNC_BEG:
                                mCallback.onSportDataCmdHistorySyncBegin();
                                break;
                            case KEY_SPORTS_HIS_SYNC_END:
                                // Here need check have total data or not
                                ApplicationLayerTodaySumSportPacket todaySumSportPacket = new ApplicationLayerTodaySumSportPacket();

                                if (keyData.length != 0) {
                                    if (!todaySumSportPacket.parseData(keyData)) {
                                        todaySumSportPacket = null;
                                    }
                                } else {
                                    todaySumSportPacket = null;
                                }
                                mCallback.onSportDataCmdHistorySyncEnd(todaySumSportPacket);
                                break;
                            case KEY_SPORTS_HRP_DATA_RSP:
                                ApplicationLayerHrpPacket hrp = new ApplicationLayerHrpPacket();
                                hrp.parseData(keyData);
                                mCallback.onSportDataCmdHrpData(hrp);
                                break;
                            case KEY_SPORTS_BP_DATA_RSP:
                                ApplicationLayerBpPacket bp = new ApplicationLayerBpPacket();
                                bp.parseData(keyData);
                                mCallback.onSportDataCmdBpData(bp);
                                break;
                            case KEY_SPORTS_HRP_DEVICE_CANCEL_READ_HRP:
                                mCallback.onSportDataCmdDeviceCancelSingleHrpRead();
                                break;
                            case KEY_SPORTS_BP_CANCEL_READ:
                                mCallback.onSportDataCmdDeviceCancelSingleBpRead();
                                break;
                            case KEY_SPORTS_HRP_CONTINUE_PARAMS_RSP:
                                boolean hrpEnable = (keyData[0] == HRP_CONTINUE_REQ_MODE_ENABLE);
                                int hrpInterval = (keyData[1] & 0xff);
                                mCallback.onSportDataCmdHrpContinueParamRsp(hrpEnable, hrpInterval);
                                break;
                            case KEY_SPORTS_DATA_RSP:
                                ApplicationLayerSportPacket sport = new ApplicationLayerSportPacket();
                                sport.parseData(keyData);
                                mCallback.onSportDataCmdSportData(sport);
                                break;
                            default:
                                ZLogger.w(D, "onDataReceive, unknown key id: " + keyId);
                                break;
                        }
                        break;
                    case CMD_CTRL:
                        // check the key id
                        switch (keyId) {
                            case KEY_CTRL_PHOTO_RSP:
                                mCallback.onTakePhotoRsp();
                                break;
                            default:
                                ZLogger.w(D, "onDataReceive, unknown key id: " + keyId);
                                break;
                        }
                        break;
                    case CMD_FACTORY_TEST:
                        // check the key id
                        switch (keyId) {
                            case KEY_FAC_TEST_SENSOR_DATA_RSP:
                                // parse the alarm list
                                ApplicationLayerFacSensorPacket sensor = new ApplicationLayerFacSensorPacket();
                                sensor.parseData(keyData);
                                mCallback.onFACCmdSensorData(sensor);
                                break;
                            default:
                                ZLogger.w(D, "onDataReceive, unknown key id: " + keyId);
                                break;
                        }
                        break;
                    case CMD_LOG:
                        // check the key id
                        switch (keyId) {
                            case KEY_LOG_START:
                                // parse the log length
                                long logLength = 0;
                                for (int i = 0; i < keyData.length; i++) {
                                    logLength = (keyData[i] & 0xFF) << (8 * (keyData.length - (i + 1)));
                                }
                                mCallback.onLogCmdStart(logLength);
                                break;
                            case KEY_LOG_END:
                                mCallback.onLogCmdEnd();
                                break;
                            case KEY_LOG_RSP:
                                // parse the alarm list
                                ApplicationLayerLogResponsePacket logResponsePacket = new ApplicationLayerLogResponsePacket();
                                logResponsePacket.parseData(keyData);
                                mCallback.onLogCmdRsp(logResponsePacket);
                                break;
                            default:
                                ZLogger.w(D, "onDataReceive, unknown key id: " + keyId);
                                break;
                        }
                        break;
                    default:
                        ZLogger.w(D, "onDataReceive, unknown command id: " + commandId);
                        break;
                }
            }
        }

        @Override
        public void onNameReceive(final String data) {
            mCallback.onNameReceive(data);
        }
    };
}

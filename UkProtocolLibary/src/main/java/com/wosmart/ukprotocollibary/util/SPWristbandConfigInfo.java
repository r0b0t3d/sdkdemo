package com.wosmart.ukprotocollibary.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SPWristbandConfigInfo {
    private static boolean DEFAULT_GENDAR = false;
    private static int DEFAULT_AGE = 20;
    private static int DEFAULT_HEIGHT = 170;
    private static int DEFAULT_WEIGHT = 60;
    private static int DEFAULT_TOTAL_STEP = 7000;
    private static int DEFAULT_LONG_SIT_ALARM_TIME = 60;
    private static int DEFAULT_ALARM_TIME = 15;
    private static boolean DEFAULT_NOTIFY_FLAG_CALL = false;
    private static boolean DEFAULT_NOTIFY_FLAG_MESSAGE = false;
    private static boolean DEFAULT_NOTIFY_FLAG_QQ = false;
    private static boolean DEFAULT_NOTIFY_FLAG_WECHAT = false;
    private static boolean DEFAULT_CONTROL_SWITCH_LOST = false;
    private static boolean DEFAULT_CONTROL_SWITCH_LONG_SIT = false;
    private static boolean DEFAULT_CONTROL_TURN_OVER_WRIST = true;
    private static boolean DEFAULT_FIRST_INITIAL = false;


    public static int DEFAULT_INVALID_VALUE = -1;
    public static int DEFAULT_CONTINUE_HRP_INTERVAL = 5;


    private static String SP_KEY_GENDAR = "SPKeyGendar";
    private static String SP_KEY_AGE = "SPKeyAge";
    private static String SP_KEY_HEIGHT = "SPKeyHeight";
    private static String SP_KEY_WEIGHT = "SPKeyWeight";
    private static String SP_KEY_TOTAL_STEP = "SPKeyTotalStep";
    private static String SP_KEY_LONG_SIT_ALARM_TIME = "SPKeyLongSitAlarmTime";
    private static String SP_KEY_LOST_ALARM_TIME = "SPKeyAlarmTime";
    private static String SP_KEY_LOST_ALARM_MUSIC = "SPKeyAlarmMusic";
    private static String SP_KEY_NOTIFY_FLAG_CALL = "SPKeyNotifyCall";
    private static String SP_KEY_NOTIFY_FLAG_MESSAGE = "SPKeyNotifyMessage";
    private static String SP_KEY_NOTIFY_FLAG_QQ = "SPKeyNotifyQQ";
    private static String SP_KEY_NOTIFY_FLAG_WECHAT = "SPKeyNotifyWechat";
    private static String SP_KEY_CONTROL_SWITCH_LOST = "SPKeyControlSwitchLost";
    private static String SP_KEY_CONTROL_SWITCH_LONG_SIT = "SPKeyControlSwitchLongSit";
    private static String SP_KEY_CONTROL_SWITCH_TURN_OVER_WRIST = "SPKeyControlSwitchTurnOverWrist";
    private static String SP_KEY_AVATAR_PATH = "SPKeyAvatarPath";
    private static String SP_KEY_NAME = "SPKeyName";
    private static String SP_KEY_BONDED_DEVICE = "SPKeyBondedDevice";
    private static String SP_KEY_ALARM_CONTROL_ONE = "SPKeyAlarmControlOne";
    private static String SP_KEY_ALARM_TIME_ONE = "SPKeyAlarmTimeOne";
    private static String SP_KEY_ALARM_FLAG_ONE = "SPKeyAlarmFlagOne";
    private static String SP_KEY_ALARM_CONTROL_TWO = "SPKeyAlarmControlTwo";
    private static String SP_KEY_ALARM_TIME_TWO = "SPKeyAlarmTimeTwo";
    private static String SP_KEY_ALARM_FLAG_TWO = "SPKeyAlarmFlagTwo";
    private static String SP_KEY_ALARM_CONTROL_THREE = "SPKeyAlarmControlThree";
    private static String SP_KEY_ALARM_TIME_THREE = "SPKeyAlarmTimeThree";
    private static String SP_KEY_ALARM_FLAG_THREE = "SPKeyAlarmFlagThree";
    private static String SP_KEY_FIRST_INITIAL_FLAG = "SPKeyFirstInitialFlag";
    private static String SP_KEY_FIRST_OTA_START_FLAG = "SPKeyFirstOTAStartFlag";
    private static String SP_KEY_FIRST_APP_START_FLAG = "SPKeyFirstAPPStartFlag";
    private static String SP_KEY_FIRST_SPLASH_START_FLAG = "SPKeyFirstSplashStartFlag";
    private static String SP_KEY_USER_ID = "SPKeyUserId";
    private static String SP_KEY_USER_NAME = "SPKeyUserName";
    private static String SP_KEY_USER_PSW = "SPKeyUserPsw";
    private static String SP_KEY_DEBUG_LOG_TYPE_MODULE_APP = "SPKeyDebugLogTypeModuleApp";
    private static String SP_KEY_DEBUG_LOG_TYPE_MODULE_LOWER_STACK = "SPKeyDebugLogTypeModuleLowerStack";
    private static String SP_KEY_DEBUG_LOG_TYPE_MODULE_UPPER_STACK = "SPKeyDebugLogTypeModuleUpperStack";
    private static String SP_KEY_DEBUG_LOG_TYPE_SLEEP = "SPKeyDebugLogTypeSleep";
    private static String SP_KEY_DEBUG_LOG_TYPE_SPORT = "SPKeyDebugLogTypeSport";
    private static String SP_KEY_DEBUG_LOG_TYPE_CONFIG = "SPKeyDebugLogTypeConfig";
    private static String SP_KEY_CONTINUE_HRP_CONTROL = "SPKeyContinueHrpControl";
    private static String SP_KEY_CONTINUE_HRP_CONTROL_INTERVAL = "SPKeyContinueHrpControlInterval";

    private static String SP_KEY_ADJUST_TARGET_STEP = "SPKeyAdjustTargetStep";
    private static String SP_KEY_ADJUST_TARGET_DISTANCE = "SPKeyAdjustTargetDistance";
    private static String SP_KEY_ADJUST_TARGET_CAL = "SPKeyAdjustTargetCal";

    // For Internet mode
    private static String SP_KEY_BMOB_LAST_HISTORY_SYNC_DATE = "SPKeyBmobLastHistorySyncDate";
    private static String SP_KEY_BMOB_LAST_SYNC_DATE = "SPKeyBmobLastSyncDate";

    private static String SP_WRISTBAND_CONFIG_INFO = "SPWristbandConfigInfo";

    public static boolean getGendar(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        boolean sex = sp.getBoolean(SP_KEY_GENDAR, DEFAULT_GENDAR);
        return sex;
    }

    public static int getAge(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        int value = sp.getInt(SP_KEY_AGE, DEFAULT_AGE);
        return value;
    }

    public static int getHeight(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        int value = sp.getInt(SP_KEY_HEIGHT, DEFAULT_HEIGHT);
        return value;
    }

    public static int getWeight(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        int value = sp.getInt(SP_KEY_WEIGHT, DEFAULT_WEIGHT);
        return value;
    }

    public static int getTotalStep(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        int value = sp.getInt(SP_KEY_TOTAL_STEP, DEFAULT_TOTAL_STEP);
        return value;
    }

    public static int getLongSitAlarmTime(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        int value = sp.getInt(SP_KEY_LONG_SIT_ALARM_TIME, DEFAULT_LONG_SIT_ALARM_TIME);
        return value;
    }

    public static int getLostAlarmTime(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        int value = sp.getInt(SP_KEY_LOST_ALARM_TIME, DEFAULT_ALARM_TIME);
        return value;
    }

    public static String getLostAlarmMusic(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        String value = sp.getString(SP_KEY_LOST_ALARM_MUSIC, null);
        return value;
    }

    public static boolean getNotifyCallFlag(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        boolean value = sp.getBoolean(SP_KEY_NOTIFY_FLAG_CALL, DEFAULT_NOTIFY_FLAG_CALL);
        return value;
    }

    public static boolean getNotifyMessageFlag(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        boolean value = sp.getBoolean(SP_KEY_NOTIFY_FLAG_MESSAGE, DEFAULT_NOTIFY_FLAG_MESSAGE);
        return value;
    }

    public static boolean getNotifyQQFlag(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        boolean value = sp.getBoolean(SP_KEY_NOTIFY_FLAG_QQ, DEFAULT_NOTIFY_FLAG_QQ);
        return value;
    }

    public static boolean getNotifyWechatFlag(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        boolean value = sp.getBoolean(SP_KEY_NOTIFY_FLAG_WECHAT, DEFAULT_NOTIFY_FLAG_WECHAT);
        return value;
    }

    public static boolean getControlSwitchLost(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        boolean value = sp.getBoolean(SP_KEY_CONTROL_SWITCH_LOST, DEFAULT_CONTROL_SWITCH_LOST);
        return value;
    }

    public static boolean getControlSwitchLongSit(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        boolean value = sp.getBoolean(SP_KEY_CONTROL_SWITCH_LONG_SIT, DEFAULT_CONTROL_SWITCH_LONG_SIT);
        return value;
    }

    public static boolean getControlSwitchTurnOverWrist(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        boolean value = sp.getBoolean(SP_KEY_CONTROL_SWITCH_TURN_OVER_WRIST, DEFAULT_CONTROL_TURN_OVER_WRIST);
        return value;
    }

    public static String getAvatarPath(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        String value = sp.getString(SP_KEY_AVATAR_PATH, null);
        return value;
    }

    public static String getName(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        String value = sp.getString(SP_KEY_NAME, null);
        return value;
    }

    public static String getBondedDevice(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        String value = sp.getString(SP_KEY_BONDED_DEVICE, null);
        return value;
    }

    public static Boolean getAlarmControlOne(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        Boolean value = sp.getBoolean(SP_KEY_ALARM_CONTROL_ONE, false);
        return value;
    }

    public static String getAlarmTimeOne(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        String value = sp.getString(SP_KEY_ALARM_TIME_ONE, null);
        return value;
    }

    public static byte getAlarmFlagOne(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        int value = sp.getInt(SP_KEY_ALARM_FLAG_ONE, 0);
        byte ret = (byte) (value & 0xff);
        return ret;
    }

    public static Boolean getAlarmControlTwo(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        Boolean value = sp.getBoolean(SP_KEY_ALARM_CONTROL_TWO, false);
        return value;
    }

    public static String getAlarmTimeTwo(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        String value = sp.getString(SP_KEY_ALARM_TIME_TWO, null);
        return value;
    }

    public static byte getAlarmFlagTwo(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        int value = sp.getInt(SP_KEY_ALARM_FLAG_TWO, 0);
        byte ret = (byte) (value & 0xff);
        return ret;
    }

    public static Boolean getAlarmControlThree(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        Boolean value = sp.getBoolean(SP_KEY_ALARM_CONTROL_THREE, false);
        return value;
    }

    public static String getAlarmTimeThree(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        String value = sp.getString(SP_KEY_ALARM_TIME_THREE, null);
        return value;
    }

    public static byte getAlarmFlagThree(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        int value = sp.getInt(SP_KEY_ALARM_FLAG_THREE, 0);
        byte ret = (byte) (value & 0xff);
        return ret;
    }

    public static boolean getFirstInitialFlag(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        boolean value = sp.getBoolean(SP_KEY_FIRST_INITIAL_FLAG, DEFAULT_FIRST_INITIAL);
        return value;
    }

    public static boolean getFirstOTAStartFlag(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        boolean value = sp.getBoolean(SP_KEY_FIRST_OTA_START_FLAG, true);
        return value;
    }

    public static boolean getFirstAppStartFlag(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        boolean value = sp.getBoolean(SP_KEY_FIRST_APP_START_FLAG, true);
        return value;
    }

    public static boolean getFirstSplashStartFlag(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        boolean value = sp.getBoolean(SP_KEY_FIRST_SPLASH_START_FLAG, true);
        return value;
    }

    public static String getUserId(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        String value = sp.getString(SP_KEY_USER_ID, null);
        return value;
    }

    public static String getUserName(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        String value = sp.getString(SP_KEY_USER_NAME, null);
        return value;
    }

    public static String getUserPsw(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        String value = sp.getString(SP_KEY_USER_PSW, null);
        return value;
    }

    public static String getBmobLastHistorySyncDate(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        String value = sp.getString(SP_KEY_BMOB_LAST_HISTORY_SYNC_DATE, null);
        return value;
    }

    public static String getBmobLastSyncDate(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        String value = sp.getString(SP_KEY_BMOB_LAST_SYNC_DATE, null);
        return value;
    }

    public static String getInfoKeyValue(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        String value = sp.getString(key, null);
        return value;
    }

    public static boolean getDebugLogTypeModuleApp(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        boolean value = sp.getBoolean(SP_KEY_DEBUG_LOG_TYPE_MODULE_APP, true);
        return value;
    }

    public static boolean getDebugLogTypeModuleLowerStack(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        boolean value = sp.getBoolean(SP_KEY_DEBUG_LOG_TYPE_MODULE_LOWER_STACK, true);
        return value;
    }

    public static boolean getDebugLogTypeModuleUpperStack(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        boolean value = sp.getBoolean(SP_KEY_DEBUG_LOG_TYPE_MODULE_UPPER_STACK, true);
        return value;
    }

    public static boolean getDebugLogTypeSleep(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        boolean value = sp.getBoolean(SP_KEY_DEBUG_LOG_TYPE_SLEEP, true);
        return value;
    }

    public static boolean getDebugLogTypeSport(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        boolean value = sp.getBoolean(SP_KEY_DEBUG_LOG_TYPE_SPORT, true);
        return value;
    }

    public static boolean getDebugLogTypeConfig(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        boolean value = sp.getBoolean(SP_KEY_DEBUG_LOG_TYPE_CONFIG, true);
        return value;
    }

    public static long getAdjustTargetStep(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        long value = sp.getLong(SP_KEY_ADJUST_TARGET_STEP, DEFAULT_INVALID_VALUE);
        return value;
    }

    public static long getAdjustTargetDistance(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        long value = sp.getLong(SP_KEY_ADJUST_TARGET_DISTANCE, DEFAULT_INVALID_VALUE);
        return value;
    }

    public static long getAdjustTargetCal(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        long value = sp.getLong(SP_KEY_ADJUST_TARGET_CAL, DEFAULT_INVALID_VALUE);
        return value;
    }

    public static boolean getContinueHrpControl(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        boolean value = sp.getBoolean(SP_KEY_CONTINUE_HRP_CONTROL, false);
        return value;
    }

    public static int getContinueHrpControlInterval(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        int value = sp.getInt(SP_KEY_CONTINUE_HRP_CONTROL_INTERVAL, DEFAULT_CONTINUE_HRP_INTERVAL);
        return value;
    }


    public static void setGendar(Context context, Boolean v) {
        if (v == null) {
            v = DEFAULT_GENDAR;
        }
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean(SP_KEY_GENDAR, v);
        ed.apply();
    }

    public static void setAge(Context context, Integer v) {
        if (v == null) {
            v = DEFAULT_AGE;
        }
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putInt(SP_KEY_AGE, v);
        ed.apply();
    }

    public static void setHeight(Context context, Integer v) {
        if (v == null) {
            v = DEFAULT_HEIGHT;
        }
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putInt(SP_KEY_HEIGHT, v);
        ed.apply();
    }

    public static void setWeight(Context context, Integer v) {
        if (v == null) {
            v = DEFAULT_WEIGHT;
        }
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putInt(SP_KEY_WEIGHT, v);
        ed.apply();
    }

    public static void setTotalStep(Context context, Integer v) {
        if (v == null) {
            v = DEFAULT_TOTAL_STEP;
        }
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putInt(SP_KEY_TOTAL_STEP, v);
        ed.apply();
    }

    public static void setLongSitAlarmTime(Context context, Integer v) {
        if (v == null) {
            v = DEFAULT_LONG_SIT_ALARM_TIME;
        }
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putInt(SP_KEY_LONG_SIT_ALARM_TIME, v);
        ed.apply();
    }

    public static void setLostAlarmTime(Context context, Integer v) {
        if (v == null) {
            v = DEFAULT_LONG_SIT_ALARM_TIME;
        }
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putInt(SP_KEY_LOST_ALARM_TIME, v);
        ed.apply();
    }

    public static void setLostAlarmMusic(Context context, String v) {
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString(SP_KEY_LOST_ALARM_MUSIC, v);
        ed.apply();
    }

    public static void setNotifyCallFlag(Context context, Boolean v) {
        if (v == null) {
            v = DEFAULT_NOTIFY_FLAG_CALL;
        }
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean(SP_KEY_NOTIFY_FLAG_CALL, v);
        ed.apply();
    }

    public static void setNotifyMessageFlag(Context context, Boolean v) {
        if (v == null) {
            v = DEFAULT_NOTIFY_FLAG_MESSAGE;
        }
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean(SP_KEY_NOTIFY_FLAG_MESSAGE, v);
        ed.apply();
    }

    public static void setNotifyQQFlag(Context context, Boolean v) {
        if (v == null) {
            v = DEFAULT_NOTIFY_FLAG_QQ;
        }
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean(SP_KEY_NOTIFY_FLAG_QQ, v);
        ed.apply();
    }

    public static void setNotifyWechatFlag(Context context, Boolean v) {
        if (v == null) {
            v = DEFAULT_NOTIFY_FLAG_WECHAT;
        }
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean(SP_KEY_NOTIFY_FLAG_WECHAT, v);
        ed.apply();
    }

    public static void setControlSwitchLost(Context context, Boolean v) {
        if (v == null) {
            v = DEFAULT_CONTROL_SWITCH_LOST;
        }
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean(SP_KEY_CONTROL_SWITCH_LOST, v);
        ed.apply();
    }

    public static void setControlSwitchLongSit(Context context, Boolean v) {
        if (v == null) {
            v = DEFAULT_CONTROL_SWITCH_LONG_SIT;
        }
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean(SP_KEY_CONTROL_SWITCH_LONG_SIT, v);
        ed.apply();
    }

    public static void setControlSwitchTurnOverWrist(Context context, Boolean v) {
        if (v == null) {
            v = DEFAULT_CONTROL_TURN_OVER_WRIST;
        }
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean(SP_KEY_CONTROL_SWITCH_TURN_OVER_WRIST, v);
        ed.apply();
    }

    public static void setAvatarPath(Context context, String v) {
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString(SP_KEY_AVATAR_PATH, v);
        ed.apply();
    }

    public static void setName(Context context, String v) {
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString(SP_KEY_NAME, v);
        ed.apply();
    }

    public static void setBondedDevice(Context context, String v) {
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString(SP_KEY_BONDED_DEVICE, v);
        ed.apply();
    }

    public static void setAlarmControlOne(Context context, Boolean v) {
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean(SP_KEY_ALARM_CONTROL_ONE, v);
        ed.apply();
    }

    public static void setAlarmTimeOne(Context context, String v) {
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString(SP_KEY_ALARM_TIME_ONE, v);
        ed.apply();
    }

    public static void setAlarmFlagOne(Context context, byte v) {
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putInt(SP_KEY_ALARM_FLAG_ONE, v);
        ed.apply();
    }

    public static void setAlarmControlTwo(Context context, Boolean v) {
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean(SP_KEY_ALARM_CONTROL_TWO, v);
        ed.apply();
    }

    public static void setAlarmTimeTwo(Context context, String v) {
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString(SP_KEY_ALARM_TIME_TWO, v);
        ed.apply();
    }

    public static void setAlarmFlagTwo(Context context, byte v) {
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putInt(SP_KEY_ALARM_FLAG_TWO, v);
        ed.apply();
    }

    public static void setAlarmControlThree(Context context, Boolean v) {
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean(SP_KEY_ALARM_CONTROL_THREE, v);
        ed.apply();
    }

    public static void setAlarmTimeThree(Context context, String v) {
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString(SP_KEY_ALARM_TIME_THREE, v);
        ed.apply();
    }

    public static void setAlarmFlagThree(Context context, byte v) {
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putInt(SP_KEY_ALARM_FLAG_THREE, v);
        ed.apply();
    }

    public static void setFirstInitialFlag(Context context, boolean v) {
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean(SP_KEY_FIRST_INITIAL_FLAG, v);
        ed.apply();
    }

    public static void setFirstOtaStartFlag(Context context, boolean v) {
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean(SP_KEY_FIRST_OTA_START_FLAG, v);
        ed.apply();
    }

    public static void setFirstAppStartFlag(Context context, boolean v) {
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean(SP_KEY_FIRST_APP_START_FLAG, v);
        ed.apply();
    }

    public static void setFirstSplashStartFlag(Context context, boolean v) {
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean(SP_KEY_FIRST_SPLASH_START_FLAG, v);
        ed.apply();
    }

    public static void setUserId(Context context, String v) {
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString(SP_KEY_USER_ID, v);
        ed.apply();
    }

    public static void setUserName(Context context, String v) {
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString(SP_KEY_USER_NAME, v);
        ed.apply();
    }

    public static void setUserPsw(Context context, String v) {
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString(SP_KEY_USER_PSW, v);
        ed.apply();
    }

    public static void setBmobLastHistorySyncDate(Context context, String v) {
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString(SP_KEY_BMOB_LAST_HISTORY_SYNC_DATE, v);
        ed.apply();
    }

    // First login must set this value
    public static void setBmobLastSyncDate(Context context, String v) {
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString(SP_KEY_BMOB_LAST_SYNC_DATE, v);
        ed.apply();
    }

    public static void setDebugLogTypeModuleApp(Context context, boolean v) {
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean(SP_KEY_DEBUG_LOG_TYPE_MODULE_APP, v);
        ed.apply();
    }

    public static void setDebugLogTypeModuleLowerStack(Context context, boolean v) {
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean(SP_KEY_DEBUG_LOG_TYPE_MODULE_LOWER_STACK, v);
        ed.apply();
    }

    public static void setDebugLogTypeModuleUpperStack(Context context, boolean v) {
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean(SP_KEY_DEBUG_LOG_TYPE_MODULE_UPPER_STACK, v);
        ed.apply();
    }


    public static void setDebugLogTypeSleep(Context context, boolean v) {
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean(SP_KEY_DEBUG_LOG_TYPE_SLEEP, v);
        ed.apply();
    }

    public static void setDebugLogTypeSport(Context context, boolean v) {
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean(SP_KEY_DEBUG_LOG_TYPE_SPORT, v);
        ed.apply();
    }

    public static void setDebugLogTypeConfig(Context context, boolean v) {
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean(SP_KEY_DEBUG_LOG_TYPE_CONFIG, v);
        ed.apply();
    }

    public static void setInfoKeyValue(Context context, String key, String v) {
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString(key, v);
        ed.apply();
    }

    public static void setAdjustTargetStep(Context context, long v) {
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putLong(SP_KEY_ADJUST_TARGET_STEP, v);
        ed.apply();
    }

    public static void setAdjustTargetDistance(Context context, long v) {
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putLong(SP_KEY_ADJUST_TARGET_DISTANCE, v);
        ed.apply();
    }

    public static void setAdjustTargetCal(Context context, long v) {
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putLong(SP_KEY_ADJUST_TARGET_CAL, v);
        ed.apply();
    }

    public static void setContinueHrpControl(Context context, boolean v) {
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean(SP_KEY_CONTINUE_HRP_CONTROL, v);
        ed.apply();
    }

    public static void setContinueHrpControlInterval(Context context, int v) {

        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putInt(SP_KEY_CONTINUE_HRP_CONTROL_INTERVAL, v);
        ed.apply();
    }

    public static void deleteAll(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SP_WRISTBAND_CONFIG_INFO, Context.MODE_PRIVATE);
        sp.edit().clear().apply();
    }
}

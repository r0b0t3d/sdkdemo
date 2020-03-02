package com.wosmart.ukprotocollibary.util;

import android.content.Context;
import android.util.SparseArray;

import com.realsil.realteksdk.logger.ZLogger;
import com.wosmart.ukprotocollibary.applicationlayer.ApplicationLayer;
import com.wosmart.ukprotocollibary.applicationlayer.ApplicationLayerTodaySumSportPacket;
import com.wosmart.ukprotocollibary.model.hrp.HrpData;
import com.wosmart.ukprotocollibary.model.sleep.SleepData;
import com.wosmart.ukprotocollibary.model.sleep.SleepSubData;
import com.wosmart.ukprotocollibary.model.sleep.filter.SleepFilter;
import com.wosmart.ukprotocollibary.model.sleep.filter.SleepFilterData;
import com.wosmart.ukprotocollibary.model.sport.SportData;
import com.wosmart.ukprotocollibary.model.sport.SportSubData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class WristbandCalculator {
    private static final String TAG = "WristbandCalculator";
    private static final boolean D = true;


    /**
     * Get the hour-sportData key-value of the special date. The input data must
     * make sure every offset only have a valid data.
     *
     * @param sports the input data
     * @return the total hour-sportData key-value of the date.
     */
    public static SparseArray<SportSubData> getAllHourDataWithSameDate(List<SportData> sports) {
        SparseArray<SportSubData> hourData = new SparseArray<>();

        int hour;
        SportSubData subData;
        for (SportData sp : sports) {
            hour = ((sp.getOffset()) / 4) + 1;// offset start from 0
            if (hourData.get(hour) == null) {
                subData = new SportSubData(sp.getStepCount(),
                        sp.getCalory(),
                        sp.getDistance());
            } else {
                subData = hourData.get(hour);
                subData.setStepCount(sp.getStepCount() + subData.getStepCount());
                subData.setCalory(sp.getCalory() + subData.getCalory());
                subData.setDistance(sp.getDistance() + subData.getDistance());
            }
            // update the data
            hourData.put(hour, subData);
        }
        // display all the hour-SportSubData key-value
        for (int i = 0; i < hourData.size(); i++) {
            SportSubData val = hourData.get(i);
            if (val != null) {
                ZLogger.d(D, val.toString());
            }


        }
        return hourData;
    }


    /**
     * Get the hour-sportData key-value of the special date. The input can
     * be the origin data from database, it will select by date, and unique
     * data by the offset.
     *
     * @param y      the special year
     * @param m      the special month
     * @param d      the special day
     * @param sports the input data
     * @return the total hour-sportData key-value of the date.
     */
    public static SparseArray<SportSubData> getAllHourDataByDate(
            int y, int m, int d,
            List<SportData> sports) {
        // get the special date sport data
        List<SportData> sps = getSubSportDataByDate(y, m, d, sports);
        SportSubData subData = new SportSubData();
        if (sps == null) {
            ZLogger.e(D, "didn't find the data in list by date.");
            return null;
        }
        // get every offset data, and sort by offset
        List<SportData> offsetDataMap = getAllUniqueOffsetDataWithSameDate(sps);

        return getAllHourDataWithSameDate(offsetDataMap);
    }

    /**
     * Get sum of the sport data of the special date.
     *
     * @param y      the special year
     * @param m      the special month
     * @param d      the special day
     * @param sports the input data
     * @return the total sport data of the date.
     */
    public static SportSubData sumOfSportDataByDate(
            int y, int m, int d,
            List<SportData> sports) {
        // get the special date sport data
        List<SportData> sps = getSubSportDataByDate(y, m, d, sports);
        SportSubData subData = new SportSubData();
        if (sps == null) {
            ZLogger.e(D, "didn't find the data in list by date.");
            return null;
        }
        // get every offset data, and sort by offset
        HashMap<Integer, SportData> offsetDataMap = getAllOffsetDataWithSameDate(sps);

        // iterator all the offset-SportData key-value
        Iterator iter = offsetDataMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            //Object key = entry.getKey();
            SportData val = (SportData) entry.getValue();
            subData.setStepCount(subData.getStepCount() + val.getStepCount());
            subData.setCalory(subData.getCalory() + val.getCalory());
            subData.setDistance(subData.getDistance() + val.getDistance());
        }
        ZLogger.i(D, "year: " + y
                + ", month: " + m
                + ", day: " + d
                + ", sub sport data: " + subData.toString());
        return subData;
    }

    /**
     * Get all the Unique SportData(One Offset have only one data), The input data must be the same date,
     * if not, it will return error result.
     *
     * @param sports the input data
     * @return All the offset-SportData key-value.
     */
    public static List<SportData> getAllUniqueOffsetDataWithSameDate(List<SportData> sports) {
        ArrayList<SportData> offsetDataMap = new ArrayList<SportData>();
        HashMap<Integer, ArrayList<SportData>> map = getAllOffsetDataListWithSameDate(sports);
        if (map == null) {
            ZLogger.e(D, "map empty.");
            return null;
        }

        // iterator all the offset-SportData key-value
        Iterator iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Integer key = (Integer) entry.getKey();
            ArrayList<SportData> val = (ArrayList<SportData>) entry.getValue();

            SportData offsetVal = findLastDataInOffsetByDate(val);
            offsetDataMap.add(offsetVal);
        }

        if (offsetDataMap == null) {
            ZLogger.e(D, "offsetDataMap empty.");
            return null;
        }

        return offsetDataMap;
    }


    /**
     * Get all the offset-SportData key-value, The input data must be the same date,
     * if not, it will return error result.
     *
     * @param sports the input data
     * @return All the offset-SportData key-value.
     */
    public static HashMap<Integer, SportData> getAllOffsetDataWithSameDate(List<SportData> sports) {
        HashMap<Integer, SportData> offsetDataMap = new HashMap<>();
        HashMap<Integer, ArrayList<SportData>> map = getAllOffsetDataListWithSameDate(sports);
        if (map == null) {
            ZLogger.e(D, "map empty.");
            return null;
        }

        // iterator all the offset-SportData key-value
        Iterator iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Integer key = (Integer) entry.getKey();
            ArrayList<SportData> val = (ArrayList<SportData>) entry.getValue();
            SportData offsetVal = findLastDataInOffsetByDate(val);
            offsetDataMap.put(key, offsetVal);
        }

        if (offsetDataMap == null) {
            ZLogger.e(D, "offsetDataMap empty.");
            return null;
        }

        return offsetDataMap;
    }

    /**
     * Get all the offset-SportDataList key-value, The input data must be the same date,
     * if not, it will return error result.
     *
     * @param sports the input data
     * @return All the offset-SportDataList key-value.
     */
    public static HashMap<Integer, ArrayList<SportData>> getAllOffsetDataListWithSameDate(List<SportData> sports) {
        ArrayList<Integer> listOffset = new ArrayList<Integer>();
        HashMap<Integer, ArrayList<SportData>> offsetDataMap = new HashMap<Integer, ArrayList<SportData>>();
        for (SportData sp : sports) {
            ArrayList<SportData> sds;
            if (offsetDataMap.get(sp.getOffset()) == null) {
                sds = new ArrayList<SportData>();
            } else {
                sds = offsetDataMap.get(sp.getOffset());
            }

            sds.add(sp);
            offsetDataMap.put(sp.getOffset(), sds);
        }
        if (offsetDataMap.size() == 0) {
            ZLogger.e(D, "map empty.");
            return null;
        }
        return offsetDataMap;
    }


    /**
     * Get the sub SportData list by the special date, if didn't find, it will return
     * null.
     *
     * @param sports the input data
     * @return Sub data list of the special date.
     */
    public static List<SportData> getSubSportDataByDate(
            int y, int m, int d,
            List<SportData> sports) {
        ArrayList<SportData> sps = new ArrayList<>();
        for (SportData sp : sports) {
            if (sp.getYear() == y
                    && sp.getMonth() == m
                    && sp.getDay() == d) {
                sps.add(sp);
            }
        }
        if (sps.size() == 0) {
            ZLogger.e(D, "didn't find the data in list by date.");
            return null;
        }
        return sps;
    }

    /**
     * Find the last valid data in the offset, the input data must be with the
     * same date(year, month, day) and same offset. If input with the various
     * date, the return result may be not exact. If input with the various offset,
     * the it will return null.
     *
     * @param sports the input data
     * @return The last valid data in the offset.
     */
    public static SportData findLastDataInOffsetByDate(List<SportData> sports) {
        SportData sp;
        // Check the input data
        if (sports == null
                || sports.size() == 0) {
            ZLogger.d(D, "The input sport data error.");
            return null;
        }
        // Check offset
        if (findValidOffset(sports) == -1) {
            ZLogger.d(D, "The input sport data error with error offset.");
            return null;
        }
        sp = findLastDataInOffsetByDateWithoutCheck(sports);

        return sp;
    }

    /**
     * Find the last valid data in the offset, do not do data check, you must
     * make sure the input data list with same offset.
     *
     * @param sports the input data
     * @return The last valid data in the offset.
     */
    public static SportData findLastDataInOffsetByDateWithoutCheck(List<SportData> sports) {
        Date maxDate = sports.get(0).getDate();
        SportData maxSportData = sports.get(0);
        for (SportData sp : sports) {
            if (sp.getDate().compareTo(maxDate) > 0) {
                maxDate = sp.getDate();
                maxSportData = sp;
            }
        }

        ZLogger.d(D, "The last data, " + toString(maxSportData));
        return maxSportData;
    }
    /*
    public static String toString(SportData sp) {
		return "year: " + sp.getYear()
				+ ", month: " + sp.getMonth()
				+ ", day: " + sp.getDay()
				+ ", offset: " + sp.getOffset()
				+ ", sport mode: " + sp.getMode()
				+ ", stepCount: " + sp.getStepCount()
				+ ", activeTime: " + sp.getActiveTime()
				+ ", calory: " + sp.getCalory()
				+ ", distance: " + sp.getDistance()
				+ ", date: " + sp.getDate();
	}*/

    public static String toString(SportData sp) {
        int startHour = ((sp.getOffset()) / 4);// offset start from 0
        int startMinute = ((sp.getOffset()) % 4) * 15;
        String startHourStr = String.valueOf(startHour).length() == 1
                ? "0" + String.valueOf(startHour)
                : String.valueOf(startHour);
        String startMinuteStr = String.valueOf(startMinute).length() == 1
                ? "0" + String.valueOf(startMinute)
                : String.valueOf(startMinute);
        SimpleDateFormat shortDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return "Year: " + sp.getYear()
                + ", Month: " + sp.getMonth()
                + ", Day: " + sp.getDay()
                + ", Offset: " + String.valueOf(startHourStr + ":" + startMinuteStr + "(" + sp.getOffset() + ")")
                + ", Step: " + sp.getStepCount()
                + ", Calory: " + sp.getCalory()
                + ", Distance: " + sp.getDistance()
                + ", Sport Mode: " + sp.getMode()
                + ", ActiveTime: " + sp.getActiveTime()
                + ", Date: " + shortDateFormat.format(sp.getDate());
    }

    public static String toString(SleepData data) {
        SimpleDateFormat shortDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String modeString = "";
        switch (data.getMode()) {
            case ApplicationLayer.SLEEP_MODE_START_SLEEP:
                modeString = "Start Sleep(0x01)";
                break;
            case ApplicationLayer.SLEEP_MODE_START_DEEP_SLEEP:
                modeString = "Start Deep Sleep(0x02)";
                break;
            case ApplicationLayer.SLEEP_MODE_START_WAKE:
                modeString = "Start Wake(0x03)";
                break;
            default:
                modeString = "Error mode(" + String.valueOf(data.getMode()) + ")";
                break;
        }
        int startHour = (int) data.getMinutes() / 60;
        int startMinute = (int) data.getMinutes() % 60;
        String startHourStr = String.valueOf(startHour).length() == 1
                ? "0" + String.valueOf(startHour)
                : String.valueOf(startHour);
        String startMinuteStr = String.valueOf(startMinute).length() == 1
                ? "0" + String.valueOf(startMinute)
                : String.valueOf(startMinute);
//        return "Date: " + data.getYear()
//                + "/" + data.getMonth()
//                + "/" + data.getDay()
//                + " " + String.valueOf(startHourStr + ":" + startMinuteStr + "(" + data.getMinutes() + ")")
//                + ", Sleep mode: " + modeString
//                + ", Date: " + shortDateFormat.format(data.getDate());
        return String.format("%d/%d/%d %02d:%02d(%d %.2f), %s, %s",
                data.getYear(), data.getMonth(), data.getDay(), startHour % 24, startMinute,
                data.getMinutes(), data.getMinutes() / 60f, modeString, shortDateFormat.format(data.getDate()));
    }

//    public static String toString(LoginDebugData data) {
//        SimpleDateFormat shortDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        return "PhoneAndroidVersion: " + data.getPhoneAndroidVersion()
//                + ", PhoneType: " + data.getPhoneType()
//                + ", ApkVersion: " + data.getApkVersion()
//                + ", ApkBuildType: " + data.getApkBuildType()
//                + ", TargetAppVersion: " + data.getTargetAppVersion()
//                + ", TargetPatchVersion: " + data.getTargetPatchVersion()
//                + ", Date: " + shortDateFormat.format(data.getDate());
//    }

    public static String toString(HrpData data) {
        SimpleDateFormat shortDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return "Value: " + data.getValue()
                + ", Date: " + shortDateFormat.format(data.getDate());
    }

    public static String toString(List<SportData> sps) {
        String str = new String();
        for (SportData sp : sps) {
            str += toString(sp);
        }
        return str;
    }

    /**
     * Find the valid offset in the list, if the input list has various offset,
     * it will return -1.
     *
     * @param sports the input data
     * @return The valid offset.
     */
    public static int findValidOffset(List<SportData> sports) {
        // Check the input data
        if (sports == null
                || sports.size() == 0) {
            ZLogger.e(D, "The input sport data error.");
            return -1;
        }
        int offset = sports.get(0).getOffset();
        for (SportData sp : sports) {
            if (offset != sp.getOffset()) {
                ZLogger.e(D, "The input sport data error, have too many offset.");
                return -1;
            }
        }
        ZLogger.d(D, "offset: " + offset);
        return offset;
    }

    /**
     * Get nearly offset step data, use to sync to remote
     *
     * @param sports the input data, you must make sure the input data sort by
     *               time, order by ascend.
     * @return the total sport data of the date.
     */
    public static SportData getNearlyOffsetStepData(List<SportData> sports) {
        Calendar c1 = Calendar.getInstance();
        int Year = c1.get(Calendar.YEAR);
        int Month = c1.get(Calendar.MONTH) + 1;
        int Day = c1.get(Calendar.DATE);
        int Hour = c1.get(Calendar.HOUR_OF_DAY);
        int Minutes = c1.get(Calendar.MINUTE);
        int Offset = (Hour * 60 + Minutes) / 15;
        // we should get the last one, in current time.
        for (int i = sports.size() - 1; i >= 0; i--) {
            SportData sportData = sports.get(i);
            if (sportData.getYear() == Year
                    && sportData.getMonth() == Month
                    && sportData.getDay() == Day
                    && sportData.getOffset() == Offset) {
                ZLogger.d(D, toString(sportData));
                return sportData;
            }
        }
        return null;
    }

    public static void adjustTodayTotalStepDataNew(Context context, ApplicationLayerTodaySumSportPacket data) {

//        Calendar c1 = Calendar.getInstance();
//        List<SportData> sports = GlobalGreenDAO.getInstance().loadSportDataByDate(c1.get(Calendar.YEAR),
//                c1.get(Calendar.MONTH) + 1,// here need add 1, because it origin range is 0 - 11;
//                c1.get(Calendar.DATE));
//
//        SportSubData subData = sumOfSportDataByDate(c1.get(Calendar.YEAR),
//                c1.get(Calendar.MONTH) + 1,// here need add 1, because it origin range is 0 - 11;
//                c1.get(Calendar.DATE),
//                sports);
//        if (subData == null) {
//            ZLogger.e(D, "with no subData");
//            return;
//        }
//
//        int offset = data.getOffset();
//        long diffStep = data.getTotalStep() - subData.getStepCount();
//        long diffCalory = data.getTotalCalory() - subData.getCalory();
//        long diffDistance = data.getTotalDistance() - subData.getDistance();
//        ZLogger.d(D, "offset: " + offset
//                + ", data.getTotalStep(): " + data.getTotalStep()
//                + ", data.getTotalCalory(): " + data.getTotalCalory()
//                + ", data.getTotalDistance(): " + data.getTotalDistance()
//                + ", diffStep: " + diffStep
//                + ", diffCalory: " + diffCalory
//                + ", diffDistance: " + diffDistance);
//        SPWristbandConfigInfo.setAdjustTargetStep(context, diffStep);
//        SPWristbandConfigInfo.setAdjustTargetDistance(context, diffDistance);
//        SPWristbandConfigInfo.setAdjustTargetCal(context, diffCalory);
    }

    public static void adjustTodayTotalStepData(ApplicationLayerTodaySumSportPacket data) {
//        Calendar c1 = Calendar.getInstance();
//        List<SportData> sports = GlobalGreenDAO.getInstance().loadSportDataByDate(c1.get(Calendar.YEAR),
//                c1.get(Calendar.MONTH) + 1,// here need add 1, because it origin range is 0 - 11;
//                c1.get(Calendar.DATE));
//
//        SportSubData subData = sumOfSportDataByDate(c1.get(Calendar.YEAR),
//                c1.get(Calendar.MONTH) + 1,// here need add 1, because it origin range is 0 - 11;
//                c1.get(Calendar.DATE),
//                sports);
//        if (subData == null) {
//            ZLogger.e(D, "with no subData");
//            return;
//        }
//        int offset = data.getOffset();
//        long diffStep = data.getTotalStep() - subData.getStepCount();
//        long diffCalory = data.getTotalCalory() - subData.getCalory();
//        long diffDistance = data.getTotalDistance() - subData.getDistance();
//        ZLogger.d(D, "offset: " + offset
//                + ", diffStep: " + diffStep
//                + ", diffCalory: " + diffCalory
//                + ", diffDistance: " + diffDistance);
//        // get every offset data, and sort by offset
//        HashMap<Integer, SportData> offsetDataMap = getAllOffsetDataWithSameDate(sports);
//
//        if (offsetDataMap == null) {
//            ZLogger.e(D, "with no offsetDataMap");
//            return;
//        }
//        // get the sort key
//        Object[] keyArray = offsetDataMap.keySet().toArray();
//        Arrays.sort(keyArray);
//
//        for (int i = keyArray.length - 1; i >= 0; i--) {
//            int offsetValue = (Integer) keyArray[i];
//            SportData sportData = offsetDataMap.get(keyArray[i]);
//            ZLogger.d(D, "Key: " + offsetValue + ", data: " + toString(sportData));
//            ZLogger.d(D, "diffStep: " + diffStep
//                    + ", diffCalory: " + diffCalory
//                    + ", diffDistance: " + diffDistance);
//            if (offsetValue < offset) {
//                if (diffStep == 0
//                        && diffCalory == 0
//                        && diffDistance == 0) {
//                    ZLogger.i(D, "adjustTodayTotalStepData OK!");
//                    break;
//                }
//
//                long tempStep;
//                long tempCalory;
//                long tempDistance;
//                // Update step diff
//                if (diffStep >= 0) {
//                    tempStep = sportData.getStepCount() + diffStep;
//                } else {
//                    if (sportData.getStepCount() + diffStep >= 0) {
//                        tempStep = sportData.getStepCount() + diffStep;
//                    } else {
//                        tempStep = 0L;
//                    }
//                }
//                // update diff value
//                diffStep = (sportData.getStepCount() + diffStep) - tempStep;
//
//
//                // Update Calory diff
//                if (diffCalory >= 0) {
//                    tempCalory = sportData.getCalory() + diffCalory;
//                } else {
//                    if (sportData.getCalory() + diffCalory >= 0) {
//                        tempCalory = sportData.getCalory() + diffCalory;
//                    } else {
//                        tempCalory = 0L;
//                    }
//                }
//                // update diff value
//                diffCalory = (sportData.getCalory() + diffCalory) - tempCalory;
//
//
//                // Update Distance diff
//                if (diffDistance >= 0) {
//                    tempDistance = sportData.getDistance() + diffDistance;
//                } else {
//                    if (sportData.getDistance() + diffDistance >= 0) {
//                        tempDistance = sportData.getDistance() + diffDistance;
//                    } else {
//                        tempDistance = 0L;
//                    }
//                }
//                // update diff value
//                diffDistance = (sportData.getDistance() + diffDistance) - tempDistance;
//
//
//                // save the data
//                SportData temp = new SportData(null
//                        , sportData.getYear(), sportData.getMonth(), sportData.getDay()
//                        , sportData.getOffset()
//                        , -1// mode
//                        , (int) tempStep
//                        , -1// active time
//                        , (int) tempCalory, (int) tempDistance
//                        , new Date());
//                ZLogger.d(D, "Adjust data: " + toString(temp));
////                GlobalGreenDAO.getInstance().saveSportData(temp);
//            }
//        }
        
/*
        // iterator all the offset-SportData key-value
		Iterator iter = offsetDataMap.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			//Object key = entry.getKey();
			SportData val = (SportData)entry.getValue();
			subData.setStepCount(subData.getStepCount() + val.getStepCount());
			subData.setCalory(subData.getCalory() + val.getCalory());
			subData.setDistance(subData.getDistance() + val.getDistance());
		}
		*/
    }

    /**
     * Get sum of the sleep data of the special date. No Error Check(
     * This method is use to calculate the 18:00 PM - 10:00 AM
     *
     * @param y      the special year
     * @param m      the special month
     * @param d      the special day
     * @param sleeps the input data
     * @return the total sport data of the date.
     */
    public static SleepSubData sumOfSleepDataByDateSpecNoErrorCheck(
            int y, int m, int d,
            List<SleepData> sleeps) {
        return sumOfSleepDataByMinutesSpecNoErrorCheck(SleepFilter.filter(y, m, d, sleeps));
    }

    /**
     * Get sum of the sleep data. No Error check
     * This method is use to calculate the 18:00 PM - 10:00 AM
     *
     * @param sleeps the input data
     * @return the total sport data of the date.
     */
    public static SleepSubData sumOfSleepDataByMinutesSpecNoErrorCheck(
            List<SleepFilterData> sleeps) {
        if (sleeps == null || sleeps.size() <= 0) {
            return null;
        }

        SleepSubData subData = new SleepSubData();

        SleepFilterData lastSleepData = null;
        for (int i = 0; i < sleeps.size(); i++) {
            SleepFilterData sl = sleeps.get(i);
            ZLogger.d(D, "sort data. "
                    + sl.toString());
            if (lastSleepData == null) {// get the last mode.
                lastSleepData = sl;
            } else {
                boolean needUpdateLast = true;
                switch (lastSleepData.getMode()) {
                    case ApplicationLayer.SLEEP_MODE_START_WAKE:
                        switch (sl.getMode()) {
                            case ApplicationLayer.SLEEP_MODE_START_SLEEP:
                                //
                                break;

                            default:
                                ZLogger.e(D, "The input data may be is error"
                                        + ", lastSleepData.getMode(): " + lastSleepData.getMode()
                                        + ", sl.getMode(): " + sl.getMode());
                                needUpdateLast = false;
                                break;
                        }
                        break;

                    case ApplicationLayer.SLEEP_MODE_START_SLEEP:
                        switch (sl.getMode()) {
                            case ApplicationLayer.SLEEP_MODE_START_DEEP_SLEEP:
                                subData.setLightSleepTime(subData.getLightSleepTime()
                                        + sl.getMinutes() - lastSleepData.getMinutes());
                                break;
                            case ApplicationLayer.SLEEP_MODE_START_WAKE:
                                if (i != sleeps.size() - 1) {
                                    subData.setAwakeTimes(subData.getAwakeTimes() + 1);
                                }
                                subData.setLightSleepTime(subData.getLightSleepTime()
                                        + sl.getMinutes() - lastSleepData.getMinutes());
                                break;
                            default:
                                ZLogger.e(D, "The input data may be is error"
                                        + ", lastSleepData.getMode(): " + lastSleepData.getMode()
                                        + ", sl.getMode(): " + sl.getMode());
                                needUpdateLast = false;
                                break;
                        }
                        break;
                    case ApplicationLayer.SLEEP_MODE_START_DEEP_SLEEP:
                        switch (sl.getMode()) {
                            case ApplicationLayer.SLEEP_MODE_START_SLEEP:
                                subData.setDeepSleepTime(subData.getDeepSleepTime()
                                        + sl.getMinutes() - lastSleepData.getMinutes());
                                break;
                            case ApplicationLayer.SLEEP_MODE_START_WAKE:
                                if (i != sleeps.size() - 1) {
                                    subData.setAwakeTimes(subData.getAwakeTimes() + 1);
                                }
                                subData.setDeepSleepTime(subData.getDeepSleepTime()
                                        + sl.getMinutes() - lastSleepData.getMinutes());
                                break;
                            default:
                                ZLogger.e(D, "The input data may be is error"
                                        + ", lastSleepData.getMode(): " + lastSleepData.getMode()
                                        + ", sl.getMode(): " + sl.getMode());
                                needUpdateLast = false;
                                break;
                        }
                        break;
                }

                ZLogger.i(D, "lastSleepData.getMode(): " + lastSleepData.getMode()
                        + ", sl.getMode(): " + sl.getMode()
                        + ", needUpdateLast: " + needUpdateLast
                        + ", sub sleep data: " + subData.toString());
                if (needUpdateLast) {
                    // update last sleep data
                    lastSleepData = sl;
                }
            }
        }

        ZLogger.i(D, "sub sleep data: " + subData.toString());
        return subData;
    }

    public static SleepSubData sumOfSleepDataByDateSpecNoErrorCheckWithErrorFilter(
            int y, int m, int d,
            List<SleepData> sleeps) {

        return sumOfSleepDataByMinutesSpecNoErrorCheckWithErrorFilter(SleepFilter.filter(y, m, d, sleeps));
    }

    public static SleepSubData sumOfSleepDataByMinutesSpecNoErrorCheckWithErrorFilter(
            List<SleepFilterData> sleeps) {
        if (sleeps == null || sleeps.size() <= 0) {
            return null;
        }

        SleepSubData subData = new SleepSubData();

        ArrayList<SleepFilterData> filter = SleepFilter.filterAwake(sleeps);

        SleepFilterData lastSleepData = null;
        boolean dropBeh = false;
        for (int i = 0; i < filter.size(); i++) {
            if (dropBeh) {
                ZLogger.w("睡眠数据过滤完毕，清除后面到数据");
                if (lastSleepData != null) {
                    ZLogger.e(D, "dropBeh, lastMode: " + lastSleepData.toString());
                }
                break;
            }

            SleepFilterData sl = filter.get(i);
            if (sl == null) {
                continue;
            }
            ZLogger.d(D, sl.toString());

            if (lastSleepData == null) {
                // get the last mode.
                lastSleepData = sl;
                continue;
            }
            ZLogger.d(D, "lastMode: " + lastSleepData.toString());

            boolean needUpdateLast = true;
            switch (lastSleepData.getMode()) {
                case ApplicationLayer.SLEEP_MODE_START_WAKE: {
                    switch (sl.getMode()) {
                        case ApplicationLayer.SLEEP_MODE_START_DEEP_SLEEP:
                        case ApplicationLayer.SLEEP_MODE_START_SLEEP: {
                            ZLogger.d("清醒中断");
                            // Check the wake time
                            int checkResult = SleepFilter.checkAwakeData(lastSleepData.getMinutes(), sl.getMinutes());
                            ZLogger.i(D, "checkResult: " + checkResult);
                            if (checkResult == SleepFilter.CHECK_DROP_ALL) {
                                return null;
                            } else if (checkResult == SleepFilter.CHECK_DROP_FRONT) {
                                // Clear pre-info
                                subData = new SleepSubData();
                            } else if (checkResult == SleepFilter.CHECK_DROP_BEHIND) {
                                dropBeh = true;
                            }
                            break;
                        }
                        default:
                            ZLogger.w(D, "The input data may be error"
                                    + ", lastSleepData.getMode(): " + lastSleepData.getMode()
                                    + ", sl.getMode(): " + sl.getMode());
                            needUpdateLast = false;
                            break;
                    }
                    break;
                }
                case ApplicationLayer.SLEEP_MODE_START_SLEEP: {
                    ZLogger.d("浅睡中断");
                    switch (sl.getMode()) {
                        case ApplicationLayer.SLEEP_MODE_START_DEEP_SLEEP:
                            subData.setLightSleepTime(subData.getLightSleepTime()
                                    + sl.getMinutes() - lastSleepData.getMinutes());
                            break;
                        case ApplicationLayer.SLEEP_MODE_START_WAKE:
                            if (i != filter.size() - 1) {
                                subData.setAwakeTimes(subData.getAwakeTimes() + 1);
                            }
                            subData.setLightSleepTime(subData.getLightSleepTime()
                                    + sl.getMinutes() - lastSleepData.getMinutes());
                            break;
                        default:
                            ZLogger.w(D, "The input data may be error"
                                    + ", lastSleepData.getMode(): " + lastSleepData.getMode()
                                    + ", sl.getMode(): " + sl.getMode());
                            needUpdateLast = false;
                            break;
                    }
                    break;
                }
                case ApplicationLayer.SLEEP_MODE_START_DEEP_SLEEP: {
                    //深睡中断
                    if (sl.getMode() != ApplicationLayer.SLEEP_MODE_START_DEEP_SLEEP) {
                        // Check the deep sleep time
                        int checkResultDeep = SleepFilter.checkDeepSleep(lastSleepData.getMinutes(), sl.getMinutes());
                        ZLogger.i(D, "checkResultDeep: " + checkResultDeep);
                        if (checkResultDeep == SleepFilter.CHECK_DROP_ALL) {
                            return null;
                        } else if (checkResultDeep == SleepFilter.CHECK_DROP_FRONT) {
                            // Clear pre-info
                            subData = new SleepSubData();
                            break;
                        } else if (checkResultDeep == SleepFilter.CHECK_DROP_BEHIND) {
                            dropBeh = true;
                            break;
                        }
                    }
                    switch (sl.getMode()) {
                        case ApplicationLayer.SLEEP_MODE_START_SLEEP:
                            subData.setDeepSleepTime(subData.getDeepSleepTime()
                                    + sl.getMinutes() - lastSleepData.getMinutes());
                            break;
                        case ApplicationLayer.SLEEP_MODE_START_WAKE:
                            if (i != filter.size() - 1) {
                                subData.setAwakeTimes(subData.getAwakeTimes() + 1);
                            }
                            subData.setDeepSleepTime(subData.getDeepSleepTime()
                                    + sl.getMinutes() - lastSleepData.getMinutes());
                            break;
                        default:
                            ZLogger.e(D, "The input data may be is error"
                                    + ", lastSleepData.getMode(): " + lastSleepData.getMode()
                                    + ", sl.getMode(): " + sl.getMode());
                            needUpdateLast = false;
                            break;
                    }
                    break;
                }
            }
//                ZLogger.i(D, "lastSleepData.getMode(): " + lastSleepData.getMode()
//                        + ", sl.getMode(): " + sl.getMode()
//                        + ", needUpdateLast: " + needUpdateLast
//                        + ", sub sleep data: " + subData.toString());
            if (needUpdateLast) {
                // update last sleep data
                lastSleepData = sl;
            }
            ZLogger.d("subData: " + subData.toString());
        }

        ZLogger.i(D, "sub sleep data: " + subData.toString());
        return subData;
    }

    /**
     * Get sum of the sleep data.
     * This method is use to calculate the 18:00 PM - 10:00 AM
     *
     * @param sleeps the input data
     * @return the total sport data of the date.
     */
    public static SleepSubData sumOfSleepDataByMinutesSpec(
            List<SleepFilterData> sleeps) {
        SleepSubData subData = new SleepSubData();

        SleepFilterData lastSleepData = null;
        for (SleepFilterData sl : sleeps) {
            ZLogger.d(D, "sort data. "
                    + sl.toString());
            if (lastSleepData == null) {// get the last mode.
                lastSleepData = sl;
            } else {
                switch (lastSleepData.getMode()) {
                    case ApplicationLayer.SLEEP_MODE_START_WAKE:
                        switch (sl.getMode()) {
                            case ApplicationLayer.SLEEP_MODE_START_SLEEP:
                                //
                                break;
                            default:
                                ZLogger.e(D, "The input data may be is error"
                                        + ", lastSleepData.getMode(): " + lastSleepData.getMode()
                                        + ", sl.getMode(): " + sl.getMode());
                                return null;
                        }
                        break;

                    case ApplicationLayer.SLEEP_MODE_START_SLEEP:
                        switch (sl.getMode()) {
                            case ApplicationLayer.SLEEP_MODE_START_DEEP_SLEEP:
                                subData.setLightSleepTime(subData.getLightSleepTime()
                                        + sl.getMinutes() - lastSleepData.getMinutes());
                                break;
                            case ApplicationLayer.SLEEP_MODE_START_WAKE:
                                subData.setAwakeTimes(subData.getAwakeTimes() + 1);
                                subData.setLightSleepTime(subData.getLightSleepTime()
                                        + sl.getMinutes() - lastSleepData.getMinutes());
                                break;
                            default:
                                ZLogger.e(D, "The input data may be is error"
                                        + ", lastSleepData.getMode(): " + lastSleepData.getMode()
                                        + ", sl.getMode(): " + sl.getMode());
                                return null;
                        }
                        break;
                    case ApplicationLayer.SLEEP_MODE_START_DEEP_SLEEP:
                        switch (sl.getMode()) {
                            case ApplicationLayer.SLEEP_MODE_START_SLEEP:
                                subData.setDeepSleepTime(subData.getDeepSleepTime()
                                        + sl.getMinutes() - lastSleepData.getMinutes());
                                break;
                            case ApplicationLayer.SLEEP_MODE_START_WAKE:
                                subData.setAwakeTimes(subData.getAwakeTimes() + 1);
                                subData.setDeepSleepTime(subData.getDeepSleepTime()
                                        + sl.getMinutes() - lastSleepData.getMinutes());
                                break;
                            default:
                                ZLogger.e(D, "The input data may be is error"
                                        + ", lastSleepData.getMode(): " + lastSleepData.getMode()
                                        + ", sl.getMode(): " + sl.getMode());
                                return null;
                        }
                        break;
                }

                ZLogger.i(D, "lastSleepData.getMode(): " + lastSleepData.getMode()
                        + ", sl.getMode(): " + sl.getMode()
                        + ", sub sleep data: " + subData.toString());

                // update last sleep data
                lastSleepData = sl;
            }
        }

        ZLogger.i(D, "sub sleep data: " + subData.toString());
        return subData;
    }

    /**
     * Get sum of the sleep data.
     *
     * @param sleeps the input data
     * @return the total sport data of the date.
     */
    public static SleepSubData sumOfSleepDataByMinutes(
            List<SleepData> sleeps) {
        SleepSubData subData = new SleepSubData();

        ArrayList<SleepData> sls = new ArrayList<>();
        for (SleepData sl : sleeps) {
            sls.add(sl);
        }
        List<SleepData> slss;
        // increase sort the sleep data by minutes
        Collections.sort(sls, new SleepFilter.IncreaseComparator());

        for (SleepData sl : sls) {
            ZLogger.d(D, "sort data. "
                    + toString(sl));
        }
        SleepData lastSleepData = null;
        for (SleepData sl : sls) {
            if (lastSleepData == null) {// get the last mode.
                lastSleepData = sl;
            } else {
                switch (lastSleepData.getMode()) {
                    case SLEEP_MODE_START_DEEP_SLEEP:
                        switch (sl.getMode()) {
                            case SLEEP_MODE_START_LIGHT_SLEEP_MODE_2:
                                subData.setDeepSleepTime(subData.getDeepSleepTime()
                                        + sl.getMinutes() - lastSleepData.getMinutes());
                                break;
                            default:
                                ZLogger.e(D, "The input data may be is error"
                                        + ", lastSleepData.getMode(): " + lastSleepData.getMode()
                                        + ", sl.getMode(): " + sl.getMode());
                                return null;
                        }
                        break;

                    case SLEEP_MODE_START_LIGHT_SLEEP_MODE_1:
                        switch (sl.getMode()) {
                            case SLEEP_MODE_START_DEEP_SLEEP:
                                subData.setLightSleepTime(subData.getLightSleepTime()
                                        + sl.getMinutes() - lastSleepData.getMinutes());
                                break;
                            case SLEEP_MODE_EXIT_SLEEP:
                                subData.setAwakeTimes(subData.getAwakeTimes() + 1);
                                subData.setLightSleepTime(subData.getLightSleepTime()
                                        + sl.getMinutes() - lastSleepData.getMinutes());
                                break;
                            default:
                                ZLogger.e(D, "The input data may be is error"
                                        + ", lastSleepData.getMode(): " + lastSleepData.getMode()
                                        + ", sl.getMode(): " + sl.getMode());
                                return null;
                        }
                        break;
                    case SLEEP_MODE_START_LIGHT_SLEEP_MODE_2:
                        switch (sl.getMode()) {
                            case SLEEP_MODE_START_LIGHT_SLEEP_MODE_1:
                                subData.setLightSleepTime(subData.getLightSleepTime()
                                        + sl.getMinutes() - lastSleepData.getMinutes());
                                break;
                            case SLEEP_MODE_EXIT_SLEEP:
                                subData.setAwakeTimes(subData.getAwakeTimes() + 1);
                                subData.setLightSleepTime(subData.getLightSleepTime()
                                        + sl.getMinutes() - lastSleepData.getMinutes());
                                break;
                            default:
                                ZLogger.e(D, "The input data may be is error"
                                        + ", lastSleepData.getMode(): " + lastSleepData.getMode()
                                        + ", sl.getMode(): " + sl.getMode());
                                return null;
                        }
                        break;
                    case SLEEP_MODE_START_ENTER_SLEEP:
                        switch (sl.getMode()) {
                            case SLEEP_MODE_START_LIGHT_SLEEP_MODE_1:
                                // do nothing
                                break;
                            default:
                                ZLogger.e(D, "The input data may be is error"
                                        + ", lastSleepData.getMode(): " + lastSleepData.getMode()
                                        + ", sl.getMode(): " + sl.getMode());
                                return null;
                        }
                        break;
                    case SLEEP_MODE_EXIT_SLEEP:
                        switch (sl.getMode()) {
                            case SLEEP_MODE_START_ENTER_SLEEP:
                                // do nothing
                                break;
                            default:
                                ZLogger.e(D, "The input data may be is error"
                                        + ", lastSleepData.getMode(): " + lastSleepData.getMode()
                                        + ", sl.getMode(): " + sl.getMode());
                                return null;
                        }
                        break;
                }

                ZLogger.i(D, "lastSleepData.getMode(): " + lastSleepData.getMode()
                        + ", sl.getMode(): " + sl.getMode()
                        + ", sub sleep data: " + subData.toString());

                // update last sleep data
                lastSleepData = sl;
            }
        }

        ZLogger.i(D, "sub sleep data: " + subData.toString());
        return subData;
    }

    /**
     * Get sum of the sleep data of the special date.
     * This method is use to calculate the 18:00 PM - 10:00 AM
     *
     * @param y      the special year
     * @param m      the special month
     * @param d      the special day
     * @param sleeps the input data
     * @return the total sport data of the date.
     */
    public static SleepSubData sumOfSleepDataByDateSpec(
            int y, int m, int d,
            List<SleepData> sleeps) {

        ArrayList<SleepFilterData> sls = SleepFilter.filter(y, m, d, sleeps);
        if (sls != null && sls.size() > 0) {
            sumOfSleepDataByMinutesSpec(sls);
        }

        return null;
    }

    /**
     * Get sum of the sleep data of the special date.
     *
     * @param y      the special year
     * @param m      the special month
     * @param d      the special day
     * @param sleeps the input data
     * @return the total sport data of the date.
     */
    public static SleepSubData sumOfSleepDataByDate(
            int y, int m, int d,
            List<SleepData> sleeps) {
        // get the special date sleep data
        List<SleepData> sls = SleepFilter.getSubSleepDataByDate(y, m, d, sleeps);
        if (sls == null) {
            ZLogger.e(D, "didn't find the data in list by date.");
            return null;
        }

        return sumOfSleepDataByMinutes(sls);
    }

    private static long getDiffTimeWithMinute(Date d1, Date d2) {
        long nd = 1000 * 24 * 60 * 60;// The number of milliseconds in a day
        long nh = 1000 * 60 * 60;// The number of milliseconds in a hour
        long nm = 1000 * 60;// The number of milliseconds in a minute
        long ns = 1000;// The number of milliseconds in a second

        long diff = d1.getTime() - d2.getTime();
        long day = diff / nd;// Calculate how many days in diff
        long hour = diff % nd / nh + day * 24;// Calculate how many hours in diff
        long min = diff % nd % nh / nm + day * 24 * 60;// Calculate how many minutes in diff
        //long sec = diff % nd % nh % nm / ns;// Calculate how many seconds in diff
        /*
        if(D) Log.d(TAG, "d1.getTime(): " + d1.getTime()
				+ ", d2.getTime(): " + d2.getTime()
				+ ", diff: " + diff
				+ ", min: " + min);
				*/
        return min;
    }

    private long getDiffTimeWithHour(Date d1, Date d2) {
        long nd = 1000 * 24 * 60 * 60;// The number of milliseconds in a day
        long nh = 1000 * 60 * 60;// The number of milliseconds in a hour
        long nm = 1000 * 60;// The number of milliseconds in a minute
        long ns = 1000;// The number of milliseconds in a second

        long diff = d1.getTime() - d2.getTime();
        long day = diff / nd;// Calculate how many days in diff
        long hour = diff % nd / nh + day * 24;// Calculate how many hours in diff
        //long min = diff % nd % nh / nm + day * 24 * 60;// Calculate how many minutes in diff
        //long sec = diff % nd % nh % nm / ns;// Calculate how many seconds in diff

        return hour;
    }

    public static final int SLEEP_MODE_START_DEEP_SLEEP = 1;
    public static final int SLEEP_MODE_START_LIGHT_SLEEP_MODE_1 = 2;
    public static final int SLEEP_MODE_START_LIGHT_SLEEP_MODE_2 = 3;
    public static final int SLEEP_MODE_START_ENTER_SLEEP = 4;
    public static final int SLEEP_MODE_EXIT_SLEEP = 5;

	
	/*
    public static String toString(SleepData sl) {
		return "year: " + sl.getYear()
				+ ", month: " + sl.getMonth()
				+ ", day: " + sl.getDay()
				+ ", minutes: " + sl.getMinutes()
				+ ", sleep mode: " + sl.getMode()
				+ ", date: " + sl.getDate();
	}*/

    /**
     * Sleep data list Decrease Comparator class, sort by the minutes.
     */
    public static class SleepDecreaseComparator implements Comparator {

        public int compare(Object arg0, Object arg1) {
            // TODO Auto-generated method stub

            return compareSleep((SleepData) arg0, (SleepData) arg1);
        }

        public int compareSleep(SleepData o1, SleepData o2) {
            if (o1.getMinutes() > o2.getMinutes()) {
                return -1;
            } else if (o1.getMinutes() < o2.getMinutes()) {
                return 1;
            } else {
                return 0;
            }
        }
    }


    /**
     * Get the max days in the special month.
     *
     * @param year  the special year
     * @param month the special month
     * @return The max days in the special month.
     */
    public static int getMonthMaxDays(int year, int month) {
        int maxDays;
        Calendar rightNow = Calendar.getInstance();

        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy/MM");

        try {
            rightNow.setTime(simpleDate.parse(year + "/" + month));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        maxDays = rightNow.getActualMaximum(Calendar.DAY_OF_MONTH);
        ZLogger.d(D, "year: " + year
                + ", month: " + month
                + ", maxDays: " + maxDays);

        return maxDays;
    }

    /**
     * Get the week of the special day.
     *
     * @param year  the special year
     * @param month the special month
     * @param day   the special day
     * @return The week of the special day.
     */
    public static int getWeekOfDay(int year, int month, int day) {
        int week;
        Calendar rightNow = Calendar.getInstance();

        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy/MM/dd");

        try {
            rightNow.setTime(simpleDate.parse(year + "/" + month + "/" + day));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        week = rightNow.get(Calendar.DAY_OF_WEEK);
        ZLogger.d(D, "year: " + year
                + ", month: " + month
                + ", day: " + day
                + ", week: " + week);

        return week;
    }

}

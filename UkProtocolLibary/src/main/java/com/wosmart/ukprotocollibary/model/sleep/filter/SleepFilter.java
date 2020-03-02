package com.wosmart.ukprotocollibary.model.sleep.filter;

import com.realsil.realteksdk.logger.ZLogger;
import com.wosmart.ukprotocollibary.applicationlayer.ApplicationLayer;
import com.wosmart.ukprotocollibary.model.sleep.SleepData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 睡眠数据过滤机制
 * <ol>
 * Updated on 22/08/2017.
 * <li>在01:00点前出现清醒时间，且清醒时间（整体在01：00之前）大于等于20min，则删除之前的睡眠数据。(考虑到早睡情况，这条待确认，考虑目前的测试群体)</li>
 * <li>早上05:00点之后（整体在05：00之后），出现清醒时间超过30min，则之后的数据全部清除 </li>
 * <li>01:00之前最晚的一次清醒，如果之前只有清醒和浅睡，则删除该清醒状态以前的睡眠数据</li>
 * <li>05:00之后最早的一次清醒，如果之后只有清醒和浅睡，则删除该清醒状态以后的睡眠数据</li>
 * <li>01:00之前到05:00之后，为清醒状态，所有睡眠数据清除（核心区处于清醒状态）</li>
 *
 * <li>01:00点之前深睡超过1.5小时（整体在01：00之前），则之前的睡眠数据全部清除</li>
 * <li>05:00之后（整体在05：00之后），深睡超过1.5小时，则之后的睡眠数据全部清除</li>
 * <li>01:00和05:00之间(可包含1点或者5点)，深睡超过2.5小时，所有睡眠数据清除</li>
 *
 * <li>什么时候show UI，如何show:
 * ①当前阶段不show，show之前完整的阶段，并按照 1，2，3，4，5，6，7过滤；
 * ②总睡眠（清醒前后的 深睡 + 浅睡 ）时间小于2个小时不show；</li>
 * </ol>
 * Created by bingshanguxue on 22/08/2017.
 */

public class SleepFilter {
    private static final boolean D = true;

    public static final int HALF_AN_HOUR = 30;
    public static final int MINUTES_OF_HOUR = 60;
    public static final int MINUTES_OF_DAY = 24 * MINUTES_OF_HOUR;

    public static final int START_SLEEP_TIME_MINUTE = 18 * MINUTES_OF_HOUR;
    public static final int END_SLEEP_TIME_MINUTE = 18 * MINUTES_OF_HOUR - 1;// Change to 17:59

    public static final int START_AWAKE_CHECK_TIME_MINUTE = MINUTES_OF_HOUR + MINUTES_OF_DAY;
    public static final int END_AWAKE_CHECK_TIME_MINUTE = 5 * MINUTES_OF_HOUR + MINUTES_OF_DAY;

    public static final int START_AWAKE_ARRAY_CHECK_TIME_MINUTE_ADJUST = MINUTES_OF_HOUR + MINUTES_OF_DAY;

    public static final int ALLOW_AWAKE_CHECK_TIME_MINUTE_PRE = 20;
    public static final int ALLOW_AWAKE_CHECK_TIME_MINUTE_BEH = MINUTES_OF_HOUR;
    public static final int ALLOW_AWAKE_CHECK_TIME_MINUTE = ALLOW_AWAKE_CHECK_TIME_MINUTE_PRE;

    public static final int START_DEEP_CHECK_TIME_MINUTE = MINUTES_OF_HOUR + MINUTES_OF_DAY;
    public static final int END_DEEP_CHECK_TIME_MINUTE = 5 * MINUTES_OF_HOUR + MINUTES_OF_DAY;

    public static final int ALLOW_DEEP_CHECK_TIME_MINUTE_PRE = (int) (1.5 * MINUTES_OF_HOUR);
    public static final int ALLOW_DEEP_CHECK_TIME_MINUTE_BEH = (int) (2.5 * MINUTES_OF_HOUR);
    public static final int ALLOW_DEEP_CHECK_TIME_MINUTE = ALLOW_DEEP_CHECK_TIME_MINUTE_PRE;

    public static final int CHECK_NO_ERROR = 0;
    public static final int CHECK_DROP_FRONT = 1;
    public static final int CHECK_DROP_BEHIND = 2;
    public static final int CHECK_DROP_ALL = 3;

    private static final int INDEX_NA = -1;//初始化


    /**
     * 获取<前一天晚上到今天白天区间>睡眠数据
     * 按升序排序并删除时间重复的数据
     */
    public static ArrayList<SleepFilterData> filter(int y, int m, int d, List<SleepData> sleeps) {
        ArrayList<SleepFilterData> sls = new ArrayList<>();

        Calendar c1 = Calendar.getInstance();
        c1.set(y, m - 1, d);// here need decrease 1 of month
        c1.add(Calendar.DATE, -1);
        int yesterdayYear = c1.get(Calendar.YEAR);
        int yesterdayMonth = c1.get(Calendar.MONTH) + 1;
        int yesterdayDay = c1.get(Calendar.DATE);
        ZLogger.d(D, String.format("%d/%d/%d ~ %d/%d/%d",
                yesterdayYear, yesterdayMonth, yesterdayDay,
                y, m, d));

        List<SleepData> d1 = getSubSleepDataByDate(yesterdayYear, yesterdayMonth, yesterdayDay, sleeps);
        if (d1 != null && d1.size() > 0) {
            for (SleepData sl : d1) {
                if (sl.getMinutes() >= SleepFilter.START_SLEEP_TIME_MINUTE
                        && sl.getMinutes() <= SleepFilter.MINUTES_OF_DAY) {
                    SleepFilterData tmp = new SleepFilterData(sl.getId(), sl.getYear(), sl.getMonth(), sl.getDay(),
                            sl.getMinutes(), sl.getMode(), sl.getDate());
                    tmp.setMinutesAxes(tmp.getMinutes() - SleepFilter.START_SLEEP_TIME_MINUTE);
                    sls.add(tmp);
                }
            }
        }
        List<SleepData> d2 = getSubSleepDataByDate(y, m, d, sleeps);
        if (d2 != null && d2.size() > 0) {
            for (SleepData sl : d2) {
                if (sl.getMinutes() >= 0
                        && sl.getMinutes() <= SleepFilter.END_SLEEP_TIME_MINUTE) {
                    SleepFilterData tmp = new SleepFilterData(sl.getId(), sl.getYear(), sl.getMonth(), sl.getDay(),
                            sl.getMinutes(), sl.getMode(), sl.getDate());
                    tmp.setMinutes(tmp.getMinutes() + SleepFilter.MINUTES_OF_DAY);
                    tmp.setMinutesAxes(tmp.getMinutes() - SleepFilter.START_SLEEP_TIME_MINUTE);
                    sls.add(tmp);
                }
            }
        }

        // increase sort the sleep data by minutes
        Collections.sort(sls, new SleepFilterData.IncreaseComparator());

        // Remove the error minute data
        removeSameMinuteSleepData(sls);

        return sls;
    }

    /**
     * 获取指定时间点前最晚的一次清醒
     * */
    private static int getLastAwakeIndex(List<SleepFilterData> sleepDatas, int flagTime) {
        int ret = INDEX_NA;
        int len = sleepDatas.size();
        for (int i = 0; i < len; i++) {
            SleepFilterData sleepData = sleepDatas.get(i);
            if (sleepData.getMinutes() < flagTime) {
                if (sleepData.getMode() == ApplicationLayer.SLEEP_MODE_START_WAKE) {
                    ret = i;
                }
            } else {
                break;
            }
        }

        return ret;
    }

    /**
     * 获取指定时间点后最早的一次清醒
     * */
    private static int getFirstAwakeIndex(List<SleepFilterData> sleepDatas, int flagTime) {
        int ret = INDEX_NA;
        int len = sleepDatas.size();
        for (int i = len -1 ; i >= 0; i--) {
            SleepFilterData sleepData = sleepDatas.get(i);
            if (sleepData.getMinutes() > flagTime) {
                if (sleepData.getMode() == ApplicationLayer.SLEEP_MODE_START_WAKE) {
                    ret = i;
                }
            } else {
                break;
            }
        }

        return ret;
    }

    /**
     * <ol>
     * <li>一点之前最晚的一次清醒，如果之前只有清醒和浅睡，则删除该清醒状态以前的睡眠数据</li>
     * <li>5点之后最早的一次清醒，如果之后只有清醒和浅睡，则删除该清醒状态以后的睡眠数据</li>
     * </ol>
     */
    public static ArrayList<SleepFilterData> filterAwake(List<SleepFilterData> sleepDatas) {
        if (sleepDatas == null || sleepDatas.size() <= 0) {
            return null;
        }

        ArrayList<SleepFilterData> filter1 = new ArrayList<>();
        int len = sleepDatas.size();
        int lastWakeIndex1 = getLastAwakeIndex(sleepDatas, START_AWAKE_CHECK_TIME_MINUTE);
        if (lastWakeIndex1 != INDEX_NA) {
            boolean flag = true;
            for (int i = 0; i < lastWakeIndex1; i++) {
                SleepFilterData sleepData = sleepDatas.get(i);
                if (sleepData.getMode() == ApplicationLayer.SLEEP_MODE_START_DEEP_SLEEP) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                ZLogger.d("一点之前最晚的一次清醒，如果之前只有清醒和浅睡，则删除该清醒状态以前的睡眠数据");
                for (int i = lastWakeIndex1; i < len; i++) {
                    filter1.add(sleepDatas.get(i));
                }
            } else {
                filter1.addAll(sleepDatas);
            }
        } else {
            filter1.addAll(sleepDatas);
        }


        ArrayList<SleepFilterData> filter2 = new ArrayList<>();
        len = filter1.size();
        int firstWakeIndex1 = getFirstAwakeIndex(filter1, END_AWAKE_CHECK_TIME_MINUTE);
        if (firstWakeIndex1 != INDEX_NA) {
            boolean flag = true;
            for (int i = lastWakeIndex1 + 1; i < len; i++) {
                SleepFilterData sleepData = sleepDatas.get(i);
                if (sleepData.getMode() == ApplicationLayer.SLEEP_MODE_START_DEEP_SLEEP) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                ZLogger.d("5点之后最早的一次清醒，如果之后只有清醒和浅睡，则删除该清醒状态以后的睡眠数据");
                for (int i = 0; i <= firstWakeIndex1; i++) {
                    filter2.add(sleepDatas.get(i));
                }
            } else {
                filter2.addAll(filter1);
            }
        } else {
            filter2.addAll(filter1);
        }

        return filter2;
    }

    /**
     * 清醒数据过滤
     * <ol>
     * <li>在01:00点前出现清醒时间，且清醒时间（整体在01：00之前）大于等于20min，则删除之前的睡眠数据。(考虑到早睡情况，这条待确认，考虑目前的测试群体)</li>
     * <li>早上05:00点之后（整体在05：00之后），出现清醒时间超过30min，则之后的数据全部清除</li>
     * <li>凌晨01：00点之前到早晨05：00点之后，为清醒状态，所有睡眠数据清除（核心区处于清醒状态）</li>
     * </ol>
     */
    public static int checkAwakeData(int lastMinutes, int curMinutes) {
        int diffMinutes;
        //凌晨1点之前出现清醒时间
        if (lastMinutes < START_AWAKE_CHECK_TIME_MINUTE) {
            //清醒时段在凌晨1点之前
            if (curMinutes < START_AWAKE_CHECK_TIME_MINUTE) {
                diffMinutes = curMinutes - lastMinutes;
                //清醒时间大于等于20min
                if (diffMinutes >= ALLOW_AWAKE_CHECK_TIME_MINUTE_PRE) {
                    ZLogger.d("在01:00点前出现清醒时间，且清醒时间（整体在01：00之前）大于等于20min，则删除之前的睡眠数据");
                    return CHECK_DROP_FRONT;
                }
            } else if (curMinutes > END_AWAKE_CHECK_TIME_MINUTE) {
                ZLogger.d("凌晨01：00点之前到早晨05：00点之后，为清醒状态，所有睡眠数据清除");
                return CHECK_DROP_ALL;
            }
        }
        //凌晨5点之后出现清醒时间
        else if (lastMinutes > END_AWAKE_CHECK_TIME_MINUTE) {
            //清醒时段在凌晨5点之后
            if (curMinutes > END_AWAKE_CHECK_TIME_MINUTE) {
                //清醒时间超过30min
                diffMinutes = curMinutes - lastMinutes;
                if (diffMinutes > HALF_AN_HOUR) {
                    ZLogger.d("早上05:00点之后（整体在05：00之后），出现清醒时间超过30min，则之后的数据全部清除");
                    return CHECK_DROP_BEHIND;
                }
            }
        }

        return CHECK_NO_ERROR;
    }


    /**
     * 清醒数据过滤
     * <ol>
     * <li>01:00点之前深睡超过1.5小时（整体在01：00之前），则之前的睡眠数据全部清除</li>
     * <li>05:00之后（整体在05：00之后），深睡超过1.5小时，则之后的睡眠数据全部清除</li>
     * <li>01:00和05:00之间(可包含1点或者5点)，深睡超过2.5小时，所有睡眠数据清除</li>
     * </ol>
     */
    public static int checkDeepSleep(int lastMinutes, int curMinutes) {
        int diffMinutes;
        //凌晨1点之前出现深睡时间
        if (lastMinutes < START_DEEP_CHECK_TIME_MINUTE) {
            //深睡时段在凌晨1点之前
            if (curMinutes < START_DEEP_CHECK_TIME_MINUTE) {
                diffMinutes = curMinutes - lastMinutes;
                if (diffMinutes >= ALLOW_DEEP_CHECK_TIME_MINUTE_PRE) {
                    ZLogger.d("01:00点之前深睡超过1.5小时（整体在01：00之前），则之前的睡眠数据全部清除");
                    return CHECK_DROP_FRONT;
                }
            } else {
                diffMinutes = Math.min(curMinutes, END_DEEP_CHECK_TIME_MINUTE) - START_DEEP_CHECK_TIME_MINUTE;
                if (diffMinutes >= ALLOW_DEEP_CHECK_TIME_MINUTE_BEH) {
                    ZLogger.d("01:00和05:00之间(可包含1点或者5点)，深睡超过2.5小时，所有睡眠数据清除");
                    return CHECK_DROP_ALL;
                }
            }
        }
        //凌晨5点之后出现深睡时间
        else if (lastMinutes > END_DEEP_CHECK_TIME_MINUTE) {
            //深睡时段在凌晨5点之后
            if (curMinutes > END_DEEP_CHECK_TIME_MINUTE) {
                diffMinutes = curMinutes - lastMinutes;
                if (diffMinutes >= ALLOW_DEEP_CHECK_TIME_MINUTE_PRE) {
                    ZLogger.d("05:00之后（整体在05：00之后），深睡超过1.5小时，则之后的睡眠数据全部清除");
                    return CHECK_DROP_BEHIND;
                }
            }
        } else {
            diffMinutes = Math.min(curMinutes, END_DEEP_CHECK_TIME_MINUTE) - lastMinutes;
            if (diffMinutes >= ALLOW_DEEP_CHECK_TIME_MINUTE_BEH) {
                ZLogger.d("01:00和05:00之间(可包含1点或者5点)，深睡超过2.5小时，所有睡眠数据清除");
                return CHECK_DROP_ALL;
            }
        }

        return CHECK_NO_ERROR;
    }

    /**
     * 判断是否显示睡眠数据
     * <ol>
     * <li>当前阶段不show，show之前完整的阶段，并按照 1，2，3，4，5，6，7过滤</li>
     * <li>总睡眠（清醒前后的 深睡 + 浅睡 ）时间小于2个小时不show</li>
     * </ol>
     */
    public static boolean isNeedShowUi() {
        return true;
    }

    /**
     * Remove some minute data.
     * You must make sure the data have be sort.
     */
    public static void removeSameMinuteSleepData(List<SleepFilterData> sleeps) {
        int lastMinute = -1;
        SleepFilterData sl;
        if (sleeps != null) {
            for (int i = 0; i < sleeps.size(); i++) {
                sl = sleeps.get(i);
                if (lastMinute != -1) {
                    if (sl.getMinutes() == lastMinute) {
                        sleeps.remove(sl);
                        ZLogger.d(D, "lastMinute: " + lastMinute
                                + ", same date: " + sl.toString());
                        i = i - 1;
                        continue;
                    }
                }
                lastMinute = sl.getMinutes();
            }
        }
    }

    /**
     * Get the sub SleepData list by the special date, if didn't find, it will return
     * null.
     *
     * @param sleeps the input data
     * @return Sub data list of the special date.
     */
    public static List<SleepData> getSubSleepDataByDate(
            int y, int m, int d,
            List<SleepData> sleeps) {
        ArrayList<SleepData> sls = new ArrayList<>();
        for (SleepData sl : sleeps) {
            if (sl.getYear() == y
                    && sl.getMonth() == m
                    && sl.getDay() == d) {
                sls.add(sl);
            }
        }
        return sls;
    }



    /**
     * Sleep data list Increase Comparator class, sort by the minutes.
     */
    public static class IncreaseComparator implements Comparator {

        public int compare(Object arg0, Object arg1) {
            return compareSleep((SleepData) arg0, (SleepData) arg1);
        }

        public int compareSleep(SleepData o1, SleepData o2) {
            if (o1.getMinutes() > o2.getMinutes()) {
                return 1;
            } else if (o1.getMinutes() < o2.getMinutes()) {
                return -1;
            } else {
                return 0;
            }
        }
    }
}

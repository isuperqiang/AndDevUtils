package com.richie.utils;

import com.richie.easylog.ILogger;
import com.richie.easylog.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by songbao on 2018/1/24 0024
 * My email : logisong@163.com
 * The role of this :
 */
public class DateUtils {
    private static ILogger logger = LoggerFactory.getLogger(DateUtils.class);

    private static String mYear; // 当前年
    private static String mMonth; // 月
    private static String mDay;

    /**
     * 获取当前月日
     *
     * @return
     */
    public static String getDate() {
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        mYear = String.valueOf(c.get(Calendar.YEAR));// 获取当前年份
        mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
        mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
        return mYear + "-" + mMonth + "-" + mDay;
    }

    /**
     * 获取当日零点的时间戳
     *
     * @return
     */
    public static long getTodayZeroTime() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    /**
     * 格式化时间，显示时分。12:00
     * HH  大写24小时制  小写12小时制
     *
     * @param time
     * @return
     */
    public static String formatTimeInHourMin(long time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return simpleDateFormat.format(new Date(time));
    }

    /**
     * 格式化时间，显示年月日
     *
     * @param time
     * @return
     */
    public static String formatTimeInYearMonDay(long time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return simpleDateFormat.format(new Date(time));
    }

    /**
     * 格式化时间
     *
     * @param timeInMills
     * @return
     */
    public static String formatDateTime(long timeInMills) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        return simpleDateFormat.format(new Date(timeInMills));
    }

    /**
     * 从字符串解析日期，返回毫秒值
     *
     * @param dateS
     * @return
     */
    public static long parseMillsFromDateStr(String dateS) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        Date date = null;
        try {
            date = simpleDateFormat.parse(dateS);
        } catch (ParseException e) {
            logger.error(e);
        }
        return date != null ? date.getTime() : System.currentTimeMillis();
    }

    /**
     * 从字符串解析日期，返回毫秒值
     *
     * @param dateS
     * @return
     */
    public static long parseMillsFromDateString(String dateS) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        Date date = null;
        try {
            date = simpleDateFormat.parse(dateS);
        } catch (ParseException e) {
            logger.error(e);
        }
        return date != null ? date.getTime() : 0;
    }

}

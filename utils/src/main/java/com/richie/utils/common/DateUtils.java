package com.richie.utils.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Richie on 2018/1/24 0024
 */
public final class DateUtils {

    /**
     * 获取当前月日
     *
     * @return
     */
    public static String getDate() {
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        String year = String.valueOf(c.get(Calendar.YEAR));
        String month = String.valueOf(c.get(Calendar.MONTH) + 1);
        String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
        return year + "-" + month + "-" + day;
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
            // ignored
        }
        return date != null ? date.getTime() : System.currentTimeMillis();
    }

}

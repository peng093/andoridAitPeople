package com.example.atpeople.myapplication.ui.calendar.utils;

import android.util.Log;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

/**
 * Created by memfis on 3/26/15.
 * Base collection of useful methods for working with
 *
 * @see java.util.Date
 */
public final class DateUtils {

    private final static String TAG = "DateUtils";

    public static final int MAX_YEAR_RANGE = 50;

    public static final long WEEK_TIME_MILLIS = 7 * 24 * 60 * 60 * 1000;

    public static final String MMMM_yyyy = "MMMM yyyy";
    public static final String cccc_dd = "cccc dd";

    public static final Calendar today = getCalendarInstance();//TODO: need update current value correctly

    public static Locale danish = new Locale("da", "DK");
    public static Map<String, DateFormat> map = new HashMap<>();

    public static Locale getLocale() {
        return danish;
    }

    public static Calendar getCalendarInstance() {
        try {
            return Calendar.getInstance(getTimeZone(), getLocale());
        } catch (Exception e) {
            Log.e(TAG, "Can't create instance of the specified calendar. ");
        }
        return Calendar.getInstance();
    }

    public static TimeZone getTimeZone() {
        return TimeZone.getDefault();
    }


    /**
     * 将毫秒值转换为指定格式的字符串
     * @param : @param time	毫秒值
     * @param : @param fomat 格式
     * @return type:String	返回格式化后的字符串
     */
    public static String getFormatString(long time, String fomat) {
        if (time!=0){
            int b=(""+time).length();
            // 如果不大于9位数,就*1000
            long newTime=b>10?time:time*1000;
            String formatString = "";
            if (fomat != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat(fomat);
                formatString = dateFormat.format(new Date(newTime));
            }
            return formatString;
        }else {
            return "Unknown";
        }
    }

    public static boolean isTheSameDay(long time,long time2) {
        try {
            Calendar nowCal = Calendar.getInstance();
            Calendar dataCal = Calendar.getInstance();
            SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
            SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");

            String data1 = df1.format(time);
            String data2 = df2.format(time2);
            java.util.Date now = df1.parse(data1);
            java.util.Date date = df2.parse(data2);
            nowCal.setTime(now);
            dataCal.setTime(date);
            return isSameDay(nowCal, dataCal);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Calendar setTimeToYearStart(Calendar calendar) {
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }


    public static Calendar setTimeToYearEnd(Calendar calendar) {
        calendar.set(Calendar.MONTH, Calendar.DECEMBER);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar;
    }

    public static Date setTimeToYearStart(Date date) {
        Calendar calendar = getCalendarInstance();
        calendar.setTime(date);
        setTimeToMidnight(calendar);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        return calendar.getTime();
    }

    public static Date setTimeToYearEnd(Date date) {
        Calendar calendar = getCalendarInstance();
        calendar.setTime(date);
        setTimeToEndOfTheDay(calendar);
        calendar.set(Calendar.MONTH, Calendar.DECEMBER);
        calendar.set(Calendar.DAY_OF_MONTH, 31);
        return calendar.getTime();
    }

    public static Date setTimeToMonthStart(Date date) {
        Calendar calendar = getCalendarInstance();
        calendar.setTime(date);
        setTimeToMidnight(calendar);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }

    public static Calendar setTimeToMonthStart(Calendar calendar) {
        calendar = setTimeToMidnight(calendar);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar;
    }

    public static Date setTimeToMonthEnd(Date date) {
        Calendar calendar = getCalendarInstance();
        calendar.setTime(date);
        setTimeToEndOfTheDay(calendar);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return calendar.getTime();
    }

    public static Calendar setTimeToMidnight(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar;
    }

    /**
     * Sets the date time to the midnight.
     *
     * @param date - to set the time
     * @return - the same date with new time
     */
    public static Date setTimeToMidnight(Date date) {
        Calendar calendar = getCalendarInstance();
        calendar.setTimeInMillis(date.getTime());
        calendar = setTimeToMidnight(calendar);

        return calendar.getTime();
    }

    public static Calendar setTimeToEndOfTheDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar;
    }

    /**
     * Sets the date time to the end of the day.
     *
     * @param date - to set the time
     * @return - the same date with new time
     */
    public static Date setTimeToEndOfTheDay(Date date) {
        Calendar calendar = getCalendarInstance();
        calendar.setTimeInMillis(date.getTime());
        calendar = setTimeToEndOfTheDay(calendar);


        return calendar.getTime();
    }

    /**
     * Method returns number of days between two dates using hours/minutes for calculation.
     * Example: start = 20/12/2015T15:00:00, end = 21/12/2015T13:00:00, result = 0 days.
     * Use the same time for different dates to avoid such problem.
     *
     * @param start - start date
     * @param end   - end period
     * @return - number of years between start and end date.
     * @see #setTimeToMidnight(java.util.Date)
     * @see #setTimeToEndOfTheDay(java.util.Date)
     */

    /**
     * @Author Peng
     * @Date 2020/3/4 17:20
     * @Describe 根据当前月份第一天和最后一天来计算共有几个周末
     */
    public static int monthBetweenPure(Date start, Date end) {
        Calendar startCalendar = getCalendarInstance();
        startCalendar.setTime(start);
        Calendar endCalendar = getCalendarInstance();
        endCalendar.setTime(end);
        Log.e(TAG, "endMONTH: "+endCalendar.get(Calendar.MONTH) );
        Log.e(TAG, "startMONTH: "+startCalendar.get(Calendar.MONTH) );
        Log.e(TAG, "endYEAR: "+endCalendar.get(Calendar.YEAR) );
        Log.e(TAG, "startYEAR: "+startCalendar.get(Calendar.YEAR) );
        int diffYear = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
        int diffMonth = diffYear * 12 + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);
        return diffMonth;
    }






    public static void resetTime(GregorianCalendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }





    public static long getCurrentTime() {
        Calendar calendar = getCalendarInstance();
        long offset = calendar.get(Calendar.ZONE_OFFSET);
        return calendar.getTimeInMillis() - offset;
    }

    public static boolean isLastDayOfTheMonth(Date date) {
        Calendar calendar = getCalendarInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH) == calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public static Date addYearToDate(Date date, int yearsToAdd) {
        Calendar calendar = getCalendarInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, yearsToAdd);
        return calendar.getTime();
    }

    public static Date addMonthToDate(Date date, int monthsToAdd) {
        Calendar calendar = getCalendarInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, monthsToAdd);
        return calendar.getTime();
    }

    public static Date addDayToDate(Date date, int daysToAdd) {
        Calendar calendar = getCalendarInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, daysToAdd);
        return calendar.getTime();
    }

    public static String dateToString(Calendar calendar, String pattern) {
        Date date = calendarToDate(calendar);
        return dateToString(date, pattern);
    }

    public static Date calendarToDate(Calendar calendar) {
        return new Date(calendar.getTimeInMillis());
    }

    public static String dateToString(Date date, String pattern) {
        return getDateFormat(pattern).format(date);
    }

    public static DateFormat getDateFormat(String pattern) {
        if (!map.containsKey(pattern)) {
            DateFormat df = new SimpleDateFormat(pattern, getLocale());
            map.put(pattern, df);
        }
        return map.get(pattern);
    }

    public static boolean isSameDay(Date firstDate, Date secondDate) {
        Calendar firstCalendar = getCalendarInstance();
        Calendar secondCalendar = getCalendarInstance();

        firstCalendar.setTime(firstDate);
        secondCalendar.setTime(secondDate);

        return isSameDay(firstCalendar, secondCalendar);
    }

    public static boolean isSameMonth(Calendar firstCalendar, Calendar secondCalendar) {
        if (firstCalendar == null || secondCalendar == null) return false;
        return firstCalendar.get(Calendar.YEAR) == secondCalendar.get(Calendar.YEAR)
                && firstCalendar.get(Calendar.MONTH) == secondCalendar.get(Calendar.MONTH);
    }

    public static boolean isSameYear(Calendar firstCalendar, Calendar secondCalendar) {
        if (firstCalendar == null || secondCalendar == null) return false;
        return firstCalendar.get(Calendar.YEAR) == secondCalendar.get(Calendar.YEAR);
    }

    public static boolean isSameDay(Calendar firstDate, Calendar secondDate) {
        return (firstDate.get(Calendar.DAY_OF_YEAR) == secondDate.get(Calendar.DAY_OF_YEAR)
                && firstDate.get(Calendar.YEAR) == secondDate.get(Calendar.YEAR));
    }

    //TODO: find other solution without creation of Calendar instance for performance

    public static Date roundDateToMinute(Date date) {
        if (date == null) return null;
        Calendar calendar = getCalendarInstance();
        calendar.setTime(date);

        if (calendar.get(Calendar.SECOND) > 30) {
            calendar.add(Calendar.MINUTE, 1);
        }

        calendar.set(Calendar.SECOND, 0);

        return calendar.getTime();
    }

    public static Date cutDateSeconds(Date date) {
        if (date == null) return null;
        Calendar calendar = getCalendarInstance();
        calendar.setTime(date);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    /**
     * <p>Checks if a date is today.</p>
     *
     * @param date the date, not altered, not null.
     * @return true if the date is today.
     * @throws IllegalArgumentException if the date is <code>null</code>
     */
    public static boolean isToday(Date date) {
        return isSameDay(date, today.getTime());
    }

    /**
     * <p>Checks if a calendar date is today.</p>
     *
     * @param calendar the calendar, not altered, not null
     * @return true if cal date is today
     * @throws IllegalArgumentException if the calendar is <code>null</code>
     */
    public static boolean isToday(Calendar calendar) {
        return isSameDay(calendar, today);
    }
}

package com.example.atpeople.myapplication.ui.calendar.utils;

import android.text.format.DateFormat;
import android.util.Log;
import android.util.Pair;

import com.example.atpeople.myapplication.ui.calendar.model.ListItemModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Create by peng on 2020/3/5
 */
public class TimeUtils {
    static Calendar calendar = Calendar.getInstance();

    /**
     * 获取指定年份、月份的第一天是一周中的第几天
     *
     * @param year  年
     * @param month 月
     * @return year年-month月的第一天是一周中的第几天（周日为第一天）
     * @descript 周日为第一天，周6为第七天
     */
    public static int getFirstDayOfWeekInMonth(int year, int month) {
        // 设置当前日期为：year年-month月-1号
        calendar.set(year, month, 1);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 获取指定年份、月份的上一月份的最后一天
     *
     * @param year  年
     * @param month 月
     * @return year年-month月的上一月的最后一天
     */
    public static int getLastDayOfLastMonth(int year, int month) {
        calendar.set(year, month, 1);
        // 向前滚动一个月
        calendar.add(Calendar.MONTH, -1);
        // 返回最后一天
        return calendar.getActualMaximum(Calendar.DATE);
    }

    /**
     * 获取指定年份、月份的上个月的最后一天是一周中的第几天
     *
     * @param year
     * @param month
     * @return year年-month月的上一月的最后一天是一周中的第几天（周日为第一天）
     */
    public static int getLastDayOfWeekInLastMonth(int year, int month) {
        calendar.set(year, month, 1);
        // 向前滚动一天，即到前一月最后一天
        calendar.add(Calendar.DATE, -1);
        // 返回最后一天是一周的第几天
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 获取指定年份、月份的下个月的第一天是一周中的第几天
     *
     * @param year
     * @param month
     * @return year年-month月的上一月的第一天是一周中的第几天（周日为第一天）
     */
    public static int getFirstDayOfWeekInNextMonth(int year, int month) {
        calendar.set(year, month, 1);
        // 向后滚动一天，即到后一月
        calendar.add(Calendar.MONTH, 1);
        // 返回第一天是一周的第几天
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * @Author Peng
     * @Date 2020/3/5 8:52
     * @Describe 获取指定xx年~xx年的，每一年的月份
     */

    public static List<Pair> getInitMonthMapWithZero(int star_year, int end_year) {
        end_year = end_year + 1;
        // Pair 可以存两个不同类型的数据
        List<Pair> list = new ArrayList<>();
        for (int i = star_year; i < end_year; i++) {
            for (int i1 = 0; i1 < 12; i1++) {
                list.add(new Pair(i, i1 + 1));
            }
        }
        return list;
    }


    /**
     * 演示用
     *
     * @param date
     * @param format
     * @return
     */
    public static String formatDate(Date date, String format) {
        if (null == format || "".equals(format.trim())) {
            format = "yyyy-MM-dd";
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    /**
     * 演示用
     *
     * @param dateStr
     * @param format
     * @return
     */
    public static Date parseDate(String dateStr, String format) {
        if (null == format || "".equals(format.trim())) {
            format = "yyyy-MM-dd";
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        try {
            return dateFormat.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 获取每个月第一个星期日的日期
     * @param year  年份
     * @param month 月份
     * @return Date 日期
     */
    public static void getFirstSundayOfMonth(int year, int month, List<ListItemModel> listItemModels) {
        Calendar cal = Calendar.getInstance();
        // 设置当前月的 1号
        cal.set(year,month - 1,1);
        int monthValue = cal.get(Calendar.MONTH);
        // Calendar.SUNDAY==1 表示本周的第一天
        // 查看1号是第一周（1号最靠近的周，肯定是第一周）的第几天，
        // 如果不是第一天就往后加一天，直到是一周中的第一天为止
        while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            Log.e(TAG, "DAY_OF_WEEK=="+cal.get(Calendar.DAY_OF_WEEK));
            cal.add(Calendar.DATE, 1);
        }
        // 计算当前月的，所有周的起始日期
        while (cal.get(Calendar.MONTH) == monthValue) {
            // 上面已经知道第一周的第一天，往后加6天就是第一周的最后一天
            Calendar start = DateUtils.setTimeToMidnight((Calendar) cal.clone());
            cal.add(Calendar.DATE, 6);
            Calendar end = DateUtils.setTimeToEndOfTheDay((Calendar) cal.clone());
            listItemModels.add(new ListItemModel(start, new Pair<>(start, end), ListItemModel.WEEK));
            // 新的一周要加1天来计算
            cal.add(Calendar.DATE, 1);
        }
    }
}

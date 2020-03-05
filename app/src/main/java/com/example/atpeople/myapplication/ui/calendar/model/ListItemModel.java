package com.example.atpeople.myapplication.ui.calendar.model;

import java.util.Calendar;

/**
 * Create by peng on 2020/3/5
 */
public class ListItemModel {
    public static final int MONTH = 460;
    public static final int WEEK = 430;
    public static final int EVENT = 610;

    private Calendar calendar;
    private Object value;
    private int type;

    public ListItemModel(Calendar calendar, Object value, int type) {
        this.calendar = calendar;
        this.value = value;
        this.type = type;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}

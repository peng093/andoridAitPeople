package com.example.atpeople.myapplication.ui.chart;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

/**
 * Create by peng on 2019/8/2
 */
public class XAxisValueFormatter implements IAxisValueFormatter {
    // private String[] xStrs = new String[]{"春", "夏", "秋", "冬"};
    private String[] mMonths = new String[]{
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dec"};
    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        int position = (int) value;
        if (position >= 12) {
            position = 0;
        }
        return mMonths[position];
    }

}

package com.example.atpeople.myapplication.ui.chart;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.atpeople.myapplication.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by peng on 2019/8/2
 */
public class MPAndroidChart extends AppCompatActivity implements OnChartValueSelectedListener {
    private String[] mMonths = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dec"};

    private Typeface mTfRegular;
    private Typeface mTfLight;
    protected BarChart mChart;
    private PieChart pieChart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mpa_chart);
        mTfRegular = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
        mTfLight = Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf");

        mChart = findViewById(R.id.chart1);
        pieChart = (PieChart) findViewById(R.id.pie_chat1);

        initBarChart();
        // 饼状态
        showhodlePieChart();
    }
    /**
     * 初始化柱形图控件属性
     */
    private void initBarChart() {
        mChart.setOnChartValueSelectedListener(this);
        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);
        mChart.getDescription().setEnabled(false);
        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        mChart.setMaxVisibleValueCount(60);
        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);
        mChart.setDrawGridBackground(false);

        //自定义坐标轴适配器，配置在X轴，xAxis.setValueFormatter(xAxisFormatter);
        IAxisValueFormatter xAxisFormatter = new XAxisValueFormatter();

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTypeface(mTfLight);
        xAxis.setDrawAxisLine(false);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(xAxisFormatter);


        //自定义坐标轴适配器，配置在Y轴。leftAxis.setValueFormatter(custom);
        IAxisValueFormatter custom = new MyAxisValueFormatter();

        //设置限制临界线
        LimitLine limitLine = new LimitLine(3f, "临界点");
        limitLine.setLineColor(Color.GREEN);
        limitLine.setLineWidth(1f);
        limitLine.setTextColor(Color.GREEN);

        //获取到图形左边的Y轴
        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.addLimitLine(limitLine);
        leftAxis.setTypeface(mTfLight);
        leftAxis.setLabelCount(8, false);
        leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setAxisMaximum(16f);
        //获取到图形右边的Y轴，并设置为不显示
        mChart.getAxisRight().setEnabled(false);

        //图例设置
        Legend legend = mChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);
        legend.setForm(Legend.LegendForm.SQUARE);
        // 设置左边的数值倍数
        legend.setFormSize(9f);
        legend.setTextSize(11f);
        legend.setXEntrySpace(4f);

        //如果点击柱形图，会弹出pop提示框.XYMarkerView为自定义弹出框
        XYMarkerView mv = new XYMarkerView(this, xAxisFormatter);
        mv.setChartView(mChart);
        mChart.setMarker(mv);
        setBarChartData();
    }


    /**
     * @Author Peng
     * @Date 2019/8/2 11:02
     * @Describe 饼图
     */
    private void showhodlePieChart() {
        // 设置每份所占数量
        List<PieEntry> yvals = new ArrayList<>();
        yvals.add(new PieEntry(1.0f, "汉族"));
        yvals.add(new PieEntry(2.0f, "回族"));
        yvals.add(new PieEntry(3.0f, "壮族"));
        yvals.add(new PieEntry(1.0f, "维吾尔族"));
        yvals.add(new PieEntry(3.0f, "土家族"));
        //设置每份的颜色
        List<Integer> colors = new ArrayList<>();
        colors.add(Color.RED);
        colors.add(Color.BLACK);
        colors.add(Color.BLUE);
        colors.add(Color.YELLOW);
        colors.add(Color.GREEN);

        PieChartManagger pieChartManagger=new PieChartManagger(pieChart);
        pieChartManagger.showSolidPieChart(yvals,colors);
    }

    private float getRandom(float range, float startsfrom) {
        return (float) (Math.random() * range) + startsfrom;
    }


    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }
    /**
     * @Author Peng
     * @Date 2019/8/2 10:25
     * @Describe x轴设置数据
     */
    private void setBarChartData() {
        // 加入有12个月，给每一个月份，设置数值，并保存到集合中
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        for (int i = 0; i < 12; i++) {
            yVals1.add(new BarEntry(i, i+1));
        }

        BarDataSet set1;
        if (mChart.getData() != null && mChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            // BarDataSet需要集合+左下角标题，得到一个对象
            set1 = new BarDataSet(yVals1, "The year 2019");
            set1.setDrawIcons(false);
            // 类BarDataSet实现了IBarDataSet
            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);
            // BarData需要接口集合
            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setValueTypeface(mTfLight);
            data.setBarWidth(0.9f);

            mChart.setData(data);
        }
    }

}

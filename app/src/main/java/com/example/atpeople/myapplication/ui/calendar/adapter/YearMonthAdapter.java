package com.example.atpeople.myapplication.ui.calendar.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.atpeople.myapplication.R;
import com.example.atpeople.myapplication.ui.calendar.model.ListItemModel;
import com.example.atpeople.myapplication.ui.calendar.utils.TimeUtils;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


/**
 * Create by peng on 2019/7/18
 */
public class YearMonthAdapter extends BaseQuickAdapter<Pair, BaseViewHolder> {
    ImageView iv_month;
    List<Event> eventList;
    public YearMonthAdapter(@Nullable List<Pair> data, List<Event> eventList) {
        super(R.layout.adapter_year_month, data);
        this.eventList=eventList;
}

    @Override
    protected void convert(BaseViewHolder helper, Pair item) {
        iv_month=helper.getView(R.id.iv_month);
        TextView tv_text=helper.getView(R.id.tv_text);
        RecyclerView rv_week_list=helper.getView(R.id.rv_week_list);
        tv_text.setText(item.first+" "+ item.second+"月");
        iv_month.setImageResource(getBackgroundId((int)item.second-1));
        showAllWeeksOnMonth(item,rv_week_list);
    }

    private void showAllWeeksOnMonth(Pair item,RecyclerView rv_list) {
        rv_list.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
        List<ListItemModel> listItemModels=new ArrayList<>();
        // 获取该年月的所有周数
        TimeUtils.getFirstSundayOfMonth((int)item.first,(int)item.second,listItemModels);
        WeeksAdapter weeksAdapter=new WeeksAdapter(listItemModels,eventList);
        rv_list.setAdapter(weeksAdapter);
    }
    private int getBackgroundId(int month) {
        int backgroundId = R.mipmap.bkg_12_dec;
        if (month == Calendar.JANUARY) {
            backgroundId = R.mipmap.bkg_01_jan;
        } else if (month == Calendar.FEBRUARY) {
            backgroundId = R.mipmap.bkg_02_feb;
        } else if (month == Calendar.MARCH) {
            backgroundId = R.mipmap.bkg_03_mar;
        } else if (month == Calendar.APRIL) {
            backgroundId = R.mipmap.bkg_04_apr;
        } else if (month == Calendar.MAY) {
            backgroundId = R.mipmap.bkg_05_may;
        } else if (month == Calendar.JUNE) {
            backgroundId = R.mipmap.bkg_06_jun;
        } else if (month == Calendar.JULY) {
            backgroundId = R.mipmap.bkg_07_jul;
        } else if (month == Calendar.AUGUST) {
            backgroundId = R.mipmap.bkg_08_aug;
        } else if (month == Calendar.SEPTEMBER) {
            backgroundId = R.mipmap.bkg_09_sep;
        } else if (month == Calendar.OCTOBER) {
            backgroundId = R.mipmap.bkg_10_oct;
        } else if (month == Calendar.NOVEMBER) {
            backgroundId = R.mipmap.bkg_11_nov;
        }
        return backgroundId;
    }

}

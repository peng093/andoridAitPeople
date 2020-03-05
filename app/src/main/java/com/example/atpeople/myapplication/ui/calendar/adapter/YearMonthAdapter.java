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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Create by peng on 2019/7/18
 */
public class YearMonthAdapter extends BaseQuickAdapter<Pair, BaseViewHolder> {
    ImageView iv_month;
    public YearMonthAdapter(@Nullable List<Pair> data) {
        super(R.layout.adapter_year_month, data);

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
        TimeUtils.getFirstSundayOfMonth((int)item.first,(int)item.second,listItemModels);
        WeeksAdapter weeksAdapter=new WeeksAdapter(new ArrayList<ListItemModel>());
        rv_list.setAdapter(weeksAdapter);
        weeksAdapter.setNewData(listItemModels);
    }
    private int getBackgroundId(int month) {
        int backgroundId = io.github.memfis19.cadar.R.drawable.bkg_12_december;
        if (month == Calendar.JANUARY) {
            backgroundId = io.github.memfis19.cadar.R.drawable.bkg_01_january;
        } else if (month == Calendar.FEBRUARY) {
            backgroundId = io.github.memfis19.cadar.R.drawable.bkg_02_february;
        } else if (month == Calendar.MARCH) {
            backgroundId = io.github.memfis19.cadar.R.drawable.bkg_03_march;
        } else if (month == Calendar.APRIL) {
            backgroundId = io.github.memfis19.cadar.R.drawable.bkg_04_april;
        } else if (month == Calendar.MAY) {
            backgroundId = io.github.memfis19.cadar.R.drawable.bkg_05_may;
        } else if (month == Calendar.JUNE) {
            backgroundId = io.github.memfis19.cadar.R.drawable.bkg_06_june;
        } else if (month == Calendar.JULY) {
            backgroundId = io.github.memfis19.cadar.R.drawable.bkg_07_july;
        } else if (month == Calendar.AUGUST) {
            backgroundId = io.github.memfis19.cadar.R.drawable.bkg_08_august;
        } else if (month == Calendar.SEPTEMBER) {
            backgroundId = io.github.memfis19.cadar.R.drawable.bkg_09_september;
        } else if (month == Calendar.OCTOBER) {
            backgroundId = io.github.memfis19.cadar.R.drawable.bkg_10_october;
        } else if (month == Calendar.NOVEMBER) {
            backgroundId = io.github.memfis19.cadar.R.drawable.bkg_11_november;
        }
        return backgroundId;
    }
}

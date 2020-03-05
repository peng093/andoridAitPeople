package com.example.atpeople.myapplication.ui.calendar.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Pair;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.atpeople.myapplication.R;
import com.example.atpeople.myapplication.ui.calendar.GoogleCalendarActivity;
import com.example.atpeople.myapplication.ui.calendar.model.ListItemModel;
import com.example.atpeople.myapplication.ui.calendar.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.github.memfis19.cadar.data.entity.Event;

/**
 * Create by peng on 2019/7/18
 */
public class WeeksAdapter extends BaseQuickAdapter<ListItemModel, BaseViewHolder> {
    public WeeksAdapter(@Nullable List<ListItemModel> data) {
        super(R.layout.adapter_weeks, data);

}

    @Override
    protected void convert(BaseViewHolder helper, ListItemModel item) {
        TextView tv_text=helper.getView(R.id.tv_text);
        RecyclerView rv_event_list=helper.getView(R.id.rv_event_list);
        Pair pair=(Pair) item.getValue();
        Calendar st_calendar=(Calendar)pair.first;
        Calendar en_calendar=(Calendar)pair.second;
        String start_week= DateFormat.format("MM/dd", st_calendar).toString();
        String end_week= DateFormat.format("MM/dd", en_calendar).toString();
        // 1-02 1-08
        tv_text.setText(start_week+"-"+end_week);
        showEventList(st_calendar,en_calendar,rv_event_list);
    }

    private void showEventList(Calendar st_calendar,Calendar en_calendar,RecyclerView rv_event_list) {
        // 根据当前周的起始日期，去匹配事件的日期
        rv_event_list.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
        EventListAdapter eventListAdapter=new EventListAdapter(new ArrayList<Event>());
        rv_event_list.setAdapter(eventListAdapter);
        List<Event>list= GoogleCalendarActivity.getEventListByTime(st_calendar,en_calendar);
        eventListAdapter.setNewData(list);
    }
}

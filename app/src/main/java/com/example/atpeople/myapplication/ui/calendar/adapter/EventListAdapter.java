package com.example.atpeople.myapplication.ui.calendar.adapter;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.format.DateFormat;
import android.text.style.RelativeSizeSpan;
import android.util.Pair;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.atpeople.myapplication.R;
import com.example.atpeople.myapplication.ui.calendar.model.ListItemModel;
import com.example.atpeople.myapplication.util.TextBitmap;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import io.github.memfis19.cadar.data.entity.Event;
import io.github.memfis19.cadar.internal.utils.DateUtils;

/**
 * Create by peng on 2019/7/18
 */
public class EventListAdapter extends BaseQuickAdapter<Event, BaseViewHolder> {
    private final String DAY_NUMBER_FORMAT = "dd";
    private final String WEEK_DAY_FORMAT = "EEE";
    private final String TIME_FORMAT = "HH:mm";

    public EventListAdapter(@Nullable List<Event> data) {
        super(R.layout.adapter_events, data);

}

    @Override
    protected void convert(BaseViewHolder helper, Event item) {
        TextView day_title=helper.getView(R.id.day_title);
        LinearLayout event_layout=helper.getView(R.id.event_layout);
        TextView event_title=helper.getView(R.id.event_title);
        TextView event_time=helper.getView(R.id.event_time);

        int radius=TextBitmap.dip2px(3);
        int stroke=TextBitmap.dip2px(2);
        Drawable fill = TextBitmap.getRoundRectDrawable(radius, Color.parseColor("#0087be"), true, stroke);
        event_layout.setBackground(fill);

        // 对比前一条数据是否在同一天
        final boolean displayDayIndicator;
        if(getData().size()>1){
            if(helper.getLayoutPosition()>0){
                Event beforeItem= getData().get(helper.getLayoutPosition()-1);
                // 相同就不显示
                displayDayIndicator = !DateUtils.isSameDay(beforeItem.getEventStartDate(), item.getEventStartDate());
            }else {
                displayDayIndicator=true;
            }
        }else {
            displayDayIndicator=true;
        }

        if (displayDayIndicator) {
            day_title.setVisibility(View.VISIBLE);
            // 几号
            String dayNumber = DateFormat.format(DAY_NUMBER_FORMAT, item.getEventStartDate()).toString();
            int dayNumberLength = dayNumber.length();
            // 周几
            String week=DateFormat.format(WEEK_DAY_FORMAT, item.getEventStartDate()).toString();
            Spannable dayLabelText = new SpannableString(dayNumber + "\n" + week);
            int wholeLength = dayLabelText.length();
            // RelativeSizeSpan设置字体的相对大小
            dayLabelText.setSpan(new RelativeSizeSpan(0.5f), dayNumberLength, wholeLength, 0);
            day_title.setText(dayLabelText);
        } else {
            day_title.setVisibility(View.INVISIBLE);
        }

        event_title.setText(item.getEventTitle());
        SimpleDateFormat timeFormat = new SimpleDateFormat(TIME_FORMAT, DateUtils.getLocale());
        event_time.setText(timeFormat.format(item.getEventStartDate()));
    }

}

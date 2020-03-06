package io.github.memfis19.cadar.internal.ui.month.adapter.holder;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import io.github.memfis19.cadar.R;
import io.github.memfis19.cadar.data.entity.Event;
import io.github.memfis19.cadar.event.OnDayChangeListener;
import io.github.memfis19.cadar.internal.ui.month.MonthCalendarHelper;
import io.github.memfis19.cadar.internal.ui.month.adapter.decorator.MonthDayDecorator;
import io.github.memfis19.cadar.internal.ui.month.adapter.decorator.factory.MonthDayDecoratorFactory;
import io.github.memfis19.cadar.internal.utils.DateUtils;

/**
 * Created by memfis on 7/13/16.
 */
public class MonthDayHolder extends RecyclerView.ViewHolder {

    private View itemView;

    private TextView dayNumberView;
    private LinearLayout lly_root;
    private ImageView iv_indicator;
    private Calendar day;
    private boolean displayDaysOutOfMonth = true;

    private MonthDayDecorator monthDayDecorator;

    public MonthDayHolder(View itemView,
                          boolean displayDaysOutOfMonth,
                          final OnDayChangeListener onDateChangeListener,
                          MonthDayDecoratorFactory monthDayDecoratorFactory) {
        super(itemView);

        this.itemView = itemView;
        this.displayDaysOutOfMonth = displayDaysOutOfMonth;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onDateChangeListener != null) {
                    MonthCalendarHelper.updateSelectedDay(day);
                    onDateChangeListener.onDayChanged(day);
                }
            }
        });

        if (monthDayDecoratorFactory != null) {
            monthDayDecorator = monthDayDecoratorFactory.createMonthDayDecorator(itemView);
        } else {
            dayNumberView = (TextView) itemView.findViewById(R.id.month_view_item_content);
            lly_root = (LinearLayout) itemView.findViewById(R.id.lly_root);
            iv_indicator= (ImageView) itemView.findViewById(R.id.iv_indicator);
        }
    }

    public void bindView(final Calendar monthDay,
                         final Calendar month,
                         @Nullable List<Event> eventList,
                         boolean isSelected,
                         boolean isToday) {

        day = monthDay;

        if (monthDayDecorator != null) {
            monthDayDecorator.onBindDayView(itemView, monthDay, month, eventList, isSelected, isToday);
        } else {

            if (!displayDaysOutOfMonth) {
                if (!DateUtils.isSameMonth(monthDay, month)) {
                    itemView.setVisibility(View.GONE);
                    return;
                } else itemView.setVisibility(View.VISIBLE);
            }

            dayNumberView.setText(String.valueOf(monthDay.get(Calendar.DAY_OF_MONTH)));

            if (isSelected) {
                lly_root.setBackgroundResource(R.drawable.event_selected_background);
            } else if (isToday) {
                lly_root.setBackgroundResource(R.drawable.event_today_background);
            } else {
                lly_root.setBackgroundColor(Color.TRANSPARENT);
            }
            // 如果当天有事件，显示小圆点
            long totay=monthDay.getTimeInMillis();
//            if (GoogleCalendarActivity.isHasEventToday(totay)) {
//                iv_indicator.setVisibility(View.INVISIBLE);
//            } else {
//                iv_indicator.setVisibility(View.VISIBLE);
//            }
        }
    }
}

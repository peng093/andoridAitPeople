package com.example.atpeople.myapplication.ui.calendar;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.widget.LinearLayout;

import com.example.atpeople.myapplication.R;
import com.example.atpeople.myapplication.baseActivity.BaseActivity;
import com.example.atpeople.myapplication.ui.calendar.adapter.YearMonthAdapter;
import com.example.atpeople.myapplication.ui.calendar.model.EventModel;
import com.example.atpeople.myapplication.ui.calendar.utils.TimeUtils;
import com.example.atpeople.myapplication.util.TextBitmap;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.Nullable;
import butterknife.BindView;
import io.github.memfis19.cadar.CalendarController;
import io.github.memfis19.cadar.data.entity.Event;
import io.github.memfis19.cadar.event.CalendarPrepareCallback;
import io.github.memfis19.cadar.event.DisplayEventCallback;
import io.github.memfis19.cadar.event.OnDayChangeListener;
import io.github.memfis19.cadar.event.OnMonthChangeListener;
import io.github.memfis19.cadar.settings.MonthCalendarConfiguration;
import io.github.memfis19.cadar.view.MonthCalendar;


/**
 * Create by peng on 2020/3/3
 */
public class GoogleCalendarActivity extends BaseActivity {
    private static final String TAG = "GoogleCalendarActivity";
    @BindView(R.id.monthCalendar)
    MonthCalendar monthCalendar;
    @BindView(R.id.rv_list)
    RecyclerView rv_list;

    private static List<Event> events = new ArrayList<>();
    private static List<EventModel> event_list = new ArrayList<>();
    YearMonthAdapter yearMonthAdapter;

    List<Pair> pairList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int initLayout() {
        return R.layout.activity_month_list_interaction_layout;
    }

    @Override
    protected void initView() {
        // 获取当前年月日
        Calendar CD = Calendar.getInstance();
        int YY = CD.get(Calendar.YEAR);
        int MM = CD.get(Calendar.MONTH) + 1;
        int DD = CD.get(Calendar.DATE);
        setTitle(YY + "/" + MM + "/" + DD);
        setTopLeftButton(0, new OnClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });
        int width = TextBitmap.dip2px(60);
        Bitmap bitmap = TextBitmap.getRectTextBitmap("5", R.mipmap.ic_today, width, width, Color.BLACK);
        setTopRightButton2("我的", bitmap, new OnClickListener() {
            @Override
            public void onClick() {
                showToast("你点击了右上角的图标");
            }
        });
        setShowStatusBar(true);
        setStatusBarFontColor(this, Color.WHITE);
        // 年月列表初始化
        final LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv_list.setLayoutManager(linearLayoutManager);
        yearMonthAdapter = new YearMonthAdapter(new ArrayList<Pair>());
        rv_list.setAdapter(yearMonthAdapter);
        rv_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                // 滑动完成后执行
                if(RecyclerView.SCROLL_STATE_IDLE==newState){
                    LinearLayoutManager l = (LinearLayoutManager)recyclerView.getLayoutManager();
                    int nowPos = l.findFirstVisibleItemPosition();
                    Pair pair= yearMonthAdapter.getData().get(nowPos);
                    Log.e(TAG, "当前滚动的位置是"+nowPos+"===="+(int)pair.first+"-"+(int)pair.second);
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

            }
        });
    }

    @Override
    protected void initData() {
        events.clear();
        event_list.clear();
        Date date3 = new Date();
        for (int i = 0; i < 3; i++) {
            EventModel event = new EventModel();
            event.setId(System.currentTimeMillis());
            event.setTitle("#自定义的日历" + i);
            event.setDescription("呵呵呵");
            event.setStartDate(date3);
            event.setEndDate(date3);
            event.setSt_time(System.currentTimeMillis()/1000);
            events.add(event);
            event_list.add(event);
        }
        Date date4 = new Date(2020,2,6);
        for (int i = 0; i < 7; i++) {
            EventModel event = new EventModel();
            event.setId(System.currentTimeMillis());
            event.setTitle("#3月6号的测试" + i);
            event.setDescription("呵呵呵");
            event.setStartDate(date4);
            event.setEndDate(date4);
            event.setSt_time(i<4?1583510399:1583683199);
            events.add(event);
            event_list.add(event);
        }
        MonthCalendarConfiguration.Builder builder = new MonthCalendarConfiguration.Builder();
        // 监听日期初始化 ok回调
        monthCalendar.setCalendarPrepareCallback(new CalendarPrepareCallback() {
            @Override
            public void onCalendarReady(CalendarController calendar) {
                if (calendar == monthCalendar) {
                    monthCalendar.displayEvents(new ArrayList<>(events), new DisplayEventCallback<Calendar>() {
                        @Override
                        public void onEventsDisplayed(Calendar month) {

                        }
                    });
                }
            }
        });
        // 设置显示的周期--按月来显示日历
        builder.setDisplayPeriod(Calendar.MONTH, 3);
        // 创建 日历
        monthCalendar.prepareCalendar(builder.build());
        // 监听日期天数改变(点击天数)
        monthCalendar.setOnDayChangeListener(new OnDayChangeListener() {
            @Override
            public void onDayChanged(Calendar calendar) {
                //listCalendar.setSelectedDay(DateUtils.setTimeToMidnight((Calendar) calendar.clone()), false);
                int YY = calendar.get(Calendar.YEAR);
                int MM = calendar.get(Calendar.MONTH) + 1;
                int DD = calendar.get(Calendar.DATE);
                Log.e(TAG, "onDayChanged:" + YY + "/" + MM + "/" + DD);
                setTitle(YY + "/" + MM + "/" + DD);
                int position = getDatePosition(YY, MM);
                rv_list.scrollToPosition(position);
            }
        });
        // 监听日期月份改变（横向滑动）
        monthCalendar.setOnMonthChangeListener(new OnMonthChangeListener() {
            @Override
            public void onMonthChanged(Calendar calendar) {
                // listCalendar.setSelectedDay(DateUtils.setTimeToMonthStart((Calendar) calendar.clone()), false);
                int YY = calendar.get(Calendar.YEAR);
                int MM = calendar.get(Calendar.MONTH) + 1;
                setTitle(YY + "/" + MM);
                int position = getDatePosition(YY, MM);
                rv_list.scrollToPosition(position);
            }
        });
        // 列表赋值
        pairList = TimeUtils.getInitMonthMapWithZero(2000, 2090);
        yearMonthAdapter.setNewData(pairList);
    }

    /**
     * @Author Peng
     * @Date 2020/3/5 15:48
     * @Describe 根据数据年月匹配数据在列表中的位置
     */
    public int getDatePosition(int yy, int month) {
        int scrollPosition = 0;
        // 遍历所有的条目
        for (int i = 0; i < pairList.size(); ++i) {
            if ((int) pairList.get(i).first == yy && (int) pairList.get(i).second == month) {
                scrollPosition = i;
                break;
            }
        }
        return scrollPosition;
    }

    /**
     * @Author Peng
     * @Date 2020/3/5 15:48
     * @Describe 据数据年月匹配 对应的事件列表
     */
    public static List<Event> getEventListByTime(Calendar st_calendar, Calendar en_calendar) {
        long start_week_time = st_calendar.getTimeInMillis();
        long end_week_time = en_calendar.getTimeInMillis();
        List<Event> list = new ArrayList<>();
        for (EventModel event : event_list) {
            if (event.getSt_time()*1000 > start_week_time && event.getSt_time()*1000  < end_week_time) {
                list.add(event);
            }
        }
        return list;
    }
}

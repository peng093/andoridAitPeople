package com.example.atpeople.myapplication.ui.calendar;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;


import com.example.atpeople.myapplication.R;
import com.example.atpeople.myapplication.baseActivity.BaseActivity;
import com.example.atpeople.myapplication.ui.calendar.adapter.YearMonthAdapter;
import com.example.atpeople.myapplication.ui.calendar.model.AxonEventModel;
import com.example.atpeople.myapplication.ui.calendar.model.ListItemModel;
import com.example.atpeople.myapplication.ui.calendar.utils.TimeUtils;
import com.example.atpeople.myapplication.util.TextBitmap;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.comparators.EventComparator;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import androidx.annotation.Nullable;
import butterknife.BindView;


/**
 * Create by peng on 2020/3/3
 */
public class GoogleCalendarActivity extends BaseActivity {
    private static final String TAG = "GoogleCalendarActivity";
    @BindView(R.id.compactcalendar_view)
    CompactCalendarView compactCalendarView;
    @BindView(R.id.rv_list)
    RecyclerView rv_list;

    YearMonthAdapter yearMonthAdapter;

    List<Pair> pairList;
    List<Event>list;
    private boolean shouldShow = true;
    private Comparator<Event> eventsComparator = new EventComparator();
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
                if(!shouldShow){
                    shouldShow=true;
                    compactCalendarView.showCalendarWithAnimation();
                }
                // 清除所有事件
                compactCalendarView.removeAllEvents();
                Calendar cal=Calendar.getInstance();
                cal.add(Calendar.DATE, 0);
                Event ev = new Event(Color.parseColor("#0187be"), cal.getTimeInMillis(),new AxonEventModel(0,"#测试同一天"));
                list.add(ev);
                // 按日期排序
                Collections.sort(list, eventsComparator);
                compactCalendarView.addEvents(list);
                yearMonthAdapter.notifyDataSetChanged();
            }
        });
        setShowStatusBar(true);
        setStatusBarFontColor(this, Color.WHITE);
        // 年月列表初始化
        list=new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            Calendar calendar=Calendar.getInstance();
            calendar.add(Calendar.DATE, i);
            Event event = new Event(Color.parseColor("#0187be"), calendar.getTimeInMillis(),new AxonEventModel(i,"#测试自定义事件"));
            list.add(event);
        }
        final LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv_list.setLayoutManager(linearLayoutManager);
        yearMonthAdapter = new YearMonthAdapter(new ArrayList<Pair>(),list);
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
                // 滚动就隐藏日历--暂时改为手动点击标题隐藏
//                if (compactCalendarView.isAnimating()){return;}
//                if(shouldShow){
//                    compactCalendarView.hideCalendarWithAnimation();
//                    shouldShow=false;
//                }
            }
        });
    }

    @Override
    protected void initData() {
        TimeZone tz = TimeZone.getDefault();
        compactCalendarView.setLocale(tz,Locale.CHINESE);
        compactCalendarView.setUseThreeLetterAbbreviation(true);
        compactCalendarView.setFirstDayOfWeek(Calendar.MONDAY);
        for (Event event : list) {
            compactCalendarView.addEvent(event);
        }
        // 获取某一天的事件
        List<Event> events = compactCalendarView.getEvents(1583553423000L);
        // 监听日期选中
        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                // 选中就关闭日历
               if(shouldShow){
                   compactCalendarView.hideCalendarWithAnimation();;
                   shouldShow=false;
               }
                //List<Event> events = compactCalendarView.getEvents(dateClicked);
                Calendar calendar=Calendar.getInstance();
                calendar.setTimeInMillis(dateClicked.getTime());
                int YY = calendar.get(Calendar.YEAR);
                int MM = calendar.get(Calendar.MONTH) + 1;
                int DD = calendar.get(Calendar.DATE);
                setTitle(YY + "/" + MM + "/" + DD);

                int position = getDatePosition(YY, MM);
                rv_list.scrollToPosition(position);

                // 计算天所在列表的位置
                // int position_day=getPositionOnWeeks(YY,MM,DD);
            }

            @Override
            public void onMonthScroll(Date date) {
                Log.d(TAG, "Month was scrolled to: " + date);
                Calendar calendar=Calendar.getInstance();
                calendar.setTimeInMillis(date.getTime());
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
     * @Date 2020/3/6 9:04
     * @Describe 根据选中天数，匹配该天在周列表中的位置
     */
    private int getPositionOnWeeks(int YY ,int MM,int DD){
        int positon=0;
        List<ListItemModel> listItemModels=new ArrayList<>();
        TimeUtils.getFirstSundayOfMonth(YY,MM,listItemModels);
        for (int i = 0; i < listItemModels.size(); i++) {
            Pair pair=(Pair) listItemModels.get(i).getValue();
            Calendar cal_sta=(Calendar)pair.first;
            Calendar cal_end=(Calendar)pair.second;
            int sta_day= cal_sta.get(Calendar.DATE);
            int sta_end= cal_end.get(Calendar.DATE);
            if(sta_day<DD && DD<sta_end){
                positon=i;
                break;
            }
        }
        return positon;
    }


}

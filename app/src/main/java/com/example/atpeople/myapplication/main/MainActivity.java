package com.example.atpeople.myapplication.main;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.example.atpeople.myapplication.AppStart;
import com.example.atpeople.myapplication.R;
import com.example.atpeople.myapplication.atPeople.model.AtBean;
import com.example.atpeople.myapplication.util.BackgroundColorUtil;
import com.example.atpeople.myapplication.util.TipHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "MainActivity";
    private ViewGroup mListView;
    Drawable bg_color;
    @BindView(R.id.lly_root)
    ScrollView lly_root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setTheme(R.style.tapActive);
        mListView =findViewById(R.id.list);
        int radus=BackgroundColorUtil.dip2px(this,10);
        bg_color= BackgroundColorUtil.getRandomColorDrawable(radus,true,1);
        addDemo("UiActivity", UiActivity.class);
        addDemo("RxBingdingActivity", RxBingdingActivity.class);
        addDemo("NetworkRequestActivity", NetworkRequestActivity.class);
        addDemo("UseBaseActivity", UseBaseActivity.class);
    }

    private void addDemo(String demoName, Class<? extends Activity> activityClass) {
        Button b = new Button(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(0,BackgroundColorUtil.dip2px(this,10),0,0);
        b.setLayoutParams(layoutParams);
        b.setText(demoName);
        b.setTag(activityClass);
        b.setTextColor(Color.BLACK);
        b.setAllCaps(false);
        b.setOnClickListener(this);
        mListView.addView(b);
    }

    @Override
    public void onClick(View view) {
        List<AtBean> list1=new ArrayList<>();
        list1.add(new AtBean(1,"text",1,1));
        list1.add(new AtBean(2,"text2",2,2));
        List<AtBean> list2=new ArrayList<>();
        list2.add(new AtBean(2,"text2",2,2));
        list2.add(new AtBean(1,"text",1,1));
//        String str1=GsonUtil.toJson(list1);
//        String str2=GsonUtil.toJson(list2);
//        Log.e(TAG, "str1: "+str1);
//        Log.e(TAG, "str2: "+str2);
        Log.e(TAG, "对比元素: "+compareUser(list1,list2));
        List<Float> list=new ArrayList<>();
        list.add(0.9f);
        list.add(0.5f);
        list.add(5.5f);
        list.add(0.7f);
        Collections.sort(list);
        Log.e(TAG, "排序后: "+list); //[0.5, 0.7, 0.9, 5.5]
        // 振动主要是延时触发，及振动时长-就两个比较重要的
        TipHelper.Vibrate(this, new long[]{0,300,300}, false);
        Class activityClass = (Class) view.getTag();
        //startActivity(new Intent(this, activityClass));
        Intent intent = new Intent(this, activityClass);
        ActivityOptions options = ActivityOptions.makeScaleUpAnimation(lly_root, lly_root.getWidth()/2,
                lly_root.getHeight()/2,0 ,0 );
        startActivity(intent, options.toBundle());

    }

    public static void setClickRipple(View view){
        TypedValue typedValue = new TypedValue();
        AppStart.getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, typedValue, true);
        int[] attribute = new int[]{android.R.attr.selectableItemBackground};
        TypedArray typedArray = AppStart.getContext().getTheme().obtainStyledAttributes(typedValue.resourceId, attribute);
        view.setForeground(typedArray.getDrawable(0));
    }

    private boolean compareUser(List<AtBean>user1,List<AtBean>user2){
        for (int i = 0; i < user1.size(); i++) {
            for (int i1 = 0; i1 < user2.size(); i1++) {
                if (user1.get(i).getId()==user2.get(i1).getId()){
                    if(user1.get(i).getName()!=user2.get(i1).getName() || user1.get(i).getStartPos()!=user2.get(i1).getStartPos()){
                        return false;
                    }
                }
            }
        }
        return true;
    }
}

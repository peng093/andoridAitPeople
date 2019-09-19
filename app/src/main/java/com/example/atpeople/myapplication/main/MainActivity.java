package com.example.atpeople.myapplication.main;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.atpeople.myapplication.AppStart;
import com.example.atpeople.myapplication.R;
import com.example.atpeople.myapplication.atpeople.AitPeople;
import com.example.atpeople.myapplication.rxbinding.Login;
import com.example.atpeople.myapplication.ui.search.SearchActivity;
import com.example.atpeople.myapplication.ui.betterspinner.Spinner;
import com.example.atpeople.myapplication.ui.canvas.CanvasView;
import com.example.atpeople.myapplication.ui.cardrecognition.CardRecognition;
import com.example.atpeople.myapplication.ui.chart.MPAndroidChart;
import com.example.atpeople.myapplication.ui.chip.ChipView;
import com.example.atpeople.myapplication.ui.carview.Carview;
import com.example.atpeople.myapplication.ui.circularprogressview.CircularProgressView;
import com.example.atpeople.myapplication.ui.foldingmenu.FoldingMenu;
import com.example.atpeople.myapplication.ui.launchwithvideo.LaunchWithVideo;
import com.example.atpeople.myapplication.ui.notify.Notify;
import com.example.atpeople.myapplication.ui.slidinguppanel.SlidingUpPanel;
import com.example.atpeople.myapplication.ui.textanimation.TextAnimation;
import com.example.atpeople.myapplication.ui.userinfo.UserInfo;
import com.example.atpeople.myapplication.ui.webview.WebView;
import com.example.atpeople.myapplication.util.BackgroundColorUtil;
import com.yw.game.floatmenu.FloatItem;
import com.yw.game.floatmenu.FloatLogoMenu;
import com.yw.game.floatmenu.FloatMenuView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "MainActivity";
    private ViewGroup mListView;
    Drawable bg_color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTheme(R.style.tapActive);
        mListView =findViewById(R.id.list);
        int radus=BackgroundColorUtil.dip2px(this,10);
        bg_color= BackgroundColorUtil.getRandomColorDrawable(radus,true,1);
        addDemo("UiActivity", UiActivity.class);
        addDemo("RxBingdingActivity", RxBingdingActivity.class);
        addDemo("NetworkRequestActivity", NetworkRequestActivity.class);

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
        Class activityClass = (Class) view.getTag();
        startActivity(new Intent(this, activityClass));
    }

    public static void setClickRipple(View view){
        TypedValue typedValue = new TypedValue();
        AppStart.getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, typedValue, true);
        int[] attribute = new int[]{android.R.attr.selectableItemBackground};
        TypedArray typedArray = AppStart.getContext().getTheme().obtainStyledAttributes(typedValue.resourceId, attribute);
        view.setForeground(typedArray.getDrawable(0));
    }
}

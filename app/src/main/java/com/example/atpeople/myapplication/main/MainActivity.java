package com.example.atpeople.myapplication.main;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.atpeople.myapplication.R;
import com.example.atpeople.myapplication.atpeople.AitPeople;
import com.example.atpeople.myapplication.ui.camera.SmartsCamera;
import com.example.atpeople.myapplication.ui.canvas.CanvasView;
import com.example.atpeople.myapplication.ui.cardrecognition.CardRecognition;
import com.example.atpeople.myapplication.ui.chart.MPAndroidChart;
import com.example.atpeople.myapplication.ui.chip.ChipView;
import com.example.atpeople.myapplication.ui.carview.Carview;
import com.example.atpeople.myapplication.ui.circularprogressview.CircularProgressView;
import com.example.atpeople.myapplication.ui.foldingmenu.FoldingMenu;
import com.example.atpeople.myapplication.ui.notify.Notify;
import com.example.atpeople.myapplication.ui.slidinguppanel.SlidingUpPanel;
import com.example.atpeople.myapplication.ui.webview.WebView;
import com.example.atpeople.myapplication.util.BackgroundColorUtil;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "MainActivity";
    private ViewGroup mListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView =findViewById(R.id.list);
        addDemo("AitPeople", AitPeople.class);
        addDemo("Carview", Carview.class);
        addDemo("CircularProgressView", CircularProgressView.class);
        addDemo("FoldingMenu", FoldingMenu.class);
        addDemo("SlidingUpPanel", SlidingUpPanel.class);
        addDemo("ChipView", ChipView.class);
        addDemo("CanvasView", CanvasView.class);
        addDemo("CardRecognition", CardRecognition.class);
        addDemo("MPAndroidChart", MPAndroidChart.class);
        addDemo("Notify", Notify.class);
        addDemo("WebView", WebView.class);
    }

    private void addDemo(String demoName, Class<? extends Activity> activityClass) {
        Button b = new Button(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(0,BackgroundColorUtil.dip2px(this,10),0,0);
        b.setLayoutParams(layoutParams);
        b.setText(demoName);
        b.setTag(activityClass);
        b.setTextColor(Color.WHITE);
        int radus=BackgroundColorUtil.dip2px(this,10);
        b.setBackground(BackgroundColorUtil.getRandomColorDrawable(radus,true,1));
        b.setOnClickListener(this);
        mListView.addView(b);
    }

    @Override
    public void onClick(View view) {
        Class activityClass = (Class) view.getTag();
        startActivity(new Intent(this, activityClass));
    }
}

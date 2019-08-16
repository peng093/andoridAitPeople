package com.example.atpeople.myapplication.main;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.atpeople.myapplication.R;
import com.example.atpeople.myapplication.atpeople.AitPeople;
import com.example.atpeople.myapplication.rxbinding.Login;
import com.example.atpeople.myapplication.ui.betterspinner.Spinner;
import com.example.atpeople.myapplication.ui.canvas.CanvasView;
import com.example.atpeople.myapplication.ui.cardrecognition.CardRecognition;
import com.example.atpeople.myapplication.ui.carview.Carview;
import com.example.atpeople.myapplication.ui.chart.MPAndroidChart;
import com.example.atpeople.myapplication.ui.chip.ChipView;
import com.example.atpeople.myapplication.ui.circularprogressview.CircularProgressView;
import com.example.atpeople.myapplication.ui.foldingmenu.FoldingMenu;
import com.example.atpeople.myapplication.ui.launchwithvideo.LaunchWithVideo;
import com.example.atpeople.myapplication.ui.notify.Notify;
import com.example.atpeople.myapplication.ui.search.SearchActivity;
import com.example.atpeople.myapplication.ui.slidinguppanel.SlidingUpPanel;
import com.example.atpeople.myapplication.ui.textanimation.TextAnimation;
import com.example.atpeople.myapplication.ui.userinfo.UserInfo;
import com.example.atpeople.myapplication.ui.webview.WebView;
import com.example.atpeople.myapplication.util.BackgroundColorUtil;
import com.yw.game.floatmenu.FloatItem;
import com.yw.game.floatmenu.FloatLogoMenu;
import com.yw.game.floatmenu.FloatMenuView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class UiActivity extends AppCompatActivity implements View.OnClickListener{
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private ViewGroup mListView;
    /**悬浮窗*/
    FloatLogoMenu mFloatMenu;
    ArrayList<FloatItem> itemList = new ArrayList<>();
    private Activity mActivity;
    String HOME = "首页";
    String FEEDBACK = "客服";
    String MESSAGE = "消息";
    String[] MENU_ITEMS = {HOME, FEEDBACK, MESSAGE};
    private int[] menuIcons = new int[]{R.drawable.yw_menu_account, R.drawable.yw_menu_fb, R.drawable.yw_menu_msg};
    Drawable bg_color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mListView =findViewById(R.id.list);

        toolbar.setVisibility(View.VISIBLE);
        toolbar.setTitle("UiActivity");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int radus=BackgroundColorUtil.dip2px(this,10);
        bg_color= BackgroundColorUtil.getRandomColorDrawable(radus,true,1);

        addDemo("UserInfo", UserInfo.class);
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
        addDemo("Spinner", Spinner.class);
        addDemo("TextAnimation", TextAnimation.class);
        addDemo("LaunchWithVideo", LaunchWithVideo.class);
        addDemo("SearchActivity", SearchActivity.class);
        initFloatMenu();
    }

    private void addDemo(String demoName, Class<? extends Activity> activityClass) {
        Button b = new Button(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(0,BackgroundColorUtil.dip2px(this,10),0,0);
        b.setLayoutParams(layoutParams);
        b.setText(demoName);
        b.setTag(activityClass);
        b.setTextColor(Color.WHITE);
        b.setAllCaps(false);
        b.setBackground(bg_color);
        b.setOnClickListener(this);
        mListView.addView(b);
    }

    @Override
    public void onClick(View view) {
        Class activityClass = (Class) view.getTag();
        startActivity(new Intent(this, activityClass));
    }

    /**
     * @Author Peng
     * @Date 2019/8/16 14:08
     * @Describe 悬浮窗
     */
    private void initFloatMenu() {
        mActivity = this;
        for (int i = 0; i < menuIcons.length; i++) {
            itemList.add(new FloatItem(MENU_ITEMS[i], 0x99000000, 0x99000000, BitmapFactory.decodeResource(this.getResources(), menuIcons[i]), String.valueOf(i + 1)));
        }
        mFloatMenu = new FloatLogoMenu.Builder()
                .withActivity(mActivity)
                // 如果是要系统级的悬浮窗，需要动态申请悬浮于其他app上层的权限，否则无效果
                //.withContext(mActivity.getApplication())
                .logo(BitmapFactory.decodeResource(getResources(), R.drawable.yw_game_logo))
                .drawCicleMenuBg(true)
                .backMenuColor(0xffe4e3e1)
                .setBgDrawable(this.getResources().getDrawable(R.drawable.yw_game_float_menu_bg))
                //这个背景色需要和logo的背景色一致
                .setFloatItems(itemList)
                .defaultLocation(FloatLogoMenu.RIGHT)
                .drawRedPointNum(false)
                .showWithListener(new FloatMenuView.OnMenuClickListener() {
                    @Override
                    public void onItemClick(int position, String title) {
                        Toast.makeText(UiActivity.this, "position " + position + " title:" + title + " is clicked.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void dismiss() {

                    }
                });

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshDot();
            }
        }, 5000);
    }
    public void refreshDot() {
        for (FloatItem menuItem : itemList) {
            if (TextUtils.equals(menuItem.getTitle(), "我的")) {
                menuItem.dotNum = String.valueOf(8);
            }
        }
        mFloatMenu.setFloatItemList(itemList);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mFloatMenu.destoryFloat();
    }
}

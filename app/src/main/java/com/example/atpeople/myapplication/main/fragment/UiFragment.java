package com.example.atpeople.myapplication.main.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.example.atpeople.myapplication.R;
import com.example.atpeople.myapplication.atPeople.AitPeople;
import com.example.atpeople.myapplication.base.BaseFragment;
import com.example.atpeople.myapplication.ui.ImageSelectActivity;
import com.example.atpeople.myapplication.ui.ListDrag;
import com.example.atpeople.myapplication.ui.ScanQRCode;
import com.example.atpeople.myapplication.ui.SendSmsActivity;
import com.example.atpeople.myapplication.ui.annotation.AnnotationActivity;
import com.example.atpeople.myapplication.ui.betterSpinner.Spinner;
import com.example.atpeople.myapplication.ui.calendar.GoogleCalendarActivity;
import com.example.atpeople.myapplication.ui.canvas.CanvasView;
import com.example.atpeople.myapplication.ui.carView.Carview;
import com.example.atpeople.myapplication.ui.cardRecognition.CardRecognition;
import com.example.atpeople.myapplication.ui.chart.MPAndroidChart;
import com.example.atpeople.myapplication.ui.chip.ChipView;
import com.example.atpeople.myapplication.ui.circularProgressView.CircularProgressView;
import com.example.atpeople.myapplication.ui.dragLayout.YoutubeActivity;
import com.example.atpeople.myapplication.ui.foldingMenu.FoldingMenu;
import com.example.atpeople.myapplication.ui.gesturepsw.GesturePswActivity;
import com.example.atpeople.myapplication.ui.launchWithVideo.LaunchWithVideo;
import com.example.atpeople.myapplication.ui.mediaPlayBySeekbar.MediaPlayBySeekbar;
import com.example.atpeople.myapplication.ui.notify.Notify;
import com.example.atpeople.myapplication.ui.paymentCode.PaymentCode;
import com.example.atpeople.myapplication.ui.search.SearchActivity;
import com.example.atpeople.myapplication.ui.slidinGupPanel.SlidingUpPanel;
import com.example.atpeople.myapplication.ui.table.Table;
import com.example.atpeople.myapplication.ui.textAnimation.CombinationAnimation;
import com.example.atpeople.myapplication.ui.textAnimation.TextAnimation;
import com.example.atpeople.myapplication.ui.userInfo.UserInfo;
import com.example.atpeople.myapplication.ui.webView.MyWebView;
import com.example.atpeople.myapplication.util.BackgroundColorUtil;
import com.example.atpeople.myapplication.util.RemoveDuplicateUtil;
import com.example.atpeople.myapplication.util.TestA;
import com.example.atpeople.myapplication.util.TestB;
import com.yw.game.floatmenu.FloatItem;
import com.yw.game.floatmenu.FloatLogoMenu;
import com.yw.game.floatmenu.FloatMenuView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Create by peng on 2020/4/2
 */
public class UiFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.list)
    ViewGroup mListView;

    /**悬浮窗*/
    FloatLogoMenu mFloatMenu;
    ArrayList<FloatItem> itemList = new ArrayList<>();
    String HOME = "首页";
    String FEEDBACK = "客服";
    String MESSAGE = "消息";
    String[] MENU_ITEMS = {HOME, FEEDBACK, MESSAGE};
    private int[] menuIcons = new int[]{R.drawable.yw_menu_account, R.drawable.yw_menu_fb, R.drawable.yw_menu_msg};
    Drawable bg_color;

    @Override
    protected int initLayout() {
        return R.layout.fragment_main;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        int radus=BackgroundColorUtil.dip2px(getContext(),10);
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
        addDemo("MyWebView", MyWebView.class);
        addDemo("Spinner", Spinner.class);
        addDemo("TextAnimation", TextAnimation.class);
        addDemo("LaunchWithVideo", LaunchWithVideo.class);
        addDemo("SearchActivity", SearchActivity.class);
        addDemo("手势登录", GesturePswActivity.class);
        addDemo("Table(注解的简单使用)", Table.class);
        addDemo("YoutubeActivity", YoutubeActivity.class);
        addDemo("录音拖动播放", MediaPlayBySeekbar.class);
        addDemo("支付密码UI", PaymentCode.class);
        addDemo("组合动画", CombinationAnimation.class);
        addDemo("仿谷歌日历", GoogleCalendarActivity.class);
        addDemo("Rtsp", SendSmsActivity.class);
        addDemo("扫一扫", ScanQRCode.class);
        addDemo("图片选择器,压缩对比", ImageSelectActivity.class);
        addDemo("列表拖拽", ListDrag.class);
        addDemo("自定义注解绑定类、属性、方法", AnnotationActivity.class);
        initFloatMenu();
    }

    private void addDemo(String demoName, Class<? extends Activity> activityClass) {
        Button b = new Button(getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(0, BackgroundColorUtil.dip2px(getContext(),10),0,0);
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
        startActivity(new Intent(getContext(), activityClass));
    }

    /**
     * @Author Peng
     * @Date 2019/8/16 14:08
     * @Describe 悬浮窗
     */
    private void initFloatMenu() {
        for (int i = 0; i < menuIcons.length; i++) {
            itemList.add(new FloatItem(MENU_ITEMS[i], 0x99000000, 0x99000000, BitmapFactory.decodeResource(this.getResources(), menuIcons[i]), String.valueOf(i + 1)));
        }
        mFloatMenu = new FloatLogoMenu.Builder()
                .withActivity(getContext())
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
                        Toast.makeText(getActivity(), "position " + position + " title:" + title + " is clicked.", Toast.LENGTH_SHORT).show();
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
    public void onDestroy() {
        super.onDestroy();
        mFloatMenu.destoryFloat();
    }
}

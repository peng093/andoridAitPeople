package com.example.atpeople.myapplication.main;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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
import com.example.atpeople.myapplication.util.ToastUtil;
import com.huawei.hms.api.ConnectionResult;
import com.huawei.hms.api.HuaweiApiClient;
import com.huawei.hms.support.api.client.PendingResult;
import com.huawei.hms.support.api.client.ResultCallback;
import com.huawei.hms.support.api.push.HuaweiPush;
import com.huawei.hms.support.api.push.TokenResult;

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
    HuaweiApiClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setTheme(R.style.tapActive);
        mListView =findViewById(R.id.list);
        huaweiPushInit();

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

    private void huaweiPushInit() {
        client = new HuaweiApiClient.Builder(this)
                .addApi(HuaweiPush.PUSH_API)
                .addConnectionCallbacks(new HuaweiApiClient.ConnectionCallbacks() {
                    @Override public void onConnected() {
                        //华为移动服务client连接成功，在这边处理业务自己的事件
                        Log.i(TAG, "HuaweiApiClient 连接成功");
                        ToastUtil.makeText(getBaseContext(),"HuaweiApiClient 连接成功");
                        getTokenAsyn();
                    }
                    @Override public void onConnectionSuspended(int i) {
                        //HuaweiApiClient断开连接的时候，业务可以处理自己的事件
                        Log.i(TAG, "HuaweiApiClient 连接断开");
                        ToastUtil.makeText(getBaseContext(),"HuaweiApiClient 连接断开");
                        // client.connect();
                    }
                })
                .addOnConnectionFailedListener(new HuaweiApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult arg0) {
                        Log.i(TAG, "HuaweiApiClient连接失败，错误码：" + arg0.getErrorCode());
                        ToastUtil.makeText(getBaseContext(),"HuaweiApiClient连接失败，错误码：" + arg0.getErrorCode());
                    }

                })
                .build();
        client.connect(this);
    }
    private void getTokenAsyn() {
        getActivityUri();
        if (!client.isConnected()) {
            Log.e(TAG, "获取TOKEN失败，原因：HuaweiApiClient未连接");
            return;
        }
        PendingResult<TokenResult> tokenResult = HuaweiPush.HuaweiPushApi.getToken(client);
        tokenResult.setResultCallback(new ResultCallback<TokenResult>() {
            @Override
            public void onResult(TokenResult result) {
                Log.e(TAG,"异步回调接口result："+result.getTokenRes().getToken());
            }
        });
    }
    private void getActivityUri(){
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("wonderfullpush://com.wonderfull.android.push/notification?action=your parameter"));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        String intnetUri = intent.toUri(Intent.URI_INTENT_SCHEME);
        Log.d("hwpush", "intnetUri===" + intnetUri);
    }
}

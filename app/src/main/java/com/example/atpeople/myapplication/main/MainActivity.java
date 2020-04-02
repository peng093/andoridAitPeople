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
import com.example.atpeople.myapplication.base.BaseActivity;
import com.example.atpeople.myapplication.main.fragment.MBaseFragment;
import com.example.atpeople.myapplication.main.fragment.NetWorkFragment;
import com.example.atpeople.myapplication.main.fragment.RxJavaFragment;
import com.example.atpeople.myapplication.main.fragment.UiFragment;
import com.example.atpeople.myapplication.util.BackgroundColorUtil;
import com.example.atpeople.myapplication.util.TipHelper;
import com.example.atpeople.myapplication.util.ToastUtil;
import com.hjm.bottomtabbar.BottomTabBar;
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


public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
    @BindView(R.id.bottom_tab_bar)
    BottomTabBar mBottomTabBar;

    HuaweiApiClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        huaweiPushInit();
//        addDemo("UiActivity", UiActivity.class);
//        addDemo("RxBingdingActivity", RxBingdingActivity.class);
//        addDemo("NetworkRequestActivity", NetworkRequestActivity.class);
//        addDemo("UseBaseActivity", UseBaseActivity.class);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        setToolbarShow(false);
        mBottomTabBar.init(getSupportFragmentManager())
                .setImgSize(30,30)
                .setFontSize(10)
                .setTabPadding(4,6,10)
                .setChangeColor(Color.RED,Color.DKGRAY)
                .addTabItem("常用UI", R.mipmap.ic_common_tab_index_select, R.mipmap.ic_common_tab_index_unselect, UiFragment.class)
                .addTabItem("RxJava",R.mipmap.ic_common_tab_hot_select, R.mipmap.ic_common_tab_hot_unselect, RxJavaFragment.class)
                .addTabItem("网络请求",R.mipmap.ic_common_tab_publish_select, R.mipmap.ic_common_tab_publish_unselect, NetWorkFragment.class)
                .addTabItem("基类使用",R.mipmap.ic_common_tab_user_select, R.mipmap.ic_common_tab_user_unselect, MBaseFragment.class)
                .isShowDivider(false)
                .setOnTabChangeListener(new BottomTabBar.OnTabChangeListener() {
                    @Override
                    public void onTabChange(int position, String name, View view) {
                        if(position==3){
                            startActivity(new Intent(getBaseContext(), UseBaseActivity.class));
                        }
                    }
                });
    }

    @Override
    protected void initData() {

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

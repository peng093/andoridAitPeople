package com.example.atpeople.myapplication.main;

import android.content.Intent;

import android.net.Uri;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.example.atpeople.myapplication.R;
import com.example.atpeople.myapplication.base.BaseActivity;
import com.example.atpeople.myapplication.main.fragment.MBaseFragment;
import com.example.atpeople.myapplication.main.fragment.NetWorkFragment;
import com.example.atpeople.myapplication.main.fragment.RxJavaFragment;
import com.example.atpeople.myapplication.main.fragment.UiFragment;
import com.example.atpeople.myapplication.ui.paymentCode.PayFragment;
import com.example.atpeople.myapplication.util.ToastUtil;
import com.huawei.hms.api.ConnectionResult;
import com.huawei.hms.api.HuaweiApiClient;
import com.huawei.hms.support.api.client.PendingResult;
import com.huawei.hms.support.api.client.ResultCallback;
import com.huawei.hms.support.api.push.HuaweiPush;
import com.huawei.hms.support.api.push.TokenResult;
import com.kongzue.tabbar.Tab;
import com.kongzue.tabbar.TabBarView;
import com.kongzue.tabbar.interfaces.OnTabChangeListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
    @BindView(R.id.tabbar)
    TabBarView tabbar;
    @BindView(R.id.fl)
    FrameLayout fl;

    FragmentManager fm;
    List<Fragment> fragments=new ArrayList<>();

    HuaweiApiClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        huaweiPushInit();
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        fm = getSupportFragmentManager();
        setToolbarShow(false);
        List<Tab> tabs = new ArrayList<>();
        tabs.add(new Tab(this, "常用UI", R.mipmap.ic_common_tab_index_unselect).setMaxUnreadNum(99));
        tabs.add(new Tab(this, "RxJava", R.mipmap.ic_common_tab_hot_unselect).setMaxUnreadNum(99));
        tabs.add(new Tab(this, "网络请求", R.mipmap.ic_common_tab_publish_unselect).setMaxUnreadNum(99));
        tabs.add(new Tab(this, "基类使用", R.mipmap.ic_common_tab_user_unselect).setMaxUnreadNum(99));
        tabbar.setTab(tabs);
        tabbar.setOnTabChangeListener(new OnTabChangeListener() {
            @Override
            public void onTabChanged(View v, int index) {
                Log.i(">>>", "onTabChanged: " + index);
                if(index==3){
                    startActivity(new Intent(getBaseContext(), UseBaseActivity.class));
                }else {
                    setTabSelection(index);
                }
            }
        });
        // 默认选中第一个
        tabbar.setNormalFocusIndex(0);
        // 角标
        tabbar.setUnreadNum(0, 123);
        // fragmnet
        fragments.add(new UiFragment());
        fragments.add(new RxJavaFragment());
        fragments.add(new NetWorkFragment());
        fragments.add(new MBaseFragment());
        setTabSelection(0);
    }

    @Override
    protected void initData() {

    }

    private void setTabSelection(int index){
        FragmentTransaction ft = fm.beginTransaction();
        hideAllFragment(ft);
        if(!fragments.get(index).isAdded()){
            ft.add(R.id.fl, fragments.get(index));
            ft.show(fragments.get(index));
        }else{
            ft.show(fragments.get(index));
        }
        ft.commit();
    }


    private void hideAllFragment(FragmentTransaction ft){
        for (Fragment fragment : fragments) {
            if(fragment.isVisible()){
                ft.hide(fragment);
            }
        }
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
